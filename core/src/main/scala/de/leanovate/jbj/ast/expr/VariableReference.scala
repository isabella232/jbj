package de.leanovate.jbj.ast.expr

import de.leanovate.jbj.ast.{Name, Reference}
import de.leanovate.jbj.runtime.Context
import java.io.PrintStream
import de.leanovate.jbj.runtime.value.{ValueOrRef, ValueRef, NullVal}

case class VariableReference(variableName: Name) extends Reference {
  override def eval(implicit ctx: Context) = ctx.findVariable(variableName.evalName).map(_.value).getOrElse(NullVal)

  override def evalRef(implicit ctx: Context) = ctx.findVariable(variableName.evalName)

  override def assignRef(valueOrRef: ValueOrRef)(implicit ctx: Context) {
    var name = variableName.evalName
    ctx.findVariable(name) match {
      case Some(valueRef) => valueRef.value = valueOrRef.value
      case None => ctx.defineVariable(name, ValueRef(valueOrRef.value))
      case _ =>
    }
  }

  override def dump(out: PrintStream, ident: String) {
    super.dump(out, ident)
    variableName.dump(out, ident + "  ")
  }
}
