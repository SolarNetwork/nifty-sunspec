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

import static net.solarnetwork.sunspec.modbus.ModbusDataType.StringUtf8;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for SunSpec compliant meters.
 * 
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address.
 * </p>
 * 
 * @author matt
 * @version 1.0
 */
public enum CommonModelRegister implements ModbusReference {

	/** Manufacturer name, as NULL-terminated string. */
	Manufacturer(0, StringUtf8, 16),

	/** Meter model, as NULL-terminated string. */
	Model(16, StringUtf8, 16),

	/** Meter options, as NULL-terminated string. */
	Options(32, StringUtf8, 8),

	/** Meter version, as NULL-terminated string. */
	Version(40, StringUtf8, 8),

	/** Serial number, as NULL-terminated string. */
	SerialNumber(48, StringUtf8, 16),

	/** The device address, which is the Modbus unit ID. */
	DeviceAddress(64, UInt16);

	private final int address;
	private final ModbusDataType dataType;
	private final int wordLength;

	private CommonModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, dataType.getWordLength());
	}

	private CommonModelRegister(int address, ModbusDataType dataType, int wordLength) {
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
