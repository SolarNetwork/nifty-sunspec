/* ==================================================================
 * BatteryState.java - 5/10/2026 7:58:02 pm
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
 * Battery bank state.
 *
 * @author matt
 * @version 1.0
 */
public enum BatteryState implements CodedValue {

	/** Disconnected. */
	Disconnected(1, "Disconnected"),

	/** Initializing. */
	Initializing(2, "Initializing"),

	/** Connected. */
	Connected(3, "Connected"),

	/** Standby. */
	Standby(4, "Standby"),

	/** State of charge protection. */
	SocProtection(5, "State of charge protection"),

	/** Suspending. */
	Suspending(6, "Suspending"),

	/** Fault. */
	Fault(99, "Fault"),

	;

	private final int code;
	private final String description;

	private BatteryState(int code, String description) {
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
