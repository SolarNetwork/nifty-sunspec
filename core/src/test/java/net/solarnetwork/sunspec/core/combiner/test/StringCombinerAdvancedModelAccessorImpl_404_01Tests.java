/* ==================================================================
 * StringCombinerAdvancedModelAccessorImpl_404_01Tests.java - 5/10/2026 7:44:39 am
 *
 * Copyright 2026 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.core.combiner.test;

import static net.solarnetwork.sunspec.core.combiner.test.StringCombinerTestUtils.assertAdvancedDcInput;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.GenericModelEvent;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.combiner.StringCombinerAdvancedModelAccessor;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelEvent;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelId;
import net.solarnetwork.sunspec.api.combiner.StringCombinerAdvancedModelAccessor.AdvancedDcInput;
import net.solarnetwork.sunspec.core.combiner.StringCombinerAdvancedModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link StringCombinerAdvancedModelAccessorImpl} class,
 * using synthetic model 404 data.
 *
 * @author matt
 * @version 1.0
 */
public class StringCombinerAdvancedModelAccessorImpl_404_01Tests {

	private StringCombinerAdvancedModelAccessor getTestModel() {
		ModelData data = ModelDataUtils.getModelDataInstance(getClass(), "test-data-404-01.txt");
		return data.findTypedModel(StringCombinerAdvancedModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(StringCombinerAdvancedModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		StringCombinerAdvancedModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(StringCombinerAdvancedModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(72, from(StringCombinerAdvancedModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(StringCombinerModelId.AdvancedStringCombiner2,
					from(StringCombinerAdvancedModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(25, from(StringCombinerAdvancedModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(14, from(StringCombinerAdvancedModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(53, from(StringCombinerAdvancedModelAccessor::getModelLength))
			.as("Model repeating instance count")
			.returns(2, from(StringCombinerAdvancedModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void values() {
		// GIVEN
		StringCombinerAdvancedModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Current")
			.returns(16.6f, from(StringCombinerAdvancedModelAccessor::getDCCurrent))
			.as("Charge 0 not accumulated, an acc32 for model 404")
			.returns(null, from(StringCombinerAdvancedModelAccessor::getDCChargeDelivered))
			.as("Voltage")
			.returns(602.0f, from(StringCombinerAdvancedModelAccessor::getDCVoltage))
			.as("Temperature")
			.returns(-5.0f, from(StringCombinerAdvancedModelAccessor::getTemperature))
			.as("Power")
			.returns(9960, from(StringCombinerAdvancedModelAccessor::getDCPower))
			.as("Energy")
			.returns(987650L, from(StringCombinerAdvancedModelAccessor::getDCEnergy))
			.as("Performance ratio 0x8000 not implemented, an int16 for model 404")
			.returns(null, from(StringCombinerAdvancedModelAccessor::getDCPerformanceRatio))
			;
		// @formatter:on
	}

	@Test
	public void events() {
		// GIVEN
		StringCombinerAdvancedModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Events")
			.returns(Set.<ModelEvent> of(StringCombinerModelEvent.CombinerCabinetOpen),
					from(StringCombinerAdvancedModelAccessor::getEvents))
			.as("Vendor events")
			.returns(Set.of(), from(StringCombinerAdvancedModelAccessor::getVendorEvents))
			;
		// @formatter:on
	}

	@Test
	public void inputs() {
		// WHEN
		List<AdvancedDcInput> inputs = getTestModel().getAdvancedDcInputs();

		// THEN
		// @formatter:off
		then(inputs)
			.as("Inputs count")
			.hasSize(2)
			;
		// @formatter:on

		// inputs use the input scale factors, and charge and energy are acc32 types
		assertAdvancedDcInput("Input 1", inputs.get(0), 1, 8.31f, 0xFFFFFFFFL, 602.1f, 4980, 493800L,
				0.97f, 14, Set.of(StringCombinerModelEvent.LowVoltage),
				Set.of(new GenericModelEvent(1)));
		assertAdvancedDcInput("Input 2", inputs.get(1), 2, null, null, null, -12, null, null, 14,
				Set.of(), Set.of());
	}

}
