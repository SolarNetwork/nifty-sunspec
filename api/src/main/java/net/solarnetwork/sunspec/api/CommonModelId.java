/* ==================================================================
 * CommonModelId.java - 22/05/2018 11:26:25 AM
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
 * {@link ModelId} for the SunSpec common model standard.
 * 
 * @author matt
 * @version 1.1
 */
public enum CommonModelId implements ModelId {

	/** The "common" model information. */
	CommonModel(1, "Common model");

	private final int id;
	private final String description;

	private CommonModelId(int id, String description) {
		this.id = id;
		this.description = description;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Class<? extends ModelAccessor> getModelAccessorType() {
		return CommonModelAccessor.class;
	}

	/**
	 * Get an enumeration for an ID value.
	 * 
	 * @param id
	 *        the ID to get the enum value for
	 * @return the enumeration value
	 * @throws IllegalArgumentException
	 *         if {@code id} is not supported
	 */
	public static CommonModelId forId(int id) {
		if ( id == 1 ) {
			return CommonModel;
		}
		throw new IllegalArgumentException("ID [" + id + "] not supported");
	}

}
