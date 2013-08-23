package de.leanovate.jbj.runtime.buildin

import scala.collection.Map
import de.leanovate.jbj.runtime.value.{ObjectPropertyKey, PAny, ObjectVal}
import scala.collection.mutable
import de.leanovate.jbj.ast.Expr
import de.leanovate.jbj.ast.NamespaceName
import de.leanovate.jbj.ast.ClassEntry
import de.leanovate.jbj.runtime.PClass
import de.leanovate.jbj.runtime.context.Context
import sun.org.mozilla.javascript.internal.ast.ObjectProperty

object StdClass extends PClass {
  override def classEntry = ClassEntry.CLASS

  override def name = NamespaceName(relative = false, "stdClass")

  override def superClass = None

  override def newInstance(parameters: List[Expr])(implicit ctx: Context) =
    new ObjectVal(this, ctx.global.instanceCounter.incrementAndGet(), mutable.LinkedHashMap.empty[ObjectPropertyKey.Key, PAny])

  override def methods = Map.empty
}
