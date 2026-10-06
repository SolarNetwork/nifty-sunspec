/* ==================================================================
 * CommonModelAccessor.java - 22/05/2018 9:13:28 AM
 *
 * Copyright 2018 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.api;

import org.jspecify.annotations.Nullable;

/**
 * API for accessing common model data.
 *
 * @author matt
 * @version 1.1
 */
public interface CommonModelAccessor extends ModelAccessor {

	@Override
	default int getFixedBlockLength() {
		return getModelLength();
	}

	/**
	 * Get the device manufacturer.
	 *
	 * @return the manufacturer
	 */
	@Nullable
	String getManufacturer();

	/**
	 * Get the device model name.
	 *
	 * @return the device model
	 */
	@Nullable
	String getModelName();

	/**
	 * Get the device options.
	 *
	 * @return the options
	 */
	@Nullable
	String getOptions();

	/**
	 * Get the device version.
	 *
	 * @return the version
	 */
	@Nullable
	String getVersion();

	/**
	 * Get the serial number.
	 *
	 * @return the serial number
	 */
	@Nullable
	String getSerialNumber();

	/**
	 * Get the device ID (the Modbus unit ID).
	 *
	 * @return the device address
	 */
	@Nullable
	Integer getDeviceAddress();

}
