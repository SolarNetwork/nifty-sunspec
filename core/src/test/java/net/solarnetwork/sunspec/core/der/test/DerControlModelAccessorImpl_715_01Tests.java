/* ==================================================================
 * DerControlModelAccessorImpl_715_01Tests.java - 5/10/2026 10:42:15 am
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
import net.solarnetwork.sunspec.api.der.DerControlModelAccessor;
import net.solarnetwork.sunspec.api.der.DerControlModelRegister;
import net.solarnetwork.sunspec.api.der.DerLocalRemoteControl;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerOperationCommand;
import net.solarnetwork.sunspec.core.der.DerControlModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.support.StaticDataMapModbusConnection;

/**
 * Test cases for the {@link DerControlModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class DerControlModelAccessorImpl_715_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	private DerControlModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerControlModelAccessor.class);
	}

	private static DerControlModelAccessorImpl discoverModel(ModbusConnection conn) {
		return (DerControlModelAccessorImpl) ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(DerControlModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerControlModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerControlModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(1265, from(DerControlModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(1267, from(DerControlModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.Control, from(DerControlModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(7, from(DerControlModelAccessor::getFixedBlockLength))
			.as("Model length")
			.returns(7, from(DerControlModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void values() {
		// GIVEN
		DerControlModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Local or remote control")
			.returns(DerLocalRemoteControl.Remote, from(DerControlModelAccessor::getLocalRemoteControl))
			.as("DER heartbeat")
			.returns(10615L, from(DerControlModelAccessor::getDerHeartbeat))
			.as("Controller heartbeat not implemented")
			.returns(null, from(DerControlModelAccessor::getControllerHeartbeat))
			.as("Operation command")
			.returns(DerOperationCommand.Start, from(DerControlModelAccessor::getOperationCommand))
			;
		// @formatter:on
	}

	@Test
	public void writeValues() throws IOException {
		// GIVEN
		StaticDataMapModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		DerControlModelAccessor model = discoverModel(conn);

		// WHEN
		model.setControllerHeartbeat(conn, 123456L);
		model.setOperationCommand(conn, DerOperationCommand.EnterStandby);
		model.resetAlarms(conn);

		// THEN
		// @formatter:off
		then(model.getOperationCommand())
			.as("Model data updated")
			.isEqualTo(DerOperationCommand.EnterStandby)
			;
		// @formatter:on

		DerControlModelAccessorImpl device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Controller heartbeat")
			.returns(123456L, from(DerControlModelAccessorImpl::getControllerHeartbeat))
			.as("Operation command")
			.returns(DerOperationCommand.EnterStandby,
					from(DerControlModelAccessorImpl::getOperationCommand))
			;
		then(device.getIntegerValue(DerControlModelRegister.AlarmReset))
			.as("Alarm reset")
			.isEqualTo(1)
			;
		then(device.getDerHeartbeat())
			.as("DER heartbeat unchanged")
			.isEqualTo(10615L)
			;
		// @formatter:on
	}

	@Test
	public void writeControllerHeartbeat_notImplementedValue() throws IOException {
		// GIVEN
		StaticDataMapModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		DerControlModelAccessor model = discoverModel(conn);

		// WHEN
		Throwable t = catchThrowable(() -> model.setControllerHeartbeat(conn, 0xFFFFFFFFL));

		// THEN
		// @formatter:off
		then(t)
			.as("The uint32 not implemented value rejected")
			.isInstanceOf(IllegalArgumentException.class)
			;
		then(discoverModel(conn).getControllerHeartbeat())
			.as("Device not updated")
			.isNull()
			;
		// @formatter:on
	}

}
