/* ==================================================================
 * MiniMeteorologicalModelAccessorImpl.java - 10/07/2023 7:16:18 am
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
import net.solarnetwork.sunspec.api.environmental.MiniMeteorologicalModelAccessor;
import net.solarnetwork.sunspec.api.environmental.MiniMeteorologicalModelRegister;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link MiniMeteorologicalModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class MiniMeteorologicalModelAccessorImpl extends BaseModelAccessor
		implements MiniMeteorologicalModelAccessor {

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
	public MiniMeteorologicalModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
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
	public MiniMeteorologicalModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, EnvironmentalModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(MiniMeteorologicalModelRegister.class);
	}

	@Override
	public @Nullable Integer getGlobalHorizontalIrradiance() {
		return getIntegerValue(MiniMeteorologicalModelRegister.IrradianceGH);
	}

	@Override
	public @Nullable Float getBackOfModuleTemperature() {
		Number n = getValue(MiniMeteorologicalModelRegister.TemperatureBOM);
		return (n != null ? n.floatValue() / 10f : null);
	}

	@Override
	public @Nullable Float getAmbientTemperature() {
		Number n = getValue(MiniMeteorologicalModelRegister.TemperatureAmbient);
		return (n != null ? n.floatValue() / 10f : null);
	}

	@Override
	public @Nullable Integer getWindSpeed() {
		return getIntegerValue(MiniMeteorologicalModelRegister.WindSpeed);
	}

	@Override
	public @Nullable Object getPointValue(ModbusReference point) {
		if ( !(point instanceof MiniMeteorologicalModelRegister r) ) {
			return null;
		}
		return switch (r) {
			case IrradianceGH -> getGlobalHorizontalIrradiance();
			case TemperatureBOM -> getBackOfModuleTemperature();
			case TemperatureAmbient -> getAmbientTemperature();
			case WindSpeed -> getWindSpeed();
		};
	}

}
