/* ==================================================================
 * MeterModelEvent.java - 22/05/2018 5:57:49 AM
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

package net.solarnetwork.sunspec.api.meter;

import java.util.Set;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.SunSpecUtils;

/**
 * Meter type events.
 * 
 * @author matt
 * @version 1.0
 */
public enum MeterModelEvent implements ModelEvent {

	/** Loss of power or phase. */
	PowerFailure(2, "Loss of power or phase"),

	/** Voltage below threshold (phase loss). */
	UnderVoltage(3, "Voltage below threshold (phase loss)"),

	/** Power factor below threshold. */
	LowPowerFactor(4, "Power factor below threshold"),

	/** Current input over threshold. */
	OverCurrent(5, "Current input over threshold"),

	/** Voltage input over threshold. */
	OverVoltage(6, "Voltage input over threshold"),

	/** Sensor not connected. */
	MissingSensor(7, "Sensor not connected"),

	/** Reserved 1. */
	Reserved_01(8, "Reserved 1"),

	/** Reserved 2. */
	Reserved_02(9, "Reserved 2"),

	/** Reserved 3. */
	Reserved_03(10, "Reserved 3"),

	/** Reserved 4. */
	Reserved_04(11, "Reserved 4"),

	/** Reserved 5. */
	Reserved_05(12, "Reserved 5"),

	/** Reserved 6. */
	Reserved_06(13, "Reserved 6"),

	/** Reserved 7. */
	Reserved_07(14, "Reserved 7"),

	/** Reserved 8. */
	Reserved_08(15, "Reserved 8"),

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

	private MeterModelEvent(int index, String description) {
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
	public static MeterModelEvent forIndex(int index) {
		for ( MeterModelEvent e : MeterModelEvent.values() ) {
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
	public static Set<MeterModelEvent> forBitmask(long bitmask) {
		return SunSpecUtils.bitfieldValues(bitmask, 2, MeterModelEvent.class);
	}

}
