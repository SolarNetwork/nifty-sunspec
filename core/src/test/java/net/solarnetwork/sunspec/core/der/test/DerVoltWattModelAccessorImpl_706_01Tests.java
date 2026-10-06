/* ==================================================================
 * DerVoltWattModelAccessorImpl_706_01Tests.java - 5/10/2026 5:31:07 pm
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

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.der.DerActivePowerReference;
import net.solarnetwork.sunspec.api.der.DerCurvePoint;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerVoltWattModelAccessor;
import net.solarnetwork.sunspec.api.der.DerVoltWattModelAccessor.VoltWattCurve;
import net.solarnetwork.sunspec.core.der.DerVoltWattModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * Test cases for the {@link DerVoltWattModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class DerVoltWattModelAccessorImpl_706_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	/** The model block address. */
	private static final int BLOCK_ADDRESS = 541;

	/** The curve 3 address: 13 fixed registers, then 9 registers per curve. */
	private static final int CURVE_3_ADDRESS = BLOCK_ADDRESS + 13 + 9 * 2;

	private DerVoltWattModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerVoltWattModelAccessor.class);
	}

	private static DerVoltWattModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn).findTypedModel(DerVoltWattModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerVoltWattModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerVoltWattModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(539, from(DerVoltWattModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BLOCK_ADDRESS, from(DerVoltWattModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.VoltWatt, from(DerVoltWattModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(13, from(DerVoltWattModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(9, from(DerVoltWattModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(40, from(DerVoltWattModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void curves() {
		// GIVEN
		DerVoltWattModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Enabled")
			.returns(true, from(DerVoltWattModelAccessor::isEnabled))
			.as("Curve count")
			.returns(3, from(DerVoltWattModelAccessor::getCurveCount))
			.as("Curve point count")
			.returns(2, from(DerVoltWattModelAccessor::getCurvePointCount))
			;
		// @formatter:on

		List<VoltWattCurve> curves = model.getCurves();
		// @formatter:off
		then(curves)
			.as("Curves")
			.hasSize(3)
			;
		// @formatter:on
		VoltWattCurve curve = curves.get(0);
		// @formatter:off
		then(curve)
			.as("Read-only")
			.returns(true, from(VoltWattCurve::isReadOnly))
			.as("Dependent reference")
			.returns(DerActivePowerReference.MaximumActivePowerPercent,
					from(VoltWattCurve::getDependentReference))
			.as("Open loop response time")
			.returns(10.0f, from(VoltWattCurve::getOpenLoopResponseTime))
			.as("Points")
			.returns(List.of(new DerCurvePoint(106.0f, 100.0f), new DerCurvePoint(110.0f, 20.0f)),
					from(VoltWattCurve::getPoints))
			;
		then(curves.get(2).isReadOnly())
			.as("Stored curve read-only")
			.isFalse()
			;
		// @formatter:on
	}

	@Test
	public void writeCurve() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		VoltWattCurve curve = discoverModel(conn).getCurves().get(2);

		// WHEN
		curve.setDependentReference(conn, DerActivePowerReference.AvailableActivePowerPercent);
		curve.setOpenLoopResponseTime(conn, 2.5f);
		curve.setPoints(conn,
				List.of(new DerCurvePoint(105.0f, 100.0f), new DerCurvePoint(109.0f, 0.0f)));

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Writes")
			.isEqualTo(List.of(List.of(CURVE_3_ADDRESS + 1, 1), List.of(CURVE_3_ADDRESS + 2, 2),
					List.of(CURVE_3_ADDRESS + 5, 4), List.of(CURVE_3_ADDRESS, 1)))
			;
		// @formatter:on

		VoltWattCurve device = discoverModel(conn).getCurves().get(2);
		// @formatter:off
		then(device)
			.as("Dependent reference")
			.returns(DerActivePowerReference.AvailableActivePowerPercent,
					from(VoltWattCurve::getDependentReference))
			.as("Open loop response time")
			.returns(2.5f, from(VoltWattCurve::getOpenLoopResponseTime))
			.as("Points")
			.returns(List.of(new DerCurvePoint(105.0f, 100.0f), new DerCurvePoint(109.0f, 0.0f)),
					from(VoltWattCurve::getPoints))
			;
		// @formatter:on
	}

}
