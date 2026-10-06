/* ==================================================================
 * GenericModelEventTests.java - 5/10/2026 10:31:20 pm
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
