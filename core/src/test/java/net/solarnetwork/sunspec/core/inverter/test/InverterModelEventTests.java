/* ==================================================================
 * InverterModelEventTests.java - 6/10/2026 12:16:05 pm
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
