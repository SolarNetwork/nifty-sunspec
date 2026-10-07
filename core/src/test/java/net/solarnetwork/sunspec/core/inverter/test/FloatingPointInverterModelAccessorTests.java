/* ==================================================================
 * FloatingPointInverterModelAccessorTests.java - 8/10/2019 3:36:49 pm
 * 
 * Copyright 2019 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.core.inverter.test;

import static net.solarnetwork.sunspec.api.AcPhase.PhaseA;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseB;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseC;
import static net.solarnetwork.sunspec.test.DataUtils.bitSetForBigInteger;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.within;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.OperatingState;
import net.solarnetwork.sunspec.api.inverter.FloatingPointInverterModelRegister;
import net.solarnetwork.sunspec.api.inverter.InverterModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterOperatingState;
import net.solarnetwork.sunspec.core.ModelDataFactory;
import net.solarnetwork.sunspec.core.inverter.FloatingPointInverterModelAccessor;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;
import net.solarnetwork.sunspec.modbus.support.StaticDataMapReadonlyModbusConnection;

/**
 * Test cases for the {@link FloatingPointInverterModelAccessor} class.
 *
 * @author matt
 * @version 1.0
 */
public class FloatingPointInverterModelAccessorTests {

	private static final Logger log = LoggerFactory
			.getLogger(FloatingPointInverterModelAccessorTests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-113-01.txt");
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
			.returns("Symo 3.0-3-S", from(CommonModelAccessor::getModelName))
			.as("Options")
			.returns("3.4.2-1", from(CommonModelAccessor::getOptions))
			.as("Version")
			.returns("0.3.11.10", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("29251001150340235", from(CommonModelAccessor::getSerialNumber))
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
		InverterModelAccessor meterAccessor = data.findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(meterAccessor)
			.as("Model found by accessor type")
			.isInstanceOf(FloatingPointInverterModelAccessor.class)
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
			.isInstanceOf(FloatingPointInverterModelAccessor.class)
			;
		// @formatter:on
	}

	@Test
	public void current() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getCurrent())
			.as("Total")
			.isEqualTo(0.7f)
			;
		then((double) model.accessorForPhase(PhaseA).getCurrent())
			.as("Phase A")
			.isCloseTo(0.2, within(0.1))
			;
		then(model.accessorForPhase(PhaseB).getCurrent())
			.as("Phase B")
			.isEqualTo(0.17f)
			;
		then((double) model.accessorForPhase(PhaseC).getCurrent())
			.as("Phase C")
			.isCloseTo(0.33, within(0.01))
			;
		// @formatter:on
	}

	@Test
	public void voltageLL() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);
		FloatingPointInverterModelAccessor imm = (FloatingPointInverterModelAccessor) model;

		// THEN
		// @formatter:off
		then(imm.getValue(FloatingPointInverterModelRegister.VoltagePhaseAPhaseB))
			.as("Phase AB")
			.isEqualTo(431.0f)
			;
		then(imm.getValue(FloatingPointInverterModelRegister.VoltagePhaseBPhaseC))
			.as("Phase BC")
			.isEqualTo(427.0f)
			;
		then(imm.getValue(FloatingPointInverterModelRegister.VoltagePhaseCPhaseA))
			.as("Phase CA")
			.isEqualTo(426.2f)
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
			.returns(247.40001f, from(m -> m.accessorForPhase(PhaseA).getVoltage()))
			.as("Phase B")
			.returns(250.90001f, from(m -> m.accessorForPhase(PhaseB).getVoltage()))
			.as("Phase C")
			.returns(246.1f, from(m -> m.accessorForPhase(PhaseC).getVoltage()))
			;
		then((double) model.getVoltage())
			.as("Average")
			.isCloseTo(248.13, within(0.01))
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
			.returns(new BigDecimal("70"), from(InverterModelAccessor::getActivePower))
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
			.isEqualTo(50.05f)
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
			.returns(new BigDecimal("70"), from(InverterModelAccessor::getApparentPower))
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
			.returns(BigDecimal.ZERO, from(InverterModelAccessor::getReactivePower))
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
			.returns(1.0f, from(InverterModelAccessor::getPowerFactor))
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
			.returns(new BigDecimal("11937020"), from(InverterModelAccessor::getActiveEnergyExported))
			;
		// @formatter:on
	}

	@Test
	public void activeEnergyExport_zero() throws IOException {
		// GIVEN
		final int[] data = ModelDataUtils.parseTestData(getClass(), "test-data-113-01.txt");
		final int blockAddress = getTestDataInstance().findTypedModel(InverterModelAccessor.class)
				.getBlockAddress();

		// change the energy to 0, which is a valid float32 value (it is not an accumulator type)
		final int addr = blockAddress
				+ FloatingPointInverterModelRegister.ActiveEnergyExportedTotal.getAddress();
		data[addr] = 0;
		data[addr + 1] = 0;

		// WHEN
		InverterModelAccessor model = ModelDataFactory.getInstance()
				.getModelData(new StaticDataMapReadonlyModbusConnection(data))
				.findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getActiveEnergyExported())
			.as("Total of 0 is a value")
			.isEqualTo(BigDecimal.ZERO)
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
			;
		then((double) model.getDcCurrent())
			.as("Total")
			.isCloseTo(0.15, within(0.01))
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
			.isCloseTo(406.9, within(0.01))
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
			.returns(new BigDecimal("61.034996"), from(InverterModelAccessor::getDcPower))
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

	@Test
	public void vendorOperatingState() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// WHEN
		Integer state = model.getVendorOperatingState();

		// THEN
		// @formatter:off
		then(state)
			.as("Vendor operating state")
			.isEqualTo(4)
			;
		// @formatter:on
	}

	@Test
	public void vendorEvents() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// WHEN
		BitSet events = model.getVendorEvents();

		// THEN
		BigInteger expected = new BigInteger("00070008000500060003000400010002", 16);
		// @formatter:off
		then(events)
			.as("No vendor events")
			.isEqualTo(bitSetForBigInteger(expected))
			;
		// @formatter:on
	}

	@Test
	public void vendorEvents_mostSignificantBit() {
		// GIVEN
		// the second vendor event field, with the most significant bit set
		InverterModelAccessor model = ModelDataUtils.getModelDataInstanceWithRegisters(getClass(),
				"test-data-113-01.txt", 125, 0x8003, 0x0004).findTypedModel(InverterModelAccessor.class);

		// THEN
		BigInteger expected = new BigInteger("00070008000500060000000000010002", 16);
		// @formatter:off
		then(model.getVendorEvents())
			.as("Second vendor event field not implemented")
			.isEqualTo(bitSetForBigInteger(expected))
			;
		// @formatter:on
	}

}
