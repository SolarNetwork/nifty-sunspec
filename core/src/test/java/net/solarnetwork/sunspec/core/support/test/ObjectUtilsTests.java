/* ==================================================================
 * ObjectUtilsTests.java - 7/10/2021 10:22:46 AM
 *
 * Copyright 2021 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.core.support.test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.core.support.ObjectUtils;

/**
 * Test cases for the {@link ObjectUtils} class.
 *
 * @author matt
 * @version 1.0
 */
public class ObjectUtilsTests {

	@Test
	public void requireNonNullArgument_notNull() {
		// GIVEN
		String arg = "foo";

		// WHEN
		String result = ObjectUtils.requireNonNullArgument(arg, "fooBar");

		// THEN
		// @formatter:off
		then(result)
			.as("Argument returned when not null")
			.isSameAs(arg)
			;
		// @formatter:on
	}

	@Test
	public void requireNonNullArgument_null() {
		// @formatter:off
		thenThrownBy(() -> ObjectUtils.requireNonNullArgument(null, "fooBar"))
			.as("IllegalArgumentException thrown")
			.isInstanceOf(IllegalArgumentException.class)
			.as("Message includes argument name")
			.hasMessage("The fooBar argument must not be null.")
			;
		// @formatter:on
	}

	@Test
	public void nonnull_notNull() {
		// GIVEN
		String prop = "foo";

		// WHEN
		String result = ObjectUtils.nonnull(prop, "Foo");

		// THEN
		// @formatter:off
		then(result)
			.as("Property returned when not null")
			.isSameAs(prop)
			;
		// @formatter:on
	}

	@Test
	public void nonnull_null() {
		// @formatter:off
		thenThrownBy(() -> ObjectUtils.nonnull(null, "FooService"))
			.as("IllegalStateException thrown")
			.isInstanceOf(IllegalStateException.class)
			.as("Message includes property name")
			.hasMessage("FooService is not available.")
			;
		// @formatter:on
	}

}
