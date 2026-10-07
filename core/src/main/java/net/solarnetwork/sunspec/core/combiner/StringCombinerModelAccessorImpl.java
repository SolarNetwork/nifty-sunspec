/* ==================================================================
 * StringCombinerModelAccessorImpl.java - 10/09/2019 7:03:23 am
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
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelAccessor;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelEvent;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelId;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelRegister;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Data access object for an string combiner model.
 *
 * @author matt
 * @version 1.0
 */
public class StringCombinerModelAccessorImpl extends BaseModelAccessor
		implements StringCombinerModelAccessor {

	/** The basic string combiner (401) model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 14;

	/** The basic string combiner v2 (403) model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH_2 = 16;

	/** The basic string combiner model input repeating block length. */
	public static final int REPEATING_BLOCK_LENGTH = 8;

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
	public StringCombinerModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
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
	public StringCombinerModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, StringCombinerModelId.forId(modelId));
	}

	private boolean isVersion2() {
		return StringCombinerModelId.BasicStringCombiner2 == getModelId();
	}

	@Override
	public int getFixedBlockLength() {
		return isVersion2() ? FIXED_BLOCK_LENGTH_2 : FIXED_BLOCK_LENGTH;
	}

	@Override
	public int getRepeatingBlockInstanceLength() {
		return REPEATING_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.range(StringCombinerModelRegister.ScaleFactorDcCurrent,
				StringCombinerModelRegister.ScaleFactorInputDcCharge);
	}

	@Override
	protected Collection<? extends ModbusReference> getRepeatingBlockRegisters() {
		return EnumSet.range(StringCombinerModelRegister.InputId,
				StringCombinerModelRegister.InputDcChargeV2);
	}

	@Override
	public @Nullable Float getDCCurrent() {
		Number n = getScaledValue(StringCombinerModelRegister.DcCurrent,
				StringCombinerModelRegister.ScaleFactorDcCurrent);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public @Nullable Long getDCChargeDelivered() {
		Number n = getScaledValue(
				isVersion2() ? StringCombinerModelRegister.DcChargeV2
						: StringCombinerModelRegister.DcCharge,
				StringCombinerModelRegister.ScaleFactorDcCharge);
		return (n != null ? n.longValue() : null);
	}

	@Override
	public @Nullable Float getDCVoltage() {
		Number n = getScaledValue(
				isVersion2() ? StringCombinerModelRegister.DcVoltageV2
						: StringCombinerModelRegister.DcVoltage,
				StringCombinerModelRegister.ScaleFactorDcVoltage);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public @Nullable Float getTemperature() {
		return getFloatValue(StringCombinerModelRegister.Temperature);
	}

	@Override
	public List<DcInput> getDcInputs() {
		Integer n = getIntegerValue(StringCombinerModelRegister.InputCount);
		final int count = (n != null ? n.intValue() : 0);
		if ( count < 1 ) {
			return Collections.emptyList();
		}
		List<DcInput> result = new ArrayList<>(count);
		for ( int i = 0; i < count; i++ ) {
			result.add(new StringCombinerDcInput(i));
		}
		return result;
	}

	@Override
	public Set<? extends ModelEvent> getEvents() {
		Number n = getBitfield(StringCombinerModelRegister.EventsBitmask);
		return StringCombinerModelEvent.forBitmask(n != null ? n.longValue() : 0L);
	}

	@Override
	public Set<ModelEvent> getVendorEvents() {
		Number n = getBitfield(StringCombinerModelRegister.VendorEventsBitmask);
		return GenericModelEvent.forBitmask(n != null ? n.longValue() : 0L);
	}

	private class StringCombinerDcInput implements DcInput {

		private final int index;

		private StringCombinerDcInput(int index) {
			super();
			this.index = index;
		}

		private int inputAddress() {
			return getBlockAddress() + getFixedBlockLength() + index * REPEATING_BLOCK_LENGTH;
		}

		@Override
		public @Nullable Integer getInputId() {
			return getIntegerValue(StringCombinerModelRegister.InputId, inputAddress());
		}

		@Override
		public @Nullable Float getDCCurrent() {
			Number n = getScaledValue(StringCombinerModelRegister.InputDcCurrent,
					isVersion2() ? StringCombinerModelRegister.ScaleFactorInputDcCurrent
							: StringCombinerModelRegister.ScaleFactorDcCurrent,
					inputAddress(), getBlockAddress());
			return (n != null ? n.floatValue() : null);
		}

		@Override
		public @Nullable Long getDCChargeDelivered() {
			Number n = isVersion2()
					? getScaledValue(StringCombinerModelRegister.InputDcChargeV2,
							StringCombinerModelRegister.ScaleFactorInputDcCharge, inputAddress(),
							getBlockAddress())
					: getScaledValue(StringCombinerModelRegister.InputDcCharge,
							StringCombinerModelRegister.ScaleFactorDcCharge, inputAddress(),
							getBlockAddress());
			return (n != null ? n.longValue() : null);
		}

		@Override
		public Set<? extends ModelEvent> getEvents() {
			Number n = getBitfield(StringCombinerModelRegister.InputEventsBitmask, inputAddress());
			return StringCombinerModelEvent.forBitmask(n != null ? n.longValue() : 0L);
		}

		@Override
		public Set<ModelEvent> getVendorEvents() {
			Number n = getBitfield(StringCombinerModelRegister.InputVendorEventsBitmask, inputAddress());
			return GenericModelEvent.forBitmask(n != null ? n.longValue() : 0L);
		}

	}
}
