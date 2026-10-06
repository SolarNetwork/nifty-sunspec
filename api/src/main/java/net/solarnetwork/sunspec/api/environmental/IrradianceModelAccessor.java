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

import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;

/**
 * API for accessing irradiance model data.
 *
 * @author matt
 * @version 1.0
 * @since 4.2
 */
public interface IrradianceModelAccessor extends ModelAccessor {

	/**
	 * Get the global horizontal irradiance, in W/m2.
	 *
	 * @return the irradiance
	 */
	@Nullable
	Integer getGlobalHorizontalIrradiance();

	/**
	 * Get the plane-of-array irradiance, in W/m2.
	 *
	 * @return the irradiance
	 */
	@Nullable
	Integer getPlaneOfArrayIrradiance();

	/**
	 * Get the diffuse irradiance, in W/m2.
	 *
	 * @return the irradiance
	 */
	@Nullable
	Integer getDiffuseIrradiance();

	/**
	 * Get the direct normal irradiance, in W/m2.
	 *
	 * @return the irradiance
	 */
	@Nullable
	Integer getDirectNormalIrradiance();

	/**
	 * Get some other irradiance, in W/m2.
	 *
	 * @return the irradiance
	 */
	@Nullable
	Integer getOtherIrradiance();
}
