package de.leanovate.jbj.ast.stmt.loop

import de.leanovate.jbj.ast._
import de.leanovate.jbj.runtime._
import scala.annotation.tailrec
import de.leanovate.jbj.runtime.value.{PAny, PVal, ArrayVal}
import de.leanovate.jbj.runtime.SuccessExecResult
import de.leanovate.jbj.ast.stmt.BlockLike
import de.leanovate.jbj.runtime.context.Context
import de.leanovate.jbj.runtime.BreakExecResult
import de.leanovate.jbj.runtime.ReturnExecResult
import de.leanovate.jbj.runtime.ContinueExecResult

case class ForeachStmt(arrayExpr: Expr,
                       keyAssign: Option[ForeachAssignment],
                       valueAssign: ForeachAssignment,
                       stmts: List[Stmt]) extends Stmt with BlockLike {

  def exec(implicit ctx: Context) = {
    arrayExpr.eval.asVal match {
      case array: ArrayVal =>
        execValues(array, array.keyValues.toList)
      case _ =>
    }
    SuccessExecResult
  }

  @tailrec
  private def execValues(array: ArrayVal, remain: List[(PVal, PAny)])(implicit context: Context): ExecResult =
    remain match {
      case head :: tail =>
        keyAssign.foreach(_.assignKey(head._1))
        valueAssign.assignValue(head._2, head._1, array)
        execStmts(stmts) match {
          case BreakExecResult(depth) if depth > 1 => BreakExecResult(depth - 1)
          case BreakExecResult(_) => SuccessExecResult
          case ContinueExecResult(depth) if depth > 1 => ContinueExecResult(depth - 1)
          case ContinueExecResult(_) => execValues(array, tail)
          case result: ReturnExecResult => result
          case _ => execValues(array, tail)
        }
      case Nil => SuccessExecResult
    }

  override def visit[R](visitor: NodeVisitor[R]) =
    visitor(this).thenChild(arrayExpr).thenChild(keyAssign).thenChild(valueAssign).thenChildren(stmts)
}