/* ==================================================================
 * InverterBasicSettingsModelAccessorImpl.java - 15/10/2018 3:03:04 PM
 *
 * Copyright 2018 SolarNetwork.net Dev Team
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
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.AcPhase;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.ReactivePowerAction;
import net.solarnetwork.sunspec.api.inverter.ApparentPowerCalculationMethod;
import net.solarnetwork.sunspec.api.inverter.InverterApparentPowerCalculationMethod;
import net.solarnetwork.sunspec.api.inverter.InverterBasicSettingsModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterBasicSettingsRegister;
import net.solarnetwork.sunspec.api.inverter.InverterControlModelId;
import net.solarnetwork.sunspec.api.inverter.InverterReactivePowerAction;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Data access object for an inverter basic settings model.
 *
 * @author matt
 * @version 1.0
 */
public class InverterBasicSettingsModelAccessorImpl extends BaseModelAccessor
		implements InverterBasicSettingsModelAccessor {

	/** The model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 30;

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
	public InverterBasicSettingsModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
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
	public InverterBasicSettingsModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, InverterControlModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(InverterBasicSettingsRegister.class);
	}

	@Override
	public @Nullable BigDecimal getActivePowerMaximum() {
		return getScaledValue(InverterBasicSettingsRegister.ActivePowerMaximum,
				InverterBasicSettingsRegister.ScaleFactorActivePowerMaximum);
	}

	@Override
	public void setActivePowerMaximum(ModbusConnection conn, BigDecimal watts) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.ActivePowerMaximum,
				InverterBasicSettingsRegister.ScaleFactorActivePowerMaximum, watts);
	}

	@Override
	public @Nullable Float getPccVoltage() {
		Number n = getScaledValue(InverterBasicSettingsRegister.VoltagePcc,
				InverterBasicSettingsRegister.ScaleFactorVoltagePcc);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public void setPccVoltage(ModbusConnection conn, float volts) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.VoltagePcc,
				InverterBasicSettingsRegister.ScaleFactorVoltagePcc, volts);
	}

	@Override
	public @Nullable Float getPccVoltageOffset() {
		Number n = getScaledValue(InverterBasicSettingsRegister.VoltagePccOffset,
				InverterBasicSettingsRegister.ScaleFactorVoltagePccOffset);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public void setPccVoltageOffset(ModbusConnection conn, float volts) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.VoltagePccOffset,
				InverterBasicSettingsRegister.ScaleFactorVoltagePccOffset, volts);
	}

	@Override
	public @Nullable Float getVoltageMaximum() {
		Number n = getScaledValue(InverterBasicSettingsRegister.VoltageMaximum,
				InverterBasicSettingsRegister.ScaleFactorVoltageMinimumMaximum);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public void setVoltageMaximum(ModbusConnection conn, float volts) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.VoltageMaximum,
				InverterBasicSettingsRegister.ScaleFactorVoltageMinimumMaximum, volts);
	}

	@Override
	public @Nullable Float getVoltageMinimum() {
		Number n = getScaledValue(InverterBasicSettingsRegister.VoltageMinimum,
				InverterBasicSettingsRegister.ScaleFactorVoltageMinimumMaximum);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public void setVoltageMinimum(ModbusConnection conn, float volts) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.VoltageMinimum,
				InverterBasicSettingsRegister.ScaleFactorVoltageMinimumMaximum, volts);
	}

	@Override
	public @Nullable BigDecimal getApparentPowerMaximum() {
		return getScaledValue(InverterBasicSettingsRegister.ApparentPowerMaximum,
				InverterBasicSettingsRegister.ScaleFactorApparentPowerMaximum);
	}

	@Override
	public void setApparentPowerMaximum(ModbusConnection conn, BigDecimal voltAmps) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.ApparentPowerMaximum,
				InverterBasicSettingsRegister.ScaleFactorApparentPowerMaximum, voltAmps);
	}

	@Override
	public @Nullable BigDecimal getReactivePowerQ1Maximum() {
		return getScaledValue(InverterBasicSettingsRegister.ReactivePowerQ1Maximum,
				InverterBasicSettingsRegister.ScaleFactorReactivePowerMaximum);
	}

	@Override
	public void setReactivePowerQ1Maximum(ModbusConnection conn, BigDecimal vars) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.ReactivePowerQ1Maximum,
				InverterBasicSettingsRegister.ScaleFactorReactivePowerMaximum, vars);
	}

	@Override
	public @Nullable BigDecimal getReactivePowerQ2Maximum() {
		return getScaledValue(InverterBasicSettingsRegister.ReactivePowerQ2Maximum,
				InverterBasicSettingsRegister.ScaleFactorReactivePowerMaximum);
	}

	@Override
	public void setReactivePowerQ2Maximum(ModbusConnection conn, BigDecimal vars) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.ReactivePowerQ2Maximum,
				InverterBasicSettingsRegister.ScaleFactorReactivePowerMaximum, vars);
	}

	@Override
	public @Nullable BigDecimal getReactivePowerQ3Maximum() {
		return getScaledValue(InverterBasicSettingsRegister.ReactivePowerQ3Maximum,
				InverterBasicSettingsRegister.ScaleFactorReactivePowerMaximum);
	}

	@Override
	public void setReactivePowerQ3Maximum(ModbusConnection conn, BigDecimal vars) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.ReactivePowerQ3Maximum,
				InverterBasicSettingsRegister.ScaleFactorReactivePowerMaximum, vars);
	}

	@Override
	public @Nullable BigDecimal getReactivePowerQ4Maximum() {
		return getScaledValue(InverterBasicSettingsRegister.ReactivePowerQ4Maximum,
				InverterBasicSettingsRegister.ScaleFactorReactivePowerMaximum);
	}

	@Override
	public void setReactivePowerQ4Maximum(ModbusConnection conn, BigDecimal vars) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.ReactivePowerQ4Maximum,
				InverterBasicSettingsRegister.ScaleFactorReactivePowerMaximum, vars);
	}

	@Override
	public @Nullable Float getActivePowerRampRate() {
		Number n = getScaledValue(InverterBasicSettingsRegister.ActivePowerRampRate,
				InverterBasicSettingsRegister.ScaleFactorActivePowerRampRate);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public void setActivePowerRampRate(ModbusConnection conn, float percentPerSecond)
			throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.ActivePowerRampRate,
				InverterBasicSettingsRegister.ScaleFactorActivePowerRampRate, percentPerSecond);
	}

	@Override
	public @Nullable Float getPowerFactorQ1Minimum() {
		Number n = getScaledValue(InverterBasicSettingsRegister.PowerFactorQ1Minimum,
				InverterBasicSettingsRegister.ScaleFactorPowerFactorMinimum);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public void setPowerFactorQ1Minimum(ModbusConnection conn, float powerFactor) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.PowerFactorQ1Minimum,
				InverterBasicSettingsRegister.ScaleFactorPowerFactorMinimum, powerFactor);
	}

	@Override
	public @Nullable Float getPowerFactorQ2Minimum() {
		Number n = getScaledValue(InverterBasicSettingsRegister.PowerFactorQ2Minimum,
				InverterBasicSettingsRegister.ScaleFactorPowerFactorMinimum);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public void setPowerFactorQ2Minimum(ModbusConnection conn, float powerFactor) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.PowerFactorQ2Minimum,
				InverterBasicSettingsRegister.ScaleFactorPowerFactorMinimum, powerFactor);
	}

	@Override
	public @Nullable Float getPowerFactorQ3Minimum() {
		Number n = getScaledValue(InverterBasicSettingsRegister.PowerFactorQ3Minimum,
				InverterBasicSettingsRegister.ScaleFactorPowerFactorMinimum);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public void setPowerFactorQ3Minimum(ModbusConnection conn, float powerFactor) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.PowerFactorQ3Minimum,
				InverterBasicSettingsRegister.ScaleFactorPowerFactorMinimum, powerFactor);
	}

	@Override
	public @Nullable Float getPowerFactorQ4Minimum() {
		Number n = getScaledValue(InverterBasicSettingsRegister.PowerFactorQ4Minimum,
				InverterBasicSettingsRegister.ScaleFactorPowerFactorMinimum);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public void setPowerFactorQ4Minimum(ModbusConnection conn, float powerFactor) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.PowerFactorQ4Minimum,
				InverterBasicSettingsRegister.ScaleFactorPowerFactorMinimum, powerFactor);
	}

	@Override
	public @Nullable ReactivePowerAction getImportExportChangeReactivePowerAction() {
		return getCodedValue(InverterBasicSettingsRegister.ImportExportChangeReactivePowerAction,
				InverterReactivePowerAction.class);
	}

	@Override
	public void setImportExportChangeReactivePowerAction(ModbusConnection conn,
			ReactivePowerAction action) throws IOException {
		writeValue(conn, InverterBasicSettingsRegister.ImportExportChangeReactivePowerAction,
				action.getCode());
	}

	@Override
	public @Nullable ApparentPowerCalculationMethod getApparentPowerCalculationMethod() {
		return getCodedValue(InverterBasicSettingsRegister.ApparentPowerCalculationMethod,
				InverterApparentPowerCalculationMethod.class);
	}

	@Override
	public void setApparentPowerCalculationMethod(ModbusConnection conn,
			ApparentPowerCalculationMethod method) throws IOException {
		writeValue(conn, InverterBasicSettingsRegister.ApparentPowerCalculationMethod, method.getCode());
	}

	@Override
	public @Nullable Float getActivePowerRampRateMaximum() {
		Number n = getScaledValue(InverterBasicSettingsRegister.ActivePowerRampRateMaximum,
				InverterBasicSettingsRegister.ScaleFactorActivePowerRampRateMaximum);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public void setActivePowerRampRateMaximum(ModbusConnection conn, float percent) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.ActivePowerRampRateMaximum,
				InverterBasicSettingsRegister.ScaleFactorActivePowerRampRateMaximum, percent);
	}

	@Override
	public @Nullable Float getEcpFrequency() {
		Number n = getScaledValue(InverterBasicSettingsRegister.EcpNominalFrequency,
				InverterBasicSettingsRegister.ScaleFactorEcpNominalFrequency);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public void setEcpFrequency(ModbusConnection conn, float hertz) throws IOException {
		writeScaledValue(conn, InverterBasicSettingsRegister.EcpNominalFrequency,
				InverterBasicSettingsRegister.ScaleFactorEcpNominalFrequency, hertz);
	}

	@Override
	public @Nullable AcPhase getConnectedPhase() {
		Integer n = getIntegerValue(InverterBasicSettingsRegister.ConnectedPhase);
		if ( n == null ) {
			return null;
		}
		return switch (n) {
			case 1 -> AcPhase.PhaseA;
			case 2 -> AcPhase.PhaseB;
			case 3 -> AcPhase.PhaseC;
			default -> null;
		};
	}

	@Override
	public void setConnectedPhase(ModbusConnection conn, AcPhase phase) throws IOException {
		switch (phase) {
			// the SunSpec phase codes match the AcPhase numbers
			case PhaseA, PhaseB, PhaseC -> writeValue(conn, InverterBasicSettingsRegister.ConnectedPhase,
					phase.getNumber());
			default -> throw new IllegalArgumentException(
					String.format("The %s phase is not a connected phase.", phase));
		}
	}

}
