package de.leanovate.jbj.tests.lang

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FreeSpec
import de.leanovate.jbj.tests.TestJbjExecutor
import org.scalatest.matchers.MustMatchers

@RunWith(classOf[JUnitRunner])
class StaticSpec extends FreeSpec with TestJbjExecutor with MustMatchers {
  "Static tests" - {
    "Static keyword - basic tests" in {
      // lang/static_basic_001
      resultOf(
        """<?php
          |
          |echo "\nSame variable used as static and non static.\n";
          |function staticNonStatic() {
          |	echo "---------\n";
          |	$a=0;
          |	echo "$a\n";
          |	static $a=10;
          |	echo "$a\n";
          |	$a++;
          |}
          |staticNonStatic();
          |staticNonStatic();
          |staticNonStatic();
          |
          |echo "\nLots of initialisations in the same statement.\n";
          |function manyInits() {
          |	static $counter=0;
          |	echo "------------- Call $counter --------------\n";
          |	static $a, $b=10, $c=20, $d, $e=30;
          |	echo "Unitialised      :$a\n";
          |	echo "Initialised to 10:$b\n";
          |	echo "Initialised to 20:$c\n";
          |	echo "Unitialised      :$d\n";
          |	echo "Initialised to 30:$e\n";
          |	$a++;
          |	$b++;
          |	$c++;
          |	$d++;
          |	$e++;
          |	$counter++;
          |}
          |manyInits();
          |manyInits();
          |manyInits();
          |
          |echo "\nUsing static keyword at global scope\n";
          |for ($i=0; $i<3; $i++) {
          |   static $s, $k=10;
          |   echo "$s $k\n";
          |   $s++;
          |   $k++;
          |}
          |?>""".stripMargin
      ) must be(
        """
          |Same variable used as static and non static.
          |---------
          |0
          |10
          |---------
          |0
          |11
          |---------
          |0
          |12
          |
          |Lots of initialisations in the same statement.
          |------------- Call 0 --------------
          |Unitialised      :
          |Initialised to 10:10
          |Initialised to 20:20
          |Unitialised      :
          |Initialised to 30:30
          |------------- Call 1 --------------
          |Unitialised      :1
          |Initialised to 10:11
          |Initialised to 20:21
          |Unitialised      :1
          |Initialised to 30:31
          |------------- Call 2 --------------
          |Unitialised      :2
          |Initialised to 10:12
          |Initialised to 20:22
          |Unitialised      :2
          |Initialised to 30:32
          |
          |Using static keyword at global scope
          | 10
          |1 11
          |2 12
          |""".stripMargin
      )
    }

    "Multiple declarations of the same static variable" in {
      resultOf(
        """<?php
          |
          |$a = 5;
          |
          |var_dump($a);
          |
          |static $a = 10;
          |static $a = 11;
          |
          |var_dump($a);
          |
          |function foo() {
          |	static $a = 13;
          |	static $a = 14;
          |
          |	var_dump($a);
          |}
          |
          |foo();
          |
          |?>""".stripMargin
      ) must be (
        """int(5)
          |int(11)
          |int(14)
          |""".stripMargin
      )
    }
  }
}