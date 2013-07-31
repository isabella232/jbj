package de.leanovate.jbj.ast.expr.calc

import de.leanovate.jbj.ast.Expr
import de.leanovate.jbj.runtime.Context
import de.leanovate.jbj.runtime.value.{FloatVal, NumericVal, IntegerVal}

case class MulExpr(left: Expr, right: Expr) extends Expr {
  override def eval(ctx: Context) = left.eval(ctx).toNum * right.eval(ctx).toNum
}
