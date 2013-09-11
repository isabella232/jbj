/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.runtime.context

import java.io.PrintStream
import scala.collection.immutable.Stack
import de.leanovate.jbj.runtime.value.{PAny, PVar}
import de.leanovate.jbj.runtime._
import de.leanovate.jbj.api.JbjSettings
import scala.collection.mutable
import de.leanovate.jbj.runtime.output.OutputBuffer
import de.leanovate.jbj.runtime.types.PFunction

trait Context {
  private val _autoReleasePool = mutable.ListBuffer.empty[PAny]
  private var _currentPosition: NodePosition = NoNodePosition

  def name: String

  def global: GlobalContext

  def static: StaticContext

  def settings: JbjSettings

  def out: OutputBuffer

  def err: Option[PrintStream]

  def currentPosition: NodePosition = _currentPosition

  def currentPosition_=(pos: NodePosition) {
    _currentPosition = pos
  }

  lazy val log: Log = new Log(this, out, err)

  def stack: Stack[NodePosition]

  def findFunction(name: NamespaceName): Option[PFunction]

  def defineFunction(function: PFunction)

  def findOrDefineVariable(name: String): PVar = {
    val optVar = findVariable(name)
    if (optVar.isDefined)
      optVar.get
    else {
      val pVar = PVar()
      defineVariable(name, pVar)
      pVar
    }
  }

  def findVariable(name: String): Option[PVar]

  def defineVariable(name: String, variable: PVar)

  def undefineVariable(name: String)

  def poolAutoRelease(pAny: PAny) {
    pAny.retain()
    _autoReleasePool += pAny
  }

  def autoRelease() {
    _autoReleasePool.foreach(_.release()(this))
    _autoReleasePool.clear()
  }

  def cleanup()
}
