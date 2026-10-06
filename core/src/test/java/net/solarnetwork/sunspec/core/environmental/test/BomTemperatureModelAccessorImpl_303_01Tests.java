/* ==================================================================
 * BomTemperatureModelAccessorImpl_303_01Tests.java - 5/07/2023 10:43:25 am
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
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.environmental.BomTemperatureModelAccessor;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.core.environmental.BomTemperatureModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link BomTemperatureModelAccessorImpl} class.
 * 
 * @author matt
 * @version 1.0
 */
public class BomTemperatureModelAccessorImpl_303_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(BomTemperatureModelAccessorImpl_303_01Tests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-307-01.txt");
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
		BomTemperatureModelAccessor accessor = data.findTypedModel(BomTemperatureModelAccessor.class);

		// THEN
		// @formatter:off
		then(accessor)
			.as("Model found by accessor type")
			.isInstanceOf(BomTemperatureModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		BomTemperatureModelAccessor model = getTestDataInstance()
				.findTypedModel(BomTemperatureModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(90, from(BomTemperatureModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(92, from(BomTemperatureModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(EnvironmentalModelId.BackOfModuleTemperature,
					from(BomTemperatureModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(0, from(BomTemperatureModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(1, from(BomTemperatureModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(3, from(BomTemperatureModelAccessor::getModelLength))
			.as("Model length")
			.returns(3, from(BomTemperatureModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void data() {
		// GIVEN
		BomTemperatureModelAccessor model = getTestDataInstance()
				.findTypedModel(BomTemperatureModelAccessor.class);

		// WHEN
		List<Float> temps = model.getBackOfModuleTemperatures();

		// THEN
		// @formatter:off
		then(temps)
			.as("3 temps returned")
			.hasSize(3)
			;
		// @formatter:on
		for ( int i = 0; i < 3; i++ ) {
			// @formatter:off
			then(temps.get(i))
				.as(String.format("Temp %d", i + 1))
				.isEqualTo(23.4f + (0.1f * i))
				;
			// @formatter:on
		}
	}

}
