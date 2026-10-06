/* ==================================================================
 * InverterPricingSignalType.java - 6/10/2026 9:15:26 am
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
 * Inverter pricing signal type, which defines the meaning of a pricing signal.
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum InverterPricingSignalType implements CodedValue {

	/** An unknown or other type of value. */
	Unknown(0, "Unknown"),

	/**
	 * An absolute price in the local rate, for example {@literal 23}
	 * (cents/kWh).
	 */
	Absolute(1, "Absolute price"),

	/**
	 * A relative price in the local rate, for example {@literal -5}
	 * (cents/kWh).
	 */
	Relative(2, "Relative price"),

	/**
	 * A price multiplier percentage, for example {@literal 15} for a 15% uplift
	 * in the rate.
	 */
	Multiplier(3, "Price multiplier"),

	/**
	 * A price level, for example from {@literal 0} for the lowest to
	 * {@literal 4} for the highest.
	 */
	Level(4, "Price level"),

	;

	private final int code;
	private final String description;

	private InverterPricingSignalType(int code, String description) {
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
