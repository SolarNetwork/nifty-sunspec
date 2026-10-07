/* ==================================================================
 * DerDcPortType.java - 5/10/2026 10:12:08 am
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
 * DER DC port type.
 *
 * @author matt
 * @version 1.0
 */
public enum DerDcPortType implements CodedValue {

	/** Photovoltaic. */
	Photovoltaic(0, "Photovoltaic"),

	/** Energy storage system. */
	EnergyStorageSystem(1, "Energy storage system"),

	/** Electric vehicle. */
	ElectricVehicle(2, "Electric vehicle"),

	/** Generic injecting. */
	GenericInjecting(3, "Generic injecting"),

	/** Generic absorbing. */
	GenericAbsorbing(4, "Generic absorbing"),

	/** Generic bidirectional. */
	GenericBidirectional(5, "Generic bidirectional"),

	/** DC to DC. */
	DcToDc(6, "DC to DC"),

	;

	private final int code;
	private final String description;

	private DerDcPortType(int code, String description) {
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
