/* ==================================================================
 * ModbusReadFunction.java - 21/12/2017 2:48:46 PM
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
 * Modbus read functions.
 *
 * <p>
 * These functions are separated from the write functions to help API methods be
 * explicit about requiring read versus write functions.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public enum ModbusReadFunction implements ModbusReadingFunction {

	/** Read coil. */
	ReadCoil(1),

	/** Read discreet input. */
	ReadDiscreteInput(2),

	/** Read holding register. */
	ReadHoldingRegister(3),

	/** Read input register. */
	ReadInputRegister(4),

	;

	private final int code;

	private ModbusReadFunction(int code) {
		this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}

}
