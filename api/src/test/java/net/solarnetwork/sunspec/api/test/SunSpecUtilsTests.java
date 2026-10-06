/* ==================================================================
 * SunSpecUtilsTests.java - 6/10/2026 12:14:31 pm
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
import java.util.EnumSet;
import org.junit.jupiter.api.Test;
import net.solarnetwork.domain.Bitmaskable;
import net.solarnetwork.sunspec.api.SunSpecUtils;

/**
 * Test cases for the {@link SunSpecUtils} class.
 *
 * @author matt
 * @version 1.0
 */
public class SunSpecUtilsTests {

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
	public void bitfieldValues_null() {
		// @formatter:off
		then(SunSpecUtils.bitfieldValues(null, 1, TestFlag.class))
			.as("Not available")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void bitfieldValues_zero() {
		// @formatter:off
		then(SunSpecUtils.bitfieldValues(0, 2, TestFlag.class))
			.as("No bits set")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void bitfieldValues_bitfield16() {
		// @formatter:off
		then(SunSpecUtils.bitfieldValues(0x0003, 1, TestFlag.class))
			.as("Values for bits set")
			.isEqualTo(EnumSet.of(TestFlag.A, TestFlag.B))
			;
		// @formatter:on
	}

	@Test
	public void bitfieldValues_bitfield16MostSignificantBit() {
		// @formatter:off
		then(SunSpecUtils.bitfieldValues(0x8003, 1, TestFlag.class))
			.as("Bitfield16 with MSB set not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void bitfieldValues_bitfield16NotImplemented() {
		// @formatter:off
		then(SunSpecUtils.bitfieldValues(0xFFFF, 1, TestFlag.class))
			.as("Bitfield16 not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void bitfieldValues_bitfield32() {
		// @formatter:off
		then(SunSpecUtils.bitfieldValues(0x40008001L, 2, TestFlag.class))
			.as("Values for bits set, including bit 15")
			.isEqualTo(EnumSet.of(TestFlag.A, TestFlag.C, TestFlag.D))
			;
		// @formatter:on
	}

	@Test
	public void bitfieldValues_bitfield32MostSignificantBit() {
		// @formatter:off
		then(SunSpecUtils.bitfieldValues(0x80000001L, 2, TestFlag.class))
			.as("Bitfield32 with MSB set not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void bitfieldValues_bitfield32NotImplemented() {
		// @formatter:off
		then(SunSpecUtils.bitfieldValues(0xFFFFFFFFL, 2, TestFlag.class))
			.as("Bitfield32 not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void bitfieldValues_undefinedBits() {
		// @formatter:off
		then(SunSpecUtils.bitfieldValues(0x0005, 1, TestFlag.class))
			.as("Undefined bits ignored")
			.isEqualTo(EnumSet.of(TestFlag.A))
			;
		// @formatter:on
	}

}
