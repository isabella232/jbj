package de.leanovate.jbj.core.ast.expr.calc

import de.leanovate.jbj.core.ast.Expr
import de.leanovate.jbj.core.ast.expr.BinaryExpr
import de.leanovate.jbj.core.runtime.context.Context

case class BitAndExpr(left: Expr, right: Expr) extends BinaryExpr {
  override def eval(implicit ctx: Context) = left.eval & right.eval
}