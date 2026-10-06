/* ==================================================================
 * CommonModelRegister.java - 21/05/2018 5:14:54 PM
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

package net.solarnetwork.sunspec.api;

import static net.solarnetwork.sunspec.modbus.ModbusDataType.StringAscii;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReadFunction;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus model metadata mappings for SunSpec compliant meters.
 * 
 * @author matt
 * @version 1.0
 */
public enum ModelRegister implements ModbusReference {

	/**
	 * The preferred SunSpec base address, to contain the string
	 * {@literal SunS}.
	 * 
	 * <p>
	 * Note the common model block begins after the base 2 registers.
	 * </p>
	 */
	BaseAddress(40000, StringAscii, 2),

	/**
	 * The first alternate SunSpec base address, to contain the string
	 * {@literal SunS}.
	 */
	BaseAddressAlt1(50000, StringAscii, 2),

	/**
	 * The second alternate SunSpec base address, to contain the string
	 * {@literal SunS}.
	 */
	BaseAddressAlt2(0, StringAscii, 2),

	/** The model ID, block relative. */
	ModelId(0, UInt16),

	/** Model block length, block relative. */
	ModelLength(1, UInt16);

	/**
	 * The ASCII string content that must be in the base address to adhere to
	 * the SunSpec standard.
	 * 
	 * <p>
	 * In hex, this is {@literal 0x5375 6E53}.
	 * </p>
	 */
	public static final String BASE_ADDRESS_MAGIC_STRING = "SunS";

	/** A list of the base address registers, in order of priority. */
	public static final List<ModelRegister> BASE_ADDRESSES = Collections.unmodifiableList(Arrays.asList(
			ModelRegister.BaseAddress, ModelRegister.BaseAddressAlt1, ModelRegister.BaseAddressAlt2));

	private final int address;
	private final ModbusDataType dataType;
	private final int wordLength;

	private ModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, dataType.getWordLength());
	}

	private ModelRegister(int address, ModbusDataType dataType, int wordLength) {
		this.address = address;
		this.dataType = dataType;
		this.wordLength = wordLength;
	}

	@Override
	public int getAddress() {
		return address;
	}

	@Override
	public ModbusDataType getDataType() {
		return dataType;
	}

	@Override
	public ModbusReadFunction getFunction() {
		return ModbusReadFunction.ReadHoldingRegister;
	}

	/**
	 * Get the data type word length.
	 * 
	 * @return the word length
	 */
	@Override
	public int getWordLength() {
		return wordLength;
	}

}
