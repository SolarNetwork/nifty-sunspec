/* ==================================================================
 * MeterModelId.java - 22/05/2018 6:21:42 AM
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

/**
 * API for a model identifier.
 * 
 * @author matt
 * @version 1.1
 */
public interface ModelId {

	/** The model ID for the "end" identifier. */
	int SUN_SPEC_END_ID = 0xFFFF;

	/**
	 * Get the model ID.
	 * 
	 * @return the model ID
	 */
	int getId();

	/**
	 * Get a description of the event.
	 * 
	 * @return a description
	 */
	String getDescription();

	/**
	 * Get a model accessor type suitable for this model.
	 * 
	 * @return the model accessor type
	 */
	Class<? extends ModelAccessor> getModelAccessorType();

}
