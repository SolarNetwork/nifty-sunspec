/* ==================================================================
 * ModbusDataUtilsTests.java - 10/04/2018 2:26:24 PM
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

package net.solarnetwork.sunspec.modbus.support.test;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.within;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.support.ModbusDataUtils;

/**
 * Test cases for the {@link ModbusDataUtils} class.
 *
 * @author matt
 * @version 1.0
 */
public class ModbusDataUtilsTests {

	@Test
	public void parseFloat32() {
		// WHEN
		Float result = ModbusDataUtils.parseFloat32((short) 0x403F, (short) 0xA7F6);

		// THEN
		// @formatter:off
		then(result)
			.as("Float value")
			.isNotNull()
			.isCloseTo(2.994626f, within(0.000001f))
			;
		// @formatter:on
	}

	@Test
	public void parseFloat32_nan() {
		// WHEN
		Float result = ModbusDataUtils.parseFloat32((short) 0xFFC0, (short) 0x0000);

		// THEN
		// @formatter:off
		then(result)
			.as("NaN float value is not available")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void parseFloat64() {
		// WHEN
		Double result = ModbusDataUtils.parseFloat64((short) 0x4009, (short) 0x21FB, (short) 0x5444,
				(short) 0x2D18);

		// THEN
		// @formatter:off
		then(result)
			.as("Double value")
			.isEqualTo(Math.PI)
			;
		// @formatter:on
	}

	@Test
	public void parseFloat64_nan() {
		// WHEN
		Double result = ModbusDataUtils.parseFloat64((short) 0xFFF8, (short) 0x0000, (short) 0x0000,
				(short) 0x0000);

		// THEN
		// @formatter:off
		then(result)
			.as("NaN double value is not available")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void encodeInt16() {
		// WHEN
		short[] words = ModbusDataUtils.encodeInt16((short) -2);

		// THEN
		// @formatter:off
		then(words)
			.as("Word encoded")
			.containsExactly(0xFFFE)
			;
		// @formatter:on
	}

	@Test
	public void encodeInt16_null() {
		// WHEN
		short[] words = ModbusDataUtils.encodeInt16(null);

		// THEN
		// @formatter:off
		then(words)
			.as("Null encoded as zero")
			.containsExactly(0)
			;
		// @formatter:on
	}

	@Test
	public void encodeUnsignedInt16() {
		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInt16(65000);

		// THEN
		// @formatter:off
		then(words)
			.as("Word encoded")
			.containsExactly(0xFDE8)
			;
		// @formatter:on
	}

	@Test
	public void encodeUnsignedInt16_null() {
		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInt16(null);

		// THEN
		// @formatter:off
		then(words)
			.as("Null encoded as zero")
			.containsExactly(0)
			;
		// @formatter:on
	}

	@Test
	public void encodeInt32() {
		// WHEN
		short[] words = ModbusDataUtils.encodeInt32(-12313489);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded")
			.containsExactly(0xFF44, 0x1C6F)
			;
		// @formatter:on
	}

	@Test
	public void encodeUnsignedInt32() {
		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInt32(3000000000L);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded for a value larger than an int")
			.containsExactly(0xB2D0, 0x5E00)
			;
		// @formatter:on
	}

	@Test
	public void encodeUnsignedInt32_null() {
		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInt32(null);

		// THEN
		// @formatter:off
		then(words)
			.as("Null encoded as zero")
			.containsExactly(0, 0)
			;
		// @formatter:on
	}

	@Test
	public void encodeInt64() {
		// WHEN
		short[] words = ModbusDataUtils.encodeInt64(0x0123456789ABCDEFL);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded")
			.containsExactly(0x0123, 0x4567, 0x89AB, 0xCDEF)
			;
		// @formatter:on
	}

	@Test
	public void encodeInt64_negative() {
		// WHEN
		short[] words = ModbusDataUtils.encodeInt64(-2L);

		// THEN
		// @formatter:off
		then(words)
			.as("Two's complement words encoded")
			.containsExactly(0xFFFF, 0xFFFF, 0xFFFF, 0xFFFE)
			;
		// @formatter:on
	}

	@Test
	public void encodeInt64_null() {
		// WHEN
		short[] words = ModbusDataUtils.encodeInt64(null);

		// THEN
		// @formatter:off
		then(words)
			.as("Null encoded as zero")
			.containsExactly(0, 0, 0, 0)
			;
		// @formatter:on
	}

	@Test
	public void parseInt32() {
		// WHEN
		Integer result = ModbusDataUtils.parseInt32((short) 0xFF44, (short) 0x1C6F);

		// THEN
		// @formatter:off
		then(result)
			.as("Integer value")
			.isEqualTo(-12313489)
			;
		// @formatter:on
	}

	@Test
	public void parseUnsignedInt32() {
		// WHEN
		Long result = ModbusDataUtils.parseUnsignedInt32((short) 0xB2D0, (short) 0x5E00);

		// THEN
		// @formatter:off
		then(result)
			.as("Unsigned value larger than an int")
			.isEqualTo(3000000000L)
			;
		// @formatter:on
	}

	@Test
	public void parseInt64() {
		// WHEN
		Long result = ModbusDataUtils.parseInt64((short) 0x0123, (short) 0x4567, (short) 0x89AB,
				(short) 0xCDEF);

		// THEN
		// @formatter:off
		then(result)
			.as("Long value")
			.isEqualTo(0x0123456789ABCDEFL)
			;
		// @formatter:on
	}

	@Test
	public void parseInt64_negative() {
		// WHEN
		Long result = ModbusDataUtils.parseInt64((short) 0xFFFF, (short) 0xFFFF, (short) 0xFFFF,
				(short) 0xFFFE);

		// THEN
		// @formatter:off
		then(result)
			.as("Two's complement value")
			.isEqualTo(-2L)
			;
		// @formatter:on
	}

	@Test
	public void parseUnsignedInt64() {
		// WHEN
		BigInteger result = ModbusDataUtils.parseUnsignedInt64((short) 0xFFFF, (short) 0xFFFF,
				(short) 0xFFFF, (short) 0xFFFE);

		// THEN
		// @formatter:off
		then(result)
			.as("Unsigned value larger than a long")
			.isEqualTo(new BigInteger("FFFFFFFFFFFFFFFE", 16))
			;
		// @formatter:on
	}

	@Test
	public void parseUnsignedInt64_small() {
		// WHEN
		BigInteger result = ModbusDataUtils.parseUnsignedInt64((short) 0x0000, (short) 0x0000,
				(short) 0x1234, (short) 0x5678);

		// THEN
		// @formatter:off
		then(result)
			.as("Unsigned value")
			.isEqualTo(BigInteger.valueOf(0x12345678L))
			;
		// @formatter:on
	}

	@Test
	public void encodeUnsignedInt64_many() {
		for ( long l = 0; l < 5000000; l += 17 ) {
			// WHEN
			short[] words = ModbusDataUtils.encodeUnsignedInt64(new BigInteger(String.valueOf(l)));

			// THEN
			// @formatter:off
			then(words)
				.as("%d converted", l)
				.containsExactly((int) ((l >> 48) & 0xFFFF), (int) ((l >> 32) & 0xFFFF),
						(int) ((l >> 16) & 0xFFFF), (int) (l & 0xFFFF))
				;
			// @formatter:on
		}
	}

	@Test
	public void encodeUnsignedInt64_small() {
		// GIVEN
		BigInteger bint = new BigInteger("12345678", 16);

		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInt64(bint);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded, padded to 64 bits")
			.containsExactly(0, 0, 0x1234, 0x5678)
			;
		// @formatter:on
	}

	@Test
	public void encodeUnsignedInt64_big() {
		// GIVEN
		BigInteger bint = new BigInteger("175816FE2F85866B", 16);

		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInt64(bint);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded")
			.containsExactly(0x1758, 0x16FE, 0x2F85, 0x866B)
			;
		// @formatter:on
	}

	@Test
	public void encodeUnsignedInt64_droppingExcessBytes() {
		// GIVEN
		BigInteger bint = new BigInteger("FF00175816FE2F85866B", 16);

		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInt64(bint);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded, with the bytes beyond 64 bits dropped")
			.containsExactly(0x1758, 0x16FE, 0x2F85, 0x866B)
			;
		// @formatter:on
	}

	@Test
	public void encodeUnsignedInteger_largerThan64() {
		// GIVEN
		BigInteger bint = new BigInteger("FF00175816FE2F85866B", 16);

		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInteger(bint);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded")
			.containsExactly(0xFF00, 0x1758, 0x16FE, 0x2F85, 0x866B)
			;
		// @formatter:on
	}

	@Test
	public void encodeUnsignedInteger_wayLargerThan64() {
		// GIVEN
		BigInteger bint = new BigInteger("00328586616F5866FF001755866816FE2F586685866B5866", 16);

		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInteger(bint);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded")
			.containsExactly(0x0032, 0x8586, 0x616F, 0x5866, 0xFF00, 0x1755, 0x8668, 0x16FE, 0x2F58,
					0x6685, 0x866B, 0x5866)
			;
		// @formatter:on
	}

	@Test
	public void encodeNumber_int32() {
		// WHEN
		short[] words = ModbusDataUtils.encodeNumber(ModbusDataType.Int32, -12313489);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded with the most significant first")
			.containsExactly(0xFF44, 0x1C6F)
			;
		// @formatter:on
	}

	@Test
	public void encodeNumber_nullNumber() {
		// WHEN
		short[] words = ModbusDataUtils.encodeNumber(ModbusDataType.Int32, null);

		// THEN
		// @formatter:off
		then(words)
			.as("Null encoded as zero")
			.containsExactly(0, 0)
			;
		// @formatter:on
	}

	@Test
	public void encodeNumber_unsupportedType() {
		// WHEN
		Throwable t = catchThrowable(() -> ModbusDataUtils.encodeNumber(ModbusDataType.StringAscii, 1));

		// THEN
		// @formatter:off
		then(t)
			.as("String type cannot be encoded as a number")
			.isInstanceOf(IllegalArgumentException.class)
			;
		// @formatter:on
	}

	@Test
	public void encodeFloat32() {
		// GIVEN
		Float f = Float.intBitsToFloat(0x403FA7F6);

		// WHEN
		short[] words = ModbusDataUtils.encodeFloat32(f);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded")
			.containsExactly(0x403F, 0xA7F6)
			;
		// @formatter:on
	}

	@Test
	public void encodeFloat64() {
		// GIVEN
		Double d = Double.longBitsToDouble(0x403FA7F6403FA7F6L);

		// WHEN
		short[] words = ModbusDataUtils.encodeFloat64(d);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded")
			.containsExactly(0x403F, 0xA7F6, 0x403F, 0xA7F6)
			;
		// @formatter:on
	}

	@Test
	public void shortArray() {
		// WHEN
		short[] words = ModbusDataUtils.shortArray(new int[] { 1, 0x7FFF, 0x8000, 0xFFFF });

		// THEN
		// @formatter:off
		then(words)
			.as("Values converted to signed 16-bit words")
			.containsExactly((short) 1, Short.MAX_VALUE, Short.MIN_VALUE, (short) -1)
			;
		// @formatter:on
	}

	@Test
	public void shortArray_null() {
		// WHEN
		short[] words = ModbusDataUtils.shortArray(null);

		// THEN
		// @formatter:off
		then(words)
			.as("Null array converted to null")
			.isNull()
			;
		// @formatter:on
	}

}
