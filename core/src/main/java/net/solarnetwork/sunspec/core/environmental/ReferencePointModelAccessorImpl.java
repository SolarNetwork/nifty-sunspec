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

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.api.environmental.ReferencePoint;
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

	/** The model repeating block length. */
	public static final int REPEATING_BLOCK_LENGTH = 7;

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
		return 0;
	}

	@Override
	public int getRepeatingBlockInstanceLength() {
		return REPEATING_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getRepeatingBlockRegisters() {
		return EnumSet.allOf(ReferencePointModelRegister.class);
	}

	@SuppressWarnings("EnumOrdinal")
	@Override
	public List<ReferencePoint> getReferencePoints() {
		final int count = getModelLength();
		final int baseAddr = getBlockAddress();
		final List<ReferencePoint> points = new ArrayList<>(count);
		final int propCount = ReferencePointModelRegister.values().length;
		final @Nullable Integer[] data = new Integer[propCount];
		for ( int i = 0; i < count; i += REPEATING_BLOCK_LENGTH ) {
			final int blockAddr = baseAddr + i;
			for ( int j = 0; j < propCount; j++ ) {
				data[j] = getIntegerValue(ReferencePointModelRegister.values()[j], blockAddr);
			}
			points.add(new ReferencePoint(data));
		}
		return points;
	}

}
