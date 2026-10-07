/* ==================================================================
 * BatteryType.java - 5/10/2026 7:58:02 pm
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
 * Battery type.
 *
 * @author matt
 * @version 1.0
 */
public enum BatteryType implements CodedValue {

	/** Not applicable or unknown. */
	Unknown(0, "Not applicable or unknown"),

	/** Lead acid. */
	LeadAcid(1, "Lead acid"),

	/** Nickel metal hydride. */
	NickelMetalHydride(2, "Nickel metal hydride"),

	/** Nickel cadmium. */
	NickelCadmium(3, "Nickel cadmium"),

	/** Lithium-ion. */
	LithiumIon(4, "Lithium-ion"),

	/** Carbon zinc. */
	CarbonZinc(5, "Carbon zinc"),

	/** Zinc chloride. */
	ZincChloride(6, "Zinc chloride"),

	/** Alkaline. */
	Alkaline(7, "Alkaline"),

	/** Rechargeable alkaline. */
	RechargeableAlkaline(8, "Rechargeable alkaline"),

	/** Sodium sulfur. */
	SodiumSulfur(9, "Sodium sulfur"),

	/** Flow. */
	Flow(10, "Flow"),

	/** Other. */
	Other(99, "Other"),

	;

	private final int code;
	private final String description;

	private BatteryType(int code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public int getCode() {
		return code;
	}

	/**
	 * Get a description of the type.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
