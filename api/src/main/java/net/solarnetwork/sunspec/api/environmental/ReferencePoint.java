/* ==================================================================
 * ReferencePoint.java - 9/07/2023 4:30:30 pm
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

/**
 * Reference point data.
 *
 * @author matt
 * @version 1.0
 * @since 4.2
 */
public class ReferencePoint {

	private final @Nullable Integer irradiance;
	private final @Nullable Float current;
	private final @Nullable Float voltage;
	private final @Nullable Float temperature;

	/**
	 * Constructor.
	 *
	 * @param irradiance
	 *        the irradiance, in W/m2
	 * @param current
	 *        the current, in amps
	 * @param voltage
	 *        the voltage, in volts
	 * @param temperature
	 *        the temperature, in degrees Celsius
	 */
	public ReferencePoint(@Nullable Integer irradiance, @Nullable Float current, @Nullable Float voltage,
			@Nullable Float temperature) {
		super();
		this.irradiance = irradiance;
		this.current = current;
		this.voltage = voltage;
		this.temperature = temperature;
	}

	/**
	 * Constructor.
	 *
	 * @param data
	 *        an array of raw reference point data, in irradiance, current,
	 *        voltage, temperature order; the array values are copied and
	 *        adjusted to scale
	 */
	public ReferencePoint(@Nullable Integer @Nullable [] data) {
		super();
		this.irradiance = (data != null && data.length > 0 ? data[0] : null);
		this.current = (data != null && data.length > 1 && data[1] != null
				? (data[1].floatValue() / 100f)
				: null);
		this.voltage = (data != null && data.length > 2 && data[2] != null
				? (data[2].floatValue() / 100f)
				: null);
		this.temperature = (data != null && data.length > 3 && data[3] != null
				? (data[3].floatValue() / 10f)
				: null);
	}

	/**
	 * Get the irradiance.
	 *
	 * @return the irradiance, in W/m2
	 */
	public @Nullable Integer getIrradiance() {
		return irradiance;
	}

	/**
	 * Get the current.
	 *
	 * @return the current, in amps
	 */
	public @Nullable Float getCurrent() {
		return current;
	}

	/**
	 * Get the voltage.
	 *
	 * @return the voltage, in volts
	 */
	public @Nullable Float getVoltage() {
		return voltage;
	}

	/**
	 * Get the temperature, in degrees Celsius
	 *
	 * @return the temperature
	 */
	public @Nullable Float getTemperature() {
		return temperature;
	}

}
