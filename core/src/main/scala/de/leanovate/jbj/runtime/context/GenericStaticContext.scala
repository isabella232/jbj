package de.leanovate.jbj.runtime.context

import scala.collection.mutable
import de.leanovate.jbj.runtime._
import de.leanovate.jbj.ast.{Prog, NodePosition, NamespaceName}
import scala.collection.immutable.Stack
import de.leanovate.jbj.runtime.value.{PVar, PVal}

class GenericStaticContext(var global: GlobalContext) extends Context with StaticContext {
  private val variables = mutable.Map.empty[String, PVar]

  def static = this

  lazy val settings = global.settings

  def out = global.out

  def err = global.err

  def stack: Stack[NodePosition] = Stack.empty[NodePosition]

  variables.put("GLOBALS", PVar(global.GLOBALS))

  def findConstant(name: String): Option[PVal] = global.findConstant(name)

  def defineConstant(name: String, value: PVal, caseInsensitive: Boolean) {
    global.defineConstant(name, value, caseInsensitive)
  }

  def findVariable(name: String) = variables.get(name)

  def defineVariable(name: String, valueRef: PVar) {
    variables.get(name).foreach(_.cleanup())
    variables.put(name, valueRef)
  }

  def undefineVariable(name: String) {
    variables.remove(name).foreach(_.cleanup())
  }

  def findFunction(name: NamespaceName) = global.findFunction(name)

  def defineFunction(function: PFunction) {
    global.defineFunction(function)
  }
}