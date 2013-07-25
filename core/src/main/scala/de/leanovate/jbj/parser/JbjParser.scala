package de.leanovate.jbj.parser

import de.leanovate.jbj.ast._
import de.leanovate.jbj.ast.stmt._
import de.leanovate.jbj.ast.stmt.cond._
import de.leanovate.jbj.ast.stmt.cond.SwitchStmt
import de.leanovate.jbj.ast.stmt.loop.{ForeachKeyValueStmt, ForeachValueStmt, ForStmt, WhileStmt}
import scala.collection.mutable
import scala.util.parsing.combinator.Parsers
import de.leanovate.jbj.ast.expr.value._
import scala.language.implicitConversions
import de.leanovate.jbj.ast.expr._
import de.leanovate.jbj.ast.stmt.GlobalAssignStmt
import de.leanovate.jbj.ast.stmt.cond.DefaultCaseBlock
import de.leanovate.jbj.ast.expr.calc.AddExpr
import de.leanovate.jbj.ast.expr.comp.BoolAndExpr
import de.leanovate.jbj.ast.stmt.cond.IfStmt
import de.leanovate.jbj.parser.JbjTokens.NumericLit
import de.leanovate.jbj.ast.stmt.ReturnStmt
import de.leanovate.jbj.ast.expr.calc.SubExpr
import scala.Some
import de.leanovate.jbj.parser.JbjTokens.Inline
import de.leanovate.jbj.ast.expr.comp.LtExpr
import de.leanovate.jbj.ast.expr.comp.GeExpr
import de.leanovate.jbj.ast.Prog
import de.leanovate.jbj.ast.stmt.ExprStmt
import de.leanovate.jbj.ast.stmt.cond.ElseIfBlock
import de.leanovate.jbj.ast.expr.NegExpr
import de.leanovate.jbj.ast.stmt.ParameterDef
import de.leanovate.jbj.ast.expr.DotExpr
import de.leanovate.jbj.parser.JbjTokens.ScriptEnd
import de.leanovate.jbj.parser.JbjTokens.ScriptStart
import de.leanovate.jbj.ast.expr.VarGetAndDecrExpr
import de.leanovate.jbj.ast.expr.calc.MulExpr
import de.leanovate.jbj.ast.expr.value.IntegerConstExpr
import de.leanovate.jbj.ast.stmt.StaticAssignStmt
import de.leanovate.jbj.parser.JbjTokens.InterpolatedStringLit
import de.leanovate.jbj.ast.stmt.AssignStmt
import de.leanovate.jbj.ast.stmt.FunctionDefStmt
import de.leanovate.jbj.ast.stmt.Assignment
import de.leanovate.jbj.parser.JbjTokens.Identifier
import de.leanovate.jbj.ast.expr.comp.LeExpr
import de.leanovate.jbj.ast.stmt.cond.CaseBlock
import de.leanovate.jbj.ast.expr.VarGetExpr
import de.leanovate.jbj.runtime.context.GlobalContext
import de.leanovate.jbj.ast.expr.comp.EqExpr
import de.leanovate.jbj.ast.stmt.InlineStmt
import de.leanovate.jbj.ast.expr.VarGetAndIncrExpr
import de.leanovate.jbj.ast.expr.comp.BoolOrExpr
import de.leanovate.jbj.parser.JbjTokens.Keyword
import de.leanovate.jbj.ast.expr.value.StringConstExpr
import de.leanovate.jbj.parser.JbjTokens.StringLit
import de.leanovate.jbj.parser.JbjTokens.VarIdentifier
import de.leanovate.jbj.ast.expr.CallExpr
import de.leanovate.jbj.ast.expr.comp.GtExpr
import de.leanovate.jbj.parser.JbjTokens.ScriptStartEcho
import de.leanovate.jbj.ast.expr.calc.DivExpr
import de.leanovate.jbj.ast.stmt.EchoStmt

object JbjParser extends Parsers {
  type Elem = JbjTokens.Token

  private val keywordCache = mutable.HashMap[String, Parser[Keyword]]()

  def value: Parser[Expr] =
    (numericLit ^^ (s => IntegerConstExpr(s.position, s.chars.toLong))
      | stringLit ^^ (s => StringConstExpr(s.position, s.chars))
      | interpolatedStringLit ^^ (s => InterpolatedStringExpr(s.position, s.charOrInterpolations))
      )

