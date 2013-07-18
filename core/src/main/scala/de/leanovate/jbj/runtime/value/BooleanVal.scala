package de.leanovate.jbj.ast.value

import java.io.PrintStream
import de.leanovate.jbj.runtime.Value

case class BooleanVal(value: Boolean) extends Value {
  def toOutput(out: PrintStream) {
    if (value) out.print("1")
  }

  def toStr = StringVal(if (value) "1" else "")

  def toNum = IntegerVal(if (value) 1 else 0)

  def toBool = this

  def isNull = false

  def isUndefined = false

  def copy = this

  def incr = this

  def decr = this
}