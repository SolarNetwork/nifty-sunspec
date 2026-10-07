/* ==================================================================
 * DerDcMeasurementModelAccessor.java - 5/10/2026 10:24:03 am
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;

/**
 * API for accessing SunSpec DER DC measurement model data.
 *
 * <p>
 * This API corresponds to the SunSpec model number <b>714</b>.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public interface DerDcMeasurementModelAccessor extends ModelAccessor {

	/**
	 * API for a single DC port.
	 */
	interface DcPort {

		/**
		 * Get the port type.
		 *
		 * @return the type, or {@code null} if not available
		 */
		@Nullable
		DerDcPortType getPortType();

		/**
		 * Get the port ID.
		 *
		 * @return the ID, or {@code null} if not available
		 */
		@Nullable
		Integer getPortId();

		/**
		 * Get the port name.
		 *
		 * @return the name, or {@code null} if not available
		 */
		@Nullable
		String getPortName();

		/**
		 * Get the DC current.
		 *
		 * @return the current, in A, or {@code null} if not available
		 */
		@Nullable
		Float getDCCurrent();

		/**
		 * Get the DC voltage.
		 *
		 * @return the voltage, in V, or {@code null} if not available
		 */
		@Nullable
		Float getDCVoltage();

		/**
		 * Get the DC power.
		 *
		 * @return the power, in W, or {@code null} if not available
		 */
		@Nullable
		BigDecimal getDCPower();

		/**
		 * Get the total DC energy injected.
		 *
		 * @return the energy, in Wh, or {@code null} if not available
		 */
		@Nullable
		BigDecimal getDCEnergyInjected();

		/**
		 * Get the total DC energy absorbed.
		 *
		 * @return the energy, in Wh, or {@code null} if not available
		 */
		@Nullable
		BigDecimal getDCEnergyAbsorbed();

		/**
		 * Get the port temperature.
		 *
		 * @return the temperature, in degrees Celsius, or {@code null} if not
		 *         available
		 */
		@Nullable
		Float getTemperature();

		/**
		 * Get the port status.
		 *
		 * @return the status, or {@code null} if not available
		 */
		@Nullable
		DerDcPortStatus getPortStatus();

		/**
		 * Get the active port alarms.
		 *
		 * @return the alarms, as {@link DerDcPortAlarm} values, never
		 *         {@code null}
		 */
		Set<? extends ModelEvent> getEvents();

	}

	/**
	 * Get the indexes of the ports with active alarms.
	 *
	 * @return the port indexes, in the order returned by {@link #getDcPorts()}
	 *         starting from {@literal 0}, never {@code null}
	 */
	Set<Integer> getAlarmedPortIndexes();

	/**
	 * Get the number of DC ports.
	 *
	 * @return the port count, or {@code null} if not available
	 */
	@Nullable
	Integer getPortCount();

	/**
	 * Get the total DC current for all ports.
	 *
	 * @return the current, in A, or {@code null} if not available
	 */
	@Nullable
	Float getDCCurrent();

	/**
	 * Get the total DC power for all ports.
	 *
	 * @return the power, in W, or {@code null} if not available
	 */
	@Nullable
	BigDecimal getDCPower();

	/**
	 * Get the total DC energy injected for all ports.
	 *
	 * @return the energy, in Wh, or {@code null} if not available
	 */
	@Nullable
	BigDecimal getDCEnergyInjected();

	/**
	 * Get the total DC energy absorbed for all ports.
	 *
	 * @return the energy, in Wh, or {@code null} if not available
	 */
	@Nullable
	BigDecimal getDCEnergyAbsorbed();

	/**
	 * Get the DC ports.
	 *
	 * <p>
	 * The number of ports is the port count, limited to the number of port
	 * blocks the model length allows for. If the port count is not available,
	 * the model length alone determines the number of ports.
	 * </p>
	 *
	 * @return the ports, never {@code null}
	 */
	List<DcPort> getDcPorts();

}
