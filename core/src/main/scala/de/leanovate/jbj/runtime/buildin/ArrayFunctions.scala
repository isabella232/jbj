package de.leanovate.jbj.runtime.buildin

import de.leanovate.jbj.runtime.value.{ArrayVal, UndefinedVal}
import de.leanovate.jbj.runtime._
import de.leanovate.jbj.ast.NodePosition
import scala.collection.mutable
import de.leanovate.jbj.runtime.IntArrayKey
import de.leanovate.jbj.ast.NamespaceName
import scala.Some
import de.leanovate.jbj.runtime.value.IntegerVal

object ArrayFunctions {
  val functions = Seq(
    BuildinFunction1("count", {
      case Some(array: ArrayVal) => array.count
      case Some(_) => IntegerVal(1)
    }),
    new PFunction() {
      def name = NamespaceName("array_merge")

      def call(ctx: Context, callerPosition: NodePosition, parameters: List[Value]) =
        parameters match {
          case params if !params.isEmpty =>
            var count: Long = -1
            var builder = mutable.LinkedHashMap.newBuilder[ArrayKey, Value]
            params.foreach {
              case array: ArrayVal =>
                array.keyValues.map {
                  case (IntArrayKey(_), value) =>
                    count += 1
                    builder += IntArrayKey(count) -> value
                  case (key, value) =>
                    builder += key -> value
                }
            }
            Left(new ArrayVal(builder.result()))
          case _ =>
            ctx.log.warn(callerPosition, "array_merge() expects at least 1 parameter, 0 given")
            Left(UndefinedVal)
        }
    })
}