package de.leanovate.jbj.parser

import de.leanovate.jbj.parser.JbjTokens._
import scala.util.parsing.input.CharArrayReader._

object DoubleQuotesLexer extends Lexer with CommonLexerPatterns {
  val token: Parser[(Token, Option[LexerMode])] =
    '$' ~> rep1(identChar) <~ guard(str("->") ~ identChar) ^^ {
      name => Variable(name mkString "") -> Some(LookingForPropertyLexerMode(DoubleQuotedLexerMode))
    } | doubleQuotedStr ^^ {
      str => EncapsAndWhitespace(str) -> None
    } | '$' ~ '{' ^^^ (Keyword("${") -> None) |
      '{' ~ '$' ^^^ (Keyword("{$") -> None) |
      '$' ~> rep1(identChar) ^^ {
        name => Variable(name mkString "") -> None
      } | '"' ^^^ Keyword("\"") -> Some(ScriptingLexerMode)

  private def doubleQuotedChar: Parser[Char] = encapsCharReplacements | chrExcept('\"', '$', '{', EofCh) |
    '$' <~ not(identChar | '{') | '{' <~ not('$')

  private def doubleQuotedStr: Parser[String] = rep1(doubleQuotedChar) ^^ (_ mkString "")
}