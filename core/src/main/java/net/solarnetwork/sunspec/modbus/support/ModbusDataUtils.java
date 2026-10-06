/* ==================================================================
 * ModbusDataUtils.java - 10/04/2018 2:02:53 PM
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

package net.solarnetwork.sunspec.modbus.support;

import static net.solarnetwork.sunspec.modbus.ModbusWordOrder.LeastToMostSignificant;
import static net.solarnetwork.sunspec.modbus.ModbusWordOrder.MostToLeastSignificant;
import java.math.BigInteger;
import java.util.Arrays;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusWordOrder;
import net.solarnetwork.util.Half;

/**
 * Utilities for converting to/from Modbus 16-bit register values.
 *
 * <p>
 * All Modbus register values are stored using {@code short} values, which in
 * Java are always treated as 16-bit signed integers.
 * </p>
 *
 * @author matt
 * @version 2.3
 * @since 2.6
 */
public final class ModbusDataUtils {

	private ModbusDataUtils() {
		// not available
	}

	/**
	 * Convert an array of ints to shorts.
	 *
	 * @param array
	 *        the array to convert
	 * @return the converted array, or {@code null} if {@code array} is
	 *         {@code null}
	 */
	public static short @Nullable [] shortArray(int @Nullable [] array) {
		if ( array == null ) {
			return null;
		}
		final int count = array.length;
		final short[] result = new short[count];
		for ( int i = 0; i < count; i++ ) {
			result[i] = (short) array[i];
		}
		return result;
	}

	/**
	 * Encode a number into raw Modbus register values.
	 *
	 * @param dataType
	 *        the desired Modbus data type
	 * @param number
	 *        the number to encode
	 * @return the encoded register values, in
	 *         {@link ModbusWordOrder#MostToLeastSignificant} word order
	 * @throws IllegalArgumentException
	 *         if {@code dataType} is not supported
	 * @see #encodeNumber(ModbusDataType, Number, ModbusWordOrder)
	 */
	public static short @Nullable [] encodeNumber(ModbusDataType dataType, @Nullable Number number) {
		return encodeNumber(dataType, number, MostToLeastSignificant);
	}

	/**
	 * Encode a number into raw Modbus register values.
	 *
	 * <p>
	 * This method always returns a value if {@code number} is {@code null}. If
	 * {@code dataType} is not a supported type an,
	 * {@link IllegalArgumentException} will be thrown.
	 * </p>
	 *
	 * @param dataType
	 *        the desired Modbus data type
	 * @param number
	 *        the number to encode
	 * @param wordOrder
	 *        the desired word order
	 * @return the encoded register values
	 * @throws IllegalArgumentException
	 *         if {@code dataType} is not supported
	 * @since 1.1
	 */
	public static short @Nullable [] encodeNumber(ModbusDataType dataType, @Nullable Number number,
			ModbusWordOrder wordOrder) {
		final short[] result = switch (dataType) {
			case Boolean -> new short[] {
					(number != null && number.intValue() != 0 ? (short) 1 : (short) 0) };
			case Float16 -> new short[] { encodeFloat16(number instanceof Half n ? n
					: number != null ? Half.valueOf(number.toString()) : null) };
			case Float32 -> encodeFloat32(number != null ? number.floatValue() : 0f);
			case Float64 -> encodeFloat64(number != null ? number.doubleValue() : 0.0);
			case Int16 -> encodeInt16(number != null ? number.shortValue() : (short) 0);
			case UInt16 -> encodeUnsignedInt16(number != null ? number.intValue() : 0);
			case Int32 -> encodeInt32(number != null ? number.intValue() : 0);
			case UInt32 -> encodeUnsignedInt32(number != null ? number.longValue() : 0L);
			case Int64 -> encodeInt64(number != null ? number.longValue() : 0L);
			case UInt64 -> {
				try {
					yield encodeUnsignedInt64(number instanceof BigInteger n ? n
							: number != null ? new BigInteger(number.toString()) : BigInteger.ZERO);
				} catch ( NumberFormatException e ) {
					yield encodeInt64(0L);
				}
			}
			case Bytes -> {
				try {
					yield encodeUnsignedInteger(number instanceof BigInteger n ? n
							: number != null ? new BigInteger(number.toString()) : BigInteger.ZERO);
				} catch ( NumberFormatException e ) {
					yield new short[] { 0 };
				}
			}
			default -> throw new IllegalArgumentException(
					"Data type " + dataType + " cannot be converted into a number");
		};
		if ( wordOrder == LeastToMostSignificant ) {
			swapWordOrder(result);
		}
		return result;
	}

