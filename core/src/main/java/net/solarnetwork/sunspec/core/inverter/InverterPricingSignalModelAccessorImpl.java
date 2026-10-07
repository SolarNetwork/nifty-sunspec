/* ==================================================================
 * InverterPricingSignalModelAccessorImpl.java - 6/10/2026 9:27:15 am
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

package net.solarnetwork.sunspec.core.inverter;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.inverter.InverterControlModelId;
import net.solarnetwork.sunspec.api.inverter.InverterPricingSignalModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterPricingSignalModelRegister;
import net.solarnetwork.sunspec.api.inverter.InverterPricingSignalType;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link InverterPricingSignalModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class InverterPricingSignalModelAccessorImpl extends BaseModelAccessor
		implements InverterPricingSignalModelAccessor {

	/** The pricing signal model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 8;

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
	public InverterPricingSignalModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@link InverterControlModelId} class will be used as the
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
	public InverterPricingSignalModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, InverterControlModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(InverterPricingSignalModelRegister.class);
	}

	@Override
	public @Nullable Boolean isPricingEnabled() {
		return getBitfieldBit(InverterPricingSignalModelRegister.PricingEnabled, 0);
	}

	@Override
	public void setPricingEnabled(ModbusConnection conn, boolean enabled) throws IOException {
		writeValue(conn, InverterPricingSignalModelRegister.PricingEnabled, enabled ? 1 : 0);
	}

	@Override
	public @Nullable InverterPricingSignalType getPricingSignalType() {
		return getCodedValue(InverterPricingSignalModelRegister.PricingSignalType,
				InverterPricingSignalType.class);
	}

	@Override
	public void setPricingSignalType(ModbusConnection conn, InverterPricingSignalType type)
			throws IOException {
		writeValue(conn, InverterPricingSignalModelRegister.PricingSignalType, type.getCode());
	}

	@Override
	public @Nullable Float getPricingSignal() {
		return getScaledFloatValue(InverterPricingSignalModelRegister.PricingSignal,
				InverterPricingSignalModelRegister.ScaleFactorPricingSignal);
	}

	@Override
	public void setPricingSignal(ModbusConnection conn, float signal) throws IOException {
		writeScaledValue(conn, InverterPricingSignalModelRegister.PricingSignal,
				InverterPricingSignalModelRegister.ScaleFactorPricingSignal, signal);
	}

	@Override
	public @Nullable Integer getPricingTimeWindow() {
		return getIntegerValue(InverterPricingSignalModelRegister.PricingTimeWindow);
	}

	@Override
	public void setPricingTimeWindow(ModbusConnection conn, int seconds) throws IOException {
		writeValue(conn, InverterPricingSignalModelRegister.PricingTimeWindow, seconds);
	}

	@Override
	public @Nullable Integer getPricingReversionTime() {
		return getIntegerValue(InverterPricingSignalModelRegister.PricingReversionTime);
	}

	@Override
	public void setPricingReversionTime(ModbusConnection conn, int seconds) throws IOException {
		writeValue(conn, InverterPricingSignalModelRegister.PricingReversionTime, seconds);
	}

	@Override
	public @Nullable Integer getPricingRampTime() {
		return getIntegerValue(InverterPricingSignalModelRegister.PricingRampTime);
	}

	@Override
	public void setPricingRampTime(ModbusConnection conn, int seconds) throws IOException {
		writeValue(conn, InverterPricingSignalModelRegister.PricingRampTime, seconds);
	}

	@Override
	public @Nullable Object getPointValue(ModbusReference point) {
		if ( !(point instanceof InverterPricingSignalModelRegister r) ) {
			return null;
		}
		return switch (r) {
			case PricingEnabled -> isPricingEnabled();
			case PricingSignalType -> getPricingSignalType();
			case PricingSignal -> getPricingSignal();
			case PricingTimeWindow -> getPricingTimeWindow();
			case PricingReversionTime -> getPricingReversionTime();
			case PricingRampTime -> getPricingRampTime();
			case ScaleFactorPricingSignal -> null;
		};
	}

}
