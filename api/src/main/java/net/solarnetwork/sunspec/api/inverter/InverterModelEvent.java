/* ==================================================================
 * InverterModelEvent.java - 5/10/2018 4:24:50 PM
 * 
 * Copyright 2018 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.api.inverter;

import java.util.Set;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.SunSpecUtils;

/**
 * Inverter type events.
 * 
 * @author matt
 * @version 1.0
 */
public enum InverterModelEvent implements ModelEvent {

	/** Ground fault. */
	GroundFault(0, "Ground fault"),

	/** DC over voltage. */
	DcOverVoltage(1, "DC over voltage"),

	/** AC disconnect open. */
	AcDisconnect(2, "AC disconnect open"),

	/** DC disconnect open. */
	DcDisconnect(3, "DC disconnect open"),

	/** Grid disconnect. */
	GridDisconnect(4, "Grid disconnect"),

	/** Cabinet open. */
	CabinetOpen(5, "Cabinet open"),

	/** Manual shutdown. */
	ManualShutdown(6, "Manual shutdown"),

	/** Over temperature. */
	OverTemperature(7, "Over temperature"),

	/** Frequency above limit. */
	OverFrequency(8, "Frequency above limit"),

	/** Frequency under limit. */
	UnderFrequency(9, "Frequency under limit"),

	/** AC voltage above limit. */
	AcOverVoltage(10, "AC voltage above limit"),

	/** AC voltage under limit. */
	AcUnderVoltage(11, "AC voltage under limit"),

	/** Blown string fuse on input. */
	BlownStringFuse(12, "Blown string fuse on input"),

	/** Under temperature. */
	UnderTemperature(13, "Under temperature"),

	/** Generic memory or communication error (internal). */
	MemoryLoss(14, "Generic memory or communication error (internal)"),

	/** Hardware test failure. */
	HwTestFailure(15, "Hardware test failure"),

	/** OEM 1. */
	OEM_01(16, "OEM 1"),

	/** OEM 2. */
	OEM_02(17, "OEM 2"),

	/** OEM 3. */
	OEM_03(18, "OEM 3"),

	/** OEM 4. */
	OEM_04(19, "OEM 4"),

	/** OEM 5. */
	OEM_05(20, "OEM 5"),

	/** OEM 6. */
	OEM_06(21, "OEM 6"),

	/** OEM 7. */
	OEM_07(22, "OEM 7"),

	/** OEM 8. */
	OEM_08(23, "OEM 8"),

	/** OEM 9. */
	OEM_09(24, "OEM 9"),

	/** OEM 10. */
	OEM_10(25, "OEM 10"),

	/** OEM 11. */
	OEM_11(26, "OEM 11"),

	/** OEM 12. */
	OEM_12(27, "OEM 12"),

	/** OEM 13. */
	OEM_13(28, "OEM 13"),

	/** OEM 14. */
	OEM_14(29, "OEM 14"),

	/** OEM 15. */
	OEM_15(30, "OEM 15"),

	;

	private final int index;
	private final String description;

	private InverterModelEvent(int index, String description) {
		this.index = index;
		this.description = description;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Get an enumeration for an index value.
	 * 
	 * @param index
	 *        the ID to get the enum value for
	 * @return the enumeration value
	 * @throws IllegalArgumentException
	 *         if {@code index} is not supported
	 */
	public static InverterModelEvent forIndex(int index) {
		for ( InverterModelEvent e : InverterModelEvent.values() ) {
			if ( e.index == index ) {
				return e;
			}
		}
		throw new IllegalArgumentException("Index [" + index + "] not supported");
	}

	/**
	 * Get a set of events from a bitmask.
	 *
	 * <p>
	 * SunSpec bitfields never have their most significant bit set, so a 32-bit
	 * bitmask with that bit set, including the SunSpec "not implemented" value,
	 * results in an empty set.
	 * </p>
	 *
	 * @param bitmask
	 *        the 32-bit bitmask
	 * @return the active events
	 */
	public static Set<InverterModelEvent> forBitmask(long bitmask) {
		return SunSpecUtils.bitfieldValues(bitmask, 2, InverterModelEvent.class);
	}

}
