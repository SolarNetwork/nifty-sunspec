/* ==================================================================
 * ModbusUtils.java - 6 Oct 2026 7:14:38 pm
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

package net.solarnetwork.sunspec.modbus.support;

import java.util.List;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.util.IntRange;

/**
 * Modbus utilities.
 *
 * @author matt
 * @version 1.0
 */
public final class ModbusUtils {

	private ModbusUtils() {
		// not available
	}

	/**
	 * Add the address ranges of registers that span more than one register to a
	 * list.
	 *
	 * @param ranges
	 *        the list to add the ranges to
	 * @param address
	 *        the address the register addresses are relative to
	 * @param refs
	 *        the registers
	 * @since 2.1
	 */
	public static void addMultiRegisterAddressRanges(final List<IntRange> ranges, final int address,
			final Iterable<? extends ModbusReference> refs) {
		for ( ModbusReference ref : refs ) {
			final int len = ref.getWordLength();
			if ( len > 1 ) {
				final int start = address + ref.getAddress();
				ranges.add(new IntRange(start, start + len - 1));
			}
		}
	}

}
