/* ==================================================================
 * OperatingState.java - 5/10/2018 4:34:08 PM
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

import net.solarnetwork.domain.DeviceOperatingState;

/**
 * API for an operational state.
 * 
 * @author matt
 * @version 1.1
 */
public interface OperatingState {

	/**
	 * Get the state code.
	 * 
	 * @return the code
	 */
	int getCode();

	/**
	 * Get a description of the state.
	 * 
	 * @return a description
	 */
	String getDescription();

	/**
	 * Get a {@link DeviceOperatingState} out of this state.
	 * 
	 * @return the device operating state, never {@code null}
	 * @since 1.1
	 */
	DeviceOperatingState asDeviceOperatingState();

}
