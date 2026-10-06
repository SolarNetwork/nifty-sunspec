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
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.api.environmental.ReferencePoint;
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
			.getLogger(InclinometerModelAccessorImpl_304_01Tests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-306-01.txt");
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
			.returns(72, from(ReferencePointModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(EnvironmentalModelId.ReferencePoint, from(ReferencePointModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(0, from(ReferencePointModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(7, from(ReferencePointModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(14, from(ReferencePointModelAccessor::getModelLength))
			.as("Model length")
			.returns(2, from(ReferencePointModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void data() {
		// GIVEN
		ReferencePointModelAccessor model = getTestDataInstance()
				.findTypedModel(ReferencePointModelAccessor.class);

		// WHEN
		List<ReferencePoint> points = model.getReferencePoints();

		// THEN
		// @formatter:off
		then(points)
			.as("2 reference points returned")
			.hasSize(2)
			;
		// @formatter:on
		ReferencePoint p = points.get(0);
		// @formatter:off
		then(p)
			.as("ReferencePoint 1 irradiance")
			.returns(12345, from(ReferencePoint::getIrradiance))
			.as("ReferencePoint 1 current")
			.returns(1.23f, from(ReferencePoint::getCurrent))
			.as("ReferencePoint 1 voltage")
			.returns(2.34f, from(ReferencePoint::getVoltage))
			.as("ReferencePoint 1 temperature")
			.returns(34.5f, from(ReferencePoint::getTemperature))
			;
		// @formatter:on
		p = points.get(1);
		// @formatter:off
		then(p)
			.as("ReferencePoint 2 irradiance")
			.returns(23456, from(ReferencePoint::getIrradiance))
			.as("ReferencePoint 2 current")
			.returns(-2.34f, from(ReferencePoint::getCurrent))
			.as("ReferencePoint 2 voltage")
			.returns(-3.45f, from(ReferencePoint::getVoltage))
			.as("ReferencePoint 2 temperature")
			.returns(-45.6f, from(ReferencePoint::getTemperature))
			;
		// @formatter:on
	}

}
