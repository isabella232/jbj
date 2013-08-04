package de.leanovate.jbj.runtime.exception

import de.leanovate.jbj.runtime.Context
import de.leanovate.jbj.ast.NodePosition

class FatalErrorJbjException(message: String)(implicit ctx: Context, position: NodePosition) extends JbjException(message) {
  ctx.log.fatal(position, message)
}
