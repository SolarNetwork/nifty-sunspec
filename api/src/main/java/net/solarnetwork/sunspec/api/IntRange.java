/* ==================================================================
 * IntRange.java - 15/01/2020 10:28:27 am
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

/**
 * An immutable integer range with inclusive min/max values.
 *
 * <p>
 * Inspired and adapted from <a href="http://pcj.sourceforge.net/">PCJ's</a>
 * {@code bak.pcj.set.IntRange} class.
 * </p>
 *
 * @param min
 *        the minimum value
 * @param max
 *        the maximum value
 * @author matt
 * @version 1.0
 */
public record IntRange(int min, int max) implements Comparable<IntRange> {

	/**
	 * Constructor.
	 *
	 * <p>
	 * Note that if {@code min > max} then {@link #min()} will return
	 * {@code max} and {@link #max()} will return {@code min}. That is, the
	 * minimum and maximum values passed to this constructor will be compared
	 * before storing in this class so that {@link #min()} always returns the
	 * actual minimum value.
	 * </p>
	 *
	 * @param min
	 *        the minimum value
	 * @param max
	 *        the maximum value
	 */
	public IntRange {
		if ( min > max ) {
			final int tmp = min;
			min = max;
			max = tmp;
		}
	}

	/**
	 * Get the number of integer values between {@code min} and {@code max},
	 * inclusive.
	 *
	 * <p>
	 * The result overflows for ranges that contain more than
	 * {@code Integer.MAX_VALUE} values.
	 * </p>
	 *
	 * @return the inclusive length between {@code min} and {@code max}
	 */
	public int length() {
		return (max - min) + 1;
	}

	/**
	 * Test if this range intersects with a given range.
	 *
	 * @param o
	 *        the range to compare to this range
	 * @return {@literal true} if this range intersects (overlaps) with the
	 *         given range
	 */
	public boolean intersects(final IntRange o) {
		return (min >= o.min && min <= o.max) || (o.min >= min && o.min <= max);
	}

	/**
	 * Test if this range is adjacent to (but not intersecting) a given range.
	 *
	 * @param o
	 *        the range to compare to this range
	 * @return {@literal true} if this range is adjacent to the given range
	 */
	public boolean adjacentTo(final IntRange o) {
		return (max + 1L == o.min) || (o.max + 1L == min);
	}

	/**
	 * Test if this range could be merged with another range.
	 *
	 * <p>
	 * Two ranges can be merged if they are either adjacent to or intersect with
	 * each other.
	 * </p>
	 *
	 * @param o
	 *        the range to test
	 * @return {@literal true} if this range is either adjacent to or intersects
	 *         with the given range
	 */
	public boolean canMergeWith(final IntRange o) {
		return intersects(o) || adjacentTo(o);
	}

	/**
	 * Merge this range with a given range, returning the merged range.
	 *
	 * @param o
	 *        the range to merge with this range
	 * @return the new merged range
	 * @throws IllegalArgumentException
	 *         if the this range cannot be merged with the given range
	 */
	public IntRange mergeWith(final IntRange o) {
		if ( !canMergeWith(o) ) {
			throw new IllegalArgumentException("IntRange " + this + " cannot be merged with " + o);
		}
		int a = min < o.min ? min : o.min;
		int b = max > o.max ? max : o.max;
		return a == min && b == max ? this : a == o.min && b == o.max ? o : new IntRange(a, b);
	}

	/**
	 * Compares this object with the specified object for order.
	 *
	 * <p>
	 * This implementation only compares the {@code min} values of each range,
	 * so it is not consistent with {@link #equals(Object)}.
	 * </p>
	 *
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(final IntRange o) {
		return Integer.compare(min, o.min);
	}

	@Override
	public String toString() {
		return "[" + min + ".." + max + "]";
	}

}
