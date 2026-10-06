/* ==================================================================
 * DerEnterServiceModelAccessorImpl_703_01Tests.java - 5/10/2026 9:32:29 am
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
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.der.DerEnterServiceModelAccessor;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.core.der.DerEnterServiceModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.support.StaticDataMapModbusConnection;

/**
 * Test cases for the {@link DerEnterServiceModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class DerEnterServiceModelAccessorImpl_703_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	private DerEnterServiceModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerEnterServiceModelAccessor.class);
	}

	private static DerEnterServiceModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(DerEnterServiceModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerEnterServiceModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerEnterServiceModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(384, from(DerEnterServiceModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(386, from(DerEnterServiceModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.EnterService, from(DerEnterServiceModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(17, from(DerEnterServiceModelAccessor::getFixedBlockLength))
			.as("Model length")
			.returns(17, from(DerEnterServiceModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void values() {
		// GIVEN
		DerEnterServiceModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Permitted")
			.returns(true, from(DerEnterServiceModelAccessor::isEnterServicePermitted))
			.as("Voltage high")
			.returns(106.0f, from(DerEnterServiceModelAccessor::getVoltageHigh))
			.as("Voltage low")
			.returns(95.0f, from(DerEnterServiceModelAccessor::getVoltageLow))
			.as("Frequency high")
			.returns(61.0f, from(DerEnterServiceModelAccessor::getFrequencyHigh))
			.as("Frequency low")
			.returns(59.9f, from(DerEnterServiceModelAccessor::getFrequencyLow))
			.as("Delay")
			.returns(600L, from(DerEnterServiceModelAccessor::getDelay))
			.as("Random delay")
			.returns(1000L, from(DerEnterServiceModelAccessor::getRandomDelay))
			.as("Ramp time")
			.returns(1000L, from(DerEnterServiceModelAccessor::getRampTime))
			.as("Delay remaining")
			.returns(0L, from(DerEnterServiceModelAccessor::getDelayRemaining))
			;
		// @formatter:on
	}

	@Test
	public void writeValues() throws IOException {
		// GIVEN
		StaticDataMapModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		DerEnterServiceModelAccessor model = discoverModel(conn);

		// WHEN
		model.setEnterServicePermitted(conn, false);
		model.setVoltageHigh(conn, 105.5f);
		model.setVoltageLow(conn, 91.7f);
		model.setFrequencyHigh(conn, 60.5f);
		model.setFrequencyLow(conn, 59.55f);
		model.setDelay(conn, 300);
		model.setRandomDelay(conn, 120);
		model.setRampTime(conn, 60);

		// THEN
		// @formatter:off
		then(model.isEnterServicePermitted())
			.as("Model data updated")
			.isFalse()
			;
		// @formatter:on

		DerEnterServiceModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Permitted")
			.returns(false, from(DerEnterServiceModelAccessor::isEnterServicePermitted))
			.as("Voltage high")
			.returns(105.5f, from(DerEnterServiceModelAccessor::getVoltageHigh))
			.as("Voltage low")
			.returns(91.7f, from(DerEnterServiceModelAccessor::getVoltageLow))
			.as("Frequency high")
			.returns(60.5f, from(DerEnterServiceModelAccessor::getFrequencyHigh))
			.as("Frequency low")
			.returns(59.55f, from(DerEnterServiceModelAccessor::getFrequencyLow))
			.as("Delay")
			.returns(300L, from(DerEnterServiceModelAccessor::getDelay))
			.as("Random delay")
			.returns(120L, from(DerEnterServiceModelAccessor::getRandomDelay))
			.as("Ramp time")
			.returns(60L, from(DerEnterServiceModelAccessor::getRampTime))
			.as("Delay remaining unchanged")
			.returns(0L, from(DerEnterServiceModelAccessor::getDelayRemaining))
			;
		// @formatter:on
	}

	@Test
	public void writeValue_outOfRange() throws IOException {
		// GIVEN
		StaticDataMapModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		DerEnterServiceModelAccessor model = discoverModel(conn);

		// WHEN
		Throwable t = catchThrowable(() -> model.setVoltageHigh(conn, 7000f));

		// THEN
		// @formatter:off
		then(t)
			.as("Scaled value larger than uint16 rejected")
			.isInstanceOf(IllegalArgumentException.class)
			;
		then(discoverModel(conn).getVoltageHigh())
			.as("Device not updated")
			.isEqualTo(106.0f)
			;
		// @formatter:on
	}

}
