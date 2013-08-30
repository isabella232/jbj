/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.core.ast.stmt.cond

import de.leanovate.jbj.core.ast._
import de.leanovate.jbj.core.runtime.SuccessExecResult
import de.leanovate.jbj.core.runtime.context.{Context, StaticContext}
import de.leanovate.jbj.core.ast.stmt.BlockLike
import de.leanovate.jbj.core.runtime.BreakExecResult

case class SwitchStmt(expr: Expr, cases: List[SwitchCase]) extends Stmt with StaticInitializer with BlockLike {
  private val staticInitializers =
    cases.map(_.stmts.filter(_.isInstanceOf[StaticInitializer]).map(_.asInstanceOf[StaticInitializer])).flatten

  override def exec(implicit ctx: Context) = {
    val value = expr.eval.asVal

    execStmts(cases.dropWhile(!_.matches(value)).map(_.stmts).flatten) match {
      case BreakExecResult(depth) if depth > 1 => BreakExecResult(depth - 1)
      case BreakExecResult(_) => SuccessExecResult
      case result => result
    }
  }

  override def initializeStatic(staticCtx: StaticContext)(implicit ctx: Context) {
    staticInitializers.foreach(_.initializeStatic(staticCtx))
  }

  override def visit[R](visitor: NodeVisitor[R]) = visitor(this).thenChildren(cases)
}