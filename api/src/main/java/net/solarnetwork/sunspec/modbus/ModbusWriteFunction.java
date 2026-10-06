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
 * Modbus write functions.
 *
 * <p>
 * These functions are separated from the read functions to help API methods be
 * explicit about requiring write versus read functions.
 * </p>
 *
 * @author matt
 * @version 2.1
 * @since 2.5
 */
public enum ModbusWriteFunction implements ModbusFunction {

	/** Write a coil register. */
	WriteCoil(5, ModbusRegisterBlockType.Coil),

	/** Write a holding register. */
	WriteHoldingRegister(6, ModbusRegisterBlockType.Holding),

	/** Write multiple coils. */
	WriteMultipleCoils(15, ModbusRegisterBlockType.Coil),

	/** Write multiple holding. */
	WriteMultipleHoldingRegisters(16, ModbusRegisterBlockType.Holding);

	private final int code;
	private final ModbusRegisterBlockType blockType;

	private ModbusWriteFunction(int code, ModbusRegisterBlockType blockType) {
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
	public static ModbusWriteFunction forCode(int code) {
		for ( ModbusWriteFunction e : ModbusWriteFunction.values() ) {
			if ( code == e.code ) {
				return e;
			}
		}
		throw new IllegalArgumentException("Unknown ModbusWriteFunction code [" + code + "]");
	}

	@Override
	public boolean isReadFunction() {
		return false;
	}

	@Override
	public @Nullable ModbusFunction oppositeFunction() {
		return switch (this) {
			case WriteCoil, WriteMultipleCoils -> ModbusReadFunction.ReadCoil;
			case WriteHoldingRegister, WriteMultipleHoldingRegisters -> ModbusReadFunction.ReadHoldingRegister;
			default -> null;
		};
	}

	@Override
	public ModbusRegisterBlockType blockType() {
		return blockType;
	}

}
