/* ==================================================================
 * GenericModelEvent.java - 10/09/2019 9:36:24 am
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
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A basic immutable implementation of {@link ModelEvent}.
 *
 * @author matt
 * @version 1.2
 * @since 1.4
 */
public class GenericModelEvent implements ModelEvent, Comparable<GenericModelEvent> {

	private final int index;
	private final String description;

	/**
	 * Constructor.
	 *
	 * @param index
	 *        the index
	 */
	public GenericModelEvent(int index) {
		this(index, String.valueOf(index));
	}

	/**
	 * Constructor.
	 *
	 * @param index
	 *        the index
	 * @param description
	 *        the description
	 */
	public GenericModelEvent(int index, String description) {
		super();
		this.index = index;
		this.description = description;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int compareTo(GenericModelEvent o) {
		if ( o == null ) {
			return -1;
		}
		int l = getIndex();
		int r = o.getIndex();
		return (l < r ? -1 : l > r ? 1 : 0);
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(index);
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		if ( !(obj instanceof GenericModelEvent other) ) {
			return false;
		}
		return index == other.index;
	}

	/**
	 * Get a set of events from a bitmask.
	 *
	 * <p>
	 * SunSpec bitfields never have their most significant bit set, so a 32-bit
	 * bitmask with that bit set, including the SunSpec "not implemented" value,
	 * results in an empty set.
	 * </p>
	 *
	 * @param bitmask
	 *        the 32-bit bitmask
	 * @return the active events
	 */
	public static Set<ModelEvent> forBitmask(long bitmask) {
		if ( bitmask == 0 || (bitmask & 0x80000000L) != 0 ) {
			return Collections.emptySet();
		}
		Set<ModelEvent> result = new LinkedHashSet<>(32);
		for ( int i = 0; i < 31; i++ ) {
			if ( ((bitmask >> i) & 0x1) == 1 ) {
				result.add(new GenericModelEvent(i));
			}
		}
		return result;
	}

}
