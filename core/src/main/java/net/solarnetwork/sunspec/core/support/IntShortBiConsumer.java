/* ==================================================================
 * IntShortBiConsumer.java - 19/01/2020 7:46:47 am
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

package net.solarnetwork.sunspec.core.support;

/**
 * Represents an operation that accepts int and short arguments and returns no
 * result.
 *
 * @author matt
 * @version 1.0
 */
@FunctionalInterface
public interface IntShortBiConsumer {

	/**
	 * Applies this operator to the given operands.
	 *
	 * @param a
	 *        the first input argument
	 * @param b
	 *        the second input argument
	 */
	void accept(int a, short b);

}
