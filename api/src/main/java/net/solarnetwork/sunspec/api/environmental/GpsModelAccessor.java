/* ==================================================================
 * GpsModelAccessor.java - 8/07/2023 9:35:59 am
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

import java.math.BigDecimal;
import java.time.Instant;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;

/**
 * API for accessing GPS model data.
 *
 * @author matt
 * @version 1.0
 */
public interface GpsModelAccessor extends ModelAccessor {

	/**
	 * Get the GPS timestamp.
	 *
	 * @return the timestamp
	 */
	@Nullable
	Instant getGpsTimestamp();

	/**
	 * Get a location name.
	 *
	 * @return the location name
	 */
	@Nullable
	String getLocationName();

	/**
	 * Get the latitude, in decimal degrees.
	 *
	 * @return the latitude
	 */
	@Nullable
	BigDecimal getLatitude();

	/**
	 * Get the longitude, in decimal degrees.
	 *
	 * @return the longitude
	 */
	@Nullable
	BigDecimal getLongitude();

	/**
	 * Get the altitude, in meters.
	 *
	 * @return the altitude
	 */
	@Nullable
	Integer getAltitude();

}
