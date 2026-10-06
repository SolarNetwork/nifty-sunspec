/* ==================================================================
 * InverterExtendedMeasurementsModelAccessorImplTests.java - 6/10/2026 9:52:10 am
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

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.inverter.InverterConnectionStatus;
import net.solarnetwork.sunspec.api.inverter.InverterControlFunction;
import net.solarnetwork.sunspec.api.inverter.InverterControlModelId;
import net.solarnetwork.sunspec.api.inverter.InverterExtendedMeasurementsModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterRideThrough;
import net.solarnetwork.sunspec.api.inverter.InverterSetpointLimit;
import net.solarnetwork.sunspec.core.inverter.InverterExtendedMeasurementsModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;

/**
 * Test cases for the {@link InverterExtendedMeasurementsModelAccessorImpl}
 * class.
 *
 * @author matt
 * @version 1.0
 */
public class InverterExtendedMeasurementsModelAccessorImplTests {

	/** A Fronius IG Plus capture. */
	private static final String FRONIUS_TEST_DATA = "test-data-101-01.txt";

	/** An SMA capture. */
	private static final String SMA_TEST_DATA = "test-data-103-05.txt";

	/** A Fronius Symo capture. */
	private static final String FRONIUS_SYMO_TEST_DATA = "test-data-113-01.txt";

