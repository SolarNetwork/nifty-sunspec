/* ==================================================================
 * DerNormalOperatingCategory.java - 5/10/2026 9:32:29 am
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
 * DER normal operating performance category, as specified in IEEE 1547-2018.
 *
 * @author matt
 * @version 1.0
 */
public enum DerNormalOperatingCategory implements CodedValue {

	/** Category A. */
	CategoryA(0, "Category A"),

	/** Category B. */
	CategoryB(1, "Category B"),

	;

	private final int code;
	private final String description;

	private DerNormalOperatingCategory(int code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public int getCode() {
		return code;
	}

	/**
	 * Get a description of the category.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
