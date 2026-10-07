/* ==================================================================
 * DistributedEnergyResourceType.java - 15/10/2018 9:37:22 AM
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

package net.solarnetwork.sunspec.api.inverter;

/**
 * API for a DER type.
 * 
 * @author matt
 * @version 1.0
 */
public interface DistributedEnergyResourceType {

	/**
	 * Get the DER type code.
	 * 
	 * @return the code
	 */
	int getCode();

	/**
	 * Get a description of the DER type.
	 * 
	 * @return a description
	 */
	String getDescription();

}
