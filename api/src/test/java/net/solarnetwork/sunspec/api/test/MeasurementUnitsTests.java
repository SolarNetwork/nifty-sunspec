/* ==================================================================
 * MeasurementUnitsTests.java - 7/10/2026 6:11:16 pm
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

package net.solarnetwork.sunspec.api.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.CommonModelRegister;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.MeasurementUnits;
import net.solarnetwork.sunspec.api.ModelRegister;
import net.solarnetwork.sunspec.api.combiner.StringCombinerAdvancedModelRegister;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelRegister;
import net.solarnetwork.sunspec.api.der.DerAcControlsModelRegister;
import net.solarnetwork.sunspec.api.der.DerAcMeasurementModelRegister;
import net.solarnetwork.sunspec.api.der.DerCapacityModelRegister;
import net.solarnetwork.sunspec.api.der.DerControlModelRegister;
import net.solarnetwork.sunspec.api.der.DerCurveModelRegister;
import net.solarnetwork.sunspec.api.der.DerDcMeasurementModelRegister;
import net.solarnetwork.sunspec.api.der.DerEnterServiceModelRegister;
import net.solarnetwork.sunspec.api.der.DerFrequencyDroopModelRegister;
import net.solarnetwork.sunspec.api.der.DerStorageCapacityModelRegister;
import net.solarnetwork.sunspec.api.der.DerTripModelRegister;
import net.solarnetwork.sunspec.api.der.DerVoltVarModelRegister;
import net.solarnetwork.sunspec.api.der.DerVoltWattModelRegister;
import net.solarnetwork.sunspec.api.der.DerWattVarModelRegister;
import net.solarnetwork.sunspec.api.environmental.BomTemperatureModelRegister;
import net.solarnetwork.sunspec.api.environmental.GpsModelRegister;
import net.solarnetwork.sunspec.api.environmental.InclinometerModelRegister;
import net.solarnetwork.sunspec.api.environmental.IrradianceModelRegister;
import net.solarnetwork.sunspec.api.environmental.MeteorologicalModelRegister;
import net.solarnetwork.sunspec.api.environmental.MiniMeteorologicalModelRegister;
import net.solarnetwork.sunspec.api.environmental.ReferencePointModelRegister;
import net.solarnetwork.sunspec.api.inverter.FloatingPointInverterModelRegister;
import net.solarnetwork.sunspec.api.inverter.IntegerInverterModelRegister;
import net.solarnetwork.sunspec.api.inverter.InverterBasicSettingsRegister;
import net.solarnetwork.sunspec.api.inverter.InverterBasicStorageControlsModelRegister;
import net.solarnetwork.sunspec.api.inverter.InverterExtendedMeasurementsModelRegister;
import net.solarnetwork.sunspec.api.inverter.InverterImmediateControlsModelRegister;
import net.solarnetwork.sunspec.api.inverter.InverterMpptExtensionModelRegister;
import net.solarnetwork.sunspec.api.inverter.InverterNameplateRatingsRegister;
import net.solarnetwork.sunspec.api.inverter.InverterPricingSignalModelRegister;
import net.solarnetwork.sunspec.api.meter.FloatingPointMeterModelRegister;
import net.solarnetwork.sunspec.api.meter.IntegerMeterModelRegister;
import net.solarnetwork.sunspec.api.storage.BatteryBaseModelRegister;
import net.solarnetwork.sunspec.api.storage.LithiumIonBankModelRegister;
import net.solarnetwork.sunspec.api.storage.LithiumIonModuleModelRegister;
import net.solarnetwork.sunspec.api.storage.LithiumIonStringModelRegister;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Test cases for the {@link MeasurementUnits} of the register enumerations.
 *
 * @author matt
 * @version 1.0
 */
public class MeasurementUnitsTests {

	// @formatter:off
	private static final List<Class<? extends Enum<? extends ModbusReference>>> REGISTER_TYPES = List.of(
			BatteryBaseModelRegister.class,
			BomTemperatureModelRegister.class,
			CommonModelRegister.class,
			DerAcControlsModelRegister.class,
			DerAcMeasurementModelRegister.class,
			DerCapacityModelRegister.class,
			DerControlModelRegister.class,
			DerCurveModelRegister.class,
			DerDcMeasurementModelRegister.class,
			DerEnterServiceModelRegister.class,
			DerFrequencyDroopModelRegister.class,
			DerStorageCapacityModelRegister.class,
			DerTripModelRegister.class,
			DerVoltVarModelRegister.class,
			DerVoltWattModelRegister.class,
			DerWattVarModelRegister.class,
			FloatingPointInverterModelRegister.class,
			FloatingPointMeterModelRegister.class,
			GpsModelRegister.class,
			InclinometerModelRegister.class,
			IntegerInverterModelRegister.class,
			IntegerMeterModelRegister.class,
			InverterBasicSettingsRegister.class,
			InverterBasicStorageControlsModelRegister.class,
			InverterExtendedMeasurementsModelRegister.class,
			InverterImmediateControlsModelRegister.class,
			InverterMpptExtensionModelRegister.class,
			InverterNameplateRatingsRegister.class,
			InverterPricingSignalModelRegister.class,
			IrradianceModelRegister.class,
			LithiumIonBankModelRegister.class,
			LithiumIonModuleModelRegister.class,
			LithiumIonStringModelRegister.class,
			MeteorologicalModelRegister.class,
			MiniMeteorologicalModelRegister.class,
			ModelRegister.class,
			ReferencePointModelRegister.class,
			StringCombinerAdvancedModelRegister.class,
			StringCombinerModelRegister.class
			);
	// @formatter:on

