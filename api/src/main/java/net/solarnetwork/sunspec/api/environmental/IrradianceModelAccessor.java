/* ==================================================================
 * IrradianceModelAccessor.java - 5/07/2023 8:15:41 am
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
 * API for accessing irradiance model data.
 *
 * @author matt
 * @version 1.0
 */
public interface IrradianceModelAccessor extends ModelAccessor {

	/**
	 * API for a single set of irradiance measurements.
	 */
	interface Irradiance {

		/**
		 * Get the global horizontal irradiance.
		 *
		 * @return the irradiance, in W/m2, or {@code null} if not available
		 */
		@Nullable
		Integer getGlobalHorizontalIrradiance();

		/**
		 * Get the plane-of-array irradiance.
		 *
		 * @return the irradiance, in W/m2, or {@code null} if not available
		 */
		@Nullable
		Integer getPlaneOfArrayIrradiance();

		/**
		 * Get the diffuse irradiance.
		 *
		 * @return the irradiance, in W/m2, or {@code null} if not available
		 */
		@Nullable
		Integer getDiffuseIrradiance();

		/**
		 * Get the direct normal irradiance.
		 *
		 * @return the irradiance, in W/m2, or {@code null} if not available
		 */
		@Nullable
		Integer getDirectNormalIrradiance();

		/**
		 * Get some other irradiance.
		 *
		 * @return the irradiance, in W/m2, or {@code null} if not available
		 */
		@Nullable
		Integer getOtherIrradiance();

	}

	/**
	 * Get the irradiance measurements.
	 *
	 * <p>
	 * The model holds one set of measurements in each repeating block instance,
	 * so the number of sets is determined by the model length.
	 * </p>
	 *
	 * @return the irradiance measurements, never {@code null}
	 */
	List<Irradiance> getIrradiances();

	/**
	 * Get the first available irradiance measurements.
	 *
	 * @return the first available irradiance measurements, or {@code null} if
	 *         there are none
	 */
	default @Nullable Irradiance getIrradiance() {
		List<Irradiance> irradiances = getIrradiances();
		return (!irradiances.isEmpty() ? irradiances.get(0) : null);
	}

}
