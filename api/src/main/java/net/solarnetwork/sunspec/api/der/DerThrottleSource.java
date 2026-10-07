/* ==================================================================
 * DerThrottleSource.java - 5/10/2026 8:27:22 am
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

import net.solarnetwork.sunspec.api.Bitmaskable;

/**
 * DER active power throttling sources.
 *
 * @author matt
 * @version 1.0
 */
public enum DerThrottleSource implements Bitmaskable {

	/** Limit maximum active power. */
	MaxActivePower(0, "Limit maximum active power"),

	/** Fixed active power. */
	FixedActivePower(1, "Fixed active power"),

	/** Fixed reactive power. */
	FixedReactivePower(2, "Fixed reactive power"),

	/** Fixed power factor. */
	FixedPowerFactor(3, "Fixed power factor"),

	/** Volt-var function. */
	VoltVar(4, "Volt-var function"),

	/** Frequency-watt function. */
	FrequencyWatt(5, "Frequency-watt function"),

	/** Dynamic reactive current function. */
	DynamicReactiveCurrent(6, "Dynamic reactive current function"),

	/** Low voltage ride-through. */
	LowVoltageRideThrough(7, "Low voltage ride-through"),

	/** High voltage ride-through. */
	HighVoltageRideThrough(8, "High voltage ride-through"),

	/** Watt-var function. */
	WattVar(9, "Watt-var function"),

	/** Volt-watt function. */
	VoltWatt(10, "Volt-watt function"),

	/** Scheduling. */
	Scheduled(11, "Scheduling"),

	/** Low frequency ride-through. */
	LowFrequencyRideThrough(12, "Low frequency ride-through"),

	/** High frequency ride-through. */
	HighFrequencyRideThrough(13, "High frequency ride-through"),

	/** Derated. */
	Derated(14, "Derated"),

	;

	private final int offset;
	private final String description;

	private DerThrottleSource(int offset, String description) {
		this.offset = offset;
		this.description = description;
	}

	@Override
	public int bitmaskBitOffset() {
		return offset;
	}

	/**
	 * Get a description of the throttling source.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
