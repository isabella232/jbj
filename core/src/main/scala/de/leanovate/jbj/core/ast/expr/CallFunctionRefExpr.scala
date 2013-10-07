/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.core.ast.expr

import de.leanovate.jbj.core.ast.{Name, Expr}
import de.leanovate.jbj.runtime.value.PAny
import de.leanovate.jbj.runtime.context.Context

case class CallFunctionRefExpr(functionName: Name, parameters: List[Expr]) extends CallRefExpr {
  def call(implicit ctx: Context): PAny = {
    val name = functionName.evalNamespaceName
    ctx.call(name, parameters.map(ExprParam.apply))
  }
}
