/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.core.runtime

import de.leanovate.jbj.core.ast.{Expr, ClassEntry, NamespaceName}
import de.leanovate.jbj.core.runtime.value._
import de.leanovate.jbj.core.runtime.exception.FatalErrorJbjException
import scala.annotation.tailrec
import de.leanovate.jbj.core.runtime.context._
import de.leanovate.jbj.core.ast.expr.value.ScalarExpr
import de.leanovate.jbj.core.runtime.context.MethodContext
import scala.Some

trait PClass {
  def classEntry: ClassEntry.Type

  def isAbstract = classEntry == ClassEntry.ABSTRACT_CLASS

  def name: NamespaceName

  def superClass: Option[PClass]

  def interfaces: Seq[PInterface]

  def classConstants: Map[String, ConstVal]

  def initializeStatic(staticContext: StaticContext)(implicit ctx: Context)

  def newEmptyInstance(pClass: PClass)(implicit ctx: Context): ObjectVal = ObjectVal(pClass)

  def initializeInstance(instance: ObjectVal)(implicit ctx: Context) {}

  def newInstance(parameters: List[Expr])(implicit ctx: Context): ObjectVal

  def destructInstance(instance: ObjectVal)(implicit ctx: Context)

  def invokeMethod(ctx: Context, optInstance: Option[ObjectVal], methodName: String,
                   parameters: List[Expr]) = {
    findMethod(methodName) match {
      case Some(method) =>
        optInstance.map {
          instance =>
            method.invoke(ctx, instance, parameters)
        }.getOrElse {
          method.invokeStatic(ctx, parameters)
        }
      case None if optInstance.isDefined && (!ctx.isInstanceOf[MethodContext] ||
        ctx.asInstanceOf[MethodContext].pMethod.name != "__call" ||
        ctx.asInstanceOf[MethodContext].instance.pClass != this) =>
        optInstance.get.pClass.findMethod("__call") match {
          case Some(method) =>
            val parameterArray = ArrayVal(parameters.map {
              expr =>
                None -> expr.eval(ctx).asVal.copy
            }: _*)(ctx)
            method.invoke(ctx, optInstance.get,
              ScalarExpr(StringVal(methodName)(ctx)) :: ScalarExpr(parameterArray) :: Nil)
          case None =>
            throw new FatalErrorJbjException("Call to undefined method %s::%s()".format(name.toString, methodName))(ctx)

        }
      case None =>
        throw new FatalErrorJbjException("Call to undefined method %s::%s()".format(name.toString, methodName))(ctx)
    }
  }

  def methods: Map[String, PMethod]

  def findMethod(methodName: String): Option[PMethod] = methods.get(methodName.toLowerCase)

  @tailrec
  final def isAssignableFrom(other: PClass): Boolean = this == other || (other.superClass match {
    case None => false
    case Some(s) => isAssignableFrom(s)
  })
}