/* ==================================================================
 * OperatingState.java - 5/10/2018 4:32:22 PM
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

import net.solarnetwork.domain.DeviceOperatingState;
import net.solarnetwork.sunspec.api.OperatingState;
import net.solarnetwork.sunspec.modbus.ModbusConstants;

/**
 * Operating state for inverters.
 * 
 * @author matt
 * @version 1.3
 */
public enum InverterOperatingState implements OperatingState {

	/** Device operating normally. */
	Normal(0, "Device operating normally"),

	/** Device is not operating. */
	Off(1, "Device is not operating"),

	/** Device is sleeping / auto-shudown. */
	Sleeping(2, "Device is sleeping / auto-shudown"),

	/** Device is starting up. */
	Starting(3, "Device is starting up"),

	/** Device is auto tracking maximum power point. */
	Mppt(4, "Device is auto tracking maximum power point"),

	/** Device is operating at reduced power output. */
	Throttled(5, "Device is operating at reduced power output"),

	/** Device is shutting down. */
	ShuttingDown(6, "Device is shutting down"),

	/** One or more faults exist. */
	Fault(7, "One or more faults exist"),

	/** Device is in standby mode. */
	Standby(8, "Device is in standby mode"),

	/** Device is in test mode. */
	Test(9, "Device is in test mode");

	private final int code;
	private final String description;

	private InverterOperatingState(int index, String description) {
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
	 * Get a {@link DeviceOperatingState}.
	 * 
	 * @return the device operating state, never {@code null}
	 * @since 1.2
	 */
	@Override
	public DeviceOperatingState asDeviceOperatingState() {
		return switch (this) {
			case Normal, Mppt -> DeviceOperatingState.Normal;
			case Off, ShuttingDown -> DeviceOperatingState.Shutdown;
			case Sleeping, Standby -> DeviceOperatingState.Standby;
			case Starting, Test -> DeviceOperatingState.Starting;
			case Throttled -> DeviceOperatingState.Override;
			case Fault -> DeviceOperatingState.Fault;
			default -> DeviceOperatingState.Unknown;
		};
	}

	/**
	 * Get an enumeration for an index value.
	 * 
	 * @param code
	 *        the code to get the enum value for
	 * @return the enumeration value
	 * @throws IllegalArgumentException
	 *         if {@code code} is not supported
	 */
	public static InverterOperatingState forCode(int code) {
		if ( (code & ModbusConstants.NAN_ENUM16) == ModbusConstants.NAN_ENUM16 ) {
			return Normal;
		}
		for ( InverterOperatingState e : InverterOperatingState.values() ) {
			if ( e.code == code ) {
				return e;
			}
		}
		throw new IllegalArgumentException("Code [" + code + "] not supported");
	}

}
