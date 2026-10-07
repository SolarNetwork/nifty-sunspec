/* ==================================================================
 * BatteryDisabledReason.java - 5/10/2026 9:05:44 pm
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
 * Battery string disabled reason.
 *
 * @author matt
 * @version 1.0
 */
public enum BatteryDisabledReason implements CodedValue {

	/** Not disabled. */
	None(0, "Not disabled"),

	/** Fault. */
	Fault(1, "Fault"),

	/** Maintenance. */
	Maintenance(2, "Maintenance"),

	/** Disabled by an operator or external controller. */
	External(3, "Disabled by an operator or external controller"),

	/** Other. */
	Other(4, "Other"),

	;

	private final int code;
	private final String description;

	private BatteryDisabledReason(int code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public int getCode() {
		return code;
	}

	/**
	 * Get a description of the reason.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
