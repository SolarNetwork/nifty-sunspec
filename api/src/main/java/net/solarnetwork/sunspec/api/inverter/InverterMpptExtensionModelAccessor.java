/* ==================================================================
 * InverterMpptExtensionModelAccessor.java - 6/09/2019 5:25:06 pm
 *
 * Copyright 2019 SolarNetwork.net Dev Team
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

import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.OperatingState;

/**
 * API for accessing inverter MPPT extension model data.
 *
 * @author matt
 * @version 1.1
 * @since 1.4
 */
public interface InverterMpptExtensionModelAccessor extends ModelAccessor {

	/**
	 * API for an individual DC module (repeating block).
	 */
	interface DcModule {

		/**
		 * Get the module ID.
		 *
		 * @return the module ID
		 */
		@Nullable
		Integer getInputId();

		/**
		 * Get the module name.
		 *
		 * @return the module name
		 */
		@Nullable
		String getInputName();

		/**
		 * Get the DC current, in amps.
		 *
		 * @return the DC current
		 */
		@Nullable
		Float getDCCurrent();

		/**
		 * Get the DC voltage, in volts.
		 *
		 * @return the DC voltage
		 */
		@Nullable
		Float getDCVoltage();

		/**
		 * Get the DC power, in W.
		 *
		 * @return the DC power
		 */
		@Nullable
		Integer getDCPower();

		/**
		 * Get the DC energy delivered (imported), in Wh.
		 *
		 * @return the delivered active energy
		 */
		@Nullable
		Long getDCEnergyDelivered();

		/**
		 * Gets the time stamp of the data, in seconds since the epoch.
		 *
		 * @return the data time stamp
		 */
		@Nullable
		Long getDataTimestamp();

		/**
		 * Get the temperature of the module, in degrees Celsius.
		 *
		 * @return the temperature
		 */
		@Nullable
		Float getTemperature();

		/**
		 * Get the module operating state.
		 *
		 * @return the operating state
		 */
		@Nullable
		OperatingState getOperatingState();

		/**
		 * Get the active events for the module.
		 *
		 * @return the events, never {@code null}
		 */
		Set<? extends ModelEvent> getEvents();

	}

	/**
	 * Get the list of available DC modules.
	 *
	 * @return the modules, never {@code null}
	 */
	List<DcModule> getDcModules();

	/**
	 * Get the active events.
	 *
	 * @return the events, never {@code null}
	 */
	Set<? extends ModelEvent> getEvents();

	/**
	 * Get the timestamp period.
	 *
	 * @return the period
	 */
	@Nullable
	Integer getTimestampPeriod();

}
