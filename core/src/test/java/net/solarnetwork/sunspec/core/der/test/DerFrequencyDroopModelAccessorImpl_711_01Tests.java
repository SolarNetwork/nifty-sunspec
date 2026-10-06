/* ==================================================================
 * DerFrequencyDroopModelAccessorImpl_711_01Tests.java - 5/10/2026 7:12:33 pm
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
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.der.DerAdoptResult;
import net.solarnetwork.sunspec.api.der.DerFrequencyDroopModelAccessor;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerFrequencyDroopModelAccessor.FrequencyDroopControl;
import net.solarnetwork.sunspec.core.der.DerFrequencyDroopModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * Test cases for the {@link DerFrequencyDroopModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class DerFrequencyDroopModelAccessorImpl_711_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	/** The model block address. */
	private static final int BLOCK_ADDRESS = 1107;

	private DerFrequencyDroopModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerFrequencyDroopModelAccessor.class);
	}

	private RecordingModbusConnection writableConnection() {
		return ModelDataUtils.getWritableModbusConnection(getClass(), TEST_DATA);
	}

	private static DerFrequencyDroopModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(DerFrequencyDroopModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerFrequencyDroopModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerFrequencyDroopModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(1105, from(DerFrequencyDroopModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BLOCK_ADDRESS, from(DerFrequencyDroopModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.FrequencyDroop, from(DerFrequencyDroopModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(12, from(DerFrequencyDroopModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(10, from(DerFrequencyDroopModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model repeating instance count")
			.returns(3, from(DerFrequencyDroopModelAccessor::getRepeatingBlockInstanceCount))
			.as("Model length")
			.returns(42, from(DerFrequencyDroopModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void controlManagement() {
		// GIVEN
		DerFrequencyDroopModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Enabled")
			.returns(false, from(DerFrequencyDroopModelAccessor::isEnabled))
			.as("Control count")
			.returns(3, from(DerFrequencyDroopModelAccessor::getControlCount))
			.as("Adopt control request")
			.returns(0, from(DerFrequencyDroopModelAccessor::getAdoptControlRequest))
			.as("Adopt control result")
			.returns(DerAdoptResult.InProgress,
					from(DerFrequencyDroopModelAccessor::getAdoptControlResult))
			.as("Reversion time not implemented")
			.returns(null, from(DerFrequencyDroopModelAccessor::getReversionTime))
			.as("Reversion time remaining not implemented")
			.returns(null, from(DerFrequencyDroopModelAccessor::getReversionTimeRemaining))
			.as("Reversion control not implemented")
			.returns(null, from(DerFrequencyDroopModelAccessor::getReversionControl))
			;
		// @formatter:on
	}

	@Test
	public void controls() {
		// WHEN
		List<FrequencyDroopControl> controls = getTestModel().getControls();

		// THEN
		// @formatter:off
		then(controls)
			.as("Controls")
			.hasSize(3)
			;
		// @formatter:on

		FrequencyDroopControl control = controls.get(0);
		// @formatter:off
		then(control)
			.as("Index")
			.returns(1, from(FrequencyDroopControl::getIndex))
			.as("Read-only")
			.returns(true, from(FrequencyDroopControl::isReadOnly))
			.as("Over-frequency deadband")
			.returns(0.036f, from(FrequencyDroopControl::getOverFrequencyDeadband))
			.as("Under-frequency deadband")
			.returns(0.036f, from(FrequencyDroopControl::getUnderFrequencyDeadband))
			.as("Over-frequency change ratio")
			.returns(0.05f, from(FrequencyDroopControl::getOverFrequencyChangeRatio))
			.as("Under-frequency change ratio")
			.returns(0.05f, from(FrequencyDroopControl::getUnderFrequencyChangeRatio))
			.as("Open loop response time")
			.returns(5.0f, from(FrequencyDroopControl::getOpenLoopResponseTime))
			.as("Minimum active power")
			.returns(0, from(FrequencyDroopControl::getMinimumActivePower))
			;
		// @formatter:on

		for ( int i = 1; i < 3; i++ ) {
			FrequencyDroopControl stored = controls.get(i);
			String prefix = "Control " + (i + 1);
			// @formatter:off
			then(stored)
				.as(prefix + " index")
				.returns(i + 1, from(FrequencyDroopControl::getIndex))
				.as(prefix + " read-only")
				.returns(false, from(FrequencyDroopControl::isReadOnly))
				.as(prefix + " over-frequency deadband")
				.returns(0.0f, from(FrequencyDroopControl::getOverFrequencyDeadband))
				;
			// @formatter:on
		}
	}

	@Test
	public void writeControl() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		FrequencyDroopControl control = discoverModel(conn).getControls().get(1);

		// WHEN
		control.setOverFrequencyDeadband(conn, 0.017f);
		control.setUnderFrequencyDeadband(conn, 0.025f);
		control.setOverFrequencyChangeRatio(conn, 0.04f);
		control.setUnderFrequencyChangeRatio(conn, 0.03f);
		control.setOpenLoopResponseTime(conn, 10.5f);
		control.setMinimumActivePower(conn, -50);

		// THEN
		List<FrequencyDroopControl> controls = discoverModel(conn).getControls();
		FrequencyDroopControl device = controls.get(1);
		// @formatter:off
		then(device)
			.as("Over-frequency deadband")
			.returns(0.017f, from(FrequencyDroopControl::getOverFrequencyDeadband))
			.as("Under-frequency deadband")
			.returns(0.025f, from(FrequencyDroopControl::getUnderFrequencyDeadband))
			.as("Over-frequency change ratio")
			.returns(0.04f, from(FrequencyDroopControl::getOverFrequencyChangeRatio))
			.as("Under-frequency change ratio")
			.returns(0.03f, from(FrequencyDroopControl::getUnderFrequencyChangeRatio))
			.as("Open loop response time")
			.returns(10.5f, from(FrequencyDroopControl::getOpenLoopResponseTime))
			.as("Minimum active power")
			.returns(-50, from(FrequencyDroopControl::getMinimumActivePower))
			;
		then(controls.get(2).getOpenLoopResponseTime())
			.as("Control 3 unchanged")
			.isEqualTo(0.0f)
			;
		// @formatter:on
	}

	@Test
	public void setMinimumActivePower_outOfRange() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		FrequencyDroopControl control = discoverModel(conn).getControls().get(1);

		// THEN
		// @formatter:off
		for ( int percent : new int[] { -101, 101 } ) {
			thenThrownBy(() -> control.setMinimumActivePower(conn, percent))
				.as("Minimum active power %d%% rejected", percent)
				.isInstanceOf(IllegalArgumentException.class)
				;
		}
		then(conn.getWrites())
			.as("Nothing written")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void readOnlyControl() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		FrequencyDroopControl control = discoverModel(conn).getControls().get(0);

		// WHEN
		Throwable t = catchThrowable(() -> control.setOverFrequencyDeadband(conn, 0.017f));

		// THEN
		// @formatter:off
		then(t)
			.as("Writing to the read-only control rejected")
			.isInstanceOf(UnsupportedOperationException.class)
			;
		then(conn.getWrites())
			.as("Nothing written")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void adoptControl() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerFrequencyDroopModelAccessor model = discoverModel(conn);

		// WHEN
		final List<Throwable> errors = new ArrayList<>();
		for ( int index : new int[] { -1, 4 } ) {
			errors.add(catchThrowable(() -> model.adoptControl(conn, index)));
		}
		model.adoptControl(conn, 0);
		model.adoptControl(conn, 2);

		// THEN
		// @formatter:off
		then(errors)
			.as("Control indexes -1 and 4 rejected")
			.hasOnlyElementsOfType(IllegalArgumentException.class)
			;
		then(conn.getWrites())
			.as("Writes")
			.isEqualTo(List.of(List.of(BLOCK_ADDRESS + 1, 1), List.of(BLOCK_ADDRESS + 1, 1)))
			;
		then(discoverModel(conn).getAdoptControlRequest())
			.as("Adopt control request")
			.isEqualTo(2)
			;
		// @formatter:on
	}

	@Test
	public void writeControlManagement() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerFrequencyDroopModelAccessor model = discoverModel(conn);

		// WHEN
		final List<Throwable> errors = new ArrayList<>();
		for ( int index : new int[] { 0, 4 } ) {
			errors.add(catchThrowable(() -> model.setReversionControl(conn, index)));
		}
		model.setEnabled(conn, true);
		model.setReversionTime(conn, 300);
		model.setReversionControl(conn, 3);

		// THEN
		DerFrequencyDroopModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(errors)
			.as("Reversion control indexes 0 and 4 rejected")
			.hasOnlyElementsOfType(IllegalArgumentException.class)
			;
		then(device)
			.as("Enabled")
			.returns(true, from(DerFrequencyDroopModelAccessor::isEnabled))
			.as("Reversion time")
			.returns(300L, from(DerFrequencyDroopModelAccessor::getReversionTime))
			.as("Reversion control")
			.returns(3, from(DerFrequencyDroopModelAccessor::getReversionControl))
			;
		// @formatter:on
	}

}
