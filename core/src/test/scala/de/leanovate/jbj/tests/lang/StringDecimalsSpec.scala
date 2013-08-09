package de.leanovate.jbj.tests.lang

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FreeSpec
import de.leanovate.jbj.tests.TestJbjExecutor
import org.scalatest.matchers.MustMatchers

@RunWith(classOf[JUnitRunner])
class StringDecimalsSpec extends FreeSpec with TestJbjExecutor with MustMatchers {
  "String decimals" - {
    "String conversion with multiple decimal points" in {
      // lang/string_decimals_001
      script(
        """<?php
          |function test($str) {
          |  echo "\n--> Testing $str:\n";
          |  var_dump((int)$str);
          |  var_dump((float)$str);
          |  var_dump($str > 0);
          |}
          |
          |test("..9");
          |test(".9.");
          |test("9..");
          |test("9.9.");
          |test("9.9.9");
          |?>
          |===DONE===""".stripMargin
      ).result must haveOutput(
        """
          |--> Testing ..9:
          |int(0)
          |float(0)
          |bool(false)
          |
          |--> Testing .9.:
          |int(0)
          |float(0.9)
          |bool(true)
          |
          |--> Testing 9..:
          |int(9)
          |float(9)
          |bool(true)
          |
          |--> Testing 9.9.:
          |int(9)
          |float(9.9)
          |bool(true)
          |
          |--> Testing 9.9.9:
          |int(9)
          |float(9.9)
          |bool(true)
          |===DONE===""".stripMargin
      )
    }
  }
}