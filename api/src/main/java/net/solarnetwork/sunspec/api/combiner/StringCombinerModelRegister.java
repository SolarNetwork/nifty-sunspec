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
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec compliant basic
 * string combiner model.
 *
 * <p>
 * These mappings correspond to the SunSpec model numbers <b>401</b> and
 * <b>403</b>. Where model 403 defines a point with a different data type than
 * model 401, the constant with a {@code V2} suffix provides the model 403
 * mapping.
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
public enum StringCombinerModelRegister implements ModbusReference {

	/** Current scale factor, as *10^X. */
	ScaleFactorDcCurrent(0, Int16, ScaleFactor),

	/** Charge (amp hour) scale factor, as *10^X. */
	ScaleFactorDcCharge(1, Int16, ScaleFactor),

	/** Voltage scale factor, as *10^X. */
	ScaleFactorDcVoltage(2, Int16, ScaleFactor),

	/** The maximum DC current rating, in amps. */
	DcCurrentMaxRating(3, UInt16),

	/** The count of inputs (repeating block). */
	InputCount(4, UInt16),

	/** Events, see {@link StringCombinerModelEvent}. */
	EventsBitmask(5, UInt32, Bitfield),

	/** Vendor events. */
	VendorEventsBitmask(7, UInt32, Bitfield),

	/** DC current, in amps. */
	DcCurrent(9, Int16),

	/** Total metered charge, in amp-hours (model 401). */
	DcCharge(10, UInt32),

	/**
	 * Total metered charge, in amp-hours (model 403).
	 *
	 */
	DcChargeV2(10, UInt32, Accumulator),

	/** Output voltage, in volts (model 401). */
	DcVoltage(12, UInt16),

	/**
	 * Output voltage, in volts (model 403).
	 *
	 */
	DcVoltageV2(12, Int16),

	/** Internal operating temperature, in degrees Celsius. */
	Temperature(13, Int16),

	/** Current scale factor, as *10^X. */
	ScaleFactorInputDcCurrent(14, Int16, ScaleFactor),

	/** Charge (amp hour) scale factor, as *10^X. */
	ScaleFactorInputDcCharge(15, Int16, ScaleFactor),

	/** Input ID. */
	InputId(0, UInt16),

	/** Input events, see {@link StringCombinerModelEvent}. */
	InputEventsBitmask(1, UInt32, Bitfield),

	/** Input vendor events. */
	InputVendorEventsBitmask(3, UInt32, Bitfield),

	/** DC current, in amps. */
	InputDcCurrent(5, Int16),

	/** Total metered charge, in amp-hours (model 401). */
	InputDcCharge(6, UInt32),

	/**
	 * Total metered charge, in amp-hours (model 403).
	 *
	 */
	InputDcChargeV2(6, UInt32, Accumulator),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final int wordLength;
	private final @Nullable DataClassification classification;

	private StringCombinerModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, dataType.getWordLength());
	}

	private StringCombinerModelRegister(int address, ModbusDataType dataType,
			@Nullable DataClassification classification) {
		this(address, dataType, dataType.getWordLength(), classification);
	}

	private StringCombinerModelRegister(int address, ModbusDataType dataType, int wordLength) {
		this(address, dataType, wordLength, null);
	}

	private StringCombinerModelRegister(int address, ModbusDataType dataType, int wordLength,
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

	@Override
	public String getName() {
		return name();
	}

}
