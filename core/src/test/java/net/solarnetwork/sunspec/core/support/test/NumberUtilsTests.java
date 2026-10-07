/* ==================================================================
 * NumberUtilsTests.java - 15/03/2018 2:54:58 PM
 *
 * Copyright 2018 SolarNetwork.net Dev Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================================
 */

package net.solarnetwork.sunspec.core.support.test;

import static org.assertj.core.api.BDDAssertions.then;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.core.support.NumberUtils;

/**
 * Test cases for the {@link NumberUtils} class.
 *
 * @author matt
 * @version 1.0
 */
public class NumberUtilsTests {

	@Test
	public void bigDecimalForNumber_null() {
		// @formatter:off
		then(NumberUtils.bigDecimalForNumber(null))
			.as("Null returned for null")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void bigDecimalForNumber_wholeNumbers() {
		// @formatter:off
		then(NumberUtils.bigDecimalForNumber((byte) 12))
			.as("Byte converted")
			.isEqualTo(new BigDecimal("12"))
			;
		then(NumberUtils.bigDecimalForNumber((short) 1234))
			.as("Short converted")
			.isEqualTo(new BigDecimal("1234"))
			;
		then(NumberUtils.bigDecimalForNumber(123456))
			.as("Integer converted")
			.isEqualTo(new BigDecimal("123456"))
			;
		then(NumberUtils.bigDecimalForNumber(123456789123L))
			.as("Long converted")
			.isEqualTo(new BigDecimal("123456789123"))
			;
		then(NumberUtils.bigDecimalForNumber(new BigInteger("123456789012345678901234567890")))
			.as("BigInteger converted")
			.isEqualTo(new BigDecimal("123456789012345678901234567890"))
			;
		// @formatter:on
	}

	@Test
	public void bigDecimalForNumber_float() {
		// @formatter:off
		then(NumberUtils.bigDecimalForNumber(123.123f))
			.as("Float converted from its shortest decimal form")
			.isEqualTo(new BigDecimal("123.123"))
			;
		// @formatter:on
	}

	@Test
	public void bigDecimalForNumber_double() {
		// @formatter:off
		then(NumberUtils.bigDecimalForNumber(123.123456))
			.as("Double converted from its shortest decimal form")
			.isEqualTo(new BigDecimal("123.123456"))
			;
		// @formatter:on
	}

	@Test
	public void bigDecimalForNumber_bigDecimal() {
		// GIVEN
		BigDecimal n = new BigDecimal("1.23");

		// THEN
		// @formatter:off
		then(NumberUtils.bigDecimalForNumber(n))
			.as("BigDecimal returned as-is")
			.isSameAs(n)
			;
		// @formatter:on
	}

	@Test
	public void bigIntegerForNumber_null() {
		// @formatter:off
		then(NumberUtils.bigIntegerForNumber(null))
			.as("Null returned for null")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void bigIntegerForNumber_bigInteger() {
		// GIVEN
		BigInteger n = new BigInteger("123456789012345678901234567890");

		// THEN
		// @formatter:off
		then(NumberUtils.bigIntegerForNumber(n))
			.as("BigInteger returned as-is")
			.isSameAs(n)
			;
		// @formatter:on
	}

	@Test
	public void bigIntegerForNumber_decimals() {
		// @formatter:off
		then(NumberUtils.bigIntegerForNumber(new BigDecimal("123.9")))
			.as("BigDecimal fraction discarded")
			.isEqualTo(BigInteger.valueOf(123))
			;
		then(NumberUtils.bigIntegerForNumber(-123.9))
			.as("Double fraction discarded")
			.isEqualTo(BigInteger.valueOf(-123))
			;
		// @formatter:on
	}

	@Test
	public void bigIntegerForNumber_wholeNumbers() {
		// @formatter:off
		then(NumberUtils.bigIntegerForNumber(123456))
			.as("Integer converted")
			.isEqualTo(BigInteger.valueOf(123456))
			;
		then(NumberUtils.bigIntegerForNumber(Long.MAX_VALUE))
			.as("Long converted")
			.isEqualTo(BigInteger.valueOf(Long.MAX_VALUE))
			;
		// @formatter:on
	}

	@Test
	public void maximumDecimalScale_nullValue() {
		// @formatter:off
		then(NumberUtils.maximumDecimalScale(null, 0))
			.as("Null returned for null")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void maximumDecimalScale_negativeScale() {
		// GIVEN
		BigDecimal n = new BigDecimal("1.23");

		// THEN
		// @formatter:off
		then(NumberUtils.maximumDecimalScale(n, -1))
			.as("Value unchanged for negative maximum scale")
			.isSameAs(n)
			;
		// @formatter:on
	}

	@Test
	public void maximumDecimalScale_withinScale() {
		// GIVEN
		BigDecimal n = new BigDecimal("1.2");

		// THEN
		// @formatter:off
		then(NumberUtils.maximumDecimalScale(n, 2))
			.as("Value unchanged when within maximum scale")
			.isSameAs(n)
			;
		// @formatter:on
	}

	@Test
	public void maximumDecimalScale_zeroScale() {
		// @formatter:off
		then(NumberUtils.maximumDecimalScale(new BigDecimal("1.23"), 0))
			.as("Value rounded to whole number")
			.isEqualTo(new BigDecimal("1"))
			;
		// @formatter:on
	}

	@Test
	public void maximumDecimalScale_scaleRoundedUp() {
		// @formatter:off
		then(NumberUtils.maximumDecimalScale(new BigDecimal("1.28"), 1))
			.as("Value rounded up")
			.isEqualTo(new BigDecimal("1.3"))
			;
		then(NumberUtils.maximumDecimalScale(new BigDecimal("1.25"), 1))
			.as("Value halfway rounded up")
			.isEqualTo(new BigDecimal("1.3"))
			;
		// @formatter:on
	}

	@Test
	public void maximumDecimalScale_float() {
		// @formatter:off
		then(NumberUtils.maximumDecimalScale(2.5f, 0))
			.as("Float converted and rounded")
			.isEqualTo(new BigDecimal("3"))
			;
		// @formatter:on
	}

	@Test
	public void scaled_null() {
		// @formatter:off
		then(NumberUtils.scaled(null, 1))
			.as("Null returned for null")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void scaled_negative() {
		// @formatter:off
		then(NumberUtils.scaled(1, -4))
			.as("Decimal point moved left")
			.isEqualTo(new BigDecimal("0.0001"))
			;
		// @formatter:on
	}

	@Test
	public void scaled_positive() {
		// @formatter:off
		then(NumberUtils.scaled(1, 4))
			.as("Decimal point moved right")
			.isEqualTo(new BigDecimal("10000"))
			;
		// @formatter:on
	}

	@Test
	public void scaled_zero() {
		// @formatter:off
		then(NumberUtils.scaled(1.5f, 0))
			.as("Value converted but not scaled")
			.isEqualTo(new BigDecimal("1.5"))
			;
		// @formatter:on
	}

}
