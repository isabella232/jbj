/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.core.ast.stmt.loop

import de.leanovate.jbj.core.ast._
import de.leanovate.jbj.runtime._
import scala.annotation.tailrec
import de.leanovate.jbj.runtime.value._
import de.leanovate.jbj.runtime.SuccessExecResult
import de.leanovate.jbj.runtime.context.Context
import de.leanovate.jbj.buildins.types.{PIteratorAggregate, PIterator}
import de.leanovate.jbj.runtime.BreakExecResult
import de.leanovate.jbj.runtime.ReturnExecResult
import de.leanovate.jbj.runtime.ContinueExecResult

case class ForeachStmt(valueExpr: Expr,
                       keyAssign: Option[ForeachAssignment],
                       valueAssign: ForeachAssignment,
                       stmts: List[Stmt]) extends Stmt with BlockLike {

  def exec(implicit ctx: Context) = {
    valueExpr.eval.concrete match {
      case array: ArrayVal =>
        array.iteratorReset()
        execValues(array, array.keyValues.toList)
      case obj: ObjectVal if obj.instanceOf(PIteratorAggregate) =>
        val iterator = PIteratorAggregate.cast(obj).getIterator()
        iterator.obj.retain()
        iterator.rewind()
        val result = execIterator(iterator)
        iterator.obj.release()
        result
      case obj: ObjectVal if obj.instanceOf(PIterator) =>
        val iterator = PIterator.cast(obj)
        iterator.rewind()
        execIterator(iterator)
      case _ =>
        ctx.log.warn("Invalid argument supplied for foreach()")
        SuccessExecResult
    }
  }

  @tailrec
  private def execValues(array: ArrayVal, remain: List[(PVal, PAny)])(implicit context: Context): ExecResult = {
    remain match {
      case head :: tail =>
        array.iteratorAdvance()
        keyAssign.foreach(_.assignKey(head._1))
        val value = if (valueAssign.hasValueRef) {
          head._2 match {
            case pVar: PVar =>
              pVar
            case pVal: PVal =>
              val pVar = PVar(pVal)
              array.setAt(head._1, pVar)
              pVar
          }
        } else {
          head._2
        }
        valueAssign.assignValue(value)
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
  }

  private def execIterator(iterator: PIterator)(implicit ctx: Context): ExecResult = {
    while (iterator.valid) {
      valueAssign.assignValue(iterator.current)
      keyAssign.foreach(_.assignKey(iterator.key))
      execStmts(stmts) match {
        case BreakExecResult(depth) if depth > 1 => return BreakExecResult(depth - 1)
        case BreakExecResult(_) => return SuccessExecResult
        case ContinueExecResult(depth) if depth > 1 => return ContinueExecResult(depth - 1)
        case result: ReturnExecResult => return result
        case _ =>
      }
      iterator.next()
    }
    SuccessExecResult
  }

  override def visit[R](visitor: NodeVisitor[R]) =
    visitor(this).thenChild(valueExpr).thenChild(keyAssign).thenChild(valueAssign).thenChildren(stmts)
}