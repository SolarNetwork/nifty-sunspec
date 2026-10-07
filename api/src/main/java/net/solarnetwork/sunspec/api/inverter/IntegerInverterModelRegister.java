/* ==================================================================
 * IntegerInverterModelRegister.java - 5/10/2018 4:16:34 PM
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

import static net.solarnetwork.sunspec.api.DataClassification.Accumulator;
import static net.solarnetwork.sunspec.api.DataClassification.Bitfield;
import static net.solarnetwork.sunspec.api.DataClassification.Enumeration;
import static net.solarnetwork.sunspec.api.DataClassification.ScaleFactor;
import static net.solarnetwork.sunspec.api.MeasurementUnits.AMPERE;
import static net.solarnetwork.sunspec.api.MeasurementUnits.DEGREE_CELSIUS;
import static net.solarnetwork.sunspec.api.MeasurementUnits.HERTZ;
import static net.solarnetwork.sunspec.api.MeasurementUnits.VOLT;
import static net.solarnetwork.sunspec.api.MeasurementUnits.VOLT_AMPERE;
import static net.solarnetwork.sunspec.api.MeasurementUnits.VOLT_AMPERE_REACTIVE;
import static net.solarnetwork.sunspec.api.MeasurementUnits.WATT;
import static net.solarnetwork.sunspec.api.MeasurementUnits.WATT_HOUR;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt32;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for SunSpec compliant integer
 * inverter models.
 *
 * <p>
 * The integer inverter models includes the following model IDs:
 * </p>
 *
 * <ul>
 * <li>101</li>
 * <li>102</li>
 * <li>103</li>
 * </ul>
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public enum IntegerInverterModelRegister implements ModbusReference {

	// Current

	/** Current total, in A. */
	CurrentTotal(0, UInt16),

	/** Current on phase A, in A. */
	CurrentPhaseA(1, UInt16),

	/** Current on phase B, in A. */
	CurrentPhaseB(2, UInt16),

	/** Current on phase C, in A. */
	CurrentPhaseC(3, UInt16),

	/** Current scale factor, as *10^X. */
	ScaleFactorCurrent(4, Int16, ScaleFactor),

	// Voltage

	/** Phase A-to-Phase B voltage, reported in V. */
	VoltagePhaseAPhaseB(5, UInt16),

	/** Phase B-to-Phase C voltage, reported in V. */
	VoltagePhaseBPhaseC(6, UInt16),

	/** Phase C-to-Phase A voltage, reported in V. */
	VoltagePhaseCPhaseA(7, UInt16),

	/** Phase A-to-neutral voltage, reported in V. */
	VoltagePhaseANeutral(8, UInt16),

	/** Phase B-to-neutral voltage, reported in V. */
	VoltagePhaseBNeutral(9, UInt16),

	/** Phase C-to-neutral voltage, reported in V. */
	VoltagePhaseCNeutral(10, UInt16),

	/** Voltage scale factor, as *10^X. */
	ScaleFactorVoltage(11, Int16, ScaleFactor),

	// Active (Real) Power (W)

	/** Active power total, reported in W. */
	ActivePowerTotal(12, Int16),

	/** Active power scale factor, as *10^X. */
	ScaleFactorActivePower(13, Int16, ScaleFactor),

	// Frequency

	/** AC frequency, reported in Hz. */
	Frequency(14, UInt16),

	/** Frequency scale factor, as *10^X. */
	ScaleFactorFrequency(15, Int16, ScaleFactor),

	// Apparent Power (VA)

	/** Apparent power total, reported in VA. */
	ApparentPowerTotal(16, Int16),

	/** Apparent power scale factor, as *10^X. */
	ScaleFactorApparentPower(17, Int16, ScaleFactor),

	// Reactive Power (VAR)

	/** Reactive power total, reported in VAR. */
	ReactivePowerTotal(18, Int16),

	/** Reactive power scale factor, as *10^X. */
	ScaleFactorReactivePower(19, Int16, ScaleFactor),

	// Power factor

	/** AC power factor, reported a a decimal percentage -1..1. */
	PowerFactorAverage(20, Int16),

	/** AC power factor scale factor, as *10^X. */
	ScaleFactorPowerFactor(21, Int16, ScaleFactor),

	// Active energy

	/** Total active (real) energy exported (received), in Wh. */
	ActiveEnergyExportedTotal(22, UInt32, Accumulator),

	/** Active (real) energy scale factor, as *10^X. */
	ScaleFactorActiveEnergy(24, Int16, ScaleFactor),

	// DC current

	/** DC current total, in A. */
	DcCurrentTotal(25, UInt16),

	/** DC current scale factor, as *10^X. */
	ScaleFactorDcCurrent(26, Int16, ScaleFactor),

	// DC voltage

	/** DC voltage total, in V. */
	DcVoltageTotal(27, UInt16),

	/** DC voltage scale factor, as *10^X. */
	ScaleFactorDcVoltage(28, Int16, ScaleFactor),

	// Active (Real) Power (W)

	/** DC power total, reported in W. */
	DcPowerTotal(29, Int16),

	/** DC power scale factor, as *10^X. */
	ScaleFactorDcPower(30, Int16, ScaleFactor),

	// Temperatures (degrees C)

	/** Cabinet temperature, reported in degrees C. */
	TemperatureCabinet(31, Int16),

	/** Heat sink temperature, reported in degrees C. */
	TemperatureHeatSink(32, Int16),

	/** Transformer temperature, reported in degrees C. */
	TemperatureTransformer(33, Int16),

	/** Other temperature, reported in degrees C. */
	TemperatureOther(34, Int16),

	/** Temperature scale factor, as *10^X. */
	ScaleFactorTemperature(35, Int16, ScaleFactor),

	/** Operating state, see {@link InverterOperatingState}. */
	OperatingState(36, UInt16, Enumeration),

	/** Vendor specific operating state. */
	OperatingStateVendor(37, UInt16, Enumeration),

	/** Events bitmask, see {@link InverterModelEvent}. */
	EventsBitmask(38, UInt32, Bitfield),

	/** Events bitmask 2, reserved for future use. */
	Events2Bitmask(40, UInt32, Bitfield),

	/** Vendor events bitmask. */
	EventsVendorBitmask(42, UInt32, Bitfield),

	/** Vendor events bitmask 2. */
	Events2VendorBitmask(44, UInt32, Bitfield),

	/** Vendor events bitmask 3. */
	Events3VendorBitmask(46, UInt32, Bitfield),

	/** Vendor events bitmask 4. */
	Events4VendorBitmask(48, UInt32, Bitfield);

	private final int address;
	private final ModbusDataType dataType;
	private final int wordLength;
	private final @Nullable DataClassification classification;

	private IntegerInverterModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, dataType.getWordLength());
	}

	private IntegerInverterModelRegister(int address, ModbusDataType dataType,
			@Nullable DataClassification classification) {
		this(address, dataType, dataType.getWordLength(), classification);
	}

	private IntegerInverterModelRegister(int address, ModbusDataType dataType, int wordLength) {
		this(address, dataType, wordLength, null);
	}

	private IntegerInverterModelRegister(int address, ModbusDataType dataType, int wordLength,
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
	 * This is the enumeration constant name, without the {@code Total} or
	 * {@code Average} suffix of the points for all phases, and without the
	 * {@code Bitmask} suffix of bitfield points.
	 * </p>
	 */
	@Override
	public String getName() {
		return switch (this) {
			case CurrentTotal -> "Current";
			case ActivePowerTotal -> "ActivePower";
			case ApparentPowerTotal -> "ApparentPower";
			case ReactivePowerTotal -> "ReactivePower";
			case PowerFactorAverage -> "PowerFactor";
			case ActiveEnergyExportedTotal -> "ActiveEnergyExported";
			case DcCurrentTotal -> "DcCurrent";
			case DcVoltageTotal -> "DcVoltage";
			case DcPowerTotal -> "DcPower";
			case EventsBitmask -> "Events";
			case Events2Bitmask -> "Events2";
			case EventsVendorBitmask -> "VendorEvents";
			case Events2VendorBitmask -> "VendorEvents2";
			case Events3VendorBitmask -> "VendorEvents3";
			case Events4VendorBitmask -> "VendorEvents4";
			default -> name();
		};
	}

	@Override
	public @Nullable String getMeasurementUnit() {
		return switch (this) {
			case CurrentTotal, CurrentPhaseA, CurrentPhaseB, CurrentPhaseC, DcCurrentTotal -> AMPERE;
			case VoltagePhaseAPhaseB, VoltagePhaseBPhaseC, VoltagePhaseCPhaseA -> VOLT;
			case VoltagePhaseANeutral, VoltagePhaseBNeutral, VoltagePhaseCNeutral -> VOLT;
			case DcVoltageTotal -> VOLT;
			case ActivePowerTotal, DcPowerTotal -> WATT;
			case Frequency -> HERTZ;
			case ApparentPowerTotal -> VOLT_AMPERE;
			case ReactivePowerTotal -> VOLT_AMPERE_REACTIVE;
			case ActiveEnergyExportedTotal -> WATT_HOUR;
			case TemperatureCabinet, TemperatureHeatSink, TemperatureTransformer -> DEGREE_CELSIUS;
			case TemperatureOther -> DEGREE_CELSIUS;
			default -> null;
		};
	}

}
