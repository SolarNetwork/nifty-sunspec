/* ==================================================================
 * InverterBasicSettingsRegister.java - 15/10/2018 2:20:12 PM
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
import static net.solarnetwork.sunspec.api.PointAccess.ReadWrite;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.AcPhase;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.PointAccess;
import net.solarnetwork.sunspec.api.ReactivePowerAction;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for SunSpec model 121.
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public enum InverterBasicSettingsRegister implements ModbusReference {

	// Active (Real) Power (W)

	/** Active power maximum, reported in W. */
	ActivePowerMaximum(0, UInt16, ReadWrite),

	/** Active power maximum scale factor, as *10^X. */
	ScaleFactorActivePowerMaximum(20, Int16, ScaleFactor),

	// Voltage

	/** PCC voltage, in V. */
	VoltagePcc(1, UInt16, ReadWrite),

	/** PCC voltage scale factor, as *10^X. */
	ScaleFactorVoltagePcc(21, Int16, ScaleFactor),

	/** Voltage offset from the PCC to the inverter, in V. */
	VoltagePccOffset(2, Int16, ReadWrite),

	/** PCC voltage offset scale factor, as *10^X. */
	ScaleFactorVoltagePccOffset(22, Int16, ScaleFactor),

	/** Maximum voltage, in V. */
	VoltageMaximum(3, UInt16, ReadWrite),

	/** Minimum voltage, in V. */
	VoltageMinimum(4, UInt16, ReadWrite),

	/** Minimum and maximum voltage scale factor, as *10^X. */
	ScaleFactorVoltageMinimumMaximum(23, Int16, ScaleFactor),

	// Apparent Power (VA)

	/** Apparent power maximum, reported in VA. */
	ApparentPowerMaximum(5, UInt16, ReadWrite),

	/** Apparent power maximum scale factor, as *10^X. */
	ScaleFactorApparentPowerMaximum(24, Int16, ScaleFactor),

	// Reactive Power (VAR)

	/** Reactive power maximum Q1, reported in VAR. */
	ReactivePowerQ1Maximum(6, Int16, ReadWrite),

	/** Reactive power maximum Q2, reported in VAR. */
	ReactivePowerQ2Maximum(7, Int16, ReadWrite),

	/** Reactive power maximum Q3, reported in VAR. */
	ReactivePowerQ3Maximum(8, Int16, ReadWrite),

	/** Reactive power maximum Q4, reported in VAR. */
	ReactivePowerQ4Maximum(9, Int16, ReadWrite),

	/** Reactive power maximum scale factor, as *10^X. */
	ScaleFactorReactivePowerMaximum(25, Int16, ScaleFactor),

	// Ramp rate

	/** Active power ramp rate, in % active power maximum / second. */
	ActivePowerRampRate(10, UInt16, ReadWrite),

	/** Active power ramp rate scale factor, as *10^X. */
	ScaleFactorActivePowerRampRate(26, Int16, ScaleFactor),

	/** Active power ramp rate maximum, in % ActivePowerRampRate. */
	ActivePowerRampRateMaximum(17, UInt16, ReadWrite),

	/** Active power ramp rate maximum scale factor, as *10^X. */
	ScaleFactorActivePowerRampRateMaximum(28, Int16, ScaleFactor),

	// Power factor

	/** AC power factor minimum Q1, reported as a decimal -1..1. */
	PowerFactorQ1Minimum(11, Int16, ReadWrite),

	/** AC power factor minimum Q2, reported as a decimal -1..1. */
	PowerFactorQ2Minimum(12, Int16, ReadWrite),

	/** AC power factor minimum Q3, reported as a decimal -1..1. */
	PowerFactorQ3Minimum(13, Int16, ReadWrite),

	/** AC power factor minimum Q4, reported as a decimal -1..1. */
	PowerFactorQ4Minimum(14, Int16, ReadWrite),

	/** AC power factor minimum scale factor, as *10^X. */
	ScaleFactorPowerFactorMinimum(27, Int16, ScaleFactor),

	// Reactive power action

	/**
	 * Reactive power charge/discharge change action, see
	 * {@link ReactivePowerAction}.
	 */
	ImportExportChangeReactivePowerAction(15, UInt16, Enumeration, ReadWrite),

	// Apparent power calculation method

	/**
	 * Apparent power calculation method, see
	 * {@link ApparentPowerCalculationMethod}.
	 */
	ApparentPowerCalculationMethod(16, UInt16, Enumeration, ReadWrite),

	// Frequency

	/** Nominal frequency at the electrical connection point (ECP), in Hz. */
	EcpNominalFrequency(18, UInt16, ReadWrite),

	/** Nominal ECP frequency scale factor, as *10^X. */
	ScaleFactorEcpNominalFrequency(29, Int16, ScaleFactor),

	// Phase

	/** Connected phase for single-phase inverters, see {@link AcPhase}. */
	ConnectedPhase(19, UInt16, Enumeration, ReadWrite);

	private final int address;
	private final ModbusDataType dataType;
	private final @Nullable DataClassification classification;
	private final PointAccess access;

	private InverterBasicSettingsRegister(int address, ModbusDataType dataType,
			DataClassification classification) {
		this(address, dataType, classification, PointAccess.ReadOnly);
	}

	private InverterBasicSettingsRegister(int address, ModbusDataType dataType, PointAccess access) {
		this(address, dataType, null, access);
	}

	private InverterBasicSettingsRegister(int address, ModbusDataType dataType,
			@Nullable DataClassification classification, PointAccess access) {
		this.address = address;
		this.dataType = dataType;
		this.classification = classification;
		this.access = access;
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
		return dataType.getWordLength();
	}

	@Override
	public @Nullable DataClassification getClassification() {
		return classification;
	}

	@Override
	public PointAccess getAccess() {
		return access;
	}

}
