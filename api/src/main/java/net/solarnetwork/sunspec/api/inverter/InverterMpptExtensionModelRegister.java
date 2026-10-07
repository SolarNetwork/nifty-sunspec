/* ==================================================================
 * InverterMpptExtensionModelRegister.java - 6/09/2019 5:59:57 pm
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

package net.solarnetwork.sunspec.api.inverter;

import static net.solarnetwork.sunspec.api.DataClassification.Accumulator;
import static net.solarnetwork.sunspec.api.DataClassification.Bitfield;
import static net.solarnetwork.sunspec.api.DataClassification.ScaleFactor;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.StringUtf8;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt32;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec compliant MPPT
 * inverter extension model.
 *
 * <p>
 * These mappings correspond to the SunSpec model number <b>160</b>.
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
public enum InverterMpptExtensionModelRegister implements ModbusReference {

	/** Current scale factor, as *10^X. */
	ScaleFactorDcCurrent(0, Int16, ScaleFactor),

	/** Voltage scale factor, as *10^X. */
	ScaleFactorDcVoltage(1, Int16, ScaleFactor),

	/** Power scale factor, as *10^X. */
	ScaleFactorDcPower(2, Int16, ScaleFactor),

	/** Energy scale factor, as *10^X. */
	ScaleFactorDcEnergy(3, Int16, ScaleFactor),

	/** Global events bitmask, see {@link InverterMpptExtensionModelEvent}. */
	EventsBitmask(4, UInt32, Bitfield),

	/** The count of modules (repeating block). */
	ModuleCount(6, UInt16),

	/** The timestamp period. */
	TimestampPeriod(7, UInt16),

	/** Module input ID. */
	ModuleInputId(0, UInt16),

	/** Module name. */
	ModuleName(1, StringUtf8, 8),

	/** Module DC current, in amps. */
	ModuleDcCurrent(9, UInt16),

	/** Module DC voltage, in volts. */
	ModuleDcVoltage(10, UInt16),

	/** Module DC power, in watts. */
	ModuleDcPower(11, UInt16),

	/** Module lifetime energy, in watt-hours. */
	ModuleLifetimeEnergy(12, UInt32, Accumulator),

	/** Module timestamp, in seconds. */
	ModuleTimestamp(14, UInt32),

	/** Module temperature, in degrees Celsius. */
	ModuleTemperature(16, Int16),

	/** Module operating state, see {@link InverterOperatingState}. */
	ModuleOperatingState(17, UInt16),

	/** Module events, see {@link InverterMpptExtensionModelEvent}. */
	ModuleEventsBitmask(18, UInt32, Bitfield);

	private final int address;
	private final ModbusDataType dataType;
	private final int wordLength;
	private final @Nullable DataClassification classification;

	private InverterMpptExtensionModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, dataType.getWordLength());
	}

	private InverterMpptExtensionModelRegister(int address, ModbusDataType dataType,
			@Nullable DataClassification classification) {
		this(address, dataType, dataType.getWordLength(), classification);
	}

	private InverterMpptExtensionModelRegister(int address, ModbusDataType dataType, int wordLength) {
		this(address, dataType, wordLength, null);
	}

	private InverterMpptExtensionModelRegister(int address, ModbusDataType dataType, int wordLength,
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

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This is the enumeration constant name, without the {@code Bitmask} suffix
	 * of bitfield points.
	 * </p>
	 */
	@Override
	public String getName() {
		return switch (this) {
			case EventsBitmask -> "Events";
			case ModuleEventsBitmask -> "ModuleEvents";
			default -> name();
		};
	}

}
