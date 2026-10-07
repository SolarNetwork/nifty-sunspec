/* ==================================================================
 * DerReactivePowerSetpointMode.java - 5/10/2026 2:41:17 pm
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

import net.solarnetwork.sunspec.api.CodedValue;

/**
 * DER reactive power setpoint mode.
 *
 * @author matt
 * @version 1.0
 */
public enum DerReactivePowerSetpointMode implements CodedValue {

	/** Percentage of maximum active power. */
	MaximumActivePowerPercent(0, "Percentage of maximum active power"),

	/** Percentage of maximum reactive power. */
	MaximumReactivePowerPercent(1, "Percentage of maximum reactive power"),

	/** Percentage of available reactive power. */
	AvailableReactivePowerPercent(2, "Percentage of available reactive power"),

	/** Percentage of maximum apparent power. */
	MaximumApparentPowerPercent(3, "Percentage of maximum apparent power"),

	/** Vars. */
	Vars(4, "Vars"),

	;

	private final int code;
	private final String description;

	private DerReactivePowerSetpointMode(int code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public int getCode() {
		return code;
	}

	/**
	 * Get a description of the mode.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
