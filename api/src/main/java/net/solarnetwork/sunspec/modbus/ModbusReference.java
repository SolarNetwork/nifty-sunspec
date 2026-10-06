/* ==================================================================
 * ModbusReference.java - 15/05/2018 11:04:04 AM
 *
 * Copyright 2018 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.modbus;

import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.util.IntRangeSet;

/**
 * A reference to a Modbus register (or registers).
 *
 * @author matt
 * @version 2.0
 * @since 2.8
 */
public interface ModbusReference {

	/**
	 * Get the register address.
	 *
	 * @return the address
	 */
	int getAddress();

	/**
	 * Get the data type.
	 *
	 * @return the data type
	 */
	ModbusDataType getDataType();

	/**
	 * Get the read function for accessing the register.
	 *
	 * @return the read function
	 */
	ModbusReadFunction getFunction();

	/**
	 * Get the number of Modbus words to include.
	 *
	 * @return the word length
	 */
	int getWordLength();

	/**
	 * Create a Modbus register address set from enum {@link ModbusReference}
	 * values.
	 *
	 * @param <T>
	 *        the enum type that also implements {@link ModbusReference}
	 * @param clazz
	 *        the enum class to extract the register set from
	 * @param prefixes
	 *        an optional set of enum prefixes to restrict the result to; if not
	 *        provided then all enum values will be included
	 * @return the range set, never {@code null}
	 * @see #createAddressSet(ModbusReference[], Set)
	 * @since 2.0
	 */
	static <T extends Enum<?> & ModbusReference> IntRangeSet createAddressSet(Class<T> clazz,
			@Nullable Set<String> prefixes) {
		return createAddressSet(clazz.getEnumConstants(), prefixes);
	}

	/**
	 * Create a Modbus register address set from an array of
	 * {@link ModbusReference} values.
	 *
	 * @param refs
	 *        the list of references to extract addresses from
	 * @param prefixes
	 *        an optional set of enum prefixes to restrict the result to, which
	 *        are compared to the {@link Object#toString()} value of each
	 *        {@link ModbusReference} in {@code refs}; if not provided then all
	 *        values will be included
	 * @return the range set, never {@code null}
	 * @since 2.0
	 */
	static IntRangeSet createAddressSet(ModbusReference[] refs, @Nullable Set<String> prefixes) {
		IntRangeSet set = new IntRangeSet();
		for ( ModbusReference r : refs ) {
			if ( prefixes != null ) {
				String name = r.toString();
				boolean found = false;
				for ( String prefix : prefixes ) {
					if ( name.startsWith(prefix) ) {
						found = true;
						break;
					}
				}
				if ( !found ) {
					continue;
				}
			}

			int len = r.getWordLength();
			if ( len > 0 ) {
				set.addRange(r.getAddress(), r.getAddress() + len - 1);
			}
		}
		return set;
	}

}
