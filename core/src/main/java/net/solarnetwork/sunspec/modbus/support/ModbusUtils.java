/* ==================================================================
 * ModbusUtils.java - 6 Oct 2026 7:14:38 pm
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
