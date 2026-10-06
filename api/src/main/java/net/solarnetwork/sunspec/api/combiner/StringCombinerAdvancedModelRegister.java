/* ==================================================================
 * StringCombinerModelRegister.java - 10/09/2019 7:17:54 am
 *
 * Copyright 2019 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.api.combiner;

import static net.solarnetwork.sunspec.api.DataClassification.Accumulator;
import static net.solarnetwork.sunspec.api.DataClassification.Bitfield;
import static net.solarnetwork.sunspec.api.DataClassification.ScaleFactor;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt32;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReadFunction;
import net.solarnetwork.sunspec.modbus.SunspecModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec compliant advanced
 * string combiner model.
 *
 * <p>
 * These mappings correspond to the SunSpec model numbers <b>402</b> and
 * <b>404</b>. Where model 404 defines a point with a different data type than
 * model 402, the constant with a {@code V2} suffix provides the model 404
 * mapping.
 * </p>
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block.
 * </p>
 *
 * @author matt
 * @version 1.1
 * @since 1.4
 */
public enum StringCombinerAdvancedModelRegister implements SunspecModbusReference {

	/** Current scale factor, as *10^X. */
	ScaleFactorDcCurrent(0, Int16, ScaleFactor),

	/** Charge (amp hour) scale factor, as *10^X. */
	ScaleFactorDcCharge(1, Int16, ScaleFactor),

	/** Voltage scale factor, as *10^X. */
	ScaleFactorDcVoltage(2, Int16, ScaleFactor),

	/** Power scale factor, as *10^X. */
	ScaleFactorDcPower(3, Int16, ScaleFactor),

	/** Energy scale factor, as *10^X. */
	ScaleFactorDcEnergy(4, Int16, ScaleFactor),

	/** The maximum DC current rating, in amps. */
	DcCurrentMaxRating(5, UInt16),

	/** The count of inputs (repeating block). */
	InputCount(6, UInt16),

	/** Events, see {@link StringCombinerModelEvent}. */
	EventsBitmask(7, UInt32, Bitfield),

	/** Vendor events. */
	VendorEventsBitmask(9, UInt32, Bitfield),

	/** DC current, in amps. */
	DcCurrent(11, Int16),

	/** Total metered charge, in amp-hours (model 402). */
	DcCharge(12, UInt32),

	/**
	 * Total metered charge, in amp-hours (model 404).
	 *
	 * @since 1.1
	 */
	DcChargeV2(12, UInt32, Accumulator),

	/** Output voltage, in volts (model 402). */
	DcVoltage(14, UInt16),

	/**
	 * Output voltage, in volts (model 404).
	 *
	 * @since 1.1
	 */
	DcVoltageV2(14, Int16),

	/** Internal operating temperature, in degrees Celsius. */
	Temperature(15, Int16),

	/** Output power, in watts. */
	DcPower(16, Int16),

	/** Performance ratio, as a percentage (model 402). */
	DcPerformanceRatio(17, UInt16),

	/**
	 * Performance ratio, as a percentage (model 404).
	 *
	 * @since 1.1
	 */
	DcPerformanceRatioV2(17, Int16),

	/** Output energy, in watt-hours (model 402). */
	DcEnergy(18, UInt32),

	/**
	 * Output energy, in watt-hours (model 404).
	 *
	 * @since 1.1
	 */
	DcEnergyV2(18, UInt32, Accumulator),

	/** Input current scale factor, as *10^X. */
	ScaleFactorInputDcCurrent(20, Int16, ScaleFactor),

	/** Input charge (amp hour) scale factor, as *10^X. */
	ScaleFactorInputDcCharge(21, Int16, ScaleFactor),

	/** Input voltage scale factor, as *10^X. */
	ScaleFactorInputDcVoltage(22, Int16, ScaleFactor),

	/** Input power scale factor, as *10^X. */
	ScaleFactorInputDcPower(23, Int16, ScaleFactor),

	/** Input energy scale factor, as *10^X. */
	ScaleFactorInputDcEnergy(24, Int16, ScaleFactor),

	/** Input ID. */
	InputId(0, UInt16),

	/** Input events, see {@link StringCombinerModelEvent}. */
	InputEventsBitmask(1, UInt32, Bitfield),

	/** Input vendor events. */
	InputVendorEventsBitmask(3, UInt32, Bitfield),

	/** DC current, in amps. */
	InputDcCurrent(5, Int16),

	/** Total metered charge, in amp-hours (model 402). */
	InputDcCharge(6, UInt32),

	/**
	 * Total metered charge, in amp-hours (model 404).
	 *
	 * @since 1.1
	 */
	InputDcChargeV2(6, UInt32, Accumulator),

	/** String input voltage, in volts (model 402). */
	InputDcVoltage(8, UInt16),

	/**
	 * String input voltage, in volts (model 404).
	 *
	 * @since 1.1
	 */
	InputDcVoltageV2(8, Int16),

	/** String input power, in watts. */
	InputDcPower(9, Int16),

	/** String input energy, in watt-hours (model 402). */
	InputDcEnergy(10, UInt32),

	/**
	 * String input energy, in watt-hours (model 404).
	 *
	 * @since 1.1
	 */
	InputDcEnergyV2(10, UInt32, Accumulator),

	/** String performance ratio, as a percentage. */
	InputDcPerformanceRatio(12, UInt16),

	/** Number of modules in this input string. */
	InputModuleCount(13, UInt16);

	private final int address;
	private final ModbusDataType dataType;
	private final int wordLength;
	private final @Nullable DataClassification classification;

	private StringCombinerAdvancedModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, dataType.getWordLength());
	}

	private StringCombinerAdvancedModelRegister(int address, ModbusDataType dataType,
			@Nullable DataClassification classification) {
		this(address, dataType, dataType.getWordLength(), classification);
	}

	private StringCombinerAdvancedModelRegister(int address, ModbusDataType dataType, int wordLength) {
		this(address, dataType, wordLength, null);
	}

	private StringCombinerAdvancedModelRegister(int address, ModbusDataType dataType, int wordLength,
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
