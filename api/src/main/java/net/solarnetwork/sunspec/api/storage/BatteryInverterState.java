/* ==================================================================
 * BatteryInverterState.java - 5/10/2026 7:58:02 pm
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

package net.solarnetwork.sunspec.api.storage;

import net.solarnetwork.sunspec.api.CodedValue;

/**
 * Inverter state, as given to the battery.
 *
 * @author matt
 * @version 1.0
 */
public enum BatteryInverterState implements CodedValue {

	/** Inverter stopped. */
	Stopped(1, "Inverter stopped"),

	/** Inverter standby. */
	Standby(2, "Inverter standby"),

	/** Inverter started. */
	Started(3, "Inverter started"),

	;

	private final int code;
	private final String description;

	private BatteryInverterState(int code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public int getCode() {
		return code;
	}

	/**
	 * Get a description of the state.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
