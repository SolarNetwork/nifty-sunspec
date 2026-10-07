/* ==================================================================
 * DerTripLowVoltageModelAccessorImpl_707_01Tests.java - 5/10/2026 6:48:10 pm
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.PointMapMode;
import net.solarnetwork.sunspec.api.der.DerAdoptResult;
import net.solarnetwork.sunspec.api.der.DerCurve;
import net.solarnetwork.sunspec.api.der.DerCurvePoint;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerTripCurveSet;
import net.solarnetwork.sunspec.api.der.DerTripLowVoltageModelAccessor;
import net.solarnetwork.sunspec.core.der.DerTripLowVoltageModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * Test cases for the {@link DerTripLowVoltageModelAccessorImpl} class,
 * including the curve set support shared by the other DER trip models.
 *
 * @author matt
 * @version 1.0
 */
public class DerTripLowVoltageModelAccessorImpl_707_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	/** The model block address. */
	private static final int BLOCK_ADDRESS = 583;

	/**
	 * The curve set 2 address: 7 fixed registers, then 67 registers per set.
	 */
	private static final int SET_2_ADDRESS = BLOCK_ADDRESS + 7 + 67;

	/**
	 * The curve set 2 must trip curve address, after the read-only register.
	 */
	private static final int SET_2_MUST_TRIP_ADDRESS = SET_2_ADDRESS + 1;

	/** The curve set 2 may trip curve address, after the 22 register curve. */
	private static final int SET_2_MAY_TRIP_ADDRESS = SET_2_MUST_TRIP_ADDRESS + 22;

	private static final List<DerCurvePoint> MUST_TRIP_POINTS = List.of(new DerCurvePoint(0.0f, 0.16f),
			new DerCurvePoint(45.0f, 0.16f), new DerCurvePoint(45.0f, 10.0f),
			new DerCurvePoint(70.0f, 10.0f), new DerCurvePoint(70.0f, 10.0f));

	private static final List<DerCurvePoint> MOM_CESS_POINTS = List.of(new DerCurvePoint(0.0f, 0.08f),
			new DerCurvePoint(50.0f, 0.08f));

	private static final List<DerCurvePoint> NEW_MUST_TRIP_POINTS = List.of(
			new DerCurvePoint(0.0f, 0.16f), new DerCurvePoint(50.0f, 0.16f),
			new DerCurvePoint(50.0f, 2.0f), new DerCurvePoint(88.0f, 2.0f),
			new DerCurvePoint(88.0f, 21.0f));

	private DerTripLowVoltageModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerTripLowVoltageModelAccessor.class);
	}

	private RecordingModbusConnection writableConnection() {
		return ModelDataUtils.getWritableModbusConnection(getClass(), TEST_DATA);
	}

	private static DerTripLowVoltageModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(DerTripLowVoltageModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerTripLowVoltageModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerTripLowVoltageModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(581, from(DerTripLowVoltageModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BLOCK_ADDRESS, from(DerTripLowVoltageModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.TripLowVoltage, from(DerTripLowVoltageModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(7, from(DerTripLowVoltageModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(67, from(DerTripLowVoltageModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model repeating instance count")
			.returns(2, from(DerTripLowVoltageModelAccessor::getRepeatingBlockInstanceCount))
			.as("Model length")
			.returns(141, from(DerTripLowVoltageModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void curveManagement() {
		// GIVEN
		DerTripLowVoltageModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Enabled")
			.returns(true, from(DerTripLowVoltageModelAccessor::isEnabled))
			.as("Curve set count")
			.returns(2, from(DerTripLowVoltageModelAccessor::getCurveSetCount))
			.as("Curve point count")
			.returns(7, from(DerTripLowVoltageModelAccessor::getCurvePointCount))
			.as("Adopt curve request")
			.returns(0, from(DerTripLowVoltageModelAccessor::getAdoptCurveRequest))
			.as("Adopt curve result")
			.returns(DerAdoptResult.InProgress,
					from(DerTripLowVoltageModelAccessor::getAdoptCurveResult))
			;
		// @formatter:on
	}

	@Test
	public void curveSets() {
		// WHEN
		List<DerTripCurveSet> sets = getTestModel().getCurveSets();

		// THEN
		// @formatter:off
		then(sets)
			.as("Curve sets")
			.hasSize(2)
			;
		// @formatter:on
		for ( DerTripCurveSet set : sets ) {
			String prefix = "Set " + set.getIndex();
			boolean active = set.getIndex() == 1;
			// @formatter:off
			then(set.isReadOnly())
				.as(prefix + " read-only")
				.isEqualTo(active)
				;
			// @formatter:on

			DerCurve mustTrip = set.getMustTripCurve();
			// @formatter:off
			then(mustTrip)
				.as(prefix + " must trip index")
				.returns(set.getIndex(), from(DerCurve::getIndex))
				.as(prefix + " must trip read-only")
				.returns(active, from(DerCurve::isReadOnly))
				.as(prefix + " must trip active points")
				.returns(5, from(DerCurve::getActivePointCount))
				.as(prefix + " must trip points")
				.returns(MUST_TRIP_POINTS, from(DerCurve::getPoints))
				;
			// @formatter:on

			DerCurve mayTrip = set.getMayTripCurve();
			// @formatter:off
			then(mayTrip)
				.as(prefix + " may trip active points not implemented")
				.returns(null, from(DerCurve::getActivePointCount))
				.as(prefix + " may trip points")
				.returns(Collections.emptyList(), from(DerCurve::getPoints))
				;
			// @formatter:on

			DerCurve momCess = set.getMomentaryCessationCurve();
			// @formatter:off
			then(momCess)
				.as(prefix + " momentary cessation active points")
				.returns(2, from(DerCurve::getActivePointCount))
				.as(prefix + " momentary cessation points")
				.returns(MOM_CESS_POINTS, from(DerCurve::getPoints))
				;
			// @formatter:on
		}
	}

	@Test
	public void writeMustTripCurve() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerCurve curve = discoverModel(conn).getCurveSets().get(1).getMustTripCurve();

		// WHEN
		curve.setPoints(conn, NEW_MUST_TRIP_POINTS);

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Points written in one request, then the active point count")
			.isEqualTo(List.of(List.of(SET_2_MUST_TRIP_ADDRESS + 1, 15),
					List.of(SET_2_MUST_TRIP_ADDRESS, 1)))
			;
		// @formatter:on

		List<DerTripCurveSet> sets = discoverModel(conn).getCurveSets();
		// @formatter:off
		then(sets.get(1).getMustTripCurve().getPoints())
			.as("Must trip points")
			.isEqualTo(NEW_MUST_TRIP_POINTS)
			;
		then(sets.get(1).getMomentaryCessationCurve().getPoints())
			.as("Momentary cessation unchanged")
			.isEqualTo(MOM_CESS_POINTS)
			;
		then(sets.get(0).getMustTripCurve().getPoints())
			.as("Active set unchanged")
			.isEqualTo(MUST_TRIP_POINTS)
			;
		// @formatter:on
	}

	@Test
	public void clearMayTripCurve() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerCurve curve = discoverModel(conn).getCurveSets().get(1).getMayTripCurve();

		// WHEN
		curve.setPoints(conn, List.of());

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Only the active point count written")
			.isEqualTo(List.of(List.of(SET_2_MAY_TRIP_ADDRESS, 1)))
			;
		// @formatter:on
		DerCurve device = discoverModel(conn).getCurveSets().get(1).getMayTripCurve();
		// @formatter:off
		then(device)
			.as("Active point count")
			.returns(0, from(DerCurve::getActivePointCount))
			.as("Points")
			.returns(Collections.emptyList(), from(DerCurve::getPoints))
			;
		// @formatter:on
	}

	@Test
	public void setActivePointCount() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerCurve curve = discoverModel(conn).getCurveSets().get(1).getMomentaryCessationCurve();

		// WHEN
		curve.setActivePointCount(conn, 1);

		// THEN
		// @formatter:off
		then(discoverModel(conn).getCurveSets().get(1).getMomentaryCessationCurve().getPoints())
			.as("Points")
			.isEqualTo(MOM_CESS_POINTS.subList(0, 1))
			;
		// @formatter:on
	}

	@Test
	public void invalidPointCount() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerCurve curve = discoverModel(conn).getCurveSets().get(1).getMustTripCurve();
		DerCurvePoint p = new DerCurvePoint(50.0f, 1.0f);

		// THEN
		// @formatter:off
		thenThrownBy(() -> curve.setPoints(conn, List.of(p, p, p, p, p, p, p, p)))
			.as("More points than the curve point count rejected")
			.isInstanceOf(IllegalArgumentException.class)
			;
		for ( int count : new int[] { -1, 8 } ) {
			thenThrownBy(() -> curve.setActivePointCount(conn, count))
				.as("Active point count %d rejected", count)
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
	public void readOnlyCurveSet() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerTripCurveSet set = discoverModel(conn).getCurveSets().get(0);

		// THEN
		// @formatter:off
		thenThrownBy(() -> set.getMustTripCurve().setPoints(conn, NEW_MUST_TRIP_POINTS))
			.as("Writing points to the read-only curve set rejected")
			.isInstanceOf(UnsupportedOperationException.class)
			;
		thenThrownBy(() -> set.getMayTripCurve().setActivePointCount(conn, 0))
			.as("Writing the active point count to the read-only curve set rejected")
			.isInstanceOf(UnsupportedOperationException.class)
			;
		then(conn.getWrites())
			.as("Nothing written")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void adoptCurveSet() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = writableConnection();
		DerTripLowVoltageModelAccessor model = discoverModel(conn);

		// WHEN
		final List<Throwable> errors = new ArrayList<>();
		for ( int index : new int[] { 1, 3 } ) {
			errors.add(catchThrowable(() -> model.adoptCurveSet(conn, index)));
		}
		model.adoptCurveSet(conn, 2);
		model.setEnabled(conn, false);

		// THEN
		// @formatter:off
		then(errors)
			.as("Curve set indexes 1 and 3 rejected")
			.hasOnlyElementsOfType(IllegalArgumentException.class)
			;
		then(conn.getWrites())
			.as("Writes")
			.isEqualTo(List.of(List.of(BLOCK_ADDRESS + 1, 1), List.of(BLOCK_ADDRESS, 1)))
			;
		// @formatter:on
		DerTripLowVoltageModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Adopt curve request")
			.returns(2, from(DerTripLowVoltageModelAccessor::getAdoptCurveRequest))
			.as("Enabled")
			.returns(false, from(DerTripLowVoltageModelAccessor::isEnabled))
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
			.doesNotContainKeys("scaleFactorVoltage", "scaleFactorTime")
			.as("Curve set count")
			.containsEntry("numberOfCurveSets", 2)
			.as("Curve set 1 read-only")
			.containsEntry("curveSetReadOnly_1", true)
			.as("Curve set 1 must trip active point count")
			.containsEntry("curveActivePointCount_1_mustTrip", 5)
			.as("Curve set 1 must trip point 2 voltage")
			.containsEntry("pointVoltage_1_mustTrip_2", 45.0f)
			.as("Curve set 1 must trip point 2 time")
			.containsEntry("pointVoltageTime_1_mustTrip_2", 0.16f)
			.as("Curve set 2 momentary cessation point 2 voltage")
			.containsEntry("pointVoltage_2_momentaryCessation_2", 50.0f)
			.as("May trip curve not implemented")
			.doesNotContainKey("curveActivePointCount_1_mayTrip")
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
			.extractingByKey("curveSets", LIST)
			.as("Map for each curve set")
			.hasSize(2)
			.element(0, MAP)
			.as("Curve set 1 read-only")
			.containsEntry("curveSetReadOnly", true)
			.as("Curve set 1 without the unavailable may trip curve")
			.containsOnlyKeys("curveSetReadOnly", "mustTrip", "momentaryCessation")
			.extractingByKey("momentaryCessation", MAP)
			.as("Momentary cessation curve")
			.containsEntry("curveActivePointCount", 2)
			.extractingByKey("points", LIST)
			.as("Momentary cessation points")
			.containsExactly(
					Map.of("pointVoltage", 0.0f, "pointVoltageTime", 0.08f),
					Map.of("pointVoltage", 50.0f, "pointVoltageTime", 0.08f))
			;
		// @formatter:on
	}

}
