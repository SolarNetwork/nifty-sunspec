/* ==================================================================
 * StringCombinerModelAccessorImpl_403_01Tests.java - 5/10/2026 7:44:39 am
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

package net.solarnetwork.sunspec.core.combiner.test;

import static net.solarnetwork.sunspec.core.combiner.test.StringCombinerTestUtils.assertDcInput;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.GenericModelEvent;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelAccessor;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelEvent;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelId;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelAccessor.DcInput;
import net.solarnetwork.sunspec.core.combiner.StringCombinerModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link StringCombinerModelAccessorImpl} class, using
 * synthetic model 403 data.
 *
 * @author matt
 * @version 1.0
 */
public class StringCombinerModelAccessorImpl_403_01Tests {

	private StringCombinerModelAccessor getTestModel() {
		ModelData data = ModelDataUtils.getModelDataInstance(getClass(), "test-data-403-01.txt");
		return data.findTypedModel(StringCombinerModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(StringCombinerModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		StringCombinerModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(StringCombinerModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(72, from(StringCombinerModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(StringCombinerModelId.BasicStringCombiner2,
					from(StringCombinerModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(16, from(StringCombinerModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(8, from(StringCombinerModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(32, from(StringCombinerModelAccessor::getModelLength))
			.as("Model repeating instance count")
			.returns(2, from(StringCombinerModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void values() {
		// GIVEN
		StringCombinerModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Current")
			.returns(16.3f, from(StringCombinerModelAccessor::getDCCurrent))
			.as("Charge 0xFFFFFFFF is a value, an acc32 for model 403")
			.returns(BigDecimal.valueOf(0xFFFFFFFFL), from(StringCombinerModelAccessor::getDCChargeDelivered))
			.as("Voltage 0x8000 not implemented, an int16 for model 403")
			.returns(null, from(StringCombinerModelAccessor::getDCVoltage))
			.as("Temperature")
			.returns(28.0f, from(StringCombinerModelAccessor::getTemperature))
			;
		// @formatter:on
	}

	@Test
	public void events() {
		// GIVEN
		StringCombinerModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Events")
			.returns(Set.<ModelEvent> of(StringCombinerModelEvent.ReversedPolarity),
					from(StringCombinerModelAccessor::getEvents))
			.as("Vendor events")
			.returns(Set.of(), from(StringCombinerModelAccessor::getVendorEvents))
			;
		// @formatter:on
	}

	@Test
	public void inputs() {
		// WHEN
		List<DcInput> inputs = getTestModel().getDcInputs();

		// THEN
		// @formatter:off
		then(inputs)
			.as("Inputs count")
			.hasSize(2)
			;
		// @formatter:on

		// inputs use the input scale factors, and charge is an acc32 type
		assertDcInput("Input 1", inputs.get(0), 1, 8.15f, new BigDecimal("43210"),
				Set.of(StringCombinerModelEvent.FuseFault), Set.of(new GenericModelEvent(2)));
		assertDcInput("Input 2", inputs.get(1), 2, 8.12f, null, Set.of(), Set.of());
	}

}
