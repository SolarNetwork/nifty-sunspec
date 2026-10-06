/* ==================================================================
 * IrradianceModelAccessorImpl_302_01Tests.java - 5/07/2023 8:38:52 am
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
import net.solarnetwork.sunspec.api.environmental.IrradianceModelAccessor;
import net.solarnetwork.sunspec.core.environmental.IrradianceModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link IrradianceModelAccessorImpl} class.
 * 
 * @author matt
 * @version 1.0
 */
public class IrradianceModelAccessorImpl_302_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(IrradianceModelAccessorImpl_302_01Tests.class);

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
		IrradianceModelAccessor accessor = data.findTypedModel(IrradianceModelAccessor.class);

		// THEN
		// @formatter:off
		then(accessor)
			.as("Model found by accessor type")
			.isInstanceOf(IrradianceModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		IrradianceModelAccessor model = getTestDataInstance()
				.findTypedModel(IrradianceModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(83, from(IrradianceModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(85, from(IrradianceModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(EnvironmentalModelId.Irradiance, from(IrradianceModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(5, from(IrradianceModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(IrradianceModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(5, from(IrradianceModelAccessor::getModelLength))
			.as("Model length")
			.returns(0, from(IrradianceModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void data() {
		// GIVEN
		IrradianceModelAccessor model = getTestDataInstance()
				.findTypedModel(IrradianceModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("GHI")
			.returns(257, from(IrradianceModelAccessor::getGlobalHorizontalIrradiance))
			.as("POAI")
			.returns(258, from(IrradianceModelAccessor::getPlaneOfArrayIrradiance))
			.as("DFI")
			.returns(259, from(IrradianceModelAccessor::getDiffuseIrradiance))
			.as("DNI")
			.returns(260, from(IrradianceModelAccessor::getDirectNormalIrradiance))
			.as("OTI")
			.returns(261, from(IrradianceModelAccessor::getOtherIrradiance))
			;
		// @formatter:on
	}

}
