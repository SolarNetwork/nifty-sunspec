/* ==================================================================
 * InverterChargeSource.java - 6/10/2026 8:42:55 am
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

import net.solarnetwork.domain.CodedValue;

/**
 * Inverter storage charge source setting.
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum InverterChargeSource implements CodedValue {

	/** PV. */
	Pv(0, "PV"),

	/** Grid. */
	Grid(1, "Grid"),

	;

	private final int code;
	private final String description;

	private InverterChargeSource(int code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public int getCode() {
		return code;
	}

	/**
	 * Get a description of the source.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
