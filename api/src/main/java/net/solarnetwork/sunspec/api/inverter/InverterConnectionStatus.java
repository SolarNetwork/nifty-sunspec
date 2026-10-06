/* ==================================================================
 * InverterConnectionStatus.java - 6/10/2026 7:30:12 am
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
 * Inverter connection status flags.
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum InverterConnectionStatus implements Bitmaskable {

	/** Connected. */
	Connected(0, "Connected"),

	/** Available. */
	Available(1, "Available"),

	/** Operating. */
	Operating(2, "Operating"),

	/** Test. */
	Test(3, "Test"),

	;

	private final int offset;
	private final String description;

	private InverterConnectionStatus(int offset, String description) {
		this.offset = offset;
		this.description = description;
	}

	@Override
	public int bitmaskBitOffset() {
		return offset;
	}

	/**
	 * Get a description of the status.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
