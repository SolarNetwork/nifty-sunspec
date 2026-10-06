/* ==================================================================
 * MeterModelEventTests.java - 6/10/2026 5:34:52 pm
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

package net.solarnetwork.sunspec.core.meter.test;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.meter.MeterModelEvent;

/**
 * Test cases for the {@link MeterModelEvent} class.
 *
 * @author matt
 * @version 1.0
 */
public class MeterModelEventTests {

	@Test
	public void forBitmask() {
		// @formatter:off
		then(MeterModelEvent.forBitmask(0x14L))
			.as("Events from bits")
			.containsExactly(MeterModelEvent.PowerFailure, MeterModelEvent.LowPowerFactor)
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_highestEvent() {
		// @formatter:off
		then(MeterModelEvent.forBitmask(0x40000000L))
			.as("Event from bit 30")
			.containsExactly(MeterModelEvent.forIndex(30))
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_zero() {
		// @formatter:off
		then(MeterModelEvent.forBitmask(0L))
			.as("No events")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_notImplemented() {
		// @formatter:off
		then(MeterModelEvent.forBitmask(0xFFFFFFFFL))
			.as("Not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_mostSignificantBit() {
		// @formatter:off
		then(MeterModelEvent.forBitmask(0x80000014L))
			.as("Most significant bit set means not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

}
