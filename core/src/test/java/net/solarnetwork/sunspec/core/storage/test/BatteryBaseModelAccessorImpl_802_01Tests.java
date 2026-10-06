/* ==================================================================
 * BatteryBaseModelAccessorImpl_802_01Tests.java - 5/10/2026 8:21:46 pm
 *
 * Copyright 2026 SolarNetwork.net Dev Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * ==================================================================
 */

package net.solarnetwork.sunspec.core.storage.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.time.LocalDate;
import java.util.BitSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.der.DerLocalRemoteControl;
import net.solarnetwork.sunspec.api.storage.BatteryBaseModelAccessor;
import net.solarnetwork.sunspec.api.storage.BatteryChargeStatus;
import net.solarnetwork.sunspec.api.storage.BatteryEvent;
import net.solarnetwork.sunspec.api.storage.BatteryInverterState;
import net.solarnetwork.sunspec.api.storage.BatteryInverterStateRequest;
import net.solarnetwork.sunspec.api.storage.BatteryOperation;
import net.solarnetwork.sunspec.api.storage.BatteryState;
import net.solarnetwork.sunspec.api.storage.BatteryType;
import net.solarnetwork.sunspec.api.storage.StorageModelId;
import net.solarnetwork.sunspec.core.storage.BatteryBaseModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * Test cases for the {@link BatteryBaseModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class BatteryBaseModelAccessorImpl_802_01Tests {

	private static final String TEST_DATA = "test-data-802-01.txt";

	/** The model block address. */
	private static final int BLOCK_ADDRESS = 72;

	private BatteryBaseModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(BatteryBaseModelAccessor.class);
	}

	private BatteryBaseModelAccessor getTestModel(int address, int... words) {
		return ModelDataUtils.getModelDataInstanceWithRegisters(getClass(), TEST_DATA, address, words)
				.findTypedModel(BatteryBaseModelAccessor.class);
	}

	private static BatteryBaseModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn).findTypedModel(BatteryBaseModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(BatteryBaseModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		BatteryBaseModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(BatteryBaseModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BLOCK_ADDRESS, from(BatteryBaseModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(StorageModelId.BatteryBase, from(BatteryBaseModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(62, from(BatteryBaseModelAccessor::getFixedBlockLength))
			.as("Model length")
			.returns(62, from(BatteryBaseModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void ratings() {
		// GIVEN
		BatteryBaseModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Charge capacity")
			.returns(100.0f, from(BatteryBaseModelAccessor::getChargeCapacityRating))
			.as("Energy capacity")
			.returns(13500L, from(BatteryBaseModelAccessor::getEnergyCapacityRating))
			.as("Maximum charge rate")
			.returns(5000, from(BatteryBaseModelAccessor::getChargeRateMaximumRating))
			.as("Maximum discharge rate")
			.returns(7000, from(BatteryBaseModelAccessor::getDischargeRateMaximumRating))
			.as("Self discharge rate")
			.returns(0.5f, from(BatteryBaseModelAccessor::getSelfDischargeRate))
			.as("Maximum state of charge")
			.returns(100.0f, from(BatteryBaseModelAccessor::getStateOfChargeMaximumRating))
			.as("Minimum state of charge")
			.returns(5.0f, from(BatteryBaseModelAccessor::getStateOfChargeMinimumRating))
			.as("Maximum reserve")
			.returns(95.0f, from(BatteryBaseModelAccessor::getStateOfChargeReserveMaximum))
			.as("Minimum reserve")
			.returns(10.0f, from(BatteryBaseModelAccessor::getStateOfChargeReserveMinimum))
			;
		// @formatter:on
	}

	@Test
	public void status() {
		// GIVEN
		BatteryBaseModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("State of charge")
			.returns(87.3f, from(BatteryBaseModelAccessor::getStateOfCharge))
			.as("Depth of discharge")
			.returns(12.7f, from(BatteryBaseModelAccessor::getDepthOfDischarge))
			.as("State of health")
			.returns(98.5f, from(BatteryBaseModelAccessor::getStateOfHealth))
			.as("Cycle count")
			.returns(312L, from(BatteryBaseModelAccessor::getCycleCount))
			.as("Charge status")
			.returns(BatteryChargeStatus.Discharging, from(BatteryBaseModelAccessor::getChargeStatus))
			.as("Local or remote control")
			.returns(DerLocalRemoteControl.Remote, from(BatteryBaseModelAccessor::getLocalRemoteControl))
			.as("Battery heartbeat")
			.returns(4242, from(BatteryBaseModelAccessor::getBatteryHeartbeat))
			.as("Controller heartbeat")
			.returns(4240, from(BatteryBaseModelAccessor::getControllerHeartbeat))
			.as("Alarm reset in progress")
			.returns(false, from(BatteryBaseModelAccessor::isAlarmResetInProgress))
			.as("Battery type")
			.returns(BatteryType.LithiumIon, from(BatteryBaseModelAccessor::getBatteryType))
			.as("Battery state")
			.returns(BatteryState.Connected, from(BatteryBaseModelAccessor::getBatteryState))
			.as("Vendor battery state not implemented")
			.returns(null, from(BatteryBaseModelAccessor::getVendorBatteryState))
			.as("Warranty date, 10000 days after 1 January 2000")
			.returns(LocalDate.of(2027, 5, 19), from(BatteryBaseModelAccessor::getWarrantyDate))
			;
		// @formatter:on
	}

	@Test
	public void events() {
		// GIVEN
		BatteryBaseModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model.getEvents())
			.as("Events")
			.isEqualTo(Set.of(BatteryEvent.OverTemperatureWarning, BatteryEvent.VoltageImbalanceWarning,
					BatteryEvent.Reserved1))
			;
		// @formatter:on
		BitSet vendorEvents = new BitSet();
		vendorEvents.set(0);
		vendorEvents.set(2);
		vendorEvents.set(32);
		// @formatter:off
		then(model.getVendorEvents())
			.as("Vendor events, with the second field offset by 32")
			.isEqualTo(vendorEvents)
			;
		// @formatter:on
	}

	@Test
	public void events_mostSignificantBit() {
		// GIVEN
		// Evt1 at offset 24 and EvtVnd2 at offset 30, each with the most significant bit set
		BatteryBaseModelAccessor model = getTestModel(BLOCK_ADDRESS + 24, 0x8000, 0x0004, 0, 0, 0,
				0x0005, 0x8000, 0x0001);

		// THEN
		// @formatter:off
		then(model.getEvents())
			.as("Events not implemented")
			.isEmpty()
			;
		// @formatter:on
		BitSet vendorEvents = new BitSet();
		vendorEvents.set(0);
		vendorEvents.set(2);
		// @formatter:off
		then(model.getVendorEvents())
			.as("Second vendor event field not implemented")
			.isEqualTo(vendorEvents)
			;
		// @formatter:on
	}

	@Test
	public void warrantyDate_notImplemented() {
		// GIVEN
		BatteryBaseModelAccessor model = getTestModel(BLOCK_ADDRESS + 22, 0xFFFF, 0xFFFF);

		// THEN
		// @formatter:off
		then(model.getWarrantyDate())
			.as("Warranty date not implemented")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void measurements() {
		// GIVEN
		BatteryBaseModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("DC voltage")
			.returns(51.2f, from(BatteryBaseModelAccessor::getDCVoltage))
			.as("Maximum voltage")
			.returns(57.6f, from(BatteryBaseModelAccessor::getMaximumVoltage))
			.as("Minimum voltage")
			.returns(44.8f, from(BatteryBaseModelAccessor::getMinimumVoltage))
			.as("Maximum cell voltage")
			.returns(3.35f, from(BatteryBaseModelAccessor::getMaximumCellVoltage))
			.as("Maximum cell voltage string")
			.returns(1, from(BatteryBaseModelAccessor::getMaximumCellVoltageStringIndex))
			.as("Maximum cell voltage module")
			.returns(4, from(BatteryBaseModelAccessor::getMaximumCellVoltageModuleIndex))
			.as("Minimum cell voltage")
			.returns(3.31f, from(BatteryBaseModelAccessor::getMinimumCellVoltage))
			.as("Minimum cell voltage string")
			.returns(2, from(BatteryBaseModelAccessor::getMinimumCellVoltageStringIndex))
			.as("Minimum cell voltage module")
			.returns(7, from(BatteryBaseModelAccessor::getMinimumCellVoltageModuleIndex))
			.as("Average cell voltage")
			.returns(3.33f, from(BatteryBaseModelAccessor::getAverageCellVoltage))
			.as("DC current")
			.returns(-45.6f, from(BatteryBaseModelAccessor::getDCCurrent))
			.as("Maximum charge current")
			.returns(100.0f, from(BatteryBaseModelAccessor::getMaximumChargeCurrent))
			.as("Maximum discharge current")
			.returns(150.0f, from(BatteryBaseModelAccessor::getMaximumDischargeCurrent))
			.as("DC power")
			.returns(-2335, from(BatteryBaseModelAccessor::getDCPower))
			;
		// @formatter:on
	}

	@Test
	public void requestsAndCommands() {
		// GIVEN
		BatteryBaseModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Inverter state request")
			.returns(BatteryInverterStateRequest.NoRequest,
					from(BatteryBaseModelAccessor::getInverterStateRequest))
			.as("Power request")
			.returns(0, from(BatteryBaseModelAccessor::getPowerRequest))
			.as("Operation")
			.returns(BatteryOperation.Connect, from(BatteryBaseModelAccessor::getOperation))
			.as("Inverter state")
			.returns(BatteryInverterState.Started, from(BatteryBaseModelAccessor::getInverterState))
			;
		// @formatter:on
	}

	@Test
	public void writeSettings() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		BatteryBaseModelAccessor model = discoverModel(conn);

		// WHEN
		model.setStateOfChargeReserveMaximum(conn, 90.5f);
		model.setStateOfChargeReserveMinimum(conn, 12.0f);
		model.setControllerHeartbeat(conn, 4241);
		model.resetAlarms(conn);
		model.setOperation(conn, BatteryOperation.Disconnect);
		model.setInverterState(conn, BatteryInverterState.Standby);

		// THEN
		BatteryBaseModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Maximum reserve")
			.returns(90.5f, from(BatteryBaseModelAccessor::getStateOfChargeReserveMaximum))
			.as("Minimum reserve")
			.returns(12.0f, from(BatteryBaseModelAccessor::getStateOfChargeReserveMinimum))
			.as("Controller heartbeat")
			.returns(4241, from(BatteryBaseModelAccessor::getControllerHeartbeat))
			.as("Alarm reset in progress")
			.returns(true, from(BatteryBaseModelAccessor::isAlarmResetInProgress))
			.as("Operation")
			.returns(BatteryOperation.Disconnect, from(BatteryBaseModelAccessor::getOperation))
			.as("Inverter state")
			.returns(BatteryInverterState.Standby, from(BatteryBaseModelAccessor::getInverterState))
			.as("Battery heartbeat unchanged")
			.returns(4242, from(BatteryBaseModelAccessor::getBatteryHeartbeat))
			;
		// @formatter:on
	}

}
