/* ==================================================================
 * StringCombinerModelEvent.java - 5/10/2018 4:24:50 PM
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

package net.solarnetwork.sunspec.api.combiner;

import java.util.Set;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.SunSpecUtils;

/**
 * String combiner type events.
 *
 * @author matt
 * @version 1.1
 * @since 1.4
 */
public enum StringCombinerModelEvent implements ModelEvent {

	/** Low voltage. */
	LowVoltage(0, "Low voltage"),

	/** Low power. */
	LowPower(1, "Low power"),

	/** Low efficiency. */
	LowEfficiency(2, "Low efficiency"),

	/** Current. */
	Current(3, "Current"),

	/** Voltage. */
	Voltage(4, "Voltage"),

	/** Power. */
	Power(5, "Power"),

	/** PR. */
	PR(6, "PR"),

	/** Disconnected. */
	Disconnected(7, "Disconnected"),

	/** FuseFault. */
	FuseFault(8, "FuseFault"),

	/** Combiner fuse fault. */
	CombinerFuseFault(9, "Combiner fuse fault"),

	/** Combiner cabinet open. */
	CombinerCabinetOpen(10, "Combiner cabinet open"),

	/** Temperature. */
	Temperature(11, "Temperature"),

	/** Ground fault. */
	GroundFault(12, "Ground fault"),

	/** Reversed polarity. */
	ReversedPolarity(13, "Reversed polarity"),

	/** Incompatible. */
	Incompatible(14, "Incompatible"),

	/** Communication error. */
	CommError(15, "Communication error"),

	/** Internal error. */
	InternalError(16, "Internal error"),

	/** Theft. */
	Theft(17, "Theft"),

	/** Arc detected. */
	ArcDetected(18, "Arc detected");

	private final int index;
	private final String description;

	private StringCombinerModelEvent(int index, String description) {
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
	public static StringCombinerModelEvent forIndex(int index) {
		for ( StringCombinerModelEvent e : StringCombinerModelEvent.values() ) {
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
	public static Set<StringCombinerModelEvent> forBitmask(long bitmask) {
		return SunSpecUtils.bitfieldValues(bitmask, 2, StringCombinerModelEvent.class);
	}

}
