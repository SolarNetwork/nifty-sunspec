/* ==================================================================
 * ReferencePointModelAccessorImpl_306_01Tests.java - 9/07/2023 4:53:29 pm
 *
 * Copyright 2023 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.core.environmental.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.api.environmental.ReferencePointModelAccessor;
import net.solarnetwork.sunspec.core.environmental.ReferencePointModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link ReferencePointModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class ReferencePointModelAccessorImpl_306_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(ReferencePointModelAccessorImpl_306_01Tests.class);

	/** Synthetic data, as there is no capture from a device. */
	private static final String TEST_DATA = "test-data-306-01.txt";

	/** The model block address in the test data. */
	private static final int BLOCK_ADDRESS = 72;

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA);
	}

	private ReferencePointModelAccessor getTestModel(int address, int... words) {
		return ModelDataUtils.getModelDataInstanceWithRegisters(getClass(), TEST_DATA, address, words)
				.findTypedModel(ReferencePointModelAccessor.class);
	}

	@Test
	public void dataDebugString() {
		ModelData data = getTestDataInstance();
		log.debug("Got test data: " + data.dataDebugString());
	}

	@Test
	public void findTypedModel() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		ReferencePointModelAccessor accessor = data.findTypedModel(ReferencePointModelAccessor.class);

		// THEN
		// @formatter:off
		then(accessor)
			.as("Model found by accessor type")
			.isInstanceOf(ReferencePointModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		ReferencePointModelAccessor model = getTestDataInstance()
				.findTypedModel(ReferencePointModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(ReferencePointModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BLOCK_ADDRESS, from(ReferencePointModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(EnvironmentalModelId.ReferencePoint, from(ReferencePointModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(4, from(ReferencePointModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(ReferencePointModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(4, from(ReferencePointModelAccessor::getModelLength))
			.as("Model repeating instance count")
			.returns(0, from(ReferencePointModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void data() {
		// GIVEN
		ReferencePointModelAccessor model = getTestDataInstance()
				.findTypedModel(ReferencePointModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("GHI")
			.returns(950, from(ReferencePointModelAccessor::getGlobalHorizontalIrradiance))
			.as("Current")
			.returns(9, from(ReferencePointModelAccessor::getCurrent))
			.as("Voltage")
			.returns(38, from(ReferencePointModelAccessor::getVoltage))
			.as("Temperature")
			.returns(25, from(ReferencePointModelAccessor::getTemperature))
			;
		// @formatter:on
	}

	@Test
	public void data_maximum() {
		// GIVEN
		ReferencePointModelAccessor model = getTestModel(BLOCK_ADDRESS, 0xFFFE, 0xFFFE, 0xFFFE, 0xFFFE);

		// THEN
		// @formatter:off
		then(model)
			.as("GHI read as unsigned")
			.returns(65534, from(ReferencePointModelAccessor::getGlobalHorizontalIrradiance))
			.as("Current read as unsigned")
			.returns(65534, from(ReferencePointModelAccessor::getCurrent))
			.as("Voltage read as unsigned")
			.returns(65534, from(ReferencePointModelAccessor::getVoltage))
			.as("Temperature read as unsigned")
			.returns(65534, from(ReferencePointModelAccessor::getTemperature))
			;
		// @formatter:on
	}

	@Test
	public void data_notImplemented() {
		// GIVEN
		ReferencePointModelAccessor model = getTestModel(BLOCK_ADDRESS, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF);

		// THEN
		// @formatter:off
		then(model)
			.as("GHI not implemented")
			.returns(null, from(ReferencePointModelAccessor::getGlobalHorizontalIrradiance))
			.as("Current not implemented")
			.returns(null, from(ReferencePointModelAccessor::getCurrent))
			.as("Voltage not implemented")
			.returns(null, from(ReferencePointModelAccessor::getVoltage))
			.as("Temperature not implemented")
			.returns(null, from(ReferencePointModelAccessor::getTemperature))
			;
		// @formatter:on
	}

}
