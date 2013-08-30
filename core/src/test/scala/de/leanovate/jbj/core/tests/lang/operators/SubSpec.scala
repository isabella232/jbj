/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                 (Tests based on PHP)   */

package de.leanovate.jbj.core.tests.lang.operators

import org.specs2.mutable.SpecificationWithJUnit
import de.leanovate.jbj.core.tests.TestJbjExecutor

class SubSpec extends SpecificationWithJUnit with TestJbjExecutor {
  "Subtract operator" should {
    "Test - operator : 64bit long tests" in {
      // lang/operators/subtract_basiclong_64bit
      script(
        """<?php
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
          |$otherVals = array(0, 1, -1, 7, 9, 65, -44, MAX_32Bit, MAX_64Bit);
          |
          |error_reporting(E_ERROR);
          |
          |foreach ($longVals as $longVal) {
          |   foreach($otherVals as $otherVal) {
          |	   echo "--- testing: $longVal - $otherVal ---\n";
          |      var_dump($longVal-$otherVal);
          |   }
          |}
          |
          |foreach ($otherVals as $otherVal) {
          |   foreach($longVals as $longVal) {
          |	   echo "--- testing: $otherVal - $longVal ---\n";
          |      var_dump($otherVal-$longVal);
          |   }
          |}
          |
          |?>
          |===DONE===""".stripMargin
      ).result must haveOutput(
        """--- testing: 9223372036854775807 - 0 ---
          |int(9223372036854775807)
          |--- testing: 9223372036854775807 - 1 ---
          |int(9223372036854775806)
          |--- testing: 9223372036854775807 - -1 ---
          |float(9.2233720368548E+18)
          |--- testing: 9223372036854775807 - 7 ---
          |int(9223372036854775800)
          |--- testing: 9223372036854775807 - 9 ---
          |int(9223372036854775798)
          |--- testing: 9223372036854775807 - 65 ---
          |int(9223372036854775742)
          |--- testing: 9223372036854775807 - -44 ---
          |float(9.2233720368548E+18)
          |--- testing: 9223372036854775807 - 2147483647 ---
          |int(9223372034707292160)
          |--- testing: 9223372036854775807 - 9223372036854775807 ---
          |int(0)
          |--- testing: -9223372036854775808 - 0 ---
          |int(-9223372036854775808)
          |--- testing: -9223372036854775808 - 1 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9223372036854775808 - -1 ---
          |int(-9223372036854775807)
          |--- testing: -9223372036854775808 - 7 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9223372036854775808 - 9 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9223372036854775808 - 65 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9223372036854775808 - -44 ---
          |int(-9223372036854775764)
          |--- testing: -9223372036854775808 - 2147483647 ---
          |float(-9.2233720390023E+18)
          |--- testing: -9223372036854775808 - 9223372036854775807 ---
          |float(-1.844674407371E+19)
          |--- testing: 2147483647 - 0 ---
          |int(2147483647)
          |--- testing: 2147483647 - 1 ---
          |int(2147483646)
          |--- testing: 2147483647 - -1 ---
          |int(2147483648)
          |--- testing: 2147483647 - 7 ---
          |int(2147483640)
          |--- testing: 2147483647 - 9 ---
          |int(2147483638)
          |--- testing: 2147483647 - 65 ---
          |int(2147483582)
          |--- testing: 2147483647 - -44 ---
          |int(2147483691)
          |--- testing: 2147483647 - 2147483647 ---
          |int(0)
          |--- testing: 2147483647 - 9223372036854775807 ---
          |int(-9223372034707292160)
          |--- testing: -2147483648 - 0 ---
          |int(-2147483648)
          |--- testing: -2147483648 - 1 ---
          |int(-2147483649)
          |--- testing: -2147483648 - -1 ---
          |int(-2147483647)
          |--- testing: -2147483648 - 7 ---
          |int(-2147483655)
          |--- testing: -2147483648 - 9 ---
          |int(-2147483657)
          |--- testing: -2147483648 - 65 ---
          |int(-2147483713)
          |--- testing: -2147483648 - -44 ---
          |int(-2147483604)
          |--- testing: -2147483648 - 2147483647 ---
          |int(-4294967295)
          |--- testing: -2147483648 - 9223372036854775807 ---
          |float(-9.2233720390023E+18)
          |--- testing: 9223372034707292160 - 0 ---
          |int(9223372034707292160)
          |--- testing: 9223372034707292160 - 1 ---
          |int(9223372034707292159)
          |--- testing: 9223372034707292160 - -1 ---
          |int(9223372034707292161)
          |--- testing: 9223372034707292160 - 7 ---
          |int(9223372034707292153)
          |--- testing: 9223372034707292160 - 9 ---
          |int(9223372034707292151)
          |--- testing: 9223372034707292160 - 65 ---
          |int(9223372034707292095)
          |--- testing: 9223372034707292160 - -44 ---
          |int(9223372034707292204)
          |--- testing: 9223372034707292160 - 2147483647 ---
          |int(9223372032559808513)
          |--- testing: 9223372034707292160 - 9223372036854775807 ---
          |int(-2147483647)
          |--- testing: -9223372034707292160 - 0 ---
          |int(-9223372034707292160)
          |--- testing: -9223372034707292160 - 1 ---
          |int(-9223372034707292161)
          |--- testing: -9223372034707292160 - -1 ---
          |int(-9223372034707292159)
          |--- testing: -9223372034707292160 - 7 ---
          |int(-9223372034707292167)
          |--- testing: -9223372034707292160 - 9 ---
          |int(-9223372034707292169)
          |--- testing: -9223372034707292160 - 65 ---
          |int(-9223372034707292225)
          |--- testing: -9223372034707292160 - -44 ---
          |int(-9223372034707292116)
          |--- testing: -9223372034707292160 - 2147483647 ---
          |int(-9223372036854775807)
          |--- testing: -9223372034707292160 - 9223372036854775807 ---
          |float(-1.8446744071562E+19)
          |--- testing: 2147483648 - 0 ---
          |int(2147483648)
          |--- testing: 2147483648 - 1 ---
          |int(2147483647)
          |--- testing: 2147483648 - -1 ---
          |int(2147483649)
          |--- testing: 2147483648 - 7 ---
          |int(2147483641)
          |--- testing: 2147483648 - 9 ---
          |int(2147483639)
          |--- testing: 2147483648 - 65 ---
          |int(2147483583)
          |--- testing: 2147483648 - -44 ---
          |int(2147483692)
          |--- testing: 2147483648 - 2147483647 ---
          |int(1)
          |--- testing: 2147483648 - 9223372036854775807 ---
          |int(-9223372034707292159)
          |--- testing: -2147483649 - 0 ---
          |int(-2147483649)
          |--- testing: -2147483649 - 1 ---
          |int(-2147483650)
          |--- testing: -2147483649 - -1 ---
          |int(-2147483648)
          |--- testing: -2147483649 - 7 ---
          |int(-2147483656)
          |--- testing: -2147483649 - 9 ---
          |int(-2147483658)
          |--- testing: -2147483649 - 65 ---
          |int(-2147483714)
          |--- testing: -2147483649 - -44 ---
          |int(-2147483605)
          |--- testing: -2147483649 - 2147483647 ---
          |int(-4294967296)
          |--- testing: -2147483649 - 9223372036854775807 ---
          |float(-9.2233720390023E+18)
          |--- testing: 4294967294 - 0 ---
          |int(4294967294)
          |--- testing: 4294967294 - 1 ---
          |int(4294967293)
          |--- testing: 4294967294 - -1 ---
          |int(4294967295)
          |--- testing: 4294967294 - 7 ---
          |int(4294967287)
          |--- testing: 4294967294 - 9 ---
          |int(4294967285)
          |--- testing: 4294967294 - 65 ---
          |int(4294967229)
          |--- testing: 4294967294 - -44 ---
          |int(4294967338)
          |--- testing: 4294967294 - 2147483647 ---
          |int(2147483647)
          |--- testing: 4294967294 - 9223372036854775807 ---
          |int(-9223372032559808513)
          |--- testing: 4294967295 - 0 ---
          |int(4294967295)
          |--- testing: 4294967295 - 1 ---
          |int(4294967294)
          |--- testing: 4294967295 - -1 ---
          |int(4294967296)
          |--- testing: 4294967295 - 7 ---
          |int(4294967288)
          |--- testing: 4294967295 - 9 ---
          |int(4294967286)
          |--- testing: 4294967295 - 65 ---
          |int(4294967230)
          |--- testing: 4294967295 - -44 ---
          |int(4294967339)
          |--- testing: 4294967295 - 2147483647 ---
          |int(2147483648)
          |--- testing: 4294967295 - 9223372036854775807 ---
          |int(-9223372032559808512)
          |--- testing: 4294967293 - 0 ---
          |int(4294967293)
          |--- testing: 4294967293 - 1 ---
          |int(4294967292)
          |--- testing: 4294967293 - -1 ---
          |int(4294967294)
          |--- testing: 4294967293 - 7 ---
          |int(4294967286)
          |--- testing: 4294967293 - 9 ---
          |int(4294967284)
          |--- testing: 4294967293 - 65 ---
          |int(4294967228)
          |--- testing: 4294967293 - -44 ---
          |int(4294967337)
          |--- testing: 4294967293 - 2147483647 ---
          |int(2147483646)
          |--- testing: 4294967293 - 9223372036854775807 ---
          |int(-9223372032559808514)
          |--- testing: 9223372036854775806 - 0 ---
          |int(9223372036854775806)
          |--- testing: 9223372036854775806 - 1 ---
          |int(9223372036854775805)
          |--- testing: 9223372036854775806 - -1 ---
          |int(9223372036854775807)
          |--- testing: 9223372036854775806 - 7 ---
          |int(9223372036854775799)
          |--- testing: 9223372036854775806 - 9 ---
          |int(9223372036854775797)
          |--- testing: 9223372036854775806 - 65 ---
          |int(9223372036854775741)
          |--- testing: 9223372036854775806 - -44 ---
          |float(9.2233720368548E+18)
          |--- testing: 9223372036854775806 - 2147483647 ---
          |int(9223372034707292159)
          |--- testing: 9223372036854775806 - 9223372036854775807 ---
          |int(-1)
          |--- testing: 9.2233720368548E+18 - 0 ---
          |float(9.2233720368548E+18)
          |--- testing: 9.2233720368548E+18 - 1 ---
          |float(9.2233720368548E+18)
          |--- testing: 9.2233720368548E+18 - -1 ---
          |float(9.2233720368548E+18)
          |--- testing: 9.2233720368548E+18 - 7 ---
          |float(9.2233720368548E+18)
          |--- testing: 9.2233720368548E+18 - 9 ---
          |float(9.2233720368548E+18)
          |--- testing: 9.2233720368548E+18 - 65 ---
          |float(9.2233720368548E+18)
          |--- testing: 9.2233720368548E+18 - -44 ---
          |float(9.2233720368548E+18)
          |--- testing: 9.2233720368548E+18 - 2147483647 ---
          |float(9.2233720347073E+18)
          |--- testing: 9.2233720368548E+18 - 9223372036854775807 ---
          |float(0)
          |--- testing: -9223372036854775807 - 0 ---
          |int(-9223372036854775807)
          |--- testing: -9223372036854775807 - 1 ---
          |int(-9223372036854775808)
          |--- testing: -9223372036854775807 - -1 ---
          |int(-9223372036854775806)
          |--- testing: -9223372036854775807 - 7 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9223372036854775807 - 9 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9223372036854775807 - 65 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9223372036854775807 - -44 ---
          |int(-9223372036854775763)
          |--- testing: -9223372036854775807 - 2147483647 ---
          |float(-9.2233720390023E+18)
          |--- testing: -9223372036854775807 - 9223372036854775807 ---
          |float(-1.844674407371E+19)
          |--- testing: -9.2233720368548E+18 - 0 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9.2233720368548E+18 - 1 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9.2233720368548E+18 - -1 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9.2233720368548E+18 - 7 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9.2233720368548E+18 - 9 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9.2233720368548E+18 - 65 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9.2233720368548E+18 - -44 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9.2233720368548E+18 - 2147483647 ---
          |float(-9.2233720390023E+18)
          |--- testing: -9.2233720368548E+18 - 9223372036854775807 ---
          |float(-1.844674407371E+19)
          |--- testing: 0 - 9223372036854775807 ---
          |int(-9223372036854775807)
          |--- testing: 0 - -9223372036854775808 ---
          |float(9.2233720368548E+18)
          |--- testing: 0 - 2147483647 ---
          |int(-2147483647)
          |--- testing: 0 - -2147483648 ---
          |int(2147483648)
          |--- testing: 0 - 9223372034707292160 ---
          |int(-9223372034707292160)
          |--- testing: 0 - -9223372034707292160 ---
          |int(9223372034707292160)
          |--- testing: 0 - 2147483648 ---
          |int(-2147483648)
          |--- testing: 0 - -2147483649 ---
          |int(2147483649)
          |--- testing: 0 - 4294967294 ---
          |int(-4294967294)
          |--- testing: 0 - 4294967295 ---
          |int(-4294967295)
          |--- testing: 0 - 4294967293 ---
          |int(-4294967293)
          |--- testing: 0 - 9223372036854775806 ---
          |int(-9223372036854775806)
          |--- testing: 0 - 9.2233720368548E+18 ---
          |float(-9.2233720368548E+18)
          |--- testing: 0 - -9223372036854775807 ---
          |int(9223372036854775807)
          |--- testing: 0 - -9.2233720368548E+18 ---
          |float(9.2233720368548E+18)
          |--- testing: 1 - 9223372036854775807 ---
          |int(-9223372036854775806)
          |--- testing: 1 - -9223372036854775808 ---
          |float(9.2233720368548E+18)
          |--- testing: 1 - 2147483647 ---
          |int(-2147483646)
          |--- testing: 1 - -2147483648 ---
          |int(2147483649)
          |--- testing: 1 - 9223372034707292160 ---
          |int(-9223372034707292159)
          |--- testing: 1 - -9223372034707292160 ---
          |int(9223372034707292161)
          |--- testing: 1 - 2147483648 ---
          |int(-2147483647)
          |--- testing: 1 - -2147483649 ---
          |int(2147483650)
          |--- testing: 1 - 4294967294 ---
          |int(-4294967293)
          |--- testing: 1 - 4294967295 ---
          |int(-4294967294)
          |--- testing: 1 - 4294967293 ---
          |int(-4294967292)
          |--- testing: 1 - 9223372036854775806 ---
          |int(-9223372036854775805)
          |--- testing: 1 - 9.2233720368548E+18 ---
          |float(-9.2233720368548E+18)
          |--- testing: 1 - -9223372036854775807 ---
          |float(9.2233720368548E+18)
          |--- testing: 1 - -9.2233720368548E+18 ---
          |float(9.2233720368548E+18)
          |--- testing: -1 - 9223372036854775807 ---
          |int(-9223372036854775808)
          |--- testing: -1 - -9223372036854775808 ---
          |int(9223372036854775807)
          |--- testing: -1 - 2147483647 ---
          |int(-2147483648)
          |--- testing: -1 - -2147483648 ---
          |int(2147483647)
          |--- testing: -1 - 9223372034707292160 ---
          |int(-9223372034707292161)
          |--- testing: -1 - -9223372034707292160 ---
          |int(9223372034707292159)
          |--- testing: -1 - 2147483648 ---
          |int(-2147483649)
          |--- testing: -1 - -2147483649 ---
          |int(2147483648)
          |--- testing: -1 - 4294967294 ---
          |int(-4294967295)
          |--- testing: -1 - 4294967295 ---
          |int(-4294967296)
          |--- testing: -1 - 4294967293 ---
          |int(-4294967294)
          |--- testing: -1 - 9223372036854775806 ---
          |int(-9223372036854775807)
          |--- testing: -1 - 9.2233720368548E+18 ---
          |float(-9.2233720368548E+18)
          |--- testing: -1 - -9223372036854775807 ---
          |int(9223372036854775806)
          |--- testing: -1 - -9.2233720368548E+18 ---
          |float(9.2233720368548E+18)
          |--- testing: 7 - 9223372036854775807 ---
          |int(-9223372036854775800)
          |--- testing: 7 - -9223372036854775808 ---
          |float(9.2233720368548E+18)
          |--- testing: 7 - 2147483647 ---
          |int(-2147483640)
          |--- testing: 7 - -2147483648 ---
          |int(2147483655)
          |--- testing: 7 - 9223372034707292160 ---
          |int(-9223372034707292153)
          |--- testing: 7 - -9223372034707292160 ---
          |int(9223372034707292167)
          |--- testing: 7 - 2147483648 ---
          |int(-2147483641)
          |--- testing: 7 - -2147483649 ---
          |int(2147483656)
          |--- testing: 7 - 4294967294 ---
          |int(-4294967287)
          |--- testing: 7 - 4294967295 ---
          |int(-4294967288)
          |--- testing: 7 - 4294967293 ---
          |int(-4294967286)
          |--- testing: 7 - 9223372036854775806 ---
          |int(-9223372036854775799)
          |--- testing: 7 - 9.2233720368548E+18 ---
          |float(-9.2233720368548E+18)
          |--- testing: 7 - -9223372036854775807 ---
          |float(9.2233720368548E+18)
          |--- testing: 7 - -9.2233720368548E+18 ---
          |float(9.2233720368548E+18)
          |--- testing: 9 - 9223372036854775807 ---
          |int(-9223372036854775798)
          |--- testing: 9 - -9223372036854775808 ---
          |float(9.2233720368548E+18)
          |--- testing: 9 - 2147483647 ---
          |int(-2147483638)
          |--- testing: 9 - -2147483648 ---
          |int(2147483657)
          |--- testing: 9 - 9223372034707292160 ---
          |int(-9223372034707292151)
          |--- testing: 9 - -9223372034707292160 ---
          |int(9223372034707292169)
          |--- testing: 9 - 2147483648 ---
          |int(-2147483639)
          |--- testing: 9 - -2147483649 ---
          |int(2147483658)
          |--- testing: 9 - 4294967294 ---
          |int(-4294967285)
          |--- testing: 9 - 4294967295 ---
          |int(-4294967286)
          |--- testing: 9 - 4294967293 ---
          |int(-4294967284)
          |--- testing: 9 - 9223372036854775806 ---
          |int(-9223372036854775797)
          |--- testing: 9 - 9.2233720368548E+18 ---
          |float(-9.2233720368548E+18)
          |--- testing: 9 - -9223372036854775807 ---
          |float(9.2233720368548E+18)
          |--- testing: 9 - -9.2233720368548E+18 ---
          |float(9.2233720368548E+18)
          |--- testing: 65 - 9223372036854775807 ---
          |int(-9223372036854775742)
          |--- testing: 65 - -9223372036854775808 ---
          |float(9.2233720368548E+18)
          |--- testing: 65 - 2147483647 ---
          |int(-2147483582)
          |--- testing: 65 - -2147483648 ---
          |int(2147483713)
          |--- testing: 65 - 9223372034707292160 ---
          |int(-9223372034707292095)
          |--- testing: 65 - -9223372034707292160 ---
          |int(9223372034707292225)
          |--- testing: 65 - 2147483648 ---
          |int(-2147483583)
          |--- testing: 65 - -2147483649 ---
          |int(2147483714)
          |--- testing: 65 - 4294967294 ---
          |int(-4294967229)
          |--- testing: 65 - 4294967295 ---
          |int(-4294967230)
          |--- testing: 65 - 4294967293 ---
          |int(-4294967228)
          |--- testing: 65 - 9223372036854775806 ---
          |int(-9223372036854775741)
          |--- testing: 65 - 9.2233720368548E+18 ---
          |float(-9.2233720368548E+18)
          |--- testing: 65 - -9223372036854775807 ---
          |float(9.2233720368548E+18)
          |--- testing: 65 - -9.2233720368548E+18 ---
          |float(9.2233720368548E+18)
          |--- testing: -44 - 9223372036854775807 ---
          |float(-9.2233720368548E+18)
          |--- testing: -44 - -9223372036854775808 ---
          |int(9223372036854775764)
          |--- testing: -44 - 2147483647 ---
          |int(-2147483691)
          |--- testing: -44 - -2147483648 ---
          |int(2147483604)
          |--- testing: -44 - 9223372034707292160 ---
          |int(-9223372034707292204)
          |--- testing: -44 - -9223372034707292160 ---
          |int(9223372034707292116)
          |--- testing: -44 - 2147483648 ---
          |int(-2147483692)
          |--- testing: -44 - -2147483649 ---
          |int(2147483605)
          |--- testing: -44 - 4294967294 ---
          |int(-4294967338)
          |--- testing: -44 - 4294967295 ---
          |int(-4294967339)
          |--- testing: -44 - 4294967293 ---
          |int(-4294967337)
          |--- testing: -44 - 9223372036854775806 ---
          |float(-9.2233720368548E+18)
          |--- testing: -44 - 9.2233720368548E+18 ---
          |float(-9.2233720368548E+18)
          |--- testing: -44 - -9223372036854775807 ---
          |int(9223372036854775763)
          |--- testing: -44 - -9.2233720368548E+18 ---
          |float(9.2233720368548E+18)
          |--- testing: 2147483647 - 9223372036854775807 ---
          |int(-9223372034707292160)
          |--- testing: 2147483647 - -9223372036854775808 ---
          |float(9.2233720390023E+18)
          |--- testing: 2147483647 - 2147483647 ---
          |int(0)
          |--- testing: 2147483647 - -2147483648 ---
          |int(4294967295)
          |--- testing: 2147483647 - 9223372034707292160 ---
          |int(-9223372032559808513)
          |--- testing: 2147483647 - -9223372034707292160 ---
          |int(9223372036854775807)
          |--- testing: 2147483647 - 2147483648 ---
          |int(-1)
          |--- testing: 2147483647 - -2147483649 ---
          |int(4294967296)
          |--- testing: 2147483647 - 4294967294 ---
          |int(-2147483647)
          |--- testing: 2147483647 - 4294967295 ---
          |int(-2147483648)
          |--- testing: 2147483647 - 4294967293 ---
          |int(-2147483646)
          |--- testing: 2147483647 - 9223372036854775806 ---
          |int(-9223372034707292159)
          |--- testing: 2147483647 - 9.2233720368548E+18 ---
          |float(-9.2233720347073E+18)
          |--- testing: 2147483647 - -9223372036854775807 ---
          |float(9.2233720390023E+18)
          |--- testing: 2147483647 - -9.2233720368548E+18 ---
          |float(9.2233720390023E+18)
          |--- testing: 9223372036854775807 - 9223372036854775807 ---
          |int(0)
          |--- testing: 9223372036854775807 - -9223372036854775808 ---
          |float(1.844674407371E+19)
          |--- testing: 9223372036854775807 - 2147483647 ---
          |int(9223372034707292160)
          |--- testing: 9223372036854775807 - -2147483648 ---
          |float(9.2233720390023E+18)
          |--- testing: 9223372036854775807 - 9223372034707292160 ---
          |int(2147483647)
          |--- testing: 9223372036854775807 - -9223372034707292160 ---
          |float(1.8446744071562E+19)
          |--- testing: 9223372036854775807 - 2147483648 ---
          |int(9223372034707292159)
          |--- testing: 9223372036854775807 - -2147483649 ---
          |float(9.2233720390023E+18)
          |--- testing: 9223372036854775807 - 4294967294 ---
          |int(9223372032559808513)
          |--- testing: 9223372036854775807 - 4294967295 ---
          |int(9223372032559808512)
          |--- testing: 9223372036854775807 - 4294967293 ---
          |int(9223372032559808514)
          |--- testing: 9223372036854775807 - 9223372036854775806 ---
          |int(1)
          |--- testing: 9223372036854775807 - 9.2233720368548E+18 ---
          |float(0)
          |--- testing: 9223372036854775807 - -9223372036854775807 ---
          |float(1.844674407371E+19)
          |--- testing: 9223372036854775807 - -9.2233720368548E+18 ---
          |float(1.844674407371E+19)
          |===DONE===""".stripMargin
      )
    }

    "Test - operator : various numbers as strings" in {
      // lang/operators/subtract_verationStr
      script(
        """<?php
          |
          |$strVals = array(
          |   "0","65","-44", "1.2", "-7.7", "abc", "123abc", "123e5", "123e5xyz", " 123abc", "123 abc", "123abc ", "3.4a",
          |   "a5.9"
          |);
          |
          |error_reporting(E_ERROR);
          |
          |foreach ($strVals as $strVal) {
          |   foreach($strVals as $otherVal) {
          |	   echo "--- testing: '$strVal' - '$otherVal' ---\n";
          |      var_dump($strVal-$otherVal);
          |   }
          |}
          |
          |
          |?>
          |===DONE===""".stripMargin
      ).result must haveOutput(
        """--- testing: '0' - '0' ---
          |int(0)
          |--- testing: '0' - '65' ---
          |int(-65)
          |--- testing: '0' - '-44' ---
          |int(44)
          |--- testing: '0' - '1.2' ---
          |float(-1.2)
          |--- testing: '0' - '-7.7' ---
          |float(7.7)
          |--- testing: '0' - 'abc' ---
          |int(0)
          |--- testing: '0' - '123abc' ---
          |int(-123)
          |--- testing: '0' - '123e5' ---
          |float(-12300000)
          |--- testing: '0' - '123e5xyz' ---
          |float(-12300000)
          |--- testing: '0' - ' 123abc' ---
          |int(-123)
          |--- testing: '0' - '123 abc' ---
          |int(-123)
          |--- testing: '0' - '123abc ' ---
          |int(-123)
          |--- testing: '0' - '3.4a' ---
          |float(-3.4)
          |--- testing: '0' - 'a5.9' ---
          |int(0)
          |--- testing: '65' - '0' ---
          |int(65)
          |--- testing: '65' - '65' ---
          |int(0)
          |--- testing: '65' - '-44' ---
          |int(109)
          |--- testing: '65' - '1.2' ---
          |float(63.8)
          |--- testing: '65' - '-7.7' ---
          |float(72.7)
          |--- testing: '65' - 'abc' ---
          |int(65)
          |--- testing: '65' - '123abc' ---
          |int(-58)
          |--- testing: '65' - '123e5' ---
          |float(-12299935)
          |--- testing: '65' - '123e5xyz' ---
          |float(-12299935)
          |--- testing: '65' - ' 123abc' ---
          |int(-58)
          |--- testing: '65' - '123 abc' ---
          |int(-58)
          |--- testing: '65' - '123abc ' ---
          |int(-58)
          |--- testing: '65' - '3.4a' ---
          |float(61.6)
          |--- testing: '65' - 'a5.9' ---
          |int(65)
          |--- testing: '-44' - '0' ---
          |int(-44)
          |--- testing: '-44' - '65' ---
          |int(-109)
          |--- testing: '-44' - '-44' ---
          |int(0)
          |--- testing: '-44' - '1.2' ---
          |float(-45.2)
          |--- testing: '-44' - '-7.7' ---
          |float(-36.3)
          |--- testing: '-44' - 'abc' ---
          |int(-44)
          |--- testing: '-44' - '123abc' ---
          |int(-167)
          |--- testing: '-44' - '123e5' ---
          |float(-12300044)
          |--- testing: '-44' - '123e5xyz' ---
          |float(-12300044)
          |--- testing: '-44' - ' 123abc' ---
          |int(-167)
          |--- testing: '-44' - '123 abc' ---
          |int(-167)
          |--- testing: '-44' - '123abc ' ---
          |int(-167)
          |--- testing: '-44' - '3.4a' ---
          |float(-47.4)
          |--- testing: '-44' - 'a5.9' ---
          |int(-44)
          |--- testing: '1.2' - '0' ---
          |float(1.2)
          |--- testing: '1.2' - '65' ---
          |float(-63.8)
          |--- testing: '1.2' - '-44' ---
          |float(45.2)
          |--- testing: '1.2' - '1.2' ---
          |float(0)
          |--- testing: '1.2' - '-7.7' ---
          |float(8.9)
          |--- testing: '1.2' - 'abc' ---
          |float(1.2)
          |--- testing: '1.2' - '123abc' ---
          |float(-121.8)
          |--- testing: '1.2' - '123e5' ---
          |float(-12299998.8)
          |--- testing: '1.2' - '123e5xyz' ---
          |float(-12299998.8)
          |--- testing: '1.2' - ' 123abc' ---
          |float(-121.8)
          |--- testing: '1.2' - '123 abc' ---
          |float(-121.8)
          |--- testing: '1.2' - '123abc ' ---
          |float(-121.8)
          |--- testing: '1.2' - '3.4a' ---
          |float(-2.2)
          |--- testing: '1.2' - 'a5.9' ---
          |float(1.2)
          |--- testing: '-7.7' - '0' ---
          |float(-7.7)
          |--- testing: '-7.7' - '65' ---
          |float(-72.7)
          |--- testing: '-7.7' - '-44' ---
          |float(36.3)
          |--- testing: '-7.7' - '1.2' ---
          |float(-8.9)
          |--- testing: '-7.7' - '-7.7' ---
          |float(0)
          |--- testing: '-7.7' - 'abc' ---
          |float(-7.7)
          |--- testing: '-7.7' - '123abc' ---
          |float(-130.7)
          |--- testing: '-7.7' - '123e5' ---
          |float(-12300007.7)
          |--- testing: '-7.7' - '123e5xyz' ---
          |float(-12300007.7)
          |--- testing: '-7.7' - ' 123abc' ---
          |float(-130.7)
          |--- testing: '-7.7' - '123 abc' ---
          |float(-130.7)
          |--- testing: '-7.7' - '123abc ' ---
          |float(-130.7)
          |--- testing: '-7.7' - '3.4a' ---
          |float(-11.1)
          |--- testing: '-7.7' - 'a5.9' ---
          |float(-7.7)
          |--- testing: 'abc' - '0' ---
          |int(0)
          |--- testing: 'abc' - '65' ---
          |int(-65)
          |--- testing: 'abc' - '-44' ---
          |int(44)
          |--- testing: 'abc' - '1.2' ---
          |float(-1.2)
          |--- testing: 'abc' - '-7.7' ---
          |float(7.7)
          |--- testing: 'abc' - 'abc' ---
          |int(0)
          |--- testing: 'abc' - '123abc' ---
          |int(-123)
          |--- testing: 'abc' - '123e5' ---
          |float(-12300000)
          |--- testing: 'abc' - '123e5xyz' ---
          |float(-12300000)
          |--- testing: 'abc' - ' 123abc' ---
          |int(-123)
          |--- testing: 'abc' - '123 abc' ---
          |int(-123)
          |--- testing: 'abc' - '123abc ' ---
          |int(-123)
          |--- testing: 'abc' - '3.4a' ---
          |float(-3.4)
          |--- testing: 'abc' - 'a5.9' ---
          |int(0)
          |--- testing: '123abc' - '0' ---
          |int(123)
          |--- testing: '123abc' - '65' ---
          |int(58)
          |--- testing: '123abc' - '-44' ---
          |int(167)
          |--- testing: '123abc' - '1.2' ---
          |float(121.8)
          |--- testing: '123abc' - '-7.7' ---
          |float(130.7)
          |--- testing: '123abc' - 'abc' ---
          |int(123)
          |--- testing: '123abc' - '123abc' ---
          |int(0)
          |--- testing: '123abc' - '123e5' ---
          |float(-12299877)
          |--- testing: '123abc' - '123e5xyz' ---
          |float(-12299877)
          |--- testing: '123abc' - ' 123abc' ---
          |int(0)
          |--- testing: '123abc' - '123 abc' ---
          |int(0)
          |--- testing: '123abc' - '123abc ' ---
          |int(0)
          |--- testing: '123abc' - '3.4a' ---
          |float(119.6)
          |--- testing: '123abc' - 'a5.9' ---
          |int(123)
          |--- testing: '123e5' - '0' ---
          |float(12300000)
          |--- testing: '123e5' - '65' ---
          |float(12299935)
          |--- testing: '123e5' - '-44' ---
          |float(12300044)
          |--- testing: '123e5' - '1.2' ---
          |float(12299998.8)
          |--- testing: '123e5' - '-7.7' ---
          |float(12300007.7)
          |--- testing: '123e5' - 'abc' ---
          |float(12300000)
          |--- testing: '123e5' - '123abc' ---
          |float(12299877)
          |--- testing: '123e5' - '123e5' ---
          |float(0)
          |--- testing: '123e5' - '123e5xyz' ---
          |float(0)
          |--- testing: '123e5' - ' 123abc' ---
          |float(12299877)
          |--- testing: '123e5' - '123 abc' ---
          |float(12299877)
          |--- testing: '123e5' - '123abc ' ---
          |float(12299877)
          |--- testing: '123e5' - '3.4a' ---
          |float(12299996.6)
          |--- testing: '123e5' - 'a5.9' ---
          |float(12300000)
          |--- testing: '123e5xyz' - '0' ---
          |float(12300000)
          |--- testing: '123e5xyz' - '65' ---
          |float(12299935)
          |--- testing: '123e5xyz' - '-44' ---
          |float(12300044)
          |--- testing: '123e5xyz' - '1.2' ---
          |float(12299998.8)
          |--- testing: '123e5xyz' - '-7.7' ---
          |float(12300007.7)
          |--- testing: '123e5xyz' - 'abc' ---
          |float(12300000)
          |--- testing: '123e5xyz' - '123abc' ---
          |float(12299877)
          |--- testing: '123e5xyz' - '123e5' ---
          |float(0)
          |--- testing: '123e5xyz' - '123e5xyz' ---
          |float(0)
          |--- testing: '123e5xyz' - ' 123abc' ---
          |float(12299877)
          |--- testing: '123e5xyz' - '123 abc' ---
          |float(12299877)
          |--- testing: '123e5xyz' - '123abc ' ---
          |float(12299877)
          |--- testing: '123e5xyz' - '3.4a' ---
          |float(12299996.6)
          |--- testing: '123e5xyz' - 'a5.9' ---
          |float(12300000)
          |--- testing: ' 123abc' - '0' ---
          |int(123)
          |--- testing: ' 123abc' - '65' ---
          |int(58)
          |--- testing: ' 123abc' - '-44' ---
          |int(167)
          |--- testing: ' 123abc' - '1.2' ---
          |float(121.8)
          |--- testing: ' 123abc' - '-7.7' ---
          |float(130.7)
          |--- testing: ' 123abc' - 'abc' ---
          |int(123)
          |--- testing: ' 123abc' - '123abc' ---
          |int(0)
          |--- testing: ' 123abc' - '123e5' ---
          |float(-12299877)
          |--- testing: ' 123abc' - '123e5xyz' ---
          |float(-12299877)
          |--- testing: ' 123abc' - ' 123abc' ---
          |int(0)
          |--- testing: ' 123abc' - '123 abc' ---
          |int(0)
          |--- testing: ' 123abc' - '123abc ' ---
          |int(0)
          |--- testing: ' 123abc' - '3.4a' ---
          |float(119.6)
          |--- testing: ' 123abc' - 'a5.9' ---
          |int(123)
          |--- testing: '123 abc' - '0' ---
          |int(123)
          |--- testing: '123 abc' - '65' ---
          |int(58)
          |--- testing: '123 abc' - '-44' ---
          |int(167)
          |--- testing: '123 abc' - '1.2' ---
          |float(121.8)
          |--- testing: '123 abc' - '-7.7' ---
          |float(130.7)
          |--- testing: '123 abc' - 'abc' ---
          |int(123)
          |--- testing: '123 abc' - '123abc' ---
          |int(0)
          |--- testing: '123 abc' - '123e5' ---
          |float(-12299877)
          |--- testing: '123 abc' - '123e5xyz' ---
          |float(-12299877)
          |--- testing: '123 abc' - ' 123abc' ---
          |int(0)
          |--- testing: '123 abc' - '123 abc' ---
          |int(0)
          |--- testing: '123 abc' - '123abc ' ---
          |int(0)
          |--- testing: '123 abc' - '3.4a' ---
          |float(119.6)
          |--- testing: '123 abc' - 'a5.9' ---
          |int(123)
          |--- testing: '123abc ' - '0' ---
          |int(123)
          |--- testing: '123abc ' - '65' ---
          |int(58)
          |--- testing: '123abc ' - '-44' ---
          |int(167)
          |--- testing: '123abc ' - '1.2' ---
          |float(121.8)
          |--- testing: '123abc ' - '-7.7' ---
          |float(130.7)
          |--- testing: '123abc ' - 'abc' ---
          |int(123)
          |--- testing: '123abc ' - '123abc' ---
          |int(0)
          |--- testing: '123abc ' - '123e5' ---
          |float(-12299877)
          |--- testing: '123abc ' - '123e5xyz' ---
          |float(-12299877)
          |--- testing: '123abc ' - ' 123abc' ---
          |int(0)
          |--- testing: '123abc ' - '123 abc' ---
          |int(0)
          |--- testing: '123abc ' - '123abc ' ---
          |int(0)
          |--- testing: '123abc ' - '3.4a' ---
          |float(119.6)
          |--- testing: '123abc ' - 'a5.9' ---
          |int(123)
          |--- testing: '3.4a' - '0' ---
          |float(3.4)
          |--- testing: '3.4a' - '65' ---
          |float(-61.6)
          |--- testing: '3.4a' - '-44' ---
          |float(47.4)
          |--- testing: '3.4a' - '1.2' ---
          |float(2.2)
          |--- testing: '3.4a' - '-7.7' ---
          |float(11.1)
          |--- testing: '3.4a' - 'abc' ---
          |float(3.4)
          |--- testing: '3.4a' - '123abc' ---
          |float(-119.6)
          |--- testing: '3.4a' - '123e5' ---
          |float(-12299996.6)
          |--- testing: '3.4a' - '123e5xyz' ---
          |float(-12299996.6)
          |--- testing: '3.4a' - ' 123abc' ---
          |float(-119.6)
          |--- testing: '3.4a' - '123 abc' ---
          |float(-119.6)
          |--- testing: '3.4a' - '123abc ' ---
          |float(-119.6)
          |--- testing: '3.4a' - '3.4a' ---
          |float(0)
          |--- testing: '3.4a' - 'a5.9' ---
          |float(3.4)
          |--- testing: 'a5.9' - '0' ---
          |int(0)
          |--- testing: 'a5.9' - '65' ---
          |int(-65)
          |--- testing: 'a5.9' - '-44' ---
          |int(44)
          |--- testing: 'a5.9' - '1.2' ---
          |float(-1.2)
          |--- testing: 'a5.9' - '-7.7' ---
          |float(7.7)
          |--- testing: 'a5.9' - 'abc' ---
          |int(0)
          |--- testing: 'a5.9' - '123abc' ---
          |int(-123)
          |--- testing: 'a5.9' - '123e5' ---
          |float(-12300000)
          |--- testing: 'a5.9' - '123e5xyz' ---
          |float(-12300000)
          |--- testing: 'a5.9' - ' 123abc' ---
          |int(-123)
          |--- testing: 'a5.9' - '123 abc' ---
          |int(-123)
          |--- testing: 'a5.9' - '123abc ' ---
          |int(-123)
          |--- testing: 'a5.9' - '3.4a' ---
          |float(-3.4)
          |--- testing: 'a5.9' - 'a5.9' ---
          |int(0)
          |===DONE===""".stripMargin
      )
    }
  }
}