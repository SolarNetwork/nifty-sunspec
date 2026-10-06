/* ==================================================================
 * DerAlarm.java - 5/10/2026 8:27:22 am
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
 * DER alarms.
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum DerAlarm implements ModelEvent {

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
	HardwareTestFailure(15, "Hardware test failure"),

	/** Manufacturer alarm, see the manufacturer alarm information. */
	ManufacturerAlarm(16, "Manufacturer alarm"),

	;

	private final int index;
	private final String description;

	private DerAlarm(int index, String description) {
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
	 * Get a set of alarms from a bitmask.
	 *
	 * <p>
	 * SunSpec bitfields never have their most significant bit set, so a bitmask
	 * with that bit set, including the SunSpec "not implemented" value, results
	 * in an empty set.
	 * </p>
	 *
	 * @param bitmask
	 *        the bitmask
	 * @return the active alarms, never {@code null}
	 */
	public static Set<DerAlarm> forBitmask(long bitmask) {
		return SunSpecUtils.bitfieldValues(bitmask, 2, DerAlarm.class);
	}

}
