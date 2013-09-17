/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                                        */

package de.leanovate.jbj.core.parser

import de.leanovate.jbj.core.parser.JbjTokens._
import scala.util.parsing.input.CharArrayReader._
import de.leanovate.jbj.core.parser.JbjTokens.Variable
import de.leanovate.jbj.core.parser.JbjTokens.EncapsAndWhitespace
import de.leanovate.jbj.core.parser.JbjTokens.Keyword

class HereDocLexer(mode: HeredocLexerMode) extends Lexer with CommonLexerPatterns {
  val token: Parser[(Token, Option[LexerMode])] =
    '$' ~> rep1(identChar) <~ guard(str("->") ~ identChar) ^^ {
      name => Variable(name mkString "") -> Some(LookingForPropertyLexerMode(mode))
    } | '$' ~ '{' ^^^ (Keyword("${") -> None) |
      '{' ~ '$' ^^^ (Keyword("{$") -> None) |
      '$' ~> rep1(identChar) ^^ {
        name => Variable(name mkString "") -> None
      } | newLine ~> str(mode.endMarker) ^^ {
      s => HereDocEnd(s) -> Some(mode.prevMode)
    } | hereDocStr ^^ {
      str => EncapsAndWhitespace(str) -> None
    }

  private def hereDocChar: Parser[Char] = encapsCharReplacements |
    chrExcept('\n', '$', '{', EofCh) | '\n' <~ not(str(mode.endMarker)) | '$' <~ not(identChar | '{') | '{' <~ not('$')

  private def hereDocStr: Parser[String] = rep1(hereDocChar) ^^ (_ mkString "")
}
