/* ==================================================================
 * IntegerMeterModelAccessorTests.java - 22/05/2018 1:36:13 PM
 * 
 * Copyright 2018 SolarNetwork.net Dev Team
 * 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 * 02111-1307 USA
 * ==================================================================
 */

package net.solarnetwork.sunspec.core.meter.test;

import static net.solarnetwork.domain.AcPhase.PhaseA;
import static net.solarnetwork.domain.AcPhase.PhaseB;
import static net.solarnetwork.domain.AcPhase.PhaseC;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.util.BitSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;
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
 * @version 1.1
 */
public class IntegerMeterModelAccessor_203_03Tests {

	private static final Logger log = LoggerFactory
			.getLogger(IntegerMeterModelAccessor_203_03Tests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-203-03.txt");
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
		then(data.getManufacturer())
			.as("Manufacturer")
			.startsWith("ACCUENERGY")
			;
		then(data)
			.as("Model name")
			.returns("AcuRev1310", from(CommonModelAccessor::getModelName))
			.as("Options")
			.returns("AcuRev1312", from(CommonModelAccessor::getOptions))
			.as("Version")
			.returns("H:2.01 S:2.16", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("E3T18102491", from(CommonModelAccessor::getSerialNumber))
			.as("Device address")
			.returns(1, from(CommonModelAccessor::getDeviceAddress))
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
			.as("No events")
			.isEqualTo(0)
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
			.returns(160.61f, from(MeterModelAccessor::getCurrent))
			.as("Phase A")
			.returns(54.47f, from(m -> m.accessorForPhase(PhaseA).getCurrent()))
			.as("Phase B")
			.returns(52.64f, from(m -> m.accessorForPhase(PhaseB).getCurrent()))
			.as("Phase C")
			.returns(53.5f, from(m -> m.accessorForPhase(PhaseC).getCurrent()))
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
			.returns(123.7f, from(MeterModelAccessor::getVoltage))
			.as("Phase A")
			.returns(123.2f, from(m -> m.accessorForPhase(PhaseA).getVoltage()))
			.as("Phase B")
			.returns(123.9f, from(m -> m.accessorForPhase(PhaseB).getVoltage()))
			.as("Phase C")
			.returns(124.2f, from(m -> m.accessorForPhase(PhaseC).getVoltage()))
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
			.isEqualTo(214.3f)
			;
		then(imm.getVoltageValue(IntegerMeterModelRegister.VoltagePhaseAPhaseB))
			.as("Phase A")
			.isEqualTo(213.6f)
			;
		then(imm.getVoltageValue(IntegerMeterModelRegister.VoltagePhaseBPhaseC))
			.as("Phase B")
			.isEqualTo(215.0f)
			;
		then(imm.getVoltageValue(IntegerMeterModelRegister.VoltagePhaseCPhaseA))
			.as("Phase C")
			.isEqualTo(214.5f)
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
			.returns(132, from(MeterModelAccessor::getActivePower))
			.as("Phase A")
			.returns(6323, from(m -> m.accessorForPhase(PhaseA).getActivePower()))
			.as("Phase B")
			.returns(-1052, from(m -> m.accessorForPhase(PhaseB).getActivePower()))
			.as("Phase C")
			.returns(-5139, from(m -> m.accessorForPhase(PhaseC).getActivePower()))
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
			.returns(19847, from(MeterModelAccessor::getApparentPower))
			.as("Phase A")
			.returns(6720, from(m -> m.accessorForPhase(PhaseA).getApparentPower()))
			.as("Phase B")
			.returns(6502, from(m -> m.accessorForPhase(PhaseB).getApparentPower()))
			.as("Phase C")
			.returns(6625, from(m -> m.accessorForPhase(PhaseC).getApparentPower()))
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
			.returns(93, from(MeterModelAccessor::getReactivePower))
			.as("Phase A")
			.returns(2314, from(m -> m.accessorForPhase(PhaseA).getReactivePower()))
			.as("Phase B")
			.returns(-6428, from(m -> m.accessorForPhase(PhaseB).getReactivePower()))
			.as("Phase C")
			.returns(4206, from(m -> m.accessorForPhase(PhaseC).getReactivePower()))
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
			.returns(0.007f, from(MeterModelAccessor::getPowerFactor))
			.as("Phase A")
			.returns(0.941f, from(m -> m.accessorForPhase(PhaseA).getPowerFactor()))
			.as("Phase B")
			.returns(-0.161f, from(m -> m.accessorForPhase(PhaseB).getPowerFactor()))
			.as("Phase C")
			.returns(-0.776f, from(m -> m.accessorForPhase(PhaseC).getPowerFactor()))
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
			.returns(6L, from(m -> m.accessorForPhase(PhaseB).getActiveEnergyExported()))
			.as("Phase C")
			.returns(26L, from(m -> m.accessorForPhase(PhaseC).getActiveEnergyExported()))
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
			.returns(0L, from(MeterModelAccessor::getActiveEnergyImported))
			.as("Phase A")
			.returns(33L, from(m -> m.accessorForPhase(PhaseA).getActiveEnergyImported()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getActiveEnergyImported()))
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
			.returns(null, from(MeterModelAccessor::getApparentEnergyExported))
			.as("Phase A")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getApparentEnergyExported()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getApparentEnergyExported()))
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
			.returns(104L, from(MeterModelAccessor::getApparentEnergyImported))
			.as("Phase A")
			.returns(35L, from(m -> m.accessorForPhase(PhaseA).getApparentEnergyImported()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getApparentEnergyImported()))
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
			.returns(0L, from(MeterModelAccessor::getReactiveEnergyImported))
			.as("Phase A")
			.returns(11L, from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyImported()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getReactiveEnergyImported()))
			.as("Phase C")
			.returns(22L, from(m -> m.accessorForPhase(PhaseC).getReactiveEnergyImported()))
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
			.returns(0L, from(MeterModelAccessor::getReactiveEnergyExported))
			.as("Phase A")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyExported()))
			.as("Phase B")
			.returns(33L, from(m -> m.accessorForPhase(PhaseB).getReactiveEnergyExported()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getReactiveEnergyExported()))
			;
		// @formatter:on
	}

}
