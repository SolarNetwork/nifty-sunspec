/* ==================================================================
 * DerStorageCapacityModelAccessor.java - 5/10/2026 10:18:41 am
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

import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;

/**
 * API for accessing SunSpec DER storage capacity model data.
 *
 * <p>
 * This API corresponds to the SunSpec model number <b>713</b>.
 * </p>
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public interface DerStorageCapacityModelAccessor extends ModelAccessor {

	/**
	 * Get the energy rating of the storage.
	 *
	 * @return the energy rating, in Wh, or {@code null} if not available
	 */
	@Nullable
	Long getEnergyRating();

	/**
	 * Get the energy available in the storage.
	 *
	 * <p>
	 * SunSpec defines this as the energy rating multiplied by the state of
	 * charge and the state of health.
	 * </p>
	 *
	 * @return the energy available, in Wh, or {@code null} if not available
	 */
	@Nullable
	Long getEnergyAvailable();

	/**
	 * Get the state of charge.
	 *
	 * <p>
	 * SunSpec defines the state of charge as {@literal 0} for a DER without
	 * storage capabilities.
	 * </p>
	 *
	 * @return the state of charge, as a percentage (0 - 100), or {@code null}
	 *         if not available
	 */
	@Nullable
	Float getStateOfCharge();

	/**
	 * Get the state of health.
	 *
	 * @return the state of health, as a percentage (0 - 100), or {@code null}
	 *         if not available
	 */
	@Nullable
	Float getStateOfHealth();

	/**
	 * Get the storage status.
	 *
	 * @return the status, or {@code null} if not available
	 */
	@Nullable
	DerStorageStatus getStorageStatus();

}
