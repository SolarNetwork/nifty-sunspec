/* ==================================================================
 * InverterModelEventTests.java - 6/10/2026 12:16:05 pm
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

package net.solarnetwork.sunspec.core.inverter.test;

import static org.assertj.core.api.BDDAssertions.then;
import java.util.EnumSet;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.inverter.InverterModelEvent;

/**
 * Test cases for the {@link InverterModelEvent} class.
 *
 * @author matt
 * @version 1.0
 */
public class InverterModelEventTests {

	@Test
	public void forBitmask() {
		// @formatter:off
		then(InverterModelEvent.forBitmask(0x5L))
			.as("Events from bits")
			.isEqualTo(EnumSet.of(InverterModelEvent.GroundFault, InverterModelEvent.AcDisconnect))
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_highestEvent() {
		// @formatter:off
		then(InverterModelEvent.forBitmask(0x40000000L))
			.as("Highest defined event")
			.isEqualTo(EnumSet.of(InverterModelEvent.OEM_15))
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_zero() {
		// @formatter:off
		then(InverterModelEvent.forBitmask(0L))
			.as("No events")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_notImplemented() {
		// @formatter:off
		then(InverterModelEvent.forBitmask(0xFFFFFFFFL))
			.as("Not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_mostSignificantBit() {
		// @formatter:off
		then(InverterModelEvent.forBitmask(0x80000000L | 0x5L))
			.as("Most significant bit set means not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

}
