/* ==================================================================
 * BomTemperatureModelAccessorImpl_303_01Tests.java - 5/07/2023 10:43:25 am
 * 
 * Copyright 2023 SolarNetwork.net Dev Team
 * 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 * 02111-1307 USA
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
