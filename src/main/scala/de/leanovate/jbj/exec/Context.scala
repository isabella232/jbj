package de.leanovate.jbj.exec

import java.io.PrintStream
import de.leanovate.jbj.ast.{Value, Function}
import de.leanovate.jbj.ast.value.ValueRef

trait Context {
  def out: PrintStream

  def findVariable(name: String): Option[ValueRef]

  def defineVariable(name: String, static: Boolean, value: Value)

  def findFunction(name: String): Option[Function]
}
