/* ==================================================================
 * StringCombinerAdvancedModelAccessorImpl_402_01Tests.java - 10/09/2019 3:32:24 pm
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

package net.solarnetwork.sunspec.core.combiner.test;

import static net.solarnetwork.sunspec.core.combiner.test.StringCombinerTestUtils.assertAdvancedDcInput;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.combiner.StringCombinerAdvancedModelAccessor;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelId;
import net.solarnetwork.sunspec.api.combiner.StringCombinerAdvancedModelAccessor.AdvancedDcInput;
import net.solarnetwork.sunspec.api.inverter.InverterModelAccessor;
import net.solarnetwork.sunspec.core.combiner.StringCombinerAdvancedModelAccessorImpl;
import net.solarnetwork.sunspec.core.inverter.IntegerInverterModelAccessor;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link StringCombinerAdvancedModelAccessorImpl} class.
 *
 * <p>
 * This device uses the legacy 13 register input layout.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public class StringCombinerAdvancedModelAccessorImpl_402_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(StringCombinerAdvancedModelAccessorImpl_402_01Tests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-402-01.txt");
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
			.returns("Solren", from(CommonModelAccessor::getManufacturer))
			.as("Model name")
			.returns("PVI85", from(CommonModelAccessor::getModelName))
			.as("Options")
			.returns("208VAC", from(CommonModelAccessor::getOptions))
			.as("Version")
			.returns("C20130730", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("130602-14", from(CommonModelAccessor::getSerialNumber))
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
		StringCombinerAdvancedModelAccessor accessor = data
				.findTypedModel(StringCombinerAdvancedModelAccessor.class);

		// THEN
		// @formatter:off
		then(accessor)
			.as("Model found by accessor type")
			.isInstanceOf(StringCombinerAdvancedModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void getTypedModel() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		InverterModelAccessor accessor = data.getTypedModel();

		// THEN
		// @formatter:off
		then(accessor)
			.as("First model as typed accessor")
			.isInstanceOf(IntegerInverterModelAccessor.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		StringCombinerAdvancedModelAccessor model = getTestDataInstance()
				.findTypedModel(StringCombinerAdvancedModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(122, from(StringCombinerAdvancedModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(124, from(StringCombinerAdvancedModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(StringCombinerModelId.AdvancedStringCombiner,
					from(StringCombinerAdvancedModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(20, from(StringCombinerAdvancedModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(13, from(StringCombinerAdvancedModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(124, from(StringCombinerAdvancedModelAccessor::getModelLength))
			.as("Model length")
			.returns(8, from(StringCombinerAdvancedModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void voltage() {
		// GIVEN
		StringCombinerAdvancedModelAccessor model = getTestDataInstance()
				.findTypedModel(StringCombinerAdvancedModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getDCVoltage())
			.as("Voltage")
			.isEqualTo(338.5f)
			;
		// @formatter:on
	}

	@Test
	public void values() {
		// GIVEN
		StringCombinerAdvancedModelAccessor model = getTestDataInstance()
				.findTypedModel(StringCombinerAdvancedModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Current")
			.returns(0.0f, from(StringCombinerAdvancedModelAccessor::getDCCurrent))
			.as("Charge scale factor not implemented")
			.returns(null, from(StringCombinerAdvancedModelAccessor::getDCChargeDelivered))
			.as("Temperature not implemented")
			.returns(null, from(StringCombinerAdvancedModelAccessor::getTemperature))
			.as("Power not implemented")
			.returns(null, from(StringCombinerAdvancedModelAccessor::getDCPower))
			.as("Energy scale factor not implemented")
			.returns(null, from(StringCombinerAdvancedModelAccessor::getDCEnergy))
			.as("Performance ratio not implemented")
			.returns(null, from(StringCombinerAdvancedModelAccessor::getDCPerformanceRatio))
			;
		// @formatter:on
	}

	@Test
	public void events() {
		// GIVEN
		StringCombinerAdvancedModelAccessor model = getTestDataInstance()
				.findTypedModel(StringCombinerAdvancedModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Events")
			.returns(Set.of(), from(StringCombinerAdvancedModelAccessor::getEvents))
			.as("Vendor events")
			.returns(Set.of(), from(StringCombinerAdvancedModelAccessor::getVendorEvents))
			;
		// @formatter:on
	}

	@Test
	public void inputs() {
		// GIVEN
		StringCombinerAdvancedModelAccessor model = getTestDataInstance()
				.findTypedModel(StringCombinerAdvancedModelAccessor.class);

		// WHEN
		List<AdvancedDcInput> inputs = model.getAdvancedDcInputs();

		// THEN
		// @formatter:off
		then(inputs)
			.as("Inputs count")
			.hasSize(8)
			;
		// @formatter:on
		for ( int i = 0; i < 8; i++ ) {
			// charge uses the DCAhr_SF scale factor, which is not implemented
			assertAdvancedDcInput("Input " + (i + 1), inputs.get(i), i + 1, null, null, null, null, 0L,
					null, null, Set.of(), Set.of());
		}
	}

}