	/**
	 * Encode a 16-bit signed integer value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the value to encode
	 * @return the register values, which will have a length of {@literal 1}
	 */
	public static short[] encodeInt16(@Nullable Short value) {
		short bits = (value != null ? value.shortValue() : (short) 0);
		return new short[] { bits };
	}

	/**
	 * Encode a 16-bit unsigned integer value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the value to encode
	 * @return the register values, which will have a length of {@literal 1}
	 */
	public static short[] encodeUnsignedInt16(@Nullable Integer value) {
		int bits = (value != null ? value : 0);
		return new short[] { (short) (bits & 0xFFFF) };
	}

	/**
	 * Encode a 32-bit signed integer value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the value to encode
	 * @return the register values, which will have a length of {@literal 2} and
	 *         use {@link ModbusWordOrder#MostToLeastSignificant} word order
	 */
	public static short[] encodeInt32(@Nullable Integer value) {
		return encodeInt32(value, MostToLeastSignificant);
	}

	/**
	 * Encode a 32-bit signed integer value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the value to encode
	 * @param wordOrder
	 *        the resulting word order
	 * @return the register values, which will have a length of {@literal 2}
	 * @since 1.1
	 */
	public static short[] encodeInt32(@Nullable Integer value, ModbusWordOrder wordOrder) {
		int bits = (value != null ? value : 0);
		short[] result = new short[] { (short) ((bits >> 16) & 0xFFFF), (short) (bits & 0xFFFF) };
		if ( wordOrder == LeastToMostSignificant ) {
			swapWordOrder(result);
		}
		return result;
	}

	/**
	 * Encode a 32-bit unsigned integer value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the value to encode
	 * @return the register values, which will have a length of {@literal 2} and
	 *         use {@link ModbusWordOrder#MostToLeastSignificant} word order
	 */
	public static short[] encodeUnsignedInt32(@Nullable Long value) {
		return encodeUnsignedInt32(value, MostToLeastSignificant);
	}

	/**
	 * Encode a 32-bit unsigned integer value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the value to encode
	 * @param wordOrder
	 *        the resulting word order
	 * @return the register values, which will have a length of {@literal 2}
	 * @since 1.1
	 */
	public static short[] encodeUnsignedInt32(@Nullable Long value, ModbusWordOrder wordOrder) {
		short[] words = encodeInt64(value, wordOrder);
		if ( wordOrder == MostToLeastSignificant ) {
			return new short[] { words[2], words[3] };
		}
		return new short[] { words[0], words[1] };
	}

	/**
	 * Encode a 64-bit signed integer value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the value to encode
	 * @return the register values, which will have a length of {@literal 4} and
	 *         use {@link ModbusWordOrder#MostToLeastSignificant} word order
	 */
	public static short[] encodeInt64(@Nullable Long value) {
		return encodeInt64(value, MostToLeastSignificant);
	}

	/**
	 * Encode a 64-bit signed integer value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the value to encode
	 * @param wordOrder
	 *        the resulting word order
	 * @return the register values, which will have a length of {@literal 4}
	 * @since 1.1
	 */
	public static short[] encodeInt64(@Nullable Long value, ModbusWordOrder wordOrder) {
		long bits = (value != null ? value : 0);
		short[] result = new short[] { (short) ((bits >> 48) & 0xFFFF), (short) ((bits >> 32) & 0xFFFF),
				(short) ((bits >> 16) & 0xFFFF), (short) (bits & 0xFFFF) };
		if ( wordOrder == LeastToMostSignificant ) {
			swapWordOrder(result);
		}
		return result;
	}

	/**
	 * Encode an 64-bit unsigned integer value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the integer to encode
	 * @return the register values, which will have a length of {@literal 4} and
	 *         use {@link ModbusWordOrder#MostToLeastSignificant} word order
	 */
	public static short[] encodeUnsignedInt64(@Nullable BigInteger value) {
		return encodeUnsignedInt64(value, MostToLeastSignificant);
	}

