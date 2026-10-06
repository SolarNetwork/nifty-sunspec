/* ==================================================================
 * InverterControlFunction.java - 6/10/2026 7:33:05 am
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

import net.solarnetwork.domain.Bitmaskable;

/**
 * Inverter control functions.
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum InverterControlFunction implements Bitmaskable {

	/** Fixed active power. */
	FixedActivePower(0, "Fixed active power"),

	/** Fixed reactive power. */
	FixedReactivePower(1, "Fixed reactive power"),

	/** Fixed power factor. */
	FixedPowerFactor(2, "Fixed power factor"),

	/** Volt-var function. */
	VoltVar(3, "Volt-var function"),

	/** Parameterized frequency-watt function. */
	ParameterizedFrequencyWatt(4, "Parameterized frequency-watt function"),

	/** Curve-based frequency-watt function. */
	CurveBasedFrequencyWatt(5, "Curve-based frequency-watt function"),

	/** Dynamic reactive current function. */
	DynamicReactiveCurrent(6, "Dynamic reactive current function"),

	/** Low voltage ride-through. */
	LowVoltageRideThrough(7, "Low voltage ride-through"),

	/** High voltage ride-through. */
	HighVoltageRideThrough(8, "High voltage ride-through"),

	/** Watt-power factor function. */
	WattPowerFactor(9, "Watt-power factor function"),

	/** Volt-watt function. */
	VoltWatt(10, "Volt-watt function"),

	/** Scheduling. */
	Scheduled(12, "Scheduling"),

	/** Low frequency ride-through. */
	LowFrequencyRideThrough(13, "Low frequency ride-through"),

	/** High frequency ride-through. */
	HighFrequencyRideThrough(14, "High frequency ride-through"),

	;

	private final int offset;
	private final String description;

	private InverterControlFunction(int offset, String description) {
		this.offset = offset;
		this.description = description;
	}

	@Override
	public int bitmaskBitOffset() {
		return offset;
	}

	/**
	 * Get a description of the function.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
