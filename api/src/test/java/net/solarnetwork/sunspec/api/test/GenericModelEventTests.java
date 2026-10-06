/* ==================================================================
 * GenericModelEventTests.java - 5/10/2026 10:31:20 pm
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

package net.solarnetwork.sunspec.api.test;

import static org.assertj.core.api.BDDAssertions.then;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.GenericModelEvent;
import net.solarnetwork.sunspec.api.ModelEvent;

/**
 * Test cases for the {@link GenericModelEvent} class.
 *
 * @author matt
 * @version 1.0
 */
public class GenericModelEventTests {

	@Test
	public void forBitmask() {
		// @formatter:off
		then(GenericModelEvent.forBitmask(0x40000005L))
			.as("Events for the set bits")
			.isEqualTo(Set.<ModelEvent> of(new GenericModelEvent(0), new GenericModelEvent(2),
					new GenericModelEvent(30)))
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_none() {
		// @formatter:off
		then(GenericModelEvent.forBitmask(0L))
			.as("No events")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_notImplemented() {
		// @formatter:off
		then(GenericModelEvent.forBitmask(0xFFFFFFFFL))
			.as("Not implemented value has no events")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void forBitmask_mostSignificantBit() {
		// @formatter:off
		then(GenericModelEvent.forBitmask(0x80000001L))
			.as("Bitmask with the most significant bit set is not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

}
