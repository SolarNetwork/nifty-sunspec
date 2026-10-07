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
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.PointMapMode;
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
			.as("Active power")
			.returns(new BigDecimal("6950.5"), from(MeterModelAccessor::getActivePower))
			.as("Apparent power")
			.returns(new BigDecimal("7020"), from(MeterModelAccessor::getApparentPower))
			.as("Reactive power")
			.returns(new BigDecimal("-980"), from(MeterModelAccessor::getReactivePower))
			.as("Active energy exported")
			.returns(new BigDecimal("1234567"), from(MeterModelAccessor::getActiveEnergyExported))
			.as("Active energy imported")
			.returns(new BigDecimal("7654321"), from(MeterModelAccessor::getActiveEnergyImported))
			.as("Apparent energy exported")
			.returns(new BigDecimal("1300000"), from(MeterModelAccessor::getApparentEnergyExported))
			.as("Apparent energy imported")
			.returns(new BigDecimal("7700000"), from(MeterModelAccessor::getApparentEnergyImported))
			.as("Reactive energy imported from Q1 and Q2")
			.returns(new BigDecimal("1200"), from(MeterModelAccessor::getReactiveEnergyImported))
			.as("Reactive energy exported from Q3 and Q4")
			.returns(new BigDecimal("700"), from(MeterModelAccessor::getReactiveEnergyExported))
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
			.returns(new BigDecimal("2300.25"), from(MeterModelAccessor::getActivePower))
			.as("Apparent power")
			.returns(new BigDecimal("2335"), from(MeterModelAccessor::getApparentPower))
			.as("Reactive power")
			.returns(new BigDecimal("-330.25"), from(MeterModelAccessor::getReactivePower))
			.as("Active energy exported")
			.returns(new BigDecimal("411522.25"), from(MeterModelAccessor::getActiveEnergyExported))
			.as("Active energy imported")
			.returns(new BigDecimal("2551440"), from(MeterModelAccessor::getActiveEnergyImported))
			.as("Apparent energy exported")
			.returns(new BigDecimal("433333"), from(MeterModelAccessor::getApparentEnergyExported))
			.as("Apparent energy imported")
			.returns(new BigDecimal("2566666.5"), from(MeterModelAccessor::getApparentEnergyImported))
			.as("Reactive energy imported")
			.returns(new BigDecimal("399"), from(MeterModelAccessor::getReactiveEnergyImported))
			.as("Reactive energy exported without Q4")
			.returns(new BigDecimal("100"), from(MeterModelAccessor::getReactiveEnergyExported))
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
			.as("Active power")
			.returns(new BigDecimal("2325.5"), from(MeterModelAccessor::getActivePower))
			.as("Apparent power")
			.returns(new BigDecimal("2342.5"), from(MeterModelAccessor::getApparentPower))
			.as("Reactive power")
			.returns(new BigDecimal("-325.5"), from(MeterModelAccessor::getReactivePower))
			.as("Active energy exported")
			.returns(new BigDecimal("411522.5"), from(MeterModelAccessor::getActiveEnergyExported))
			.as("Active energy imported")
			.returns(new BigDecimal("2551440.5"), from(MeterModelAccessor::getActiveEnergyImported))
			.as("Reactive energy imported")
			.returns(new BigDecimal("400.5"), from(MeterModelAccessor::getReactiveEnergyImported))
			.as("Reactive energy exported")
			.returns(new BigDecimal("233"), from(MeterModelAccessor::getReactiveEnergyExported))
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
			.returns(new BigDecimal("2324.75"), from(MeterModelAccessor::getActivePower))
			.as("Reactive power")
			.returns(new BigDecimal("-324.25"), from(MeterModelAccessor::getReactivePower))
			.as("Reactive energy exported")
			.returns(new BigDecimal("234"), from(MeterModelAccessor::getReactiveEnergyExported))
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

	@Test
	public void reactiveEnergyQuadrants() {
		// GIVEN
		MeterModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total Q1")
			.returns(new BigDecimal("1000"), from(MeterModelAccessor::getReactiveEnergyImportedQ1))
			.as("Total Q2")
			.returns(new BigDecimal("200"), from(MeterModelAccessor::getReactiveEnergyImportedQ2))
			.as("Total Q3")
			.returns(new BigDecimal("300"), from(MeterModelAccessor::getReactiveEnergyExportedQ3))
			.as("Total Q4")
			.returns(new BigDecimal("400"), from(MeterModelAccessor::getReactiveEnergyExportedQ4))
			.as("Phase A Q1")
			.returns(new BigDecimal("333"),
					from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyImportedQ1()))
			.as("Phase B Q2")
			.returns(new BigDecimal("67"),
					from(m -> m.accessorForPhase(PhaseB).getReactiveEnergyImportedQ2()))
			.as("Phase C Q3")
			.returns(new BigDecimal("100"),
					from(m -> m.accessorForPhase(PhaseC).getReactiveEnergyExportedQ3()))
			.as("Phase A Q4 not available")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyExportedQ4()))
			;
		// @formatter:on
	}

	@Test
	public void pointMap() {
		// GIVEN
		MeterModelAccessor model = getTestModel();

		// WHEN
		Map<String, Object> result = model.toPointMap(PointMapMode.Flat);

		// THEN
		// @formatter:off
		then(result)
			.as("Unavailable points left out")
			.doesNotContainKeys("powerFactor_c", "reactiveEnergyExportedQ4_a")
			.as("Total current")
			.containsEntry("current", 30.5f)
			.as("Phase current")
			.containsEntry("current_b", 10.2f)
			.as("Line-to-line average voltage")
			.containsEntry("lineVoltage", 399.0f)
			.as("Phase A to B voltage")
			.containsEntry("voltage_ab", 398.6f)
			.as("Phase active power")
			.containsEntry("activePower_a", new BigDecimal("2300.25"))
			.as("Reactive energy quadrant")
			.containsEntry("reactiveEnergyExportedQ3", new BigDecimal("300"))
			.as("Phase reactive energy quadrant")
			.containsEntry("reactiveEnergyImportedQ1_c", new BigDecimal("333.5"))
			.as("Events")
			.containsEntry("events",
					Set.<ModelEvent> of(MeterModelEvent.PowerFailure, MeterModelEvent.LowPowerFactor))
			;
		// @formatter:on
	}

}
