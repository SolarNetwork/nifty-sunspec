/* ==================================================================
 * DerAcMeasurementModelAccessorImpl_701_01Tests.java - 5/10/2026 8:27:22 am
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

package net.solarnetwork.sunspec.core.der.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.domain.AcPhase;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.der.DerAcMeasurementModelAccessor;
import net.solarnetwork.sunspec.api.der.DerAcWiringType;
import net.solarnetwork.sunspec.api.der.DerAlarm;
import net.solarnetwork.sunspec.api.der.DerGridConnectionState;
import net.solarnetwork.sunspec.api.der.DerInverterState;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerOperatingState;
import net.solarnetwork.sunspec.api.der.DerOperationalCharacteristic;
import net.solarnetwork.sunspec.api.inverter.InverterModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterOperatingState;
import net.solarnetwork.sunspec.core.der.DerAcMeasurementModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link DerAcMeasurementModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class DerAcMeasurementModelAccessorImpl_701_01Tests {

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-der-01.txt");
	}

	private DerAcMeasurementModelAccessor getTestModel() {
		return getTestDataInstance().findTypedModel(DerAcMeasurementModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerAcMeasurementModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void findTypedModel_inverter() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		InverterModelAccessor model = data.findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("DER AC measurement model found as inverter model")
			.isInstanceOf(DerAcMeasurementModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerAcMeasurementModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(177, from(DerAcMeasurementModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(179, from(DerAcMeasurementModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.AcMeasurement, from(DerAcMeasurementModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(153, from(DerAcMeasurementModelAccessor::getFixedBlockLength))
			.as("Model length")
			.returns(153, from(DerAcMeasurementModelAccessor::getModelLength))
			.as("Model repeating instance count")
			.returns(0, from(DerAcMeasurementModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void deviceInfo() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// THEN
		// @formatter:off
		then(data)
			.as("Manufacturer")
			.returns("OutBack Power", from(ModelData::getManufacturer))
			.as("Model")
			.returns("OGHI8048A", from(ModelData::getModelName))
			.as("Version")
			.returns("1.0.20.3812", from(ModelData::getVersion))
			.as("Serial number")
			.returns("OGHI2232F0100079", from(ModelData::getSerialNumber))
			;
		// @formatter:on
	}

	@Test
	public void states() {
		// GIVEN
		DerAcMeasurementModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("AC wiring type")
			.returns(DerAcWiringType.SplitPhase, from(DerAcMeasurementModelAccessor::getAcWiringType))
			.as("DER operating state")
			.returns(DerOperatingState.On, from(DerAcMeasurementModelAccessor::getDerOperatingState))
			.as("Inverter state")
			.returns(DerInverterState.Running, from(DerAcMeasurementModelAccessor::getInverterState))
			.as("Inverter operating state")
			.returns(InverterOperatingState.Normal,
					from(DerAcMeasurementModelAccessor::getOperatingState))
			.as("Grid connection state")
			.returns(DerGridConnectionState.Disconnected,
					from(DerAcMeasurementModelAccessor::getGridConnectionState))
			.as("Operational characteristics")
			.returns(Set.of(DerOperationalCharacteristic.GridForming),
					from(DerAcMeasurementModelAccessor::getOperationalCharacteristics))
			;
		// @formatter:on
	}

	@Test
	public void alarms() {
		// GIVEN
		DerAcMeasurementModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Alarms")
			.returns(Set.<ModelEvent> of(DerAlarm.GridDisconnect, DerAlarm.UnderFrequency,
					DerAlarm.AcUnderVoltage), from(DerAcMeasurementModelAccessor::getEvents))
			.as("Vendor events")
			.returns(null, from(DerAcMeasurementModelAccessor::getVendorEvents))
			.as("Manufacturer alarm info")
			.returns(null, from(DerAcMeasurementModelAccessor::getManufacturerAlarmInfo))
			;
		// @formatter:on
	}

	@Test
	public void throttling() {
		// GIVEN
		DerAcMeasurementModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Throttle percent")
			.returns(0, from(DerAcMeasurementModelAccessor::getThrottlePercent))
			.as("Throttle sources 0xFFFF9AC8 not implemented, as the MSB is set")
			.returns(Set.of(), from(DerAcMeasurementModelAccessor::getThrottleSources))
			;
		// @formatter:on
	}

	@Test
	public void totals() {
		// GIVEN
		DerAcMeasurementModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Active power")
			.returns(0, from(DerAcMeasurementModelAccessor::getActivePower))
			.as("Apparent power")
			.returns(0, from(DerAcMeasurementModelAccessor::getApparentPower))
			.as("Reactive power")
			.returns(0, from(DerAcMeasurementModelAccessor::getReactivePower))
			.as("Power factor")
			.returns(0.0f, from(DerAcMeasurementModelAccessor::getPowerFactor))
			.as("Current")
			.returns(0.0f, from(DerAcMeasurementModelAccessor::getCurrent))
			.as("Line to neutral voltage")
			.returns(120.0f, from(DerAcMeasurementModelAccessor::getVoltage))
			.as("Line to line voltage")
			.returns(240.0f, from(DerAcMeasurementModelAccessor::getLineVoltage))
			.as("Frequency")
			.returns(60.0f, from(DerAcMeasurementModelAccessor::getFrequency))
			;
		// @formatter:on
	}

	@Test
	public void energy() {
		// GIVEN
		DerAcMeasurementModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Active energy exported is injected")
			.returns(0L, from(DerAcMeasurementModelAccessor::getActiveEnergyExported))
			.as("Active energy imported is absorbed")
			.returns(0L, from(DerAcMeasurementModelAccessor::getActiveEnergyImported))
			.as("Reactive energy exported is injected")
			.returns(0L, from(DerAcMeasurementModelAccessor::getReactiveEnergyExported))
			.as("Reactive energy imported is absorbed")
			.returns(0L, from(DerAcMeasurementModelAccessor::getReactiveEnergyImported))
			;
		// @formatter:on
	}

	@Test
	public void dc() {
		// GIVEN
		DerAcMeasurementModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("DC current")
			.returns(null, from(DerAcMeasurementModelAccessor::getDcCurrent))
			.as("DC voltage")
			.returns(null, from(DerAcMeasurementModelAccessor::getDcVoltage))
			.as("DC power")
			.returns(null, from(DerAcMeasurementModelAccessor::getDcPower))
			;
		// @formatter:on
	}

	@Test
	public void temperatures() {
		// GIVEN
		DerAcMeasurementModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Ambient")
			.returns(null, from(DerAcMeasurementModelAccessor::getAmbientTemperature))
			.as("Cabinet")
			.returns(null, from(DerAcMeasurementModelAccessor::getCabinetTemperature))
			.as("Heat sink")
			.returns(null, from(DerAcMeasurementModelAccessor::getHeatSinkTemperature))
			.as("Transformer")
			.returns(57.0f, from(DerAcMeasurementModelAccessor::getTransformerTemperature))
			.as("Switch")
			.returns(31.0f, from(DerAcMeasurementModelAccessor::getSwitchTemperature))
			.as("Other")
			.returns(null, from(DerAcMeasurementModelAccessor::getOtherTemperature))
			;
		// @formatter:on
	}

	@Test
	public void phaseTotal() {
		// GIVEN
		DerAcMeasurementModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model.accessorForPhase(AcPhase.Total))
			.as("Total phase is model")
			.isSameAs(model)
			;
		// @formatter:on
	}

	@Test
	public void phaseA() {
		// GIVEN
		InverterModelAccessor phase = getTestModel().accessorForPhase(AcPhase.PhaseA);

		// THEN
		// @formatter:off
		then(phase)
			.as("Active power")
			.returns(null, from(InverterModelAccessor::getActivePower))
			.as("Apparent power")
			.returns(null, from(InverterModelAccessor::getApparentPower))
			.as("Reactive power")
			.returns(null, from(InverterModelAccessor::getReactivePower))
			.as("Power factor")
			.returns(-0.001f, from(InverterModelAccessor::getPowerFactor))
			.as("Current")
			.returns(0.0f, from(InverterModelAccessor::getCurrent))
			.as("Line voltage")
			.returns(null, from(InverterModelAccessor::getLineVoltage))
			.as("Voltage")
			.returns(120.04f, from(InverterModelAccessor::getVoltage))
			.as("Active energy exported")
			.returns(null, from(InverterModelAccessor::getActiveEnergyExported))
			.as("Active energy imported")
			.returns(null, from(InverterModelAccessor::getActiveEnergyImported))
			.as("Reactive energy exported")
			.returns(null, from(InverterModelAccessor::getReactiveEnergyExported))
			.as("Reactive energy imported")
			.returns(null, from(InverterModelAccessor::getReactiveEnergyImported))
			.as("Frequency is total")
			.returns(60.0f, from(InverterModelAccessor::getFrequency))
			.as("Operating state is total")
			.returns(InverterOperatingState.Normal, from(InverterModelAccessor::getOperatingState))
			;
		// @formatter:on
	}

	@Test
	public void phaseB() {
		// GIVEN
		InverterModelAccessor phase = getTestModel().accessorForPhase(AcPhase.PhaseB);

		// THEN
		// @formatter:off
		then(phase)
			.as("Active power")
			.returns(null, from(InverterModelAccessor::getActivePower))
			.as("Power factor")
			.returns(-0.001f, from(InverterModelAccessor::getPowerFactor))
			.as("Current")
			.returns(0.0f, from(InverterModelAccessor::getCurrent))
			.as("Line voltage")
			.returns(null, from(InverterModelAccessor::getLineVoltage))
			.as("Voltage")
			.returns(119.96f, from(InverterModelAccessor::getVoltage))
			.as("Active energy exported")
			.returns(null, from(InverterModelAccessor::getActiveEnergyExported))
			;
		// @formatter:on
	}

	@Test
	public void phaseC() {
		// GIVEN
		InverterModelAccessor phase = getTestModel().accessorForPhase(AcPhase.PhaseC);

		// THEN
		// @formatter:off
		then(phase)
			.as("Active power")
			.returns(null, from(InverterModelAccessor::getActivePower))
			.as("Power factor")
			.returns(-0.001f, from(InverterModelAccessor::getPowerFactor))
			.as("Current")
			.returns(null, from(InverterModelAccessor::getCurrent))
			.as("Line voltage")
			.returns(null, from(InverterModelAccessor::getLineVoltage))
			.as("Voltage")
			.returns(null, from(InverterModelAccessor::getVoltage))
			.as("Active energy exported")
			.returns(null, from(InverterModelAccessor::getActiveEnergyExported))
			;
		// @formatter:on
	}

}
