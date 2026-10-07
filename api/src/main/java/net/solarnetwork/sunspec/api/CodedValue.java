/* ==================================================================
 * CodedValue.java - 25/02/2020 7:25:22 pm
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

package net.solarnetwork.sunspec.api;

import org.jspecify.annotations.Nullable;

/**
 * API for something that has a "code" value.
 *
 * <p>
 * This can be used in enumerations to provide a consistent way to exchange
 * enumerated values with integers that do not depend on the ordinal position
 * (or string value) of the enum.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public interface CodedValue {

	/**
	 * Get the coded value.
	 *
	 * @return the code
	 */
	int getCode();

	/**
	 * Convert a code value into an enum value.
	 *
	 * @param <T>
	 *        the value type
	 * @param code
	 *        the code to get an enum value for
	 * @param clazz
	 *        the class of an enumeration of {@link CodedValue} objects
	 * @param defaultValue
	 *        a default value to return if no matching code is found
	 * @return the first enumeration value, in ordinal order, that has the given
	 *         code value, or {@code defaultValue} if not found
	 */
	static <T extends Enum<T> & CodedValue> @Nullable T forCodeValue(int code, Class<T> clazz,
			@Nullable T defaultValue) {
		return forCodeValue(code, clazz.getEnumConstants(), defaultValue);
	}

	/**
	 * Convert a code value into an enum value.
	 *
	 * @param <T>
	 *        the value type
	 * @param code
	 *        the code to get an enum value for
	 * @param values
	 *        the values to search through
	 * @param defaultValue
	 *        a default value to return if no matching code is found
	 * @return the first value, in array order, that has the given code value,
	 *         or {@code defaultValue} if not found
	 */
	static <T extends CodedValue> @Nullable T forCodeValue(int code, T[] values,
			@Nullable T defaultValue) {
		for ( T v : values ) {
			if ( code == v.getCode() ) {
				return v;
			}
		}
		return defaultValue;
	}

}
