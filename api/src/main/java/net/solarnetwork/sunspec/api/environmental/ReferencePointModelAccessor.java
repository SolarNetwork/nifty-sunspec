/* ==================================================================
 * ReferencePointModelAccessor.java - 9/07/2023 4:28:44 pm
 *
 * Copyright 2023 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.api.environmental;

import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;

/**
 * API for accessing reference point model data.
 *
 * <p>
 * SunSpec defines every reference point value as an unsigned 16-bit integer
 * without a scale factor.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public interface ReferencePointModelAccessor extends ModelAccessor {

	/**
	 * Get the global horizontal irradiance.
	 *
	 * @return the irradiance, in W/m2
	 */
	@Nullable
	Integer getGlobalHorizontalIrradiance();

	/**
	 * Get the current measurement at the reference point.
	 *
	 * @return the current, in amps
	 */
	@Nullable
	Integer getCurrent();

	/**
	 * Get the voltage measurement at the reference point.
	 *
	 * @return the voltage, in volts
	 */
	@Nullable
	Integer getVoltage();

	/**
	 * Get the temperature measurement at the reference point.
	 *
	 * @return the temperature, in degrees Celsius
	 */
	@Nullable
	Integer getTemperature();

}
