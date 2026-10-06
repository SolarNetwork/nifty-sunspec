/* ==================================================================
 * MeteorologicalModelAccessor.java - 10/07/2023 8:35:40 am
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
 * API for accessing meteorological model data.
 *
 * @author matt
 * @version 1.0
 * @since 4.2
 */
public interface MeteorologicalModelAccessor extends ModelAccessor {

	/**
	 * Get the ambient temperature.
	 *
	 * @return the temperature, in degrees Celsius
	 */
	@Nullable
	Float getAmbientTemperature();

	/**
	 * Get the relative humidity.
	 *
	 * @return the humidity, as an integer percentage
	 */
	@Nullable
	Integer getRelativeHumidity();

	/**
	 * Get the atmospheric pressure, in pascals.
	 *
	 * @return the atmospheric pressure, in pascals
	 */
	@Nullable
	Integer getAtmosphericPressure();

	/**
	 * Get the wind speed.
	 *
	 * @return the wind speed, in m/s
	 */
	@Nullable
	Integer getWindSpeed();

	/**
	 * Get the wind direction.
	 *
	 * @return the wind direction, in degrees
	 */
	@Nullable
	Integer getWindDirection();

	/**
	 * Get the rain accumulation, since the last reading.
	 *
	 * @return the rain accumulation, in mm
	 */
	@Nullable
	Integer getRainAccumulation();

	/**
	 * Get the snow accumulation, since the last reading.
	 *
	 * @return the snow accumulation, in mm
	 */
	@Nullable
	Integer getSnowAccumulation();

	/**
	 * Get the precipitation type.
	 *
	 * @return the precipitation type
	 */
	@Nullable
	PrecipitationType getPrecipitationType();

	/**
	 * Get the electric field.
	 *
	 * @return the electric field, in V/m
	 */
	@Nullable
	Integer getElectricField();

	/**
	 * Get the surface wetness.
	 *
	 * @return the surface wetness, in Ohm
	 */
	@Nullable
	Integer getSurfaceWetness();

	/**
	 * Get the soil moisture.
	 *
	 * @return the soil moisture, as an integer percentage
	 */
	@Nullable
	Integer getSoilMoisture();

}
