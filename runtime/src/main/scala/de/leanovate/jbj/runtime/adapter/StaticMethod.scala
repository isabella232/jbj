/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.runtime.adapter

import de.leanovate.jbj.runtime.types.{PParam, PMethod, PParamDef, PClass}
import de.leanovate.jbj.runtime.value.ObjectVal
import de.leanovate.jbj.runtime.context.Context

abstract class StaticMethod(val declaringClass: PClass, val name: String,
                            val parameters: Seq[PParamDef] = Seq.empty,
                            val isFinal: Boolean = false) extends PMethod {
  def isAbstract = false

  def isStatic = true

  def isPrivate = false

  def isProtected = false

  def invoke(instance: ObjectVal, parameters: List[PParam])(implicit callerCtx: Context) = invokeStatic(parameters)
}
