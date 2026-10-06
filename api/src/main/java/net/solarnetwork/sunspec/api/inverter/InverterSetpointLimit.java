/* ==================================================================
 * InverterSetpointLimit.java - 6/10/2026 7:31:40 am
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
 * Inverter setpoint limits.
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum InverterSetpointLimit implements Bitmaskable {

	/** Maximum active power. */
	MaximumActivePower(0, "Maximum active power"),

	/** Maximum apparent power. */
	MaximumApparentPower(1, "Maximum apparent power"),

	/** Available reactive power. */
	AvailableReactivePower(2, "Available reactive power"),

	/** Maximum reactive power in quadrant 1. */
	MaximumReactivePowerQ1(3, "Maximum reactive power in quadrant 1"),

	/** Maximum reactive power in quadrant 2. */
	MaximumReactivePowerQ2(4, "Maximum reactive power in quadrant 2"),

	/** Maximum reactive power in quadrant 3. */
	MaximumReactivePowerQ3(5, "Maximum reactive power in quadrant 3"),

	/** Maximum reactive power in quadrant 4. */
	MaximumReactivePowerQ4(6, "Maximum reactive power in quadrant 4"),

	/** Minimum power factor in quadrant 1. */
	MinimumPowerFactorQ1(7, "Minimum power factor in quadrant 1"),

	/** Minimum power factor in quadrant 2. */
	MinimumPowerFactorQ2(8, "Minimum power factor in quadrant 2"),

	/** Minimum power factor in quadrant 3. */
	MinimumPowerFactorQ3(9, "Minimum power factor in quadrant 3"),

	/** Minimum power factor in quadrant 4. */
	MinimumPowerFactorQ4(10, "Minimum power factor in quadrant 4"),

	;

	private final int offset;
	private final String description;

	private InverterSetpointLimit(int offset, String description) {
		this.offset = offset;
		this.description = description;
	}

	@Override
	public int bitmaskBitOffset() {
		return offset;
	}

	/**
	 * Get a description of the limit.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
