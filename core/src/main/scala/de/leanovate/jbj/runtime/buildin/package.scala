package de.leanovate.jbj.runtime

import de.leanovate.jbj.runtime.value._
import de.leanovate.jbj.ast.{ClassEntry, NamespaceName, NodePosition}
import scala.collection.Map
import scala.collection.mutable
import de.leanovate.jbj.runtime.value.IntegerVal

package object buildin {
  val buildinFunctions: Map[Seq[String], PFunction] = (
    ArrayFunctions.functions ++
      ClassFunctions.functions ++
      FileFunctions.functions ++
      StringFunctions.functions ++
      OutputFunctions.functions ++
      RuntimeFunctions.functions).map {
    function => function.name.lowercase -> function
  }.toMap

  val buildinConstants: Map[String, Value] = Seq(
    "TRUE" -> BooleanVal.TRUE,
    "FALSE" -> BooleanVal.FALSE,
    "NULL" -> NullVal,
    "E_ERROR" -> IntegerVal(Settings.E_ERROR),
    "E_WARNING" -> IntegerVal(Settings.E_WARNING),
    "E_PARSE" -> IntegerVal(Settings.E_PARSE),
    "E_NOTICE" -> IntegerVal(Settings.E_NOTICE),
    "E_CORE_ERROR" -> IntegerVal(Settings.E_CORE_ERROR),
    "E_CORE_WARNING" -> IntegerVal(Settings.E_CORE_WARNING),
    "E_COMPILE_ERROR" -> IntegerVal(Settings.E_COMPILE_ERROR),
    "E_COMPILE_WARNING" -> IntegerVal(Settings.E_COMPILE_WARNING),
    "E_USER_ERROR" -> IntegerVal(Settings.E_USER_ERROR),
    "E_USER_WARNING" -> IntegerVal(Settings.E_USER_WARNING),
    "E_USER_NOTICE" -> IntegerVal(Settings.E_USER_NOTICE),
    "E_STRICT" -> IntegerVal(Settings.E_STRICT),
    "E_RECOVERABLE_ERROR" -> IntegerVal(Settings.E_RECOVERABLE_ERROR),
    "E_DEPRECATED" -> IntegerVal(Settings.E_DEPRECATED),
    "E_USER_DEPRECATED" -> IntegerVal(Settings.E_USER_DEPRECATED),
    "E_ALL" -> IntegerVal(Settings.E_ALL)
  ).toMap


  val stdClass = new PClass {
    override def classEntry = ClassEntry.CLASS

    override def name = NamespaceName(relative = false, "stdClass")

    override def superClass = None

    override def newInstance(ctx: Context, callerPosition: NodePosition, parameters: List[Value]) =
      new ObjectVal(this, instanceCounter.incrementAndGet(), mutable.LinkedHashMap.empty[ArrayKey, Value])

    override def methods = Map.empty
  }

  val Exception = new PClass {
    override def classEntry = ClassEntry.CLASS

    override def name = NamespaceName(relative = false, "Exception")

    override def superClass = None

    override def newInstance(ctx: Context, callerPosition: NodePosition, parameters: List[Value]) = {
      val (message: Value, code: Value, previous: Value) = parameters match {
        case Nil => (StringVal(""), IntegerVal(0), NullVal)
        case msg :: Nil => (msg.toStr, IntegerVal(0), NullVal)
        case msg :: c :: Nil => (msg.toStr, c.toInteger, NullVal)
        case msg :: c :: prev => (msg.toStr, c.toInteger, prev)
      }
      ObjectVal(this,
        Some(StringVal("message")) -> message,
        Some(StringVal("code")) -> code,
        Some(StringVal("previous")) -> previous,
        Some(StringVal("file")) -> StringVal(callerPosition.fileName),
        Some(StringVal("line")) -> IntegerVal(callerPosition.line))
    }

    override def methods = Map.empty
  }

  val buildinClasses: Map[Seq[String], PClass] = Seq(
    stdClass, Exception
  ).map {
    c =>
      c.name.lowercase -> c
  }.toMap
}
