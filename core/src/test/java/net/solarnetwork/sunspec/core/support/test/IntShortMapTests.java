/* ==================================================================
 * IntShortMapTests.java - 18/01/2020 8:41:45 am
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

package net.solarnetwork.sunspec.core.support.test;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.entry;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.core.support.IntShortMap;

/**
 * Test cases for the {@link IntShortMap} class.
 *
 * @author matt
 * @version 1.0
 */
public class IntShortMapTests {

	/**
	 * Create a map from key/value pairs, put in the given order.
	 *
	 * @param keysAndValues
	 *        the key/value pairs
	 * @return the map
	 */
	private static IntShortMap map(int... keysAndValues) {
		final IntShortMap m = new IntShortMap();
		for ( int i = 0; i < keysAndValues.length; i += 2 ) {
			m.putValue(keysAndValues[i], keysAndValues[i + 1]);
		}
		return m;
	}

	/**
	 * Get all map entries, in iteration order.
	 *
	 * @param m
	 *        the map
	 * @return the entries
	 */
	private static Map<Integer, Short> entries(IntShortMap m) {
		final Map<Integer, Short> result = new LinkedHashMap<>(m.size());
		m.forEachOrdered((k, v) -> result.put(k, v));
		return result;
	}

	/**
	 * Get a range of map entries, in iteration order.
	 *
	 * @param m
	 *        the map
	 * @param min
	 *        the minimum key (inclusive)
	 * @param max
	 *        the maximum key (exclusive)
	 * @return the entries
	 */
	private static Map<Integer, Short> entries(IntShortMap m, int min, int max) {
		final Map<Integer, Short> result = new LinkedHashMap<>(m.size());
		m.forEachOrdered(min, max, (k, v) -> result.put(k, v));
		return result;
	}

	@Test
	public void construct() {
		// WHEN
		IntShortMap m = new IntShortMap();

		// THEN
		// @formatter:off
		then(m)
			.as("Map is empty")
			.returns(true, from(IntShortMap::isEmpty))
			.as("Size is zero")
			.returns(0, from(IntShortMap::size))
			.as("Zero returned for nonexistent key")
			.returns((short) 0, from(map -> map.getValue(1)))
			;
		// @formatter:on
	}

	@Test
	public void construct_zeroCapacity() {
		// GIVEN
		IntShortMap m = new IntShortMap(0);

		// WHEN
		Short prev = m.putValue(1, 2);

		// THEN
		// @formatter:off
		then(prev)
			.as("No previous value")
			.isNull()
			;
		then(entries(m))
			.as("Value added after capacity expanded")
			.containsExactly(entry(1, (short) 2))
			;
		// @formatter:on
	}

	@Test
	public void construct_negativeCapacity() {
		// @formatter:off
		thenThrownBy(() -> new IntShortMap(-1))
			.as("Negative capacity rejected")
			.isInstanceOf(IllegalArgumentException.class)
			;
		// @formatter:on
	}

	@Test
	public void put_initial() {
		// GIVEN
		IntShortMap m = new IntShortMap();

		// WHEN
		Short prev = m.putValue(1, 2);

		// THEN
		// @formatter:off
		then(prev)
			.as("No previous value")
			.isNull()
			;
		then(m)
			.as("Map not empty")
			.returns(false, from(IntShortMap::isEmpty))
			.as("Size updated")
			.returns(1, from(IntShortMap::size))
			.as("Value stored")
			.returns((short) 2, from(map -> map.getValue(1)))
			;
		// @formatter:on
	}

	@Test
	public void put_tail() {
		// GIVEN
		IntShortMap m = map(1, 1);

		// WHEN
		Short prev = m.putValue(2, 2);

		// THEN
		// @formatter:off
		then(prev)
			.as("No previous value")
			.isNull()
			;
		then(entries(m))
			.as("Key appended")
			.containsExactly(entry(1, (short) 1), entry(2, (short) 2))
			;
		// @formatter:on
	}

