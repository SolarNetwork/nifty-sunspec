/* ==================================================================
 * MeteorologicalModelAccessorImpl.java - 10/07/2023 8:43:47 am
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
import net.solarnetwork.domain.CodedValue;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.api.environmental.MeteorologicalModelAccessor;
import net.solarnetwork.sunspec.api.environmental.MeteorologicalModelRegister;
import net.solarnetwork.sunspec.api.environmental.PrecipitationType;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link MeteorologicalModelAccessor}.
 *
 * @author matt
 * @version 1.1
 * @since 4.2
 */
public class MeteorologicalModelAccessorImpl extends BaseModelAccessor
		implements MeteorologicalModelAccessor {

	/** The model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 11;

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
	public MeteorologicalModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
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
	public MeteorologicalModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, EnvironmentalModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(MeteorologicalModelRegister.class);
	}

	@Override
	public @Nullable Float getAmbientTemperature() {
		Number n = getValue(MeteorologicalModelRegister.TemperatureAmbient);
		return (n != null ? n.floatValue() / 10f : null);
	}

	@Override
	public @Nullable Integer getRelativeHumidity() {
		return getIntegerValue(MeteorologicalModelRegister.RelativeHumidity);
	}

	@Override
	public @Nullable Integer getAtmosphericPressure() {
		Number n = getValue(MeteorologicalModelRegister.BarometricPressure);
		return (n != null ? n.intValue() * 100 : null);
	}

	@Override
	public @Nullable Integer getWindSpeed() {
		return getIntegerValue(MeteorologicalModelRegister.WindSpeed);
	}

	@Override
	public @Nullable Integer getWindDirection() {
		return getIntegerValue(MeteorologicalModelRegister.WindDirection);
	}

	@Override
	public @Nullable Integer getRainAccumulation() {
		return getIntegerValue(MeteorologicalModelRegister.Rain);
	}

	@Override
	public @Nullable Integer getSnowAccumulation() {
		return getIntegerValue(MeteorologicalModelRegister.Snow);
	}

	@Override
	public @Nullable PrecipitationType getPrecipitationType() {
		Number n = getValue(MeteorologicalModelRegister.PrecipitationType);
		return (n != null ? CodedValue.forCodeValue(n.intValue(), PrecipitationType.class, null) : null);

	}

	@Override
	public @Nullable Integer getElectricField() {
		return getIntegerValue(MeteorologicalModelRegister.ElectricField);
	}

	@Override
	public @Nullable Integer getSurfaceWetness() {
		Number n = getValue(MeteorologicalModelRegister.SurfaceWetness);
		return (n != null ? n.intValue() * 1000 : null);
	}

	@Override
	public @Nullable Integer getSoilMoisture() {
		return getIntegerValue(MeteorologicalModelRegister.SoilMoisture);
	}

}
