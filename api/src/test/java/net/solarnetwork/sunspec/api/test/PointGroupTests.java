/* ==================================================================
 * PointGroupTests.java - 7/10/2026 5:41:27 pm
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

package net.solarnetwork.sunspec.api.test;

import static net.solarnetwork.sunspec.api.DataClassification.ScaleFactor;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.PointGroup;
import net.solarnetwork.sunspec.api.PointGroupList;
import net.solarnetwork.sunspec.api.PointMapMode;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Test cases for the {@link PointGroup} class.
 *
 * @author matt
 * @version 1.0
 */
public class PointGroupTests {

	/** Test points. */
	private enum TestRegister implements ModbusReference {

		Alpha(0, UInt16),

		BetaPhaseA(1, UInt16),

		ScaleFactorAlpha(2, Int16, ScaleFactor),

		Gamma(3, UInt16),

		;

		private final int address;
		private final ModbusDataType dataType;
		private final @Nullable DataClassification classification;

		private TestRegister(int address, ModbusDataType dataType) {
			this(address, dataType, null);
		}

		private TestRegister(int address, ModbusDataType dataType,
				@Nullable DataClassification classification) {
			this.address = address;
			this.dataType = dataType;
			this.classification = classification;
		}

		@Override
		public int getAddress() {
			return address;
		}

		@Override
		public ModbusDataType getDataType() {
			return dataType;
		}

		@Override
		public int getWordLength() {
			return dataType.getWordLength();
		}

		@Override
		public @Nullable DataClassification getClassification() {
			return classification;
		}

		@Override
		public String getName() {
			return name();
		}

	}

	/** A test group with fixed point values. */
	private static final class TestGroup implements PointGroup {

		private final Map<ModbusReference, @Nullable Object> values = new LinkedHashMap<>();
		private final List<PointGroupList> groups = new ArrayList<>();

		private TestGroup with(ModbusReference point, @Nullable Object value) {
			values.put(point, value);
			return this;
		}

		private TestGroup with(PointGroupList list) {
			groups.add(list);
			return this;
		}

		@Override
		public Collection<? extends ModbusReference> getPointReferences() {
			return values.keySet();
		}

		@Override
		public @Nullable Object getPointValue(ModbusReference point) {
			return values.get(point);
		}

		@Override
		public List<PointGroupList> getPointGroups() {
			return groups;
		}

	}

	/**
	 * Create a group with a repeating group of curves, the first with a single
	 * group with a repeating group of points.
	 *
	 * @return the group
	 */
	private static TestGroup curvesGroup() {
		// @formatter:off
		return new TestGroup()
				.with(TestRegister.Alpha, 1)
				.with(PointGroupList.repeating("Curves", List.of(
						new TestGroup()
							.with(TestRegister.Gamma, 3)
							.with(PointGroupList.single("MustTrip", new TestGroup()
								.with(PointGroupList.repeating("Points", List.of(
									new TestGroup().with(TestRegister.Alpha, 4),
									new TestGroup().with(TestRegister.Alpha, 5)))))),
						new TestGroup()
							.with(TestRegister.Gamma, 6))));
		// @formatter:on
	}

	@Test
	public void flat_points() {
		// GIVEN
		// @formatter:off
		TestGroup group = new TestGroup()
				.with(TestRegister.Alpha, 1)
				.with(TestRegister.BetaPhaseA, 2)
				.with(TestRegister.ScaleFactorAlpha, -1)
				.with(TestRegister.Gamma, null);
		// @formatter:on

		// WHEN
		Map<String, Object> result = group.toPointMap(PointMapMode.Flat);

		// THEN
		// @formatter:off
		then(result)
			.as("Points mapped to point keys, without scale factors or missing values")
			.containsExactly(
					Map.entry("alpha", 1),
					Map.entry("beta_a", 2))
			;
		// @formatter:on
	}

