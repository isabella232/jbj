package de.leanovate.jbj.runtime.buildin

import de.leanovate.jbj.runtime.{Value, PFunction, Context}
import de.leanovate.jbj.ast.{NamespaceName, NodePosition}

case class BuildinFunction1(_name: String, impl: PartialFunction[Option[Value], Value]) extends PFunction {
  def name = NamespaceName(_name)

  def call(ctx: Context, callerPosition: NodePosition, parameters: List[Value]) = parameters match {
    case param :: Nil => Left(impl.apply(Some(param)))
    case _ => Left(impl.apply(None))
  }
}
