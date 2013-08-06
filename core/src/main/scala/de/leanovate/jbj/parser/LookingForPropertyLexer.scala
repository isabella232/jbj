package de.leanovate.jbj.parser

import de.leanovate.jbj.parser.JbjTokens._

class LookingForPropertyLexer(prevMode: LexerMode) extends Lexer with CommonLexerPatterns {
  override def token: Parser[(Token, Option[LexerMode])] =
    str("->") ^^^ Keyword("->") -> None |
      identChar ~ rep(identChar | digit) ^^ {
        case first ~ rest => Identifier(first :: rest mkString "") -> Some(prevMode)
      }
}
