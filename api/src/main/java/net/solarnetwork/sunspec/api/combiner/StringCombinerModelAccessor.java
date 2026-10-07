/* ==================================================================
 * StringCombinerModelAccessor.java - 10/09/2019 6:59:43 am
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

package net.solarnetwork.sunspec.api.combiner;

import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;

/**
 * API for accessing string combiner model data.
 *
 * @author matt
 * @version 1.0
 */
public interface StringCombinerModelAccessor extends ModelAccessor {

	/**
	 * API for an individual DC input (repeating block).
	 */
	interface DcInput {

		/**
		 * Get the module ID.
		 *
		 * @return the module ID
		 */
		@Nullable
		Integer getInputId();

		/**
		 * Get the DC current, in amps.
		 *
		 * @return the DC current
		 */
		@Nullable
		Float getDCCurrent();

		/**
		 * Get the DC charge delivered (imported), in amp-hours.
		 *
		 * @return the delivered charge
		 */
		@Nullable
		Long getDCChargeDelivered();

		/**
		 * Get the active events for the module.
		 *
		 * @return the events, never {@code null}
		 * @see StringCombinerModelEvent
		 */
		Set<? extends ModelEvent> getEvents();

		/**
		 * Get the active vendor events.
		 *
		 * @return the vendor events, never {@code null}
		 */
		Set<? extends ModelEvent> getVendorEvents();

	}

	/**
	 * Get the DC current, in amps.
	 *
	 * @return the DC current
	 */
	@Nullable
	Float getDCCurrent();

	/**
	 * Get the DC charge delivered (imported), in amp-hours.
	 *
	 * @return the delivered charge
	 */
	@Nullable
	Long getDCChargeDelivered();

	/**
	 * Get the DC voltage, in volts.
	 *
	 * @return the DC voltage
	 */
	@Nullable
	Float getDCVoltage();

	/**
	 * Get the temperature of the combiner, in degrees Celsius.
	 *
	 * @return the temperature
	 */
	@Nullable
	Float getTemperature();

	/**
	 * Get the active events.
	 *
	 * @return the events, never {@code null}
	 * @see StringCombinerModelEvent
	 */
	Set<? extends ModelEvent> getEvents();

	/**
	 * Get the active vendor events.
	 *
	 * @return the vendor events, never {@code null}
	 */
	Set<? extends ModelEvent> getVendorEvents();

	/**
	 * Get the list of available DC inputs.
	 *
	 * @return the inputs, never {@code null}
	 */
	List<DcInput> getDcInputs();

}
