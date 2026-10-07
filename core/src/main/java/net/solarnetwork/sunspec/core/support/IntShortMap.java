/* ==================================================================
 * IntShortMap.java - 17/01/2020 1:16:38 pm
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

import static java.util.Arrays.binarySearch;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * A map of primitive integer keys to primitive short values, optimized for
 * sparse array like storage.
 *
 * <p>
 * This implementation is optimized for small sizes and appending keys in
 * ascending order. Random mutations trigger array copies that can slow
 * performance down considerably. Accessing values run in {@code O(log n)} time.
 * Keys are maintained in ascending order, so iteration occurs also in ascending
 * order.
 * </p>
 *
 * <p>
 * <b>This class is not thread-safe.</b> If multiple threads access an instance
 * concurrently, and at least one of them modifies it, then all access must be
 * synchronized externally, including read-only methods like
 * {@link #getValue(int)}, {@link #forEachOrdered(IntShortBiConsumer)}, and
 * {@link #clone()}. A read concurrent with a modification can return the value
 * of a different key or throw an exception, not just return an outdated value.
 * </p>
 *
 * <p>
 * The {@code forEachOrdered()} methods are <i>fail-fast</i>: if the map is
 * structurally modified while iterating, they throw a
 * {@link ConcurrentModificationException}. Adding a key is a structural
 * modification; changing the value of an existing key is not. Fail-fast
 * behavior is best-effort, so it cannot be relied on to detect unsynchronized
 * concurrent modification.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public class IntShortMap implements Cloneable {

	/** The default initial capacity. */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;

	/**
	 * The default value that causes {@code NoSuchElementException} to be thrown
	 * in {@link #getValue(int)}, when passed to
	 * {@link #IntShortMap(int, short)}.
	 *
	 * <p>
	 * This is also the 16-bit value {@code 0x8000}. To return that value for
	 * nonexistent keys instead, use {@link #IntShortMap(int, short, boolean)}.
	 * </p>
	 */
	public static final short VALUE_NO_SUCH_ELEMENT = Short.MIN_VALUE;

	private final short notFoundValue;
	private final boolean throwIfNotFound;
	private int[] keys;
	private short[] values;
	private int size;

	/** The count of structural modifications, for fail-fast iteration. */
	private int modCount;

	/**
	 * Default constructor.
	 *
	 * <p>
	 * Defaults to returning {@literal 0} for nonexistent keys in
	 * {@link #getValue(int)}.
	 * </p>
	 */
	public IntShortMap() {
		this(DEFAULT_INITIAL_CAPACITY, (short) 0);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * Defaults to returning {@literal 0} for nonexistent keys in
	 * {@link #getValue(int)}.
	 * </p>
	 *
	 * @param initialCapacity
	 *        the initial capacity
	 * @throws IllegalArgumentException
	 *         if {@code initialCapacity} is negative
	 */
	public IntShortMap(int initialCapacity) {
		this(initialCapacity, (short) 0);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * As {@link #VALUE_NO_SUCH_ELEMENT} means to throw an exception, this
	 * constructor cannot create a map that returns that value for nonexistent
	 * keys. Use {@link #IntShortMap(int, short, boolean)} for that.
	 * </p>
	 *
	 * @param initialCapacity
	 *        the initial capacity
	 * @param notFoundValue
	 *        the value to return in {@link #getValue(int)} if a key is not
	 *        found, or {@link #VALUE_NO_SUCH_ELEMENT} to throw a
	 *        {@link NoSuchElementException}
	 * @throws IllegalArgumentException
	 *         if {@code initialCapacity} is negative
	 */
	public IntShortMap(int initialCapacity, short notFoundValue) {
		this(initialCapacity, notFoundValue, notFoundValue == VALUE_NO_SUCH_ELEMENT);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * Unlike {@link #IntShortMap(int, short)}, this treats every
	 * {@code notFoundValue} as a value to return, including
	 * {@link #VALUE_NO_SUCH_ELEMENT}.
	 * </p>
	 *
	 * @param initialCapacity
	 *        the initial capacity
	 * @param notFoundValue
	 *        the value to return in {@link #getValue(int)} if a key is not
	 *        found, when {@code throwIfNotFound} is {@literal false}
	 * @param throwIfNotFound
	 *        {@literal true} to throw a {@link NoSuchElementException} in
	 *        {@link #getValue(int)} if a key is not found, instead of returning
	 *        {@code notFoundValue}
	 * @throws IllegalArgumentException
	 *         if {@code initialCapacity} is negative
	 */
	public IntShortMap(int initialCapacity, short notFoundValue, boolean throwIfNotFound) {
		super();
		if ( initialCapacity < 0 ) {
			throw new IllegalArgumentException("The initial capacity must be 0 or more.");
		}
		this.notFoundValue = notFoundValue;
		this.throwIfNotFound = throwIfNotFound;
		this.keys = new int[initialCapacity];
		this.values = new short[initialCapacity];
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("{");
		final int len = size;
		for ( int i = 0; i < len; i++ ) {
			if ( i > 0 ) {
				buf.append(", ");
			}
			buf.append(keys[i]).append("=").append(values[i]);
		}
		buf.append("}");
		return buf.toString();
	}

	/**
	 * Create a copy of this map.
	 *
	 * <p>
	 * The capacity of the copy is reduced to the size of this map, unless this
	 * map is empty.
	 * </p>
	 *
	 * @return the copy
	 */
	@Override
	public IntShortMap clone() {
		final IntShortMap m;
		try {
			m = (IntShortMap) super.clone();
		} catch ( CloneNotSupportedException e ) {
			// should not get here
			throw new RuntimeException(e);
		}
		final int len = (m.size > 0 ? m.size : m.keys.length);
		m.keys = Arrays.copyOf(m.keys, len);
		m.values = Arrays.copyOf(m.values, len);
		return m;
	}

	/**
	 * Get the number of keys in this map.
	 *
	 * @return the number of keys
	 */
	public int size() {
		return size;
	}

	/**
	 * Test if this map has no keys.
	 *
	 * @return {@literal true} if this map has no keys
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Iterate over all key/value pairs in this map.
	 *
	 * @param action
	 *        the consumer to handle the key/value pairs
	 * @throws ConcurrentModificationException
	 *         if this map is structurally modified while iterating, for example
	 *         by {@code action}
	 */
	public void forEachOrdered(IntShortBiConsumer action) {
		Objects.requireNonNull(action);
		final int mc = modCount;
		for ( int i = 0; modCount == mc && i < size; i++ ) {
			action.accept(keys[i], values[i]);
		}
		if ( modCount != mc ) {
			throw new ConcurrentModificationException();
		}
	}

	/**
	 * Iterate over a range of key/value pairs in this map.
	 *
	 * @param min
	 *        the minimum key value (inclusive)
	 * @param max
	 *        the maximum key value (exclusive)
	 * @param action
	 *        the consumer to handle the key/value pairs
	 * @throws ConcurrentModificationException
	 *         if this map is structurally modified while iterating, for example
	 *         by {@code action}
	 */
	public void forEachOrdered(int min, int max, IntShortBiConsumer action) {
		Objects.requireNonNull(action);
		final int mc = modCount;
		int start = binarySearch(keys, 0, size, min);
		if ( start < 0 ) {
			start = -(start + 1);
		}
		for ( int i = start; modCount == mc && i < size && keys[i] < max; i++ ) {
			action.accept(keys[i], values[i]);
		}
		if ( modCount != mc ) {
			throw new ConcurrentModificationException();
		}
	}

	/**
	 * Test if a key exists in this map.
	 *
	 * @param k
	 *        the key to test
	 * @return {@literal true} if the key exists in this map
	 */
	public boolean containsKey(final int k) {
		final int idx = binarySearch(keys, 0, size, k);
		return idx >= 0;
	}

	/**
	 * Get the value for a given key.
	 *
	 * @param k
	 *        the key of the value to get
	 * @return the associated value, or the configured not-found value if
	 *         {@code k} is not present
	 * @throws NoSuchElementException
	 *         if {@code k} is not present and this map is configured to throw
	 *         an exception for nonexistent keys
	 */
	public short getValue(final int k) {
		final int idx = binarySearch(keys, 0, size, k);
		if ( idx >= 0 ) {
			return values[idx];
		}
		if ( throwIfNotFound ) {
			throw new NoSuchElementException();
		}
		return notFoundValue;
	}

	/**
	 * Put a value.
	 *
	 * @param k
	 *        the key
	 * @param value
	 *        the value, which will be down-cast to a short
	 * @return the previous value associated with {@code k}, or {@code null} if
	 *         none
	 */
	public @Nullable Short putValue(final int k, final int value) {
		return putValue(k, (short) value);
	}

	/**
	 * Put a value.
	 *
	 * @param k
	 *        the key
	 * @param value
	 *        the value
	 * @return the previous value associated with {@code k}, or {@code null} if
	 *         none
	 */
	public @Nullable Short putValue(final int k, final short value) {
		// find position to insert key at; if larger than highest key, we can insert at end
		final int idx = (size == 0 || k > keys[size - 1] ? -size - 1 : binarySearch(keys, 0, size, k));

		Short prev = null;
		if ( idx >= 0 && size > 0 ) {
			// key already present, so replace value
			prev = values[idx];
			values[idx] = value;
		} else {
			// key not present; insert, expanding capacity if necessary
			final int p = -(idx + 1);
			if ( size >= keys.length ) {
				// expand capacity by 50%
				expandCapacity();
			}
			if ( p < size ) {
				// have to insert into middle of array, so shift higher slots right
				System.arraycopy(keys, p, keys, p + 1, (size - p));
				System.arraycopy(values, p, values, p + 1, (size - p));
			}
			keys[p] = k;
			values[p] = value;
			size++;
			modCount++;
		}
		return prev;
	}

	private void expandCapacity() {
		final int oldLen = keys.length;
		final int newLen = oldLen + oldLen / 2 + 1;
		int[] newKeys = new int[newLen];
		System.arraycopy(keys, 0, newKeys, 0, oldLen);
		short[] newValues = new short[newLen];
		System.arraycopy(values, 0, newValues, 0, oldLen);
		this.keys = newKeys;
		this.values = newValues;
	}

}
