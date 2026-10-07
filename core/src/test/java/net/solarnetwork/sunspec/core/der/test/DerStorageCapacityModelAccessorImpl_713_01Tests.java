/* ==================================================================
 * DerStorageCapacityModelAccessorImpl_713_01Tests.java - 5/10/2026 10:42:15 am
 *
 * Copyright 2026 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.core.der.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerStorageCapacityModelAccessor;
import net.solarnetwork.sunspec.api.der.DerStorageStatus;
import net.solarnetwork.sunspec.core.der.DerStorageCapacityModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;

/**
 * Test cases for the {@link DerStorageCapacityModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class DerStorageCapacityModelAccessorImpl_713_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	private DerStorageCapacityModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerStorageCapacityModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerStorageCapacityModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerStorageCapacityModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(1211, from(DerStorageCapacityModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(1213, from(DerStorageCapacityModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.StorageCapacity, from(DerStorageCapacityModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(7, from(DerStorageCapacityModelAccessor::getFixedBlockLength))
			.as("Model length")
			.returns(7, from(DerStorageCapacityModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void values() {
		// GIVEN
		DerStorageCapacityModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Energy rating not implemented")
			.returns(null, from(DerStorageCapacityModelAccessor::getEnergyRating))
			.as("Energy available not implemented")
			.returns(null, from(DerStorageCapacityModelAccessor::getEnergyAvailable))
			.as("State of charge")
			.returns(100.0f, from(DerStorageCapacityModelAccessor::getStateOfCharge))
			.as("State of health not implemented")
			.returns(null, from(DerStorageCapacityModelAccessor::getStateOfHealth))
			.as("Status not implemented")
			.returns(null, from(DerStorageCapacityModelAccessor::getStorageStatus))
			;
		// @formatter:on
	}

	@Test
	public void syntheticValues() {
		// GIVEN
		// replace the whole block: WHRtg, WHAvail, SoC, SoH, Sta, WH_SF, Pct_SF
		DerStorageCapacityModelAccessor model = ModelDataUtils
				.getModelDataInstanceWithRegisters(getClass(), TEST_DATA, 1213, 1350, 1215, 900, 1000, 1,
						1, 0xFFFF)
				.findTypedModel(DerStorageCapacityModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Energy rating")
			.returns(new BigDecimal("13500"), from(DerStorageCapacityModelAccessor::getEnergyRating))
			.as("Energy available")
			.returns(new BigDecimal("12150"), from(DerStorageCapacityModelAccessor::getEnergyAvailable))
			.as("State of charge")
			.returns(90.0f, from(DerStorageCapacityModelAccessor::getStateOfCharge))
			.as("State of health")
			.returns(100.0f, from(DerStorageCapacityModelAccessor::getStateOfHealth))
			.as("Status")
			.returns(DerStorageStatus.Warning, from(DerStorageCapacityModelAccessor::getStorageStatus))
			;
		// @formatter:on
	}

}
