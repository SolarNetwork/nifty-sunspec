/* ==================================================================
 * StringCombinerAdvancedModelAccessorImpl.java - 10/09/2019 3:49:52 pm
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

package net.solarnetwork.sunspec.core.combiner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.GenericModelEvent;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.combiner.StringCombinerAdvancedModelAccessor;
import net.solarnetwork.sunspec.api.combiner.StringCombinerAdvancedModelRegister;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelEvent;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelId;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link StringCombinerAdvancedModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class StringCombinerAdvancedModelAccessorImpl extends BaseModelAccessor
		implements StringCombinerAdvancedModelAccessor {

	/** The advanced string combiner (402) model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 20;

	/** The advanced string combiner v2 (404) model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH_2 = 25;

	/** The advanced string combiner model input repeating block length. */
	public static final int REPEATING_BLOCK_LENGTH = 14;

	/**
	 * The legacy advanced string combiner model input repeating block length.
	 */
	public static final int REPEATING_BLOCK_LENGTH_LEGACY = 13;

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
	public StringCombinerAdvancedModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@link StringCombinerModelId} class will be used as the
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
	public StringCombinerAdvancedModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, StringCombinerModelId.forId(modelId));
	}

	private boolean isVersion2() {
		return StringCombinerModelId.AdvancedStringCombiner2 == getModelId();
	}

	@Override
	public int getFixedBlockLength() {
		return isVersion2() ? FIXED_BLOCK_LENGTH_2 : FIXED_BLOCK_LENGTH;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This returns {@link #REPEATING_BLOCK_LENGTH_LEGACY} if the model length
	 * fits that length but not {@link #REPEATING_BLOCK_LENGTH}.
	 * </p>
	 */
	@Override
	public int getRepeatingBlockInstanceLength() {
		final int repeatingLen = getModelLength() - getFixedBlockLength();
		if ( repeatingLen % REPEATING_BLOCK_LENGTH != 0
				&& repeatingLen % REPEATING_BLOCK_LENGTH_LEGACY == 0 ) {
			return REPEATING_BLOCK_LENGTH_LEGACY;
		}
		return REPEATING_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.range(StringCombinerAdvancedModelRegister.ScaleFactorDcCurrent,
				StringCombinerAdvancedModelRegister.ScaleFactorInputDcEnergy);
	}

	@Override
	protected Collection<? extends ModbusReference> getRepeatingBlockRegisters() {
		return EnumSet.range(StringCombinerAdvancedModelRegister.InputId,
				StringCombinerAdvancedModelRegister.InputModuleCount);
	}

	@Override
	public @Nullable Float getDCCurrent() {
		Number n = getScaledValue(StringCombinerAdvancedModelRegister.DcCurrent,
				StringCombinerAdvancedModelRegister.ScaleFactorDcCurrent);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public @Nullable Long getDCChargeDelivered() {
		Number n = getScaledValue(
				isVersion2() ? StringCombinerAdvancedModelRegister.DcChargeV2
						: StringCombinerAdvancedModelRegister.DcCharge,
				StringCombinerAdvancedModelRegister.ScaleFactorDcCharge);
		return (n != null ? n.longValue() : null);
	}

	@Override
	public @Nullable Float getDCVoltage() {
		Number n = getScaledValue(
				isVersion2() ? StringCombinerAdvancedModelRegister.DcVoltageV2
						: StringCombinerAdvancedModelRegister.DcVoltage,
				StringCombinerAdvancedModelRegister.ScaleFactorDcVoltage);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public @Nullable Float getTemperature() {
		return getFloatValue(StringCombinerAdvancedModelRegister.Temperature);
	}

	@Override
	public @Nullable Integer getDCPower() {
		Number n = getScaledValue(StringCombinerAdvancedModelRegister.DcPower,
				StringCombinerAdvancedModelRegister.ScaleFactorDcPower);
		return (n != null ? n.intValue() : null);
	}

	@Override
	public @Nullable Long getDCEnergy() {
		Number n = getScaledValue(
				isVersion2() ? StringCombinerAdvancedModelRegister.DcEnergyV2
						: StringCombinerAdvancedModelRegister.DcEnergy,
				StringCombinerAdvancedModelRegister.ScaleFactorDcEnergy);
		return (n != null ? n.longValue() : null);
	}

	@Override
	public @Nullable Float getDCPerformanceRatio() {
		Float n = getFloatValue(isVersion2() ? StringCombinerAdvancedModelRegister.DcPerformanceRatioV2
				: StringCombinerAdvancedModelRegister.DcPerformanceRatio);
		return (n != null ? n.floatValue() / 100f : null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<DcInput> getDcInputs() {
		return (List) getAdvancedDcInputs();
	}

	@Override
	public Set<? extends ModelEvent> getEvents() {
		Number n = getBitfield(StringCombinerAdvancedModelRegister.EventsBitmask);
		return StringCombinerModelEvent.forBitmask(n != null ? n.longValue() : 0L);
	}

	@Override
	public Set<ModelEvent> getVendorEvents() {
		Number n = getBitfield(StringCombinerAdvancedModelRegister.VendorEventsBitmask);
		return GenericModelEvent.forBitmask(n != null ? n.longValue() : 0L);
	}

	@Override
	public List<AdvancedDcInput> getAdvancedDcInputs() {
		Number n = getIntegerValue(StringCombinerAdvancedModelRegister.InputCount);
		final int count = (n != null ? n.intValue() : 0);
		if ( count < 1 ) {
			return Collections.emptyList();
		}
		List<AdvancedDcInput> result = new ArrayList<>(count);
		for ( int i = 0; i < count; i++ ) {
			result.add(new StringCombinerAdvancedDcInput(i));
		}
		return result;
	}

	private class StringCombinerAdvancedDcInput implements AdvancedDcInput {

		private final int index;

		private StringCombinerAdvancedDcInput(int index) {
			super();
			this.index = index;
		}

		private int inputAddress() {
			return getBlockAddress() + getFixedBlockLength() + index * getRepeatingBlockInstanceLength();
		}

		@Override
		public @Nullable Integer getInputId() {
			return getIntegerValue(StringCombinerAdvancedModelRegister.InputId, inputAddress());
		}

		@Override
		public @Nullable Float getDCCurrent() {
			Number n = getScaledValue(StringCombinerAdvancedModelRegister.InputDcCurrent,
					isVersion2() ? StringCombinerAdvancedModelRegister.ScaleFactorInputDcCurrent
							: StringCombinerAdvancedModelRegister.ScaleFactorDcCurrent,
					inputAddress(), getBlockAddress());
			return (n != null ? n.floatValue() : null);
		}

		@Override
		public @Nullable Long getDCChargeDelivered() {
			Number n = isVersion2()
					? getScaledValue(StringCombinerAdvancedModelRegister.InputDcChargeV2,
							StringCombinerAdvancedModelRegister.ScaleFactorInputDcCharge, inputAddress(),
							getBlockAddress())
					: getScaledValue(StringCombinerAdvancedModelRegister.InputDcCharge,
							StringCombinerAdvancedModelRegister.ScaleFactorDcCharge, inputAddress(),
							getBlockAddress());
			return (n != null ? n.longValue() : null);
		}

		@Override
		public Set<? extends ModelEvent> getEvents() {
			Number n = getBitfield(StringCombinerAdvancedModelRegister.InputEventsBitmask,
					inputAddress());
			return StringCombinerModelEvent.forBitmask(n != null ? n.longValue() : 0L);
		}

		@Override
		public Set<? extends ModelEvent> getVendorEvents() {
			Number n = getBitfield(StringCombinerAdvancedModelRegister.InputVendorEventsBitmask,
					inputAddress());
			return GenericModelEvent.forBitmask(n != null ? n.longValue() : 0L);
		}

		@Override
		public @Nullable Float getDCVoltage() {
			Number n = isVersion2()
					? getScaledValue(StringCombinerAdvancedModelRegister.InputDcVoltageV2,
							StringCombinerAdvancedModelRegister.ScaleFactorInputDcVoltage,
							inputAddress(), getBlockAddress())
					: getScaledValue(StringCombinerAdvancedModelRegister.InputDcVoltage,
							StringCombinerAdvancedModelRegister.ScaleFactorDcVoltage, inputAddress(),
							getBlockAddress());
			return (n != null ? n.floatValue() : null);
		}

		@Override
		public @Nullable Integer getDCPower() {
			// model 402 defines the input power scale factor as DCWh_SF
			Number n = getScaledValue(StringCombinerAdvancedModelRegister.InputDcPower,
					isVersion2() ? StringCombinerAdvancedModelRegister.ScaleFactorInputDcPower
							: StringCombinerAdvancedModelRegister.ScaleFactorDcEnergy,
					inputAddress(), getBlockAddress());
			return (n != null ? n.intValue() : null);
		}

		@Override
		public @Nullable Long getDCEnergy() {
			if ( !isVersion2() ) {
				// model 402 does not define an input energy scale factor
				return getLongValue(StringCombinerAdvancedModelRegister.InputDcEnergy, inputAddress());
			}
			Number n = getScaledValue(StringCombinerAdvancedModelRegister.InputDcEnergyV2,
					StringCombinerAdvancedModelRegister.ScaleFactorInputDcEnergy, inputAddress(),
					getBlockAddress());
			return (n != null ? n.longValue() : null);
		}

		@Override
		public @Nullable Float getDCPerformanceRatio() {
			Float n = getFloatValue(StringCombinerAdvancedModelRegister.InputDcPerformanceRatio,
					inputAddress());
			return (n != null ? n.floatValue() / 100f : null);
		}

		@Override
		public @Nullable Integer getModuleCount() {
			if ( getRepeatingBlockInstanceLength() != REPEATING_BLOCK_LENGTH ) {
				return null;
			}
			return getIntegerValue(StringCombinerAdvancedModelRegister.InputModuleCount, inputAddress());
		}

	}
}
