package de.leanovate.jbj.ast

import de.leanovate.jbj.runtime.Context

trait StaticInitializer {
  def initializeStatic(ctx: Context)
}