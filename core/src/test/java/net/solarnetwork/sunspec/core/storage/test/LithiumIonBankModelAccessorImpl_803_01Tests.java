/* ==================================================================
 * LithiumIonBankModelAccessorImpl_803_01Tests.java - 5/10/2026 9:40:12 pm
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

package net.solarnetwork.sunspec.core.storage.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.util.BitSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.storage.BatteryConnectionFailure;
import net.solarnetwork.sunspec.api.storage.BatteryConnectionStatus;
import net.solarnetwork.sunspec.api.storage.BatteryDisabledReason;
import net.solarnetwork.sunspec.api.storage.BatteryEnableOperation;
import net.solarnetwork.sunspec.api.storage.BatteryOperation;
import net.solarnetwork.sunspec.api.storage.LithiumIonBankModelAccessor;
import net.solarnetwork.sunspec.api.storage.LithiumIonStringEvent;
import net.solarnetwork.sunspec.api.storage.StorageModelId;
import net.solarnetwork.sunspec.api.storage.LithiumIonBankModelAccessor.BatteryString;
import net.solarnetwork.sunspec.core.storage.LithiumIonBankModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * Test cases for the {@link LithiumIonBankModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class LithiumIonBankModelAccessorImpl_803_01Tests {

	private static final String TEST_DATA = "test-data-803-01.txt";

	/** The model block address. */
	private static final int BLOCK_ADDRESS = 72;

	/**
	 * The string 2 address: 26 fixed registers, then 32 registers per string.
	 */
	private static final int STRING_2_ADDRESS = BLOCK_ADDRESS + 26 + 32;

	private LithiumIonBankModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(LithiumIonBankModelAccessor.class);
	}

	private static LithiumIonBankModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(LithiumIonBankModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(LithiumIonBankModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		LithiumIonBankModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(LithiumIonBankModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BLOCK_ADDRESS, from(LithiumIonBankModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(StorageModelId.LithiumIonBank, from(LithiumIonBankModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(26, from(LithiumIonBankModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(32, from(LithiumIonBankModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model repeating instance count")
			.returns(2, from(LithiumIonBankModelAccessor::getRepeatingBlockInstanceCount))
			.as("Model length")
			.returns(90, from(LithiumIonBankModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void bank() {
		// GIVEN
		LithiumIonBankModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("String count")
			.returns(2, from(LithiumIonBankModelAccessor::getStringCount))
			.as("Connected string count")
			.returns(1, from(LithiumIonBankModelAccessor::getConnectedStringCount))
			.as("Maximum module temperature")
			.returns(28.5f, from(LithiumIonBankModelAccessor::getMaximumModuleTemperature))
			.as("Maximum module temperature string")
			.returns(1, from(LithiumIonBankModelAccessor::getMaximumModuleTemperatureStringIndex))
			.as("Maximum module temperature module")
			.returns(3, from(LithiumIonBankModelAccessor::getMaximumModuleTemperatureModuleIndex))
			.as("Minimum module temperature")
			.returns(22.1f, from(LithiumIonBankModelAccessor::getMinimumModuleTemperature))
			.as("Minimum module temperature string")
			.returns(2, from(LithiumIonBankModelAccessor::getMinimumModuleTemperatureStringIndex))
			.as("Minimum module temperature module")
			.returns(1, from(LithiumIonBankModelAccessor::getMinimumModuleTemperatureModuleIndex))
			.as("Average module temperature")
			.returns(25.0f, from(LithiumIonBankModelAccessor::getAverageModuleTemperature))
			.as("Maximum string voltage")
			.returns(52.4f, from(LithiumIonBankModelAccessor::getMaximumStringVoltage))
			.as("Maximum string voltage string")
			.returns(1, from(LithiumIonBankModelAccessor::getMaximumStringVoltageStringIndex))
			.as("Minimum string voltage")
			.returns(51.8f, from(LithiumIonBankModelAccessor::getMinimumStringVoltage))
			.as("Minimum string voltage string")
			.returns(2, from(LithiumIonBankModelAccessor::getMinimumStringVoltageStringIndex))
			.as("Average string voltage")
			.returns(52.1f, from(LithiumIonBankModelAccessor::getAverageStringVoltage))
			.as("Maximum string current")
			.returns(12.3f, from(LithiumIonBankModelAccessor::getMaximumStringCurrent))
			.as("Maximum string current string")
			.returns(1, from(LithiumIonBankModelAccessor::getMaximumStringCurrentStringIndex))
			.as("Minimum string current")
			.returns(-1.5f, from(LithiumIonBankModelAccessor::getMinimumStringCurrent))
			.as("Minimum string current string")
			.returns(2, from(LithiumIonBankModelAccessor::getMinimumStringCurrentStringIndex))
			.as("Average string current")
			.returns(5.4f, from(LithiumIonBankModelAccessor::getAverageStringCurrent))
			.as("Balancing cell count")
			.returns(3, from(LithiumIonBankModelAccessor::getBalancingCellCount))
			;
		// @formatter:on
	}

	@Test
	public void connectedString() {
		// WHEN
		List<BatteryString> strings = getTestModel().getStrings();

		// THEN
		// @formatter:off
		then(strings)
			.as("Strings")
			.hasSize(2)
			;
		// @formatter:on

		BatteryString string = strings.get(0);
		// @formatter:off
		then(string)
			.as("Index")
			.returns(1, from(BatteryString::getIndex))
			.as("Module count")
			.returns(8, from(BatteryString::getModuleCount))
			.as("Status")
			.returns(Set.of(BatteryConnectionStatus.Enabled, BatteryConnectionStatus.ContactorClosed),
					from(BatteryString::getStatus))
			.as("Connection failure")
			.returns(BatteryConnectionFailure.None, from(BatteryString::getConnectionFailure))
			.as("State of charge")
			.returns(76.5f, from(BatteryString::getStateOfCharge))
			.as("State of health")
			.returns(97.0f, from(BatteryString::getStateOfHealth))
			.as("DC current")
			.returns(12.3f, from(BatteryString::getDCCurrent))
			.as("Maximum cell voltage")
			.returns(3.352f, from(BatteryString::getMaximumCellVoltage))
			.as("Maximum cell voltage module")
			.returns(3, from(BatteryString::getMaximumCellVoltageModuleIndex))
			.as("Minimum cell voltage")
			.returns(3.301f, from(BatteryString::getMinimumCellVoltage))
			.as("Minimum cell voltage module")
			.returns(6, from(BatteryString::getMinimumCellVoltageModuleIndex))
			.as("Average cell voltage")
			.returns(3.33f, from(BatteryString::getAverageCellVoltage))
			.as("Maximum module temperature")
			.returns(28.5f, from(BatteryString::getMaximumModuleTemperature))
			.as("Maximum module temperature module")
			.returns(3, from(BatteryString::getMaximumModuleTemperatureModuleIndex))
			.as("Minimum module temperature")
			.returns(24.0f, from(BatteryString::getMinimumModuleTemperature))
			.as("Minimum module temperature module")
			.returns(8, from(BatteryString::getMinimumModuleTemperatureModuleIndex))
			.as("Average module temperature")
			.returns(26.2f, from(BatteryString::getAverageModuleTemperature))
			.as("Disabled reason")
			.returns(BatteryDisabledReason.None, from(BatteryString::getDisabledReason))
			.as("Closed contactors")
			.returns(Set.of(0, 2), from(BatteryString::getClosedContactors))
			.as("Events, bit 24 reserved for strings")
			.returns(Set.of(LithiumIonStringEvent.Reserved1), from(BatteryString::getEvents))
			;
		// @formatter:on
		BitSet vendorEvents = new BitSet();
		vendorEvents.set(1);
		// @formatter:off
		then(string)
			.as("Vendor events")
			.returns(vendorEvents, from(BatteryString::getVendorEvents))
			.as("No enable operation in progress")
			.returns(null, from(BatteryString::getEnableOperation))
			.as("No connect operation in progress")
			.returns(null, from(BatteryString::getConnectOperation))
			;
		// @formatter:on
	}

	@Test
	public void disabledString() {
		// WHEN
		BatteryString string = getTestModel().getStrings().get(1);

		// THEN
		// @formatter:off
		then(string)
			.as("Index")
			.returns(2, from(BatteryString::getIndex))
			.as("Status")
			.returns(Set.of(), from(BatteryString::getStatus))
			.as("Connection failure")
			.returns(BatteryConnectionFailure.NotEnabled, from(BatteryString::getConnectionFailure))
			.as("State of charge not implemented")
			.returns(null, from(BatteryString::getStateOfCharge))
			.as("DC current")
			.returns(-1.5f, from(BatteryString::getDCCurrent))
			.as("Disabled reason")
			.returns(BatteryDisabledReason.External, from(BatteryString::getDisabledReason))
			.as("Closed contactors")
			.returns(Set.of(), from(BatteryString::getClosedContactors))
			.as("Events with the most significant bit set not implemented")
			.returns(Set.of(), from(BatteryString::getEvents))
			;
		// @formatter:on
		BitSet vendorEvents = new BitSet();
		vendorEvents.set(34);
		// @formatter:off
		then(string)
			.as("Vendor events, with the second field offset by 32")
			.returns(vendorEvents, from(BatteryString::getVendorEvents))
			.as("Enable operation not implemented")
			.returns(null, from(BatteryString::getEnableOperation))
			;
		// @formatter:on
	}

	@Test
	public void writeStringOperations() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		BatteryString string = discoverModel(conn).getStrings().get(1);

		// WHEN
		string.setEnableOperation(conn, BatteryEnableOperation.Enable);
		string.setConnectOperation(conn, BatteryOperation.Connect);

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Writes")
			.isEqualTo(List.of(List.of(STRING_2_ADDRESS + 28, 1), List.of(STRING_2_ADDRESS + 29, 1)))
			;
		// @formatter:on
		BatteryString device = discoverModel(conn).getStrings().get(1);
		// @formatter:off
		then(device)
			.as("Enable operation")
			.returns(BatteryEnableOperation.Enable, from(BatteryString::getEnableOperation))
			.as("Connect operation")
			.returns(BatteryOperation.Connect, from(BatteryString::getConnectOperation))
			;
		// @formatter:on
	}

}
