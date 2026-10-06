/* ==================================================================
 * MeterModelEventTests.java - 6/10/2026 5:34:52 pm
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
