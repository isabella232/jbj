package de.leanovate.jbj.ast.expr

import de.leanovate.jbj.ast.{Name, Expr}
import de.leanovate.jbj.runtime.Context
import de.leanovate.jbj.runtime.value.NullVal
import java.io.PrintStream

case class NewExpr(className: Name, parameters: List[Expr]) extends Expr {
  override def eval(implicit ctx: Context) = ctx.global.findClass(className.evalNamespaceName) match {
    case Some(pClass) =>
      pClass.newInstance(parameters)
    case None =>
      ctx.log.fatal(position, "Class '%s' not found".format(className.toString))
      NullVal
  }

  override def dump(out: PrintStream, ident: String) {
    super.dump(out, ident)
    out.println(ident + "  " + className.toString)
    parameters.foreach {
      parameter =>
        parameter.dump(out, ident + "  ")
    }
  }
}
