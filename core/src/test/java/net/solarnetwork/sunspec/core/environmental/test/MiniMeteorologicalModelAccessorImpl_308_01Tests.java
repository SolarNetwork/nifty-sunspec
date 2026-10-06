/* ==================================================================
 * MiniMeteorologicalModelAccessorImpl_308_01Tests.java - 10/07/2023 7:26:23 am
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
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.api.environmental.MiniMeteorologicalModelAccessor;
import net.solarnetwork.sunspec.core.environmental.MiniMeteorologicalModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link MiniMeteorologicalModelAccessorImpl} class.
 * 
 * @author matt
 * @version 1.0
 */
public class MiniMeteorologicalModelAccessorImpl_308_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(IrradianceModelAccessorImpl_302_01Tests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-308-01.txt");
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
		MiniMeteorologicalModelAccessor accessor = data
				.findTypedModel(MiniMeteorologicalModelAccessor.class);

		// THEN
		// @formatter:off
		then(accessor)
			.as("Model found by accessor type")
			.isInstanceOf(MiniMeteorologicalModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		MiniMeteorologicalModelAccessor model = getTestDataInstance()
				.findTypedModel(MiniMeteorologicalModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(MiniMeteorologicalModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(72, from(MiniMeteorologicalModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(EnvironmentalModelId.MiniMeteorolgical,
					from(MiniMeteorologicalModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(4, from(MiniMeteorologicalModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(MiniMeteorologicalModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(4, from(MiniMeteorologicalModelAccessor::getModelLength))
			.as("Model length")
			.returns(0, from(MiniMeteorologicalModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void data() {
		// GIVEN
		MiniMeteorologicalModelAccessor model = getTestDataInstance()
				.findTypedModel(MiniMeteorologicalModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("GHI")
			.returns(12345, from(MiniMeteorologicalModelAccessor::getGlobalHorizontalIrradiance))
			.as("BOM temp")
			.returns(123.4f, from(MiniMeteorologicalModelAccessor::getBackOfModuleTemperature))
			.as("Ambient temp")
			.returns(-2.3f, from(MiniMeteorologicalModelAccessor::getAmbientTemperature))
			.as("Wind speed")
			.returns(23, from(MiniMeteorologicalModelAccessor::getWindSpeed))
			;
		// @formatter:on
	}

}
