/* ==================================================================
 * DerAcWiringType.java - 5/10/2026 8:27:22 am
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
 * DER AC wiring type.
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum DerAcWiringType implements CodedValue {

	/** Single phase. */
	SinglePhase(0, "Single phase"),

	/** Split phase. */
	SplitPhase(1, "Split phase"),

	/** Three phase. */
	ThreePhase(2, "Three phase"),

	;

	private final int code;
	private final String description;

	private DerAcWiringType(int code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public int getCode() {
		return code;
	}

	/**
	 * Get a description of the wiring type.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
