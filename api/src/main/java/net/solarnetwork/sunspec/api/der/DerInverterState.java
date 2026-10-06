/* ==================================================================
 * DerInverterState.java - 5/10/2026 8:27:22 am
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
import net.solarnetwork.sunspec.api.inverter.InverterOperatingState;

/**
 * DER inverter state.
 *
 * <p>
 * Note the codes of this state are different from the codes of the
 * {@link InverterOperatingState} used by the inverter models. Use
 * {@link #asInverterOperatingState()} to get the equivalent inverter operating
 * state.
 * </p>
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum DerInverterState implements CodedValue {

	/** Off. */
	Off(0, "Off"),

	/** Sleeping. */
	Sleeping(1, "Sleeping"),

	/** Starting. */
	Starting(2, "Starting"),

	/** Running. */
	Running(3, "Running"),

	/** Active power throttled. */
	Throttled(4, "Active power throttled"),

	/** Shutting down. */
	ShuttingDown(5, "Shutting down"),

	/** Fault. */
	Fault(6, "Fault"),

	/** Standby. */
	Standby(7, "Standby"),

	;

	private final int code;
	private final String description;

	private DerInverterState(int code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public int getCode() {
		return code;
	}

	/**
	 * Get a description of the state.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the equivalent inverter operating state.
	 *
	 * @return the inverter operating state, never {@code null}
	 */
	public InverterOperatingState asInverterOperatingState() {
		return switch (this) {
			case Off -> InverterOperatingState.Off;
			case Sleeping -> InverterOperatingState.Sleeping;
			case Starting -> InverterOperatingState.Starting;
			case Running -> InverterOperatingState.Normal;
			case Throttled -> InverterOperatingState.Throttled;
			case ShuttingDown -> InverterOperatingState.ShuttingDown;
			case Fault -> InverterOperatingState.Fault;
			default -> InverterOperatingState.Standby;
		};
	}

}
