/* ==================================================================
 * IrradianceModelAccessorImpl.java - 5/07/2023 8:22:12 am
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
import net.solarnetwork.sunspec.api.environmental.IrradianceModelAccessor;
import net.solarnetwork.sunspec.api.environmental.IrradianceModelRegister;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link IrradianceModelAccessor}.
 *
 * @author matt
 * @version 1.1
 * @since 4.2
 */
public class IrradianceModelAccessorImpl extends BaseModelAccessor implements IrradianceModelAccessor {

	/** The irradiance model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 5;

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
	public IrradianceModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
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
	public IrradianceModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, EnvironmentalModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(IrradianceModelRegister.class);
	}

	@Override
	public @Nullable Integer getGlobalHorizontalIrradiance() {
		return getIntegerValue(IrradianceModelRegister.GHI);
	}

	@Override
	public @Nullable Integer getPlaneOfArrayIrradiance() {
		return getIntegerValue(IrradianceModelRegister.POAI);
	}

	@Override
	public @Nullable Integer getDiffuseIrradiance() {
		return getIntegerValue(IrradianceModelRegister.DFI);
	}

	@Override
	public @Nullable Integer getDirectNormalIrradiance() {
		return getIntegerValue(IrradianceModelRegister.DNI);
	}

	@Override
	public @Nullable Integer getOtherIrradiance() {
		return getIntegerValue(IrradianceModelRegister.OTI);
	}

}
