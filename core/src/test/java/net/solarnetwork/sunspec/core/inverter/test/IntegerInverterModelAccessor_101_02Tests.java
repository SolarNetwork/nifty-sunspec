/* ==================================================================
 * IntegerInverterModelAccessor_103_02Tests.java - 8/10/2018 7:06:15 AM
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

package net.solarnetwork.sunspec.core.inverter.test;

import static net.solarnetwork.domain.AcPhase.PhaseA;
import static net.solarnetwork.domain.AcPhase.PhaseB;
import static net.solarnetwork.domain.AcPhase.PhaseC;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.within;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.OperatingState;
import net.solarnetwork.sunspec.api.inverter.IntegerInverterModelRegister;
import net.solarnetwork.sunspec.api.inverter.InverterModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterModelId;
import net.solarnetwork.sunspec.api.inverter.InverterOperatingState;
import net.solarnetwork.sunspec.core.inverter.IntegerInverterModelAccessor;
import net.solarnetwork.sunspec.core.meter.test.IntegerMeterModelAccessorTests;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link IntegerInverterModelAccessor} class.
 * 
 * @author matt
 * @version 1.1
 */
public class IntegerInverterModelAccessor_101_02Tests {

	private static final Logger log = LoggerFactory.getLogger(IntegerMeterModelAccessorTests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-101-02.txt");
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
			.returns("Fronius", from(CommonModelAccessor::getManufacturer))
			.as("Model name")
			.returns("IG+V11.4", from(CommonModelAccessor::getModelName))
			.as("Options")
			.returns("2.1.18", from(CommonModelAccessor::getOptions))
			.as("Version")
			.returns("5.10.0", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("50.213262", from(CommonModelAccessor::getSerialNumber))
			.as("Device address")
			.returns(5, from(CommonModelAccessor::getDeviceAddress))
			;
		// @formatter:on
	}

	@Test
	public void findTypedModel() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		InverterModelAccessor meterAccessor = data.findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(meterAccessor)
			.as("Model found by accessor type")
			.isInstanceOf(IntegerInverterModelAccessor.class)
			;
		// @formatter:on
	}

	@Test
	public void getTypedModel() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		InverterModelAccessor meterAccessor = data.getTypedModel();

		// THEN
		// @formatter:off
		then(meterAccessor)
			.as("First model as typed accessor")
			.isInstanceOf(IntegerInverterModelAccessor.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(69, from(InverterModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(71, from(InverterModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(InverterModelId.SinglePhaseInverterInteger, from(InverterModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(50, from(InverterModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(InverterModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(50, from(InverterModelAccessor::getModelLength))
			.as("Model length")
			.returns(0, from(InverterModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void current() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(3.24f, from(InverterModelAccessor::getCurrent))
			.as("Phase A")
			.returns(3.24f, from(m -> m.accessorForPhase(PhaseA).getCurrent()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getCurrent()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getCurrent()))
			;
		// @formatter:on
	}

	@Test
	public void voltageLL() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);
		IntegerInverterModelAccessor imm = (IntegerInverterModelAccessor) model;

		// THEN
		// @formatter:off
		then(imm.getVoltageValue(IntegerInverterModelRegister.VoltagePhaseAPhaseB))
			.as("Phase AB")
			.isNull()
			;
		then(imm.getVoltageValue(IntegerInverterModelRegister.VoltagePhaseBPhaseC))
			.as("Phase BC")
			.isNull()
			;
		then(imm.getVoltageValue(IntegerInverterModelRegister.VoltagePhaseCPhaseA))
			.as("Phase CA")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void voltageLN() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Phase A")
			.returns(247.0f, from(m -> m.accessorForPhase(PhaseA).getVoltage()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getVoltage()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getVoltage()))
			;
		then((double) model.getVoltage())
			.as("Average")
			.isCloseTo(247.0, within(0.01))
			;
		// @formatter:on
	}

	@Test
	public void activePower() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Phase A")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getActivePower()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getActivePower()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getActivePower()))
			.as("Total")
			.returns(798, from(InverterModelAccessor::getActivePower))
			;
		// @formatter:on
	}

	@Test
	public void frequency() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getFrequency())
			.as("Frequency")
			.isEqualTo(60.0f)
			;
		// @formatter:on
	}

	@Test
	public void apparentPower() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Phase A")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getApparentPower()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getApparentPower()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getApparentPower()))
			.as("Total")
			.returns(800, from(InverterModelAccessor::getApparentPower))
			;
		// @formatter:on
	}

	@Test
	public void reactivePower() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Phase A")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getReactivePower()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getReactivePower()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getReactivePower()))
			.as("Total")
			.returns(56, from(InverterModelAccessor::getReactivePower))
			;
		// @formatter:on
	}

	@Test
	public void powerFactor() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Phase A")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getPowerFactor()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getPowerFactor()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getPowerFactor()))
			.as("Average")
			.returns(0.997f, from(InverterModelAccessor::getPowerFactor))
			;
		// @formatter:on
	}

	@Test
	public void activeEnergyExport() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Phase A")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getActiveEnergyExported()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getActiveEnergyExported()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getActiveEnergyExported()))
			.as("Total")
			.returns(76517500L, from(InverterModelAccessor::getActiveEnergyExported))
			;
		// @formatter:on
	}

	@Test
	public void dcCurrent() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Phase A")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getDcCurrent()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getDcCurrent()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getDcCurrent()))
			.as("Total")
			.returns(3.35f, from(InverterModelAccessor::getDcCurrent))
			;
		// @formatter:on
	}

	@Test
	public void dcVoltage() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Phase A")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getDcVoltage()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getDcVoltage()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getDcVoltage()))
			;
		then((double) model.getDcVoltage())
			.as("Average")
			.isCloseTo(242.0, within(0.01))
			;
		// @formatter:on
	}

	@Test
	public void dcPower() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Phase A")
			.returns(null, from(m -> m.accessorForPhase(PhaseA).getDcPower()))
			.as("Phase B")
			.returns(null, from(m -> m.accessorForPhase(PhaseB).getDcPower()))
			.as("Phase C")
			.returns(null, from(m -> m.accessorForPhase(PhaseC).getDcPower()))
			.as("Total")
			.returns(810, from(InverterModelAccessor::getDcPower))
			;
		// @formatter:on
	}

	@Test
	public void cabinetTemperature() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getCabinetTemperature())
			.as("Cabinet temperature")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void heatSinkTemperature() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getHeatSinkTemperature())
			.as("Heat sink temperature")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void transformerTemperature() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getTransformerTemperature())
			.as("Transformer temperature")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void otherTemperature() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getOtherTemperature())
			.as("Other temperature")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void operatingState() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// WHEN
		OperatingState state = model.getOperatingState();

		// THEN
		// @formatter:off
		then(state)
			.as("Operating state available")
			.isNotNull()
			.as("State value")
			.returns(InverterOperatingState.Mppt.getCode(), from(OperatingState::getCode))
			;
		// @formatter:on
	}

	@Test
	public void events() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// WHEN
		Set<? extends ModelEvent> events = model.getEvents();

		// THEN
		// @formatter:off
		then(events)
			.as("No events")
			.hasSize(0)
			;
		// @formatter:on
	}

}
