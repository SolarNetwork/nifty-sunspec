/* ==================================================================
 * DerAcControlsModelAccessorImpl_704_01Tests.java - 5/10/2026 3:20:44 pm
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

package net.solarnetwork.sunspec.core.der.test;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.der.DerAcControlsModelAccessor;
import net.solarnetwork.sunspec.api.der.DerActivePowerSetpointMode;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerPowerFactorExcitation;
import net.solarnetwork.sunspec.api.der.DerRampRateReference;
import net.solarnetwork.sunspec.api.der.DerReactivePowerPriority;
import net.solarnetwork.sunspec.api.der.DerReactivePowerSetpointMode;
import net.solarnetwork.sunspec.core.der.DerAcControlsModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * Test cases for the {@link DerAcControlsModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class DerAcControlsModelAccessorImpl_704_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	/** The model block address. */
	private static final int BLOCK_ADDRESS = 405;

	private DerAcControlsModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerAcControlsModelAccessor.class);
	}

	private RecordingModbusConnection writableConnection() {
		return ModelDataUtils.getWritableModbusConnection(getClass(), TEST_DATA);
	}

	private static DerAcControlsModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(DerAcControlsModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerAcControlsModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerAcControlsModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(403, from(DerAcControlsModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BLOCK_ADDRESS, from(DerAcControlsModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.AcControls, from(DerAcControlsModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(65, from(DerAcControlsModelAccessor::getFixedBlockLength))
			.as("Model length")
			.returns(65, from(DerAcControlsModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void powerFactorWhenInjecting() {
		// GIVEN
		DerAcControlsModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Enabled")
			.returns(true, from(DerAcControlsModelAccessor::isPowerFactorWhenInjectingEnabled))
			.as("Power factor")
			.returns(1.0f, from(DerAcControlsModelAccessor::getPowerFactorWhenInjecting))
			.as("Excitation")
			.returns(DerPowerFactorExcitation.UnderExcited,
					from(DerAcControlsModelAccessor::getPowerFactorExcitationWhenInjecting))
			.as("Reversion enabled not implemented")
			.returns(null, from(DerAcControlsModelAccessor::isPowerFactorWhenInjectingReversionEnabled))
			.as("Reversion time not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getPowerFactorWhenInjectingReversionTime))
			.as("Reversion time remaining not implemented")
			.returns(null,
					from(DerAcControlsModelAccessor::getPowerFactorWhenInjectingReversionTimeRemaining))
			.as("Reversion power factor not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getReversionPowerFactorWhenInjecting))
			.as("Reversion excitation not implemented")
			.returns(null,
					from(DerAcControlsModelAccessor::getReversionPowerFactorExcitationWhenInjecting))
			;
		// @formatter:on
	}

	@Test
	public void powerFactorWhenAbsorbing() {
		// GIVEN
		DerAcControlsModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Enabled")
			.returns(true, from(DerAcControlsModelAccessor::isPowerFactorWhenAbsorbingEnabled))
			.as("Power factor")
			.returns(1.0f, from(DerAcControlsModelAccessor::getPowerFactorWhenAbsorbing))
			.as("Excitation")
			.returns(DerPowerFactorExcitation.OverExcited,
					from(DerAcControlsModelAccessor::getPowerFactorExcitationWhenAbsorbing))
			.as("Reversion enabled not implemented")
			.returns(null, from(DerAcControlsModelAccessor::isPowerFactorWhenAbsorbingReversionEnabled))
			.as("Reversion time not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getPowerFactorWhenAbsorbingReversionTime))
			.as("Reversion power factor not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getReversionPowerFactorWhenAbsorbing))
			;
		// @formatter:on
	}

	@Test
	public void activePowerLimit() {
		// GIVEN
		DerAcControlsModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Enabled")
			.returns(true, from(DerAcControlsModelAccessor::isActivePowerLimitEnabled))
			.as("Limit")
			.returns(100.0f, from(DerAcControlsModelAccessor::getActivePowerLimitPercent))
			.as("Reversion limit not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getReversionActivePowerLimitPercent))
			.as("Reversion enabled not implemented")
			.returns(null, from(DerAcControlsModelAccessor::isActivePowerLimitReversionEnabled))
			.as("Reversion time not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getActivePowerLimitReversionTime))
			.as("Reversion time remaining not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getActivePowerLimitReversionTimeRemaining))
			;
		// @formatter:on
	}

	@Test
	public void activePowerSetpoint() {
		// GIVEN
		DerAcControlsModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Enabled")
			.returns(false, from(DerAcControlsModelAccessor::isActivePowerSetpointEnabled))
			.as("Mode")
			.returns(DerActivePowerSetpointMode.MaximumActivePowerPercent,
					from(DerAcControlsModelAccessor::getActivePowerSetpointMode))
			.as("Setpoint not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getActivePowerSetpoint))
			.as("Reversion setpoint not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getReversionActivePowerSetpoint))
			.as("Setpoint percent")
			.returns(-100.0f, from(DerAcControlsModelAccessor::getActivePowerSetpointPercent))
			.as("Reversion setpoint percent not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getReversionActivePowerSetpointPercent))
			.as("Reversion enabled not implemented")
			.returns(null, from(DerAcControlsModelAccessor::isActivePowerSetpointReversionEnabled))
			.as("Reversion time not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getActivePowerSetpointReversionTime))
			;
		// @formatter:on
	}

	@Test
	public void reactivePowerSetpoint() {
		// GIVEN
		DerAcControlsModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Enabled")
			.returns(false, from(DerAcControlsModelAccessor::isReactivePowerSetpointEnabled))
			.as("Mode")
			.returns(DerReactivePowerSetpointMode.MaximumActivePowerPercent,
					from(DerAcControlsModelAccessor::getReactivePowerSetpointMode))
			.as("Priority")
			.returns(DerReactivePowerPriority.ReactivePower,
					from(DerAcControlsModelAccessor::getReactivePowerPriority))
			.as("Setpoint not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getReactivePowerSetpoint))
			.as("Reversion setpoint not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getReversionReactivePowerSetpoint))
			.as("Setpoint percent")
			.returns(100.0f, from(DerAcControlsModelAccessor::getReactivePowerSetpointPercent))
			.as("Reversion setpoint percent not implemented")
			.returns(null, from(DerAcControlsModelAccessor::getReversionReactivePowerSetpointPercent))
			.as("Reversion enabled not implemented")
			.returns(null, from(DerAcControlsModelAccessor::isReactivePowerSetpointReversionEnabled))
			.as("Reversion time remaining not implemented")
			.returns(null,
					from(DerAcControlsModelAccessor::getReactivePowerSetpointReversionTimeRemaining))
			;
		// @formatter:on
	}

	@Test
	public void rampRatesAndAntiIslanding() {
		// GIVEN
		DerAcControlsModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Active power ramp rate")
			.returns(1000, from(DerAcControlsModelAccessor::getActivePowerRampRate))
			.as("Active power ramp rate reference")
			.returns(DerRampRateReference.MaximumActivePower,
					from(DerAcControlsModelAccessor::getActivePowerRampRateReference))
			.as("Reactive power ramp rate")
			.returns(0, from(DerAcControlsModelAccessor::getReactivePowerRampRate))
			.as("Anti-islanding enabled")
			.returns(true, from(DerAcControlsModelAccessor::isAntiIslandingEnabled))
			;
		// @formatter:on
	}

	@Test
	public void writePowerFactor_singleRequest() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerAcControlsModelAccessor model = discoverModel(conn);

		// WHEN
		model.setPowerFactorWhenInjecting(conn, 0.95f, DerPowerFactorExcitation.OverExcited);

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Power factor and excitation written together in one request")
			.isEqualTo(List.of(List.of(BLOCK_ADDRESS + 57, 2)))
			;
		// @formatter:on

		DerAcControlsModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Power factor")
			.returns(0.95f, from(DerAcControlsModelAccessor::getPowerFactorWhenInjecting))
			.as("Excitation")
			.returns(DerPowerFactorExcitation.OverExcited,
					from(DerAcControlsModelAccessor::getPowerFactorExcitationWhenInjecting))
			.as("Absorbing power factor unchanged")
			.returns(1.0f, from(DerAcControlsModelAccessor::getPowerFactorWhenAbsorbing))
			;
		// @formatter:on
	}

	@Test
	public void writeReversionPowerFactors() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerAcControlsModelAccessor model = discoverModel(conn);

		// WHEN
		model.setReversionPowerFactorWhenInjecting(conn, 0.9f, DerPowerFactorExcitation.OverExcited);
		model.setPowerFactorWhenAbsorbing(conn, 0.85f, DerPowerFactorExcitation.UnderExcited);
		model.setReversionPowerFactorWhenAbsorbing(conn, 0.8f, DerPowerFactorExcitation.OverExcited);

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Each pair written in one request")
			.isEqualTo(List.of(List.of(BLOCK_ADDRESS + 59, 2), List.of(BLOCK_ADDRESS + 61, 2),
					List.of(BLOCK_ADDRESS + 63, 2)))
			;
		// @formatter:on

		DerAcControlsModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Reversion power factor injecting")
			.returns(0.9f, from(DerAcControlsModelAccessor::getReversionPowerFactorWhenInjecting))
			.as("Reversion excitation injecting")
			.returns(DerPowerFactorExcitation.OverExcited,
					from(DerAcControlsModelAccessor::getReversionPowerFactorExcitationWhenInjecting))
			.as("Power factor absorbing")
			.returns(0.85f, from(DerAcControlsModelAccessor::getPowerFactorWhenAbsorbing))
			.as("Excitation absorbing")
			.returns(DerPowerFactorExcitation.UnderExcited,
					from(DerAcControlsModelAccessor::getPowerFactorExcitationWhenAbsorbing))
			.as("Reversion power factor absorbing")
			.returns(0.8f, from(DerAcControlsModelAccessor::getReversionPowerFactorWhenAbsorbing))
			.as("Reversion excitation absorbing")
			.returns(DerPowerFactorExcitation.OverExcited,
					from(DerAcControlsModelAccessor::getReversionPowerFactorExcitationWhenAbsorbing))
			;
		// @formatter:on
	}

	@Test
	public void writePowerFactor_outOfRange() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerAcControlsModelAccessor model = discoverModel(conn);

		// WHEN
		Throwable t = catchThrowable(() -> model.setPowerFactorWhenInjecting(conn, 70f,
				DerPowerFactorExcitation.OverExcited));

		// THEN
		// @formatter:off
		then(t)
			.as("Scaled value larger than uint16 rejected")
			.isInstanceOf(IllegalArgumentException.class)
			;
		then(conn.getWrites())
			.as("Nothing written")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void writeSettings() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerAcControlsModelAccessor model = discoverModel(conn);

		// WHEN
		model.setPowerFactorWhenInjectingEnabled(conn, false);
		model.setPowerFactorWhenInjectingReversionEnabled(conn, true);
		model.setPowerFactorWhenInjectingReversionTime(conn, 300);
		model.setPowerFactorWhenAbsorbingEnabled(conn, false);
		model.setPowerFactorWhenAbsorbingReversionTime(conn, 600);
		model.setActivePowerLimitEnabled(conn, false);
		model.setActivePowerLimitPercent(conn, 80.5f);
		model.setReversionActivePowerLimitPercent(conn, 100f);
		model.setActivePowerLimitReversionEnabled(conn, true);
		model.setActivePowerLimitReversionTime(conn, 900);
		model.setActivePowerSetpointEnabled(conn, true);
		model.setActivePowerSetpointMode(conn, DerActivePowerSetpointMode.Watts);
		model.setActivePowerSetpoint(conn, -2500);
		model.setReversionActivePowerSetpoint(conn, 0);
		model.setActivePowerSetpointPercent(conn, 50.5f);
		model.setReversionActivePowerSetpointPercent(conn, -25f);
		model.setActivePowerSetpointReversionEnabled(conn, true);
		model.setActivePowerSetpointReversionTime(conn, 60);
		model.setReactivePowerSetpointEnabled(conn, true);
		model.setReactivePowerSetpointMode(conn,
				DerReactivePowerSetpointMode.MaximumApparentPowerPercent);
		model.setReactivePowerPriority(conn, DerReactivePowerPriority.ActivePower);
		model.setReactivePowerSetpointPercent(conn, -30.5f);
		model.setReversionReactivePowerSetpointPercent(conn, 0f);
		model.setReactivePowerSetpointReversionEnabled(conn, false);
		model.setReactivePowerSetpointReversionTime(conn, 120);
		model.setActivePowerRampRate(conn, 20);
		model.setActivePowerRampRateReference(conn, DerRampRateReference.MaximumCurrent);
		model.setReactivePowerRampRate(conn, 15);
		model.setAntiIslandingEnabled(conn, false);

		// THEN
		DerAcControlsModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("PF injecting enabled")
			.returns(false, from(DerAcControlsModelAccessor::isPowerFactorWhenInjectingEnabled))
			.as("PF injecting reversion enabled")
			.returns(true, from(DerAcControlsModelAccessor::isPowerFactorWhenInjectingReversionEnabled))
			.as("PF injecting reversion time")
			.returns(300L, from(DerAcControlsModelAccessor::getPowerFactorWhenInjectingReversionTime))
			.as("PF injecting reversion time remaining unchanged")
			.returns(null,
					from(DerAcControlsModelAccessor::getPowerFactorWhenInjectingReversionTimeRemaining))
			.as("PF absorbing enabled")
			.returns(false, from(DerAcControlsModelAccessor::isPowerFactorWhenAbsorbingEnabled))
			.as("PF absorbing reversion time")
			.returns(600L, from(DerAcControlsModelAccessor::getPowerFactorWhenAbsorbingReversionTime))
			.as("Limit enabled")
			.returns(false, from(DerAcControlsModelAccessor::isActivePowerLimitEnabled))
			.as("Limit")
			.returns(80.5f, from(DerAcControlsModelAccessor::getActivePowerLimitPercent))
			.as("Reversion limit")
			.returns(100.0f, from(DerAcControlsModelAccessor::getReversionActivePowerLimitPercent))
			.as("Limit reversion enabled")
			.returns(true, from(DerAcControlsModelAccessor::isActivePowerLimitReversionEnabled))
			.as("Limit reversion time")
			.returns(900L, from(DerAcControlsModelAccessor::getActivePowerLimitReversionTime))
			.as("Set active power enabled")
			.returns(true, from(DerAcControlsModelAccessor::isActivePowerSetpointEnabled))
			.as("Set active power mode")
			.returns(DerActivePowerSetpointMode.Watts,
					from(DerAcControlsModelAccessor::getActivePowerSetpointMode))
			.as("Active power setpoint")
			.returns(-2500, from(DerAcControlsModelAccessor::getActivePowerSetpoint))
			.as("Reversion active power setpoint")
			.returns(0, from(DerAcControlsModelAccessor::getReversionActivePowerSetpoint))
			.as("Active power setpoint percent")
			.returns(50.5f, from(DerAcControlsModelAccessor::getActivePowerSetpointPercent))
			.as("Reversion active power setpoint percent")
			.returns(-25.0f, from(DerAcControlsModelAccessor::getReversionActivePowerSetpointPercent))
			.as("Set active power reversion enabled")
			.returns(true, from(DerAcControlsModelAccessor::isActivePowerSetpointReversionEnabled))
			.as("Set active power reversion time")
			.returns(60L, from(DerAcControlsModelAccessor::getActivePowerSetpointReversionTime))
			.as("Set reactive power enabled")
			.returns(true, from(DerAcControlsModelAccessor::isReactivePowerSetpointEnabled))
			.as("Set reactive power mode")
			.returns(DerReactivePowerSetpointMode.MaximumApparentPowerPercent,
					from(DerAcControlsModelAccessor::getReactivePowerSetpointMode))
			.as("Reactive power priority")
			.returns(DerReactivePowerPriority.ActivePower,
					from(DerAcControlsModelAccessor::getReactivePowerPriority))
			.as("Reactive power setpoint percent")
			.returns(-30.5f, from(DerAcControlsModelAccessor::getReactivePowerSetpointPercent))
			.as("Reversion reactive power setpoint percent")
			.returns(0.0f, from(DerAcControlsModelAccessor::getReversionReactivePowerSetpointPercent))
			.as("Set reactive power reversion enabled")
			.returns(false, from(DerAcControlsModelAccessor::isReactivePowerSetpointReversionEnabled))
			.as("Set reactive power reversion time")
			.returns(120L, from(DerAcControlsModelAccessor::getReactivePowerSetpointReversionTime))
			.as("Active power ramp rate")
			.returns(20, from(DerAcControlsModelAccessor::getActivePowerRampRate))
			.as("Active power ramp rate reference")
			.returns(DerRampRateReference.MaximumCurrent,
					from(DerAcControlsModelAccessor::getActivePowerRampRateReference))
			.as("Reactive power ramp rate")
			.returns(15, from(DerAcControlsModelAccessor::getReactivePowerRampRate))
			.as("Anti-islanding enabled")
			.returns(false, from(DerAcControlsModelAccessor::isAntiIslandingEnabled))
			;
		// @formatter:on
	}

	@Test
	public void writeReactivePowerSetpoint_scaleFactorNotImplemented() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerAcControlsModelAccessor model = discoverModel(conn);

		// WHEN
		Throwable t = catchThrowable(() -> model.setReactivePowerSetpoint(conn, 1000));

		// THEN
		// @formatter:off
		then(t)
			.as("Not implemented VarSet_SF scale factor prevents writing")
			.isInstanceOf(IllegalStateException.class)
			;
		then(conn.getWrites())
			.as("Nothing written")
			.isEmpty()
			;
		// @formatter:on
	}

}
