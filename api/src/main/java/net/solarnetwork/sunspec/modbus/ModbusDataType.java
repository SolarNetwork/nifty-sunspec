/* ==================================================================
 * ModbusDataType.java - 20/12/2017 1:59:32 PM
 *
 * Copyright 2017 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.modbus;

import org.jspecify.annotations.Nullable;

/**
 * An enumeration of common Modbus data types.
 *
 * @author matt
 * @version 1.1
 * @since 2.5
 */
public enum ModbusDataType {

	/** Boolean bit. */
	Boolean("bit", 1),

	/**
	 * 16-bit floating point.
	 *
	 * @since 1.1
	 */
	Float16("f16", 1),

	/** 32-bit floating point. */
	Float32("f32", 2),

	/** 64-bit floating point. */
	Float64("f64", 4),

	/** Signed 16-bit integer. */
	Int16("i16", 1),

	/** Unsigned 16-bit integer. */
	UInt16("u16", 1),

	/** Signed 32-bit integer. */
	Int32("i32", 2),

	/** Unsigned 32-bit integer. */
	UInt32("u32", 2),

	/** Signed 64-bit integer. */
	Int64("i64", 4),

	/** Unsigned 64-bit integer. */
	UInt64("u64", 4),

	/** Raw bytes. */
	Bytes("b", -1),

	/** Bytes interpreted as a UTF-8 encoded string. */
	StringUtf8("s", -1),

	/** Bytes interpreted as an ASCII encoded string. */
	StringAscii("a", -1);

	final private String key;
	final private int wordLength;

	private ModbusDataType(String key, int wordLength) {
		this.key = key;
		this.wordLength = wordLength;
	}

	/**
	 * Get the key value for this enum.
	 *
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Get the number of Modbus words (16-bit register values) this data type
	 * requires.
	 *
	 * @return the number of words, or {@literal -1} for an unknown length (for
	 *         example for strings)
	 */
	public int getWordLength() {
		return wordLength;
	}

	/**
	 * Get an enum instance for a key value.
	 *
	 * @param key
	 *        the key
	 * @return the enum
	 * @throws IllegalArgumentException
	 *         if {@code key} is not a valid value
	 */
	public static ModbusDataType forKey(@Nullable String key) {
		if ( key != null ) {
			for ( ModbusDataType e : ModbusDataType.values() ) {
				if ( key.equals(e.key) ) {
					return e;
				}
			}
		}

		throw new IllegalArgumentException("Unknown ModbusDataType key [" + key + "]");
	}

	/**
	 * Get a friendly display string for this data type.
	 *
	 * @return the display string
	 */
	public String toDisplayString() {
		return switch (this) {
			case Boolean -> "Bit (on/off)";
			case Bytes -> "Bytes, 8-bit (two per register)";
			case Float16 -> "16-bit floating point (1 register)";
			case Float32 -> "32-bit floating point (2 registers)";
			case Float64 -> "64-bit floating point (4 registers)";
			case Int16 -> "16-bit signed integer (1 register)";
			case Int32 -> "32-bit signed integer (2 registers)";
			case Int64 -> "64-bit signed integer (4 registers)";
			case StringAscii -> "String (ASCII)";
			case StringUtf8 -> "String (UTF-8)";
			case UInt16 -> "16-bit unsigned integer (1 register)";
			case UInt32 -> "32-bit unsigned integer (2 registers)";
			case UInt64 -> "64-bit unsigned integer (4 registers)";
			default -> this.toString();
		};
	}

}
