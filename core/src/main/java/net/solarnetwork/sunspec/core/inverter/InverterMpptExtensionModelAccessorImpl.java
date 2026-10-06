/* ==================================================================
 * InverterMpptExtensionModelAccessorImpl.java - 6/09/2019 5:24:30 pm
 *
 * Copyright 2019 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.core.inverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.OperatingState;
import net.solarnetwork.sunspec.api.inverter.InverterModelId;
import net.solarnetwork.sunspec.api.inverter.InverterMpptExtensionModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterMpptExtensionModelEvent;
import net.solarnetwork.sunspec.api.inverter.InverterMpptExtensionModelRegister;
import net.solarnetwork.sunspec.api.inverter.InverterOperatingState;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Data access object for an inverter MPPT extensions model.
 *
 * @author matt
 * @version 1.2
 * @since 1.4
 */
public class InverterMpptExtensionModelAccessorImpl extends BaseModelAccessor
		implements InverterMpptExtensionModelAccessor {

	/** The inverter MPPT extension model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 8;

	/** The inverter MPPT extension model module repeating block length. */
	public static final int REPEATING_BLOCK_LENGTH = 20;

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
	public InverterMpptExtensionModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@link InverterModelId} class will be used as the {@code ModelId}
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
	public InverterMpptExtensionModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, InverterModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	public int getRepeatingBlockInstanceLength() {
		return REPEATING_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.range(InverterMpptExtensionModelRegister.ScaleFactorDcCurrent,
				InverterMpptExtensionModelRegister.TimestampPeriod);
	}

	@Override
	protected Collection<? extends ModbusReference> getRepeatingBlockRegisters() {
		return EnumSet.range(InverterMpptExtensionModelRegister.ModuleInputId,
				InverterMpptExtensionModelRegister.ModuleEventsBitmask);
	}

	@Override
	public List<DcModule> getDcModules() {
		Integer n = getIntegerValue(InverterMpptExtensionModelRegister.ModuleCount);
		final int count = (n != null ? n.intValue() : 0);
		if ( count < 1 ) {
			return List.of();
		}
		List<DcModule> result = new ArrayList<>(count);
		for ( int i = 0; i < count; i++ ) {
			result.add(new InverterMpptExtensionDcModule(i));
		}
		return result;
	}

	@Override
	public Set<? extends ModelEvent> getEvents() {
		Number n = getBitfield(InverterMpptExtensionModelRegister.EventsBitmask, getBlockAddress());
		return InverterMpptExtensionModelEvent.forBitmask(n != null ? n.longValue() : 0L);
	}

	@Override
	public @Nullable Integer getTimestampPeriod() {
		return getIntegerValue(InverterMpptExtensionModelRegister.TimestampPeriod);
	}

	private class InverterMpptExtensionDcModule implements DcModule {

		private final int index;

		private InverterMpptExtensionDcModule(int index) {
			super();
			this.index = index;
		}

		@Override
		public @Nullable Integer getInputId() {
			return getIntegerValue(InverterMpptExtensionModelRegister.ModuleInputId,
					getBlockAddress() + getFixedBlockLength() + index * REPEATING_BLOCK_LENGTH);
		}

		@Override
		public @Nullable String getInputName() {
			return getStringValue(InverterMpptExtensionModelRegister.ModuleName,
					getBlockAddress() + getFixedBlockLength() + index * REPEATING_BLOCK_LENGTH);
		}

		@Override
		public @Nullable Float getDCCurrent() {
			Number n = getScaledValue(InverterMpptExtensionModelRegister.ModuleDcCurrent,
					InverterMpptExtensionModelRegister.ScaleFactorDcCurrent,
					getBlockAddress() + getFixedBlockLength() + index * REPEATING_BLOCK_LENGTH,
					getBlockAddress());
			return (n != null ? n.floatValue() : null);
		}

		@Override
		public @Nullable Float getDCVoltage() {
			Number n = getScaledValue(InverterMpptExtensionModelRegister.ModuleDcVoltage,
					InverterMpptExtensionModelRegister.ScaleFactorDcVoltage,
					getBlockAddress() + getFixedBlockLength() + index * REPEATING_BLOCK_LENGTH,
					getBlockAddress());
			return (n != null ? n.floatValue() : null);
		}

		@Override
		public @Nullable Integer getDCPower() {
			Number n = getScaledValue(InverterMpptExtensionModelRegister.ModuleDcPower,
					InverterMpptExtensionModelRegister.ScaleFactorDcPower,
					getBlockAddress() + getFixedBlockLength() + index * REPEATING_BLOCK_LENGTH,
					getBlockAddress());
			return (n != null ? n.intValue() : null);
		}

		@Override
		public @Nullable Long getDCEnergyDelivered() {
			Number n = getScaledValue(InverterMpptExtensionModelRegister.ModuleLifetimeEnergy,
					InverterMpptExtensionModelRegister.ScaleFactorDcEnergy,
					getBlockAddress() + getFixedBlockLength() + index * REPEATING_BLOCK_LENGTH,
					getBlockAddress());
			return (n != null ? n.longValue() : null);
		}

		@Override
		public @Nullable Long getDataTimestamp() {
			return getLongValue(InverterMpptExtensionModelRegister.ModuleTimestamp,
					getBlockAddress() + getFixedBlockLength() + index * REPEATING_BLOCK_LENGTH);
		}

		@Override
		public @Nullable Float getTemperature() {
			return getFloatValue(InverterMpptExtensionModelRegister.ModuleTemperature,
					getBlockAddress() + getFixedBlockLength() + index * REPEATING_BLOCK_LENGTH);
		}

		@Override
		public @Nullable OperatingState getOperatingState() {
			Integer n = getIntegerValue(InverterMpptExtensionModelRegister.ModuleOperatingState,
					getBlockAddress() + getFixedBlockLength() + index * REPEATING_BLOCK_LENGTH);
			if ( n == null ) {
				return null;
			}
			return InverterOperatingState.forCode(n.intValue());
		}

		@Override
		public Set<? extends ModelEvent> getEvents() {
			Number n = getBitfield(InverterMpptExtensionModelRegister.ModuleEventsBitmask,
					getBlockAddress() + getFixedBlockLength() + index * REPEATING_BLOCK_LENGTH);
			return InverterMpptExtensionModelEvent.forBitmask(n != null ? n.longValue() : 0L);
		}

	}

}
