/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.core.ast.stmt

import de.leanovate.jbj.core.ast.{Expr, Stmt}
import de.leanovate.jbj.core.runtime.buildin
import de.leanovate.jbj.core.runtime.value.ObjectVal
import de.leanovate.jbj.core.runtime.exception.{FatalErrorJbjException, RuntimeJbjException}
import de.leanovate.jbj.core.runtime.context.Context

case class ThrowStmt(expr: Expr) extends Stmt {
  override def exec(implicit ctx: Context) = expr.eval.asVal match {
    case obj: ObjectVal if obj.instanceOf(buildin.Exception) =>
      throw new RuntimeJbjException(obj)
    case obj: ObjectVal =>
      throw new FatalErrorJbjException("Exceptions must be valid objects derived from the Exception base class")
    case _ =>
      throw new FatalErrorJbjException("Can only throw objects")
  }
}