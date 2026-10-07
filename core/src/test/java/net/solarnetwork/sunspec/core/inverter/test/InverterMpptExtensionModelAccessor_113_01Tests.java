/* ==================================================================
 * InverterMpptExtensionModelAccessor_113_01Tests.java - 12/10/2019 7:23:08 am
 * 
 * Copyright 2019 SolarNetwork.net Dev Team
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

import static net.solarnetwork.sunspec.api.inverter.InverterOperatingState.Mppt;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.OperatingState;
import net.solarnetwork.sunspec.api.inverter.InverterMpptExtensionModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterMpptExtensionModelRegister;
import net.solarnetwork.sunspec.api.inverter.InverterMpptExtensionModelAccessor.DcModule;
import net.solarnetwork.sunspec.core.ModelDataFactory;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;
import net.solarnetwork.sunspec.modbus.support.StaticDataMapReadonlyModbusConnection;

/**
 * Test cases for {@link InverterMpptExtensionModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class InverterMpptExtensionModelAccessor_113_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(InverterMpptExtensionModelAccessor_113_01Tests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-113-01.txt");
	}

	@Test
	public void dataDebugString() {
		ModelData data = getTestDataInstance();
		log.debug("Got test data: " + data.dataDebugString());
	}

	@Test
	public void modelAccessor() {
		// GIVEN
		InverterMpptExtensionModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterMpptExtensionModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("InverterMpptExtensionModelAccessor available")
			.isNotNull()
			;
		// @formatter:on
	}

	@Test
	public void timestampPeriod() {
		// GIVEN
		InverterMpptExtensionModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterMpptExtensionModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getTimestampPeriod())
			.as("Timestamp period")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void events() {
		// GIVEN
		InverterMpptExtensionModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterMpptExtensionModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getEvents())
			.as("Events")
			.hasSize(0)
			;
		// @formatter:on
	}

	private void assertDcModule(String suffix, DcModule module, Integer id, String name, Long timestamp,
			Float current, BigDecimal energyDelivered, BigDecimal power, Float voltage,
			Float temperature, OperatingState state, Set<ModelEvent> events) {
		// @formatter:off
		then(module)
			.as("Module ID " + suffix)
			.returns(id, from(DcModule::getInputId))
			.as("Module input name " + suffix)
			.returns(name, from(DcModule::getInputName))
			.as("Module data timestamp " + suffix)
			.returns(timestamp, from(DcModule::getDataTimestamp))
			.as("Module current " + suffix)
			.returns(current, from(DcModule::getDCCurrent))
			.as("Module energy delivered " + suffix)
			.returns(energyDelivered, from(DcModule::getDCEnergyDelivered))
			.as("Module power " + suffix)
			.returns(power, from(DcModule::getDCPower))
			.as("Module voltage " + suffix)
			.returns(voltage, from(DcModule::getDCVoltage))
			.as("Module temperature " + suffix)
			.returns(temperature, from(DcModule::getTemperature))
			.as("Module operating state " + suffix)
			.returns(state, from(DcModule::getOperatingState))
			.as("Module events " + suffix)
			.returns(events, from(DcModule::getEvents))
			;
		// @formatter:on
	}

	@Test
	public void dcModules() {
		// GIVEN
		InverterMpptExtensionModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterMpptExtensionModelAccessor.class);

		// WHEN
		List<DcModule> modules = model.getDcModules();

		// THEN
		// @formatter:off
		then(modules)
			.as("DC modules")
			.hasSize(2)
			;
		// @formatter:on

		Set<ModelEvent> noEvents = Collections.emptySet();
		assertDcModule("1", modules.get(0), 1, "String 1", 623608619L, 0.15f, new BigDecimal("11937020"),
				new BigDecimal("65.95"), 439.7f, null, Mppt, noEvents);
		assertDcModule("2", modules.get(1), 2, "Not supported", null, null, null, null, null, null, null,
				noEvents);
	}

	@Test
	public void dcModules_powerScaleFactor() throws IOException {
		// GIVEN
		final int[] data = ModelDataUtils.parseTestData(getClass(), "test-data-113-01.txt");
		final int blockAddress = getTestDataInstance()
				.findTypedModel(InverterMpptExtensionModelAccessor.class).getBlockAddress();

		// change the power scale factor from -2 to -1, so it differs from the voltage scale factor
		data[blockAddress + InverterMpptExtensionModelRegister.ScaleFactorDcPower.getAddress()] = 0xFFFF;

		// WHEN
		InverterMpptExtensionModelAccessor model = ModelDataFactory.getInstance()
				.getModelData(new StaticDataMapReadonlyModbusConnection(data))
				.findTypedModel(InverterMpptExtensionModelAccessor.class);
		DcModule module = model.getDcModules().get(0);

		// THEN
		// @formatter:off
		then(module)
			.as("Module power uses power scale factor")
			.returns(new BigDecimal("659.5"), from(DcModule::getDCPower))
			.as("Module voltage uses voltage scale factor")
			.returns(439.7f, from(DcModule::getDCVoltage))
			;
		// @formatter:on
	}
}
