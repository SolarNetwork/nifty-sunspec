/* ==================================================================
 * BomTemperatureModelAccessor.java - 5/07/2023 10:26:12 am
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

import java.util.List;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;

/**
 * API for accessing back-of-module temperature data.
 *
 * @author matt
 * @version 1.0
 * @since 4.2
 */
public interface BomTemperatureModelAccessor extends ModelAccessor {

	/**
	 * Get the list of available back-of-module temperatures.
	 *
	 * @return the temperatures, each in degrees Celsius, never {@code null}
	 */
	List<Float> getBackOfModuleTemperatures();

	/**
	 * Get the first available back-of-module temperature.
	 *
	 * @return the first available temperature, or {@code null}
	 */
	default @Nullable Float getBackOfModuleTemperature() {
		List<Float> temps = getBackOfModuleTemperatures();
		return (temps != null && !temps.isEmpty() ? temps.get(0) : null);
	}

}
