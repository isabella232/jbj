package de.leanovate.jbj.parser

import scala.util.parsing.input.{CharArrayReader, Reader}
import de.leanovate.jbj.parser.JbjTokens.{Token, Inline, ScriptStart, ScriptStartEcho, EOF, errorToken}
import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.CharArrayReader.EofCh

class JbjInitialLexer(in: Reader[Char]) extends Reader[Token] with Parsers {
  type Elem = Char

  def this(in: String) = this(new CharArrayReader(in.toCharArray))

  private val (tok, rest1) = inline(in) match {
    case Success(tok, in) => (tok, in)
    case ns: NoSuccess => (errorToken(ns.msg), ns.next)
  }

  def first = tok

  def rest = tok match {
    case ScriptStart() => new JbjScriptLexer(rest1)
    case ScriptStartEcho() => new JbjScriptLexer(rest1)
    case _ => new JbjInitialLexer(rest1)
  }

  def pos = rest1.pos

  def atEnd = in.atEnd

  private def inline: Parser[Token] =
    (scriptStart ^^^ ScriptStart()
      | '<' ~ '?' ~ php ~ witespaceChar ^^^ ScriptStart()
      | '<' ~ '?' ~ '=' ~ witespaceChar ^^^ ScriptStartEcho()
      | '<' ~ '?' ^^^ ScriptStart()
      | '<' ~ '%' ~ '=' ^^^ ScriptStartEcho()
      | '<' ~ '%' ^^^ ScriptStart()
      | '<' ^^^ Inline("<")
      | EofCh ^^^ EOF
      | rep(chrExcept(EofCh, '<')) ^^ {
      chars => Inline(chars mkString "")
    })

  private def scriptStart = script ~ optWhitespace ~ language ~ optWhitespace ~ '=' ~
    optWhitespace ~ opt(quote) ~ php ~ opt(quote) ~ optWhitespace ~ '>'

  private def script = '<' ~ 's' ~ 'c' ~ 'r' ~ 'i' ~ 'p' ~ 't'

  private def language = 'l' ~ 'a' ~ 'n' ~ 'g' ~ 'u' ~ 'a' ~ 'g' ~ 'e'

  private def witespaceChar = elem("whitespace char", ch => ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n')

  private def optWhitespace = opt(rep(witespaceChar))

  private def quote = elem("quote", ch => ch == '"' || ch == '\'')

  private def php = 'p' ~ 'h' ~ 'p'

  /** A character-parser that matches any character except the ones given in `cs` (and returns it). */
  private def chrExcept(cs: Char*) = elem("", ch => (cs forall (ch != _)))
}