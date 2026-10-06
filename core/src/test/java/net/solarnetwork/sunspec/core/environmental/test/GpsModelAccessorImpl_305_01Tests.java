/* ==================================================================
 * GpsModelAccessorImpl_305_01Tests.java - 9/07/2023 7:27:06 am
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.api.environmental.GpsModelAccessor;
import net.solarnetwork.sunspec.core.environmental.GpsModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for {@link GpsModelAccessorImpl}.
 * 
 * @author matt
 * @version 1.0
 */
public class GpsModelAccessorImpl_305_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(IrradianceModelAccessorImpl_302_01Tests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-305-01.txt");
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
		GpsModelAccessor accessor = data.findTypedModel(GpsModelAccessor.class);

		// THEN
		// @formatter:off
		then(accessor)
			.as("Model found by accessor type")
			.isInstanceOf(GpsModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		GpsModelAccessor model = getTestDataInstance().findTypedModel(GpsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(GpsModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(72, from(GpsModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(EnvironmentalModelId.GPS, from(GpsModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(36, from(GpsModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(GpsModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(36, from(GpsModelAccessor::getModelLength))
			.as("Model length")
			.returns(0, from(GpsModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void data() {
		// GIVEN
		GpsModelAccessor model = getTestDataInstance().findTypedModel(GpsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("GPS timestamp")
			.returns(DateTimeFormatter.ISO_INSTANT.parse("2023-07-09T19:28:34.123Z", Instant::from),
					from(GpsModelAccessor::getGpsTimestamp))
			.as("Location")
			.returns("Home sweet home", from(GpsModelAccessor::getLocationName))
			.as("Latitude")
			.returns(new BigDecimal("-37.1133611"), from(GpsModelAccessor::getLatitude))
			.as("Latitude")
			.returns(new BigDecimal("175.8884328"), from(GpsModelAccessor::getLongitude))
			;
		// @formatter:on
	}

}
