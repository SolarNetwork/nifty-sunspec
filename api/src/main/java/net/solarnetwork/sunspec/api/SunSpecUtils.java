/* ==================================================================
 * SunSpecUtils.java - 6 Oct 2026 10:09:54 am
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

package net.solarnetwork.sunspec.api;

import java.util.Collections;
import java.util.Set;
import org.jspecify.annotations.Nullable;

/**
 * General SunSpec helper methods.
 *
 * @author matt
 * @version 1.0
 */
public final class SunSpecUtils {

	private SunSpecUtils() {
		// not available
	}

	/**
	 * Get a bitfield data property value as a set of enumeration values.
	 *
	 * <p>
	 * SunSpec bitfields never have their most significant bit set, so a value
	 * with that bit set, including the SunSpec "not implemented" value, is
	 * returned as an empty set. Bits without a corresponding enumeration value
	 * are ignored.
	 * </p>
	 *
	 * @param <T>
	 *        the enumeration type
	 * @param n
	 *        the bitmask number value
	 * @param wordLength
	 *        the number of 16-bit words the bitfield is comprised of
	 * @param type
	 *        the enumeration type
	 * @return the values, never {@code null}
	 */
	public static <T extends Enum<T> & Bitmaskable> Set<T> bitfieldValues(final @Nullable Number n,
			final int wordLength, final Class<T> type) {
		if ( n == null ) {
			return Collections.emptySet();
		}
		final long v = n.longValue();
		if ( (v & (1L << (wordLength * 16 - 1))) != 0 ) {
			return Collections.emptySet();
		}
		return Bitmaskable.setForBitmask((int) v, type);
	}

}
