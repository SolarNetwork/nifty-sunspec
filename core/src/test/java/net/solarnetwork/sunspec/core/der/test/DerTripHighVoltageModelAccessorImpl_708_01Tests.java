/* ==================================================================
 * DerTripHighVoltageModelAccessorImpl_708_01Tests.java - 5/10/2026 6:48:10 pm
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
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.der.DerCurvePoint;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerTripCurveSet;
import net.solarnetwork.sunspec.api.der.DerTripHighVoltageModelAccessor;
import net.solarnetwork.sunspec.api.der.DerTripLowVoltageModelAccessor;
import net.solarnetwork.sunspec.api.der.DerTripModelAccessor;
import net.solarnetwork.sunspec.core.der.DerTripHighVoltageModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link DerTripHighVoltageModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class DerTripHighVoltageModelAccessorImpl_708_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	private DerTripHighVoltageModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerTripHighVoltageModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerTripHighVoltageModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerTripHighVoltageModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(724, from(DerTripHighVoltageModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(726, from(DerTripHighVoltageModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.TripHighVoltage, from(DerTripHighVoltageModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(7, from(DerTripHighVoltageModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(49, from(DerTripHighVoltageModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(105, from(DerTripHighVoltageModelAccessor::getModelLength))
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
			.isEqualTo(List.of(new DerCurvePoint(0.0f, 0.16f), new DerCurvePoint(120.0f, 0.16f),
					new DerCurvePoint(120.0f, 2.0f), new DerCurvePoint(110.0f, 2.0f),
					new DerCurvePoint(110.0f, 13.0f)))
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
			.isEqualTo(2)
			;
		then(set.getMomentaryCessationCurve().getPoints())
			.as("Momentary cessation points")
			.isEqualTo(List.of(new DerCurvePoint(0.0f, 0.0f), new DerCurvePoint(120.0f, 0.08f)))
			;
		then(sets.get(1).isReadOnly())
			.as("Stored set read-only")
			.isFalse()
			;
		// @formatter:on
	}

	@Test
	public void findTypedModel_distinctFromLowVoltage() {
		// GIVEN
		ModelData data = ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA);

		// THEN
		// @formatter:off
		then(data.findTypedModel(DerTripHighVoltageModelAccessor.class).getModelId())
			.as("High voltage model found by its own type")
			.isEqualTo(DerModelId.TripHighVoltage)
			;
		then(data.findTypedModel(DerTripLowVoltageModelAccessor.class).getModelId())
			.as("Low voltage model found by its own type")
			.isEqualTo(DerModelId.TripLowVoltage)
			;
		then(data.findTypedModel(DerTripModelAccessor.class).getModelId())
			.as("Shared type finds the first trip model")
			.isEqualTo(DerModelId.TripLowVoltage)
			;
		// @formatter:on
	}

}
