/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.core.ast.expr

import de.leanovate.jbj.core.ast.{Expr, Name}
import de.leanovate.jbj.core.runtime.value.PAny
import de.leanovate.jbj.core.runtime.exception.FatalErrorJbjException
import de.leanovate.jbj.core.runtime.context.{MethodContext, Context}

case class CallStaticMethodReferableExpr(className: Name, methodName: Name, parameters: List[Expr])
  extends CallReferableExpr {
  def call(implicit ctx: Context): PAny = {
    val name = className.evalNamespaceName
    ctx.global.findClass(name).map {
      pClass =>
        ctx match {
          case MethodContext(instance, currentMethod, _) if pClass.isAssignableFrom(currentMethod.declaringClass)=>
            pClass.invokeMethod(ctx, Some(instance), methodName.evalName, parameters)
          case _ =>
            pClass.invokeMethod(ctx, None, methodName.evalName, parameters)
        }
    }.getOrElse {
      throw new FatalErrorJbjException("Class '%s' not found".format(name.toString))
    }
  }
}