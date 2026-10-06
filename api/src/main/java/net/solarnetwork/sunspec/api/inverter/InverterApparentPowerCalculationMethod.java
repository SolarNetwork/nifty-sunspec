/* ==================================================================
 * InverterApparentPowerCalculationMethod.java - 15/10/2018 2:18:04 PM
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
import net.solarnetwork.domain.CodedValue;
import net.solarnetwork.sunspec.modbus.ModbusConstants;

/**
 * Apparent power calculation method for inverters.
 *
 * @author matt
 * @version 1.1
 * @since 1.2
 */
public enum InverterApparentPowerCalculationMethod
		implements
		ApparentPowerCalculationMethod,
		CodedValue {

	/** Vector method. */
	Vector(1, "Vector"),

	/** Arithmetic method. */
	Arithmetic(2, "Arithmetic");

	private final int code;
	private final String description;

	private InverterApparentPowerCalculationMethod(int index, String description) {
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
	 * Get an enumeration for a code value.
	 *
	 * @param code
	 *        the code to get the enum value for
	 * @return the enumeration value
	 * @throws IllegalArgumentException
	 *         if {@code code} is not supported
	 */
	public static @Nullable InverterApparentPowerCalculationMethod forCode(int code) {
		if ( (code & ModbusConstants.NAN_ENUM16) == ModbusConstants.NAN_ENUM16 ) {
			return null;
		}
		for ( InverterApparentPowerCalculationMethod e : InverterApparentPowerCalculationMethod
				.values() ) {
			if ( e.code == code ) {
				return e;
			}
		}
		throw new IllegalArgumentException("Code [" + code + "] not supported");
	}

}
