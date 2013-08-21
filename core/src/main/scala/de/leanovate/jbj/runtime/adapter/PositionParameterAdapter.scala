package de.leanovate.jbj.runtime.adapter

import de.leanovate.jbj.ast.{Expr, NodePosition}
import de.leanovate.jbj.runtime.context.Context

object PositionParameterAdapter extends ParameterAdapter[NodePosition] {
  override def requiredCount = 0

  override def adapt(parameters: List[Expr])(implicit ctx: Context) =
    Some(ctx.currentPosition, parameters)
}
