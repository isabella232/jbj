package de.leanovate.jbj.ast.stmt.loop

import de.leanovate.jbj.ast.{Stmt, Expr}
import de.leanovate.jbj.runtime._
import scala.annotation.tailrec
import de.leanovate.jbj.runtime.BreakExecResult
import de.leanovate.jbj.runtime.SuccessExecResult
import de.leanovate.jbj.ast.FilePosition

case class DoWhileStmt(position: FilePosition, stmts: List[Stmt], condition: Expr) extends Stmt {
  def exec(ctx: Context): ExecResult = {
    do {
      execStmts(stmts, ctx) match {
        case BreakExecResult(depth) if depth > 1 => BreakExecResult(depth - 1)
        case BreakExecResult(_) => return SuccessExecResult()
        case result: ReturnExecResult => return result
        case _ =>
      }
    } while (condition.eval(ctx).toBool.value)
    SuccessExecResult()
  }

  @tailrec
  private def execStmts(statements: List[Stmt], context: Context): ExecResult = statements match {
    case head :: tail => head.exec(context) match {
      case SuccessExecResult() => execStmts(tail, context)
      case result => result
    }
    case Nil => SuccessExecResult()
  }
}
