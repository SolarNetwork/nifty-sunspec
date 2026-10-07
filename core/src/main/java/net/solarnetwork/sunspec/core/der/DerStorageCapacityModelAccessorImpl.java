/* ==================================================================
 * DerStorageCapacityModelAccessorImpl.java - 5/10/2026 10:18:41 am
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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.EnumSet;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerStorageCapacityModelAccessor;
import net.solarnetwork.sunspec.api.der.DerStorageCapacityModelRegister;
import net.solarnetwork.sunspec.api.der.DerStorageStatus;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link DerStorageCapacityModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class DerStorageCapacityModelAccessorImpl extends BaseModelAccessor
		implements DerStorageCapacityModelAccessor {

	/** The DER storage capacity model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 7;

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
	public DerStorageCapacityModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
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
	public DerStorageCapacityModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, DerModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(DerStorageCapacityModelRegister.class);
	}

	@Override
	public @Nullable BigDecimal getEnergyRating() {
		return getScaledValue(DerStorageCapacityModelRegister.EnergyRating,
				DerStorageCapacityModelRegister.ScaleFactorEnergy);
	}

	@Override
	public @Nullable BigDecimal getEnergyAvailable() {
		return getScaledValue(DerStorageCapacityModelRegister.EnergyAvailable,
				DerStorageCapacityModelRegister.ScaleFactorEnergy);
	}

	@Override
	public @Nullable Float getStateOfCharge() {
		return getScaledFloatValue(DerStorageCapacityModelRegister.StateOfCharge,
				DerStorageCapacityModelRegister.ScaleFactorPercent);
	}

	@Override
	public @Nullable Float getStateOfHealth() {
		return getScaledFloatValue(DerStorageCapacityModelRegister.StateOfHealth,
				DerStorageCapacityModelRegister.ScaleFactorPercent);
	}

	@Override
	public @Nullable DerStorageStatus getStorageStatus() {
		return getCodedValue(DerStorageCapacityModelRegister.Status, DerStorageStatus.class);
	}

	@Override
	public @Nullable Object getPointValue(ModbusReference point) {
		if ( !(point instanceof DerStorageCapacityModelRegister r) ) {
			return null;
		}
		return switch (r) {
			case EnergyRating -> getEnergyRating();
			case EnergyAvailable -> getEnergyAvailable();
			case StateOfCharge -> getStateOfCharge();
			case StateOfHealth -> getStateOfHealth();
			case Status -> getStorageStatus();
			case ScaleFactorEnergy -> null;
			case ScaleFactorPercent -> null;
		};
	}

}