	/**
	 * Encode an 64-bit unsigned integer value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the integer to encode
	 * @param wordOrder
	 *        the resulting word order
	 * @return the register values, which will have a length of {@literal 4}
	 * @since 1.1
	 */
	public static short[] encodeUnsignedInt64(@Nullable BigInteger value, ModbusWordOrder wordOrder) {
		byte[] bytes = (value != null ? value.toByteArray() : new byte[] { 0, 0 });

		// drop sign byte, if present and not already an even number of bytes
		if ( bytes[0] == 0 && bytes.length % 2 == 1 && bytes.length < 9 ) {
			bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
		}

		// we can only use up to 8 bytes
		if ( bytes.length > 8 ) {
			bytes = Arrays.copyOfRange(bytes, bytes.length - 8, bytes.length);
		}

		// ensure we have an even number of bytes
		if ( bytes.length % 2 == 1 ) {
			byte[] tmp = new byte[bytes.length + 1];
			System.arraycopy(bytes, 0, tmp, 1, bytes.length);
			bytes = tmp;
		}

		short[] words = new short[4];
		int offset = (8 - bytes.length) / 2;
		for ( int i = 0; i < bytes.length; i += 2 ) {
			int v = ((bytes[i] & 0xFF) << 8);
			if ( i + 1 < bytes.length ) {
				v |= (bytes[i + 1] & 0xFF);
			}
			words[offset + i / 2] = (short) (v & 0xFFFF);
		}

		if ( wordOrder == LeastToMostSignificant ) {
			swapWordOrder(words);
		}

		return words;
	}

	/**
	 * Encode an unsigned integer value into raw Modbus unsigned short register
	 * values.
	 *
	 * @param value
	 *        the integer to encode
	 * @return the register values, which will have a length equal to the number
	 *         of registers required to store the full value and use
	 *         {@link ModbusWordOrder#MostToLeastSignificant} word order
	 */
	public static short[] encodeUnsignedInteger(BigInteger value) {
		return encodeUnsignedInteger(value, MostToLeastSignificant);
	}

	/**
	 * Encode an unsigned integer value into raw Modbus unsigned short register
	 * values.
	 *
	 * @param value
	 *        the integer to encode
	 * @param wordOrder
	 *        the resulting word order
	 * @return the register values, which will have a length equal to the number
	 *         of registers required to store the full value
	 * @since 1.1
	 */
	public static short[] encodeUnsignedInteger(BigInteger value, ModbusWordOrder wordOrder) {
		byte[] bytes = value.toByteArray();

		// drop sign byte, if present and not already an even number of bytes
		if ( bytes[0] == 0 && bytes.length % 2 == 1 ) {
			bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
		}

		// ensure we have an even number of bytes
		if ( bytes.length % 2 == 1 ) {
			byte[] tmp = new byte[bytes.length + 1];
			System.arraycopy(bytes, 0, tmp, 1, bytes.length);
			bytes = tmp;
		}

		short[] words = new short[bytes.length / 2];
		for ( int i = 0; i < bytes.length; i += 2 ) {
			int v = ((bytes[i] & 0xFF) << 8);
			if ( i + 1 < bytes.length ) {
				v |= (bytes[i + 1] & 0xFF);
			}
			words[i / 2] = (short) v;
		}

		if ( wordOrder == LeastToMostSignificant ) {
			swapWordOrder(words);
		}

		return words;
	}

	/**
	 * Swap the order of an array of register values.
	 *
	 * <p>
	 * This essentially reverses the array. The array is modified in-place.
	 * </p>
	 *
	 * @param array
	 *        the data to swap
	 * @since 1.1
	 */
	public static void swapWordOrder(short[] array) {
		for ( int i = 0; i < array.length / 2; i++ ) {
			short temp = array[i];
			array[i] = array[array.length - i - 1];
			array[array.length - i - 1] = temp;
		}
	}

	/**
	 * Encode an IEEE-754 16-bit float value into a raw Modbus unsigned short
	 * register value.
	 *
	 * @param value
	 *        the half to encode
	 * @return the register value
	 * @since 2.2
	 */
	public static short encodeFloat16(@Nullable Half value) {
		return (value != null ? value.halfValue() : (short) 0);
	}

	/**
	 * Encode an IEEE-754 32-bit float value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the float to encode
	 * @return the register values, which will have a length of {@literal 2} and
	 *         use {@link ModbusWordOrder#MostToLeastSignificant} word order
	 */
	public static short[] encodeFloat32(@Nullable Float value) {
		return encodeFloat32(value, MostToLeastSignificant);
	}

