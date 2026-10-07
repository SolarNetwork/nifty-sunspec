/* ==================================================================
 * ReferencePointModelAccessorImpl.java - 9/07/2023 4:36:56 pm
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

package net.solarnetwork.sunspec.core.environmental;

import java.util.Collection;
import java.util.EnumSet;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.api.environmental.ReferencePointModelAccessor;
import net.solarnetwork.sunspec.api.environmental.ReferencePointModelRegister;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link ReferencePointModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class ReferencePointModelAccessorImpl extends BaseModelAccessor
		implements ReferencePointModelAccessor {

	/** The model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 4;

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
	public ReferencePointModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@link EnvironmentalModelId} class will be used as the
	 * {@code ModelId} instance.
	 * </p>
	 *
	 * @param data
	 *        the overall data object
	 * @param baseAddress
	 *        the base address for this model's data
	 * @param modelId
	 *        the model ID
	 */
	public ReferencePointModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, EnvironmentalModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(ReferencePointModelRegister.class);
	}

	@Override
	public @Nullable Integer getGlobalHorizontalIrradiance() {
		return getIntegerValue(ReferencePointModelRegister.GHI);
	}

	@Override
	public @Nullable Integer getCurrent() {
		return getIntegerValue(ReferencePointModelRegister.Amps);
	}

	@Override
	public @Nullable Integer getVoltage() {
		return getIntegerValue(ReferencePointModelRegister.Voltage);
	}

	@Override
	public @Nullable Integer getTemperature() {
		return getIntegerValue(ReferencePointModelRegister.Temperature);
	}

}
