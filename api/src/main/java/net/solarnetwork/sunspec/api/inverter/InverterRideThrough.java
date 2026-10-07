/* ==================================================================
 * InverterRideThrough.java - 6/10/2026 7:34:21 am
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

package net.solarnetwork.sunspec.api.inverter;

import net.solarnetwork.sunspec.api.Bitmaskable;

/**
 * Inverter ride-through functions.
 *
 * @author matt
 * @version 1.0
 */
public enum InverterRideThrough implements Bitmaskable {

	/** Low voltage ride-through. */
	LowVoltage(0, "Low voltage ride-through"),

	/** High voltage ride-through. */
	HighVoltage(1, "High voltage ride-through"),

	/** Low frequency ride-through. */
	LowFrequency(2, "Low frequency ride-through"),

	/** High frequency ride-through. */
	HighFrequency(3, "High frequency ride-through"),

	;

	private final int offset;
	private final String description;

	private InverterRideThrough(int offset, String description) {
		this.offset = offset;
		this.description = description;
	}

	@Override
	public int bitmaskBitOffset() {
		return offset;
	}

	/**
	 * Get a description of the ride-through.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
