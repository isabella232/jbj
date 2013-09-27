/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.core.ast

import de.leanovate.jbj.runtime.{NoNodePosition, NodePosition, JbjScript, ExecResult}
import de.leanovate.jbj.runtime.context.Context

case class Prog(fileName: String, stmts: Seq[Stmt]) extends Stmt with BlockLike with JbjScript {
  private lazy val staticInitializers = StaticInitializer.collect(this)

  private lazy val deprecatedNodes = visit(new Prog.DeprectatedNodeVisitor).results

  override def exec(implicit ctx: Context): ExecResult = {
    ctx.global.resetCurrentNamepsace()
    staticInitializers.foreach(_.initializeStatic(ctx.static))
    ctx.static.initialized = true

    deprecatedNodes.foreach {
      case (n, pos) =>
        ctx.log.deprecated(pos, n.deprecated.get)
    }

    registerDecls
    ctx.global.resetCurrentNamepsace()
    execStmts(stmts.toList)
  }

  override def visit[R](visitor: NodeVisitor[R]) = visitor(this).thenChildren(stmts)
}

object Prog {
  class DeprectatedNodeVisitor extends NodeVisitor[(Node, NodePosition)] {
    var pos: NodePosition = NoNodePosition

    def apply(node: Node) = node match {
      case n: Node with HasNodePosition if n.deprecated.isDefined =>
        pos = n.position
        NextChild((n, pos))
      case n if n.deprecated.isDefined => NextChild((n, pos))
      case n: Node with HasNodePosition =>
        pos = n.position
        NextChild()
      case _ => NextChild()
    }
  }
}