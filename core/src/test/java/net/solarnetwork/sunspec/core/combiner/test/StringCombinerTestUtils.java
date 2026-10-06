/* ==================================================================
 * StringCombinerTestUtils.java - 5/10/2026 7:44:39 am
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
