package de.leanovate.jbj.runtime.value

import de.leanovate.jbj.runtime._
import scala.collection.mutable
import de.leanovate.jbj.ast.NodePosition

class ArrayVal(keyValueMap: mutable.LinkedHashMap[Any, PAny]) extends PVal with ArrayLike {

  private var maxIndex: Long = (0L :: keyValueMap.keys.map {
    case idx: Long => idx
    case _ => 0L
  }.toList).max

  def keyValues(implicit ctx: Context): Seq[(PVal, PAny)] = keyValueMap.toSeq.map {
    case (key: Long, value) => IntegerVal(key) -> value
    case (key: String, value) => StringVal(key) -> value
  }

  override def toOutput(implicit ctx: Context) = "Array"

  override def toStr(implicit ctx: Context) = StringVal("Array".getBytes(ctx.settings.charset))

  override def toNum(implicit ctx: Context) = toInteger

  override def toDouble(implicit ctx: Context) = DoubleVal(0.0)

  override def toInteger(implicit ctx: Context) = IntegerVal(keyValueMap.size)

  override def toBool(implicit ctx: Context) = BooleanVal.FALSE

  override def toArray(implicit ctx: Context) = this

  override def isNull = false

  def isEmpty = keyValueMap.isEmpty

  override def copy = new ArrayVal(keyValueMap.clone())

  override def incr = this

  override def decr = this

  override def size: Int = keyValueMap.size

  override def getAt(index: Long)(implicit ctx: Context, position: NodePosition): Option[PAny] =
    keyValueMap.get(index)

  override def getAt(index: String)(implicit ctx: Context, position: NodePosition): Option[PAny] =
    keyValueMap.get(index)

  override def setAt(index: Long, value: PAny)(implicit ctx: Context, position: NodePosition) {
    if (index > maxIndex) {
      maxIndex = index
    }
    keyValueMap.get(index).foreach(_.decrRefCount())
    keyValueMap.put(index, value)
    value.incrRefCount()
  }

  override def setAt(index: String, value: PAny)(implicit ctx: Context, position: NodePosition) {
    keyValueMap.get(index).foreach(_.decrRefCount())
    keyValueMap.put(index, value)
    value.incrRefCount()
  }

  override def append(value: PAny)(implicit ctx: Context, position: NodePosition) {
    keyValueMap.get(maxIndex).foreach(_.decrRefCount())
    keyValueMap.put(maxIndex, value)
    value.incrRefCount()
    maxIndex += 1
  }

  override def unsetAt(index: Long)(implicit ctx: Context, position: NodePosition) {
    keyValueMap.remove(index).foreach(_.decrRefCount())
  }

  override def unsetAt(index: String)(implicit ctx: Context, position: NodePosition) {
    keyValueMap.remove(index).foreach(_.decrRefCount())
  }

  def count: IntegerVal = IntegerVal(keyValueMap.size)
}

object ArrayVal {
  def apply(): ArrayVal = new ArrayVal(mutable.LinkedHashMap.empty[Any, PAny])

  def apply(keyValues: (Option[PVal], PAny)*)(implicit ctx: Context): ArrayVal = {
    var nextIndex: Long = -1

    new ArrayVal(keyValues.foldLeft(mutable.LinkedHashMap.newBuilder[Any, PAny]) {
      (builder, keyValue) =>
        val key = keyValue._1.map {
          case IntegerVal(value) =>
            if (value > nextIndex)
              nextIndex = value
            value
          case NumericVal(value) =>
            if (value > nextIndex)
              nextIndex = value.toLong
            value.toLong
          case value =>
            value.toStr.asString
        }.getOrElse {
          nextIndex += 1
          nextIndex
        }

        keyValue._2.incrRefCount()
        builder += key -> keyValue._2
    }.result())
  }

  def unapply(array: ArrayVal)(implicit ctx: Context) = Some(array.keyValues)
}