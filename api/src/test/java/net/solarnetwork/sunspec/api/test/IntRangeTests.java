/* ==================================================================
 * IntRangeTests.java - 15/01/2020 1:40:13 pm
 *
 * Copyright 2020 SolarNetwork.net Dev Team
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

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.IntRange;

/**
 * Test cases for the {@link IntRange} class.
 *
 * @author matt
 * @version 1.0
 */
public class IntRangeTests {

	@Test
	public void construct() {
		// WHEN
		IntRange r = new IntRange(1, 2);

		// THEN
		// @formatter:off
		then(r)
			.as("Min set")
			.returns(1, from(IntRange::min))
			.as("Max set")
			.returns(2, from(IntRange::max))
			;
		// @formatter:on
	}

	@Test
	public void construct_flipped() {
		// WHEN
		IntRange r = new IntRange(2, 1);

		// THEN
		// @formatter:off
		then(r)
			.as("Min set from smaller value")
			.returns(1, from(IntRange::min))
			.as("Max set from larger value")
			.returns(2, from(IntRange::max))
			;
		// @formatter:on
	}

	@Test
	public void construct_singleton() {
		// WHEN
		IntRange r = new IntRange(1, 1);

		// THEN
		// @formatter:off
		then(r)
			.as("Min set")
			.returns(1, from(IntRange::min))
			.as("Max set")
			.returns(1, from(IntRange::max))
			;
		// @formatter:on
	}

	@Test
	public void length() {
		// @formatter:off
		then(new IntRange(1, 10).length())
			.as("Length includes min and max")
			.isEqualTo(10)
			;
		then(new IntRange(1, 1).length())
			.as("Singleton length")
			.isEqualTo(1)
			;
		// @formatter:on
	}

	@Test
	public void equals() {
		// GIVEN
		IntRange r = new IntRange(1, 2);

		// THEN
		// @formatter:off
		then(r)
			.as("Equal to range with same min and max")
			.isEqualTo(new IntRange(1, 2))
			.as("Equal to range constructed with min and max reversed")
			.isEqualTo(new IntRange(2, 1))
			.as("Not equal to range with different min")
			.isNotEqualTo(new IntRange(0, 2))
			.as("Not equal to range with different max")
			.isNotEqualTo(new IntRange(1, 3))
			.as("Not equal to other type")
			.isNotEqualTo("[1..2]")
			;
		// @formatter:on
	}

	@Test
	public void hashCode_consistent() {
		// @formatter:off
		then(new IntRange(1, 2).hashCode())
			.as("Equal ranges have same hash code")
			.isEqualTo(new IntRange(1, 2).hashCode())
			.as("Range constructed with min and max reversed has same hash code")
			.isEqualTo(new IntRange(2, 1).hashCode())
			;
		// @formatter:on
	}

	@Test
	public void compare_lessThan() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(2, 2);

