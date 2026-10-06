/* ==================================================================
 * InverterImmediateControlsModelAccessorImplTests.java - 6/10/2026 10:14:36 am
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

package net.solarnetwork.sunspec.core.inverter.test;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.inverter.InverterConnectionControl;
import net.solarnetwork.sunspec.api.inverter.InverterControlModelId;
import net.solarnetwork.sunspec.api.inverter.InverterImmediateControlsModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterReactivePowerPercentMode;
import net.solarnetwork.sunspec.core.inverter.InverterImmediateControlsModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * Test cases for the {@link InverterImmediateControlsModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class InverterImmediateControlsModelAccessorImplTests {

	/** A Fronius IG Plus capture. */
	private static final String FRONIUS_TEST_DATA = "test-data-101-01.txt";

	/** An SMA capture. */
	private static final String SMA_TEST_DATA = "test-data-103-05.txt";

	/** A Fronius Symo capture. */
	private static final String FRONIUS_SYMO_TEST_DATA = "test-data-113-01.txt";

	private InverterImmediateControlsModelAccessor getTestModel(String resource) {
		return ModelDataUtils.getModelDataInstance(getClass(), resource)
				.findTypedModel(InverterImmediateControlsModelAccessor.class);
	}

	private static InverterImmediateControlsModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(InverterImmediateControlsModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel(FRONIUS_TEST_DATA))
			.as("Model found by accessor type")
			.isInstanceOf(InverterImmediateControlsModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		InverterImmediateControlsModelAccessor model = getTestModel(FRONIUS_TEST_DATA);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(227, from(InverterImmediateControlsModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(229, from(InverterImmediateControlsModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(InverterControlModelId.ImmediateControls,
					from(InverterImmediateControlsModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(24, from(InverterImmediateControlsModelAccessor::getFixedBlockLength))
			.as("Model length")
			.returns(24, from(InverterImmediateControlsModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void values_fronius() {
		// GIVEN
		InverterImmediateControlsModelAccessor model = getTestModel(FRONIUS_TEST_DATA);

		// THEN
		// @formatter:off
		then(model)
			.as("Connection time window")
			.returns(0, from(InverterImmediateControlsModelAccessor::getConnectionTimeWindow))
			.as("Connection reversion time")
			.returns(0, from(InverterImmediateControlsModelAccessor::getConnectionReversionTime))
			.as("Connection control")
			.returns(InverterConnectionControl.Disconnect,
					from(InverterImmediateControlsModelAccessor::getConnectionControl))
			.as("Active power limit")
			.returns(100.0f, from(InverterImmediateControlsModelAccessor::getActivePowerLimitPercent))
			.as("Active power limit time window")
			.returns(0, from(InverterImmediateControlsModelAccessor::getActivePowerLimitTimeWindow))
			.as("Active power limit reversion time")
			.returns(0, from(InverterImmediateControlsModelAccessor::getActivePowerLimitReversionTime))
			.as("Active power limit ramp time")
			.returns(0, from(InverterImmediateControlsModelAccessor::getActivePowerLimitRampTime))
			.as("Active power limit enabled")
			.returns(false, from(InverterImmediateControlsModelAccessor::isActivePowerLimitEnabled))
			.as("Fixed power factor")
			.returns(0.0f, from(InverterImmediateControlsModelAccessor::getFixedPowerFactor))
			.as("Fixed power factor time window")
			.returns(0, from(InverterImmediateControlsModelAccessor::getFixedPowerFactorTimeWindow))
			.as("Fixed power factor reversion time")
			.returns(0, from(InverterImmediateControlsModelAccessor::getFixedPowerFactorReversionTime))
			.as("Fixed power factor ramp time")
			.returns(0, from(InverterImmediateControlsModelAccessor::getFixedPowerFactorRampTime))
			.as("Fixed power factor enabled")
			.returns(false, from(InverterImmediateControlsModelAccessor::isFixedPowerFactorEnabled))
			.as("Reactive power of maximum active power not implemented")
			.returns(null,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentOfMaximumActivePower))
			.as("Reactive power of maximum reactive power")
			.returns(0.0f,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentOfMaximumReactivePower))
			.as("Reactive power of available reactive power not implemented")
			.returns(null,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentOfAvailableReactivePower))
			.as("Reactive power time window")
			.returns(0, from(InverterImmediateControlsModelAccessor::getReactivePowerPercentTimeWindow))
			.as("Reactive power reversion time")
			.returns(0,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentReversionTime))
			.as("Reactive power ramp time")
			.returns(0, from(InverterImmediateControlsModelAccessor::getReactivePowerPercentRampTime))
			.as("Reactive power mode")
			.returns(InverterReactivePowerPercentMode.MaximumReactivePowerPercent,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentMode))
			.as("Reactive power enabled")
			.returns(false, from(InverterImmediateControlsModelAccessor::isReactivePowerPercentEnabled))
			;
		// @formatter:on
	}

	@Test
	public void values_sma() {
		// GIVEN
		InverterImmediateControlsModelAccessor model = getTestModel(SMA_TEST_DATA);

		// THEN
		// @formatter:off
		then(model)
			.as("Connection time window not implemented")
			.returns(null, from(InverterImmediateControlsModelAccessor::getConnectionTimeWindow))
			.as("Connection reversion time not implemented")
			.returns(null, from(InverterImmediateControlsModelAccessor::getConnectionReversionTime))
			.as("Connection control not implemented")
			.returns(null, from(InverterImmediateControlsModelAccessor::getConnectionControl))
			.as("Active power limit not implemented")
			.returns(null, from(InverterImmediateControlsModelAccessor::getActivePowerLimitPercent))
			.as("Active power limit ramp time not implemented")
			.returns(null, from(InverterImmediateControlsModelAccessor::getActivePowerLimitRampTime))
			.as("Active power limit enabled")
			.returns(true, from(InverterImmediateControlsModelAccessor::isActivePowerLimitEnabled))
			.as("Fixed power factor not implemented")
			.returns(null, from(InverterImmediateControlsModelAccessor::getFixedPowerFactor))
			.as("Fixed power factor enabled")
			.returns(true, from(InverterImmediateControlsModelAccessor::isFixedPowerFactorEnabled))
			.as("Reactive power of maximum reactive power not implemented")
			.returns(null,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentOfMaximumReactivePower))
			.as("Reactive power mode")
			.returns(InverterReactivePowerPercentMode.MaximumActivePowerPercent,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentMode))
			.as("Reactive power enabled")
			.returns(false, from(InverterImmediateControlsModelAccessor::isReactivePowerPercentEnabled))
			;
		// @formatter:on
	}

	@Test
	public void values_froniusSymo() {
		// GIVEN
		InverterImmediateControlsModelAccessor model = getTestModel(FRONIUS_SYMO_TEST_DATA);

		// THEN
		// @formatter:off
		then(model)
			.as("Connection control")
			.returns(InverterConnectionControl.Connect,
					from(InverterImmediateControlsModelAccessor::getConnectionControl))
			.as("Active power limit")
			.returns(100.0f, from(InverterImmediateControlsModelAccessor::getActivePowerLimitPercent))
			.as("Active power limit ramp time not implemented")
			.returns(null, from(InverterImmediateControlsModelAccessor::getActivePowerLimitRampTime))
			.as("Fixed power factor")
			.returns(1.0f, from(InverterImmediateControlsModelAccessor::getFixedPowerFactor))
			.as("Fixed power factor ramp time not implemented")
			.returns(null, from(InverterImmediateControlsModelAccessor::getFixedPowerFactorRampTime))
			.as("Reactive power ramp time not implemented")
			.returns(null, from(InverterImmediateControlsModelAccessor::getReactivePowerPercentRampTime))
			;
		// @formatter:on
	}

	@Test
	public void writeValues() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				FRONIUS_TEST_DATA);
		InverterImmediateControlsModelAccessor model = discoverModel(conn);

		// WHEN
		model.setConnectionTimeWindow(conn, 30);
		model.setConnectionReversionTime(conn, 600);
		model.setConnectionControl(conn, InverterConnectionControl.Connect);
		model.setActivePowerLimitPercent(conn, 75.0f);
		model.setActivePowerLimitTimeWindow(conn, 10);
		model.setActivePowerLimitReversionTime(conn, 900);
		model.setActivePowerLimitRampTime(conn, 5);
		model.setActivePowerLimitEnabled(conn, true);
		model.setFixedPowerFactor(conn, -0.95f);
		model.setFixedPowerFactorTimeWindow(conn, 20);
		model.setFixedPowerFactorReversionTime(conn, 1200);
		model.setFixedPowerFactorRampTime(conn, 15);
		model.setFixedPowerFactorEnabled(conn, true);
		model.setReactivePowerPercentOfMaximumActivePower(conn, -25.0f);
		model.setReactivePowerPercentOfMaximumReactivePower(conn, 40.0f);
		model.setReactivePowerPercentOfAvailableReactivePower(conn, 60.0f);
		model.setReactivePowerPercentTimeWindow(conn, 25);
		model.setReactivePowerPercentReversionTime(conn, 1800);
		model.setReactivePowerPercentRampTime(conn, 35);
		model.setReactivePowerPercentMode(conn,
				InverterReactivePowerPercentMode.AvailableReactivePowerPercent);
		model.setReactivePowerPercentEnabled(conn, true);

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Each point written to its own register, in order")
			.isEqualTo(IntStream.rangeClosed(229, 249).mapToObj(a -> List.of(a, 1)).toList())
			;
		then(model.getConnectionControl())
			.as("Model data updated")
			.isEqualTo(InverterConnectionControl.Connect)
			;
		// @formatter:on

		InverterImmediateControlsModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Connection time window")
			.returns(30, from(InverterImmediateControlsModelAccessor::getConnectionTimeWindow))
			.as("Connection reversion time")
			.returns(600, from(InverterImmediateControlsModelAccessor::getConnectionReversionTime))
			.as("Connection control")
			.returns(InverterConnectionControl.Connect,
					from(InverterImmediateControlsModelAccessor::getConnectionControl))
			.as("Active power limit")
			.returns(75.0f, from(InverterImmediateControlsModelAccessor::getActivePowerLimitPercent))
			.as("Active power limit time window")
			.returns(10, from(InverterImmediateControlsModelAccessor::getActivePowerLimitTimeWindow))
			.as("Active power limit reversion time")
			.returns(900, from(InverterImmediateControlsModelAccessor::getActivePowerLimitReversionTime))
			.as("Active power limit ramp time")
			.returns(5, from(InverterImmediateControlsModelAccessor::getActivePowerLimitRampTime))
			.as("Active power limit enabled")
			.returns(true, from(InverterImmediateControlsModelAccessor::isActivePowerLimitEnabled))
			.as("Fixed power factor")
			.returns(-0.95f, from(InverterImmediateControlsModelAccessor::getFixedPowerFactor))
			.as("Fixed power factor time window")
			.returns(20, from(InverterImmediateControlsModelAccessor::getFixedPowerFactorTimeWindow))
			.as("Fixed power factor reversion time")
			.returns(1200,
					from(InverterImmediateControlsModelAccessor::getFixedPowerFactorReversionTime))
			.as("Fixed power factor ramp time")
			.returns(15, from(InverterImmediateControlsModelAccessor::getFixedPowerFactorRampTime))
			.as("Fixed power factor enabled")
			.returns(true, from(InverterImmediateControlsModelAccessor::isFixedPowerFactorEnabled))
			.as("Reactive power of maximum active power")
			.returns(-25.0f,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentOfMaximumActivePower))
			.as("Reactive power of maximum reactive power")
			.returns(40.0f,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentOfMaximumReactivePower))
			.as("Reactive power of available reactive power")
			.returns(60.0f,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentOfAvailableReactivePower))
			.as("Reactive power time window")
			.returns(25, from(InverterImmediateControlsModelAccessor::getReactivePowerPercentTimeWindow))
			.as("Reactive power reversion time")
			.returns(1800,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentReversionTime))
			.as("Reactive power ramp time")
			.returns(35, from(InverterImmediateControlsModelAccessor::getReactivePowerPercentRampTime))
			.as("Reactive power mode")
			.returns(InverterReactivePowerPercentMode.AvailableReactivePowerPercent,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentMode))
			.as("Reactive power enabled")
			.returns(true, from(InverterImmediateControlsModelAccessor::isReactivePowerPercentEnabled))
			;
		// @formatter:on
	}

	@Test
	public void writeValues_negativeScaleFactor() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				SMA_TEST_DATA);
		InverterImmediateControlsModelAccessor model = discoverModel(conn);

		// WHEN
		model.setActivePowerLimitPercent(conn, 62.5f);
		model.setFixedPowerFactor(conn, 0.9876f);
		model.setReactivePowerPercentOfMaximumActivePower(conn, 12.34f);

		// THEN
		InverterImmediateControlsModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Active power limit")
			.returns(62.5f, from(InverterImmediateControlsModelAccessor::getActivePowerLimitPercent))
			.as("Fixed power factor")
			.returns(0.9876f, from(InverterImmediateControlsModelAccessor::getFixedPowerFactor))
			.as("Reactive power of maximum active power")
			.returns(12.34f,
					from(InverterImmediateControlsModelAccessor::getReactivePowerPercentOfMaximumActivePower))
			;
		// @formatter:on
	}

	@Test
	public void writeValue_outOfRange() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				FRONIUS_TEST_DATA);
		InverterImmediateControlsModelAccessor model = discoverModel(conn);

		// WHEN
		Throwable tooLarge = catchThrowable(() -> model.setActivePowerLimitPercent(conn, 70000.0f));
		Throwable negative = catchThrowable(() -> model.setConnectionTimeWindow(conn, -1));

		// THEN
		// @formatter:off
		then(tooLarge)
			.as("Scaled value larger than uint16 rejected")
			.isInstanceOf(IllegalArgumentException.class)
			;
		then(negative)
			.as("Negative uint16 value rejected")
			.isInstanceOf(IllegalArgumentException.class)
			;
		then(conn.getWrites())
			.as("Nothing written")
			.isEmpty()
			;
		// @formatter:on
	}

}
