package de.leanovate.jbj.ast.stmt

import de.leanovate.jbj.runtime.{Value, Context}
import de.leanovate.jbj.runtime.value.NullVal

case class ParameterDecl(typeHint:Option[TypeHint], variableName: String, byRef: Boolean, default: Option[Value]) {
  def defaultVal(ctx: Context): Value = default.getOrElse(NullVal)
}
