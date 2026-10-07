/* ==================================================================
 * DerVoltVarModelAccessorImpl_705_01Tests.java - 5/10/2026 5:31:07 pm
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
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.PointMapMode;
import net.solarnetwork.sunspec.api.der.DerAdoptResult;
import net.solarnetwork.sunspec.api.der.DerCurvePoint;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerReactivePowerPriority;
import net.solarnetwork.sunspec.api.der.DerReactivePowerReference;
import net.solarnetwork.sunspec.api.der.DerVoltVarModelAccessor;
import net.solarnetwork.sunspec.api.der.DerVoltVarModelAccessor.VoltVarCurve;
import net.solarnetwork.sunspec.core.der.DerVoltVarModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * Test cases for the {@link DerVoltVarModelAccessorImpl} class, including the
 * curve support shared by the other DER curve models.
 *
 * @author matt
 * @version 1.0
 */
public class DerVoltVarModelAccessorImpl_705_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	/** The model block address. */
	private static final int BLOCK_ADDRESS = 472;

	/** The curve 2 address: 13 fixed registers, then 18 registers per curve. */
	private static final int CURVE_2_ADDRESS = BLOCK_ADDRESS + 13 + 18;

	/** The curve 2 points address: 10 setting registers, then the points. */
	private static final int CURVE_2_POINTS_ADDRESS = CURVE_2_ADDRESS + 10;

	private DerVoltVarModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerVoltVarModelAccessor.class);
	}

	private RecordingModbusConnection writableConnection() {
		return ModelDataUtils.getWritableModbusConnection(getClass(), TEST_DATA);
	}

	private static DerVoltVarModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn).findTypedModel(DerVoltVarModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerVoltVarModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerVoltVarModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(470, from(DerVoltVarModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BLOCK_ADDRESS, from(DerVoltVarModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.VoltVar, from(DerVoltVarModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(13, from(DerVoltVarModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(18, from(DerVoltVarModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model repeating instance count")
			.returns(3, from(DerVoltVarModelAccessor::getRepeatingBlockInstanceCount))
			.as("Model length")
			.returns(67, from(DerVoltVarModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void curveManagement() {
		// GIVEN
		DerVoltVarModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Enabled")
			.returns(false, from(DerVoltVarModelAccessor::isEnabled))
			.as("Curve count")
			.returns(3, from(DerVoltVarModelAccessor::getCurveCount))
			.as("Curve point count")
			.returns(4, from(DerVoltVarModelAccessor::getCurvePointCount))
			.as("Adopt curve request")
			.returns(0, from(DerVoltVarModelAccessor::getAdoptCurveRequest))
			.as("Adopt curve result")
			.returns(DerAdoptResult.InProgress, from(DerVoltVarModelAccessor::getAdoptCurveResult))
			.as("Reversion time not implemented")
			.returns(null, from(DerVoltVarModelAccessor::getReversionTime))
			.as("Reversion time remaining not implemented")
			.returns(null, from(DerVoltVarModelAccessor::getReversionTimeRemaining))
			.as("Reversion curve not implemented")
			.returns(null, from(DerVoltVarModelAccessor::getReversionCurve))
			;
		// @formatter:on
	}

	@Test
	public void activeCurve() {
		// WHEN
		List<VoltVarCurve> curves = getTestModel().getCurves();

		// THEN
		// @formatter:off
		then(curves)
			.as("Curves")
			.hasSize(3)
			;
		// @formatter:on

		VoltVarCurve curve = curves.get(0);
		// @formatter:off
		then(curve)
			.as("Index")
			.returns(1, from(VoltVarCurve::getIndex))
			.as("Read-only")
			.returns(true, from(VoltVarCurve::isReadOnly))
			.as("Active point count")
			.returns(4, from(VoltVarCurve::getActivePointCount))
			.as("Dependent reference")
			.returns(DerReactivePowerReference.MaximumActivePowerPercent,
					from(VoltVarCurve::getDependentReference))
			.as("Power priority")
			.returns(DerReactivePowerPriority.ActivePower, from(VoltVarCurve::getPowerPriority))
			.as("Voltage reference")
			.returns(100.0f, from(VoltVarCurve::getVoltageReference))
			.as("Autonomous voltage reference")
			.returns(100.0f, from(VoltVarCurve::getAutonomousVoltageReference))
			.as("Autonomous voltage reference enabled")
			.returns(false, from(VoltVarCurve::isAutonomousVoltageReferenceEnabled))
			.as("Autonomous voltage reference time constant")
			.returns(301, from(VoltVarCurve::getAutonomousVoltageReferenceTimeConstant))
			.as("Open loop response time")
			.returns(10.0f, from(VoltVarCurve::getOpenLoopResponseTime))
			.as("Points")
			.returns(List.of(new DerCurvePoint(90.0f, 25.0f), new DerCurvePoint(100.0f, 0.0f),
					new DerCurvePoint(100.0f, 0.0f), new DerCurvePoint(110.0f, -25.0f)),
					from(VoltVarCurve::getPoints))
			;
		// @formatter:on
	}

	@Test
	public void storedCurves() {
		// WHEN
		List<VoltVarCurve> curves = getTestModel().getCurves();

		// THEN
		for ( int i = 1; i < 3; i++ ) {
			VoltVarCurve curve = curves.get(i);
			String prefix = "Curve " + (i + 1);
			// @formatter:off
			then(curve)
				.as(prefix + " index")
				.returns(i + 1, from(VoltVarCurve::getIndex))
				.as(prefix + " read-only")
				.returns(false, from(VoltVarCurve::isReadOnly))
				.as(prefix + " points")
				.returns(List.of(new DerCurvePoint(0.0f, 0.0f), new DerCurvePoint(0.0f, 0.0f),
						new DerCurvePoint(0.0f, 0.0f), new DerCurvePoint(0.0f, 0.0f)),
						from(VoltVarCurve::getPoints))
				;
			// @formatter:on
		}
	}

	@Test
	public void writeCurve() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		VoltVarCurve curve = discoverModel(conn).getCurves().get(1);

		// WHEN
		curve.setDependentReference(conn, DerReactivePowerReference.MaximumReactivePowerPercent);
		curve.setPowerPriority(conn, DerReactivePowerPriority.ReactivePower);
		curve.setVoltageReference(conn, 101.5f);
		curve.setAutonomousVoltageReferenceEnabled(conn, true);
		curve.setAutonomousVoltageReferenceTimeConstant(conn, 120);
		curve.setOpenLoopResponseTime(conn, 5.5f);
		curve.setPoints(conn, List.of(new DerCurvePoint(92.0f, 30.0f), new DerCurvePoint(98.0f, 0.0f),
				new DerCurvePoint(102.0f, 0.0f), new DerCurvePoint(108.0f, -30.0f)));

		// THEN
		List<VoltVarCurve> curves = discoverModel(conn).getCurves();
		VoltVarCurve device = curves.get(1);
		// @formatter:off
		then(device)
			.as("Dependent reference")
			.returns(DerReactivePowerReference.MaximumReactivePowerPercent,
					from(VoltVarCurve::getDependentReference))
			.as("Power priority")
			.returns(DerReactivePowerPriority.ReactivePower, from(VoltVarCurve::getPowerPriority))
			.as("Voltage reference")
			.returns(101.5f, from(VoltVarCurve::getVoltageReference))
			.as("Autonomous voltage reference enabled")
			.returns(true, from(VoltVarCurve::isAutonomousVoltageReferenceEnabled))
			.as("Autonomous voltage reference time constant")
			.returns(120, from(VoltVarCurve::getAutonomousVoltageReferenceTimeConstant))
			.as("Open loop response time")
			.returns(5.5f, from(VoltVarCurve::getOpenLoopResponseTime))
			.as("Points")
			.returns(List.of(new DerCurvePoint(92.0f, 30.0f), new DerCurvePoint(98.0f, 0.0f),
					new DerCurvePoint(102.0f, 0.0f), new DerCurvePoint(108.0f, -30.0f)),
					from(VoltVarCurve::getPoints))
			;
		then(curves.get(0).getPoints().get(0))
			.as("Active curve unchanged")
			.isEqualTo(new DerCurvePoint(90.0f, 25.0f))
			;
		then(curves.get(2).getPoints().get(0))
			.as("Curve 3 unchanged")
			.isEqualTo(new DerCurvePoint(0.0f, 0.0f))
			;
		// @formatter:on
	}

	@Test
	public void setPoints_requests() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		VoltVarCurve curve = discoverModel(conn).getCurves().get(1);

		// WHEN
		curve.setPoints(conn, List.of(new DerCurvePoint(92.0f, 30.0f), new DerCurvePoint(98.0f, 0.0f),
				new DerCurvePoint(102.0f, 0.0f), new DerCurvePoint(108.0f, -30.0f)));

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Points written in one request, then the active point count")
			.isEqualTo(List.of(List.of(CURVE_2_POINTS_ADDRESS, 8), List.of(CURVE_2_ADDRESS, 1)))
			;
		// @formatter:on
	}

	@Test
	public void setPoints_fewerThanPointCount() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		VoltVarCurve curve = discoverModel(conn).getCurves().get(1);

		// WHEN
		curve.setPoints(conn, List.of(new DerCurvePoint(95.0f, 10.0f), new DerCurvePoint(100.0f, 0.0f),
				new DerCurvePoint(105.0f, -10.0f)));

		// THEN
		VoltVarCurve device = discoverModel(conn).getCurves().get(1);
		// @formatter:off
		then(device)
			.as("Active point count")
			.returns(3, from(VoltVarCurve::getActivePointCount))
			.as("Active points")
			.returns(List.of(new DerCurvePoint(95.0f, 10.0f), new DerCurvePoint(100.0f, 0.0f),
					new DerCurvePoint(105.0f, -10.0f)), from(VoltVarCurve::getPoints))
			;
		// @formatter:on
	}

	@Test
	public void setActivePointCount() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		VoltVarCurve curve = discoverModel(conn).getCurves().get(1);

		// WHEN
		curve.setActivePointCount(conn, 2);

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Active point count written")
			.isEqualTo(List.of(List.of(CURVE_2_ADDRESS, 1)))
			;
		then(discoverModel(conn).getCurves().get(1).getPoints())
			.as("Active point count")
			.hasSize(2)
			;
		// @formatter:on
	}

	@Test
	public void setPoints_invalidCount() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		VoltVarCurve curve = discoverModel(conn).getCurves().get(1);
		DerCurvePoint p = new DerCurvePoint(100.0f, 0.0f);

		// THEN
		// @formatter:off
		for ( List<DerCurvePoint> points : List.of(List.<DerCurvePoint> of(), List.of(p, p, p, p, p)) ) {
			thenThrownBy(() -> curve.setPoints(conn, points))
				.as("Point count %d rejected", points.size())
				.isInstanceOf(IllegalArgumentException.class)
				;
		}
		thenThrownBy(() -> curve.setActivePointCount(conn, 5))
			.as("Active point count above the curve point count rejected")
			.isInstanceOf(IllegalArgumentException.class)
			;
		then(conn.getWrites())
			.as("Nothing written")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void setPoints_invalidValue() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		VoltVarCurve curve = discoverModel(conn).getCurves().get(1);

		// WHEN
		Throwable t = catchThrowable(() -> curve.setPoints(conn,
				List.of(new DerCurvePoint(100.0f, 0.0f), new DerCurvePoint(7000.0f, 0.0f))));

		// THEN
		// @formatter:off
		then(t)
			.as("Scaled value larger than uint16 rejected")
			.isInstanceOf(IllegalArgumentException.class)
			;
		then(conn.getWrites())
			.as("Nothing written")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void readOnlyCurve() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		VoltVarCurve curve = discoverModel(conn).getCurves().get(0);

		// THEN
		// @formatter:off
		thenThrownBy(() -> curve.setPoints(conn, List.of(new DerCurvePoint(100.0f, 0.0f))))
			.as("Writing points to the read-only curve rejected")
			.isInstanceOf(UnsupportedOperationException.class)
			;
		thenThrownBy(() -> curve.setDependentReference(conn,
				DerReactivePowerReference.MaximumApparentPowerPercent))
			.as("Writing a setting to the read-only curve rejected")
			.isInstanceOf(UnsupportedOperationException.class)
			;
		then(conn.getWrites())
			.as("Nothing written")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void adoptCurve() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerVoltVarModelAccessor model = discoverModel(conn);

		// WHEN
		model.adoptCurve(conn, 2);

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Adopt curve request written")
			.isEqualTo(List.of(List.of(BLOCK_ADDRESS + 1, 1)))
			;
		then(discoverModel(conn).getAdoptCurveRequest())
			.as("Adopt curve request")
			.isEqualTo(2)
			;
		// @formatter:on
	}

	@Test
	public void adoptCurve_invalidIndex() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerVoltVarModelAccessor model = discoverModel(conn);

		// THEN
		// @formatter:off
		for ( int index : new int[] { 0, 1, 4 } ) {
			thenThrownBy(() -> model.adoptCurve(conn, index))
				.as("Curve index %d rejected", index)
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
	public void writeCurveManagement() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerVoltVarModelAccessor model = discoverModel(conn);

		// WHEN
		model.setEnabled(conn, true);
		model.setReversionTime(conn, 600);
		model.setReversionCurve(conn, 3);

		// THEN
		DerVoltVarModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Enabled")
			.returns(true, from(DerVoltVarModelAccessor::isEnabled))
			.as("Reversion time")
			.returns(600L, from(DerVoltVarModelAccessor::getReversionTime))
			.as("Reversion curve")
			.returns(3, from(DerVoltVarModelAccessor::getReversionCurve))
			;
		// @formatter:on
	}

	@Test
	public void setReversionCurve_invalidIndex() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerVoltVarModelAccessor model = discoverModel(conn);

		// THEN
		// @formatter:off
		for ( int index : new int[] { 0, 4 } ) {
			thenThrownBy(() -> model.setReversionCurve(conn, index))
				.as("Curve index %d rejected", index)
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
	public void pointMap_flat() {
		// WHEN
		Map<String, Object> result = getTestModel().toPointMap(PointMapMode.Flat);

		// THEN
		// @formatter:off
		then(result)
			.as("Scale factors left out")
			.doesNotContainKeys("scaleFactorVoltage", "scaleFactorReactivePower",
					"scaleFactorResponseTime")
			.as("Fixed block enabled")
			.containsEntry("enabled", false)
			.as("Fixed block adopt result")
			.containsEntry("adoptCurveResult", DerAdoptResult.InProgress)
			.as("Fixed block curve count")
			.containsEntry("numberOfCurves", 3)
			.as("Curve 1 active point count")
			.containsEntry("curveActivePointCount_1", 4)
			.as("Curve 1 dependent reference")
			.containsEntry("curveDependentReference_1",
					DerReactivePowerReference.MaximumActivePowerPercent)
			.as("Curve 1 read-only")
			.containsEntry("curveReadOnly_1", true)
			.as("Curve 1 response time")
			.containsEntry("curveOpenLoopResponseTime_1", 10.0f)
			.as("Curve 1 point 1 voltage")
			.containsEntry("pointVoltage_1_1", 90.0f)
			.as("Curve 1 point 4 reactive power")
			.containsEntry("pointReactivePower_1_4", -25.0f)
			.as("Curve 3 read-only")
			.containsEntry("curveReadOnly_3", false)
			.as("Curve 3 point 4 voltage")
			.containsEntry("pointVoltage_3_4", 0.0f)
			.as("No fourth curve")
			.doesNotContainKey("curveReadOnly_4")
			;
		// @formatter:on
	}

	@Test
	public void pointMap_nested() {
		// WHEN
		Map<String, Object> result = getTestModel().toPointMap(PointMapMode.Nested);

		// THEN
		// @formatter:off
		then(result)
			.as("Fixed block points and curves list")
			.containsOnlyKeys("enabled", "adoptCurveRequest", "adoptCurveResult", "numberOfPoints",
					"numberOfCurves", "curves")
			.extractingByKey("curves", LIST)
			.as("Map for each curve")
			.hasSize(3)
			.element(0, MAP)
			.as("Curve 1 read-only")
			.containsEntry("curveReadOnly", true)
			.extractingByKey("points", LIST)
			.as("Map for each curve point")
			.containsExactly(
					Map.of("pointVoltage", 90.0f, "pointReactivePower", 25.0f),
					Map.of("pointVoltage", 100.0f, "pointReactivePower", 0.0f),
					Map.of("pointVoltage", 100.0f, "pointReactivePower", 0.0f),
					Map.of("pointVoltage", 110.0f, "pointReactivePower", -25.0f))
			;
		// @formatter:on
	}

}
