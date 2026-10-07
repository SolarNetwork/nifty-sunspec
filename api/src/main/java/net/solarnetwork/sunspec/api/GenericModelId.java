/* ==================================================================
 * GenericModelId.java - 8/10/2018 3:06:39 PM
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
 * Generic {@link ModelId} that can be used when an unknown model is
 * encountered.
 * 
 * @author matt
 * @version 1.0
 */
public class GenericModelId implements ModelId {

	private final int id;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *        the model ID
	 */
	public GenericModelId(int id) {
		super();
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getDescription() {
		return "Model " + id;
	}

	@Override
	public Class<? extends ModelAccessor> getModelAccessorType() {
		return ModelAccessor.class;
	}

	@Override
	public String toString() {
		return getDescription();
	}

}
