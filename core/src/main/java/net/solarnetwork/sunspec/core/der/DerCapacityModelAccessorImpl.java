/* ==================================================================
 * DerCapacityModelAccessorImpl.java - 5/10/2026 9:32:29 am
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.Bitmaskable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.der.DerAbnormalOperatingCategory;
import net.solarnetwork.sunspec.api.der.DerCapacityModelAccessor;
import net.solarnetwork.sunspec.api.der.DerCapacityModelRegister;
import net.solarnetwork.sunspec.api.der.DerControlMode;
import net.solarnetwork.sunspec.api.der.DerIntentionalIslandCategory;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerNormalOperatingCategory;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link DerCapacityModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class DerCapacityModelAccessorImpl extends BaseModelAccessor implements DerCapacityModelAccessor {

	/** The DER capacity model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 50;

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
	public DerCapacityModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
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
	public DerCapacityModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, DerModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(DerCapacityModelRegister.class);
	}

	@Override
	public @Nullable BigDecimal getActivePowerMaximumRating() {
		return getScaledValue(DerCapacityModelRegister.ActivePowerMaximumRating,
				DerCapacityModelRegister.ScaleFactorActivePower);
	}

	@Override
	public @Nullable BigDecimal getActivePowerOverExcitedRating() {
		return getScaledValue(DerCapacityModelRegister.ActivePowerOverExcitedRating,
				DerCapacityModelRegister.ScaleFactorActivePower);
	}

	@Override
	public @Nullable Float getOverExcitedPowerFactorRating() {
		return getScaledFloatValue(DerCapacityModelRegister.OverExcitedPowerFactorRating,
				DerCapacityModelRegister.ScaleFactorPowerFactor);
	}

	@Override
	public @Nullable BigDecimal getActivePowerUnderExcitedRating() {
		return getScaledValue(DerCapacityModelRegister.ActivePowerUnderExcitedRating,
				DerCapacityModelRegister.ScaleFactorActivePower);
	}

	@Override
	public @Nullable Float getUnderExcitedPowerFactorRating() {
		return getScaledFloatValue(DerCapacityModelRegister.UnderExcitedPowerFactorRating,
				DerCapacityModelRegister.ScaleFactorPowerFactor);
	}

	@Override
	public @Nullable BigDecimal getApparentPowerMaximumRating() {
		return getScaledValue(DerCapacityModelRegister.ApparentPowerMaximumRating,
				DerCapacityModelRegister.ScaleFactorApparentPower);
	}

	@Override
	public @Nullable BigDecimal getReactivePowerInjectedMaximumRating() {
		return getScaledValue(DerCapacityModelRegister.ReactivePowerInjectedMaximumRating,
				DerCapacityModelRegister.ScaleFactorReactivePower);
	}

	@Override
	public @Nullable BigDecimal getReactivePowerAbsorbedMaximumRating() {
		return getScaledValue(DerCapacityModelRegister.ReactivePowerAbsorbedMaximumRating,
				DerCapacityModelRegister.ScaleFactorReactivePower);
	}

	@Override
	public @Nullable BigDecimal getActivePowerChargeRateMaximumRating() {
		return getScaledValue(DerCapacityModelRegister.ActivePowerChargeRateMaximumRating,
				DerCapacityModelRegister.ScaleFactorActivePower);
	}

	@Override
	public @Nullable BigDecimal getActivePowerDischargeRateMaximumRating() {
		return getScaledValue(DerCapacityModelRegister.ActivePowerDischargeRateMaximumRating,
				DerCapacityModelRegister.ScaleFactorActivePower);
	}

	@Override
	public @Nullable BigDecimal getApparentPowerChargeRateMaximumRating() {
		return getScaledValue(DerCapacityModelRegister.ApparentPowerChargeRateMaximumRating,
				DerCapacityModelRegister.ScaleFactorApparentPower);
	}

	@Override
	public @Nullable BigDecimal getApparentPowerDischargeRateMaximumRating() {
		return getScaledValue(DerCapacityModelRegister.ApparentPowerDischargeRateMaximumRating,
				DerCapacityModelRegister.ScaleFactorApparentPower);
	}

	@Override
	public @Nullable Float getVoltageNominalRating() {
		return getScaledFloatValue(DerCapacityModelRegister.VoltageNominalRating,
				DerCapacityModelRegister.ScaleFactorVoltage);
	}

	@Override
	public @Nullable Float getVoltageMaximumRating() {
		return getScaledFloatValue(DerCapacityModelRegister.VoltageMaximumRating,
				DerCapacityModelRegister.ScaleFactorVoltage);
	}

	@Override
	public @Nullable Float getVoltageMinimumRating() {
		return getScaledFloatValue(DerCapacityModelRegister.VoltageMinimumRating,
				DerCapacityModelRegister.ScaleFactorVoltage);
	}

	@Override
	public @Nullable Float getCurrentMaximumRating() {
		return getScaledFloatValue(DerCapacityModelRegister.CurrentMaximumRating,
				DerCapacityModelRegister.ScaleFactorCurrent);
	}

	@Override
	public @Nullable Float getReactiveSusceptanceRating() {
		return getScaledFloatValue(DerCapacityModelRegister.ReactiveSusceptanceRating,
				DerCapacityModelRegister.ScaleFactorSusceptance);
	}

	@Override
	public @Nullable DerNormalOperatingCategory getNormalOperatingCategory() {
		return getCodedValue(DerCapacityModelRegister.NormalOperatingCategoryRating,
				DerNormalOperatingCategory.class);
	}

	@Override
	public @Nullable DerAbnormalOperatingCategory getAbnormalOperatingCategory() {
		return getCodedValue(DerCapacityModelRegister.AbnormalOperatingCategoryRating,
				DerAbnormalOperatingCategory.class);
	}

	@Override
	public Set<DerControlMode> getSupportedControlModes() {
		return getBitmaskableValues(DerCapacityModelRegister.ControlModesBitmask, DerControlMode.class);
	}

	@Override
	public Set<DerIntentionalIslandCategory> getIntentionalIslandCategoriesRating() {
		return getBitmaskableValues(DerCapacityModelRegister.IntentionalIslandCategoriesRatingBitmask,
				DerIntentionalIslandCategory.class);
	}

	@Override
	public @Nullable BigDecimal getActivePowerMaximum() {
		return getScaledValue(DerCapacityModelRegister.ActivePowerMaximum,
				DerCapacityModelRegister.ScaleFactorActivePower);
	}

	@Override
	public void setActivePowerMaximum(ModbusConnection conn, BigDecimal watts) throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.ActivePowerMaximum,
				DerCapacityModelRegister.ScaleFactorActivePower, watts);
	}

	@Override
	public @Nullable BigDecimal getActivePowerOverExcited() {
		return getScaledValue(DerCapacityModelRegister.ActivePowerOverExcited,
				DerCapacityModelRegister.ScaleFactorActivePower);
	}

	@Override
	public void setActivePowerOverExcited(ModbusConnection conn, BigDecimal watts) throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.ActivePowerOverExcited,
				DerCapacityModelRegister.ScaleFactorActivePower, watts);
	}

	@Override
	public @Nullable Float getOverExcitedPowerFactor() {
		return getScaledFloatValue(DerCapacityModelRegister.OverExcitedPowerFactor,
				DerCapacityModelRegister.ScaleFactorPowerFactor);
	}

	@Override
	public void setOverExcitedPowerFactor(ModbusConnection conn, float powerFactor) throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.OverExcitedPowerFactor,
				DerCapacityModelRegister.ScaleFactorPowerFactor, powerFactor);
	}

	@Override
	public @Nullable BigDecimal getActivePowerUnderExcited() {
		return getScaledValue(DerCapacityModelRegister.ActivePowerUnderExcited,
				DerCapacityModelRegister.ScaleFactorActivePower);
	}

	@Override
	public void setActivePowerUnderExcited(ModbusConnection conn, BigDecimal watts) throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.ActivePowerUnderExcited,
				DerCapacityModelRegister.ScaleFactorActivePower, watts);
	}

	@Override
	public @Nullable Float getUnderExcitedPowerFactor() {
		return getScaledFloatValue(DerCapacityModelRegister.UnderExcitedPowerFactor,
				DerCapacityModelRegister.ScaleFactorPowerFactor);
	}

	@Override
	public void setUnderExcitedPowerFactor(ModbusConnection conn, float powerFactor) throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.UnderExcitedPowerFactor,
				DerCapacityModelRegister.ScaleFactorPowerFactor, powerFactor);
	}

	@Override
	public @Nullable BigDecimal getApparentPowerMaximum() {
		return getScaledValue(DerCapacityModelRegister.ApparentPowerMaximum,
				DerCapacityModelRegister.ScaleFactorApparentPower);
	}

	@Override
	public void setApparentPowerMaximum(ModbusConnection conn, BigDecimal voltAmps) throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.ApparentPowerMaximum,
				DerCapacityModelRegister.ScaleFactorApparentPower, voltAmps);
	}

	@Override
	public @Nullable BigDecimal getReactivePowerInjectedMaximum() {
		return getScaledValue(DerCapacityModelRegister.ReactivePowerInjectedMaximum,
				DerCapacityModelRegister.ScaleFactorReactivePower);
	}

	@Override
	public void setReactivePowerInjectedMaximum(ModbusConnection conn, BigDecimal vars)
			throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.ReactivePowerInjectedMaximum,
				DerCapacityModelRegister.ScaleFactorReactivePower, vars);
	}

	@Override
	public @Nullable BigDecimal getReactivePowerAbsorbedMaximum() {
		return getScaledValue(DerCapacityModelRegister.ReactivePowerAbsorbedMaximum,
				DerCapacityModelRegister.ScaleFactorReactivePower);
	}

	@Override
	public void setReactivePowerAbsorbedMaximum(ModbusConnection conn, BigDecimal vars)
			throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.ReactivePowerAbsorbedMaximum,
				DerCapacityModelRegister.ScaleFactorReactivePower, vars);
	}

	@Override
	public @Nullable BigDecimal getActivePowerChargeRateMaximum() {
		return getScaledValue(DerCapacityModelRegister.ActivePowerChargeRateMaximum,
				DerCapacityModelRegister.ScaleFactorActivePower);
	}

	@Override
	public void setActivePowerChargeRateMaximum(ModbusConnection conn, BigDecimal watts)
			throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.ActivePowerChargeRateMaximum,
				DerCapacityModelRegister.ScaleFactorActivePower, watts);
	}

	@Override
	public @Nullable BigDecimal getActivePowerDischargeRateMaximum() {
		return getScaledValue(DerCapacityModelRegister.ActivePowerDischargeRateMaximum,
				DerCapacityModelRegister.ScaleFactorActivePower);
	}

	@Override
	public void setActivePowerDischargeRateMaximum(ModbusConnection conn, BigDecimal watts)
			throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.ActivePowerDischargeRateMaximum,
				DerCapacityModelRegister.ScaleFactorActivePower, watts);
	}

	@Override
	public @Nullable BigDecimal getApparentPowerChargeRateMaximum() {
		return getScaledValue(DerCapacityModelRegister.ApparentPowerChargeRateMaximum,
				DerCapacityModelRegister.ScaleFactorApparentPower);
	}

	@Override
	public void setApparentPowerChargeRateMaximum(ModbusConnection conn, BigDecimal voltAmps)
			throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.ApparentPowerChargeRateMaximum,
				DerCapacityModelRegister.ScaleFactorApparentPower, voltAmps);
	}

	@Override
	public @Nullable BigDecimal getApparentPowerDischargeRateMaximum() {
		return getScaledValue(DerCapacityModelRegister.ApparentPowerDischargeRateMaximum,
				DerCapacityModelRegister.ScaleFactorApparentPower);
	}

	@Override
	public void setApparentPowerDischargeRateMaximum(ModbusConnection conn, BigDecimal voltAmps)
			throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.ApparentPowerDischargeRateMaximum,
				DerCapacityModelRegister.ScaleFactorApparentPower, voltAmps);
	}

	@Override
	public @Nullable Float getVoltageNominal() {
		return getScaledFloatValue(DerCapacityModelRegister.VoltageNominal,
				DerCapacityModelRegister.ScaleFactorVoltage);
	}

	@Override
	public void setVoltageNominal(ModbusConnection conn, float volts) throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.VoltageNominal,
				DerCapacityModelRegister.ScaleFactorVoltage, volts);
	}

	@Override
	public @Nullable Float getVoltageMaximum() {
		return getScaledFloatValue(DerCapacityModelRegister.VoltageMaximum,
				DerCapacityModelRegister.ScaleFactorVoltage);
	}

	@Override
	public void setVoltageMaximum(ModbusConnection conn, float volts) throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.VoltageMaximum,
				DerCapacityModelRegister.ScaleFactorVoltage, volts);
	}

	@Override
	public @Nullable Float getVoltageMinimum() {
		return getScaledFloatValue(DerCapacityModelRegister.VoltageMinimum,
				DerCapacityModelRegister.ScaleFactorVoltage);
	}

	@Override
	public void setVoltageMinimum(ModbusConnection conn, float volts) throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.VoltageMinimum,
				DerCapacityModelRegister.ScaleFactorVoltage, volts);
	}

	@Override
	public @Nullable Float getCurrentMaximum() {
		return getScaledFloatValue(DerCapacityModelRegister.CurrentMaximum,
				DerCapacityModelRegister.ScaleFactorCurrent);
	}

	@Override
	public void setCurrentMaximum(ModbusConnection conn, float amps) throws IOException {
		writeScaledValue(conn, DerCapacityModelRegister.CurrentMaximum,
				DerCapacityModelRegister.ScaleFactorCurrent, amps);
	}

	@Override
	public Set<DerIntentionalIslandCategory> getIntentionalIslandCategories() {
		return getBitmaskableValues(DerCapacityModelRegister.IntentionalIslandCategoriesBitmask,
				DerIntentionalIslandCategory.class);
	}

	@Override
	public void setIntentionalIslandCategories(ModbusConnection conn,
			Set<DerIntentionalIslandCategory> categories) throws IOException {
		writeValue(conn, DerCapacityModelRegister.IntentionalIslandCategoriesBitmask,
				Bitmaskable.bitmaskValue(categories));
	}

}
