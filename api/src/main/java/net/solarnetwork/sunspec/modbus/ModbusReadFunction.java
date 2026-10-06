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

import org.jspecify.annotations.Nullable;

/**
 * Modbus read functions.
 *
 * <p>
 * These functions are separated from the write functions to help API methods be
 * explicit about requiring read versus write functions.
 * </p>
 *
 * @author matt
 * @version 2.1
 * @since 2.5
 */
public enum ModbusReadFunction implements ModbusFunction {

	/** Read coil. */
	ReadCoil(1, ModbusRegisterBlockType.Coil),

	/** Read discreet input. */
	ReadDiscreteInput(2, ModbusRegisterBlockType.Discrete),

	/** Read holding register. */
	ReadHoldingRegister(3, ModbusRegisterBlockType.Holding),

	/** Read input register. */
	ReadInputRegister(4, ModbusRegisterBlockType.Input);

	private final int code;
	private final ModbusRegisterBlockType blockType;

	private ModbusReadFunction(int code, ModbusRegisterBlockType blockType) {
		this.code = code;
		this.blockType = blockType;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String toDisplayString() {
		return this.toString().replaceAll("([a-z])([A-Z])", "$1 $2") + " (" + this.code + ")";
	}

	/**
	 * Get an enum instance for a code value.
	 *
	 * @param code
	 *        the code
	 * @return the enum
	 * @throws IllegalArgumentException
	 *         if {@code code} is not a valid value
	 */
	public static ModbusReadFunction forCode(int code) {
		for ( ModbusReadFunction e : ModbusReadFunction.values() ) {
			if ( code == e.code ) {
				return e;
			}
		}
		throw new IllegalArgumentException("Unknown ModbusReadFunction code [" + code + "]");
	}

	@Override
	public boolean isReadFunction() {
		return true;
	}

	@Override
	public @Nullable ModbusFunction oppositeFunction() {
		return switch (this) {
			case ReadCoil -> ModbusWriteFunction.WriteCoil;
			case ReadHoldingRegister -> ModbusWriteFunction.WriteHoldingRegister;
			default -> null;
		};
	}

	@Override
	public ModbusRegisterBlockType blockType() {
		return blockType;
	}

}
