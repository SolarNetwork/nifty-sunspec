/* ==================================================================
 * DerTripLowFrequencyModelAccessorImpl_709_01Tests.java - 5/10/2026 6:48:10 pm
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
import java.util.List;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.der.DerCurve;
import net.solarnetwork.sunspec.api.der.DerCurvePoint;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerTripCurveSet;
import net.solarnetwork.sunspec.api.der.DerTripLowFrequencyModelAccessor;
import net.solarnetwork.sunspec.core.der.DerTripLowFrequencyModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;

/**
 * Test cases for the {@link DerTripLowFrequencyModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class DerTripLowFrequencyModelAccessorImpl_709_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	private DerTripLowFrequencyModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerTripLowFrequencyModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerTripLowFrequencyModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerTripLowFrequencyModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(831, from(DerTripLowFrequencyModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(833, from(DerTripLowFrequencyModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.TripLowFrequency, from(DerTripLowFrequencyModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(7, from(DerTripLowFrequencyModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(64, from(DerTripLowFrequencyModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(135, from(DerTripLowFrequencyModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void activeCurveSet() {
		// WHEN
		List<DerTripCurveSet> sets = getTestModel().getCurveSets();

		// THEN
		// @formatter:off
		then(sets)
			.as("Curve sets")
			.hasSize(2)
			;
		// @formatter:on
		DerTripCurveSet set = sets.get(0);
		// @formatter:off
		then(set.isReadOnly())
			.as("Read-only")
			.isTrue()
			;
		then(set.getMustTripCurve().getPoints())
			.as("Must trip points")
			.isEqualTo(List.of(new DerCurvePoint(0.0f, 0.16f), new DerCurvePoint(56.5f, 0.16f),
					new DerCurvePoint(56.5f, 300.0f), new DerCurvePoint(58.5f, 300.0f),
					new DerCurvePoint(58.5f, 0.0f)))
			;
		then(set.getMayTripCurve().getActivePointCount())
			.as("May trip active point count")
			.isEqualTo(0)
			;
		then(set.getMayTripCurve().getPoints())
			.as("May trip points")
			.isEmpty()
			;
		then(set.getMomentaryCessationCurve().getActivePointCount())
			.as("Momentary cessation active point count")
			.isEqualTo(0)
			;
		then(set.getMomentaryCessationCurve().getPoints())
			.as("Momentary cessation points")
			.isEmpty()
			;
		then(sets.get(1).isReadOnly())
			.as("Stored set read-only")
			.isFalse()
			;
		then(sets.get(1).getMustTripCurve().getPoints())
			.as("Stored set must trip points")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void writeMustTripCurve() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		DerCurve curve = ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(DerTripLowFrequencyModelAccessor.class).getCurveSets().get(1)
				.getMustTripCurve();
		List<DerCurvePoint> points = List.of(new DerCurvePoint(0.0f, 0.16f),
				new DerCurvePoint(57.0f, 0.16f), new DerCurvePoint(57.0f, 299.0f),
				new DerCurvePoint(58.5f, 299.0f));

		// WHEN
		curve.setPoints(conn, points);

		// THEN
		// set 2 starts after the 7 register fixed block and 64 register set 1, and
		// each frequency point has a 2 register frequency and a 2 register time
		int mustTripAddress = 833 + 7 + 64 + 1;
		// @formatter:off
		then(conn.getWrites())
			.as("Points written in one request, then the active point count")
			.isEqualTo(List.of(List.of(mustTripAddress + 1, 16), List.of(mustTripAddress, 1)))
			;
		then(ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(DerTripLowFrequencyModelAccessor.class).getCurveSets().get(1)
				.getMustTripCurve().getPoints())
			.as("Points")
			.isEqualTo(points)
			;
		// @formatter:on
	}

}
