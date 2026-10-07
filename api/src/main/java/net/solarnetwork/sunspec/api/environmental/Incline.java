/* ==================================================================
 * Incline.java - 8/07/2023 8:32:43 am
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
 * Incline data structure.
 *
 * @author matt
 * @version 1.0
 */
public class Incline {

	private final @Nullable Float x;
	private final @Nullable Float y;
	private final @Nullable Float z;

	/**
	 * Constructor.
	 *
	 * @param x
	 *        the x-axis inclination, in degrees
	 * @param y
	 *        the y-axis inclination, in degrees
	 * @param z
	 *        the z-axis inclination, in degrees
	 */
	public Incline(@Nullable Float x, @Nullable Float y, @Nullable Float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Constructor.
	 *
	 * @param data
	 *        an array of raw inclination data, in x, y, z order; the array
	 *        values are copied and adjusted to scale
	 */
	public Incline(@Nullable Integer @Nullable [] data) {
		super();
		this.x = (data != null && data.length > 0 && data[0] != null ? (data[0].floatValue() / 100f)
				: null);
		this.y = (data != null && data.length > 1 && data[1] != null ? (data[1].floatValue() / 100f)
				: null);
		this.z = (data != null && data.length > 2 && data[2] != null ? (data[2].floatValue() / 100f)
				: null);
	}

	/**
	 * Get the x-axis inclination, in degrees.
	 *
	 * @return the inclination
	 */
	public @Nullable Float getInclineX() {
		return x;
	}

	/**
	 * Get the y-axis inclination, in degrees.
	 *
	 * @return the inclination
	 */
	public @Nullable Float getInclineY() {
		return y;
	}

	/**
	 * Get the z-axis inclination, in degrees.
	 *
	 * @return the inclination
	 */
	public @Nullable Float getInclineZ() {
		return z;
	}

}
