/* ==================================================================
 * ReferencePointModelRegister.java - 9/07/2023 8:11:03 am
 *
 * Copyright 2023 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.api.environmental;

import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int32;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReadFunction;
import net.solarnetwork.sunspec.modbus.SunspecModbusReference;

/**
 * Enumeration of Modbus register mappings for SunSpec compliant reference point
 * model 306.
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block.
 * </p>
 *
 * @author matt
 * @version 1.0
 * @since 4.2
 */
public enum ReferencePointModelRegister implements SunspecModbusReference {

	/** Global Horizontal Irradiance, in W/m2. */
	GHI(0, UInt16),

	/** Current measurement at reference point, in amps. */
	Amps(1, Int32),

	/** Voltage measurement at reference point, in volts. */
	Voltage(3, Int32),

	/** Temperature measurement at reference point, in degrees Celsius. */
	Temperature(5, Int32),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final int wordLength;
	private final @Nullable DataClassification classification;

	private ReferencePointModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, dataType.getWordLength());
	}

	private ReferencePointModelRegister(int address, ModbusDataType dataType, int wordLength) {
		this(address, dataType, wordLength, null);
	}

	private ReferencePointModelRegister(int address, ModbusDataType dataType, int wordLength,
			@Nullable DataClassification classification) {
		this.address = address;
		this.dataType = dataType;
		this.wordLength = wordLength;
		this.classification = classification;
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

	@Override
	public int getWordLength() {
		return wordLength;
	}

	@Override
	public @Nullable DataClassification getClassification() {
		return classification;
	}

}
