/* ==================================================================
 * DerGridConnectionState.java - 5/10/2026 8:27:22 am
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
 * DER grid connection state.
 *
 * @author matt
 * @version 1.0
 */
public enum DerGridConnectionState implements CodedValue {

	/** Disconnected from the grid. */
	Disconnected(0, "Disconnected from the grid"),

	/** Connected to the grid. */
	Connected(1, "Connected to the grid"),

	;

	private final int code;
	private final String description;

	private DerGridConnectionState(int code, String description) {
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
