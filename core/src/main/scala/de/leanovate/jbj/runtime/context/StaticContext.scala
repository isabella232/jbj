package de.leanovate.jbj.runtime.context

import de.leanovate.jbj.runtime.value.PVar

trait StaticContext {
  var initialized = false

  def findVariable(name: String): Option[PVar]

  def defineVariable(name: String, pVar: PVar)

  def undefineVariable(name: String)
}