	/**
	 * Encode an IEEE-754 32-bit float value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the float to encode
	 * @param wordOrder
	 *        the resulting word order
	 * @return the register values, which will have a length of {@literal 2}
	 * @since 1.1
	 */
	public static short[] encodeFloat32(@Nullable Float value, ModbusWordOrder wordOrder) {
		int bits = Float.floatToIntBits(value != null ? value : 0f);
		return encodeInt32(bits, wordOrder);
	}

	/**
	 * Encode an IEEE-754 32-bit float value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the float to encode
	 * @return the register values, which will have a length of {@literal 4} and
	 *         use {@link ModbusWordOrder#MostToLeastSignificant} word order
	 */
	public static short[] encodeFloat64(@Nullable Double value) {
		return encodeFloat64(value, MostToLeastSignificant);
	}

	/**
	 * Encode an IEEE-754 32-bit float value into raw Modbus unsigned short
	 * register values.
	 *
	 * @param value
	 *        the float to encode
	 * @param wordOrder
	 *        the resulting word order
	 * @return the register values, which will have a length of {@literal 4} and
	 *         use {@link ModbusWordOrder#MostToLeastSignificant} word order
	 * @since 1.1
	 */
	public static short[] encodeFloat64(@Nullable Double value, ModbusWordOrder wordOrder) {
		long bits = Double.doubleToLongBits(value != null ? value : 0.0);
		return encodeInt64(bits, wordOrder);
	}

	/**
	 * Encode an array of bytes into 16-bit raw Modbus register values.
	 *
	 * <p>
	 * Each register value will hold up to two bytes.
	 * </p>
	 *
	 * @param data
	 *        the data to encode
	 * @param wordOrder
	 *        the resulting word order
	 * @return the register values, which will have a length of
	 *         {@code data.length / 2}
	 * @since 1.1
	 */
	public static short[] encodeBytes(byte @Nullable [] data, ModbusWordOrder wordOrder) {
		if ( data == null || data.length < 1 ) {
			return new short[0];
		}
		short[] words = new short[(int) Math.ceil(data.length / 2.0)];
		for ( int i = 0, p = 0; i < data.length; i += 2, p += 1 ) {
			short n = (short) ((data[i] & 0xFF) << 8);
			if ( i + 1 < data.length ) {
				n = (short) (n | (data[i + 1] & 0xFF));
			}
			words[p] = n;
		}
		if ( wordOrder == LeastToMostSignificant ) {
			swapWordOrder(words);
		}
		return words;
	}

	/**
	 * Parse a 32-bit signed integer value from raw Modbus register values.
	 *
	 * @param hi
	 *        bits 31-16
	 * @param lo
	 *        bits 15-0
	 * @return the parsed integer
	 * @since 2.1
	 */
	public static int toInt32(final short hi, final short lo) {
		return (((hi & 0xFFFF) << 16) | (lo & 0xFFFF));
	}

	/**
	 * Parse a 32-bit signed integer value from raw Modbus register values.
	 *
	 * @param hi
	 *        bits 31-16
	 * @param lo
	 *        bits 15-0
	 * @return the parsed integer, never {@code null}
	 */
	public static Integer parseInt32(final short hi, final short lo) {
		return toInt32(hi, lo);
	}

	/**
	 * Parse a 32-bit unsigned integer value from raw Modbus register values.
	 *
	 * <p>
	 * <b>Note</b> a {@code long} is returned to support unsigned 32-bit values.
	 * </p>
	 *
	 * @param hi
	 *        bits 31-16
	 * @param lo
	 *        bits 15-0
	 * @return the parsed integer
	 * @since 2.1
	 */
	public static long toUnsignedInt32(final short hi, final short lo) {
		return (((hi & 0xFFFFL) << 16) | (lo & 0xFFFFL));
	}

	/**
	 * Parse a 32-bit unsigned integer value from raw Modbus register values.
	 *
	 * <p>
	 * <b>Note</b> a {@code Long} is returned to support unsigned 32-bit values.
	 * </p>
	 *
	 * @param hi
	 *        bits 31-16
	 * @param lo
	 *        bits 15-0
	 * @return the parsed integer, never {@code null}
	 */
	public static Long parseUnsignedInt32(final short hi, final short lo) {
		return toUnsignedInt32(hi, lo);
	}

