package de.leanovate.jbj.ast.expr.calc

import de.leanovate.jbj.ast.Expr
import de.leanovate.jbj.runtime.Context
import de.leanovate.jbj.ast.expr.BinaryExpr

case class ModExpr(left: Expr, right: Expr) extends BinaryExpr {
  override def eval(ctx: Context) = left.eval(ctx).toInteger % right.eval(ctx).toInteger
}