  def variableRef: Parser[Expr] = variable <~ "+" <~ "+" ^^ {
    s => VarGetAndIncrExpr(s.position, s.name)
  } | variable <~ "-" <~ "-" ^^ {
    s => VarGetAndDecrExpr(s.position, s.name)
  } | "+" ~> "+" ~> variable ^^ {
    s => VarGetAndIncrExpr(s.position, s.name)
  } | variable ~ rep1(indexExpr) ^^ {
    case s ~ indices => ArrayGetExpr(s.position, s.name, indices)
  } | variable ^^ {
    s => VarGetExpr(s.position, s.name)
  } | "array" ~ "(" ~ repsep(arrayValues, ",") <~ ")" ^^ {
    case array ~ _ ~ arrayValues => ArrayCreateExpr(array.position, arrayValues)
  }

  def indexExpr: Parser[Expr] = "[" ~> expr <~ "]"

  def arrayValues: Parser[(Option[Expr], Expr)] = expr ~ "=>" ~ expr ^^ {
    case indexExpr ~ _ ~ valueExpr => (Some(indexExpr), valueExpr)
  } | expr ^^ {
    valueExpr => (None, valueExpr)
  }

  def functionCall: Parser[Expr] = ident ~ "(" ~ repsep(expr, ",") <~ ")" ^^ {
    case name ~ _ ~ params => CallExpr(name.position, name.chars, params)
  }

  def parens: Parser[Expr] = "(" ~> expr <~ ")"

  def neg: Parser[NegExpr] = "-" ~ term ^^ {
    case sign ~ term => NegExpr(sign.position, term)
  }

  def constant: Parser[Expr] = ident ^^ {
    name => ConstGetExpr(name.position, name.chars)
  }

  def term: Parser[Expr] = value | variableRef | functionCall | constant | parens | neg

  def binaryOp(level: Int): Parser[((Expr, Expr) => Expr)] = {
    level match {
      case 1 =>
        "||" ^^^ {
          (a: Expr, b: Expr) => BoolOrExpr(a, b)
        } | "&&" ^^^ {
          (a: Expr, b: Expr) => BoolAndExpr(a, b)
        }
      case 2 =>
        ">" ^^^ {
          (a: Expr, b: Expr) => GtExpr(a, b)
        } | ">=" ^^^ {
          (a: Expr, b: Expr) => GeExpr(a, b)
        } | "<" ^^^ {
          (a: Expr, b: Expr) => LtExpr(a, b)
        } | "<=" ^^^ {
          (a: Expr, b: Expr) => LeExpr(a, b)
        } | "==" ^^^ {
          (a: Expr, b: Expr) => EqExpr(a, b)
        }
      case 3 =>
        "." ^^^ {
          (a: Expr, b: Expr) => DotExpr(a, b)
        }
      case 4 =>
        "+" ^^^ {
          (a: Expr, b: Expr) => AddExpr(a, b)
        } | "-" ^^^ {
          (a: Expr, b: Expr) => SubExpr(a, b)
        }
      case 5 =>
        "*" ^^^ {
          (a: Expr, b: Expr) => MulExpr(a, b)
        } | "/" ^^^ {
          (a: Expr, b: Expr) => DivExpr(a, b)
        }
      case _ => throw new RuntimeException("bad precedence level " + level)
    }
  }

  val minPrec = 1

  val maxPrec = 5

  def binary(level: Int): Parser[Expr] =
    if (level > maxPrec) {
      term
    }
    else {
      binary(level + 1) * binaryOp(level)
    }

  def expr: Parser[Expr] = binary(minPrec) | term

  def regularStmt: Parser[Stmt] =
    "static" ~ rep1sep(assignment, ",") ^^ {
      case static ~ assignments => StaticAssignStmt(static.position, assignments)
    } | "global" ~ rep1sep(assignment, ",") ^^ {
      case global ~ assignments => GlobalAssignStmt(global.position, assignments)
    } | rep1sep(assignmentWithExpr, ",") ^^ {
      assignments => AssignStmt(assignments.head.position, assignments)
    } | "echo" ~ rep1sep(expr, ",") ^^ {
      case echo ~ params => EchoStmt(echo.position, params)
    } | "return" ~ expr ^^ {
      case ret ~ expr => ReturnStmt(ret.position, expr)
    } | "break" ~ opt(numericLit) ^^ {
      case br ~ depth => BreakStmt(br.position, depth.map(_.chars.toInt).getOrElse(1))
    } | "continue" ~ opt(numericLit) ^^ {
      case con ~ depth => ContinueStmt(con.position, depth.map(_.chars.toInt).getOrElse(1))
    } | expr ^^ {
      expr => ExprStmt(expr)
    }

  def assignment: Parser[Assignment] = assignmentWithExpr | variable ^^ {
    variable => Assignment(variable.position, variable.name, None)
  }

  def assignmentWithExpr: Parser[Assignment] = variable ~ "=" ~ expr ^^ {
    case variable ~ _ ~ expr => Assignment(variable.position, variable.name, Some(expr))
  }