	/**
	 * Parse a 64-bit signed integer value from raw Modbus register values.
	 *
	 * @param h1
	 *        bits 63-48
	 * @param h2
	 *        bits 47-32
	 * @param l1
	 *        bits 31-16
	 * @param l2
	 *        bits 15-0
	 * @return the parsed integer
	 * @since 2.1
	 */
	public static long toInt64(final short h1, final short h2, final short l1, final short l2) {
		return (((h1 & 0xFFFFL) << 48) | ((h2 & 0xFFFFL) << 32) | ((l1 & 0xFFFFL) << 16)
				| (l2 & 0xFFFFL));
	}

	/**
	 * Parse a 64-bit signed integer value from raw Modbus register values.
	 *
	 * @param h1
	 *        bits 63-48
	 * @param h2
	 *        bits 47-32
	 * @param l1
	 *        bits 31-16
	 * @param l2
	 *        bits 15-0
	 * @return the parsed integer, never {@code null}
	 */
	public static Long parseInt64(final short h1, final short h2, final short l1, final short l2) {
		return toInt64(h1, h2, l1, l2);
	}

	/**
	 * Construct an 64-bit unsigned integer from raw Modbus register values.
	 *
	 * @param h1
	 *        bits 63-48
	 * @param h2
	 *        bits 47-32
	 * @param l1
	 *        bits 31-16
	 * @param l2
	 *        bits 15-0
	 * @return the parsed integer, never {@code null}
	 */
	public static BigInteger parseUnsignedInt64(final short h1, final short h2, final short l1,
			final short l2) {
		int[] data = new int[] { h1, h2, l1, l2 };
		BigInteger r = new BigInteger("0");
		for ( int i = 0; i < 4; i++ ) {
			if ( i > 0 ) {
				r = r.shiftLeft(16);
			}
			r = r.add(new BigInteger(String.valueOf(data[i] & 0xFFFF)));
		}
		return r;
	}

	/**
	 * Parse an IEEE-754 16-bit float value from raw Modbus register values.
	 *
	 * @param val
	 *        the 16 bits
	 * @return the parsed half, or {@code null} if not available or parsed half
	 *         is {@code NaN}
	 * @since 2.2
	 */
	public static @Nullable Half parseFloat16(final short val) {
		Half result = Half.valueOf(val);
		if ( result.isNaN() ) {
			result = null;
		}
		return result;
	}

	/**
	 * Parse an IEEE-754 32-bit float value from raw Modbus register values.
	 *
	 * @param hi
	 *        the high 16 bits
	 * @param lo
	 *        the low 16 bits
	 * @return the parsed float
	 * @since 2.1
	 */
	public static float toFloat32(final short hi, final short lo) {
		int int32 = toInt32(hi, lo);
		return Float.intBitsToFloat(int32);
	}

	/**
	 * Parse an IEEE-754 32-bit float value from raw Modbus register values.
	 *
	 * @param hi
	 *        the high 16 bits
	 * @param lo
	 *        the low 16 bits
	 * @return the parsed float, or {@code null} if not available or parsed
	 *         float is {@code NaN}
	 */
	public static @Nullable Float parseFloat32(final short hi, final short lo) {
		Float result = toFloat32(hi, lo);
		if ( result.isNaN() ) {
			result = null;
		}
		return result;
	}

	/**
	 * Parse an IEEE-754 64-bit floating point value from raw Modbus register
	 * values.
	 *
	 * @param h1
	 *        bits 63-48
	 * @param h2
	 *        bits 47-32
	 * @param l1
	 *        bits 31-16
	 * @param l2
	 *        bits 15-0
	 * @return the parsed double
	 * @since 2.1
	 */
	public static double toFloat64(final short h1, final short h2, final short l1, final short l2) {
		long l = toInt64(h1, h2, l1, l2);
		return Double.longBitsToDouble(l);
	}

	/**
	 * Parse an IEEE-754 64-bit floating point value from raw Modbus register
	 * values.
	 *
	 * @param h1
	 *        bits 63-48
	 * @param h2
	 *        bits 47-32
	 * @param l1
	 *        bits 31-16
	 * @param l2
	 *        bits 15-0
	 * @return the parsed double, or {@code null} if the result is {@code NaN}
	 */
	public static @Nullable Double parseFloat64(final short h1, final short h2, final short l1,
			final short l2) {
		Double result = toFloat64(h1, h2, l1, l2);
		if ( result.isNaN() ) {
			result = null;
		}
		return result;
	}

}
