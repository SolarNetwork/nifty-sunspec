/* ==================================================================
 * ModbusFunction.java - 11/03/2018 8:43:19 AM
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

package net.solarnetwork.sunspec.modbus;

import org.jspecify.annotations.Nullable;

/**
 * API for a modbus function.
 *
 * @author matt
 * @version 2.1
 * @since 2.5
 */
public interface ModbusFunction {

	/**
	 * Get the function code.
	 *
	 * @return the code
	 */
	int getCode();

	/**
	 * Get a friendly display string for this function.
	 *
	 * @return a display string
	 */
	String toDisplayString();

	/**
	 * Return {@literal true} if this function represents a read operation.
	 *
	 * @return {@literal true} if this function represents a read operation,
	 *         {@literal false} if a write operation
	 */
	boolean isReadFunction();

	/**
	 * Get an "opposite" function from this function.
	 *
	 * <p>
	 * This method is used to get a read function for a given write function,
	 * and a write function for a given read function.
	 * </p>
	 *
	 * @return the function, or {@code null} if not applicable
	 */
	@Nullable
	ModbusFunction oppositeFunction();

	/**
	 * Get the register block type related to this function.
	 *
	 * @return the block type
	 * @since 2.1
	 */
	ModbusRegisterBlockType blockType();

	/**
	 * Get a {@link ModbusFunction} for a code value.
	 *
	 * @param code
	 *        the code
	 * @return the function
	 * @throws IllegalArgumentException
	 *         if {@code code} is not supported
	 * @since 2.0
	 */
	static ModbusFunction functionForCode(int code) {
		ModbusFunction f;
		try {
			f = ModbusReadFunction.forCode(code);
		} catch ( IllegalArgumentException e ) {
			try {
				f = ModbusWriteFunction.forCode(code);
			} catch ( IllegalArgumentException e2 ) {
				throw new IllegalArgumentException("Unknown Modbus function code: " + code);
			}
		}
		return f;
	}

}
