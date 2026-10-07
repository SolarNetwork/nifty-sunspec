/* ==================================================================
 * DerIntentionalIslandCategory.java - 5/10/2026 9:32:29 am
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

import net.solarnetwork.sunspec.api.Bitmaskable;

/**
 * DER intentional island categories.
 *
 * @author matt
 * @version 1.0
 */
public enum DerIntentionalIslandCategory implements Bitmaskable {

	/** Uncategorized. */
	Uncategorized(0, "Uncategorized"),

	/** Intentional island capable. */
	IntentionalIslandCapable(1, "Intentional island capable"),

	/** Black start capable. */
	BlackStartCapable(2, "Black start capable"),

	/** Isochronous capable. */
	IsochronousCapable(3, "Isochronous capable"),

	;

	private final int offset;
	private final String description;

	private DerIntentionalIslandCategory(int offset, String description) {
		this.offset = offset;
		this.description = description;
	}

	@Override
	public int bitmaskBitOffset() {
		return offset;
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
