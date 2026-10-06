/* ==================================================================
 * DerRampRateReference.java - 5/10/2026 2:41:17 pm
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

import net.solarnetwork.domain.CodedValue;

/**
 * DER ramp rate reference.
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum DerRampRateReference implements CodedValue {

	/** Percentage of maximum current per second. */
	MaximumCurrent(0, "Percentage of maximum current per second"),

	/** Percentage of maximum active power per second. */
	MaximumActivePower(1, "Percentage of maximum active power per second"),

	;

	private final int code;
	private final String description;

	private DerRampRateReference(int code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public int getCode() {
		return code;
	}

	/**
	 * Get a description of the reference.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
