/* ==================================================================
 * InverterDerType.java - 15/10/2018 9:34:56 AM
 *
 * Copyright 2018 SolarNetwork.net Dev Team
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

import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.CodedValue;
import net.solarnetwork.sunspec.modbus.ModbusConstants;

/**
 * Enumeration of inverter DER types.
 *
 * @author matt
 * @version 1.0
 */
public enum InverterDerType implements DistributedEnergyResourceType, CodedValue {

	/** Photovoltaic generation. */
	PV(4, "Photovoltaic generation"),

	/** Photovoltaic generation with battery storage. */
	PVAndStorage(82, "Photovoltaic generation with battery storage");

	private final int code;
	private final String description;

	private InverterDerType(int index, String description) {
		this.code = index;
		this.description = description;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Get an enumeration for an index value.
	 *
	 * @param code
	 *        the code to get the enum value for
	 * @return the enumeration value, or {@code null} if {@code code} is
	 *         {@literal NaN}
	 * @throws IllegalArgumentException
	 *         if {@code code} is not supported
	 */
	public static @Nullable InverterDerType forCode(int code) {
		if ( (code & ModbusConstants.NAN_ENUM16) == ModbusConstants.NAN_ENUM16 ) {
			return null;
		}
		for ( InverterDerType e : InverterDerType.values() ) {
			if ( e.code == code ) {
				return e;
			}
		}
		throw new IllegalArgumentException("Code [" + code + "] not supported");
	}

}
