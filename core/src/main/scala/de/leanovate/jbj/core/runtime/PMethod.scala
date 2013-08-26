package de.leanovate.jbj.core.runtime

import de.leanovate.jbj.core.runtime.value.{PAny, ObjectVal}
import de.leanovate.jbj.core.ast.{MemberModifier, Expr}
import de.leanovate.jbj.core.runtime.context.Context

trait PMethod {
  def name: String

  def modifieres: Set[MemberModifier.Type]

  lazy val isPrivate = modifieres.contains(MemberModifier.PRIVATE)

  lazy val isProtected = modifieres.contains(MemberModifier.PROTECTED)

  lazy val isStatic = modifieres.contains(MemberModifier.STATIC)

  def invoke(ctx: Context, instance: ObjectVal, pClass: PClass, parameters: List[Expr]): PAny

  def invokeStatic(ctx: Context, pClass: PClass, parameters: List[Expr]): PAny

  def checkRules(pClass: PClass)(implicit ctx: Context) {}
}
