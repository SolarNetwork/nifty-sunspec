/* ==================================================================
 * IntegerMeterModelAccessorTests.java - 22/05/2018 1:36:13 PM
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

package net.solarnetwork.sunspec.core.meter.test;

import static net.solarnetwork.sunspec.api.AcPhase.PhaseA;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseB;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseC;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.PointMapMode;
import net.solarnetwork.sunspec.api.meter.IntegerMeterModelRegister;
import net.solarnetwork.sunspec.api.meter.MeterModelAccessor;
import net.solarnetwork.sunspec.api.meter.MeterModelId;
import net.solarnetwork.sunspec.core.meter.IntegerMeterModelAccessor;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link IntegerMeterModelAccessor} class.
 * 
 * @author matt
 * @version 1.0
 */
public class IntegerMeterModelAccessor_203_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(IntegerMeterModelAccessor_203_01Tests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-203-01.txt");
	}

	@Test
	public void dataDebugString() {
		ModelData data = getTestDataInstance();
		log.debug("Got test data: " + data.dataDebugString());
	}

	@Test
	public void commonModelProperties() {
		// GIVEN
		CommonModelAccessor data = getTestDataInstance();

		// THEN
		// @formatter:off
		then(data)
			.as("Manufacturer")
			.returns("Veris Industries", from(CommonModelAccessor::getManufacturer))
			.as("Model name")
			.returns("E51C2", from(CommonModelAccessor::getModelName))
			.as("Options")
			.returns("None", from(CommonModelAccessor::getOptions))
			.as("Version")
			.returns("2.115", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("4E4C3699", from(CommonModelAccessor::getSerialNumber))
			.as("Device address")
			.returns(7, from(CommonModelAccessor::getDeviceAddress))
			;
		// @formatter:on
	}

	@Test
	public void findTypedModel() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		MeterModelAccessor meterAccessor = data.findTypedModel(MeterModelAccessor.class);

		// THEN
		// @formatter:off
		then(meterAccessor)
			.as("Model found by accessor type")
			.isInstanceOf(IntegerMeterModelAccessor.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(69, from(MeterModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(71, from(MeterModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(MeterModelId.WyeConnectThreePhaseMeterInteger, from(MeterModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(105, from(MeterModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(MeterModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(105, from(MeterModelAccessor::getModelLength))
			.as("Model length")
			.returns(0, from(MeterModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void events() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// WHEN
		Set<? extends ModelEvent> events = model.getEvents();
		BitSet bitset = new BitSet();
		events.stream().mapToInt(ModelEvent::getIndex).forEach(i -> bitset.set(i));

		// THEN
		// @formatter:off
		then(bitset.cardinality())
			.as("Event count")
			.isEqualTo(1)
			;
		then(bitset.get(3))
			.as("Under voltage event")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void events_mostSignificantBit() {
		// GIVEN
		// Evt with the most significant bit set, as well as the under voltage bit
		MeterModelAccessor model = ModelDataUtils.getModelDataInstanceWithRegisters(getClass(),
				"test-data-203-01.txt", 174, 0x8000, 0x0008).findTypedModel(MeterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getEvents())
			.as("Events with the most significant bit set are not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void current() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(53.48f, from(MeterModelAccessor::getCurrent))
			.as("Phase A")
			.returns(26.81f, from(m -> m.accessorForPhase(PhaseA).getCurrent()))
			.as("Phase B")
			.returns(26.67f, from(m -> m.accessorForPhase(PhaseB).getCurrent()))
			.as("Phase C")
			.returns(0f, from(m -> m.accessorForPhase(PhaseC).getCurrent()))
			;
		// @formatter:on
	}

	@Test
	public void voltageLN() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Average")
			.returns(82.0f, from(MeterModelAccessor::getVoltage))
			.as("Phase A")
			.returns(123.0f, from(m -> m.accessorForPhase(PhaseA).getVoltage()))
			.as("Phase B")
			.returns(122.9f, from(m -> m.accessorForPhase(PhaseB).getVoltage()))
			.as("Phase C")
			.returns(0.2f, from(m -> m.accessorForPhase(PhaseC).getVoltage()))
			;
		// @formatter:on
	}

	@Test
	public void voltageLL() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();
		IntegerMeterModelAccessor imm = (IntegerMeterModelAccessor) model;

		// THEN
		// @formatter:off
		then(imm.getVoltageValue(IntegerMeterModelRegister.VoltageLineLineAverage))
			.as("Average")
			.isEqualTo(163.9f)
			;
		then(imm.getVoltageValue(IntegerMeterModelRegister.VoltagePhaseAPhaseB))
			.as("Phase A")
			.isEqualTo(245.9f)
			;
		then(imm.getVoltageValue(IntegerMeterModelRegister.VoltagePhaseBPhaseC))
			.as("Phase B")
			.isEqualTo(122.8f)
			;
		then(imm.getVoltageValue(IntegerMeterModelRegister.VoltagePhaseCPhaseA))
			.as("Phase C")
			.isEqualTo(123.0f)
			;
		// @formatter:on
	}

	@Test
	public void frequency() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model.getFrequency())
			.as("Frequency")
			.isEqualTo(60.01f)
			;
		// @formatter:on
	}

	@Test
	public void activePower() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("6540"), from(MeterModelAccessor::getActivePower))
			.as("Phase A")
			.returns(new BigDecimal("3280"), from(m -> m.accessorForPhase(PhaseA).getActivePower()))
			.as("Phase B")
			.returns(new BigDecimal("3260"), from(m -> m.accessorForPhase(PhaseB).getActivePower()))
			.as("Phase C")
			.returns(BigDecimal.ZERO, from(m -> m.accessorForPhase(PhaseC).getActivePower()))
			;
		// @formatter:on
	}

	@Test
	public void apparentPower() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("6590"), from(MeterModelAccessor::getApparentPower))
			.as("Phase A")
			.returns(new BigDecimal("3300"), from(m -> m.accessorForPhase(PhaseA).getApparentPower()))
			.as("Phase B")
			.returns(new BigDecimal("3280"), from(m -> m.accessorForPhase(PhaseB).getApparentPower()))
			.as("Phase C")
			.returns(BigDecimal.ZERO, from(m -> m.accessorForPhase(PhaseC).getApparentPower()))
			;
		// @formatter:on
	}

	@Test
	public void reactivePower() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("-790"), from(MeterModelAccessor::getReactivePower))
			.as("Phase A")
			.returns(new BigDecimal("-390"), from(m -> m.accessorForPhase(PhaseA).getReactivePower()))
			.as("Phase B")
			.returns(new BigDecimal("-390"), from(m -> m.accessorForPhase(PhaseB).getReactivePower()))
			.as("Phase C")
			.returns(BigDecimal.ZERO, from(m -> m.accessorForPhase(PhaseC).getReactivePower()))
			;
		// @formatter:on
	}

	@Test
	public void powerFactor() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Average")
			.returns(0.9925f, from(MeterModelAccessor::getPowerFactor))
			.as("Phase A")
			.returns(0.9930f, from(m -> m.accessorForPhase(PhaseA).getPowerFactor()))
			.as("Phase B")
			.returns(0.9930f, from(m -> m.accessorForPhase(PhaseB).getPowerFactor()))
			.as("Phase C")
			.returns(1.0f, from(m -> m.accessorForPhase(PhaseC).getPowerFactor()))
			;
		// @formatter:on
	}

	@Test
	public void activeEnergyExport() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(null, from(MeterModelAccessor::getActiveEnergyExported))
			.as("Phase A")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getActiveEnergyExported()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getActiveEnergyExported()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getActiveEnergyExported()))
			;
		// @formatter:on
	}

	@Test
	public void activeEnergyImport() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("906630"), from(MeterModelAccessor::getActiveEnergyImported))
			.as("Phase A")
			.returns(new BigDecimal("454560"), from(m -> m.accessorForPhase(PhaseA).getActiveEnergyImported()))
			.as("Phase B")
			.returns(new BigDecimal("1250"), from(m -> m.accessorForPhase(PhaseB).getActiveEnergyImported()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getActiveEnergyImported()))
			;
		// @formatter:on
	}

	@Test
	public void apparentEnergyExport() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("220"), from(MeterModelAccessor::getApparentEnergyExported))
			.as("Phase A")
			.returns(new BigDecimal("240"), from(m -> m.accessorForPhase(PhaseA).getApparentEnergyExported()))
			.as("Phase B")
			.returns(new BigDecimal("160"), from(m -> m.accessorForPhase(PhaseB).getApparentEnergyExported()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getApparentEnergyExported()))
			;
		// @formatter:on
	}

	@Test
	public void apparentEnergyImport() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("986930"), from(MeterModelAccessor::getApparentEnergyImported))
			.as("Phase A")
			.returns(new BigDecimal("493500"), from(m -> m.accessorForPhase(PhaseA).getApparentEnergyImported()))
			.as("Phase B")
			.returns(new BigDecimal("491010"), from(m -> m.accessorForPhase(PhaseB).getApparentEnergyImported()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getApparentEnergyImported()))
			;
		// @formatter:on
	}

	@Test
	public void reactiveEnergyImport() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(BigDecimal.valueOf((0x36D2L + 0x28L) * 10L), from(MeterModelAccessor::getReactiveEnergyImported))
			.as("Phase A")
			.returns(BigDecimal.valueOf((0x1BDDL + 0x18L) * 10L),
					from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyImported()))
			.as("Phase B")
			.returns(BigDecimal.valueOf((0x1AF4L + 0x10L) * 10L),
					from(m -> m.accessorForPhase(PhaseB).getReactiveEnergyImported()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getReactiveEnergyImported()))
			;
		// @formatter:on
	}

	@Test
	public void reactiveEnergyExport() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(BigDecimal.valueOf((0x0 + 0x1D63L) * 10L), from(MeterModelAccessor::getReactiveEnergyExported))
			.as("Phase A")
			.returns(BigDecimal.valueOf((0x0 + 0x0E49L) * 10L),
					from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyExported()))
			.as("Phase B")
			.returns(BigDecimal.valueOf((0x0L + 0x0F1AL) * 10L),
					from(m -> m.accessorForPhase(PhaseB).getReactiveEnergyExported()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getReactiveEnergyExported()))
			;
		// @formatter:on
	}

	@Test
	public void reactiveEnergyQuadrants() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total Q1")
			.returns(BigDecimal.valueOf(0x36D2L * 10L),
					from(MeterModelAccessor::getReactiveEnergyImportedQ1))
			.as("Total Q2")
			.returns(BigDecimal.valueOf(0x28L * 10L),
					from(MeterModelAccessor::getReactiveEnergyImportedQ2))
			.as("Total Q3 zero accumulator not available")
			.returns(null, from(MeterModelAccessor::getReactiveEnergyExportedQ3))
			.as("Total Q4")
			.returns(BigDecimal.valueOf(0x1D63L * 10L),
					from(MeterModelAccessor::getReactiveEnergyExportedQ4))
			.as("Phase A Q1")
			.returns(BigDecimal.valueOf(0x1BDDL * 10L),
					from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyImportedQ1()))
			.as("Phase A Q2")
			.returns(BigDecimal.valueOf(0x18L * 10L),
					from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyImportedQ2()))
			.as("Phase A Q3 zero accumulator not available")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyExportedQ3()))
			.as("Phase A Q4")
			.returns(BigDecimal.valueOf(0x0E49L * 10L),
					from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyExportedQ4()))
			;
		// @formatter:on
	}

	@Test
	public void commonModelPointMap() {
		// GIVEN
		CommonModelAccessor data = getTestDataInstance();

		// WHEN
		Map<String, Object> result = data.toPointMap(PointMapMode.Flat);

		// THEN
		// @formatter:off
		then(result)
			.as("Common model points mapped")
			.containsExactly(
					entry("manufacturer", "Veris Industries"),
					entry("model", "E51C2"),
					entry("options", "None"),
					entry("version", "2.115"),
					entry("serialNumber", "4E4C3699"),
					entry("deviceAddress", 7))
			;
		// @formatter:on
	}

	@Test
	public void pointMap() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// WHEN
		Map<String, Object> result = model.toPointMap(PointMapMode.Flat);

		// THEN
		// @formatter:off
		then(result)
			.as("Available points mapped, without scale factors")
			.containsOnlyKeys(
					"current", "current_a", "current_b", "current_c",
					"voltage", "voltage_a", "voltage_b", "voltage_c",
					"lineVoltage", "voltage_ab", "voltage_bc", "voltage_ca",
					"frequency",
					"activePower", "activePower_a", "activePower_b", "activePower_c",
					"apparentPower", "apparentPower_a", "apparentPower_b", "apparentPower_c",
					"reactivePower", "reactivePower_a", "reactivePower_b", "reactivePower_c",
					"powerFactor", "powerFactor_a", "powerFactor_b", "powerFactor_c",
					"activeEnergyImported", "activeEnergyImported_a", "activeEnergyImported_b",
					"apparentEnergyExported", "apparentEnergyExported_a", "apparentEnergyExported_b",
					"apparentEnergyImported", "apparentEnergyImported_a", "apparentEnergyImported_b",
					"reactiveEnergyImportedQ1", "reactiveEnergyImportedQ1_a",
					"reactiveEnergyImportedQ1_b",
					"reactiveEnergyImportedQ2", "reactiveEnergyImportedQ2_a",
					"reactiveEnergyImportedQ2_b",
					"reactiveEnergyExportedQ4", "reactiveEnergyExportedQ4_a",
					"reactiveEnergyExportedQ4_b",
					"events")
			.as("Total current")
			.containsEntry("current", model.getCurrent())
			.as("Phase current")
			.containsEntry("current_a", model.accessorForPhase(PhaseA).getCurrent())
			.as("Line-to-neutral average voltage")
			.containsEntry("voltage", model.getVoltage())
			.as("Phase to neutral voltage")
			.containsEntry("voltage_b", model.accessorForPhase(PhaseB).getVoltage())
			.as("Line-to-line average voltage")
			.containsEntry("lineVoltage", model.getLineVoltage())
			.as("Phase C to A voltage")
			.containsEntry("voltage_ca", model.accessorForPhase(PhaseC).getLineVoltage())
			.as("Total active power")
			.containsEntry("activePower", new BigDecimal("6540"))
			.as("Phase reactive energy quadrant")
			.containsEntry("reactiveEnergyImportedQ1_a",
					model.accessorForPhase(PhaseA).getReactiveEnergyImportedQ1())
			.as("Events")
			.containsEntry("events", model.getEvents())
			;
		// @formatter:on
	}

	@Test
	public void pointMap_reversed() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// WHEN
		Map<String, Object> result = model.reversed().toPointMap(PointMapMode.Flat);

		// THEN
		// @formatter:off
		then(result)
			.as("Current unchanged")
			.containsEntry("current", model.getCurrent())
			.as("Active power negated")
			.containsEntry("activePower", new BigDecimal("-6540"))
			.as("Phase active power negated")
			.containsEntry("activePower_a", new BigDecimal("-3280"))
			.as("Apparent power unchanged")
			.containsEntry("apparentPower", new BigDecimal("6590"))
			.as("Active energy exported from imported")
			.containsEntry("activeEnergyExported", model.getActiveEnergyImported())
			.as("Active energy imported from exported, which is not available")
			.doesNotContainKey("activeEnergyImported")
			.as("Reactive energy Q3 from Q1")
			.containsEntry("reactiveEnergyExportedQ3", model.getReactiveEnergyImportedQ1())
			.as("Reactive energy Q4 from Q2")
			.containsEntry("reactiveEnergyExportedQ4", model.getReactiveEnergyImportedQ2())
			.as("Reactive energy Q2 from Q4")
			.containsEntry("reactiveEnergyImportedQ2", model.getReactiveEnergyExportedQ4())
			.as("Reactive energy Q1 from Q3, which is not available")
			.doesNotContainKey("reactiveEnergyImportedQ1")
			;
		// @formatter:on
	}

}
