/* ==================================================================
 * ModbusConnection.java - Jul 29, 2014 11:19:18 AM
 *
 * Copyright 2007-2014 SolarNetwork.net Dev Team
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

import java.io.IOException;
import java.nio.charset.Charset;
import org.jspecify.annotations.Nullable;

/**
 * High level Modbus connection API.
 *
 * <p>
 * This API aims to simplify accessing Modbus capable devices without having any
 * direct dependency on any particular Modbus implementation.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public interface ModbusConnection {

	/**
	 * Get the Modbus Unit ID this device represents.
	 *
	 * @return the unit ID
	 */
	int getUnitId();

	/**
	 * Get the values of specific 16-bit Modbus registers as an array of 16-bit
	 * words.
	 *
	 * <p>
	 * Note that the raw short values can be treated as unsigned shorts by
	 * converting them to integers, like
	 * {@code int unsigned = ((int)s) && 0xFFFF}, or by calling
	 * {@link Short#toUnsignedInt(short)}.
	 * </p>
	 *
	 * @param function
	 *        the Modbus function code to use
	 * @param address
	 *        the 0-based Modbus register address to start reading from
	 * @param count
	 *        the number of Modbus 16-bit registers to read
	 * @return array of register values; the result will have a length equal to
	 *         {@code count}
	 * @throws IOException
	 *         if any communication error occurs
	 */
	short[] readWords(ModbusReadingFunction function, int address, int count) throws IOException;

	/**
	 * Write 16-bit word values to 16-bit Modbus registers.
	 *
	 * @param function
	 *        the Modbus function code to use
	 * @param address
	 *        the 0-based Modbus register address to start writing to
	 * @param values
	 *        the 16-bit values to write
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void writeWords(ModbusWritingFunction function, int address, short[] values) throws IOException;

	/**
	 * Read a set of registers as bytes and interpret as a string.
	 *
	 * @param function
	 *        the Modbus function code to use
	 * @param address
	 *        the 0-based Modbus register address to start reading from
	 * @param count
	 *        the number of Modbus 16-bit registers to read
	 * @param trim
	 *        if {@literal true} then remove leading/trailing whitespace from
	 *        the resulting string
	 * @param charset
	 *        the character set to interpret the bytes as
	 * @return String from interpreting raw bytes as a string
	 * @throws IOException
	 *         if any communication error occurs
	 */
	@Nullable
	String readString(ModbusReadingFunction function, int address, int count, boolean trim,
			Charset charset) throws IOException;

}