	private InverterExtendedMeasurementsModelAccessor getTestModel(String resource) {
		return ModelDataUtils.getModelDataInstance(getClass(), resource)
				.findTypedModel(InverterExtendedMeasurementsModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel(FRONIUS_TEST_DATA))
			.as("Model found by accessor type")
			.isInstanceOf(InverterExtendedMeasurementsModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		InverterExtendedMeasurementsModelAccessor model = getTestModel(FRONIUS_TEST_DATA);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(181, from(InverterExtendedMeasurementsModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(183, from(InverterExtendedMeasurementsModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(InverterControlModelId.ExtendedMeasurements,
					from(InverterExtendedMeasurementsModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(44, from(InverterExtendedMeasurementsModelAccessor::getFixedBlockLength))
			.as("Model length")
			.returns(44, from(InverterExtendedMeasurementsModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void values_fronius() {
		// GIVEN
		InverterExtendedMeasurementsModelAccessor model = getTestModel(FRONIUS_TEST_DATA);

		// THEN
		// @formatter:off
		then(model)
			.as("PV connection status")
			.returns(EnumSet.of(InverterConnectionStatus.Available, InverterConnectionStatus.Operating),
					from(InverterExtendedMeasurementsModelAccessor::getPvConnectionStatus))
			.as("Storage connection status")
			.returns(Set.of(),
					from(InverterExtendedMeasurementsModelAccessor::getStorageConnectionStatus))
			.as("ECP connected")
			.returns(false, from(InverterExtendedMeasurementsModelAccessor::isEcpConnected))
			.as("Active energy")
			.returns(76476024L, from(InverterExtendedMeasurementsModelAccessor::getActiveEnergyExported))
			.as("Apparent energy not accumulated")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::getApparentEnergyExported))
			.as("Reactive energy Q1 not accumulated")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::getReactiveEnergyQ1))
			.as("Reactive energy Q2 not accumulated")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::getReactiveEnergyQ2))
			.as("Reactive energy Q3 not accumulated")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::getReactiveEnergyQ3))
			.as("Reactive energy Q4 not accumulated")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::getReactiveEnergyQ4))
			.as("Reactive power available not implemented")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::getReactivePowerAvailable))
			.as("Active power available not implemented")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::getActivePowerAvailable))
			.as("Setpoint limits not implemented")
			.returns(Set.of(), from(InverterExtendedMeasurementsModelAccessor::getSetpointLimitsReached))
			.as("Active controls")
			.returns(Set.of(), from(InverterExtendedMeasurementsModelAccessor::getActiveControls))
			.as("Time source")
			.returns("RTC", from(InverterExtendedMeasurementsModelAccessor::getTimeSource))
			.as("Device time is the SunSpec epoch")
			.returns(Instant.parse("2000-01-01T00:00:00Z"),
					from(InverterExtendedMeasurementsModelAccessor::getDeviceTime))
			.as("Ride-throughs not implemented")
			.returns(Set.of(), from(InverterExtendedMeasurementsModelAccessor::getActiveRideThroughs))
			.as("Isolation resistance")
			.returns(0.0f, from(InverterExtendedMeasurementsModelAccessor::getIsolationResistance))
			;
		// @formatter:on
	}

	@Test
	public void values_sma() {
		// GIVEN
		InverterExtendedMeasurementsModelAccessor model = getTestModel(SMA_TEST_DATA);

		// THEN
		// @formatter:off
		then(model)
			.as("PV connection status")
			.returns(EnumSet.of(InverterConnectionStatus.Connected, InverterConnectionStatus.Operating),
					from(InverterExtendedMeasurementsModelAccessor::getPvConnectionStatus))
			.as("Storage connection status not implemented")
			.returns(Set.of(),
					from(InverterExtendedMeasurementsModelAccessor::getStorageConnectionStatus))
			.as("ECP connected")
			.returns(true, from(InverterExtendedMeasurementsModelAccessor::isEcpConnected))
			.as("Active energy")
			.returns(4418970L, from(InverterExtendedMeasurementsModelAccessor::getActiveEnergyExported))
			.as("Apparent energy not accumulated")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::getApparentEnergyExported))
			.as("Active controls not implemented")
			.returns(Set.of(), from(InverterExtendedMeasurementsModelAccessor::getActiveControls))
			.as("Time source not implemented")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::getTimeSource))
			.as("Device time not implemented")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::getDeviceTime))
			.as("Isolation resistance")
			.returns(2040000.0f, from(InverterExtendedMeasurementsModelAccessor::getIsolationResistance))
			;
		// @formatter:on
	}

	@Test
	public void values_froniusSymo() {
		// GIVEN
		InverterExtendedMeasurementsModelAccessor model = getTestModel(FRONIUS_SYMO_TEST_DATA);

		// THEN
		// @formatter:off
		then(model)
			.as("PV connection status")
			.returns(EnumSet.of(InverterConnectionStatus.Connected, InverterConnectionStatus.Available,
					InverterConnectionStatus.Operating),
					from(InverterExtendedMeasurementsModelAccessor::getPvConnectionStatus))
			.as("ECP connected")
			.returns(true, from(InverterExtendedMeasurementsModelAccessor::isEcpConnected))
			.as("Active energy")
			.returns(11937020L, from(InverterExtendedMeasurementsModelAccessor::getActiveEnergyExported))
			.as("Time source")
			.returns("RTC", from(InverterExtendedMeasurementsModelAccessor::getTimeSource))
			.as("Device time")
			.returns(Instant.parse("2019-10-05T16:36:59Z"),
					from(InverterExtendedMeasurementsModelAccessor::getDeviceTime))
			.as("Isolation resistance not implemented")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::getIsolationResistance))
			;
		// @formatter:on
	}

	@Test
	public void syntheticValues() {
		// GIVEN
		// replace the whole block with synthetic values
		InverterExtendedMeasurementsModelAccessor model = ModelDataUtils
				.getModelDataInstanceWithRegisters(getClass(), FRONIUS_TEST_DATA, 183,
				// @formatter:off
						0x0009,                         // PVConn
						0x0002,                         // StorConn
						0x8001,                         // ECPConn, MSB set
						0x8000, 0x0000, 0x0000, 0x0000, // ActWh, larger than a long
						0x0000, 0x0000, 0x0001, 0x0000, // ActVAh
						0x0000, 0x0000, 0x0000, 0x0001, // ActVArhQ1
						0x0000, 0x0000, 0x0000, 0x0002, // ActVArhQ2
						0x0000, 0x0000, 0x0000, 0x0003, // ActVArhQ3
						0x0000, 0x0000, 0x0000, 0x0004, // ActVArhQ4
						0xFB1E, 0x0000,                 // VArAval, VArAval_SF
						0x0159, 0x0001,                 // WAval, WAval_SF
						0x0000, 0x0481,                 // StSetLimMsk
						0x0000, 0x1804,                 // StActCtl, with undefined bit 11
						0x4E54, 0x5000, 0x0000, 0x0000, // TmSrc
						0x3257, 0x5FF8,                 // Tms
						0x000A,                         // RtSt
						0x05DC, 0x0003                  // Ris, Ris_SF
						// @formatter:on
				).findTypedModel(InverterExtendedMeasurementsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("PV connection status")
			.returns(EnumSet.of(InverterConnectionStatus.Connected, InverterConnectionStatus.Test),
					from(InverterExtendedMeasurementsModelAccessor::getPvConnectionStatus))
			.as("Storage connection status")
			.returns(EnumSet.of(InverterConnectionStatus.Available),
					from(InverterExtendedMeasurementsModelAccessor::getStorageConnectionStatus))
			.as("ECP connection status with MSB set not implemented")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::isEcpConnected))
			.as("Active energy larger than a long not available")
			.returns(null, from(InverterExtendedMeasurementsModelAccessor::getActiveEnergyExported))
			.as("Apparent energy")
			.returns(65536L, from(InverterExtendedMeasurementsModelAccessor::getApparentEnergyExported))
			.as("Reactive energy Q1")
			.returns(1L, from(InverterExtendedMeasurementsModelAccessor::getReactiveEnergyQ1))
			.as("Reactive energy Q2")
			.returns(2L, from(InverterExtendedMeasurementsModelAccessor::getReactiveEnergyQ2))
			.as("Reactive energy Q3")
			.returns(3L, from(InverterExtendedMeasurementsModelAccessor::getReactiveEnergyQ3))
			.as("Reactive energy Q4")
			.returns(4L, from(InverterExtendedMeasurementsModelAccessor::getReactiveEnergyQ4))
			.as("Reactive power available")
			.returns(-1250, from(InverterExtendedMeasurementsModelAccessor::getReactivePowerAvailable))
			.as("Active power available")
			.returns(3450, from(InverterExtendedMeasurementsModelAccessor::getActivePowerAvailable))
			.as("Setpoint limits")
			.returns(EnumSet.of(InverterSetpointLimit.MaximumActivePower,
					InverterSetpointLimit.MinimumPowerFactorQ1,
					InverterSetpointLimit.MinimumPowerFactorQ4),
					from(InverterExtendedMeasurementsModelAccessor::getSetpointLimitsReached))
			.as("Active controls")
			.returns(EnumSet.of(InverterControlFunction.FixedPowerFactor,
					InverterControlFunction.Scheduled),
					from(InverterExtendedMeasurementsModelAccessor::getActiveControls))
			.as("Time source")
			.returns("NTP", from(InverterExtendedMeasurementsModelAccessor::getTimeSource))
			.as("Device time")
			.returns(Instant.parse("2026-10-06T07:30:00Z"),
					from(InverterExtendedMeasurementsModelAccessor::getDeviceTime))
			.as("Ride-throughs")
			.returns(EnumSet.of(InverterRideThrough.HighVoltage, InverterRideThrough.HighFrequency),
					from(InverterExtendedMeasurementsModelAccessor::getActiveRideThroughs))
			.as("Isolation resistance")
			.returns(1500000.0f, from(InverterExtendedMeasurementsModelAccessor::getIsolationResistance))
			;
		// @formatter:on
	}

}
