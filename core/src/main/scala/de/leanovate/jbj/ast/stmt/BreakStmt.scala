package de.leanovate.jbj.ast.stmt

import de.leanovate.jbj.ast.{Expr, Stmt}
import de.leanovate.jbj.runtime.{BreakExecResult, Context}

case class BreakStmt(depth: Option[Expr]) extends Stmt {
  def exec(ctx: Context) = BreakExecResult(depth.map(_.eval(ctx).toInteger.value).getOrElse(0))
}
