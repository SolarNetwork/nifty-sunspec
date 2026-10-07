/* ==================================================================
 * DerTripLowFrequencyModelAccessorImpl.java - 5/10/2026 6:24:51 pm
 *
 * Copyright 2026 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.core.der;

import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerTripLowFrequencyModelAccessor;
import net.solarnetwork.sunspec.api.der.DerTripModelRegister;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link DerTripLowFrequencyModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class DerTripLowFrequencyModelAccessorImpl extends BaseDerTripModelAccessor
		implements DerTripLowFrequencyModelAccessor {

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
	public DerTripLowFrequencyModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId, DerTripModelRegister.PointFrequency,
				DerTripModelRegister.ScaleFactorFrequency, DerTripModelRegister.PointFrequencyTime);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@link DerModelId} class will be used as the {@code ModelId}
	 * instance.
	 * </p>
	 *
	 * @param data
	 *        the overall data object
	 * @param baseAddress
	 *        the base address for this model's data
	 * @param modelId
	 *        the model ID
	 */
	public DerTripLowFrequencyModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, DerModelId.forId(modelId));
	}

}
