package de.leanovate.jbj

import de.leanovate.jbj.runtime.{PClass, PFunction}
import de.leanovate.jbj.runtime.value.PVal

trait JbjExtension {
  def name: String

  def constants: Seq[(String, PVal)] = Seq.empty

  def function: Seq[PFunction] = Seq.empty

  def classes: Seq[PClass] = Seq.empty
}