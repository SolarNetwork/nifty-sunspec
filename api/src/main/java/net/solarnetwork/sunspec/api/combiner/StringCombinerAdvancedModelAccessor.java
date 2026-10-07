/* ==================================================================
 * StringCombinerAdvancedModelAccessor.java - 10/09/2019 3:40:07 pm
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
import org.jspecify.annotations.Nullable;

/**
 * Advanced string combiner API.
 *
 * @author matt
 * @version 1.0
 */
public interface StringCombinerAdvancedModelAccessor extends StringCombinerModelAccessor {

	/**
	 * API for an individual advanced DC input (repeating block).
	 */
	interface AdvancedDcInput extends DcInput {

		/**
		 * Get the DC voltage, in volts.
		 *
		 * @return the DC voltage
		 */
		@Nullable
		Float getDCVoltage();

		/**
		 * Get the DC power, in watts.
		 *
		 * @return the DC power
		 */
		@Nullable
		Integer getDCPower();

		/**
		 * Get the DC energy delivered (imported), in watt-hours.
		 *
		 * @return the delivered energy
		 */
		@Nullable
		Long getDCEnergy();

		/**
		 * Get the DC performance ratio, as a percentage 0-1.
		 *
		 * @return the DC performance ratio
		 */
		@Nullable
		Float getDCPerformanceRatio();

		/**
		 * Get the number of modules in this input string.
		 *
		 * @return the count of modules
		 */
		@Nullable
		Integer getModuleCount();

	}

	/**
	 * Get the DC power, in watts.
	 *
	 * @return the DC power
	 */
	@Nullable
	Integer getDCPower();

	/**
	 * Get the DC energy delivered (imported), in watt-hours.
	 *
	 * @return the delivered energy
	 */
	@Nullable
	Long getDCEnergy();

	/**
	 * Get the DC performance ratio, as a percentage 0-1.
	 *
	 * @return the DC performance ratio
	 */
	@Nullable
	Float getDCPerformanceRatio();

	/**
	 * Get the list of available DC inputs.
	 *
	 * @return the inputs, never {@code null}
	 */
	List<AdvancedDcInput> getAdvancedDcInputs();

}
