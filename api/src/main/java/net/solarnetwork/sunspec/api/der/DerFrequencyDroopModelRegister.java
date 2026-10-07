/* ==================================================================
 * DerFrequencyDroopModelRegister.java - 5/10/2026 7:12:33 pm
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
import static net.solarnetwork.sunspec.api.MeasurementUnits.HERTZ;
import static net.solarnetwork.sunspec.api.MeasurementUnits.PERCENT;
import static net.solarnetwork.sunspec.api.MeasurementUnits.SECOND;
import static net.solarnetwork.sunspec.api.PointAccess.ReadWrite;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt32;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.PointAccess;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec DER frequency droop
 * model.
 *
 * <p>
 * These mappings correspond to the SunSpec model number <b>711</b>.
 * </p>
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block, except for the {@code Control*} registers, which
 * are encoded as an offset from the start of each control block.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public enum DerFrequencyDroopModelRegister implements ModbusReference {

	/** The function enable setting. */
	Enabled(0, UInt16, Enumeration, ReadWrite),

	/** The index of the control to make active. */
	AdoptControlRequest(1, UInt16, ReadWrite),

	/** The result of the last adopt control request. */
	AdoptControlResult(2, UInt16, Enumeration),

	/** The number of controls. */
	NumberOfControls(3, UInt16),

	/** The reversion timeout, in seconds. */
	ReversionTime(4, UInt32, ReadWrite),

	/** The reversion time remaining, in seconds. */
	ReversionTimeRemaining(6, UInt32),

	/** The index of the control to adopt when the reversion timeout expires. */
	ReversionControl(8, UInt16, ReadWrite),

	/** The deadband scale factor. */
	ScaleFactorDeadband(9, Int16, ScaleFactor),

	/** The frequency change ratio scale factor. */
	ScaleFactorChangeRatio(10, Int16, ScaleFactor),

	/** The open loop response time scale factor. */
	ScaleFactorResponseTime(11, Int16, ScaleFactor),

	// controls

	/** The over-frequency deadband, in hertz. */
	ControlOverFrequencyDeadband(0, UInt32, ReadWrite),

	/** The under-frequency deadband, in hertz. */
	ControlUnderFrequencyDeadband(2, UInt32, ReadWrite),

	/** The over-frequency change ratio. */
	ControlOverFrequencyChangeRatio(4, UInt16, ReadWrite),

	/** The under-frequency change ratio. */
	ControlUnderFrequencyChangeRatio(5, UInt16, ReadWrite),

	/** The open loop response time, in seconds. */
	ControlOpenLoopResponseTime(6, UInt32, ReadWrite),

	/**
	 * The minimum active power, as a percentage of the active power rating.
	 */
	ControlMinimumActivePower(8, Int16, ReadWrite),

	/** The control read-only setting. */
	ControlReadOnly(9, UInt16, Enumeration),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final @Nullable DataClassification classification;
	private final PointAccess access;

	private DerFrequencyDroopModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, null, PointAccess.ReadOnly);
	}

	private DerFrequencyDroopModelRegister(int address, ModbusDataType dataType,
			DataClassification classification) {
		this(address, dataType, classification, PointAccess.ReadOnly);
	}

	private DerFrequencyDroopModelRegister(int address, ModbusDataType dataType, PointAccess access) {
		this(address, dataType, null, access);
	}

	private DerFrequencyDroopModelRegister(int address, ModbusDataType dataType,
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

	@Override
	public int getWordLength() {
		return dataType.getWordLength();
	}

	@Override
	public @Nullable DataClassification getClassification() {
		return classification;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public PointAccess getAccess() {
		return access;
	}

	@Override
	public @Nullable String getMeasurementUnit() {
		return switch (this) {
			case ReversionTime, ReversionTimeRemaining, ControlOpenLoopResponseTime -> SECOND;
			case ControlOverFrequencyDeadband, ControlUnderFrequencyDeadband -> HERTZ;
			case ControlMinimumActivePower -> PERCENT;
			default -> null;
		};
	}

}
