/* ==================================================================
 * BackOfModuleTemperatureAccessorImpl.java - 5/07/2023 10:34:55 am
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.PointGroup;
import net.solarnetwork.sunspec.api.PointGroupList;
import net.solarnetwork.sunspec.api.environmental.BomTemperatureModelAccessor;
import net.solarnetwork.sunspec.api.environmental.BomTemperatureModelRegister;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.core.support.SimplePointGroup;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link BomTemperatureModelAccessor}.
 * 
 * @author matt
 * @version 1.0
 */
public class BomTemperatureModelAccessorImpl extends BaseModelAccessor
		implements BomTemperatureModelAccessor {

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
	public BomTemperatureModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
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
	public BomTemperatureModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, EnvironmentalModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return 0;
	}

	@Override
	public int getRepeatingBlockInstanceLength() {
		return 1;
	}

	@Override
	protected Collection<? extends ModbusReference> getRepeatingBlockRegisters() {
		return EnumSet.allOf(BomTemperatureModelRegister.class);
	}

	@Override
	public List<Float> getBackOfModuleTemperatures() {
		final int count = getModelLength();
		final int baseAddr = getBlockAddress();
		final List<Float> temps = new ArrayList<>(count);
		for ( int i = 0; i < count; i++ ) {
			Number t = getIntegerValue(BomTemperatureModelRegister.TemperatureBOM, baseAddr + i);
			if ( t != null ) {
				temps.add(t.floatValue() / 10f);
			} else {
				temps.add(null);
			}
		}
		return temps;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This model has no fixed block points, so this implementation returns
	 * {@code null}.
	 * </p>
	 */
	@Override
	public @Nullable Object getPointValue(ModbusReference point) {
		return null;
	}

	@SuppressWarnings("unused")
	@Override
	public List<PointGroupList> getPointGroups() {
		final List<Float> temps = getBackOfModuleTemperatures();
		final List<PointGroup> groups = new ArrayList<>(temps.size());
		for ( Float temp : temps ) {
			groups.add(new SimplePointGroup(getRepeatingBlockRegisters(), p -> temp));
		}
		return List.of(PointGroupList.repeating("BackOfModuleTemperatures", groups));
	}

}
