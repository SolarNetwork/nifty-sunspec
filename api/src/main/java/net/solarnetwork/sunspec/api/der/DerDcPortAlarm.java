/* ==================================================================
 * DerDcPortAlarm.java - 5/10/2026 10:12:08 am
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

package net.solarnetwork.sunspec.api.der;

import java.util.Set;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.SunSpecUtils;

/**
 * DER DC port alarms.
 *
 * @author matt
 * @version 1.0
 */
public enum DerDcPortAlarm implements ModelEvent {

	/** Ground fault. */
	GroundFault(0, "Ground fault"),

	/** Input over voltage. */
	InputOverVoltage(1, "Input over voltage"),

	/** DC disconnect. */
	DcDisconnect(3, "DC disconnect"),

	/** Cabinet open. */
	CabinetOpen(5, "Cabinet open"),

	/** Manual shutdown. */
	ManualShutdown(6, "Manual shutdown"),

	/** Over temperature. */
	OverTemperature(7, "Over temperature"),

	/** Blown fuse. */
	BlownFuse(12, "Blown fuse"),

	/** Under temperature. */
	UnderTemperature(13, "Under temperature"),

	/** Memory loss. */
	MemoryLoss(14, "Memory loss"),

	/** Arc detection. */
	ArcDetection(15, "Arc detection"),

	/** Reserved. */
	Reserved(19, "Reserved"),

	/** Test failed. */
	TestFailed(20, "Test failed"),

	/** Input under voltage. */
	InputUnderVoltage(21, "Input under voltage"),

	/** Input over current. */
	InputOverCurrent(22, "Input over current"),

	;

	private final int index;
	private final String description;

	private DerDcPortAlarm(int index, String description) {
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
	 * Get a set of alarms for a bitmask value.
	 *
	 * <p>
	 * If the most significant bit of the 32-bit value is set, the alarms are
	 * not implemented and an empty set is returned.
	 * </p>
	 *
	 * @param bitmask
	 *        the bitmask value
	 * @return the alarms, never {@code null}
	 */
	public static Set<DerDcPortAlarm> forBitmask(long bitmask) {
		return SunSpecUtils.bitfieldValues(bitmask, 2, DerDcPortAlarm.class);
	}

}
