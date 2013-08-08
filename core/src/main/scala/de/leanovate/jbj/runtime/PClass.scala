package de.leanovate.jbj.runtime

import de.leanovate.jbj.ast.{ClassEntry, NamespaceName, NodePosition}
import de.leanovate.jbj.runtime.value.ObjectVal
import java.util.concurrent.atomic.AtomicLong

trait PClass {
  val instanceCounter = new AtomicLong(0)

  def classEntry: ClassEntry.Type

  def name: NamespaceName

  def superClass:Option[PClass]

  def newInstance(ctx: Context, callerPosition: NodePosition, parameters: List[Value]): Value

  def invokeMethod(ctx: Context, callerPosition: NodePosition, instance: ObjectVal, methodName: String, parameters: List[Value]): Either[Value, ValueRef]

  def methods:Seq[String]

  def findMethod(methodName: String): Option[PMethod]
}