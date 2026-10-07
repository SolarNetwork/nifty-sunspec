/* ==================================================================
 * InverterBasicStorageControlsModelAccessorImplTests.java - 6/10/2026 10:36:02 am
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

package net.solarnetwork.sunspec.core.inverter.test;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.inverter.InverterBasicStorageControlsModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterChargeSource;
import net.solarnetwork.sunspec.api.inverter.InverterControlModelId;
import net.solarnetwork.sunspec.api.inverter.InverterStorageControlMode;
import net.solarnetwork.sunspec.api.storage.BatteryChargeStatus;
import net.solarnetwork.sunspec.core.inverter.InverterBasicStorageControlsModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * Test cases for the {@link InverterBasicStorageControlsModelAccessorImpl}
 * class.
 *
 * @author matt
 * @version 1.0
 */
public class InverterBasicStorageControlsModelAccessorImplTests {

	/** An SMA capture. */
	private static final String TEST_DATA = "test-data-103-05.txt";

	/** The model block address in the test data. */
	private static final int BLOCK_ADDRESS = 371;

	/** Synthetic values for the whole model block. */
	// @formatter:off
	private static final int[] SYNTHETIC_BLOCK = new int[] {
			0x1388, // WChaMax
			0x03E8, // WChaGra
			0x07D0, // WDisChaGra
			0x0003, // StorCtl_Mod
			0x0226, // VAChaMax
			0x0096, // MinRsvPct
			0x0355, // ChaState
			0x04D2, // StorAval
			0x1400, // InBatV
			0x0004, // ChaSt
			0x02EE, // OutWRte
			0xFF06, // InWRte
			0x003C, // InOutWRte_WinTms
			0x0258, // InOutWRte_RvrtTms
			0x001E, // InOutWRte_RmpTms
			0x0001, // ChaGriSet
			0x0000, // WChaMax_SF
			0xFFFE, // WChaDisChaGra_SF
			0x0001, // VAChaMax_SF
			0xFFFF, // MinRsvPct_SF
			0xFFFF, // ChaState_SF
			0xFFFF, // StorAval_SF
			0xFFFE, // InBatV_SF
			0xFFFF, // InOutWRte_SF
	};
	// @formatter:on

	private InverterBasicStorageControlsModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(InverterBasicStorageControlsModelAccessor.class);
	}

	private static InverterBasicStorageControlsModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(InverterBasicStorageControlsModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(InverterBasicStorageControlsModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		InverterBasicStorageControlsModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(369, from(InverterBasicStorageControlsModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BLOCK_ADDRESS, from(InverterBasicStorageControlsModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(InverterControlModelId.BasicStorageControls,
					from(InverterBasicStorageControlsModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(24, from(InverterBasicStorageControlsModelAccessor::getFixedBlockLength))
			.as("Model length")
			.returns(24, from(InverterBasicStorageControlsModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void values() {
		// GIVEN
		// the device implements the points, but has no storage
		InverterBasicStorageControlsModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Maximum charge rate not implemented")
			.returns(null,
					from(InverterBasicStorageControlsModelAccessor::getActivePowerChargeRateMaximum))
			.as("Charge ramp rate not implemented")
			.returns(null, from(InverterBasicStorageControlsModelAccessor::getChargeRampRate))
			.as("Discharge ramp rate not implemented")
			.returns(null, from(InverterBasicStorageControlsModelAccessor::getDischargeRampRate))
			.as("Storage control modes")
			.returns(Set.of(), from(InverterBasicStorageControlsModelAccessor::getStorageControlModes))
			.as("Maximum charge apparent power not implemented")
			.returns(null,
					from(InverterBasicStorageControlsModelAccessor::getApparentPowerChargeRateMaximum))
			.as("Minimum reserve not implemented")
			.returns(null,
					from(InverterBasicStorageControlsModelAccessor::getStateOfChargeReserveMinimum))
			.as("State of charge not implemented")
			.returns(null, from(InverterBasicStorageControlsModelAccessor::getStateOfCharge))
			.as("Storage available not implemented")
			.returns(null, from(InverterBasicStorageControlsModelAccessor::getStorageAvailable))
			.as("Battery voltage not implemented")
			.returns(null, from(InverterBasicStorageControlsModelAccessor::getBatteryVoltage))
			.as("Charge status not implemented")
			.returns(null, from(InverterBasicStorageControlsModelAccessor::getChargeStatus))
			.as("Discharge rate not implemented")
			.returns(null, from(InverterBasicStorageControlsModelAccessor::getDischargeRatePercent))
			.as("Charge rate not implemented")
			.returns(null, from(InverterBasicStorageControlsModelAccessor::getChargeRatePercent))
			.as("Rate time window not implemented")
			.returns(null,
					from(InverterBasicStorageControlsModelAccessor::getChargeDischargeRateTimeWindow))
			.as("Rate reversion time not implemented")
			.returns(null,
					from(InverterBasicStorageControlsModelAccessor::getChargeDischargeRateReversionTime))
			.as("Rate ramp time not implemented")
			.returns(null,
					from(InverterBasicStorageControlsModelAccessor::getChargeDischargeRateRampTime))
			.as("Charge source not implemented")
			.returns(null, from(InverterBasicStorageControlsModelAccessor::getChargeSource))
			;
		// @formatter:on
	}

	@Test
	public void syntheticValues() {
		// GIVEN
		InverterBasicStorageControlsModelAccessor model = ModelDataUtils
				.getModelDataInstanceWithRegisters(getClass(), TEST_DATA, BLOCK_ADDRESS, SYNTHETIC_BLOCK)
				.findTypedModel(InverterBasicStorageControlsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Maximum charge rate")
			.returns(new BigDecimal("5000"),
					from(InverterBasicStorageControlsModelAccessor::getActivePowerChargeRateMaximum))
			.as("Charge ramp rate")
			.returns(10.0f, from(InverterBasicStorageControlsModelAccessor::getChargeRampRate))
			.as("Discharge ramp rate")
			.returns(20.0f, from(InverterBasicStorageControlsModelAccessor::getDischargeRampRate))
			.as("Storage control modes")
			.returns(EnumSet.of(InverterStorageControlMode.Charge, InverterStorageControlMode.Discharge),
					from(InverterBasicStorageControlsModelAccessor::getStorageControlModes))
			.as("Maximum charge apparent power")
			.returns(new BigDecimal("5500"),
					from(InverterBasicStorageControlsModelAccessor::getApparentPowerChargeRateMaximum))
			.as("Minimum reserve")
			.returns(15.0f,
					from(InverterBasicStorageControlsModelAccessor::getStateOfChargeReserveMinimum))
			.as("State of charge")
			.returns(85.3f, from(InverterBasicStorageControlsModelAccessor::getStateOfCharge))
			.as("Storage available")
			.returns(new BigDecimal("123.4"), from(InverterBasicStorageControlsModelAccessor::getStorageAvailable))
			.as("Battery voltage")
			.returns(51.2f, from(InverterBasicStorageControlsModelAccessor::getBatteryVoltage))
			.as("Charge status")
			.returns(BatteryChargeStatus.Charging,
					from(InverterBasicStorageControlsModelAccessor::getChargeStatus))
			.as("Discharge rate")
			.returns(75.0f, from(InverterBasicStorageControlsModelAccessor::getDischargeRatePercent))
			.as("Charge rate")
			.returns(-25.0f, from(InverterBasicStorageControlsModelAccessor::getChargeRatePercent))
			.as("Rate time window")
			.returns(60,
					from(InverterBasicStorageControlsModelAccessor::getChargeDischargeRateTimeWindow))
			.as("Rate reversion time")
			.returns(600,
					from(InverterBasicStorageControlsModelAccessor::getChargeDischargeRateReversionTime))
			.as("Rate ramp time")
			.returns(30, from(InverterBasicStorageControlsModelAccessor::getChargeDischargeRateRampTime))
			.as("Charge source")
			.returns(InverterChargeSource.Grid,
					from(InverterBasicStorageControlsModelAccessor::getChargeSource))
			;
		// @formatter:on
	}

	@Test
	public void writeValues() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnectionWithRegisters(
				getClass(), TEST_DATA, BLOCK_ADDRESS, SYNTHETIC_BLOCK);
		InverterBasicStorageControlsModelAccessor model = discoverModel(conn);

		// WHEN
		model.setActivePowerChargeRateMaximum(conn, new BigDecimal("4000"));
		model.setChargeRampRate(conn, 12.5f);
		model.setDischargeRampRate(conn, 7.25f);
		model.setStorageControlModes(conn, EnumSet.of(InverterStorageControlMode.Discharge));
		model.setApparentPowerChargeRateMaximum(conn, new BigDecimal("4400"));
		model.setStateOfChargeReserveMinimum(conn, 20.5f);
		model.setDischargeRatePercent(conn, 33.3f);
		model.setChargeRatePercent(conn, -12.5f);
		model.setChargeDischargeRateTimeWindow(conn, 120);
		model.setChargeDischargeRateReversionTime(conn, 3600);
		model.setChargeDischargeRateRampTime(conn, 45);
		model.setChargeSource(conn, InverterChargeSource.Pv);

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Each point written to its own register")
			.isEqualTo(List.of(List.of(371, 1), List.of(372, 1), List.of(373, 1), List.of(374, 1),
					List.of(375, 1), List.of(376, 1), List.of(381, 1), List.of(382, 1), List.of(383, 1),
					List.of(384, 1), List.of(385, 1), List.of(386, 1)))
			;
		// @formatter:on

		InverterBasicStorageControlsModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Maximum charge rate")
			.returns(new BigDecimal("4000"),
					from(InverterBasicStorageControlsModelAccessor::getActivePowerChargeRateMaximum))
			.as("Charge ramp rate")
			.returns(12.5f, from(InverterBasicStorageControlsModelAccessor::getChargeRampRate))
			.as("Discharge ramp rate")
			.returns(7.25f, from(InverterBasicStorageControlsModelAccessor::getDischargeRampRate))
			.as("Storage control modes")
			.returns(EnumSet.of(InverterStorageControlMode.Discharge),
					from(InverterBasicStorageControlsModelAccessor::getStorageControlModes))
			.as("Maximum charge apparent power")
			.returns(new BigDecimal("4400"),
					from(InverterBasicStorageControlsModelAccessor::getApparentPowerChargeRateMaximum))
			.as("Minimum reserve")
			.returns(20.5f,
					from(InverterBasicStorageControlsModelAccessor::getStateOfChargeReserveMinimum))
			.as("Discharge rate")
			.returns(33.3f, from(InverterBasicStorageControlsModelAccessor::getDischargeRatePercent))
			.as("Charge rate")
			.returns(-12.5f, from(InverterBasicStorageControlsModelAccessor::getChargeRatePercent))
			.as("Rate time window")
			.returns(120,
					from(InverterBasicStorageControlsModelAccessor::getChargeDischargeRateTimeWindow))
			.as("Rate reversion time")
			.returns(3600,
					from(InverterBasicStorageControlsModelAccessor::getChargeDischargeRateReversionTime))
			.as("Rate ramp time")
			.returns(45, from(InverterBasicStorageControlsModelAccessor::getChargeDischargeRateRampTime))
			.as("Charge source")
			.returns(InverterChargeSource.Pv,
					from(InverterBasicStorageControlsModelAccessor::getChargeSource))
			.as("Read-only state of charge unchanged")
			.returns(85.3f, from(InverterBasicStorageControlsModelAccessor::getStateOfCharge))
			;
		// @formatter:on
	}

	@Test
	public void writeValues_noModes() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnectionWithRegisters(
				getClass(), TEST_DATA, BLOCK_ADDRESS, SYNTHETIC_BLOCK);
		InverterBasicStorageControlsModelAccessor model = discoverModel(conn);

		// WHEN
		model.setStorageControlModes(conn, Set.of());

		// THEN
		// @formatter:off
		then(discoverModel(conn).getStorageControlModes())
			.as("Storage control modes cleared")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void writeValue_scaleFactorNotImplemented() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		InverterBasicStorageControlsModelAccessor model = discoverModel(conn);

		// WHEN
		Throwable t = catchThrowable(() -> model.setChargeRampRate(conn, 10.0f));
		model.setActivePowerChargeRateMaximum(conn, new BigDecimal("4000"));

		// THEN
		// @formatter:off
		then(t)
			.as("Scaled value without an implemented scale factor rejected")
			.isInstanceOf(IllegalStateException.class)
			;
		then(conn.getWrites())
			.as("Only the point with an implemented scale factor written")
			.isEqualTo(List.of(List.of(371, 1)))
			;
		then(discoverModel(conn).getActivePowerChargeRateMaximum())
			.as("Maximum charge rate")
			.isEqualTo(new BigDecimal("4000"))
			;
		// @formatter:on
	}

}
