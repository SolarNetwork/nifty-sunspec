/* ==================================================================
 * IrradianceModelRegister.java - 5/07/2023 8:09:00 am
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

import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for SunSpec compliant irradiance
 * model 302.
 *
 * <p>
 * The model has no fixed block, so all register addresses are encoded as an
 * offset from the start of each repeating block instance.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public enum IrradianceModelRegister implements ModbusReference {

	/** Global Horizontal Irradiance, in W/m2. */
	GHI(0, UInt16),

	/** Plane-of-Array Irradiance, in W/m2. */
	POAI(1, UInt16),

	/** Diffuse Irradiance, in W/m2. */
	DFI(2, UInt16),

	/** Direct Normal Irradiance, in W/m2. */
	DNI(3, UInt16),

	/** Other Irradiance, in W/m2. */
	OTI(4, UInt16),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final int wordLength;
	private final @Nullable DataClassification classification;

	private IrradianceModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, dataType.getWordLength());
	}

	private IrradianceModelRegister(int address, ModbusDataType dataType, int wordLength) {
		this(address, dataType, wordLength, null);
	}

	private IrradianceModelRegister(int address, ModbusDataType dataType, int wordLength,
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
	public int getWordLength() {
		return wordLength;
	}

	@Override
	public @Nullable DataClassification getClassification() {
		return classification;
	}

}