	private static final Set<DataClassification> UNITLESS_CLASSIFICATIONS = EnumSet.of(
			DataClassification.Bitfield, DataClassification.Enumeration, DataClassification.ScaleFactor);

	private static final Set<ModbusDataType> UNITLESS_DATA_TYPES = EnumSet.of(ModbusDataType.Boolean,
			ModbusDataType.Bytes, ModbusDataType.StringAscii, ModbusDataType.StringUtf8);

	private static List<ModbusReference> allRegisters() {
		List<ModbusReference> result = new ArrayList<>();
		for ( Class<? extends Enum<? extends ModbusReference>> type : REGISTER_TYPES ) {
			for ( Enum<? extends ModbusReference> e : type.getEnumConstants() ) {
				result.add((ModbusReference) e);
			}
		}
		return result;
	}

	private static Set<String> unitConstants() throws IllegalAccessException {
		Set<String> result = new HashSet<>();
		for ( Field f : MeasurementUnits.class.getFields() ) {
			if ( Modifier.isStatic(f.getModifiers()) && f.getType() == String.class ) {
				result.add((String) f.get(null));
			}
		}
		return result;
	}

	@Test
	public void registers_unitsAreConstants() throws IllegalAccessException {
		// GIVEN
		Set<String> units = unitConstants();

		// WHEN
		Set<String> used = allRegisters().stream().map(ModbusReference::getMeasurementUnit)
				.filter(Objects::nonNull).collect(Collectors.toSet());

		// THEN
		// @formatter:off
		then(used)
			.as("Every unit is a MeasurementUnits constant")
			.isSubsetOf(units)
			.as("Every MeasurementUnits constant is used")
			.containsExactlyInAnyOrderElementsOf(units)
			;
		// @formatter:on
	}

	@Test
	public void registers_unitless() {
		// WHEN
		List<ModbusReference> unitless = allRegisters().stream()
				.filter(r -> UNITLESS_CLASSIFICATIONS.contains(r.getClassification())
						|| UNITLESS_DATA_TYPES.contains(r.getDataType()))
				.toList();

		// THEN
		// @formatter:off
		then(unitless)
			.as("Scale factor, bitfield, enumeration, and string points found")
			.isNotEmpty()
			.allSatisfy(r -> then(r.getMeasurementUnit())
					.as("%s has no unit", r)
					.isNull())
			;
		// @formatter:on
	}

	@Test
	public void meter() {
		// @formatter:off
		then(IntegerMeterModelRegister.ActivePowerPhaseA)
			.as("Active power")
			.returns("W", from(ModbusReference::getMeasurementUnit))
			;
		then(IntegerMeterModelRegister.ReactivePowerTotal)
			.as("Reactive power")
			.returns("VAr", from(ModbusReference::getMeasurementUnit))
			;
		then(IntegerMeterModelRegister.ReactiveEnergyExportedQ3Total)
			.as("Reactive energy")
			.returns("VArh", from(ModbusReference::getMeasurementUnit))
			;
		then(IntegerMeterModelRegister.PowerFactorAverage)
			.as("Power factor, a SunSpec percentage the accessor returns as a fraction, has no unit")
			.returns(null, from(ModbusReference::getMeasurementUnit))
			;
		then(FloatingPointMeterModelRegister.PowerFactorPhaseB)
			.as("Power factor has no unit")
			.returns(null, from(ModbusReference::getMeasurementUnit))
			;
		// @formatter:on
	}

	@Test
	public void percentages() {
		// @formatter:off
		then(BatteryBaseModelRegister.StateOfCharge)
			.as("State of charge")
			.returns("%", from(ModbusReference::getMeasurementUnit))
			;
		then(DerVoltVarModelRegister.PointVoltage)
			.as("Percentage of nominal voltage")
			.returns("%", from(ModbusReference::getMeasurementUnit))
			;
		then(InverterBasicSettingsRegister.ActivePowerRampRate)
			.as("Percentage per second")
			.returns("%/s", from(ModbusReference::getMeasurementUnit))
			;
		then(StringCombinerAdvancedModelRegister.DcPerformanceRatio)
			.as("Performance ratio, a SunSpec percentage the accessor returns as a fraction, has no unit")
			.returns(null, from(ModbusReference::getMeasurementUnit))
			;
		// @formatter:on
	}

	@Test
	public void convertedUnits() {
		// @formatter:off
		then(MeteorologicalModelRegister.BarometricPressure)
			.as("Pressure is converted from hPa")
			.returns("Pa", from(ModbusReference::getMeasurementUnit))
			;
		then(MeteorologicalModelRegister.SurfaceWetness)
			.as("Surface wetness is converted from kΩ")
			.returns("Ω", from(ModbusReference::getMeasurementUnit))
			;
		then(InverterExtendedMeasurementsModelRegister.DeviceTime)
			.as("Device time is converted to an instant")
			.returns(null, from(ModbusReference::getMeasurementUnit))
			;
		// @formatter:on
	}

	@Test
	public void repeatingBlocks() {
		// @formatter:off
		then(Arrays.asList(
				IrradianceModelRegister.GHI,
				DerTripModelRegister.PointFrequency,
				DerTripModelRegister.PointFrequencyTime,
				LithiumIonModuleModelRegister.CellTemperature,
				InverterMpptExtensionModelRegister.ModuleLifetimeEnergy))
			.as("Repeating block point units")
			.extracting(ModbusReference::getMeasurementUnit)
			.containsExactly("W/m²", "Hz", "s", "°C", "Wh")
			;
		// @formatter:on
	}

}
