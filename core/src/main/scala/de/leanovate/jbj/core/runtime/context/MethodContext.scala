package de.leanovate.jbj.core.runtime.context

import de.leanovate.jbj.core.ast.{NodePosition, NamespaceName}
import de.leanovate.jbj.core.runtime._
import scala.collection.mutable
import scala.collection.immutable.Stack
import de.leanovate.jbj.core.runtime.value.{PVar, PVal, ObjectVal}

case class MethodContext(instance: ObjectVal, pClass: PClass, methodName: String, callerContext: Context)
  extends FunctionLikeContext {
  private val localVariables = mutable.Map.empty[String, PVar]

  private val identifier = "Method_" + instance.pClass.name.toString + "::" + methodName

  def name = pClass.name.toString

  lazy val global = callerContext.global

  lazy val static = global.staticContext(identifier)

  lazy val settings = global.settings

  val out = callerContext.out

  val err = callerContext.err

  lazy val stack: Stack[NodePosition] = callerContext.stack.push(callerContext.currentPosition)

  defineVariable("GLOBALS", PVar(global.GLOBALS))
  defineVariable("this", PVar(instance))

  def findConstant(name: String): Option[PVal] = global.findConstant(name)

  def defineConstant(name: String, value: PVal, caseInsensitive: Boolean) {
    global.defineConstant(name, value, caseInsensitive)
  }

  override def findVariable(name: String): Option[PVar] = localVariables.get(name)

  override def defineVariable(name: String, variable: PVar) {
    variable.retain()
    localVariables.get(name).foreach(_.release()(this))
    localVariables.put(name, variable)
  }

  override def undefineVariable(name: String) {
    localVariables.remove(name).foreach(_.release()(this))
  }

  def findFunction(name: NamespaceName) = callerContext.findFunction(name)

  def defineFunction(function: PFunction) {
    callerContext.defineFunction(function)
  }

  def cleanup() {
    localVariables.values.foreach(_.release()(this))
  }
}