	@Test
	public void put_tail_pastCapacity() {
		// GIVEN
		IntShortMap m = new IntShortMap(2);
		m.putValue(1, 1);
		m.putValue(2, 2);

		// WHEN
		Short prev = m.putValue(3, 3);

		// THEN
		// @formatter:off
		then(prev)
			.as("No previous value")
			.isNull()
			;
		then(entries(m))
			.as("Key appended after capacity expanded")
			.containsExactly(entry(1, (short) 1), entry(2, (short) 2), entry(3, (short) 3))
			;
		// @formatter:on
	}

	@Test
	public void put_replace() {
		// GIVEN
		IntShortMap m = new IntShortMap(2);
		m.putValue(1, 1);

		// WHEN
		Short prev = m.putValue(1, 2);

		// THEN
		// @formatter:off
		then(prev)
			.as("Previous value returned")
			.isEqualTo((short) 1)
			;
		then(entries(m))
			.as("Value replaced")
			.containsExactly(entry(1, (short) 2))
			;
		// @formatter:on
	}

	@Test
	public void put_head() {
		// GIVEN
		IntShortMap m = map(2, 2);

		// WHEN
		Short prev = m.putValue(1, 1);

		// THEN
		// @formatter:off
		then(prev)
			.as("No previous value")
			.isNull()
			;
		then(entries(m))
			.as("Key inserted at head")
			.containsExactly(entry(1, (short) 1), entry(2, (short) 2))
			;
		// @formatter:on
	}

	@Test
	public void put_head_pastCapacity() {
		// GIVEN
		IntShortMap m = new IntShortMap(2);
		m.putValue(3, 3);
		m.putValue(2, 2);

		// WHEN
		Short prev = m.putValue(1, 1);

		// THEN
		// @formatter:off
		then(prev)
			.as("No previous value")
			.isNull()
			;
		then(entries(m))
			.as("Key inserted at head after capacity expanded")
			.containsExactly(entry(1, (short) 1), entry(2, (short) 2), entry(3, (short) 3))
			;
		// @formatter:on
	}

	@Test
	public void put_mid() {
		// GIVEN
		IntShortMap m = new IntShortMap(4);
		m.putValue(1, 1);
		m.putValue(3, 3);

		// WHEN
		Short prev = m.putValue(2, 2);

		// THEN
		// @formatter:off
		then(prev)
			.as("No previous value")
			.isNull()
			;
		then(entries(m))
			.as("Key inserted in middle")
			.containsExactly(entry(1, (short) 1), entry(2, (short) 2), entry(3, (short) 3))
			;
		// @formatter:on
	}

	@Test
	public void put_mid2() {
		// GIVEN
		IntShortMap m = new IntShortMap(4);
		m.putValue(1, 1);
		m.putValue(3, 3);
		m.putValue(4, 4);

		// WHEN
		Short prev = m.putValue(2, 2);

		// THEN
		// @formatter:off
		then(prev)
			.as("No previous value")
			.isNull()
			;
		then(entries(m))
			.as("Key inserted in middle")
			.containsExactly(entry(1, (short) 1), entry(2, (short) 2), entry(3, (short) 3),
					entry(4, (short) 4))
			;
		// @formatter:on
	}

	@Test
	public void put_intValue() {
		// GIVEN
		IntShortMap m = new IntShortMap();

		// WHEN
		m.putValue(1, 0xFFFF);

		// THEN
		// @formatter:off
		then(m.getValue(1))
			.as("Integer value cast to short")
			.isEqualTo((short) -1)
			;
		// @formatter:on
	}

	@Test
	public void containsKey() {
		// GIVEN
		IntShortMap m = map(1, 2, 2, 3, 3, 4);

		// THEN
		// @formatter:off
		then(m)
			.as("Contains first key")
			.returns(true, from(map -> map.containsKey(1)))
			.as("Contains middle key")
			.returns(true, from(map -> map.containsKey(2)))
			.as("Contains last key")
			.returns(true, from(map -> map.containsKey(3)))
			.as("Does not contain key before first")
			.returns(false, from(map -> map.containsKey(0)))
			.as("Does not contain key after last")
			.returns(false, from(map -> map.containsKey(9)))
			;
		// @formatter:on
	}

