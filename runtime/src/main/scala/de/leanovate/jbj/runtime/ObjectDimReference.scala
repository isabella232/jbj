/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.runtime

import de.leanovate.jbj.runtime.value._
import de.leanovate.jbj.runtime.context.Context
import de.leanovate.jbj.runtime.types.PArrayAccess
import de.leanovate.jbj.runtime.exception.FatalErrorJbjException

class ObjectDimReference(arrayAccess: PArrayAccess, optArrayKey: Option[PVal])(implicit ctx: Context) extends Reference {
  def isConstant = false

  override def isDefined = {
    if (optArrayKey.isEmpty)
      false
    else
      arrayAccess.offsetExists(optArrayKey.get)
  }

  override def byVal = {
    if (optArrayKey.isEmpty)
      throw new FatalErrorJbjException("Cannot use [] for reading")
    else
      arrayAccess.offsetGet(optArrayKey.get).asVal
  }

  override def byVar = {
    PVar()
  }

  override def assign(pAny: PAny)(implicit ctx: Context) = {
    optArrayKey match {
      case Some(arrayKey) =>
        arrayAccess.offsetSet(arrayKey, pAny.asVal)
      case None =>
        arrayAccess.offsetSet(NullVal, pAny.asVal)
    }
    pAny
  }

  override def unset() {
    if (optArrayKey.isDefined) {
      arrayAccess.offsetUnset(optArrayKey.get)
    }
  }

  override def checkIndirect = {
    ctx.log.notice("Indirect modification of overloaded element of %s has no effect".format(arrayAccess.obj.pClass.name.toString))
    false
  }

  override def dim(optKey: Option[PVal] = None)(implicit ctx: Context) = byVal.concrete match {
    case obj: ObjectVal if obj.instanceOf(PArrayAccess) =>
      new ObjectDimReference(PArrayAccess.cast(obj), optKey)
    case _ =>
      new ArrayDimReference(this, optKey)
  }
}