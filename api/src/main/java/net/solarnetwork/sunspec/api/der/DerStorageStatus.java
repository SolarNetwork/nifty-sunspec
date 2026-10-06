/* ==================================================================
 * DerStorageStatus.java - 5/10/2026 10:12:08 am
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
 * DER storage status.
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum DerStorageStatus implements CodedValue {

	/** No warnings or errors pending. */
	Ok(0, "No warnings or errors pending"),

	/** One or more warnings pending. */
	Warning(1, "One or more warnings pending"),

	/** One or more errors pending. */
	Error(2, "One or more errors pending"),

	;

	private final int code;
	private final String description;

	private DerStorageStatus(int code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public int getCode() {
		return code;
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
