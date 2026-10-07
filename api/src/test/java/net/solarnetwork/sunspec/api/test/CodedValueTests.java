/* ==================================================================
 * CodedValueTests.java - 7 Oct 2026 8:59:51 am
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
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.CodedValue;

/**
 * Test cases for the {@link CodedValue} class.
 *
 * @author matt
 * @version 1.0
 */
public class CodedValueTests {

	/** Test codes. */
	private enum TestCode implements CodedValue {

		One(1),

		Two(2),

		AlsoTwo(2),

		;

		private final int code;

		private TestCode(int code) {
			this.code = code;
		}

		@Override
		public int getCode() {
			return code;
		}

	}

	@Test
	public void forCodeValue() {
		// @formatter:off
		then(CodedValue.forCodeValue(1, TestCode.class, null))
			.as("Value for code")
			.isSameAs(TestCode.One)
			;
		// @formatter:on
	}

	@Test
	public void forCodeValue_firstMatch() {
		// @formatter:off
		then(CodedValue.forCodeValue(2, TestCode.class, null))
			.as("First value in ordinal order for shared code")
			.isSameAs(TestCode.Two)
			;
		// @formatter:on
	}

	@Test
	public void forCodeValue_notFound() {
		// @formatter:off
		then(CodedValue.forCodeValue(3, TestCode.class, TestCode.One))
			.as("Default value for unknown code")
			.isSameAs(TestCode.One)
			;
		then(CodedValue.forCodeValue(3, TestCode.class, null))
			.as("Null default value for unknown code")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void forCodeValue_values() {
		// GIVEN
		TestCode[] values = new TestCode[] { TestCode.AlsoTwo, TestCode.Two };

		// THEN
		// @formatter:off
		then(CodedValue.forCodeValue(2, values, null))
			.as("First value in array order for shared code")
			.isSameAs(TestCode.AlsoTwo)
			;
		then(CodedValue.forCodeValue(1, values, TestCode.Two))
			.as("Default value for code not in array")
			.isSameAs(TestCode.Two)
			;
		// @formatter:on
	}

}
