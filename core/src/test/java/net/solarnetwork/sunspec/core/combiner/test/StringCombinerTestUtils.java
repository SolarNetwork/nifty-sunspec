/* ==================================================================
 * StringCombinerTestUtils.java - 5/10/2026 7:44:39 am
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

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.util.Set;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.combiner.StringCombinerAdvancedModelAccessor.AdvancedDcInput;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelAccessor.DcInput;

/**
 * Helper methods for string combiner tests.
 *
 * @author matt
 * @version 1.0
 */
public final class StringCombinerTestUtils {

	private StringCombinerTestUtils() {
		// not available
	}

	/**
	 * Assert the properties of a DC input.
	 *
	 * @param prefix
	 *        the assertion message prefix
	 * @param input
	 *        the input to verify
	 * @param id
	 *        the expected input ID
	 * @param current
	 *        the expected current
	 * @param charge
	 *        the expected charge delivered
	 * @param events
	 *        the expected events
	 * @param vendorEvents
	 *        the expected vendor events
	 */
	public static void assertDcInput(String prefix, DcInput input, Integer id, Float current,
			Long charge, Set<ModelEvent> events, Set<ModelEvent> vendorEvents) {
		// @formatter:off
		then(input)
			.as(prefix + " ID")
			.returns(id, from(DcInput::getInputId))
			.as(prefix + " current")
			.returns(current, from(DcInput::getDCCurrent))
			.as(prefix + " charge")
			.returns(charge, from(DcInput::getDCChargeDelivered))
			.as(prefix + " events")
			.returns(events, from(DcInput::getEvents))
			.as(prefix + " vendor events")
			.returns(vendorEvents, from(DcInput::getVendorEvents))
			;
		// @formatter:on
	}

	/**
	 * Assert the properties of an advanced DC input.
	 *
	 * @param prefix
	 *        the assertion message prefix
	 * @param input
	 *        the input to verify
	 * @param id
	 *        the expected input ID
	 * @param current
	 *        the expected current
	 * @param charge
	 *        the expected charge delivered
	 * @param voltage
	 *        the expected voltage
	 * @param power
	 *        the expected power
	 * @param energy
	 *        the expected energy
	 * @param performanceRatio
	 *        the expected performance ratio
	 * @param moduleCount
	 *        the expected module count
	 * @param events
	 *        the expected events
	 * @param vendorEvents
	 *        the expected vendor events
	 */
	public static void assertAdvancedDcInput(String prefix, AdvancedDcInput input, Integer id,
			Float current, Long charge, Float voltage, Integer power, Long energy,
			Float performanceRatio, Integer moduleCount, Set<ModelEvent> events,
			Set<ModelEvent> vendorEvents) {
		// @formatter:off
		then(input)
			.as(prefix + " ID")
			.returns(id, from(AdvancedDcInput::getInputId))
			.as(prefix + " current")
			.returns(current, from(AdvancedDcInput::getDCCurrent))
			.as(prefix + " charge")
			.returns(charge, from(AdvancedDcInput::getDCChargeDelivered))
			.as(prefix + " voltage")
			.returns(voltage, from(AdvancedDcInput::getDCVoltage))
			.as(prefix + " power")
			.returns(power, from(AdvancedDcInput::getDCPower))
			.as(prefix + " energy")
			.returns(energy, from(AdvancedDcInput::getDCEnergy))
			.as(prefix + " performance ratio")
			.returns(performanceRatio, from(AdvancedDcInput::getDCPerformanceRatio))
			.as(prefix + " module count")
			.returns(moduleCount, from(AdvancedDcInput::getModuleCount))
			.as(prefix + " events")
			.returns(events, from(AdvancedDcInput::getEvents))
			.as(prefix + " vendor events")
			.returns(vendorEvents, from(AdvancedDcInput::getVendorEvents))
			;
		// @formatter:on
	}

}
