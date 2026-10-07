/* ==================================================================
 * InverterBasicStorageControlsModelAccessorImpl.java - 6/10/2026 9:03:44 am
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
import java.math.BigDecimal;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.Bitmaskable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.inverter.InverterBasicStorageControlsModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterBasicStorageControlsModelRegister;
import net.solarnetwork.sunspec.api.inverter.InverterChargeSource;
import net.solarnetwork.sunspec.api.inverter.InverterControlModelId;
import net.solarnetwork.sunspec.api.inverter.InverterStorageControlMode;
import net.solarnetwork.sunspec.api.storage.BatteryChargeStatus;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link InverterBasicStorageControlsModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class InverterBasicStorageControlsModelAccessorImpl extends BaseModelAccessor
		implements InverterBasicStorageControlsModelAccessor {

	/** The basic storage controls model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 24;

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
	public InverterBasicStorageControlsModelAccessorImpl(ModelData data, int baseAddress,
			ModelId modelId) {
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
	public InverterBasicStorageControlsModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, InverterControlModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(InverterBasicStorageControlsModelRegister.class);
	}

	@Override
	public @Nullable BigDecimal getActivePowerChargeRateMaximum() {
		return getScaledValue(InverterBasicStorageControlsModelRegister.ActivePowerChargeRateMaximum,
				InverterBasicStorageControlsModelRegister.ScaleFactorActivePowerChargeRateMaximum);
	}

	@Override
	public void setActivePowerChargeRateMaximum(ModbusConnection conn, BigDecimal watts)
			throws IOException {
		writeScaledValue(conn, InverterBasicStorageControlsModelRegister.ActivePowerChargeRateMaximum,
				InverterBasicStorageControlsModelRegister.ScaleFactorActivePowerChargeRateMaximum,
				watts);
	}

	@Override
	public @Nullable Float getChargeRampRate() {
		return getScaledFloatValue(InverterBasicStorageControlsModelRegister.ChargeRampRate,
				InverterBasicStorageControlsModelRegister.ScaleFactorChargeDischargeRampRate);
	}

	@Override
	public void setChargeRampRate(ModbusConnection conn, float percentPerSecond) throws IOException {
		writeScaledValue(conn, InverterBasicStorageControlsModelRegister.ChargeRampRate,
				InverterBasicStorageControlsModelRegister.ScaleFactorChargeDischargeRampRate,
				percentPerSecond);
	}

	@Override
	public @Nullable Float getDischargeRampRate() {
		return getScaledFloatValue(InverterBasicStorageControlsModelRegister.DischargeRampRate,
				InverterBasicStorageControlsModelRegister.ScaleFactorChargeDischargeRampRate);
	}

	@Override
	public void setDischargeRampRate(ModbusConnection conn, float percentPerSecond) throws IOException {
		writeScaledValue(conn, InverterBasicStorageControlsModelRegister.DischargeRampRate,
				InverterBasicStorageControlsModelRegister.ScaleFactorChargeDischargeRampRate,
				percentPerSecond);
	}

	@Override
	public Set<InverterStorageControlMode> getStorageControlModes() {
		return getBitmaskableValues(InverterBasicStorageControlsModelRegister.StorageControlModes,
				InverterStorageControlMode.class);
	}

	@Override
	public void setStorageControlModes(ModbusConnection conn, Set<InverterStorageControlMode> modes)
			throws IOException {
		writeValue(conn, InverterBasicStorageControlsModelRegister.StorageControlModes,
				Bitmaskable.bitmaskValue(modes));
	}

	@Override
	public @Nullable BigDecimal getApparentPowerChargeRateMaximum() {
		return getScaledValue(InverterBasicStorageControlsModelRegister.ApparentPowerChargeRateMaximum,
				InverterBasicStorageControlsModelRegister.ScaleFactorApparentPowerChargeRateMaximum);
	}

	@Override
	public void setApparentPowerChargeRateMaximum(ModbusConnection conn, BigDecimal voltAmps)
			throws IOException {
		writeScaledValue(conn, InverterBasicStorageControlsModelRegister.ApparentPowerChargeRateMaximum,
				InverterBasicStorageControlsModelRegister.ScaleFactorApparentPowerChargeRateMaximum,
				voltAmps);
	}

	@Override
	public @Nullable Float getStateOfChargeReserveMinimum() {
		return getScaledFloatValue(InverterBasicStorageControlsModelRegister.StateOfChargeReserveMinimum,
				InverterBasicStorageControlsModelRegister.ScaleFactorStateOfChargeReserveMinimum);
	}

	@Override
	public void setStateOfChargeReserveMinimum(ModbusConnection conn, float percent) throws IOException {
		writeScaledValue(conn, InverterBasicStorageControlsModelRegister.StateOfChargeReserveMinimum,
				InverterBasicStorageControlsModelRegister.ScaleFactorStateOfChargeReserveMinimum,
				percent);
	}

	@Override
	public @Nullable Float getStateOfCharge() {
		return getScaledFloatValue(InverterBasicStorageControlsModelRegister.StateOfCharge,
				InverterBasicStorageControlsModelRegister.ScaleFactorStateOfCharge);
	}

	@Override
	public @Nullable BigDecimal getStorageAvailable() {
		return getScaledValue(InverterBasicStorageControlsModelRegister.StorageAvailable,
				InverterBasicStorageControlsModelRegister.ScaleFactorStorageAvailable);
	}

	@Override
	public @Nullable Float getBatteryVoltage() {
		return getScaledFloatValue(InverterBasicStorageControlsModelRegister.BatteryVoltage,
				InverterBasicStorageControlsModelRegister.ScaleFactorBatteryVoltage);
	}

	@Override
	public @Nullable BatteryChargeStatus getChargeStatus() {
		return getCodedValue(InverterBasicStorageControlsModelRegister.ChargeStatus,
				BatteryChargeStatus.class);
	}

	@Override
	public @Nullable Float getDischargeRatePercent() {
		return getScaledFloatValue(InverterBasicStorageControlsModelRegister.DischargeRatePercent,
				InverterBasicStorageControlsModelRegister.ScaleFactorChargeDischargeRatePercent);
	}

	@Override
	public void setDischargeRatePercent(ModbusConnection conn, float percent) throws IOException {
		writeScaledValue(conn, InverterBasicStorageControlsModelRegister.DischargeRatePercent,
				InverterBasicStorageControlsModelRegister.ScaleFactorChargeDischargeRatePercent,
				percent);
	}

	@Override
	public @Nullable Float getChargeRatePercent() {
		return getScaledFloatValue(InverterBasicStorageControlsModelRegister.ChargeRatePercent,
				InverterBasicStorageControlsModelRegister.ScaleFactorChargeDischargeRatePercent);
	}

	@Override
	public void setChargeRatePercent(ModbusConnection conn, float percent) throws IOException {
		writeScaledValue(conn, InverterBasicStorageControlsModelRegister.ChargeRatePercent,
				InverterBasicStorageControlsModelRegister.ScaleFactorChargeDischargeRatePercent,
				percent);
	}

	@Override
	public @Nullable Integer getChargeDischargeRateTimeWindow() {
		return getIntegerValue(InverterBasicStorageControlsModelRegister.ChargeDischargeRateTimeWindow);
	}

	@Override
	public void setChargeDischargeRateTimeWindow(ModbusConnection conn, int seconds) throws IOException {
		writeValue(conn, InverterBasicStorageControlsModelRegister.ChargeDischargeRateTimeWindow,
				seconds);
	}

	@Override
	public @Nullable Integer getChargeDischargeRateReversionTime() {
		return getIntegerValue(
				InverterBasicStorageControlsModelRegister.ChargeDischargeRateReversionTime);
	}

	@Override
	public void setChargeDischargeRateReversionTime(ModbusConnection conn, int seconds)
			throws IOException {
		writeValue(conn, InverterBasicStorageControlsModelRegister.ChargeDischargeRateReversionTime,
				seconds);
	}

	@Override
	public @Nullable Integer getChargeDischargeRateRampTime() {
		return getIntegerValue(InverterBasicStorageControlsModelRegister.ChargeDischargeRateRampTime);
	}

	@Override
	public void setChargeDischargeRateRampTime(ModbusConnection conn, int seconds) throws IOException {
		writeValue(conn, InverterBasicStorageControlsModelRegister.ChargeDischargeRateRampTime, seconds);
	}

	@Override
	public @Nullable InverterChargeSource getChargeSource() {
		return getCodedValue(InverterBasicStorageControlsModelRegister.ChargeSource,
				InverterChargeSource.class);
	}

	@Override
	public void setChargeSource(ModbusConnection conn, InverterChargeSource source) throws IOException {
		writeValue(conn, InverterBasicStorageControlsModelRegister.ChargeSource, source.getCode());
	}

}
