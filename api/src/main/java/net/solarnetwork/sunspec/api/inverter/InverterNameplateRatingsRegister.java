/* ==================================================================
 * InverterNameplateRatingsRegister.java - 15/10/2018 11:27:01 AM
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

package net.solarnetwork.sunspec.api.inverter;

import static net.solarnetwork.sunspec.api.DataClassification.Enumeration;
import static net.solarnetwork.sunspec.api.DataClassification.ScaleFactor;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReadFunction;
import net.solarnetwork.sunspec.modbus.SunspecModbusReference;

/**
 * Enumeration of Modbus register mappings for SunSpec model 120.
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block.
 * </p>
 *
 * @author matt
 * @version 1.1
 * @since 1.2
 */
public enum InverterNameplateRatingsRegister implements SunspecModbusReference {

	/** The DER type, see {@link DistributedEnergyResourceType}. */
	DerType(0, UInt16, Enumeration),

	// Active (Real) Power (W)

	/** Active power rating, reported in W. */
	ActivePowerRating(1, UInt16),

	/** Active power rating scale factor, as *10^X. */
	ScaleFactorActivePowerRating(2, Int16, ScaleFactor),

	// Apparent Power (VA)

	/** Apparent power rating, reported in VA. */
	ApparentPowerRating(3, UInt16),

	/** Apparent power rating scale factor, as *10^X. */
	ScaleFactorApparentPowerRating(4, Int16, ScaleFactor),

	// Reactive Power (VAR)

	/** Reactive power rating Q1, reported in VAR. */
	ReactivePowerQ1Rating(5, Int16),

	/** Reactive power rating Q2, reported in VAR. */
	ReactivePowerQ2Rating(6, Int16),

	/** Reactive power rating Q3, reported in VAR. */
	ReactivePowerQ3Rating(7, Int16),

	/** Reactive power rating Q4, reported in VAR. */
	ReactivePowerQ4Rating(8, Int16),

	/** Reactive power rating scale factor, as *10^X. */
	ScaleFactorReactivePowerRating(9, Int16, ScaleFactor),

	// Current

	/** AC current rating, in A. */
	CurrentRating(10, UInt16),

	/** AC current rating scale factor, as *10^X. */
	ScaleFactorCurrentRating(11, Int16, ScaleFactor),

	// Power factor

	/** AC power factor rating Q1, reported as a decimal -1..1. */
	PowerFactorQ1Rating(12, Int16),

	/** AC power factor rating Q2, reported as a decimal -1..1. */
	PowerFactorQ2Rating(13, Int16),

	/** AC power factor rating Q3, reported as a decimal -1..1. */
	PowerFactorQ3Rating(14, Int16),

	/** AC power factor rating Q4, reported as a decimal -1..1. */
	PowerFactorQ4Rating(15, Int16),

	/** AC power factor rating scale factor, as *10^X. */
	ScaleFactorPowerFactorRating(16, Int16, ScaleFactor),

	// Energy storage

	/** Stored energy capacity, in Wh. */
	StoredEnergyRating(17, UInt16),

	/** Stored energy capacity scale factor, as *10^X. */
	ScaleFactorStoredEnergyRating(18, Int16, ScaleFactor),

	/** Stored charge capacity, in Ah. */
	StoredChargeCapacity(19, UInt16),

	/** Stored charge capacity scale factor, as *10^X. */
	ScaleFactorStoredChargeCapacity(20, Int16, ScaleFactor),

	/** Stored energy maximum charge rating, in W. */
	StoredEnergyImportPowerRating(21, UInt16),

	/** Stored energy maximum charge rating scale factor, as *10^X. */
	ScaleFactorStoredEnergyImportPowerRating(22, Int16, ScaleFactor),

	/** Stored energy maximum discharge rating, in W. */
	StoredEnergyExportPowerRating(23, UInt16),

	/** Stored energy maximum discharge rating scale factor, as *10^X. */
	ScaleFactorStoredEnergyExportPowerRating(24, Int16, ScaleFactor);

	private final int address;
	private final ModbusDataType dataType;
	private final int wordLength;
	private final @Nullable DataClassification classification;

	private InverterNameplateRatingsRegister(int address, ModbusDataType dataType) {
		this(address, dataType, dataType.getWordLength());
	}

	private InverterNameplateRatingsRegister(int address, ModbusDataType dataType,
			@Nullable DataClassification classification) {
		this(address, dataType, dataType.getWordLength(), classification);
	}

	private InverterNameplateRatingsRegister(int address, ModbusDataType dataType, int wordLength) {
		this(address, dataType, wordLength, null);
	}

	private InverterNameplateRatingsRegister(int address, ModbusDataType dataType, int wordLength,
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

	/**
	 * Get the data type word length.
	 *
	 * @return the word length
	 */
	@Override
	public int getWordLength() {
		return wordLength;
	}

	@Override
	public @Nullable DataClassification getClassification() {
		return classification;
	}

}
