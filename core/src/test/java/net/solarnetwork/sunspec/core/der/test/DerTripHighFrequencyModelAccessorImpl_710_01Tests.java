/* ==================================================================
 * DerTripHighFrequencyModelAccessorImpl_710_01Tests.java - 5/10/2026 6:48:10 pm
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

package net.solarnetwork.sunspec.core.der.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.util.List;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.der.DerCurvePoint;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerTripCurveSet;
import net.solarnetwork.sunspec.api.der.DerTripHighFrequencyModelAccessor;
import net.solarnetwork.sunspec.core.der.DerTripHighFrequencyModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;

/**
 * Test cases for the {@link DerTripHighFrequencyModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class DerTripHighFrequencyModelAccessorImpl_710_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	private DerTripHighFrequencyModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerTripHighFrequencyModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerTripHighFrequencyModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerTripHighFrequencyModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(968, from(DerTripHighFrequencyModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(970, from(DerTripHighFrequencyModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.TripHighFrequency, from(DerTripHighFrequencyModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(7, from(DerTripHighFrequencyModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(64, from(DerTripHighFrequencyModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(135, from(DerTripHighFrequencyModelAccessor::getModelLength))
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
			.isEqualTo(List.of(new DerCurvePoint(0.0f, 0.16f), new DerCurvePoint(62.0f, 0.16f),
					new DerCurvePoint(62.0f, 300.0f), new DerCurvePoint(61.2f, 300.0f),
					new DerCurvePoint(61.2f, 0.0f)))
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

}
