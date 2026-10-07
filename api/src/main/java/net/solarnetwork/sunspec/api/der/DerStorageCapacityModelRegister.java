/* ==================================================================
 * DerStorageCapacityModelRegister.java - 5/10/2026 10:18:41 am
 *
 * Copyright 2026 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.api.der;

import static net.solarnetwork.sunspec.api.DataClassification.Enumeration;
import static net.solarnetwork.sunspec.api.DataClassification.ScaleFactor;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec DER storage capacity
 * model.
 *
 * <p>
 * These mappings correspond to the SunSpec model number <b>713</b>.
 * </p>
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public enum DerStorageCapacityModelRegister implements ModbusReference {

	/** Energy rating of the storage, in Wh. */
	EnergyRating(0, UInt16),

	/** Energy available in the storage, in Wh. */
	EnergyAvailable(1, UInt16),

	/** State of charge, as a percentage. */
	StateOfCharge(2, UInt16),

	/** State of health, as a percentage. */
	StateOfHealth(3, UInt16),

	/** Storage status, see {@link DerStorageStatus}. */
	Status(4, UInt16, Enumeration),

	/** Energy scale factor. */
	ScaleFactorEnergy(5, Int16, ScaleFactor),

	/** Percentage scale factor. */
	ScaleFactorPercent(6, Int16, ScaleFactor),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final @Nullable DataClassification classification;

	private DerStorageCapacityModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, null);
	}

	private DerStorageCapacityModelRegister(int address, ModbusDataType dataType,
			@Nullable DataClassification classification) {
		this.address = address;
		this.dataType = dataType;
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
		return dataType.getWordLength();
	}

	@Override
	public @Nullable DataClassification getClassification() {
		return classification;
	}

}
