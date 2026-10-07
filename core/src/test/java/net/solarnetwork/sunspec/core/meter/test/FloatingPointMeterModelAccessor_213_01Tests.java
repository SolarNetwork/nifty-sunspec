/* ==================================================================
 * FloatingPointMeterModelAccessor_213_01Tests.java - 6/10/2026 4:58:30 pm
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

package net.solarnetwork.sunspec.core.meter.test;

import static net.solarnetwork.sunspec.api.AcPhase.PhaseA;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseB;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseC;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.meter.MeterModelAccessor;
import net.solarnetwork.sunspec.api.meter.MeterModelEvent;
import net.solarnetwork.sunspec.api.meter.MeterModelId;
import net.solarnetwork.sunspec.core.meter.FloatingPointMeterModelAccessor;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;

/**
 * Test cases for the {@link FloatingPointMeterModelAccessor} class.
 *
 * @author matt
 * @version 1.0
 */
public class FloatingPointMeterModelAccessor_213_01Tests {

	/** Synthetic data, as there is no capture from a device. */
	private static final String TEST_DATA = "test-data-213-01.txt";

	/** The model block address in the test data. */
	private static final int BLOCK_ADDRESS = 72;

	private MeterModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(MeterModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(FloatingPointMeterModelAccessor.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		MeterModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(MeterModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BLOCK_ADDRESS, from(MeterModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(MeterModelId.WyeConnectThreePhaseMeterFloat, from(MeterModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(124, from(MeterModelAccessor::getFixedBlockLength))
			.as("Model length")
			.returns(124, from(MeterModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void values() {
		// GIVEN
		MeterModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Frequency")
			.returns(50.01f, from(MeterModelAccessor::getFrequency))
			.as("Current")
			.returns(30.5f, from(MeterModelAccessor::getCurrent))
			.as("Voltage")
			.returns(230.4f, from(MeterModelAccessor::getVoltage))
			.as("Line voltage")
			.returns(399.0f, from(MeterModelAccessor::getLineVoltage))
			.as("Power factor")
			.returns(0.99f, from(MeterModelAccessor::getPowerFactor))
			.as("Active power rounded")
			.returns(6951, from(MeterModelAccessor::getActivePower))
			.as("Apparent power")
			.returns(7020, from(MeterModelAccessor::getApparentPower))
			.as("Reactive power")
			.returns(-980, from(MeterModelAccessor::getReactivePower))
			.as("Active energy exported")
			.returns(1234567L, from(MeterModelAccessor::getActiveEnergyExported))
			.as("Active energy imported")
			.returns(7654321L, from(MeterModelAccessor::getActiveEnergyImported))
			.as("Apparent energy exported")
			.returns(1300000L, from(MeterModelAccessor::getApparentEnergyExported))
			.as("Apparent energy imported")
			.returns(7700000L, from(MeterModelAccessor::getApparentEnergyImported))
			.as("Reactive energy imported from Q1 and Q2")
			.returns(1200L, from(MeterModelAccessor::getReactiveEnergyImported))
			.as("Reactive energy exported from Q3 and Q4")
			.returns(700L, from(MeterModelAccessor::getReactiveEnergyExported))
			.as("Events")
			.returns(Set.<ModelEvent> of(MeterModelEvent.PowerFailure, MeterModelEvent.LowPowerFactor),
					from(MeterModelAccessor::getEvents))
			;
		// @formatter:on
	}

	@Test
	public void phaseA() {
		// GIVEN
		MeterModelAccessor model = getTestModel().accessorForPhase(PhaseA);

		// THEN
		// @formatter:off
		then(model)
			.as("Frequency")
			.returns(50.01f, from(MeterModelAccessor::getFrequency))
			.as("Current")
			.returns(10.1f, from(MeterModelAccessor::getCurrent))
			.as("Voltage")
			.returns(230.1f, from(MeterModelAccessor::getVoltage))
			.as("Line voltage A-B")
			.returns(398.6f, from(MeterModelAccessor::getLineVoltage))
			.as("Power factor")
			.returns(0.985f, from(MeterModelAccessor::getPowerFactor))
			.as("Active power")
			.returns(2300, from(MeterModelAccessor::getActivePower))
			.as("Apparent power")
			.returns(2335, from(MeterModelAccessor::getApparentPower))
			.as("Reactive power")
			.returns(-330, from(MeterModelAccessor::getReactivePower))
			.as("Active energy exported")
			.returns(411522L, from(MeterModelAccessor::getActiveEnergyExported))
			.as("Active energy imported")
			.returns(2551440L, from(MeterModelAccessor::getActiveEnergyImported))
			.as("Apparent energy exported")
			.returns(433333L, from(MeterModelAccessor::getApparentEnergyExported))
			.as("Apparent energy imported")
			.returns(2566667L, from(MeterModelAccessor::getApparentEnergyImported))
			.as("Reactive energy imported")
			.returns(399L, from(MeterModelAccessor::getReactiveEnergyImported))
			.as("Reactive energy exported without Q4")
			.returns(100L, from(MeterModelAccessor::getReactiveEnergyExported))
			;
		// @formatter:on
	}

	@Test
	public void phaseB() {
		// GIVEN
		MeterModelAccessor model = getTestModel().accessorForPhase(PhaseB);

		// THEN
		// @formatter:off
		then(model)
			.as("Current")
			.returns(10.2f, from(MeterModelAccessor::getCurrent))
			.as("Voltage")
			.returns(230.5f, from(MeterModelAccessor::getVoltage))
			.as("Line voltage B-C")
			.returns(399.2f, from(MeterModelAccessor::getLineVoltage))
			.as("Power factor")
			.returns(0.993f, from(MeterModelAccessor::getPowerFactor))
			.as("Active power rounded")
			.returns(2326, from(MeterModelAccessor::getActivePower))
			.as("Apparent power rounded")
			.returns(2343, from(MeterModelAccessor::getApparentPower))
			.as("Reactive power rounded")
			.returns(-326, from(MeterModelAccessor::getReactivePower))
			.as("Active energy exported")
			.returns(411523L, from(MeterModelAccessor::getActiveEnergyExported))
			.as("Active energy imported")
			.returns(2551441L, from(MeterModelAccessor::getActiveEnergyImported))
			.as("Reactive energy imported")
			.returns(401L, from(MeterModelAccessor::getReactiveEnergyImported))
			.as("Reactive energy exported")
			.returns(233L, from(MeterModelAccessor::getReactiveEnergyExported))
			;
		// @formatter:on
	}

	@Test
	public void phaseC() {
		// GIVEN
		MeterModelAccessor model = getTestModel().accessorForPhase(PhaseC);

		// THEN
		// @formatter:off
		then(model)
			.as("Current")
			.returns(10.2f, from(MeterModelAccessor::getCurrent))
			.as("Voltage")
			.returns(230.6f, from(MeterModelAccessor::getVoltage))
			.as("Line voltage C-A")
			.returns(399.3f, from(MeterModelAccessor::getLineVoltage))
			.as("Power factor not implemented")
			.returns(null, from(MeterModelAccessor::getPowerFactor))
			.as("Active power")
			.returns(2325, from(MeterModelAccessor::getActivePower))
			.as("Reactive power")
			.returns(-324, from(MeterModelAccessor::getReactivePower))
			.as("Reactive energy exported")
			.returns(234L, from(MeterModelAccessor::getReactiveEnergyExported))
			;
		// @formatter:on
	}

	@Test
	public void events_mostSignificantBit() {
		// GIVEN
		// Evt with the most significant bit set, as well as the power failure and low PF bits
		MeterModelAccessor model = ModelDataUtils.getModelDataInstanceWithRegisters(getClass(),
				TEST_DATA, BLOCK_ADDRESS + 122, 0x8000, 0x0014).findTypedModel(MeterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getEvents())
			.as("Events with the most significant bit set are not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void infiniteValue() {
		// GIVEN
		// W is +Infinity
		MeterModelAccessor model = ModelDataUtils.getModelDataInstanceWithRegisters(getClass(),
				TEST_DATA, BLOCK_ADDRESS + 26, 0x7F80, 0x0000).findTypedModel(MeterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getActivePower())
			.as("Infinite value not available")
			.isNull()
			;
		// @formatter:on
	}

}
