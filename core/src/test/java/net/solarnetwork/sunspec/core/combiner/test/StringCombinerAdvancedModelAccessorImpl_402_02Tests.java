/* ==================================================================
 * StringCombinerAdvancedModelAccessorImpl_402_02Tests.java - 5/10/2026 7:44:39 am
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
 * using synthetic model 402 data with the 14 register input layout.
 *
 * @author matt
 * @version 1.0
 */
public class StringCombinerAdvancedModelAccessorImpl_402_02Tests {

	private StringCombinerAdvancedModelAccessor getTestModel() {
		ModelData data = ModelDataUtils.getModelDataInstance(getClass(), "test-data-402-02.txt");
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
			.returns(StringCombinerModelId.AdvancedStringCombiner,
					from(StringCombinerAdvancedModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(20, from(StringCombinerAdvancedModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(14, from(StringCombinerAdvancedModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(62, from(StringCombinerAdvancedModelAccessor::getModelLength))
			.as("Model repeating instance count")
			.returns(3, from(StringCombinerAdvancedModelAccessor::getRepeatingBlockInstanceCount))
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
			.returns(24.68f, from(StringCombinerAdvancedModelAccessor::getDCCurrent))
			.as("Charge")
			.returns(1000L, from(StringCombinerAdvancedModelAccessor::getDCChargeDelivered))
			.as("Voltage")
			.returns(601.2f, from(StringCombinerAdvancedModelAccessor::getDCVoltage))
			.as("Temperature")
			.returns(31.0f, from(StringCombinerAdvancedModelAccessor::getTemperature))
			.as("Power")
			.returns(14840, from(StringCombinerAdvancedModelAccessor::getDCPower))
			.as("Energy 0xFFFFFFFF not implemented, a uint32 for model 402")
			.returns(null, from(StringCombinerAdvancedModelAccessor::getDCEnergy))
			.as("Performance ratio")
			.returns(0.92f, from(StringCombinerAdvancedModelAccessor::getDCPerformanceRatio))
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
			.returns(Set.<ModelEvent> of(StringCombinerModelEvent.LowVoltage,
					StringCombinerModelEvent.GroundFault),
					from(StringCombinerAdvancedModelAccessor::getEvents))
			.as("Vendor events")
			.returns(Set.<ModelEvent> of(new GenericModelEvent(0), new GenericModelEvent(2)),
					from(StringCombinerAdvancedModelAccessor::getVendorEvents))
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
			.hasSize(3)
			;
		// @formatter:on

		// input power uses DCWh_SF and input energy is not scaled, per the model 402 definition
		assertAdvancedDcInput("Input 1", inputs.get(0), 1, 8.23f, 333L, 601.0f, 5000, 1234567L, 0.95f,
				12, Set.of(StringCombinerModelEvent.LowEfficiency), Set.of(new GenericModelEvent(16)));
		assertAdvancedDcInput("Input 2", inputs.get(1), 2, 8.22f, 0L, 601.4f, -100, null, null, 12,
				Set.of(), Set.of());
		assertAdvancedDcInput("Input 3", inputs.get(2), 3, null, null, null, null, 0L, 0.0f, null,
				Set.of(StringCombinerModelEvent.Disconnected), Set.of());
	}

}
