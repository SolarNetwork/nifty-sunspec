/* ==================================================================
 * ModbusRegisterBlockType.java - 17/09/2020 4:07:04 PM
 * 
 * Copyright 2020 SolarNetwork.net Dev Team
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

import net.solarnetwork.domain.CodedValue;

/**
 * Modbus register block types.
 * 
 * @author matt
 * @version 1.0
 * @since 4.2
 */
public enum ModbusRegisterBlockType implements CodedValue {

	/** Coil (toggle) type. */
	Coil(1, 1, true, false),

	/** Discrete (input) type. */
	Discrete(2, 1, true, true),

	/** Holding (output) type. */
	Holding(3, 16, false, false),

	/** Input type. */
	Input(4, 16, false, true),

	;

	private final int code;
	private final int bitCount;
	private final boolean bitType;
	private final boolean readOnly;

	private ModbusRegisterBlockType(int code, int bitCount, boolean bitType, boolean readOnly) {
		this.code = code;
		this.bitCount = bitCount;
		this.bitType = bitType;
		this.readOnly = readOnly;
	}

	@Override
	public int getCode() {
		return code;
	}

	/**
	 * Get the number of bits registers of this type use.
	 * 
	 * @return the bit count
	 */
	public int getBitCount() {
		return bitCount;
	}

	/**
	 * Get the read-only flag.
	 * 
	 * @return {@literal true} if registers of this type are read-only
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Get the "bit type-ness" of this register block type.
	 * 
	 * @return {@literal true} if this is a coil or discrete register block
	 */
	public boolean isBitType() {
		return bitType;
	}

	/**
	 * Get an enumeration instance for a code value.
	 * 
	 * @param code
	 *        the code value to get the enumeration for
	 * @return the enumeration
	 * @throws IllegalArgumentException
	 *         if {@literal code} is not a valid value
	 */
	public static ModbusRegisterBlockType forCode(int code) {
		return switch (code) {
			case 1 -> Coil;
			case 2 -> Discrete;
			case 3 -> Holding;
			case 4 -> Input;
			default -> throw new IllegalArgumentException(
					"ModbusRegisterBlockType code [" + code + "] not supported.");
		};
	}

}
