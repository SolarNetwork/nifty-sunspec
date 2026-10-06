/* ==================================================================
 * DerEnterServiceModelRegister.java - 5/10/2026 9:32:29 am
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
import static net.solarnetwork.sunspec.api.PointAccess.ReadWrite;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt32;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.PointAccess;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReadFunction;
import net.solarnetwork.sunspec.modbus.SunspecModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec DER enter service
 * model.
 *
 * <p>
 * These mappings correspond to the SunSpec model number <b>703</b>.
 * </p>
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block.
 * </p>
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum DerEnterServiceModelRegister implements SunspecModbusReference {

	/** Permit enter service, as disabled (0) or enabled (1). */
	Permitted(0, UInt16, Enumeration, ReadWrite),

	/**
	 * Enter service voltage high threshold, as a percentage of nominal voltage.
	 */
	VoltageHigh(1, UInt16, ReadWrite),

	/**
	 * Enter service voltage low threshold, as a percentage of nominal voltage.
	 */
	VoltageLow(2, UInt16, ReadWrite),

	/** Enter service frequency high threshold, in Hz. */
	FrequencyHigh(3, UInt32, ReadWrite),

	/** Enter service frequency low threshold, in Hz. */
	FrequencyLow(5, UInt32, ReadWrite),

	/** Enter service delay time, in seconds. */
	Delay(7, UInt32, ReadWrite),

	/** Enter service random delay, in seconds. */
	RandomDelay(9, UInt32, ReadWrite),

	/** Enter service ramp time, in seconds. */
	RampTime(11, UInt32, ReadWrite),

	/** Enter service delay time remaining, in seconds. */
	DelayRemaining(13, UInt32),

	/** Voltage percentage scale factor, as *10^X. */
	ScaleFactorVoltage(15, Int16, ScaleFactor),

	/** Frequency scale factor, as *10^X. */
	ScaleFactorFrequency(16, Int16, ScaleFactor),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final @Nullable DataClassification classification;
	private final PointAccess access;

	private DerEnterServiceModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, null, PointAccess.ReadOnly);
	}

	private DerEnterServiceModelRegister(int address, ModbusDataType dataType,
			DataClassification classification) {
		this(address, dataType, classification, PointAccess.ReadOnly);
	}

	private DerEnterServiceModelRegister(int address, ModbusDataType dataType, PointAccess access) {
		this(address, dataType, null, access);
	}

	private DerEnterServiceModelRegister(int address, ModbusDataType dataType,
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
	public ModbusReadFunction getFunction() {
		return ModbusReadFunction.ReadHoldingRegister;
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
	public PointAccess getAccess() {
		return access;
	}

}