	@SuppressWarnings("unused")
	@Test
	public void forEachOrdered_empty() {
		// GIVEN
		IntShortMap m = new IntShortMap();
		List<Integer> keys = new ArrayList<>();

		// WHEN
		m.forEachOrdered((k, v) -> keys.add(k));

		// THEN
		// @formatter:off
		then(keys)
			.as("No keys visited")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void forEachOrdered() {
		// GIVEN
		IntShortMap m = map(1, 2, 7, 8, 3, 4);
		List<Integer> keys = new ArrayList<>(3);
		List<Short> values = new ArrayList<>(3);

		// WHEN
		m.forEachOrdered((k, v) -> {
			keys.add(k);
			values.add(v);
		});

		// THEN
		// @formatter:off
		then(keys)
			.as("Keys visited in ascending order")
			.containsExactly(1, 3, 7)
			;
		then(values)
			.as("Values visited in key order")
			.containsExactly((short) 2, (short) 4, (short) 8)
			;
		// @formatter:on
	}

	@Test
	public void forEachOrdered_range_mid_onExistingKeys() {
		// GIVEN
		IntShortMap m = map(1, 2, 7, 8, 3, 4, 9, 10);

		// WHEN
		Map<Integer, Short> result = entries(m, 3, 9);

		// THEN
		// @formatter:off
		then(result)
			.as("Entries from min (inclusive) to max (exclusive)")
			.containsExactly(entry(3, (short) 4), entry(7, (short) 8))
			;
		// @formatter:on
	}

	@Test
	public void forEachOrdered_range_mid_onNonexistingKeys() {
		// GIVEN
		IntShortMap m = map(1, 2, 7, 8, 3, 4, 10, 11);

		// WHEN
		Map<Integer, Short> result = entries(m, 2, 9);

		// THEN
		// @formatter:off
		then(result)
			.as("Entries between min and max")
			.containsExactly(entry(3, (short) 4), entry(7, (short) 8))
			;
		// @formatter:on
	}

	@Test
	public void forEachOrdered_range_head_onExistingKeys() {
		// GIVEN
		IntShortMap m = map(1, 2, 7, 8, 3, 4, 9, 10);

		// WHEN
		Map<Integer, Short> result = entries(m, 1, 7);

		// THEN
		// @formatter:off
		then(result)
			.as("Entries from first key to max (exclusive)")
			.containsExactly(entry(1, (short) 2), entry(3, (short) 4))
			;
		// @formatter:on
	}

	@Test
	public void forEachOrdered_range_head_onNonexistingKeys() {
		// GIVEN
		IntShortMap m = map(1, 2, 7, 8, 3, 4, 10, 11);

		// WHEN
		Map<Integer, Short> result = entries(m, 0, 7);

		// THEN
		// @formatter:off
		then(result)
			.as("Entries from before first key to max (exclusive)")
			.containsExactly(entry(1, (short) 2), entry(3, (short) 4))
			;
		// @formatter:on
	}

	@Test
	public void forEachOrdered_range_tail_onExistingKeys() {
		// GIVEN
		IntShortMap m = map(1, 2, 7, 8, 3, 4, 9, 10);

		// WHEN
		Map<Integer, Short> result = entries(m, 7, 10);

		// THEN
		// @formatter:off
		then(result)
			.as("Entries from min (inclusive) to last key")
			.containsExactly(entry(7, (short) 8), entry(9, (short) 10))
			;
		// @formatter:on
	}

	@Test
	public void forEachOrdered_range_tail_onNonexistingKeys() {
		// GIVEN
		IntShortMap m = map(1, 2, 7, 8, 3, 4, 10, 11);

		// WHEN
		Map<Integer, Short> result = entries(m, 5, 11);

		// THEN
		// @formatter:off
		then(result)
			.as("Entries from min to last key")
			.containsExactly(entry(7, (short) 8), entry(10, (short) 11))
			;
		// @formatter:on
	}

	@Test
	public void forEachOrdered_concurrentModification_put() {
		// GIVEN
		IntShortMap m = map(10, 1, 20, 2, 30, 3);
		List<Integer> keys = new ArrayList<>(3);

		// WHEN
		Throwable t = catchThrowable(() -> m.forEachOrdered((k, v) -> {
			keys.add(k);
			m.putValue(k + 1, v);
		}));

		// THEN
		// @formatter:off
		then(t)
			.as("Adding a key while iterating is a concurrent modification")
			.isInstanceOf(ConcurrentModificationException.class)
			;
		then(keys)
			.as("Iteration stopped at the modification")
			.containsExactly(10)
			;
		// @formatter:on
	}

	@Test
	public void forEachOrdered_range_concurrentModification_put() {
		// GIVEN
		IntShortMap m = map(10, 1, 20, 2, 30, 3);
		List<Integer> keys = new ArrayList<>(3);

		// WHEN
		Throwable t = catchThrowable(() -> m.forEachOrdered(10, 30, (k, v) -> {
			keys.add(k);
			m.putValue(k + 1, v);
		}));

		// THEN
		// @formatter:off
		then(t)
			.as("Adding a key while iterating is a concurrent modification")
			.isInstanceOf(ConcurrentModificationException.class)
			;
		then(keys)
			.as("Iteration stopped at the modification")
			.containsExactly(10)
			;
		// @formatter:on
	}

	@Test
	public void forEachOrdered_replaceValue() {
		// GIVEN
		IntShortMap m = map(10, 1, 20, 2);
		List<Integer> keys = new ArrayList<>(2);

		// WHEN
		m.forEachOrdered((k, v) -> {
			keys.add(k);
			m.putValue(k, v + 1);
		});

		// THEN
		// @formatter:off
		then(keys)
			.as("All keys visited, as replacing a value is not a concurrent modification")
			.containsExactly(10, 20)
			;
		then(entries(m))
			.as("Values replaced")
			.containsExactly(entry(10, (short) 2), entry(20, (short) 3))
			;
		// @formatter:on
	}

	@Test
	public void getValue_found() {
		// GIVEN
		IntShortMap m = map(1, 2);

		// THEN
		// @formatter:off
		then(m.getValue(1))
			.as("Found value")
			.isEqualTo((short) 2)
			;
		// @formatter:on
	}

	@Test
	public void getValue_notFound() {
		// GIVEN
		IntShortMap m = new IntShortMap(8, IntShortMap.VALUE_NO_SUCH_ELEMENT);
		m.putValue(1, 2);

		// THEN
		// @formatter:off
		thenThrownBy(() -> m.getValue(2))
			.as("Exception thrown for nonexistent key")
			.isInstanceOf(NoSuchElementException.class)
			;
		// @formatter:on
	}

	@Test
	public void getValue_notFoundWithDefault() {
		// GIVEN
		IntShortMap m = new IntShortMap(8, (short) -1);
		m.putValue(1, 2);

		// THEN
		// @formatter:off
		then(m.getValue(2))
			.as("Default value returned for nonexistent key")
			.isEqualTo((short) -1)
			;
		// @formatter:on
	}

	@Test
	public void getValue_notFoundWithNoSuchElementValueDefault() {
		// GIVEN
		IntShortMap m = new IntShortMap(8, IntShortMap.VALUE_NO_SUCH_ELEMENT, false);
		m.putValue(1, 2);

		// THEN
		// @formatter:off
		then(m.getValue(2))
			.as("0x8000 returned for nonexistent key")
			.isEqualTo((short) 0x8000)
			;
		// @formatter:on
	}

	@Test
	public void getValue_notFound_throwIfNotFound() {
		// GIVEN
		IntShortMap m = new IntShortMap(8, (short) 0, true);
		m.putValue(1, 2);

		// THEN
		// @formatter:off
		thenThrownBy(() -> m.getValue(2))
			.as("Exception thrown for nonexistent key")
			.isInstanceOf(NoSuchElementException.class)
			;
		// @formatter:on
	}

	@Test
	public void clone_empty() {
		// GIVEN
		IntShortMap m1 = new IntShortMap(8);

		// WHEN
		IntShortMap m2 = m1.clone();

		// THEN
		// @formatter:off
		then(m2)
			.as("New instance created")
			.isNotSameAs(m1)
			.as("Clone is empty")
			.returns(true, from(IntShortMap::isEmpty))
			;
		// @formatter:on
	}

	@Test
	public void clone_contents() {
		// GIVEN
		IntShortMap m1 = map(1, 1, 2, 2, 9, 9);

		// WHEN
		IntShortMap m2 = m1.clone();

		// THEN
		// @formatter:off
		then(m2)
			.as("New instance created")
			.isNotSameAs(m1)
			;
		then(entries(m2))
			.as("Contents same")
			.containsExactly(entry(1, (short) 1), entry(2, (short) 2), entry(9, (short) 9))
			;
		// @formatter:on
	}

	@Test
	public void clone_mutation() {
		// GIVEN
		IntShortMap m1 = map(1, 1, 2, 2, 3, 3);
		IntShortMap m2 = m1.clone();

		// WHEN
		m2.putValue(1, 99);
		m2.putValue(4, 100);

		// THEN
		// @formatter:off
		then(entries(m1))
			.as("Original unchanged")
			.containsExactly(entry(1, (short) 1), entry(2, (short) 2), entry(3, (short) 3))
			;
		then(entries(m2))
			.as("Clone changed")
			.containsExactly(entry(1, (short) 99), entry(2, (short) 2), entry(3, (short) 3),
					entry(4, (short) 100))
			;
		// @formatter:on
	}

	@Test
	public void clone_subclass() {
		// GIVEN
		IntShortMap m1 = new IntShortMap(8) {
			// anonymous subclass, to verify the clone has the same class
		};
		m1.putValue(1, 2);

		// WHEN
		IntShortMap m2 = m1.clone();

		// THEN
		// @formatter:off
		then(m2)
			.as("Clone has same class")
			.isInstanceOf(m1.getClass())
			;
		then(entries(m2))
			.as("Contents same")
			.containsExactly(entry(1, (short) 2))
			;
		// @formatter:on
	}

	@Test
	public void clone_notFoundValue() {
		// GIVEN
		IntShortMap m1 = new IntShortMap(8, IntShortMap.VALUE_NO_SUCH_ELEMENT);

		// WHEN
		IntShortMap m2 = m1.clone();

		// THEN
		// @formatter:off
		thenThrownBy(() -> m2.getValue(1))
			.as("Clone throws exception for nonexistent key")
			.isInstanceOf(NoSuchElementException.class)
			;
		// @formatter:on
	}

	@Test
	public void clone_notFoundValue_noSuchElementValueDefault() {
		// GIVEN
		IntShortMap m1 = new IntShortMap(8, IntShortMap.VALUE_NO_SUCH_ELEMENT, false);

		// WHEN
		IntShortMap m2 = m1.clone();

		// THEN
		// @formatter:off
		then(m2.getValue(1))
			.as("Clone returns 0x8000 for nonexistent key")
			.isEqualTo((short) 0x8000)
			;
		// @formatter:on
	}

	@Test
	public void stringValue() {
		// @formatter:off
		then(new IntShortMap())
			.as("Empty map string")
			.hasToString("{}")
			;
		then(map(3, 4, 1, 2))
			.as("Entries in key order")
			.hasToString("{1=2, 3=4}")
			;
		// @formatter:on
	}

}
