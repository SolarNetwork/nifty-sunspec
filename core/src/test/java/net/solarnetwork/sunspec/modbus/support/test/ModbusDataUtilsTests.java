/* ==================================================================
 * ModbusDataUtilsTests.java - 10/04/2018 2:26:24 PM
 *
 * Copyright 2018 SolarNetwork.net Dev Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * ==================================================================
 */

package net.solarnetwork.sunspec.modbus.support.test;

import static net.solarnetwork.sunspec.modbus.ModbusWordOrder.LeastToMostSignificant;
import static net.solarnetwork.sunspec.modbus.ModbusWordOrder.MostToLeastSignificant;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.within;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.support.ModbusDataUtils;
import net.solarnetwork.util.Half;

/**
 * Test cases for the {@link ModbusDataUtils} class.
 *
 * @author matt
 * @version 2.1
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
	public void encodeInt32_leastToMostSignificant() {
		// WHEN
		short[] words = ModbusDataUtils.encodeInt32(-12313489, LeastToMostSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded in least to most significant order")
			.containsExactly(0x1C6F, 0xFF44)
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
	public void encodeUnsignedInt32_leastToMostSignificant() {
		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInt32(3000000000L, LeastToMostSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded in least to most significant order")
			.containsExactly(0x5E00, 0xB2D0)
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
	public void encodeInt64_leastToMostSignificant() {
		// WHEN
		short[] words = ModbusDataUtils.encodeInt64(0x0123456789ABCDEFL, LeastToMostSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded in least to most significant order")
			.containsExactly(0xCDEF, 0x89AB, 0x4567, 0x0123)
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
		for ( long l = 0; l < 5000000; l += 3 ) {
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
	public void encodeUnsignedInt64_many_leastToMostSignificant() {
		for ( long l = 0; l < 5000000; l += 3 ) {
			// WHEN
			short[] words = ModbusDataUtils.encodeUnsignedInt64(new BigInteger(String.valueOf(l)),
					LeastToMostSignificant);

			// THEN
			// @formatter:off
			then(words)
				.as("%d converted", l)
				.containsExactly((int) (l & 0xFFFF), (int) ((l >> 16) & 0xFFFF),
						(int) ((l >> 32) & 0xFFFF), (int) ((l >> 48) & 0xFFFF))
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
	public void encodeUnsignedInt64_small_leastToMostSignificant() {
		// GIVEN
		BigInteger bint = new BigInteger("12345678", 16);

		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInt64(bint, LeastToMostSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded in least to most significant order, padded to 64 bits")
			.containsExactly(0x5678, 0x1234, 0, 0)
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
	public void encodeUnsignedInt64_big_leastToMostSignificant() {
		// GIVEN
		BigInteger bint = new BigInteger("175816FE2F85866B", 16);

		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInt64(bint, LeastToMostSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded in least to most significant order")
			.containsExactly(0x866B, 0x2F85, 0x16FE, 0x1758)
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
	public void encodeUnsignedInt64_droppingExcessBytes_leastToMostSignificant() {
		// GIVEN
		BigInteger bint = new BigInteger("FF00175816FE2F85866B", 16);

		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInt64(bint, LeastToMostSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded in least to most significant order, bytes beyond 64 bits dropped")
			.containsExactly(0x866B, 0x2F85, 0x16FE, 0x1758)
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
	public void encodeUnsignedInteger_largerThan64_leastToMostSignificant() {
		// GIVEN
		BigInteger bint = new BigInteger("FF00175816FE2F85866B", 16);

		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInteger(bint, LeastToMostSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded in least to most significant order")
			.containsExactly(0x866B, 0x2F85, 0x16FE, 0x1758, 0xFF00)
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
	public void encodeUnsignedInteger_wayLargerThan64_leastToMostSignificant() {
		// GIVEN
		BigInteger bint = new BigInteger("00328586616F5866FF001755866816FE2F586685866B5866", 16);

		// WHEN
		short[] words = ModbusDataUtils.encodeUnsignedInteger(bint, LeastToMostSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded in least to most significant order")
			.containsExactly(0x5866, 0x866B, 0x6685, 0x2F58, 0x16FE, 0x8668, 0x1755, 0xFF00, 0x5866,
					0x616F, 0x8586, 0x0032)
			;
		// @formatter:on
	}

	@Test
	public void encodeFloat16() {
		// GIVEN
		Half h = new Half(Half.intBitsToHalf(0x4240));

		// WHEN
		short s = ModbusDataUtils.encodeFloat16(h);

		// THEN
		// @formatter:off
		then(s)
			.as("Word encoded")
			.isEqualTo((short) 0x4240)
			;
		// @formatter:on
	}

	@Test
	public void encodeNumber_Float16() {
		// GIVEN
		Half h = new Half(Half.intBitsToHalf(0x4240));

		// WHEN
		short[] words = ModbusDataUtils.encodeNumber(ModbusDataType.Float16, h);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded")
			.containsExactly(0x4240)
			;
		// @formatter:on
	}

	@Test
	public void encodeNumber_leastToMostSignificant() {
		// WHEN
		short[] words = ModbusDataUtils.encodeNumber(ModbusDataType.Int32, -12313489,
				LeastToMostSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded in least to most significant order")
			.containsExactly(0x1C6F, 0xFF44)
			;
		// @formatter:on
	}

	@Test
	public void encodeNumber_nullNumber() {
		// WHEN
		short[] words = ModbusDataUtils.encodeNumber(ModbusDataType.Int32, null, MostToLeastSignificant);

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
		Throwable t = catchThrowable(() -> ModbusDataUtils.encodeNumber(ModbusDataType.StringAscii, 1,
				MostToLeastSignificant));

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
	public void encodeFloat32_leastToMostSignificant() {
		// GIVEN
		Float f = Float.intBitsToFloat(0x403FA7F6);

		// WHEN
		short[] words = ModbusDataUtils.encodeFloat32(f, LeastToMostSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded in least to most significant order")
			.containsExactly(0xA7F6, 0x403F)
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
	public void encodeFloat64_leastToMostSignificant() {
		// GIVEN
		Double d = Double.longBitsToDouble(0x403FA7F6403FA7F6L);

		// WHEN
		short[] words = ModbusDataUtils.encodeFloat64(d, LeastToMostSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded in least to most significant order")
			.containsExactly(0xA7F6, 0x403F, 0xA7F6, 0x403F)
			;
		// @formatter:on
	}

	@Test
	public void encodeBytes() {
		// GIVEN
		byte[] data = new byte[] { 1, 3, 5, 7, 9, 0xb, 0xd };

		// WHEN
		short[] words = ModbusDataUtils.encodeBytes(data, MostToLeastSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded, with the last word padded")
			.containsExactly(0x0103, 0x0507, 0x090b, 0x0d00)
			;
		// @formatter:on
	}

	@Test
	public void encodeBytes_leastToMostSignificant() {
		// GIVEN
		byte[] data = new byte[] { 1, 3, 5, 7, 9, 0xb, 0xd };

		// WHEN
		short[] words = ModbusDataUtils.encodeBytes(data, LeastToMostSignificant);

		// THEN
		// @formatter:off
		then(words)
			.as("Words encoded in least to most significant order, with the last word padded")
			.containsExactly(0x0d00, 0x090b, 0x0507, 0x0103)
			;
		// @formatter:on
	}

	@Test
	public void parseFloat16() {
		// WHEN
		Half h = ModbusDataUtils.parseFloat16((short) 0x4240);

		// THEN
		// @formatter:off
		then(h)
			.as("Half value")
			.isNotNull()
			.as("Half value bits")
			.returns((short) 0x4240, from(Half::halfValue))
			;
		// @formatter:on
	}

	@Test
	public void swapWordOrder() {
		// GIVEN
		short[] words = new short[] { 1, 2, 3, 4 };

		// WHEN
		ModbusDataUtils.swapWordOrder(words);

		// THEN
		// @formatter:off
		then(words)
			.as("Word order reversed in place")
			.containsExactly(4, 3, 2, 1)
			;
		// @formatter:on
	}

	@Test
	public void swapWordOrder_oddLength() {
		// GIVEN
		short[] words = new short[] { 1, 2, 3 };

		// WHEN
		ModbusDataUtils.swapWordOrder(words);

		// THEN
		// @formatter:off
		then(words)
			.as("Word order reversed in place, around the middle word")
			.containsExactly(3, 2, 1)
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
