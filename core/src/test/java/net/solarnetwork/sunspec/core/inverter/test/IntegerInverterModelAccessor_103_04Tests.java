/* ==================================================================
 * IntegerInverterModelAccessor_103_04Tests.java - 8/10/2018 7:06:15 AM
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

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.OperatingState;
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
public class IntegerInverterModelAccessor_103_04Tests {

	private static final Logger log = LoggerFactory.getLogger(IntegerMeterModelAccessorTests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-103-04.txt");
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
			.returns("0003.2221", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("7E149EF5", from(CommonModelAccessor::getSerialNumber))
			.as("Device address")
			.returns(12, from(CommonModelAccessor::getDeviceAddress))
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
			.isEqualTo(52.11f)
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
