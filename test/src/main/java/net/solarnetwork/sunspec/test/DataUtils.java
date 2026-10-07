/* ==================================================================
 * DataUtils.java - 4/08/2018 9:20:59 AM
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

package net.solarnetwork.sunspec.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for parsing test data files, commonly captured from devices via
 * other tools for use in unit tests.
 *
 * @author matt
 * @version 1.0
 */
public final class DataUtils {

	/**
	 * Parse Modbus register lines encoded in hex.
	 *
	 * <p>
	 * This method expects the lines to contain a single hex integer value at
	 * the end (excluding whitespace) but can be proceeded by anything. For
	 * example the output of the {@literal mbpoll} command produces output like
	 * this, which can be parsed by this method:
	 * </p>
	 *
	 * <pre>
	 * [0]:    0x4031
	 * [1]:    0x0632
	 * [2]:    0x01F0
	 * </pre>
	 *
	 * <p>
	 * Internally this method calls
	 * {@link #parseIntLines(BufferedReader, Pattern, int)} so lines commented
	 * with a {@literal #} character are ignored.
	 * </p>
	 *
	 * @param in
	 *        the input to read from
	 * @return the parsed values
	 * @throws IOException
	 *         if any IO error occurs
	 */
	public static int[] parseModbusHexRegisterLines(BufferedReader in) throws IOException {
		Pattern pat = Pattern.compile(".*0x([0-9A-Fa-f]+)\\s*");
		return parseIntLines(in, pat, 16);
	}

	/**
	 * Parse hex encoded integers from lines.
	 *
	 * <p>
	 * This method will read all lines from the given {@code Reader}, skip those
	 * starting with a {@literal #} character or not matching {@code pat}, and
	 * then parse the first matching group from {@code pat} as an integer
	 * string.
	 * </p>
	 *
	 * @param in
	 *        the input to read lines from
	 * @param pat
	 *        the pattern to match a single integer per line, with the first
	 *        matching group matching the integer value
	 * @param radix
	 *        the radix to parse the number as
	 * @return the parsed integer values
	 * @throws IOException
	 *         if any IO error occurs
	 */
	public static int[] parseIntLines(BufferedReader in, Pattern pat, int radix) throws IOException {
		return in.lines().filter(s -> !s.startsWith("#") && pat.matcher(s).matches()).mapToInt(s -> {
			Matcher m = pat.matcher(s);
			if ( m.matches() ) {
				return Integer.parseInt(m.group(1), radix);
			}
			return -1;
		}).toArray();
	}

	/**
	 * Parse Modbus register lines encoded in hex.
	 *
	 * <p>
	 * This method expects the lines to contain a single decimal integer
	 * register number followed by a hexadecimal integer register value at the
	 * end (excluding whitespace), as output by the {@literal mbpoll} command.
	 * For example:
	 * </p>
	 *
	 * <pre>
	 * [0]:    0x4031
	 * [1]:    0x0632
	 * [2]:    0x01F0
	 * </pre>
	 *
	 * <p>
	 * Internally this method calls
	 * {@link #parseIntKeyIntValueMappingLines(BufferedReader, Pattern, int, int)}
	 * so lines commented with a {@literal #} character are ignored.
	 * </p>
	 *
	 * @param in
	 *        the input to read from
	 * @return the parsed values
	 * @throws IOException
	 *         if any IO error occurs
	 */
	public static Map<Integer, Integer> parseModbusHexRegisterMappingLines(BufferedReader in)
			throws IOException {
		Pattern pat = Pattern.compile("\\s*\\[(\\d+)\\].*0x([0-9A-Fa-f]+)\\s*");
		return parseIntKeyIntValueMappingLines(in, pat, 10, 16);
	}

	/**
	 * Parse integer key/value pairs from lines.
	 *
	 * <p>
	 * This method will read all lines from the given {@code Reader}, skip those
	 * starting with a {@literal #} character or not matching {@code pat}, and
	 * then parse the first matching group from {@code pat} as an integer key
	 * and the second matching group as a integer value.
	 * </p>
	 *
	 * @param in
	 *        the input to read lines from
	 * @param pat
	 *        the pattern to match a single hexadecimal integer per line, with
	 *        the first matching group matching the key and the second the value
	 * @param keyRadix
	 *        the radix of the key integer string
	 * @param valueRadix
	 *        the radix of the value integer string
	 * @return the parsed integer values
	 * @throws IOException
	 *         if any IO error occurs
	 */
	public static Map<Integer, Integer> parseIntKeyIntValueMappingLines(BufferedReader in, Pattern pat,
			int keyRadix, int valueRadix) throws IOException {
		Map<Integer, Integer> result = new LinkedHashMap<>();
		in.lines().filter(s -> !s.startsWith("#") && pat.matcher(s).matches()).forEach(s -> {
			Matcher m = pat.matcher(s);
			if ( m.matches() ) {
				result.put(Integer.parseInt(m.group(1), keyRadix),
						Integer.valueOf(m.group(2), valueRadix));
			}
		});
		return result;
	}

	/**
	 * Get a bit set with the bits of a non-negative integer.
	 *
	 * @param value
	 *        the integer
	 * @return the bit set, with bit {@code i} set if bit {@code i} of
	 *         {@code value} is set
	 * @throws IllegalArgumentException
	 *         if {@code value} is negative
	 */
	public static BitSet bitSetForBigInteger(BigInteger value) {
		if ( value.signum() < 0 ) {
			throw new IllegalArgumentException("Only non-negative values are allowed.");
		}
		BitSet bs = new BitSet();
		for ( int i = 0, len = value.bitLength(); i < len; i++ ) {
			if ( value.testBit(i) ) {
				bs.set(i);
			}
		}
		return bs;
	}

	// @formatter:off
	private static final short[] COMMON_MODEL_02 = new short[] {
			0x0001,
			0x0041,
			0x5665,
			0x7269,
			0x7320,
			0x496E,
			0x6475,
			0x7374,
			0x7269,
			0x6573,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x4535,
			0x3143,
			0x3200,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x4E6F,
			0x6E65,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x322E,
			0x3130,
			0x3300,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x3445,
			0x3339,
			0x3034,
			0x3736,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x000A,
	};

	/**
	 * Get a "common model 02" test fixture.
	 *
	 * @return the common model
	 */
	public static short[] commonModel02() {
		return COMMON_MODEL_02.clone();
	}

}
