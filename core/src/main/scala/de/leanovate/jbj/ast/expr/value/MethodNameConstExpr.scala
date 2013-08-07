package de.leanovate.jbj.ast.expr.value

import de.leanovate.jbj.runtime.Context
import de.leanovate.jbj.runtime.context.MethodContext
import de.leanovate.jbj.runtime.value.StringVal
import de.leanovate.jbj.ast.Expr

case class MethodNameConstExpr() extends Expr {
  override def eval(implicit ctx: Context) = ctx match {
    case MethodContext(inst, name, _, _) => StringVal(inst.pClass.name.toString + "::" + name)
    case _ => StringVal("")
  }

}