/* ==================================================================
 * LithiumIonModuleModelRegister.java - 5/10/2026 9:05:44 pm
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
import static net.solarnetwork.sunspec.api.DataClassification.ScaleFactor;
import static net.solarnetwork.sunspec.api.MeasurementUnits.DEGREE_CELSIUS;
import static net.solarnetwork.sunspec.api.MeasurementUnits.PERCENT;
import static net.solarnetwork.sunspec.api.MeasurementUnits.VOLT;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.StringUtf8;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt32;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.PointAccess;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec lithium-ion module
 * model.
 *
 * <p>
 * These mappings correspond to the SunSpec model number <b>805</b>.
 * </p>
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block, except for the {@code Cell*} registers, which are
 * encoded as an offset from the start of each cell block.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public enum LithiumIonModuleModelRegister implements ModbusReference {

	/** The index of the string containing the module, starting from 1. */
	StringIndex(0, UInt16),

	/** The index of the module within the string, starting from 1. */
	ModuleIndex(1, UInt16),

	/** The number of cells in the module. */
	NumberOfCells(2, UInt16),

	/** Module state of charge, as a percentage. */
	StateOfCharge(3, UInt16),

	/** Module depth of discharge, as a percentage. */
	DepthOfDischarge(4, UInt16),

	/** Module state of health, as a percentage. */
	StateOfHealth(5, UInt16),

	/** The number of cycles executed. */
	CycleCount(6, UInt32),

	/** Module voltage, in V. */
	DcVoltage(8, UInt16),

	/** Maximum cell voltage in the module. */
	MaximumCellVoltage(9, UInt16),

	/** Index of the cell with the maximum cell voltage. */
	MaximumCellVoltageCellIndex(10, UInt16),

	/** Minimum cell voltage in the module. */
	MinimumCellVoltage(11, UInt16),

	/** Index of the cell with the minimum cell voltage. */
	MinimumCellVoltageCellIndex(12, UInt16),

	/** Average cell voltage in the module. */
	AverageCellVoltage(13, UInt16),

	/** Maximum cell temperature in the module. */
	MaximumCellTemperature(14, Int16),

	/** Index of the cell with the maximum cell temperature. */
	MaximumCellTemperatureCellIndex(15, UInt16),

	/** Minimum cell temperature in the module. */
	MinimumCellTemperature(16, Int16),

	/** Index of the cell with the minimum cell temperature. */
	MinimumCellTemperatureCellIndex(17, UInt16),

	/** Average cell temperature in the module. */
	AverageCellTemperature(18, Int16),

	/** The number of cells currently being balanced. */
	BalancingCellCount(19, UInt16),

	/** The module serial number. */
	SerialNumber(20, StringUtf8, 16),

	/** State of charge scale factor. */
	ScaleFactorStateOfCharge(36, Int16, ScaleFactor),

	/** State of health scale factor. */
	ScaleFactorStateOfHealth(37, Int16, ScaleFactor),

	/** Depth of discharge scale factor. */
	ScaleFactorDepthOfDischarge(38, Int16, ScaleFactor),

	/** Voltage scale factor. */
	ScaleFactorVoltage(39, Int16, ScaleFactor),

	/** Cell voltage scale factor. */
	ScaleFactorCellVoltage(40, Int16, ScaleFactor),

	/** Cell temperature scale factor. */
	ScaleFactorTemperature(41, Int16, ScaleFactor),

	// cells

	/** Cell voltage, in V. */
	CellVoltage(0, UInt16),

	/** Cell temperature, in degrees Celsius. */
	CellTemperature(1, Int16),

	/** Cell status, see {@link LithiumIonCellStatus}. */
	CellStatus(2, UInt32, Bitfield),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final int wordLength;
	private final @Nullable DataClassification classification;
	private final PointAccess access;

	private LithiumIonModuleModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, dataType.getWordLength(), null, PointAccess.ReadOnly);
	}

	private LithiumIonModuleModelRegister(int address, ModbusDataType dataType,
			DataClassification classification) {
		this(address, dataType, dataType.getWordLength(), classification, PointAccess.ReadOnly);
	}

	private LithiumIonModuleModelRegister(int address, ModbusDataType dataType, int wordLength) {
		this(address, dataType, wordLength, null, PointAccess.ReadOnly);
	}

	private LithiumIonModuleModelRegister(int address, ModbusDataType dataType, int wordLength,
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
			case StateOfCharge, DepthOfDischarge, StateOfHealth -> PERCENT;
			case DcVoltage, MaximumCellVoltage, MinimumCellVoltage, AverageCellVoltage -> VOLT;
			case CellVoltage -> VOLT;
			case MaximumCellTemperature, MinimumCellTemperature -> DEGREE_CELSIUS;
			case AverageCellTemperature, CellTemperature -> DEGREE_CELSIUS;
			default -> null;
		};
	}

}
