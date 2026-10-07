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

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.PointGroup;
import net.solarnetwork.sunspec.api.PointGroupList;
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
 * @version 1.0
 */
public class IrradianceModelAccessorImpl extends BaseModelAccessor implements IrradianceModelAccessor {

	/** The irradiance model repeating block length. */
	public static final int REPEATING_BLOCK_LENGTH = 5;

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
		return 0;
	}

	@Override
	public int getRepeatingBlockInstanceLength() {
		return REPEATING_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getRepeatingBlockRegisters() {
		return EnumSet.allOf(IrradianceModelRegister.class);
	}

	@Override
	public List<Irradiance> getIrradiances() {
		return new ArrayList<>(irradiances());
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

	@Override
	public List<PointGroupList> getPointGroups() {
		return List.of(PointGroupList.repeating("Irradiances", irradiances()));
	}

	/**
	 * Create the list of irradiances.
	 *
	 * @return the irradiances, one for each repeating block instance
	 */
	private List<IrradianceImpl> irradiances() {
		final int count = getRepeatingBlockInstanceCount();
		if ( count < 1 ) {
			return List.of();
		}
		final List<IrradianceImpl> result = new ArrayList<>(count);
		for ( int i = 0; i < count; i++ ) {
			result.add(new IrradianceImpl(i));
		}
		return result;
	}

	private final class IrradianceImpl implements Irradiance, PointGroup {

		private final int groupAddress;

		private IrradianceImpl(int index) {
			super();
			this.groupAddress = getBlockAddress() + index * REPEATING_BLOCK_LENGTH;
		}

		@Override
		public @Nullable Integer getGlobalHorizontalIrradiance() {
			return getIntegerValue(IrradianceModelRegister.GHI, groupAddress);
		}

		@Override
		public @Nullable Integer getPlaneOfArrayIrradiance() {
			return getIntegerValue(IrradianceModelRegister.POAI, groupAddress);
		}

		@Override
		public @Nullable Integer getDiffuseIrradiance() {
			return getIntegerValue(IrradianceModelRegister.DFI, groupAddress);
		}

		@Override
		public @Nullable Integer getDirectNormalIrradiance() {
			return getIntegerValue(IrradianceModelRegister.DNI, groupAddress);
		}

		@Override
		public @Nullable Integer getOtherIrradiance() {
			return getIntegerValue(IrradianceModelRegister.OTI, groupAddress);
		}

		@Override
		public Collection<? extends ModbusReference> getPointReferences() {
			return getRepeatingBlockRegisters();
		}

		@Override
		public @Nullable Object getPointValue(ModbusReference point) {
			if ( !(point instanceof IrradianceModelRegister r) ) {
				return null;
			}
			return switch (r) {
				case GHI -> getGlobalHorizontalIrradiance();
				case POAI -> getPlaneOfArrayIrradiance();
				case DFI -> getDiffuseIrradiance();
				case DNI -> getDirectNormalIrradiance();
				case OTI -> getOtherIrradiance();
			};
		}

	}

}
