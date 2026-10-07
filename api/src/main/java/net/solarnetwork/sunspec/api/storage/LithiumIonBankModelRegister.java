/* ==================================================================
 * LithiumIonBankModelRegister.java - 5/10/2026 9:05:44 pm
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

package net.solarnetwork.sunspec.api.storage;

import static net.solarnetwork.sunspec.api.DataClassification.Bitfield;
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
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec lithium-ion battery
 * bank model.
 *
 * <p>
 * These mappings correspond to the SunSpec model number <b>803</b>.
 * </p>
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block, except for the {@code String*} registers, which
 * are encoded as an offset from the start of each string block.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public enum LithiumIonBankModelRegister implements ModbusReference {

	/** The number of strings in the bank. */
	NumberOfStrings(0, UInt16),

	/** The number of strings with their contactor closed. */
	NumberOfConnectedStrings(1, UInt16),

	/** Maximum module temperature in the bank. */
	MaximumModuleTemperature(2, Int16),

	/**
	 * Index of the string containing the module with the maximum module
	 * temperature.
	 */
	MaximumModuleTemperatureStringIndex(3, UInt16),

	/** Index of the module with the maximum module temperature. */
	MaximumModuleTemperatureModuleIndex(4, UInt16),

	/** Minimum module temperature in the bank. */
	MinimumModuleTemperature(5, Int16),

	/**
	 * Index of the string containing the module with the minimum module
	 * temperature.
	 */
	MinimumModuleTemperatureStringIndex(6, UInt16),

	/** Index of the module with the minimum module temperature. */
	MinimumModuleTemperatureModuleIndex(7, UInt16),

	/** Average module temperature in the bank. */
	AverageModuleTemperature(8, Int16),

	/** Maximum string voltage in the bank. */
	MaximumStringVoltage(9, UInt16),

	/** Index of the string with the maximum string voltage. */
	MaximumStringVoltageStringIndex(10, UInt16),

	/** Minimum string voltage in the bank. */
	MinimumStringVoltage(11, UInt16),

	/** Index of the string with the minimum string voltage. */
	MinimumStringVoltageStringIndex(12, UInt16),

	/** Average string voltage in the bank. */
	AverageStringVoltage(13, UInt16),

	/** Maximum string current in the bank. */
	MaximumStringCurrent(14, Int16),

	/** Index of the string with the maximum string current. */
	MaximumStringCurrentStringIndex(15, UInt16),

	/** Minimum string current in the bank. */
	MinimumStringCurrent(16, Int16),

	/** Index of the string with the minimum string current. */
	MinimumStringCurrentStringIndex(17, UInt16),

	/** Average string current in the bank. */
	AverageStringCurrent(18, Int16),

	/** The number of cells currently being balanced. */
	BalancingCellCount(19, UInt16),

	/** Cell voltage scale factor. */
	ScaleFactorCellVoltage(20, Int16, ScaleFactor),

	/** Module temperature scale factor. */
	ScaleFactorModuleTemperature(21, Int16, ScaleFactor),

	/** Current scale factor. */
	ScaleFactorCurrent(22, Int16, ScaleFactor),

	/** State of health scale factor. */
	ScaleFactorStateOfHealth(23, Int16, ScaleFactor),

	/** State of charge scale factor. */
	ScaleFactorStateOfCharge(24, Int16, ScaleFactor),

	/** Voltage scale factor. */
	ScaleFactorVoltage(25, Int16, ScaleFactor),

	// strings

	/** The number of modules in the string. */
	StringModuleCount(0, UInt16),

	/** String status, see {@link BatteryConnectionStatus}. */
	StringStatus(1, UInt32, Bitfield),

	/** Connection failure reason, see {@link BatteryConnectionFailure}. */
	StringConnectionFailure(3, UInt16, Enumeration),

	/** String state of charge, as a percentage. */
	StringStateOfCharge(4, UInt16),

	/** String state of health, as a percentage. */
	StringStateOfHealth(5, UInt16),

	/** String current, in A. */
	StringDcCurrent(6, Int16),

	/** Maximum cell voltage in the string. */
	StringMaximumCellVoltage(7, UInt16),

	/**
	 * Index of the module containing the cell with the maximum cell voltage.
	 */
	StringMaximumCellVoltageModuleIndex(8, UInt16),

	/** Minimum cell voltage in the string. */
	StringMinimumCellVoltage(9, UInt16),

	/**
	 * Index of the module containing the cell with the minimum cell voltage.
	 */
	StringMinimumCellVoltageModuleIndex(10, UInt16),

	/** Average cell voltage in the string. */
	StringAverageCellVoltage(11, UInt16),

	/** Maximum module temperature in the string. */
	StringMaximumModuleTemperature(12, Int16),

	/** Index of the module with the maximum module temperature. */
	StringMaximumModuleTemperatureModuleIndex(13, UInt16),

	/** Minimum module temperature in the string. */
	StringMinimumModuleTemperature(14, Int16),

	/** Index of the module with the minimum module temperature. */
	StringMinimumModuleTemperatureModuleIndex(15, UInt16),

	/** Average module temperature in the string. */
	StringAverageModuleTemperature(16, Int16),

	/** The reason the string is disabled, see {@link BatteryDisabledReason}. */
	StringDisabledReason(17, UInt16, Enumeration),

	/** Contactor status bitmask, where each set bit is a closed contactor. */
	StringContactorStatus(18, UInt32, Bitfield),

	/** String events, see {@link LithiumIonStringEvent}. */
	StringEventsBitmask(20, UInt32, Bitfield),

	/** String events 2, which SunSpec defines no events for. */
	StringEvents2Bitmask(22, UInt32, Bitfield),

	/** Vendor events 1. */
	StringVendorEventsBitmask(24, UInt32, Bitfield),

	/** Vendor events 2. */
	StringVendorEvents2Bitmask(26, UInt32, Bitfield),

	/**
	 * String enable or disable operation, see {@link BatteryEnableOperation}.
	 */
	StringEnableOperation(28, UInt16, Enumeration, ReadWrite),

	/** String connect or disconnect operation, see {@link BatteryOperation}. */
	StringConnectOperation(29, UInt16, Enumeration, ReadWrite),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final int wordLength;
	private final @Nullable DataClassification classification;
	private final PointAccess access;

	private LithiumIonBankModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, dataType.getWordLength(), null, PointAccess.ReadOnly);
	}

	private LithiumIonBankModelRegister(int address, ModbusDataType dataType,
			DataClassification classification) {
		this(address, dataType, dataType.getWordLength(), classification, PointAccess.ReadOnly);
	}

	private LithiumIonBankModelRegister(int address, ModbusDataType dataType,
			DataClassification classification, PointAccess access) {
		this(address, dataType, dataType.getWordLength(), classification, access);
	}

	private LithiumIonBankModelRegister(int address, ModbusDataType dataType, int wordLength,
			@Nullable DataClassification classification, PointAccess access) {
		this.address = address;
		this.dataType = dataType;
		this.wordLength = wordLength;
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
			case StringEventsBitmask -> "StringEvents";
			case StringEvents2Bitmask -> "StringEvents2";
			case StringVendorEventsBitmask -> "StringVendorEvents";
			case StringVendorEvents2Bitmask -> "StringVendorEvents2";
			default -> name();
		};
	}

	@Override
	public PointAccess getAccess() {
		return access;
	}

}