	@Test
	public void flat_nested() {
		// GIVEN
		TestGroup group = curvesGroup();

		// WHEN
		Map<String, Object> result = group.toPointMap(PointMapMode.Flat);

		// THEN
		// @formatter:off
		then(result)
			.as("Nested points mapped with index and group key suffixes")
			.containsExactly(
					Map.entry("alpha", 1),
					Map.entry("gamma_1", 3),
					Map.entry("alpha_1_mustTrip_1", 4),
					Map.entry("alpha_1_mustTrip_2", 5),
					Map.entry("gamma_2", 6))
			;
		// @formatter:on
	}

	@Test
	public void nested() {
		// GIVEN
		TestGroup group = curvesGroup();

		// WHEN
		Map<String, Object> result = group.toPointMap(PointMapMode.Nested);

		// THEN
		// @formatter:off
		then(result)
			.as("Root point and curves list")
			.containsOnlyKeys("alpha", "curves")
			.containsEntry("alpha", 1)
			.extractingByKey("curves", LIST)
			.as("Map for each curve")
			.containsExactly(
					Map.of("gamma", 3, "mustTrip",
							Map.of("points", List.of(Map.of("alpha", 4), Map.of("alpha", 5)))),
					Map.of("gamma", 6))
			;
		// @formatter:on
	}

	@Test
	public void nested_emptyGroups() {
		// GIVEN
		// @formatter:off
		TestGroup group = new TestGroup()
				.with(TestRegister.Alpha, 1)
				.with(PointGroupList.repeating("Curves", List.of()))
				.with(PointGroupList.single("MustTrip", new TestGroup()
						.with(TestRegister.Gamma, null)))
				.with(PointGroupList.repeating("Points", List.of(
						new TestGroup().with(TestRegister.Gamma, null),
						new TestGroup().with(TestRegister.Gamma, 2))));
		// @formatter:on

		// WHEN
		Map<String, Object> result = group.toPointMap(PointMapMode.Nested);

		// THEN
		// @formatter:off
		then(result)
			.as("Groups without instances or values left out")
			.containsOnlyKeys("alpha", "points")
			.extractingByKey("points", LIST)
			.as("Repeating group instances without values kept, to preserve their positions")
			.containsExactly(Map.of(), Map.of("gamma", 2))
			;
		// @formatter:on
	}

	@Test
	public void keyMapper() {
		// GIVEN
		TestGroup group = curvesGroup();

		// WHEN
		Map<String, Object> flat = group.toPointMap(PointMapMode.Flat, n -> "x" + n);
		Map<String, Object> nested = group.toPointMap(PointMapMode.Nested, n -> "x" + n);

		// THEN
		// @formatter:off
		then(flat)
			.as("Point and group names mapped by key mapper")
			.containsOnlyKeys("xAlpha", "xGamma_1", "xAlpha_1_xMustTrip_1", "xAlpha_1_xMustTrip_2",
					"xGamma_2")
			;
		then(nested)
			.as("Group names mapped by key mapper")
			.containsOnlyKeys("xAlpha", "xCurves")
			.extractingByKey("xCurves", LIST)
			.element(0, MAP)
			.containsOnlyKeys("xGamma", "xMustTrip")
			;
		// @formatter:on
	}

	@Test
	public void keyCollision() {
		// GIVEN
		TestGroup group = new TestGroup().with(TestRegister.Alpha, 1).with(TestRegister.Gamma, 2);

		// WHEN
		@SuppressWarnings("unused")
		Throwable t = catchThrowable(() -> group.toPointMap(PointMapMode.Flat, n -> "same"));

		// THEN
		// @formatter:off
		then(t)
			.as("Key collision rejected")
			.isInstanceOf(IllegalStateException.class)
			.as("Message names both points and the key")
			.hasMessage("Both Alpha and Gamma map to the key [same].")
			;
		// @formatter:on
	}

	@Test
	public void groupList_singleWithMany() {
		// @formatter:off
		thenThrownBy(() -> new PointGroupList("MustTrip", false,
				List.of(new TestGroup(), new TestGroup())))
			.as("Non-repeating group with more than one instance rejected")
			.isInstanceOf(IllegalArgumentException.class)
			;
		// @formatter:on
	}

}
