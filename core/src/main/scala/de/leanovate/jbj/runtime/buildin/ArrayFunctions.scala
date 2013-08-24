package de.leanovate.jbj.runtime.buildin

import de.leanovate.jbj.runtime.value._
import scala.collection.mutable
import de.leanovate.jbj.runtime.annotations.GlobalFunction
import de.leanovate.jbj.runtime.context.Context

object ArrayFunctions extends WrappedFunctions {
  @GlobalFunction
  def count(value: PVal): Int = value match {
    case array: ArrayVal => array.size
    case _ => 1
  }

  @GlobalFunction
  def array_merge(values: PVal*)(implicit ctx: Context): PVal = {
    if (values.isEmpty) {
      ctx.log.warn("array_merge() expects at least 1 parameter, 0 given")
      NullVal
    } else {
      var count: Long = -1
      var builder = mutable.LinkedHashMap.newBuilder[Any, PAny]
      values.foreach {
        case array: ArrayVal =>
          array.keyValues.map {
            case (IntegerVal(_), value) =>
              count += 1
              builder += count -> value
            case (StringVal(key), value) =>
              builder += key -> value
          }
        case _ =>
      }
      new ArrayVal(builder.result())
    }
  }

  @GlobalFunction
  def array_shift(ref: PVar)(implicit ctx: Context): PVal = {
    ref.value match {
      case array: ArrayVal =>
        var count: Long = -1
        val keyValues = array.keyValues
        var builder = mutable.LinkedHashMap.newBuilder[Any, PAny]
        if (!keyValues.isEmpty) {
          keyValues.tail.foreach {
            case (IntegerVal(_), value) =>
              count += 1
              builder += count -> value
            case (StringVal(key), value) =>
              builder += key -> value
          }
        }
        ref.value = new ArrayVal(builder.result())
        keyValues.headOption.map(_._2.asVal).getOrElse(NullVal)
      case v =>
        ctx.log.warn("array_shift() expects parameter 1 to be array, %s given".format(v.typeName))
        NullVal
    }
  }

  @GlobalFunction
  def each(value: PVal)(implicit ctx: Context): PVal = value match {
    case array: ArrayVal => array.iteratorNext
    case obj: ObjectVal => obj.iteratorNext
    case _ =>
      ctx.log.warn("Variable passed to each() is not an array or object")
      NullVal
  }

  @GlobalFunction
  def reset(value: PVal)(implicit ctx: Context) {
    value match {
      case array: ArrayVal => array.iteratorReset()
      case obj: ObjectVal => obj.iteratorReset()
      case _ =>
        ctx.log.warn("Variable passed to each() is not an array or object")
        NullVal
    }
  }

  @GlobalFunction
  def implode(glueOrPieces: PVal, optPieces: Option[PVal])(implicit ctx: Context): PVal = {
    def mkstring(delim: StringVal, array: ArrayVal): StringVal = {
      val builder = Array.newBuilder[Byte]
      var first = true

      for (x <- array.keyValues) {
        if (first) {
          builder ++= x._2.asVal.toStr.chars
          first = false
        }
        else {
          builder ++= delim.chars
          builder ++= x._2.asVal.toStr.chars
        }
      }

      new StringVal(builder.result())
    }

    optPieces match {
      case Some(array: ArrayVal) =>
        mkstring(glueOrPieces.toStr, array)
      case Some(_) =>
        ctx.log.warn("implode(): Invalid arguments passed")
        NullVal
      case None =>
        glueOrPieces match {
          case array: ArrayVal =>
            mkstring(StringVal(""), array)
          case _ =>
            ctx.log.warn("implode(): Argument must be an array")
            NullVal
        }
    }
  }
}
