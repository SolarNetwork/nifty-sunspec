/* ==================================================================
 * Bitmaskable.java - 18/02/2019 10:21:15 am
 *
 * Copyright 2019 SolarNetwork.net Dev Team
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

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import org.jspecify.annotations.Nullable;

/**
 * A standardized API for domain objects that can be represented in bitmask
 * form.
 *
 * <p>
 * A bitmask is a collection of on/off flags encoded as bits within an integer
 * value.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public interface Bitmaskable {

	/**
	 * Get the bit offset.
	 *
	 * @return offset, starting from {@literal 0} for the right-most bit
	 */
	int bitmaskBitOffset();

	/**
	 * Get a bitmask value for a set of {@code Bitmaskable} objects.
	 *
	 * @param maskables
	 *        the set of {@code Bitmaskable} objects ({@code null} allowed)
	 * @return a bitmask value of all {@link Bitmaskable#bitmaskBitOffset()}
	 *         values of the given {@code maskables}
	 * @see #setForBitmask(int, Class)
	 */
	static int bitmaskValue(@Nullable Set<? extends Bitmaskable> maskables) {
		int mask = 0;
		if ( maskables != null ) {
			for ( Bitmaskable c : maskables ) {
				mask |= (1 << c.bitmaskBitOffset());
			}
		}
		return mask;
	}

	/**
	 * Convert a bitmask value into a set of {@code Bitmaskable} objects.
	 *
	 * @param <T>
	 *        the value type
	 * @param mask
	 *        a bitmask value of a set of {@code Bitmaskable} objects
	 * @param clazz
	 *        the class of an enumeration of {@link Bitmaskable} objects
	 * @return an immutable set of {@link Bitmaskable} objects, never
	 *         {@code null}
	 * @see #bitmaskValue(Set)
	 */
	static <T extends Enum<T> & Bitmaskable> Set<T> setForBitmask(int mask, Class<T> clazz) {
		Set<T> result = setForBitmask(mask, clazz.getEnumConstants());
		return (result.isEmpty() ? result : EnumSet.copyOf(result));
	}

	/**
	 * Convert a bitmask value into a set of {@code Bitmaskable} objects.
	 *
	 * @param <T>
	 *        the value type
	 * @param mask
	 *        a bitmask value of a set of {@code Bitmaskable} objects
	 * @param values
	 *        the complete set of possible {@link Bitmaskable} objects
	 * @return an immutable set of {@link Bitmaskable} objects, never
	 *         {@code null}
	 * @see #bitmaskValue(Set)
	 */
	static <T extends Bitmaskable> Set<T> setForBitmask(int mask, T[] values) {
		if ( mask < 1 ) {
			return Collections.emptySet();
		}
		Set<T> set = new HashSet<>(16);
		for ( T c : values ) {
			int b = c.bitmaskBitOffset();
			if ( ((mask >> b) & 1) == 1 ) {
				set.add(c);
			}
		}
		return (set.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(set));
	}

}
