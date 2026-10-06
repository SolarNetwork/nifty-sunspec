/* ==================================================================
 * LithiumIonModuleModelAccessorImpl_805_01Tests.java - 5/10/2026 9:40:12 pm
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
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.storage.LithiumIonCellStatus;
import net.solarnetwork.sunspec.api.storage.LithiumIonModuleModelAccessor;
import net.solarnetwork.sunspec.api.storage.StorageModelId;
import net.solarnetwork.sunspec.api.storage.LithiumIonModuleModelAccessor.BatteryCell;
import net.solarnetwork.sunspec.core.storage.LithiumIonModuleModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;

/**
 * Test cases for the {@link LithiumIonModuleModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class LithiumIonModuleModelAccessorImpl_805_01Tests {

	private static final String TEST_DATA = "test-data-805-01.txt";

	private LithiumIonModuleModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(LithiumIonModuleModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(LithiumIonModuleModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		LithiumIonModuleModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(LithiumIonModuleModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(72, from(LithiumIonModuleModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(StorageModelId.LithiumIonModule, from(LithiumIonModuleModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(42, from(LithiumIonModuleModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(4, from(LithiumIonModuleModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(58, from(LithiumIonModuleModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void module() {
		// GIVEN
		LithiumIonModuleModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("String index")
			.returns(1, from(LithiumIonModuleModelAccessor::getStringIndex))
			.as("Module index")
			.returns(2, from(LithiumIonModuleModelAccessor::getModuleIndex))
			.as("Cell count")
			.returns(4, from(LithiumIonModuleModelAccessor::getCellCount))
			.as("State of charge")
			.returns(76.5f, from(LithiumIonModuleModelAccessor::getStateOfCharge))
			.as("Depth of discharge")
			.returns(23.5f, from(LithiumIonModuleModelAccessor::getDepthOfDischarge))
			.as("State of health")
			.returns(97.0f, from(LithiumIonModuleModelAccessor::getStateOfHealth))
			.as("Cycle count")
			.returns(145L, from(LithiumIonModuleModelAccessor::getCycleCount))
			.as("DC voltage")
			.returns(13.32f, from(LithiumIonModuleModelAccessor::getDCVoltage))
			.as("Maximum cell voltage")
			.returns(3.335f, from(LithiumIonModuleModelAccessor::getMaximumCellVoltage))
			.as("Maximum cell voltage cell")
			.returns(3, from(LithiumIonModuleModelAccessor::getMaximumCellVoltageCellIndex))
			.as("Minimum cell voltage")
			.returns(3.32f, from(LithiumIonModuleModelAccessor::getMinimumCellVoltage))
			.as("Minimum cell voltage cell")
			.returns(1, from(LithiumIonModuleModelAccessor::getMinimumCellVoltageCellIndex))
			.as("Average cell voltage")
			.returns(3.33f, from(LithiumIonModuleModelAccessor::getAverageCellVoltage))
			.as("Maximum cell temperature")
			.returns(26.2f, from(LithiumIonModuleModelAccessor::getMaximumCellTemperature))
			.as("Maximum cell temperature cell")
			.returns(2, from(LithiumIonModuleModelAccessor::getMaximumCellTemperatureCellIndex))
			.as("Minimum cell temperature")
			.returns(24.8f, from(LithiumIonModuleModelAccessor::getMinimumCellTemperature))
			.as("Minimum cell temperature cell")
			.returns(4, from(LithiumIonModuleModelAccessor::getMinimumCellTemperatureCellIndex))
			.as("Average cell temperature")
			.returns(25.5f, from(LithiumIonModuleModelAccessor::getAverageCellTemperature))
			.as("Balancing cell count")
			.returns(1, from(LithiumIonModuleModelAccessor::getBalancingCellCount))
			.as("Serial number")
			.returns("LIM-0002-ABC", from(LithiumIonModuleModelAccessor::getSerialNumber))
			;
		// @formatter:on
	}

	@Test
	public void cells() {
		// WHEN
		List<BatteryCell> cells = getTestModel().getCells();

		// THEN
		// @formatter:off
		then(cells)
			.as("Cells, from the model length")
			.hasSize(4)
			;
		// @formatter:on

		BatteryCell cell = cells.get(0);
		// @formatter:off
		then(cell)
			.as("Index")
			.returns(1, from(BatteryCell::getIndex))
			.as("Voltage")
			.returns(3.32f, from(BatteryCell::getVoltage))
			.as("Temperature")
			.returns(25.0f, from(BatteryCell::getTemperature))
			.as("Status")
			.returns(Set.of(), from(BatteryCell::getStatus))
			;

		then(cells.get(2).getStatus())
			.as("Cell 3 balancing")
			.isEqualTo(Set.of(LithiumIonCellStatus.Balancing))
			;
		then(cells.get(3).getStatus())
			.as("Cell 4 status with the most significant bit set not implemented")
			.isEmpty()
			;
		then(cells.get(3).getVoltage())
			.as("Cell 4 voltage")
			.isEqualTo(3.337f)
			;
		// @formatter:on
	}

}
