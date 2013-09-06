/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.core.buildin

import de.leanovate.jbj.runtime.value._
import de.leanovate.jbj.runtime.annotations.{WanrExactly, GlobalFunction}
import de.leanovate.jbj.runtime.context.{FunctionLikeContext, Context}
import de.leanovate.jbj.runtime.exception.FatalErrorJbjException
import de.leanovate.jbj.core.ast.decl.TypeHint
import de.leanovate.jbj.core.ast.expr.ExprParam
import de.leanovate.jbj.runtime.PParam
import de.leanovate.jbj.core.ast.NamespaceName

object FunctionFunctions extends WrappedFunctions {
  @GlobalFunction
  def call_user_func(callable: PVal, parameters: PParam*)(implicit ctx: Context): PAny =
    callable match {
      case array: ArrayVal if array.keyValues.size != 2 =>
        ctx.log.warn("call_user_func() expects parameter 1 to be a valid callback, array must have exactly two members")
        NullVal
      case array: ArrayVal =>
        val objOrClassName = array.keyValues.head._2.asVal
        val methodName = array.keyValues.last._2.asVal.toStr.asString
        objOrClassName match {
          case obj: ObjectVal =>
            val (pClass, effectiveMethodName) = if (methodName.contains("::")) {
              val classAndMethod = methodName.split("::")
              ctx.global.findClass(NamespaceName(classAndMethod(0)), autoload = false).getOrElse {
                throw new FatalErrorJbjException("Class '%s' not found".format(classAndMethod(0)))
              } -> classAndMethod(1)
            } else {
              obj.pClass -> methodName
            }
            if (pClass.isCallable(ctx, Some(obj), effectiveMethodName)) {
              pClass.invokeMethod(ctx, Some(obj), effectiveMethodName, parameters.toList)
            } else {
              ctx.log.warn("call_user_func() expects parameter 1 to be a valid callback, class '%s' does not have a method '%s'".format(obj.pClass.name.toString, methodName))
              NullVal
            }
          case name =>
            ctx.global.findClass(NamespaceName(name.toStr.asString), autoload = false).map {
              pClass =>
                pClass.findMethod(methodName).map {
                  method =>
                    method.invokeStatic(ctx, parameters.toList)
                }.getOrElse {
                  ctx.log.warn("call_user_func() expects parameter 1 to be a valid callback, class '%s' does not have a method '%s'".format(pClass.name.toString, methodName))
                  NullVal
                }
            }.getOrElse {
              ctx.log.warn("call_user_func() expects parameter 1 to be a valid callback, first array member is not a valid class name or object")
              NullVal
            }
        }
      case name =>
        val functionName = name.toStr.asString
        if (functionName.contains("::")) {
          val classAndMethod = functionName.split("::")
          ctx.global.findClass(NamespaceName(classAndMethod(0)), autoload = true) match {
            case Some(pClass) =>
              pClass.findMethod(classAndMethod(1)).map {
                method =>
                  method.invokeStatic(ctx, parameters.toList)
              }.getOrElse {
                ctx.log.warn("call_user_func() expects parameter 1 to be a valid callback, class '%s' does not have a method '%s'".format(pClass.name.toString, classAndMethod(1)))
                NullVal
              }
            case None =>
              ctx.log.warn("call_user_func() expects parameter 1 to be a valid callback, class '%s' not found".format(classAndMethod(0)))
              NullVal
          }
        } else {
          ctx.findFunction(NamespaceName(functionName)).map {
            func =>
              func.call(parameters.toList)
          }.getOrElse {
            ctx.log.warn("call_user_func() expects parameter 1 to be a valid callback, function '%s' not found or invalid function name".format(functionName))
            NullVal
          }
        }
    }

  @GlobalFunction
  @WanrExactly
  def func_get_arg(value: PVal)(ctx: Context): PVal = {
    value match {
      case IntegerVal(argNum) =>
        ctx match {
          case funcCtx: FunctionLikeContext if argNum < 0 =>
            ctx.log.warn("func_get_arg():  The argument number should be >= 0")
            BooleanVal.FALSE
          case funcCtx: FunctionLikeContext if argNum >= funcCtx.functionArguments.size =>
            ctx.log.warn("func_get_arg():  Argument %d not passed to function".format(argNum))
            BooleanVal.FALSE
          case funcCtx: FunctionLikeContext =>
            funcCtx.functionArguments(argNum.toInt).asVal
          case _ =>
            ctx.log.warn("func_get_arg():  Called from the global scope - no function context")
            BooleanVal.FALSE
        }
      case _ =>
        ctx.log.warn("func_get_arg() expects parameter 1 to be long, %s given".format(TypeHint.displayType(value)))
        BooleanVal.FALSE
    }
  }

  @GlobalFunction
  def func_get_args()(implicit ctx: Context): PVal = {
    ctx match {
      case funcCtx: FunctionLikeContext =>
        ArrayVal(funcCtx.functionArguments.map(arg => None -> arg.asVal): _*)
      case _ =>
        ctx.log.warn("func_get_args():  Called from the global scope - no function context")
        BooleanVal.FALSE
    }
  }

  @GlobalFunction
  def func_num_args()(implicit ctx: Context): Int = {
    ctx match {
      case funcCtx: FunctionLikeContext =>
        funcCtx.functionArguments.size
      case _ =>
        ctx.log.warn("func_num_args():  Called from the global scope - no function context")
        -1
    }
  }
}