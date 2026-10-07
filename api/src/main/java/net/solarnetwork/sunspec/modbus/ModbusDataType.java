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

/**
 * An enumeration of common Modbus data types.
 *
 * @author matt
 * @version 1.0
 */
public enum ModbusDataType {

	/** Boolean bit. */
	Boolean(1),

	/** 32-bit floating point. */
	Float32(2),

	/** 64-bit floating point. */
	Float64(4),

	/** Signed 16-bit integer. */
	Int16(1),

	/** Unsigned 16-bit integer. */
	UInt16(1),

	/** Signed 32-bit integer. */
	Int32(2),

	/** Unsigned 32-bit integer. */
	UInt32(2),

	/** Signed 64-bit integer. */
	Int64(4),

	/** Unsigned 64-bit integer. */
	UInt64(4),

	/** Raw bytes. */
	Bytes(-1),

	/** Bytes interpreted as a UTF-8 encoded string. */
	StringUtf8(-1),

	/** Bytes interpreted as an ASCII encoded string. */
	StringAscii(-1);

	final private int wordLength;

	private ModbusDataType(int wordLength) {
		this.wordLength = wordLength;
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

}
