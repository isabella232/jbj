/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.runtime.value

import de.leanovate.jbj.runtime.context.{StaticMethodContext, MethodContext, Context}
import ObjectPropertyKey.{Key, IntKey, PublicKey, ProtectedKey, PrivateKey, PublicKeyFilter, PrivateKeyFilter}
import de.leanovate.jbj.runtime.types.{PIterator, PIteratorAggregate, PInterface, PClass}
import de.leanovate.jbj.runtime.exception.{CatchableFatalError, FatalErrorJbjException}
import de.leanovate.jbj.api.http.JbjException

trait ObjectVal extends PConcreteVal {
  def pClass: PClass

  def instanceNum: Long

  protected[value] def keyValueMap: ExtendedLinkedHashMap[Key]

  private val iteratorStateHolder = new Holder[IteratorState](keyValueMap.iteratorState(PublicKeyFilter))

  def refCount

  def keyValues(implicit ctx: Context): Seq[(Key, PAny)] = keyValueMap.toSeq

  override def toOutput(implicit ctx: Context) = pClass.findMethod("__toString").map {
    method =>
      try {
        method.invoke(this, Nil).asVal.concrete match {
          case StringVal(str) =>
            str
          case _ =>
            CatchableFatalError("Method %s::__toString() must return a string value".format(pClass.name.toString))
            ""
        }
      } catch {
        case e: JbjException =>
          throw new FatalErrorJbjException("Method %s::__toString() must not throw an exception".format(pClass.name.toString))
      }
  }.getOrElse {
    CatchableFatalError("Object of class %s could not be converted to string".format(pClass.name.toString))
    ""
  }

  override def toStr(implicit ctx: Context) = StringVal(toOutput)

  override def toNum = toInteger

  override def toDouble = DoubleVal(0.0)

  override def toInteger = IntegerVal(0)

  override def toBool = BooleanVal.TRUE

  override def toArray(implicit ctx: Context) = new ArrayVal(keyValueMap.mapDirect {
    case (IntKey(key), value) => key -> value
    case (ProtectedKey(key), value) => "\0*\0" + key -> value
    case (PrivateKey(key, className), value) => "\0" + className + "\0" + key -> value
    case (PublicKey(key), value) => key -> value
  })

  override def isNull = false

  override def copy = this

  override def incr = this

  override def decr = this

  override def typeName = "instance of %s".format(pClass.name.toString)

  override def compare(other: PVal)(implicit ctx: Context): Int = other match {
    case otherObj: ObjectVal =>
      if (pClass != otherObj.pClass)
        return Int.MinValue
      val keyIt = keyValueMap.keysIterator
      while (keyIt.hasNext) {
        val key = keyIt.next()
        val thisVal = keyValueMap(key)
        val otherVal = otherObj.keyValueMap.get(key)

        if (otherVal.isEmpty) {
          if (keyIt.hasNext)
            return Int.MinValue
          else
            return -1
        } else {
          val comp = thisVal.asVal.compare(otherVal.get.asVal)

          if (comp != null)
            return comp
        }
      }
      0
    case _ => 1
  }

  final def instanceOf(other: PInterface): Boolean = other.isAssignableFrom(pClass)

  final def instanceOf(other: PClass): Boolean = other.isAssignableFrom(pClass)

  def getProperty(name: String, className: Option[String])(implicit ctx: Context): Option[PAny] = {
    if (className.isDefined) {
      keyValueMap.get(PrivateKey(name, className.get)).map(Some.apply).getOrElse {
        keyValueMap.get(ProtectedKey(name)).map(Some.apply).getOrElse {
          keyValueMap.get(PublicKey(name))
        }
      }
    } else {
      keyValueMap.get(PublicKey(name))
    }
  }

  def definePrivateProperty(name: String, className: String, value: PAny)(implicit ctx: Context) {
    val key = PrivateKey(name, className)
    value.retain()
    keyValueMap.get(key).foreach(_.release())
    keyValueMap.put(key, value)
  }

  def defineProtectedProperty(name: String, value: PAny)(implicit ctx: Context) {
    val key = ProtectedKey(name)
    value.retain()
    keyValueMap.get(key).foreach(_.release())
    keyValueMap.put(key, value)
  }

  def definePublicProperty(name: String, value: PAny)(implicit ctx: Context) {
    if (keyValueMap.contains(ProtectedKey(name))) {
      keyValueMap.remove(ProtectedKey(name)).foreach(_.release())
    }
    val key = PublicKey(name)
    value.retain()
    keyValueMap.get(key).foreach(_.release())
    keyValueMap.put(key, value)
  }

