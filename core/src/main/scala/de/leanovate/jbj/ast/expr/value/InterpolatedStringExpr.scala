package de.leanovate.jbj.ast.expr.value

import de.leanovate.jbj.ast.Expr
import de.leanovate.jbj.runtime.Context
import de.leanovate.jbj.runtime.value.StringVal
import de.leanovate.jbj.parser.JbjParser

case class InterpolatedStringExpr(format: String, interpolations: List[Expr]) extends Expr {
  def eval(ctx: Context) = {
    val values = interpolations.map(_.eval(ctx).toStr.value)
    StringVal(format.format(values: _*))
  }
}

object InterpolatedStringExpr {
  def apply(charOrInterpolations: List[Either[Char, String]]): Expr = {
    val str = charOrInterpolations.foldLeft(StringBuilder.newBuilder) {
      case (b, Left(ch)) =>
        b += ch
      case (b, Right(_)) =>
        b ++= "%s"
    }.result()
    val interpolations = charOrInterpolations.filter(_.isRight).map {
      s => JbjParser.parseExpr(s.right.get)
    }

    if (interpolations.isEmpty)
      StringConstExpr(str)
    else
      InterpolatedStringExpr(str, interpolations)
  }
}