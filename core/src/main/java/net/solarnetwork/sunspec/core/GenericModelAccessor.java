/* ==================================================================
 * GenericModelAccessor.java - 8/10/2018 3:05:15 PM
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

package net.solarnetwork.sunspec.core;

import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Generic {@link ModelAccessor} that can be used when an unknown model is
 * encountered.
 * 
 * @author matt
 * @version 1.0
 */
public class GenericModelAccessor extends BaseModelAccessor implements ModelAccessor {

	/**
	 * Constructor.
	 * 
	 * @param data
	 *        the overall data object
	 * @param baseAddress
	 *        the base address for this model's data
	 * @param modelId
	 *        the model ID
	 */
	public GenericModelAccessor(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
	}

	@Override
	public int getFixedBlockLength() {
		return 0;
	}

}