  def blockLikeStmt: Parser[Stmt] =
    ifStmt | switchStmt | whileStmt | forStmt | foreachStmt | functionDef | classDef | block

  def closedStmt: Parser[Stmt] = regularStmt <~ ";" | blockLikeStmt

  def stmtsWithUnclosed: Parser[List[Stmt]] = rep(closedStmt) ~ opt(regularStmt) ^^ {
    case stmts ~ optUnclosed => stmts ++ optUnclosed
  }

  def stmtsOrInline: Parser[List[Stmt]] = rep(rep1(closedStmt) |
    opt(regularStmt) ~ scriptEnd ~ rep(inline) <~ scriptStart ^^ {
      case optUnclosed ~ _ ~ inline => optUnclosed.toList ++ inline
    }) ^^ {
    stmts => stmts.flatten
  }

  def block: Parser[BlockStmt] = "{" ~ stmtsOrInline <~ "}" ^^ {
    case paren ~ stmts => BlockStmt(paren.position, stmts)
  }

  def ifStmt: Parser[IfStmt] = "if" ~ "(" ~ expr ~ ")" ~ closedStmt ~ rep(elseIfBlock) ~ opt("else" ~> closedStmt) ^^ {
    case ifT ~ _ ~ cond ~ _ ~ thenBlock ~ elseIfs ~ optElse => IfStmt(ifT.position, cond, thenBlock, elseIfs, optElse)
  }

  def elseIfBlock: Parser[ElseIfBlock] = "elseif" ~> "(" ~> expr ~ ")" ~ closedStmt ^^ {
    case cond ~ _ ~ thenBlock => ElseIfBlock(cond, thenBlock)
  }

  def switchStmt: Parser[SwitchStmt] = "switch" ~ "(" ~ expr ~ ")" ~ "{" ~ switchCases ~ "}" ^^ {
    case switchT ~ _ ~ expr ~ _ ~ _ ~ cases ~ _ => SwitchStmt(switchT.position, expr, cases)
  }

  def switchCases: Parser[List[SwitchCase]] = rep(
    "case" ~> expr ~ ":" ~ stmtsOrInline ^^ {
      case expr ~ _ ~ stmts => CaseBlock(expr, stmts)
    } | "default" ~> ":" ~> stmtsOrInline ^^ {
      stmts => DefaultCaseBlock(stmts)
    }
  )

  def whileStmt: Parser[WhileStmt] = "while" ~ "(" ~ expr ~ ")" ~ closedStmt ^^ {
    case wh ~ _ ~ expr ~ _ ~ stmt => WhileStmt(wh.position, expr, stmt)
  }

  def forStmt: Parser[ForStmt] = "for" ~ "(" ~ regularStmt ~ ";" ~ expr ~ ";" ~ regularStmt ~ ")" ~ closedStmt ^^ {
    case fo ~ _ ~ beforeStmt ~ _ ~ condition ~ _ ~ afterStmt ~ _ ~ stmt =>
      ForStmt(fo.position, beforeStmt, condition, afterStmt, stmt)
  }

  def foreachStmt: Parser[Stmt] = "foreach" ~ "(" ~ expr ~ "as" ~ variable ~ opt("=>" ~> variable) ~ ")" ~ closedStmt ^^ {
    case fo ~ _ ~ arrayExpr ~ _ ~ valueName ~ None ~ _ ~ stmt =>
      ForeachValueStmt(fo.position, arrayExpr, valueName.name, stmt)
    case fo ~ _ ~ arrayExpr ~ _ ~ keyName ~ Some(valueName) ~ _ ~ stmt =>
      ForeachKeyValueStmt(fo.position, arrayExpr, keyName.name, valueName.name, stmt)
  }

  def functionDef: Parser[FunctionDefStmt] = "function" ~ ident ~ "(" ~ parameterDefs ~ ")" ~ block ^^ {
    case func ~ name ~ _ ~ parameters ~ _ ~ body => FunctionDefStmt(func.position, name.chars, parameters, body)
  }

  def classDef: Parser[ClassDefStmt] = "class" ~ ident ~ opt("extends" ~> ident) ~ block ^^ {
    case cls ~ className ~ superClassName ~ body =>
      ClassDefStmt(cls.position, className.chars, superClassName.map(_.chars), body)
  }

  def parameterDefs: Parser[List[ParameterDef]] = repsep(parameterDef, ",")

  def parameterDef: Parser[ParameterDef] = variable ^^ (v => ParameterDef(v.name, byRef = false, default = None))

  def script: Parser[List[Stmt]] =
    scriptStart ~> stmtsWithUnclosed |
      scriptStartEcho ~ rep1sep(expr, ",") ~ opt(";" ~> stmtsWithUnclosed) ^^ {
        case start ~ params ~ None => EchoStmt(start.position, params) :: Nil
        case start ~ params ~ Some(stmts) => EchoStmt(start.position, params) :: stmts
      }

