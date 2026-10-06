/* ==================================================================
 * LithiumIonStringModelAccessorImpl_804_01Tests.java - 5/10/2026 9:40:12 pm
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
import net.solarnetwork.sunspec.api.storage.BatteryEnableOperation;
import net.solarnetwork.sunspec.api.storage.BatteryOperation;
import net.solarnetwork.sunspec.api.storage.LithiumIonStringEvent;
import net.solarnetwork.sunspec.api.storage.LithiumIonStringModelAccessor;
import net.solarnetwork.sunspec.api.storage.StorageModelId;
import net.solarnetwork.sunspec.api.storage.LithiumIonStringModelAccessor.BatteryModule;
import net.solarnetwork.sunspec.core.storage.LithiumIonStringModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * Test cases for the {@link LithiumIonStringModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class LithiumIonStringModelAccessorImpl_804_01Tests {

	private static final String TEST_DATA = "test-data-804-01.txt";

	private LithiumIonStringModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(LithiumIonStringModelAccessor.class);
	}

	private static LithiumIonStringModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(LithiumIonStringModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(LithiumIonStringModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		LithiumIonStringModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(LithiumIonStringModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(72, from(LithiumIonStringModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(StorageModelId.LithiumIonString, from(LithiumIonStringModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(46, from(LithiumIonStringModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(16, from(LithiumIonStringModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(94, from(LithiumIonStringModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void string() {
		// GIVEN
		LithiumIonStringModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("String index")
			.returns(1, from(LithiumIonStringModelAccessor::getStringIndex))
			.as("Module count")
			.returns(3, from(LithiumIonStringModelAccessor::getModuleCount))
			.as("Status")
			.returns(Set.of(BatteryConnectionStatus.Enabled, BatteryConnectionStatus.ContactorClosed),
					from(LithiumIonStringModelAccessor::getStatus))
			.as("Connection failure")
			.returns(BatteryConnectionFailure.None,
					from(LithiumIonStringModelAccessor::getConnectionFailure))
			.as("Balancing cell count")
			.returns(2, from(LithiumIonStringModelAccessor::getBalancingCellCount))
			.as("State of charge")
			.returns(76.5f, from(LithiumIonStringModelAccessor::getStateOfCharge))
			.as("Depth of discharge")
			.returns(23.5f, from(LithiumIonStringModelAccessor::getDepthOfDischarge))
			.as("Cycle count")
			.returns(145L, from(LithiumIonStringModelAccessor::getCycleCount))
			.as("State of health")
			.returns(97.0f, from(LithiumIonStringModelAccessor::getStateOfHealth))
			.as("DC current")
			.returns(12.3f, from(LithiumIonStringModelAccessor::getDCCurrent))
			.as("DC voltage")
			.returns(52.4f, from(LithiumIonStringModelAccessor::getDCVoltage))
			.as("Maximum cell voltage")
			.returns(3.352f, from(LithiumIonStringModelAccessor::getMaximumCellVoltage))
			.as("Maximum cell voltage module")
			.returns(2, from(LithiumIonStringModelAccessor::getMaximumCellVoltageModuleIndex))
			.as("Minimum cell voltage")
			.returns(3.301f, from(LithiumIonStringModelAccessor::getMinimumCellVoltage))
			.as("Minimum cell voltage module")
			.returns(3, from(LithiumIonStringModelAccessor::getMinimumCellVoltageModuleIndex))
			.as("Average cell voltage")
			.returns(3.33f, from(LithiumIonStringModelAccessor::getAverageCellVoltage))
			.as("Maximum module temperature")
			.returns(28.5f, from(LithiumIonStringModelAccessor::getMaximumModuleTemperature))
			.as("Maximum module temperature module")
			.returns(2, from(LithiumIonStringModelAccessor::getMaximumModuleTemperatureModuleIndex))
			.as("Minimum module temperature")
			.returns(24.0f, from(LithiumIonStringModelAccessor::getMinimumModuleTemperature))
			.as("Minimum module temperature module")
			.returns(1, from(LithiumIonStringModelAccessor::getMinimumModuleTemperatureModuleIndex))
			.as("Average module temperature")
			.returns(26.2f, from(LithiumIonStringModelAccessor::getAverageModuleTemperature))
			.as("Closed contactors")
			.returns(Set.of(0), from(LithiumIonStringModelAccessor::getClosedContactors))
			.as("Events")
			.returns(Set.of(LithiumIonStringEvent.OverVoltageWarning),
					from(LithiumIonStringModelAccessor::getEvents))
			.as("Vendor events")
			.returns(new BitSet(), from(LithiumIonStringModelAccessor::getVendorEvents))
			.as("Enable operation in progress")
			.returns(BatteryEnableOperation.Enable,
					from(LithiumIonStringModelAccessor::getEnableOperation))
			.as("No connect operation in progress")
			.returns(null, from(LithiumIonStringModelAccessor::getConnectOperation))
			;
		// @formatter:on
	}

	@Test
	public void modules() {
		// WHEN
		List<BatteryModule> modules = getTestModel().getModules();

		// THEN
		// @formatter:off
		then(modules)
			.as("Modules")
			.hasSize(3)
			;
		// @formatter:on

		BatteryModule module = modules.get(0);
		// @formatter:off
		then(module)
			.as("Index")
			.returns(1, from(BatteryModule::getIndex))
			.as("Cell count")
			.returns(16, from(BatteryModule::getCellCount))
			.as("State of charge")
			.returns(77.0f, from(BatteryModule::getStateOfCharge))
			.as("State of health")
			.returns(98.0f, from(BatteryModule::getStateOfHealth))
			.as("Maximum cell voltage")
			.returns(3.345f, from(BatteryModule::getMaximumCellVoltage))
			.as("Maximum cell voltage cell")
			.returns(5, from(BatteryModule::getMaximumCellVoltageCellIndex))
			.as("Minimum cell voltage")
			.returns(3.31f, from(BatteryModule::getMinimumCellVoltage))
			.as("Minimum cell voltage cell")
			.returns(12, from(BatteryModule::getMinimumCellVoltageCellIndex))
			.as("Average cell voltage")
			.returns(3.33f, from(BatteryModule::getAverageCellVoltage))
			.as("Maximum cell temperature")
			.returns(27.0f, from(BatteryModule::getMaximumCellTemperature))
			.as("Maximum cell temperature cell")
			.returns(1, from(BatteryModule::getMaximumCellTemperatureCellIndex))
			.as("Minimum cell temperature")
			.returns(24.0f, from(BatteryModule::getMinimumCellTemperature))
			.as("Minimum cell temperature cell")
			.returns(16, from(BatteryModule::getMinimumCellTemperatureCellIndex))
			.as("Average cell temperature")
			.returns(25.5f, from(BatteryModule::getAverageCellTemperature))
			;
		// @formatter:on

		BatteryModule module3 = modules.get(2);
		// @formatter:off
		then(module3)
			.as("Module 3 index")
			.returns(3, from(BatteryModule::getIndex))
			.as("Module 3 state of charge not implemented")
			.returns(null, from(BatteryModule::getStateOfCharge))
			.as("Module 3 state of health not implemented")
			.returns(null, from(BatteryModule::getStateOfHealth))
			.as("Module 3 maximum cell voltage")
			.returns(3.34f, from(BatteryModule::getMaximumCellVoltage))
			;
		// @formatter:on
	}

	@Test
	public void writeStringOperations() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		LithiumIonStringModelAccessor model = discoverModel(conn);

		// WHEN
		model.setEnableOperation(conn, BatteryEnableOperation.Disable);
		model.setConnectOperation(conn, BatteryOperation.Disconnect);

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Writes")
			.isEqualTo(List.of(List.of(72 + 34, 1), List.of(72 + 35, 1)))
			;
		// @formatter:on
		LithiumIonStringModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Enable operation")
			.returns(BatteryEnableOperation.Disable,
					from(LithiumIonStringModelAccessor::getEnableOperation))
			.as("Connect operation")
			.returns(BatteryOperation.Disconnect,
					from(LithiumIonStringModelAccessor::getConnectOperation))
			;
		// @formatter:on
	}

}
