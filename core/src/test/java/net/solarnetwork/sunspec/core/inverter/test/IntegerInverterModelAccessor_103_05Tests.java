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
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterModelId;
import net.solarnetwork.sunspec.core.GenericModelAccessor;
import net.solarnetwork.sunspec.core.inverter.IntegerInverterModelAccessor;
import net.solarnetwork.sunspec.core.meter.test.IntegerMeterModelAccessorTests;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link IntegerInverterModelAccessor} class.
 * 
 * @author matt
 * @version 1.0
 */
public class IntegerInverterModelAccessor_103_05Tests {

	private static final Logger log = LoggerFactory.getLogger(IntegerMeterModelAccessorTests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-103-05.txt", true);
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
			.returns("SMA", from(CommonModelAccessor::getManufacturer))
			.as("Model name")
			.returns("Solar Inverter", from(CommonModelAccessor::getModelName))
			.as("Options")
			.returns("9338", from(CommonModelAccessor::getOptions))
			.as("Version")
			.returns("3.11.02.R", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("3009060251", from(CommonModelAccessor::getSerialNumber))
			.as("Device address")
			.returns(65535, from(CommonModelAccessor::getDeviceAddress))
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
		ModelAccessor accessor = data.getTypedModel();

		// THEN
		// @formatter:off
		then(accessor)
			.as("First model as typed accessor")
			.isInstanceOf(GenericModelAccessor.class)
			;
		then(accessor.getModelId().getId())
			.as("First model ID")
			.isEqualTo(11)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(40185, from(InverterModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(40187, from(InverterModelAccessor::getBlockAddress))
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

}
