/* ==================================================================
 * InverterReactivePowerAction.java - 15/10/2018 2:13:22 PM
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
import net.solarnetwork.sunspec.api.ReactivePowerAction;
import net.solarnetwork.sunspec.modbus.ModbusConstants;

/**
 * Reactive power action for inverters.
 *
 * @author matt
 * @version 1.1
 * @since 1.2
 */
public enum InverterReactivePowerAction implements ReactivePowerAction, CodedValue {

	/** Switch VAR characterization. */
	Switch(1, "Switch VAR characterization"),

	/** Maintain VAR characterization. */
	Maintain(2, "Maintain VAR characterization");

	private final int code;
	private final String description;

	private InverterReactivePowerAction(int index, String description) {
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
	public static @Nullable InverterReactivePowerAction forCode(int code) {
		if ( (code & ModbusConstants.NAN_ENUM16) == ModbusConstants.NAN_ENUM16 ) {
			return null;
		}
		for ( InverterReactivePowerAction e : InverterReactivePowerAction.values() ) {
			if ( e.code == code ) {
				return e;
			}
		}
		throw new IllegalArgumentException("Code [" + code + "] not supported");
	}

}
