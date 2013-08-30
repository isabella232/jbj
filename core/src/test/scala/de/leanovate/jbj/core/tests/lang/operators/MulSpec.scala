/*    _ _     _                                        *\
**   (_) |__ (_)  License: MIT  (2013)                 **
**   | |  _ \| |    http://opensource.org/licenses/MIT **
**   | | |_) | |                                       **
**  _/ |____// |  Author: Bodo Junglas                 **
\* |__/    |__/                 (Tests based on PHP)   */

package de.leanovate.jbj.core.tests.lang.operators

import org.specs2.mutable.SpecificationWithJUnit
import de.leanovate.jbj.core.tests.TestJbjExecutor

class MulSpec extends SpecificationWithJUnit with TestJbjExecutor {
  "* operator" should {
    "Test * operator : 64bit long tests" in {
      // lang/operators/multiply_basiclong_64bit.phpt
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
          |	   echo "--- testing: $longVal * $otherVal ---\n";
          |      var_dump($longVal*$otherVal);
          |   }
          |}
          |
          |foreach ($otherVals as $otherVal) {
          |   foreach($longVals as $longVal) {
          |	   echo "--- testing: $otherVal * $longVal ---\n";
          |      var_dump($otherVal*$longVal);
          |   }
          |}
          |
          |?>
          |===DONE===
          |""".stripMargin
      ).result must haveOutput(
        """--- testing: 9223372036854775807 * 0 ---
          |int(0)
          |--- testing: 9223372036854775807 * 1 ---
          |int(9223372036854775807)
          |--- testing: 9223372036854775807 * -1 ---
          |int(-9223372036854775807)
          |--- testing: 9223372036854775807 * 7 ---
          |float(6.4563604257983E+19)
          |--- testing: 9223372036854775807 * 9 ---
          |float(8.3010348331693E+19)
          |--- testing: 9223372036854775807 * 65 ---
          |float(5.9951918239556E+20)
          |--- testing: 9223372036854775807 * -44 ---
          |float(-4.0582836962161E+20)
          |--- testing: 9223372036854775807 * 2147483647 ---
          |float(1.9807040619343E+28)
          |--- testing: 9223372036854775807 * 9223372036854775807 ---
          |float(8.5070591730235E+37)
          |--- testing: -9223372036854775808 * 0 ---
          |int(0)
          |--- testing: -9223372036854775808 * 1 ---
          |int(-9223372036854775808)
          |--- testing: -9223372036854775808 * -1 ---
          |float(9.2233720368548E+18)
          |--- testing: -9223372036854775808 * 7 ---
          |float(-6.4563604257983E+19)
          |--- testing: -9223372036854775808 * 9 ---
          |float(-8.3010348331693E+19)
          |--- testing: -9223372036854775808 * 65 ---
          |float(-5.9951918239556E+20)
          |--- testing: -9223372036854775808 * -44 ---
          |float(4.0582836962161E+20)
          |--- testing: -9223372036854775808 * 2147483647 ---
          |float(-1.9807040619343E+28)
          |--- testing: -9223372036854775808 * 9223372036854775807 ---
          |float(-8.5070591730235E+37)
          |--- testing: 2147483647 * 0 ---
          |int(0)
          |--- testing: 2147483647 * 1 ---
          |int(2147483647)
          |--- testing: 2147483647 * -1 ---
          |int(-2147483647)
          |--- testing: 2147483647 * 7 ---
          |int(15032385529)
          |--- testing: 2147483647 * 9 ---
          |int(19327352823)
          |--- testing: 2147483647 * 65 ---
          |int(139586437055)
          |--- testing: 2147483647 * -44 ---
          |int(-94489280468)
          |--- testing: 2147483647 * 2147483647 ---
          |int(4611686014132420609)
          |--- testing: 2147483647 * 9223372036854775807 ---
          |float(1.9807040619343E+28)
          |--- testing: -2147483648 * 0 ---
          |int(0)
          |--- testing: -2147483648 * 1 ---
          |int(-2147483648)
          |--- testing: -2147483648 * -1 ---
          |int(2147483648)
          |--- testing: -2147483648 * 7 ---
          |int(-15032385536)
          |--- testing: -2147483648 * 9 ---
          |int(-19327352832)
          |--- testing: -2147483648 * 65 ---
          |int(-139586437120)
          |--- testing: -2147483648 * -44 ---
          |int(94489280512)
          |--- testing: -2147483648 * 2147483647 ---
          |int(-4611686016279904256)
          |--- testing: -2147483648 * 9223372036854775807 ---
          |float(-1.9807040628566E+28)
          |--- testing: 9223372034707292160 * 0 ---
          |int(0)
          |--- testing: 9223372034707292160 * 1 ---
          |int(9223372034707292160)
          |--- testing: 9223372034707292160 * -1 ---
          |int(-9223372034707292160)
          |--- testing: 9223372034707292160 * 7 ---
          |float(6.4563604242951E+19)
          |--- testing: 9223372034707292160 * 9 ---
          |float(8.3010348312366E+19)
          |--- testing: 9223372034707292160 * 65 ---
          |float(5.9951918225597E+20)
          |--- testing: 9223372034707292160 * -44 ---
          |float(-4.0582836952712E+20)
          |--- testing: 9223372034707292160 * 2147483647 ---
          |float(1.9807040614731E+28)
          |--- testing: 9223372034707292160 * 9223372036854775807 ---
          |float(8.5070591710428E+37)
          |--- testing: -9223372034707292160 * 0 ---
          |int(0)
          |--- testing: -9223372034707292160 * 1 ---
          |int(-9223372034707292160)
          |--- testing: -9223372034707292160 * -1 ---
          |int(9223372034707292160)
          |--- testing: -9223372034707292160 * 7 ---
          |float(-6.4563604242951E+19)
          |--- testing: -9223372034707292160 * 9 ---
          |float(-8.3010348312366E+19)
          |--- testing: -9223372034707292160 * 65 ---
          |float(-5.9951918225597E+20)
          |--- testing: -9223372034707292160 * -44 ---
          |float(4.0582836952712E+20)
          |--- testing: -9223372034707292160 * 2147483647 ---
          |float(-1.9807040614731E+28)
          |--- testing: -9223372034707292160 * 9223372036854775807 ---
          |float(-8.5070591710428E+37)
          |--- testing: 2147483648 * 0 ---
          |int(0)
          |--- testing: 2147483648 * 1 ---
          |int(2147483648)
          |--- testing: 2147483648 * -1 ---
          |int(-2147483648)
          |--- testing: 2147483648 * 7 ---
          |int(15032385536)
          |--- testing: 2147483648 * 9 ---
          |int(19327352832)
          |--- testing: 2147483648 * 65 ---
          |int(139586437120)
          |--- testing: 2147483648 * -44 ---
          |int(-94489280512)
          |--- testing: 2147483648 * 2147483647 ---
          |int(4611686016279904256)
          |--- testing: 2147483648 * 9223372036854775807 ---
          |float(1.9807040628566E+28)
          |--- testing: -2147483649 * 0 ---
          |int(0)
          |--- testing: -2147483649 * 1 ---
          |int(-2147483649)
          |--- testing: -2147483649 * -1 ---
          |int(2147483649)
          |--- testing: -2147483649 * 7 ---
          |int(-15032385543)
          |--- testing: -2147483649 * 9 ---
          |int(-19327352841)
          |--- testing: -2147483649 * 65 ---
          |int(-139586437185)
          |--- testing: -2147483649 * -44 ---
          |int(94489280556)
          |--- testing: -2147483649 * 2147483647 ---
          |int(-4611686018427387903)
          |--- testing: -2147483649 * 9223372036854775807 ---
          |float(-1.9807040637789E+28)
          |--- testing: 4294967294 * 0 ---
          |int(0)
          |--- testing: 4294967294 * 1 ---
          |int(4294967294)
          |--- testing: 4294967294 * -1 ---
          |int(-4294967294)
          |--- testing: 4294967294 * 7 ---
          |int(30064771058)
          |--- testing: 4294967294 * 9 ---
          |int(38654705646)
          |--- testing: 4294967294 * 65 ---
          |int(279172874110)
          |--- testing: 4294967294 * -44 ---
          |int(-188978560936)
          |--- testing: 4294967294 * 2147483647 ---
          |int(9223372028264841218)
          |--- testing: 4294967294 * 9223372036854775807 ---
          |float(3.9614081238685E+28)
          |--- testing: 4294967295 * 0 ---
          |int(0)
          |--- testing: 4294967295 * 1 ---
          |int(4294967295)
          |--- testing: 4294967295 * -1 ---
          |int(-4294967295)
          |--- testing: 4294967295 * 7 ---
          |int(30064771065)
          |--- testing: 4294967295 * 9 ---
          |int(38654705655)
          |--- testing: 4294967295 * 65 ---
          |int(279172874175)
          |--- testing: 4294967295 * -44 ---
          |int(-188978560980)
          |--- testing: 4294967295 * 2147483647 ---
          |int(9223372030412324865)
          |--- testing: 4294967295 * 9223372036854775807 ---
          |float(3.9614081247909E+28)
          |--- testing: 4294967293 * 0 ---
          |int(0)
          |--- testing: 4294967293 * 1 ---
          |int(4294967293)
          |--- testing: 4294967293 * -1 ---
          |int(-4294967293)
          |--- testing: 4294967293 * 7 ---
          |int(30064771051)
          |--- testing: 4294967293 * 9 ---
          |int(38654705637)
          |--- testing: 4294967293 * 65 ---
          |int(279172874045)
          |--- testing: 4294967293 * -44 ---
          |int(-188978560892)
          |--- testing: 4294967293 * 2147483647 ---
          |int(9223372026117357571)
          |--- testing: 4294967293 * 9223372036854775807 ---
          |float(3.9614081229462E+28)
          |--- testing: 9223372036854775806 * 0 ---
          |int(0)
          |--- testing: 9223372036854775806 * 1 ---
          |int(9223372036854775806)
          |--- testing: 9223372036854775806 * -1 ---
          |int(-9223372036854775806)
          |--- testing: 9223372036854775806 * 7 ---
          |float(6.4563604257983E+19)
          |--- testing: 9223372036854775806 * 9 ---
          |float(8.3010348331693E+19)
          |--- testing: 9223372036854775806 * 65 ---
          |float(5.9951918239556E+20)
          |--- testing: 9223372036854775806 * -44 ---
          |float(-4.0582836962161E+20)
          |--- testing: 9223372036854775806 * 2147483647 ---
          |float(1.9807040619343E+28)
          |--- testing: 9223372036854775806 * 9223372036854775807 ---
          |float(8.5070591730235E+37)
          |--- testing: 9.2233720368548E+18 * 0 ---
          |float(0)
          |--- testing: 9.2233720368548E+18 * 1 ---
          |float(9.2233720368548E+18)
          |--- testing: 9.2233720368548E+18 * -1 ---
          |float(-9.2233720368548E+18)
          |--- testing: 9.2233720368548E+18 * 7 ---
          |float(6.4563604257983E+19)
          |--- testing: 9.2233720368548E+18 * 9 ---
          |float(8.3010348331693E+19)
          |--- testing: 9.2233720368548E+18 * 65 ---
          |float(5.9951918239556E+20)
          |--- testing: 9.2233720368548E+18 * -44 ---
          |float(-4.0582836962161E+20)
          |--- testing: 9.2233720368548E+18 * 2147483647 ---
          |float(1.9807040619343E+28)
          |--- testing: 9.2233720368548E+18 * 9223372036854775807 ---
          |float(8.5070591730235E+37)
          |--- testing: -9223372036854775807 * 0 ---
          |int(0)
          |--- testing: -9223372036854775807 * 1 ---
          |int(-9223372036854775807)
          |--- testing: -9223372036854775807 * -1 ---
          |int(9223372036854775807)
          |--- testing: -9223372036854775807 * 7 ---
          |float(-6.4563604257983E+19)
          |--- testing: -9223372036854775807 * 9 ---
          |float(-8.3010348331693E+19)
          |--- testing: -9223372036854775807 * 65 ---
          |float(-5.9951918239556E+20)
          |--- testing: -9223372036854775807 * -44 ---
          |float(4.0582836962161E+20)
          |--- testing: -9223372036854775807 * 2147483647 ---
          |float(-1.9807040619343E+28)
          |--- testing: -9223372036854775807 * 9223372036854775807 ---
          |float(-8.5070591730235E+37)
          |--- testing: -9.2233720368548E+18 * 0 ---
          |float(0)
          |--- testing: -9.2233720368548E+18 * 1 ---
          |float(-9.2233720368548E+18)
          |--- testing: -9.2233720368548E+18 * -1 ---
          |float(9.2233720368548E+18)
          |--- testing: -9.2233720368548E+18 * 7 ---
          |float(-6.4563604257983E+19)
          |--- testing: -9.2233720368548E+18 * 9 ---
          |float(-8.3010348331693E+19)
          |--- testing: -9.2233720368548E+18 * 65 ---
          |float(-5.9951918239556E+20)
          |--- testing: -9.2233720368548E+18 * -44 ---
          |float(4.0582836962161E+20)
          |--- testing: -9.2233720368548E+18 * 2147483647 ---
          |float(-1.9807040619343E+28)
          |--- testing: -9.2233720368548E+18 * 9223372036854775807 ---
          |float(-8.5070591730235E+37)
          |--- testing: 0 * 9223372036854775807 ---
          |int(0)
          |--- testing: 0 * -9223372036854775808 ---
          |int(0)
          |--- testing: 0 * 2147483647 ---
          |int(0)
          |--- testing: 0 * -2147483648 ---
          |int(0)
          |--- testing: 0 * 9223372034707292160 ---
          |int(0)
          |--- testing: 0 * -9223372034707292160 ---
          |int(0)
          |--- testing: 0 * 2147483648 ---
          |int(0)
          |--- testing: 0 * -2147483649 ---
          |int(0)
          |--- testing: 0 * 4294967294 ---
          |int(0)
          |--- testing: 0 * 4294967295 ---
          |int(0)
          |--- testing: 0 * 4294967293 ---
          |int(0)
          |--- testing: 0 * 9223372036854775806 ---
          |int(0)
          |--- testing: 0 * 9.2233720368548E+18 ---
          |float(0)
          |--- testing: 0 * -9223372036854775807 ---
          |int(0)
          |--- testing: 0 * -9.2233720368548E+18 ---
          |float(0)
          |--- testing: 1 * 9223372036854775807 ---
          |int(9223372036854775807)
          |--- testing: 1 * -9223372036854775808 ---
          |int(-9223372036854775808)
          |--- testing: 1 * 2147483647 ---
          |int(2147483647)
          |--- testing: 1 * -2147483648 ---
          |int(-2147483648)
          |--- testing: 1 * 9223372034707292160 ---
          |int(9223372034707292160)
          |--- testing: 1 * -9223372034707292160 ---
          |int(-9223372034707292160)
          |--- testing: 1 * 2147483648 ---
          |int(2147483648)
          |--- testing: 1 * -2147483649 ---
          |int(-2147483649)
          |--- testing: 1 * 4294967294 ---
          |int(4294967294)
          |--- testing: 1 * 4294967295 ---
          |int(4294967295)
          |--- testing: 1 * 4294967293 ---
          |int(4294967293)
          |--- testing: 1 * 9223372036854775806 ---
          |int(9223372036854775806)
          |--- testing: 1 * 9.2233720368548E+18 ---
          |float(9.2233720368548E+18)
          |--- testing: 1 * -9223372036854775807 ---
          |int(-9223372036854775807)
          |--- testing: 1 * -9.2233720368548E+18 ---
          |float(-9.2233720368548E+18)
          |--- testing: -1 * 9223372036854775807 ---
          |int(-9223372036854775807)
          |--- testing: -1 * -9223372036854775808 ---
          |float(9.2233720368548E+18)
          |--- testing: -1 * 2147483647 ---
          |int(-2147483647)
          |--- testing: -1 * -2147483648 ---
          |int(2147483648)
          |--- testing: -1 * 9223372034707292160 ---
          |int(-9223372034707292160)
          |--- testing: -1 * -9223372034707292160 ---
          |int(9223372034707292160)
          |--- testing: -1 * 2147483648 ---
          |int(-2147483648)
          |--- testing: -1 * -2147483649 ---
          |int(2147483649)
          |--- testing: -1 * 4294967294 ---
          |int(-4294967294)
          |--- testing: -1 * 4294967295 ---
          |int(-4294967295)
          |--- testing: -1 * 4294967293 ---
          |int(-4294967293)
          |--- testing: -1 * 9223372036854775806 ---
          |int(-9223372036854775806)
          |--- testing: -1 * 9.2233720368548E+18 ---
          |float(-9.2233720368548E+18)
          |--- testing: -1 * -9223372036854775807 ---
          |int(9223372036854775807)
          |--- testing: -1 * -9.2233720368548E+18 ---
          |float(9.2233720368548E+18)
          |--- testing: 7 * 9223372036854775807 ---
          |float(6.4563604257983E+19)
          |--- testing: 7 * -9223372036854775808 ---
          |float(-6.4563604257983E+19)
          |--- testing: 7 * 2147483647 ---
          |int(15032385529)
          |--- testing: 7 * -2147483648 ---
          |int(-15032385536)
          |--- testing: 7 * 9223372034707292160 ---
          |float(6.4563604242951E+19)
          |--- testing: 7 * -9223372034707292160 ---
          |float(-6.4563604242951E+19)
          |--- testing: 7 * 2147483648 ---
          |int(15032385536)
          |--- testing: 7 * -2147483649 ---
          |int(-15032385543)
          |--- testing: 7 * 4294967294 ---
          |int(30064771058)
          |--- testing: 7 * 4294967295 ---
          |int(30064771065)
          |--- testing: 7 * 4294967293 ---
          |int(30064771051)
          |--- testing: 7 * 9223372036854775806 ---
          |float(6.4563604257983E+19)
          |--- testing: 7 * 9.2233720368548E+18 ---
          |float(6.4563604257983E+19)
          |--- testing: 7 * -9223372036854775807 ---
          |float(-6.4563604257983E+19)
          |--- testing: 7 * -9.2233720368548E+18 ---
          |float(-6.4563604257983E+19)
          |--- testing: 9 * 9223372036854775807 ---
          |float(8.3010348331693E+19)
          |--- testing: 9 * -9223372036854775808 ---
          |float(-8.3010348331693E+19)
          |--- testing: 9 * 2147483647 ---
          |int(19327352823)
          |--- testing: 9 * -2147483648 ---
          |int(-19327352832)
          |--- testing: 9 * 9223372034707292160 ---
          |float(8.3010348312366E+19)
          |--- testing: 9 * -9223372034707292160 ---
          |float(-8.3010348312366E+19)
          |--- testing: 9 * 2147483648 ---
          |int(19327352832)
          |--- testing: 9 * -2147483649 ---
          |int(-19327352841)
          |--- testing: 9 * 4294967294 ---
          |int(38654705646)
          |--- testing: 9 * 4294967295 ---
          |int(38654705655)
          |--- testing: 9 * 4294967293 ---
          |int(38654705637)
          |--- testing: 9 * 9223372036854775806 ---
          |float(8.3010348331693E+19)
          |--- testing: 9 * 9.2233720368548E+18 ---
          |float(8.3010348331693E+19)
          |--- testing: 9 * -9223372036854775807 ---
          |float(-8.3010348331693E+19)
          |--- testing: 9 * -9.2233720368548E+18 ---
          |float(-8.3010348331693E+19)
          |--- testing: 65 * 9223372036854775807 ---
          |float(5.9951918239556E+20)
          |--- testing: 65 * -9223372036854775808 ---
          |float(-5.9951918239556E+20)
          |--- testing: 65 * 2147483647 ---
          |int(139586437055)
          |--- testing: 65 * -2147483648 ---
          |int(-139586437120)
          |--- testing: 65 * 9223372034707292160 ---
          |float(5.9951918225597E+20)
          |--- testing: 65 * -9223372034707292160 ---
          |float(-5.9951918225597E+20)
          |--- testing: 65 * 2147483648 ---
          |int(139586437120)
          |--- testing: 65 * -2147483649 ---
          |int(-139586437185)
          |--- testing: 65 * 4294967294 ---
          |int(279172874110)
          |--- testing: 65 * 4294967295 ---
          |int(279172874175)
          |--- testing: 65 * 4294967293 ---
          |int(279172874045)
          |--- testing: 65 * 9223372036854775806 ---
          |float(5.9951918239556E+20)
          |--- testing: 65 * 9.2233720368548E+18 ---
          |float(5.9951918239556E+20)
          |--- testing: 65 * -9223372036854775807 ---
          |float(-5.9951918239556E+20)
          |--- testing: 65 * -9.2233720368548E+18 ---
          |float(-5.9951918239556E+20)
          |--- testing: -44 * 9223372036854775807 ---
          |float(-4.0582836962161E+20)
          |--- testing: -44 * -9223372036854775808 ---
          |float(4.0582836962161E+20)
          |--- testing: -44 * 2147483647 ---
          |int(-94489280468)
          |--- testing: -44 * -2147483648 ---
          |int(94489280512)
          |--- testing: -44 * 9223372034707292160 ---
          |float(-4.0582836952712E+20)
          |--- testing: -44 * -9223372034707292160 ---
          |float(4.0582836952712E+20)
          |--- testing: -44 * 2147483648 ---
          |int(-94489280512)
          |--- testing: -44 * -2147483649 ---
          |int(94489280556)
          |--- testing: -44 * 4294967294 ---
          |int(-188978560936)
          |--- testing: -44 * 4294967295 ---
          |int(-188978560980)
          |--- testing: -44 * 4294967293 ---
          |int(-188978560892)
          |--- testing: -44 * 9223372036854775806 ---
          |float(-4.0582836962161E+20)
          |--- testing: -44 * 9.2233720368548E+18 ---
          |float(-4.0582836962161E+20)
          |--- testing: -44 * -9223372036854775807 ---
          |float(4.0582836962161E+20)
          |--- testing: -44 * -9.2233720368548E+18 ---
          |float(4.0582836962161E+20)
          |--- testing: 2147483647 * 9223372036854775807 ---
          |float(1.9807040619343E+28)
          |--- testing: 2147483647 * -9223372036854775808 ---
          |float(-1.9807040619343E+28)
          |--- testing: 2147483647 * 2147483647 ---
          |int(4611686014132420609)
          |--- testing: 2147483647 * -2147483648 ---
          |int(-4611686016279904256)
          |--- testing: 2147483647 * 9223372034707292160 ---
          |float(1.9807040614731E+28)
          |--- testing: 2147483647 * -9223372034707292160 ---
          |float(-1.9807040614731E+28)
          |--- testing: 2147483647 * 2147483648 ---
          |int(4611686016279904256)
          |--- testing: 2147483647 * -2147483649 ---
          |int(-4611686018427387903)
          |--- testing: 2147483647 * 4294967294 ---
          |int(9223372028264841218)
          |--- testing: 2147483647 * 4294967295 ---
          |int(9223372030412324865)
          |--- testing: 2147483647 * 4294967293 ---
          |int(9223372026117357571)
          |--- testing: 2147483647 * 9223372036854775806 ---
          |float(1.9807040619343E+28)
          |--- testing: 2147483647 * 9.2233720368548E+18 ---
          |float(1.9807040619343E+28)
          |--- testing: 2147483647 * -9223372036854775807 ---
          |float(-1.9807040619343E+28)
          |--- testing: 2147483647 * -9.2233720368548E+18 ---
          |float(-1.9807040619343E+28)
          |--- testing: 9223372036854775807 * 9223372036854775807 ---
          |float(8.5070591730235E+37)
          |--- testing: 9223372036854775807 * -9223372036854775808 ---
          |float(-8.5070591730235E+37)
          |--- testing: 9223372036854775807 * 2147483647 ---
          |float(1.9807040619343E+28)
          |--- testing: 9223372036854775807 * -2147483648 ---
          |float(-1.9807040628566E+28)
          |--- testing: 9223372036854775807 * 9223372034707292160 ---
          |float(8.5070591710428E+37)
          |--- testing: 9223372036854775807 * -9223372034707292160 ---
          |float(-8.5070591710428E+37)
          |--- testing: 9223372036854775807 * 2147483648 ---
          |float(1.9807040628566E+28)
          |--- testing: 9223372036854775807 * -2147483649 ---
          |float(-1.9807040637789E+28)
          |--- testing: 9223372036854775807 * 4294967294 ---
          |float(3.9614081238685E+28)
          |--- testing: 9223372036854775807 * 4294967295 ---
          |float(3.9614081247909E+28)
          |--- testing: 9223372036854775807 * 4294967293 ---
          |float(3.9614081229462E+28)
          |--- testing: 9223372036854775807 * 9223372036854775806 ---
          |float(8.5070591730235E+37)
          |--- testing: 9223372036854775807 * 9.2233720368548E+18 ---
          |float(8.5070591730235E+37)
          |--- testing: 9223372036854775807 * -9223372036854775807 ---
          |float(-8.5070591730235E+37)
          |--- testing: 9223372036854775807 * -9.2233720368548E+18 ---
          |float(-8.5070591730235E+37)
          |===DONE===
          |""".stripMargin
      )
    }

    "Test * operator : various numbers as strings" in {
      // lang/operators/multiply_variationStr.phpt
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
          |	   echo "--- testing: '$strVal' * '$otherVal' ---\n";
          |      var_dump($strVal*$otherVal);
          |   }
          |}
          |
          |
          |?>
          |===DONE===
          |""".stripMargin
      ).result must haveOutput(
        """--- testing: '0' * '0' ---
          |int(0)
          |--- testing: '0' * '65' ---
          |int(0)
          |--- testing: '0' * '-44' ---
          |int(0)
          |--- testing: '0' * '1.2' ---
          |float(0)
          |--- testing: '0' * '-7.7' ---
          |float(0)
          |--- testing: '0' * 'abc' ---
          |int(0)
          |--- testing: '0' * '123abc' ---
          |int(0)
          |--- testing: '0' * '123e5' ---
          |float(0)
          |--- testing: '0' * '123e5xyz' ---
          |float(0)
          |--- testing: '0' * ' 123abc' ---
          |int(0)
          |--- testing: '0' * '123 abc' ---
          |int(0)
          |--- testing: '0' * '123abc ' ---
          |int(0)
          |--- testing: '0' * '3.4a' ---
          |float(0)
          |--- testing: '0' * 'a5.9' ---
          |int(0)
          |--- testing: '65' * '0' ---
          |int(0)
          |--- testing: '65' * '65' ---
          |int(4225)
          |--- testing: '65' * '-44' ---
          |int(-2860)
          |--- testing: '65' * '1.2' ---
          |float(78)
          |--- testing: '65' * '-7.7' ---
          |float(-500.5)
          |--- testing: '65' * 'abc' ---
          |int(0)
          |--- testing: '65' * '123abc' ---
          |int(7995)
          |--- testing: '65' * '123e5' ---
          |float(799500000)
          |--- testing: '65' * '123e5xyz' ---
          |float(799500000)
          |--- testing: '65' * ' 123abc' ---
          |int(7995)
          |--- testing: '65' * '123 abc' ---
          |int(7995)
          |--- testing: '65' * '123abc ' ---
          |int(7995)
          |--- testing: '65' * '3.4a' ---
          |float(221)
          |--- testing: '65' * 'a5.9' ---
          |int(0)
          |--- testing: '-44' * '0' ---
          |int(0)
          |--- testing: '-44' * '65' ---
          |int(-2860)
          |--- testing: '-44' * '-44' ---
          |int(1936)
          |--- testing: '-44' * '1.2' ---
          |float(-52.8)
          |--- testing: '-44' * '-7.7' ---
          |float(338.8)
          |--- testing: '-44' * 'abc' ---
          |int(0)
          |--- testing: '-44' * '123abc' ---
          |int(-5412)
          |--- testing: '-44' * '123e5' ---
          |float(-541200000)
          |--- testing: '-44' * '123e5xyz' ---
          |float(-541200000)
          |--- testing: '-44' * ' 123abc' ---
          |int(-5412)
          |--- testing: '-44' * '123 abc' ---
          |int(-5412)
          |--- testing: '-44' * '123abc ' ---
          |int(-5412)
          |--- testing: '-44' * '3.4a' ---
          |float(-149.6)
          |--- testing: '-44' * 'a5.9' ---
          |int(0)
          |--- testing: '1.2' * '0' ---
          |float(0)
          |--- testing: '1.2' * '65' ---
          |float(78)
          |--- testing: '1.2' * '-44' ---
          |float(-52.8)
          |--- testing: '1.2' * '1.2' ---
          |float(1.44)
          |--- testing: '1.2' * '-7.7' ---
          |float(-9.24)
          |--- testing: '1.2' * 'abc' ---
          |float(0)
          |--- testing: '1.2' * '123abc' ---
          |float(147.6)
          |--- testing: '1.2' * '123e5' ---
          |float(14760000)
          |--- testing: '1.2' * '123e5xyz' ---
          |float(14760000)
          |--- testing: '1.2' * ' 123abc' ---
          |float(147.6)
          |--- testing: '1.2' * '123 abc' ---
          |float(147.6)
          |--- testing: '1.2' * '123abc ' ---
          |float(147.6)
          |--- testing: '1.2' * '3.4a' ---
          |float(4.08)
          |--- testing: '1.2' * 'a5.9' ---
          |float(0)
          |--- testing: '-7.7' * '0' ---
          |float(0)
          |--- testing: '-7.7' * '65' ---
          |float(-500.5)
          |--- testing: '-7.7' * '-44' ---
          |float(338.8)
          |--- testing: '-7.7' * '1.2' ---
          |float(-9.24)
          |--- testing: '-7.7' * '-7.7' ---
          |float(59.29)
          |--- testing: '-7.7' * 'abc' ---
          |float(0)
          |--- testing: '-7.7' * '123abc' ---
          |float(-947.1)
          |--- testing: '-7.7' * '123e5' ---
          |float(-94710000)
          |--- testing: '-7.7' * '123e5xyz' ---
          |float(-94710000)
          |--- testing: '-7.7' * ' 123abc' ---
          |float(-947.1)
          |--- testing: '-7.7' * '123 abc' ---
          |float(-947.1)
          |--- testing: '-7.7' * '123abc ' ---
          |float(-947.1)
          |--- testing: '-7.7' * '3.4a' ---
          |float(-26.18)
          |--- testing: '-7.7' * 'a5.9' ---
          |float(0)
          |--- testing: 'abc' * '0' ---
          |int(0)
          |--- testing: 'abc' * '65' ---
          |int(0)
          |--- testing: 'abc' * '-44' ---
          |int(0)
          |--- testing: 'abc' * '1.2' ---
          |float(0)
          |--- testing: 'abc' * '-7.7' ---
          |float(0)
          |--- testing: 'abc' * 'abc' ---
          |int(0)
          |--- testing: 'abc' * '123abc' ---
          |int(0)
          |--- testing: 'abc' * '123e5' ---
          |float(0)
          |--- testing: 'abc' * '123e5xyz' ---
          |float(0)
          |--- testing: 'abc' * ' 123abc' ---
          |int(0)
          |--- testing: 'abc' * '123 abc' ---
          |int(0)
          |--- testing: 'abc' * '123abc ' ---
          |int(0)
          |--- testing: 'abc' * '3.4a' ---
          |float(0)
          |--- testing: 'abc' * 'a5.9' ---
          |int(0)
          |--- testing: '123abc' * '0' ---
          |int(0)
          |--- testing: '123abc' * '65' ---
          |int(7995)
          |--- testing: '123abc' * '-44' ---
          |int(-5412)
          |--- testing: '123abc' * '1.2' ---
          |float(147.6)
          |--- testing: '123abc' * '-7.7' ---
          |float(-947.1)
          |--- testing: '123abc' * 'abc' ---
          |int(0)
          |--- testing: '123abc' * '123abc' ---
          |int(15129)
          |--- testing: '123abc' * '123e5' ---
          |float(1512900000)
          |--- testing: '123abc' * '123e5xyz' ---
          |float(1512900000)
          |--- testing: '123abc' * ' 123abc' ---
          |int(15129)
          |--- testing: '123abc' * '123 abc' ---
          |int(15129)
          |--- testing: '123abc' * '123abc ' ---
          |int(15129)
          |--- testing: '123abc' * '3.4a' ---
          |float(418.2)
          |--- testing: '123abc' * 'a5.9' ---
          |int(0)
          |--- testing: '123e5' * '0' ---
          |float(0)
          |--- testing: '123e5' * '65' ---
          |float(799500000)
          |--- testing: '123e5' * '-44' ---
          |float(-541200000)
          |--- testing: '123e5' * '1.2' ---
          |float(14760000)
          |--- testing: '123e5' * '-7.7' ---
          |float(-94710000)
          |--- testing: '123e5' * 'abc' ---
          |float(0)
          |--- testing: '123e5' * '123abc' ---
          |float(1512900000)
          |--- testing: '123e5' * '123e5' ---
          |float(1.5129E+14)
          |--- testing: '123e5' * '123e5xyz' ---
          |float(1.5129E+14)
          |--- testing: '123e5' * ' 123abc' ---
          |float(1512900000)
          |--- testing: '123e5' * '123 abc' ---
          |float(1512900000)
          |--- testing: '123e5' * '123abc ' ---
          |float(1512900000)
          |--- testing: '123e5' * '3.4a' ---
          |float(41820000)
          |--- testing: '123e5' * 'a5.9' ---
          |float(0)
          |--- testing: '123e5xyz' * '0' ---
          |float(0)
          |--- testing: '123e5xyz' * '65' ---
          |float(799500000)
          |--- testing: '123e5xyz' * '-44' ---
          |float(-541200000)
          |--- testing: '123e5xyz' * '1.2' ---
          |float(14760000)
          |--- testing: '123e5xyz' * '-7.7' ---
          |float(-94710000)
          |--- testing: '123e5xyz' * 'abc' ---
          |float(0)
          |--- testing: '123e5xyz' * '123abc' ---
          |float(1512900000)
          |--- testing: '123e5xyz' * '123e5' ---
          |float(1.5129E+14)
          |--- testing: '123e5xyz' * '123e5xyz' ---
          |float(1.5129E+14)
          |--- testing: '123e5xyz' * ' 123abc' ---
          |float(1512900000)
          |--- testing: '123e5xyz' * '123 abc' ---
          |float(1512900000)
          |--- testing: '123e5xyz' * '123abc ' ---
          |float(1512900000)
          |--- testing: '123e5xyz' * '3.4a' ---
          |float(41820000)
          |--- testing: '123e5xyz' * 'a5.9' ---
          |float(0)
          |--- testing: ' 123abc' * '0' ---
          |int(0)
          |--- testing: ' 123abc' * '65' ---
          |int(7995)
          |--- testing: ' 123abc' * '-44' ---
          |int(-5412)
          |--- testing: ' 123abc' * '1.2' ---
          |float(147.6)
          |--- testing: ' 123abc' * '-7.7' ---
          |float(-947.1)
          |--- testing: ' 123abc' * 'abc' ---
          |int(0)
          |--- testing: ' 123abc' * '123abc' ---
          |int(15129)
          |--- testing: ' 123abc' * '123e5' ---
          |float(1512900000)
          |--- testing: ' 123abc' * '123e5xyz' ---
          |float(1512900000)
          |--- testing: ' 123abc' * ' 123abc' ---
          |int(15129)
          |--- testing: ' 123abc' * '123 abc' ---
          |int(15129)
          |--- testing: ' 123abc' * '123abc ' ---
          |int(15129)
          |--- testing: ' 123abc' * '3.4a' ---
          |float(418.2)
          |--- testing: ' 123abc' * 'a5.9' ---
          |int(0)
          |--- testing: '123 abc' * '0' ---
          |int(0)
          |--- testing: '123 abc' * '65' ---
          |int(7995)
          |--- testing: '123 abc' * '-44' ---
          |int(-5412)
          |--- testing: '123 abc' * '1.2' ---
          |float(147.6)
          |--- testing: '123 abc' * '-7.7' ---
          |float(-947.1)
          |--- testing: '123 abc' * 'abc' ---
          |int(0)
          |--- testing: '123 abc' * '123abc' ---
          |int(15129)
          |--- testing: '123 abc' * '123e5' ---
          |float(1512900000)
          |--- testing: '123 abc' * '123e5xyz' ---
          |float(1512900000)
          |--- testing: '123 abc' * ' 123abc' ---
          |int(15129)
          |--- testing: '123 abc' * '123 abc' ---
          |int(15129)
          |--- testing: '123 abc' * '123abc ' ---
          |int(15129)
          |--- testing: '123 abc' * '3.4a' ---
          |float(418.2)
          |--- testing: '123 abc' * 'a5.9' ---
          |int(0)
          |--- testing: '123abc ' * '0' ---
          |int(0)
          |--- testing: '123abc ' * '65' ---
          |int(7995)
          |--- testing: '123abc ' * '-44' ---
          |int(-5412)
          |--- testing: '123abc ' * '1.2' ---
          |float(147.6)
          |--- testing: '123abc ' * '-7.7' ---
          |float(-947.1)
          |--- testing: '123abc ' * 'abc' ---
          |int(0)
          |--- testing: '123abc ' * '123abc' ---
          |int(15129)
          |--- testing: '123abc ' * '123e5' ---
          |float(1512900000)
          |--- testing: '123abc ' * '123e5xyz' ---
          |float(1512900000)
          |--- testing: '123abc ' * ' 123abc' ---
          |int(15129)
          |--- testing: '123abc ' * '123 abc' ---
          |int(15129)
          |--- testing: '123abc ' * '123abc ' ---
          |int(15129)
          |--- testing: '123abc ' * '3.4a' ---
          |float(418.2)
          |--- testing: '123abc ' * 'a5.9' ---
          |int(0)
          |--- testing: '3.4a' * '0' ---
          |float(0)
          |--- testing: '3.4a' * '65' ---
          |float(221)
          |--- testing: '3.4a' * '-44' ---
          |float(-149.6)
          |--- testing: '3.4a' * '1.2' ---
          |float(4.08)
          |--- testing: '3.4a' * '-7.7' ---
          |float(-26.18)
          |--- testing: '3.4a' * 'abc' ---
          |float(0)
          |--- testing: '3.4a' * '123abc' ---
          |float(418.2)
          |--- testing: '3.4a' * '123e5' ---
          |float(41820000)
          |--- testing: '3.4a' * '123e5xyz' ---
          |float(41820000)
          |--- testing: '3.4a' * ' 123abc' ---
          |float(418.2)
          |--- testing: '3.4a' * '123 abc' ---
          |float(418.2)
          |--- testing: '3.4a' * '123abc ' ---
          |float(418.2)
          |--- testing: '3.4a' * '3.4a' ---
          |float(11.56)
          |--- testing: '3.4a' * 'a5.9' ---
          |float(0)
          |--- testing: 'a5.9' * '0' ---
          |int(0)
          |--- testing: 'a5.9' * '65' ---
          |int(0)
          |--- testing: 'a5.9' * '-44' ---
          |int(0)
          |--- testing: 'a5.9' * '1.2' ---
          |float(0)
          |--- testing: 'a5.9' * '-7.7' ---
          |float(0)
          |--- testing: 'a5.9' * 'abc' ---
          |int(0)
          |--- testing: 'a5.9' * '123abc' ---
          |int(0)
          |--- testing: 'a5.9' * '123e5' ---
          |float(0)
          |--- testing: 'a5.9' * '123e5xyz' ---
          |float(0)
          |--- testing: 'a5.9' * ' 123abc' ---
          |int(0)
          |--- testing: 'a5.9' * '123 abc' ---
          |int(0)
          |--- testing: 'a5.9' * '123abc ' ---
          |int(0)
          |--- testing: 'a5.9' * '3.4a' ---
          |float(0)
          |--- testing: 'a5.9' * 'a5.9' ---
          |int(0)
          |===DONE===
          |""".stripMargin
      )
    }
  }
}