  def setProperty(name: String, className: Option[String], value: PAny)(implicit ctx: Context) {
    if (className.isDefined) {
      val privateKey = PrivateKey(name, className.get)
      if (keyValueMap.contains(privateKey)) {
        value.retain()
        keyValueMap.get(privateKey).foreach(_.release())
        keyValueMap.put(privateKey, value)
      } else {
        val protectedKey = ProtectedKey(name)
        if (keyValueMap.contains(protectedKey)) {
          value.retain()
          keyValueMap.get(protectedKey).foreach(_.release())
          keyValueMap.put(protectedKey, value)
        } else {
          val key = PublicKey(name)
          value.retain()
          keyValueMap.get(key).foreach(_.release())
          keyValueMap.put(key, value)
        }
      }
    } else {
      val key = PublicKey(name)
      value.retain()
      keyValueMap.get(key).foreach(_.release())
      keyValueMap.put(key, value)
    }
  }

  def unsetProperty(name: String, className: Option[String])(implicit ctx: Context) = {
    keyValueMap.remove(PublicKey(name)).foreach(_.release())
    if (className.isDefined) {
      keyValueMap.remove(ProtectedKey(name)).foreach(_.release())
      keyValueMap.remove(PrivateKey(name, className.get)).foreach(_.release())
    }
  }

  def updateIteratorState(iteratorState: IteratorState): IteratorState = {
    if (iteratorState.isCompatible(keyValueMap))
      iteratorStateHolder.set(iteratorState)
    else
      iteratorStateHolder.clear()
    iteratorStateHolder.get()
  }

  def iteratorHasNext: Boolean = iteratorStateHolder.get().hasNext

  def iteratorCurrent(implicit ctx: Context): PVal = iteratorStateHolder.get().current

  def iteratorNext()(implicit ctx: Context): PVal = iteratorStateHolder.get().next

  def iteratorAdvance() {
    iteratorStateHolder.get().advance()
  }

  def iteratorReset()(implicit ctx: Context): IteratorState = {
    iteratorStateHolder.clear()
    ctx match {
      case MethodContext(_, method, _) if method.declaringClass.isAssignableFrom(pClass) =>
        iteratorStateHolder.set(keyValueMap.iteratorState(PrivateKeyFilter(method.declaringClass.name.toString)))
      case StaticMethodContext(method, _) if method.declaringClass.isAssignableFrom(pClass) || pClass.isAssignableFrom(method.declaringClass) =>
        iteratorStateHolder.set(keyValueMap.iteratorState(PrivateKeyFilter(method.declaringClass.name.toString)))
      case _ =>
    }
    iteratorStateHolder.get()
  }

  def cleanup()(implicit ctx: Context) {
    keyValueMap.values.foreach(_.release())
  }

  override def foreachByVal[R](f: (PVal, PAny) => Option[R])(implicit ctx: Context): Option[R] = {
    if (PIteratorAggregate.isAssignableFrom(pClass))
      PIteratorAggregate.cast(this).foreachByVal(f)
    else if (PIterator.isAssignableFrom(pClass))
      PIterator.cast(this).foreachByVal(f)
    else {
      var it = iteratorReset().copy(fixedEntries = false)
      var result = Option.empty[R]
      while (it.hasNext && result.isEmpty) {
        it = updateIteratorState(it.copy(fixedEntries = false))

        if (it.hasNext) {
          val key = it.currentKey
          val value = it.currentValue
          it.advance()
          result = f(key, value)
        }
      }
      result
    }
  }
}

object ObjectVal {
  def apply(pClass: PClass, keyValues: (Option[PVal], PVal)*)(implicit ctx: Context): ObjectVal = {
    var nextIndex: Long = -1

    val result = new StdObjectVal(pClass, ctx.global.instanceCounter.incrementAndGet,
      keyValues.foldLeft(new ExtendedLinkedHashMap[Key]) {
        (builder, keyValue) =>
          val key: Key = keyValue._1.map {
            case IntegerVal(value) =>
              if (value > nextIndex)
                nextIndex = value
              IntKey(value)
            case NumericVal(value) =>
              if (value > nextIndex)
                nextIndex = value.toLong
              IntKey(value.toLong)
            case value =>
              PublicKey(value.toStr.asString)
          }.getOrElse {
            nextIndex += 1
            IntKey(nextIndex)
          }

          builder += (key -> keyValue._2)
      })
    ctx.poolAutoRelease(result)
    result
  }

  def unapply(obj: ObjectVal)(implicit ctx: Context) = Some(obj.pClass, obj.instanceNum, obj.keyValues)
}