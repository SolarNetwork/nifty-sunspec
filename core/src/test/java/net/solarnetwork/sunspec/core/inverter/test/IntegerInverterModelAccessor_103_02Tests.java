/* ==================================================================
 * IntegerInverterModelAccessor_103_02Tests.java - 8/10/2018 7:06:15 AM
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

package net.solarnetwork.sunspec.core.inverter.test;

import static net.solarnetwork.sunspec.api.AcPhase.PhaseA;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseB;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseC;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.within;
import java.io.IOException;
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
import net.solarnetwork.sunspec.modbus.support.ModbusData.ModbusDataUpdateAction;
import net.solarnetwork.sunspec.modbus.support.ModbusData.MutableModbusData;

/**
 * Test cases for the {@link IntegerInverterModelAccessor} class.
 * 
 * @author matt
 * @version 1.0
 */
public class IntegerInverterModelAccessor_103_02Tests {

	private static final Logger log = LoggerFactory.getLogger(IntegerMeterModelAccessorTests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-103-02.txt");
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
			.returns("SolarEdge", from(CommonModelAccessor::getManufacturer))
			.as("Model name")
			.returns("SE33.3K", from(CommonModelAccessor::getModelName))
			.as("Options")
			.returns(null, from(CommonModelAccessor::getOptions))
			.as("Version")
			.returns("0003.2251", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("7E1240CC", from(CommonModelAccessor::getSerialNumber))
			.as("Device address")
			.returns(11, from(CommonModelAccessor::getDeviceAddress))
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
			.returns(InverterModelId.ThreePhaseInverterInteger, from(InverterModelAccessor::getModelId))
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
			.returns(86.89f, from(InverterModelAccessor::getCurrent))
			.as("Phase A")
			.returns(28.95f, from(m -> m.accessorForPhase(PhaseA).getCurrent()))
			.as("Phase B")
			.returns(29.02f, from(m -> m.accessorForPhase(PhaseB).getCurrent()))
			.as("Phase C")
			.returns(28.92f, from(m -> m.accessorForPhase(PhaseC).getCurrent()))
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
			.isEqualTo(498.6f)
			;
		then(imm.getVoltageValue(IntegerInverterModelRegister.VoltagePhaseBPhaseC))
			.as("Phase BC")
			.isEqualTo(495.2f)
			;
		then(imm.getVoltageValue(IntegerInverterModelRegister.VoltagePhaseCPhaseA))
			.as("Phase CA")
			.isEqualTo(495.1f)
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
			.returns(284.9f, from(m -> m.accessorForPhase(PhaseA).getVoltage()))
			.as("Phase B")
			.returns(285.1f, from(m -> m.accessorForPhase(PhaseB).getVoltage()))
			.as("Phase C")
			.returns(281.4f, from(m -> m.accessorForPhase(PhaseC).getVoltage()))
			;
		then((double) model.getVoltage())
			.as("Average")
			.isCloseTo(283.8, within(0.01))
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
			.returns(24767, from(InverterModelAccessor::getActivePower))
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
			.isEqualTo(59.99f)
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
			.returns(24797, from(InverterModelAccessor::getApparentPower))
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
			.returns(-1235, from(InverterModelAccessor::getReactivePower))
			;
		// @formatter:on
	}

	@Test
	public void powerFactor() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		InverterModelAccessor model = data.findTypedModel(InverterModelAccessor.class);

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
			.returns(-0.9987f, from(InverterModelAccessor::getPowerFactor))
			;
		then(data.getMetadataValue(IntegerInverterModelAccessor.INTEGER_PF_PCT))
			.as("Integer PF flag")
			.isEqualTo(true)
			;
		// @formatter:on
	}

	@Test
	public void powerFactorWithDecimalScale() throws IOException {
		// GIVEN
		ModelData data = getTestDataInstance();
		data.performUpdates(new ModbusDataUpdateAction() {

			@Override
			public boolean updateModbusData(MutableModbusData m) {
				// force PF scale to -4
				m.saveDataArray(new int[] { 0xFFFC }, 92);
				return false;
			}
		});
		InverterModelAccessor model = data.findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getPowerFactor())
			.as("Average")
			.isEqualTo(-0.9987f)
			;
		then(data.getMetadataValue(IntegerInverterModelAccessor.INTEGER_PF_PCT))
			.as("Integer PF flag")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void powerFactorWithIntegerScaleChangesToDecimal() throws IOException {
		// GIVEN
		ModelData data = getTestDataInstance();
		data.performUpdates(new ModbusDataUpdateAction() {

			@Override
			public boolean updateModbusData(MutableModbusData m) {
				// force PF value to 100e-2 to seem like decimal scale
				m.saveDataArray(new int[] { 0x0064 }, 91);
				return false;
			}
		});
		InverterModelAccessor model = data.findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getPowerFactor())
			.as("Average")
			.isEqualTo(1.0f)
			;
		then(data.getMetadataValue(IntegerInverterModelAccessor.INTEGER_PF_PCT))
			.as("Integer PF flag")
			.isNull()
			;
		// @formatter:on

		// AND
		data.performUpdates(new ModbusDataUpdateAction() {

			@Override
			public boolean updateModbusData(MutableModbusData m) {
				// force PF value to 1000e-2 to become integer scale
				m.saveDataArray(new int[] { 0x03E8 }, 91);
				return false;
			}
		});
		// @formatter:off
		then(model.getPowerFactor())
			.as("Average")
			.isEqualTo(0.1f)
			;
		then(data.getMetadataValue(IntegerInverterModelAccessor.INTEGER_PF_PCT))
			.as("Integer PF flag")
			.isEqualTo(true)
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
			.returns(17534384L, from(InverterModelAccessor::getActiveEnergyExported))
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
			.returns(31.20f, from(InverterModelAccessor::getDcCurrent))
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
			.isCloseTo(805.8, within(0.01))
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
			.returns(25144, from(InverterModelAccessor::getDcPower))
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
			.isEqualTo(55.31f)
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
