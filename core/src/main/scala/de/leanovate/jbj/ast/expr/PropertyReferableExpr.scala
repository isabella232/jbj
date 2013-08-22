package de.leanovate.jbj.ast.expr

import de.leanovate.jbj.ast.{Name, ReferableExpr}
import de.leanovate.jbj.runtime.Reference
import de.leanovate.jbj.runtime.value._
import java.io.PrintStream
import de.leanovate.jbj.runtime.value.StringVal
import de.leanovate.jbj.runtime.context.{Context, MethodContext}
import de.leanovate.jbj.ast.expr.value.ScalarExpr
import de.leanovate.jbj.runtime.buildin.StdClass

case class PropertyReferableExpr(reference: ReferableExpr, propertyName: Name) extends ReferableExpr {
  override def isDefined(implicit ctx: Context) = {
    if (reference.isDefined) {
      reference.eval.asVal match {
        case obj: ObjectVal =>
          val name = propertyName.evalName
          if (obj.getProperty(name).isDefined) {
            true
          } else {
            obj.pClass.findMethod("__get").isDefined
          }
        case _ => false
      }
    } else {
      false
    }
  }

  override def eval(implicit ctx: Context) = {
    reference.eval.asVal match {
      case obj: ObjectVal =>
        val name = propertyName.evalName
        obj.getProperty(name).map(_.asVal).getOrElse {
          ctx match {
            case MethodContext(inst, methodName, _) if inst.pClass == obj.pClass && methodName == "__get" =>
              ctx.log.notice("Undefined property: %s::%s".format(obj.pClass.name.toString, name))
              NullVal
            case _ =>
              obj.pClass.findMethod("__get").map(_.invoke(ctx, obj, ScalarExpr(StringVal(name)) :: Nil)).map(_.asVal).getOrElse {
                ctx.log.notice("Undefined property: %s::%s".format(obj.pClass.name.toString, name))
                NullVal
              }
          }
        }
      case _ =>
        ctx.log.notice("Trying to get property of non-object")
        NullVal
    }
  }

  override def evalRef(implicit ctx: Context) = new Reference {
    val parentRef = reference.evalRef
    val name = propertyName.evalName

    def asVal = {
      parentRef.asVal match {
        case obj: ObjectVal =>
          val name = propertyName.evalName
          obj.getProperty(name).map(_.asVal).getOrElse {
            ctx match {
              case MethodContext(inst, methodName, _) if inst.pClass == obj.pClass && methodName == "__get" =>
                NullVal
              case _ =>
                obj.pClass.findMethod("__get").map(_.invoke(ctx, obj, ScalarExpr(StringVal(name)) :: Nil)).map(_.asVal).getOrElse {
                  NullVal
                }
            }
          }
        case _ =>
          NullVal
      }
    }

    def asVar = {
      optParent(false) match {
        case Some(obj) =>
          obj.getProperty(name).map {
            case pVar: PVar => pVar
            case value: PVal =>
              val result = PVar(value)
              obj.setProperty(name, None, result)
              result
          }.getOrElse {
            val result = PVar()
            obj.setProperty(name, None, result)
            result
          }
        case None =>
          ctx.log.notice("Trying to get property of non-object")
          PVar(NullVal)
      }
    }

    def assign(pAny: PAny) = {
      optParent(true) match {
        case Some(obj) =>
          if (obj.getProperty(name).isDefined) {
            obj.setProperty(propertyName.evalName, None, pAny.asVal)
          } else {
            ctx match {
              case MethodContext(inst, methodName, _) if inst.pClass == obj.pClass && methodName == "__set" =>
                obj.setProperty(name, None, pAny.asVal)
              case _ =>
                obj.pClass.findMethod("__set").map(_.invoke(ctx, obj, ScalarExpr(StringVal(name)) :: ScalarExpr(pAny.asVal) :: Nil)).getOrElse {
                  obj.setProperty(name, None, pAny.asVal)
                }
            }
          }
        case None =>
          ctx.log.warn("Attempt to assign property of non-object")
      }
      pAny
    }

    def unset() {
      if (optParent(false).isDefined)
        optParent(false).get.unsetProperty(name)
    }

    private def optParent(withWarn: Boolean) =
      parentRef.asVal match {
        case obj: ObjectVal =>
          Some(obj)
        case NullVal =>
          if (withWarn)
            ctx.log.warn("Creating default object from empty value")
          val obj = StdClass.newInstance(Nil)(ctx)
          parentRef.asVar.asVar.value = obj
          Some(obj)
        case _ =>
          None
      }
  }

  override def dump(out: PrintStream, ident: String) {
    super.dump(out, ident)
    reference.dump(out, ident + "  ")
    propertyName.dump(out, ident + "  ")
  }
}
