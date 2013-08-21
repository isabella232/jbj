package de.leanovate.jbj.ast.expr.comp

import de.leanovate.jbj.ast.Expr
import de.leanovate.jbj.runtime.Context
import de.leanovate.jbj.runtime.value.BooleanVal
import de.leanovate.jbj.ast.expr.BinaryExpr

case class BoolXorExpr(left: Expr, right: Expr) extends BinaryExpr {
  override def eval(implicit ctx: Context) = {
    val leftVal = left.evalOld.toBool.asBoolean
    val rightVal = left.evalOld.toBool.asBoolean

    BooleanVal(leftVal ^ rightVal)
  }
}