  def inlineAndScript: Parser[List[Stmt]] = rep(inline) ~ opt(script) ^^ {
    case inline ~ None => inline
    case inline ~ Some(script) => inline ++ script
  }

  def prog: Parser[Prog] = repsep(inlineAndScript, scriptEnd) ^^ {
    stmts => Prog(FilePosition("-", 0), stmts.flatten)
  }

  def inline: Parser[InlineStmt] =
    elem("inline", _.isInstanceOf[Inline]) ^^ {
      t => InlineStmt(t.position, t.chars)
    }

  def scriptStart: Parser[String] =
    elem("scriptStart", _.isInstanceOf[ScriptStart]) ^^ (_.chars)

  def scriptStartEcho: Parser[ScriptStartEcho] =
    elem("scriptStartEcho", _.isInstanceOf[ScriptStartEcho]) ^^ (_.asInstanceOf[ScriptStartEcho])

  def scriptEnd: Parser[String] =
    elem("scriptEnd", _.isInstanceOf[ScriptEnd]) ^^ (_.chars)

  /** A parser which matches a single keyword token.
    *
    * @param chars    The character string making up the matched keyword.
    * @return a `Parser` that matches the given string
    */
  //  implicit def keyword(chars: String): Parser[String] = accept(Keyword(chars)) ^^ (_.chars)
  implicit def keyword(chars: String): Parser[Keyword] =
    keywordCache.getOrElseUpdate(chars, elem("keyword " + chars, {
      t => t.chars == chars && t.isInstanceOf[Keyword]
    }) ^^ (_.asInstanceOf[Keyword]))

  /** A parser which matches a numeric literal */
  def numericLit: Parser[NumericLit] =
    elem("number", _.isInstanceOf[NumericLit]) ^^ (_.asInstanceOf[NumericLit])

  /** A parser which matches a string literal */
  def stringLit: Parser[StringLit] =
    elem("string literal", _.isInstanceOf[StringLit]) ^^ (_.asInstanceOf[StringLit])

  def interpolatedStringLit: Parser[InterpolatedStringLit] =
    elem("interpolated string literal", _.isInstanceOf[InterpolatedStringLit]) ^^ (_.asInstanceOf[InterpolatedStringLit])

  /** A parser which matches an identifier */
  def ident: Parser[Identifier] =
    elem("identifier", _.isInstanceOf[Identifier]) ^^ (_.asInstanceOf[Identifier])

  def variable: Parser[VarIdentifier] =
    elem("variable", _.isInstanceOf[VarIdentifier]) ^^ (_.asInstanceOf[VarIdentifier])

  def parse(s: String): ParseResult[Prog] = {
    val tokens = new JbjInitialLexer(s)
    phrase(prog)(tokens)
  }

  def parseExpr(s: String): Expr = {
    val tokens = new JbjScriptLexer(s)
    phrase(expr)(tokens) match {
      case Success(expr, _) => expr
      case e: NoSuccess =>
        throw new IllegalArgumentException("Bad syntax: " + e)
    }
  }

  def apply(s: String): Prog = {
    parse(s) match {
      case Success(tree, _) => tree
      case e: NoSuccess =>
        throw new IllegalArgumentException("Bad syntax: " + e)
    }
  }

  //Simplify testing
  def test(exprstr: String) = {
    parse(exprstr) match {
      case Success(tree, _) =>
        println("Tree: " + tree)
        val context = GlobalContext(System.out, System.err)
        tree.exec(context)
      case e: NoSuccess => Console.err.println(e)
    }
  }

  //A main method for testing
  def main(args: Array[String]) = {
    test( """<?php
            |
            |define("MAX_64Bit", 9223372036854775807);
            |define("MAX_32Bit", 2147483647);
            |define("MIN_64Bit", -9223372036854775807 - 1);
            |define("MIN_32Bit", -2147483647 - 1);
            |
            |$longVals = array(
            |    MAX_64Bit, MIN_64Bit, MAX_32Bit, MIN_32Bit, MAX_64Bit - MAX_32Bit, MIN_64Bit - MIN_32Bit,
            |    MAX_32Bit + 1, MIN_32Bit - 1, MAX_32Bit * 2, (MAX_32Bit * 2) + 1, (MAX_32Bit * 2) - 1,
            |    MAX_64Bit -1, MAX_64Bit + 1, MIN_64Bit + 1, MIN_64Bit - 1
            |);
            |
            |
            |foreach ($longVals as $longVal) {
            |   echo "--- testing: $longVal ---\n";
            |   var_dump(-$longVal);
            |}
            |
            |?>""".stripMargin)
  }
}
