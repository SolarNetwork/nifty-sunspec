/* ==================================================================
 * StringCombinerModelEventTests.java - 6/10/2026 12:18:40 pm
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

import static org.assertj.core.api.BDDAssertions.then;
import java.util.EnumSet;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelEvent;

/**
 * Test cases for the {@link StringCombinerModelEvent} class.
 *
 * @author matt
 * @version 1.0
 */
public class StringCombinerModelEventTests {

	@Test
	public void forBitmask() {
		// @formatter:off
		then(StringCombinerModelEvent.forBitmask(0x40001L))
			.as("Events from bits")
			.isEqualTo(EnumSet.of(StringCombinerModelEvent.LowVoltage,
					StringCombinerModelEvent.ArcDetected))
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_highestEvent() {
		// @formatter:off
		then(StringCombinerModelEvent.forBitmask(0x40000L))
			.as("Highest defined event")
			.isEqualTo(EnumSet.of(StringCombinerModelEvent.ArcDetected))
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_zero() {
		// @formatter:off
		then(StringCombinerModelEvent.forBitmask(0L))
			.as("No events")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_notImplemented() {
		// @formatter:off
		then(StringCombinerModelEvent.forBitmask(0xFFFFFFFFL))
			.as("Not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_mostSignificantBit() {
		// @formatter:off
		then(StringCombinerModelEvent.forBitmask(0x80000000L | 0x40001L))
			.as("Most significant bit set means not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

}
