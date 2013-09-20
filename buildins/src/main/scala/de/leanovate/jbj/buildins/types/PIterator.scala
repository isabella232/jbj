/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.buildins.types

import de.leanovate.jbj.runtime.value.{PAny, PVal, ObjectVal}
import de.leanovate.jbj.runtime.types.{PInterfaceMethod, PInterface}
import de.leanovate.jbj.runtime.NamespaceName
import de.leanovate.jbj.runtime.context.Context
import de.leanovate.jbj.runtime.adapter.BooleanConverter

trait PIterator {
  def obj: ObjectVal

  def rewind()(implicit ctx: Context)

  def valid(implicit ctx: Context): Boolean

  def current(implicit ctx: Context): PAny

  def next()(implicit ctx: Context)

  def key(implicit ctx: Context): PVal
}

object PIterator extends PInterface {
  override def name = NamespaceName(relative = false, "Iterator")

  override def interfaces = List(PTraversable)

  override def declaredConstants = Map.empty

  override def methods = Seq(
    PInterfaceMethod(this, "rewind"),
    PInterfaceMethod(this, "valid"),
    PInterfaceMethod(this, "current"),
    PInterfaceMethod(this, "next"),
    PInterfaceMethod(this, "key")
  ).map {
    method => method.name.toLowerCase -> method
  }.toMap

  def cast(_obj: ObjectVal): PIterator = new PIterator {
    def obj = _obj

    def rewind()(implicit ctx: Context) {
      _obj.pClass.invokeMethod(Some(_obj), "rewind", Nil)
    }

    def valid(implicit ctx: Context) = {
      _obj.pClass.invokeMethod(Some(_obj), "valid", Nil).asVal.toBool.asBoolean
    }

    def current(implicit ctx: Context) =
      _obj.pClass.invokeMethod(Some(_obj), "current", Nil)

    def next()(implicit ctx: Context) {
      _obj.pClass.invokeMethod(Some(_obj), "next", Nil)
    }

    def key(implicit ctx: Context) =
      _obj.pClass.invokeMethod(Some(_obj), "key", Nil).asVal
  }
}