		// THEN
		// @formatter:off
		then(r1.compareTo(r2))
			.as("Range with smaller min compares less")
			.isNegative()
			;
		then(r2.compareTo(r1))
			.as("Range with larger min compares greater")
			.isPositive()
			;
		// @formatter:on
	}

	@Test
	public void compare_equal() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(1, 2);

		// THEN
		// @formatter:off
		then(r1.compareTo(r2))
			.as("Equal ranges compare equal")
			.isZero()
			;
		then(r2.compareTo(r1))
			.as("Equal ranges compare equal inverse")
			.isZero()
			;
		// @formatter:on
	}

	@Test
	public void compare_equalStart() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(1, 3);

		// THEN
		// @formatter:off
		then(r1.compareTo(r2))
			.as("Ranges compare only based on min")
			.isZero()
			;
		then(r2.compareTo(r1))
			.as("Ranges compare only based on min inverse")
			.isZero()
			;
		// @formatter:on
	}

	@Test
	public void compare_within() {
		// GIVEN
		IntRange r1 = new IntRange(1, 10);
		IntRange r2 = new IntRange(3, 6);

		// THEN
		// @formatter:off
		then(r1.compareTo(r2))
			.as("Ranges compare only based on min")
			.isNegative()
			;
		then(r2.compareTo(r1))
			.as("Ranges compare only based on min inverse")
			.isPositive()
			;
		// @formatter:on
	}

	@Test
	public void adjacent() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(3, 4);

		// THEN
		// @formatter:off
		then(r1.adjacentTo(r2))
			.as("Ranges adjacent")
			.isTrue()
			;
		then(r2.adjacentTo(r1))
			.as("Ranges adjacent inverse")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void adjacent_gap() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(4, 5);

		// THEN
		// @formatter:off
		then(r1.adjacentTo(r2))
			.as("Gapped ranges not adjacent")
			.isFalse()
			;
		then(r2.adjacentTo(r1))
			.as("Gapped ranges not adjacent inverse")
			.isFalse()
			;
		// @formatter:on
	}

	@Test
	public void adjacent_overlap() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(2, 3);

		// THEN
		// @formatter:off
		then(r1.adjacentTo(r2))
			.as("Overlapping ranges not adjacent")
			.isFalse()
			;
		then(r2.adjacentTo(r1))
			.as("Overlapping ranges not adjacent inverse")
			.isFalse()
			;
		// @formatter:on
	}

	@Test
	public void adjacent_maxValue() {
		// GIVEN
		IntRange r1 = new IntRange(Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 1);
		IntRange r2 = new IntRange(Integer.MAX_VALUE, Integer.MAX_VALUE);

		// THEN
		// @formatter:off
		then(r1.adjacentTo(r2))
			.as("Ranges adjacent")
			.isTrue()
			;
		then(r2.adjacentTo(r1))
			.as("Ranges adjacent inverse")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void adjacent_wrapAround() {
		// GIVEN
		IntRange r1 = new IntRange(Integer.MIN_VALUE, Integer.MIN_VALUE);
		IntRange r2 = new IntRange(Integer.MAX_VALUE, Integer.MAX_VALUE);

		// THEN
		// @formatter:off
		then(r1.adjacentTo(r2))
			.as("Extreme ranges not adjacent")
			.isFalse()
			;
		then(r2.adjacentTo(r1))
			.as("Extreme ranges not adjacent inverse")
			.isFalse()
			;
		then(r1.canMergeWith(r2))
			.as("Extreme ranges cannot merge")
			.isFalse()
			;
		then(r2.canMergeWith(r1))
			.as("Extreme ranges cannot merge inverse")
			.isFalse()
			;
		// @formatter:on
	}

	@Test
	public void intersects_overlap() {
		// GIVEN
		IntRange r1 = new IntRange(1, 3);
		IntRange r2 = new IntRange(2, 5);

		// THEN
		// @formatter:off
		then(r1.intersects(r2))
			.as("Overlapping ranges intersect")
			.isTrue()
			;
		then(r2.intersects(r1))
			.as("Overlapping ranges intersect inverse")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void intersects_gap() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(4, 5);

		// THEN
		// @formatter:off
		then(r1.intersects(r2))
			.as("Gapped ranges do not intersect")
			.isFalse()
			;
		then(r2.intersects(r1))
			.as("Gapped ranges do not intersect inverse")
			.isFalse()
			;
		// @formatter:on
	}

	@Test
	public void intersects_touch() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(2, 3);

		// THEN
		// @formatter:off
		then(r1.intersects(r2))
			.as("Touching ranges intersect")
			.isTrue()
			;
		then(r2.intersects(r1))
			.as("Touching ranges intersect inverse")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void intersects_adjacent() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(3, 4);

		// THEN
		// @formatter:off
		then(r1.intersects(r2))
			.as("Adjacent ranges do not intersect")
			.isFalse()
			;
		then(r2.intersects(r1))
			.as("Adjacent ranges do not intersect inverse")
			.isFalse()
			;
		// @formatter:on
	}

	@Test
	public void canMerge_adjacent() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(3, 4);

		// THEN
		// @formatter:off
		then(r1.canMergeWith(r2))
			.as("Adjacent ranges can merge")
			.isTrue()
			;
		then(r2.canMergeWith(r1))
			.as("Adjacent ranges can merge inverse")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void canMerge_overlap() {
		// GIVEN
		IntRange r1 = new IntRange(1, 3);
		IntRange r2 = new IntRange(2, 5);

		// THEN
		// @formatter:off
		then(r1.canMergeWith(r2))
			.as("Overlapping ranges can merge")
			.isTrue()
			;
		then(r2.canMergeWith(r1))
			.as("Overlapping ranges can merge inverse")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void canMerge_subset() {
		// GIVEN
		IntRange r1 = new IntRange(1, 10);
		IntRange r2 = new IntRange(3, 6);

		// THEN
		// @formatter:off
		then(r1.canMergeWith(r2))
			.as("Subset ranges can merge")
			.isTrue()
			;
		then(r2.canMergeWith(r1))
			.as("Subset ranges can merge inverse")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void canMerge_gap() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(4, 5);

		// THEN
		// @formatter:off
		then(r1.canMergeWith(r2))
			.as("Gapped ranges cannot merge")
			.isFalse()
			;
		then(r2.canMergeWith(r1))
			.as("Gapped ranges cannot merge inverse")
			.isFalse()
			;
		// @formatter:on
	}

	@Test
	public void merge_adjacent() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(3, 4);

		// THEN
		// @formatter:off
		then(r1.mergeWith(r2))
			.as("Adjacent ranges merge")
			.isEqualTo(new IntRange(1, 4))
			;
		then(r2.mergeWith(r1))
			.as("Adjacent ranges merge inverse")
			.isEqualTo(new IntRange(1, 4))
			;
		// @formatter:on
	}

	@Test
	public void merge_overlap() {
		// GIVEN
		IntRange r1 = new IntRange(1, 3);
		IntRange r2 = new IntRange(2, 5);

		// THEN
		// @formatter:off
		then(r1.mergeWith(r2))
			.as("Overlapping ranges merge")
			.isEqualTo(new IntRange(1, 5))
			;
		then(r2.mergeWith(r1))
			.as("Overlapping ranges merge inverse")
			.isEqualTo(new IntRange(1, 5))
			;
		// @formatter:on
	}

	@Test
	public void merge_subset() {
		// GIVEN
		IntRange r1 = new IntRange(1, 10);
		IntRange r2 = new IntRange(3, 6);

		// THEN
		// @formatter:off
		then(r1.mergeWith(r2))
			.as("Subset ranges merge to larger range")
			.isSameAs(r1)
			;
		then(r2.mergeWith(r1))
			.as("Subset ranges merge to larger range inverse")
			.isSameAs(r1)
			;
		// @formatter:on
	}

	@Test
	public void merge_gap() {
		// GIVEN
		IntRange r1 = new IntRange(1, 2);
		IntRange r2 = new IntRange(4, 5);

		// THEN
		// @formatter:off
		thenThrownBy(() -> r1.mergeWith(r2))
			.as("Gapped ranges cannot merge")
			.isInstanceOf(IllegalArgumentException.class)
			;
		thenThrownBy(() -> r2.mergeWith(r1))
			.as("Gapped ranges cannot merge inverse")
			.isInstanceOf(IllegalArgumentException.class)
			;
		// @formatter:on
	}

	@Test
	public void stringValue() {
		// @formatter:off
		then(new IntRange(1, 2))
			.as("String shows inclusive min and max")
			.hasToString("[1..2]")
			;
		// @formatter:on
	}

}
