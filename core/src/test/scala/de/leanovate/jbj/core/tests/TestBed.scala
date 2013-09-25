/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                 (Tests based on PHP)   */

package de.leanovate.jbj.core.tests

import scala.util.parsing.input.Reader
import de.leanovate.jbj.core.parser.JbjTokens.Token
import de.leanovate.jbj.core.parser._
import de.leanovate.jbj.core.ast.Prog
import de.leanovate.jbj.core.{JbjEnvironmentBuilder, JbjEnv}
import de.leanovate.jbj.runtime.env.CgiEnvironment
import de.leanovate.jbj.core.parser.InitialLexer
import de.leanovate.jbj.core.parser.ParseContext
import scala.Some
import de.leanovate.jbj.api.http.JbjSettings

object TestBed {
  //Simplify testing
  def test(exprstr: String) = {
    var tokens: Reader[Token] = new TokenReader(exprstr, InitialLexerMode(shortOpenTag = true, aspTags = true).newLexer())

    println("Tokens")
    var count = 0
    while (!tokens.atEnd && count < 1000) {
      println(tokens.first)
      tokens = tokens.rest
      count += 1
    }

    val jbj = JbjEnvironmentBuilder().withScriptLocator(TestLocator).withErrStream(System.err).build()
    val tokens2 = new TokenReader(exprstr, InitialLexerMode(shortOpenTag = true, aspTags = true).newLexer())
    val parser = new JbjParser(ParseContext("/classes/bla.php", jbj.settings))
    parser.phrase(parser.start)(tokens2) match {
      case parser.Success(tree: Prog, _) =>
        println("Tree")
        println(AstAsXmlNodeVisitor.dump(tree))

        implicit val context = jbj.newGlobalContext(System.out)

        CgiEnvironment.httpRequest(
          TestRequestInfo.post("/bla", "multipart/form-data; boundary=---------------------------20896060251896012921717172737",
            """-----------------------------20896060251896012921717172737
              |Content-Disposition: form-data; name="submitter"
              |
              |testname
              |-----------------------------20896060251896012921717172737
              |Content-Disposition: form-data; name="pics"; filename="bug37276.txt"
              |Content-Type: text/plain
              |
              |bug37276
              |
              |-----------------------------20896060251896012921717172737--
              | """.stripMargin.replace("\n", "\r\n"), Seq.empty))

        context.settings.setErrorReporting(JbjSettings.E_ALL)
        try {
          tree.exec(context)
        } finally {
          context.cleanup()
        }
      case e: parser.NoSuccess =>
        println(e)
    }
  }

  //A main method for testing
  def main(args: Array[String]) {
    test(
      """<?php
        |
        |define('MAX_LOOPS',5);
        |
        |function withRefValue($elements, $transform) {
        |	echo "\n---( Array with $elements element(s): )---\n";
        |	//Build array:
        |	for ($i=0; $i<$elements; $i++) {
        |		$a[] = "v.$i";
        |	}
        |	$counter=0;
        |
        |	echo "--> State of array before loop:\n";
        |	var_dump($a);
        |
        |	echo "--> Do loop:\n";
        |	foreach ($a as $k=>$v) {
        |		echo "     iteration $counter:  \$k=$k; \$v=$v\n";
        |		eval($transform);
        |		$counter++;
        |		if ($counter>MAX_LOOPS) {
        |			echo "  ** Stuck in a loop! **\n";
        |			break;
        |		}
        |	}
        |
        |	echo "--> State of array after loop:\n";
        |	var_dump($a);
        |}
        |
        |
        |echo "\nPopping elements off end of an unreferenced array";
        |$transform = 'array_pop($a);';
        |withRefValue(1, $transform);
        |withRefValue(2, $transform);
        |withRefValue(3, $transform);
        |withRefValue(4, $transform);
        |
        |echo "\n\n\nShift elements off start of an unreferenced array";
        |$transform = 'array_shift($a);';
        |withRefValue(1, $transform);
        |withRefValue(2, $transform);
        |withRefValue(3, $transform);
        |withRefValue(4, $transform);
        |
        |echo "\n\n\nRemove current element of an unreferenced array";
        |$transform = 'unset($a[$k]);';
        |withRefValue(1, $transform);
        |withRefValue(2, $transform);
        |withRefValue(3, $transform);
        |withRefValue(4, $transform);
        |
        |echo "\n\n\nAdding elements to the end of an unreferenced array";
        |$transform = 'array_push($a, "new.$counter");';
        |withRefValue(1, $transform);
        |withRefValue(2, $transform);
        |withRefValue(3, $transform);
        |withRefValue(4, $transform);
        |
        |echo "\n\n\nAdding elements to the start of an unreferenced array";
        |$transform = 'array_unshift($a, "new.$counter");';
        |withRefValue(1, $transform);
        |withRefValue(2, $transform);
        |withRefValue(3, $transform);
        |withRefValue(4, $transform);
        |
        |?>
        |""".stripMargin)
  }
}
