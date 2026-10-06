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
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.api.environmental.Incline;
import net.solarnetwork.sunspec.api.environmental.InclinometerModelAccessor;
import net.solarnetwork.sunspec.core.environmental.InclinometerModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link InclinometerModelAccessorImpl} class.
 * 
 * @author matt
 * @version 1.0
 */
public class InclinometerModelAccessorImpl_304_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(InclinometerModelAccessorImpl_304_01Tests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-304-01.txt");
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
		InclinometerModelAccessor accessor = data.findTypedModel(InclinometerModelAccessor.class);

		// THEN
		// @formatter:off
		then(accessor)
			.as("Model found by accessor type")
			.isInstanceOf(InclinometerModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		InclinometerModelAccessor model = getTestDataInstance()
				.findTypedModel(InclinometerModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(InclinometerModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(72, from(InclinometerModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(EnvironmentalModelId.Inclinometer, from(InclinometerModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(0, from(InclinometerModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(6, from(InclinometerModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(12, from(InclinometerModelAccessor::getModelLength))
			.as("Model length")
			.returns(2, from(InclinometerModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void data() {
		// GIVEN
		InclinometerModelAccessor model = getTestDataInstance()
				.findTypedModel(InclinometerModelAccessor.class);

		// WHEN
		List<Incline> inclines = model.getInclines();

		// THEN
		// @formatter:off
		then(inclines)
			.as("2 inclines returned")
			.hasSize(2)
			;
		// @formatter:on
		Incline inc = inclines.get(0);
		// @formatter:off
		then(inc)
			.as("Incline 1 x")
			.returns(245.82f, from(Incline::getInclineX))
			.as("Incline 1 y")
			.returns(10.82f, from(Incline::getInclineY))
			.as("Incline 1 z")
			.returns(735.98f, from(Incline::getInclineZ))
			;
		// @formatter:on
		inc = inclines.get(1);
		// @formatter:off
		then(inc)
			.as("Incline 2 x")
			.returns(12.0f, from(Incline::getInclineX))
			.as("Incline 2 y")
			.returns(11.82f, from(Incline::getInclineY))
			.as("Incline 2 z")
			.returns(3.02f, from(Incline::getInclineZ))
			;
		// @formatter:on
	}

}
