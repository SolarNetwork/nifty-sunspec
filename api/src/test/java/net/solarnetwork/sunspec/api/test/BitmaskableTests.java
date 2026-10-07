/* ==================================================================
 * BitmaskableTests.java - 7 Oct 2026 8:59:51 am
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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import java.util.EnumSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.Bitmaskable;

/**
 * Test cases for the {@link Bitmaskable} class.
 *
 * @author matt
 * @version 1.0
 */
public class BitmaskableTests {

	/** Test flags. */
	private enum TestFlag implements Bitmaskable {

		A(0),

		B(1),

		C(15),

		D(30),

		;

		private final int offset;

		private TestFlag(int offset) {
			this.offset = offset;
		}

		@Override
		public int bitmaskBitOffset() {
			return offset;
		}

	}

	@Test
	public void bitmaskValue_null() {
		// @formatter:off
		then(Bitmaskable.bitmaskValue(null))
			.as("No bits set for null")
			.isZero()
			;
		// @formatter:on
	}

	@Test
	public void bitmaskValue_empty() {
		// @formatter:off
		then(Bitmaskable.bitmaskValue(EnumSet.noneOf(TestFlag.class)))
			.as("No bits set for empty set")
			.isZero()
			;
		// @formatter:on
	}

	@Test
	public void bitmaskValue() {
		// WHEN
		int mask = Bitmaskable.bitmaskValue(EnumSet.of(TestFlag.A, TestFlag.C, TestFlag.D));

		// THEN
		// @formatter:off
		then(mask)
			.as("Bit set for each value offset")
			.isEqualTo(0x40008001)
			;
		// @formatter:on
	}

	@Test
	public void setForBitmask_zero() {
		// @formatter:off
		then(Bitmaskable.setForBitmask(0, TestFlag.class))
			.as("No values for zero mask")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void setForBitmask_negative() {
		// @formatter:off
		then(Bitmaskable.setForBitmask(-1, TestFlag.class))
			.as("No values for negative mask")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void setForBitmask() {
		// WHEN
		Set<TestFlag> result = Bitmaskable.setForBitmask(0x40000002, TestFlag.class);

		// THEN
		// @formatter:off
		then(result)
			.as("Values for bits set")
			.isEqualTo(EnumSet.of(TestFlag.B, TestFlag.D))
			.as("Enum set returned")
			.isInstanceOf(EnumSet.class)
			;
		// @formatter:on
	}

	@Test
	public void setForBitmask_unknownBits() {
		// WHEN
		Set<TestFlag> result = Bitmaskable.setForBitmask(0x0007, TestFlag.class);

		// THEN
		// @formatter:off
		then(result)
			.as("Bits without a value ignored")
			.isEqualTo(EnumSet.of(TestFlag.A, TestFlag.B))
			;
		// @formatter:on
	}

	@Test
	public void setForBitmask_values() {
		// WHEN
		Set<TestFlag> result = Bitmaskable.setForBitmask(0x8001, TestFlag.values());

		// THEN
		// @formatter:off
		then(result)
			.as("Values for bits set")
			.containsExactlyInAnyOrder(TestFlag.A, TestFlag.C)
			;
		thenThrownBy(() -> result.add(TestFlag.B))
			.as("Set is unmodifiable")
			.isInstanceOf(UnsupportedOperationException.class)
			;
		// @formatter:on
	}

	@Test
	public void roundTrip() {
		// GIVEN
		Set<TestFlag> all = EnumSet.allOf(TestFlag.class);

		// WHEN
		Set<TestFlag> result = Bitmaskable.setForBitmask(Bitmaskable.bitmaskValue(all), TestFlag.class);

		// THEN
		// @formatter:off
		then(result)
			.as("All values restored from bitmask")
			.isEqualTo(all)
			;
		// @formatter:on
	}

}
