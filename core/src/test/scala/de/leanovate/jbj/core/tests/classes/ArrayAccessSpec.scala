/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                 (Tests based on PHP)   */

package de.leanovate.jbj.core.tests.classes

import org.specs2.mutable.SpecificationWithJUnit
import de.leanovate.jbj.core.tests.TestJbjExecutor

class ArrayAccessSpec extends SpecificationWithJUnit with TestJbjExecutor {
  "object array access" should {
    "ZE2 ArrayAccess" in {
      // classes/array_access_001.phpt
      script(
        """<?php
          |class object implements ArrayAccess {
          |
          |	public $a = array('1st', 1, 2=>'3rd', '4th'=>4);
          |
          |	function offsetExists($index) {
          |		echo __METHOD__ . "($index)\n";
          |		return array_key_exists($index, $this->a);
          |	}
          |	function offsetGet($index) {
          |		echo __METHOD__ . "($index)\n";
          |		return $this->a[$index];
          |	}
          |	function offsetSet($index, $newval) {
          |		echo __METHOD__ . "($index,$newval)\n";
          |		return $this->a[$index] = $newval;
          |	}
          |	function offsetUnset($index) {
          |		echo __METHOD__ . "($index)\n";
          |		unset($this->a[$index]);
          |	}
          |}
          |
          |$obj = new Object;
          |
          |var_dump($obj->a);
          |
          |echo "===EMPTY===\n";
          |var_dump(empty($obj[0]));
          |var_dump(empty($obj[1]));
          |var_dump(empty($obj[2]));
          |var_dump(empty($obj['4th']));
          |var_dump(empty($obj['5th']));
          |var_dump(empty($obj[6]));
          |
          |echo "===isset===\n";
          |var_dump(isset($obj[0]));
          |var_dump(isset($obj[1]));
          |var_dump(isset($obj[2]));
          |var_dump(isset($obj['4th']));
          |var_dump(isset($obj['5th']));
          |var_dump(isset($obj[6]));
          |
          |echo "===offsetGet===\n";
          |var_dump($obj[0]);
          |var_dump($obj[1]);
          |var_dump($obj[2]);
          |var_dump($obj['4th']);
          |var_dump($obj['5th']);
          |var_dump($obj[6]);
          |
          |echo "===offsetSet===\n";
          |echo "WRITE 1\n";
          |$obj[1] = 'Changed 1';
          |var_dump($obj[1]);
          |echo "WRITE 2\n";
          |$obj['4th'] = 'Changed 4th';
          |var_dump($obj['4th']);
          |echo "WRITE 3\n";
          |$obj['5th'] = 'Added 5th';
          |var_dump($obj['5th']);
          |echo "WRITE 4\n";
          |$obj[6] = 'Added 6';
          |var_dump($obj[6]);
          |
          |var_dump($obj[0]);
          |var_dump($obj[2]);
          |
          |$x = $obj[6] = 'changed 6';
          |var_dump($obj[6]);
          |var_dump($x);
          |
          |echo "===unset===\n";
          |var_dump($obj->a);
          |unset($obj[2]);
          |unset($obj['4th']);
          |unset($obj[7]);
          |unset($obj['8th']);
          |var_dump($obj->a);
          |
          |?>
          |===DONE===
          |""".stripMargin
      ).result must haveOutput(
        """array(4) {
          |  [0]=>
          |  string(3) "1st"
          |  [1]=>
          |  int(1)
          |  [2]=>
          |  string(3) "3rd"
          |  ["4th"]=>
          |  int(4)
          |}
          |===EMPTY===
          |object::offsetExists(0)
          |object::offsetGet(0)
          |bool(false)
          |object::offsetExists(1)
          |object::offsetGet(1)
          |bool(false)
          |object::offsetExists(2)
          |object::offsetGet(2)
          |bool(false)
          |object::offsetExists(4th)
          |object::offsetGet(4th)
          |bool(false)
          |object::offsetExists(5th)
          |bool(true)
          |object::offsetExists(6)
          |bool(true)
          |===isset===
          |object::offsetExists(0)
          |bool(true)
          |object::offsetExists(1)
          |bool(true)
          |object::offsetExists(2)
          |bool(true)
          |object::offsetExists(4th)
          |bool(true)
          |object::offsetExists(5th)
          |bool(false)
          |object::offsetExists(6)
          |bool(false)
          |===offsetGet===
          |object::offsetGet(0)
          |string(3) "1st"
          |object::offsetGet(1)
          |int(1)
          |object::offsetGet(2)
          |string(3) "3rd"
          |object::offsetGet(4th)
          |int(4)
          |object::offsetGet(5th)
          |
          |Notice: Undefined index: 5th in /classes/ArrayAccessSpec.inlinePhp on line 12
          |NULL
          |object::offsetGet(6)
          |
          |Notice: Undefined offset: 6 in /classes/ArrayAccessSpec.inlinePhp on line 12
          |NULL
          |===offsetSet===
          |WRITE 1
          |object::offsetSet(1,Changed 1)
          |object::offsetGet(1)
          |string(9) "Changed 1"
          |WRITE 2
          |object::offsetSet(4th,Changed 4th)
          |object::offsetGet(4th)
          |string(11) "Changed 4th"
          |WRITE 3
          |object::offsetSet(5th,Added 5th)
          |object::offsetGet(5th)
          |string(9) "Added 5th"
          |WRITE 4
          |object::offsetSet(6,Added 6)
          |object::offsetGet(6)
          |string(7) "Added 6"
          |object::offsetGet(0)
          |string(3) "1st"
          |object::offsetGet(2)
          |string(3) "3rd"
          |object::offsetSet(6,changed 6)
          |object::offsetGet(6)
          |string(9) "changed 6"
          |string(9) "changed 6"
          |===unset===
          |array(6) {
          |  [0]=>
          |  string(3) "1st"
          |  [1]=>
          |  string(9) "Changed 1"
          |  [2]=>
          |  string(3) "3rd"
          |  ["4th"]=>
          |  string(11) "Changed 4th"
          |  ["5th"]=>
          |  string(9) "Added 5th"
          |  [6]=>
          |  string(9) "changed 6"
          |}
          |object::offsetUnset(2)
          |object::offsetUnset(4th)
          |object::offsetUnset(7)
          |object::offsetUnset(8th)
          |array(4) {
          |  [0]=>
          |  string(3) "1st"
          |  [1]=>
          |  string(9) "Changed 1"
          |  ["5th"]=>
          |  string(9) "Added 5th"
          |  [6]=>
          |  string(9) "changed 6"
          |}
          |===DONE===
          |""".stripMargin
      )
    }

    "ZE2 ArrayAccess::offsetSet without return" in {
      // classes/array_access_002.phpt
      script(
        """<?php
          |class object implements ArrayAccess {
          |
          |	public $a = array('1st', 1, 2=>'3rd', '4th'=>4);
          |
          |	function offsetExists($index) {
          |		echo __METHOD__ . "($index)\n";
          |		return array_key_exists($index, $this->a);
          |	}
          |	function offsetGet($index) {
          |		echo __METHOD__ . "($index)\n";
          |		return $this->a[$index];
          |	}
          |	function offsetSet($index, $newval) {
          |		echo __METHOD__ . "($index,$newval)\n";
          |		/*return*/ $this->a[$index] = $newval;
          |	}
          |	function offsetUnset($index) {
          |		echo __METHOD__ . "($index)\n";
          |		unset($this->a[$index]);
          |	}
          |}
          |
          |$obj = new Object;
          |
          |var_dump($obj->a);
          |
          |echo "===EMPTY===\n";
          |var_dump(empty($obj[0]));
          |var_dump(empty($obj[1]));
          |var_dump(empty($obj[2]));
          |var_dump(empty($obj['4th']));
          |var_dump(empty($obj['5th']));
          |var_dump(empty($obj[6]));
          |
          |echo "===isset===\n";
          |var_dump(isset($obj[0]));
          |var_dump(isset($obj[1]));
          |var_dump(isset($obj[2]));
          |var_dump(isset($obj['4th']));
          |var_dump(isset($obj['5th']));
          |var_dump(isset($obj[6]));
          |
          |echo "===offsetGet===\n";
          |var_dump($obj[0]);
          |var_dump($obj[1]);
          |var_dump($obj[2]);
          |var_dump($obj['4th']);
          |var_dump($obj['5th']);
          |var_dump($obj[6]);
          |
          |echo "===offsetSet===\n";
          |echo "WRITE 1\n";
          |$obj[1] = 'Changed 1';
          |var_dump($obj[1]);
          |echo "WRITE 2\n";
          |$obj['4th'] = 'Changed 4th';
          |var_dump($obj['4th']);
          |echo "WRITE 3\n";
          |$obj['5th'] = 'Added 5th';
          |var_dump($obj['5th']);
          |echo "WRITE 4\n";
          |$obj[6] = 'Added 6';
          |var_dump($obj[6]);
          |
          |var_dump($obj[0]);
          |var_dump($obj[2]);
          |
          |$x = $obj[6] = 'changed 6';
          |var_dump($obj[6]);
          |var_dump($x);
          |
          |echo "===unset===\n";
          |var_dump($obj->a);
          |unset($obj[2]);
          |unset($obj['4th']);
          |unset($obj[7]);
          |unset($obj['8th']);
          |var_dump($obj->a);
          |
          |?>
          |===DONE===
          |""".stripMargin
      ).result must haveOutput(
        """array(4) {
          |  [0]=>
          |  string(3) "1st"
          |  [1]=>
          |  int(1)
          |  [2]=>
          |  string(3) "3rd"
          |  ["4th"]=>
          |  int(4)
          |}
          |===EMPTY===
          |object::offsetExists(0)
          |object::offsetGet(0)
          |bool(false)
          |object::offsetExists(1)
          |object::offsetGet(1)
          |bool(false)
          |object::offsetExists(2)
          |object::offsetGet(2)
          |bool(false)
          |object::offsetExists(4th)
          |object::offsetGet(4th)
          |bool(false)
          |object::offsetExists(5th)
          |bool(true)
          |object::offsetExists(6)
          |bool(true)
          |===isset===
          |object::offsetExists(0)
          |bool(true)
          |object::offsetExists(1)
          |bool(true)
          |object::offsetExists(2)
          |bool(true)
          |object::offsetExists(4th)
          |bool(true)
          |object::offsetExists(5th)
          |bool(false)
          |object::offsetExists(6)
          |bool(false)
          |===offsetGet===
          |object::offsetGet(0)
          |string(3) "1st"
          |object::offsetGet(1)
          |int(1)
          |object::offsetGet(2)
          |string(3) "3rd"
          |object::offsetGet(4th)
          |int(4)
          |object::offsetGet(5th)
          |
          |Notice: Undefined index: 5th in /classes/ArrayAccessSpec.inlinePhp on line 12
          |NULL
          |object::offsetGet(6)
          |
          |Notice: Undefined offset: 6 in /classes/ArrayAccessSpec.inlinePhp on line 12
          |NULL
          |===offsetSet===
          |WRITE 1
          |object::offsetSet(1,Changed 1)
          |object::offsetGet(1)
          |string(9) "Changed 1"
          |WRITE 2
          |object::offsetSet(4th,Changed 4th)
          |object::offsetGet(4th)
          |string(11) "Changed 4th"
          |WRITE 3
          |object::offsetSet(5th,Added 5th)
          |object::offsetGet(5th)
          |string(9) "Added 5th"
          |WRITE 4
          |object::offsetSet(6,Added 6)
          |object::offsetGet(6)
          |string(7) "Added 6"
          |object::offsetGet(0)
          |string(3) "1st"
          |object::offsetGet(2)
          |string(3) "3rd"
          |object::offsetSet(6,changed 6)
          |object::offsetGet(6)
          |string(9) "changed 6"
          |string(9) "changed 6"
          |===unset===
          |array(6) {
          |  [0]=>
          |  string(3) "1st"
          |  [1]=>
          |  string(9) "Changed 1"
          |  [2]=>
          |  string(3) "3rd"
          |  ["4th"]=>
          |  string(11) "Changed 4th"
          |  ["5th"]=>
          |  string(9) "Added 5th"
          |  [6]=>
          |  string(9) "changed 6"
          |}
          |object::offsetUnset(2)
          |object::offsetUnset(4th)
          |object::offsetUnset(7)
          |object::offsetUnset(8th)
          |array(4) {
          |  [0]=>
          |  string(3) "1st"
          |  [1]=>
          |  string(9) "Changed 1"
          |  ["5th"]=>
          |  string(9) "Added 5th"
          |  [6]=>
          |  string(9) "changed 6"
          |}
          |===DONE===
          |""".stripMargin
      )
    }
  }
}
