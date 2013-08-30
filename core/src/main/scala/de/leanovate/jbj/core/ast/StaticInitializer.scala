/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.core.ast

import de.leanovate.jbj.core.runtime.context.{Context, StaticContext}
import de.leanovate.jbj.core.ast.stmt.{FunctionDeclStmt}
import de.leanovate.jbj.core.ast.stmt.decl.{ClassMethodDecl, ClassDeclStmt}

trait StaticInitializer extends HasNodePosition {
  self: Node =>
  def initializeStatic(staticCtx: StaticContext)(implicit ctx: Context)
}

object StaticInitializer {
  def collect(nodes: Node*) = {
    nodes.flatMap(_.visit[StaticInitializer](new NodeVisitor[StaticInitializer] {
      def apply(node: Node) = {
        node match {
          case classDecl: ClassDeclStmt => NextSibling()
          case funcDecl: FunctionDeclStmt => NextSibling()
          case methodDecl: ClassMethodDecl => NextSibling()
          case staticInitializer: StaticInitializer => NextChild(staticInitializer)
          case _ => NextChild()
        }
      }
    }).results)
  }
}