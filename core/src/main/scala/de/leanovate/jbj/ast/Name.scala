package de.leanovate.jbj.ast

import de.leanovate.jbj.runtime.Context

trait Name {
  def evalName(ctx: Context): String
}