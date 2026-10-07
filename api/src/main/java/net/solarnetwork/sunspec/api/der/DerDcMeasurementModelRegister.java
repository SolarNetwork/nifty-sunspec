/* ==================================================================
 * DerDcMeasurementModelRegister.java - 5/10/2026 10:24:03 am
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

import static net.solarnetwork.sunspec.api.DataClassification.Bitfield;
import static net.solarnetwork.sunspec.api.DataClassification.Enumeration;
import static net.solarnetwork.sunspec.api.DataClassification.ScaleFactor;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.StringUtf8;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt32;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt64;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec DER DC measurement
 * model.
 *
 * <p>
 * These mappings correspond to the SunSpec model number <b>714</b>.
 * </p>
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block, except for the {@code Port*} registers, which are
 * encoded as an offset from the start of each DC port block.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public enum DerDcMeasurementModelRegister implements ModbusReference {

	/**
	 * Bitmask of the ports with active alarms, where bit 0 is the first port.
	 */
	AlarmedPortsBitmask(0, UInt32, Bitfield),

	/** The number of DC ports. */
	NumberOfPorts(2, UInt16),

	/** Total DC current for all ports, in A. */
	DcCurrent(3, Int16),

	/** Total DC power for all ports, in W. */
	DcPower(4, Int16),

	/** Total DC energy injected for all ports, in Wh. */
	DcEnergyInjected(5, UInt64),

	/** Total DC energy absorbed for all ports, in Wh. */
	DcEnergyAbsorbed(9, UInt64),

	/** DC current scale factor. */
	ScaleFactorDcCurrent(13, Int16, ScaleFactor),

	/** DC voltage scale factor. */
	ScaleFactorDcVoltage(14, Int16, ScaleFactor),

	/** DC power scale factor. */
	ScaleFactorDcPower(15, Int16, ScaleFactor),

	/** DC energy scale factor. */
	ScaleFactorDcEnergy(16, Int16, ScaleFactor),

	/** Temperature scale factor. */
	ScaleFactorTemperature(17, Int16, ScaleFactor),

	// DC ports

	/** The port type, see {@link DerDcPortType}. */
	PortType(0, UInt16, Enumeration),

	/** The port ID. */
	PortId(1, UInt16),

	/** The port name. */
	PortName(2, StringUtf8, 8),

	/** The port DC current, in A. */
	PortDcCurrent(10, Int16),

	/** The port DC voltage, in V. */
	PortDcVoltage(11, UInt16),

	/** The port DC power, in W. */
	PortDcPower(12, Int16),

	/** The port DC energy injected, in Wh. */
	PortDcEnergyInjected(13, UInt64),

	/** The port DC energy absorbed, in Wh. */
	PortDcEnergyAbsorbed(17, UInt64),

	/** The port temperature, in degrees Celsius. */
	PortTemperature(21, Int16),

	/** The port status, see {@link DerDcPortStatus}. */
	PortStatus(22, UInt16, Enumeration),

	/** Bitmask of port alarms, see {@link DerDcPortAlarm}. */
	PortAlarmsBitmask(23, UInt32, Bitfield),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final int wordLength;
	private final @Nullable DataClassification classification;

	private DerDcMeasurementModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, dataType.getWordLength(), null);
	}

	private DerDcMeasurementModelRegister(int address, ModbusDataType dataType,
			@Nullable DataClassification classification) {
		this(address, dataType, dataType.getWordLength(), classification);
	}

	private DerDcMeasurementModelRegister(int address, ModbusDataType dataType, int wordLength) {
		this(address, dataType, wordLength, null);
	}

	private DerDcMeasurementModelRegister(int address, ModbusDataType dataType, int wordLength,
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

}
