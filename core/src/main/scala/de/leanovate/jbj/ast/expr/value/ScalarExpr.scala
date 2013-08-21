package de.leanovate.jbj.ast.expr.value

import de.leanovate.jbj.ast.Expr
import java.io.PrintStream
import de.leanovate.jbj.runtime.value.PVal
import de.leanovate.jbj.runtime.buildin.OutputFunctions
import de.leanovate.jbj.runtime.context.Context

case class ScalarExpr(value: PVal) extends Expr {
  override def eval(implicit ctx: Context) = value

  override def dump(out: PrintStream, ident: String) {
    super.dump(out, ident)
    out.println(ident + "  " + value.toString)
  }
}
