/* ==================================================================
 * InclinometerModelAccessor.java - 8/07/2023 8:27:21 am
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
 * API for accessing inclinometer model data.
 *
 * @author matt
 * @version 1.0
 */
public interface InclinometerModelAccessor extends ModelAccessor {

	/**
	 * Get the inclination data.
	 *
	 * @return the list of inclination data, never {@code null}
	 */
	List<Incline> getInclines();

	/**
	 * Get the first available incline element.
	 *
	 * @return the first available incline, or {@code null}
	 */
	default @Nullable Incline getIncline() {
		List<Incline> temps = getInclines();
		return (temps != null && !temps.isEmpty() ? temps.get(0) : null);
	}

}
