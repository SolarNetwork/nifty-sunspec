/* ==================================================================
 * PointGroup.java - 7/10/2026 4:48:05 pm
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * A group of SunSpec points, with optional nested groups.
 *
 * <p>
 * A model is the root group of its points. Repeating blocks, and groups such as
 * curves or curve points, are nested groups.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public interface PointGroup {

	/**
	 * Get the points of this group.
	 *
	 * <p>
	 * The points of nested groups are not included.
	 * </p>
	 *
	 * @return the points, in register order, never {@code null}
	 */
	Collection<? extends ModbusReference> getPointReferences();

	/**
	 * Get the value of a point of this group.
	 *
	 * <p>
	 * The value is the same as the corresponding accessor method returns, such
	 * as a {@code Float} or {@code BigDecimal} with any scale factor applied, a
	 * {@link CodedValue} enumeration, or a set of {@link Bitmaskable} values.
	 * </p>
	 *
	 * @param point
	 *        the point, from {@link #getPointReferences()}
	 * @return the value, or {@code null} if the value is not available or
	 *         {@code point} is not a point of this group
	 */
	@Nullable
	Object getPointValue(ModbusReference point);

	/**
	 * Get the nested groups of this group.
	 *
	 * @return the nested groups, never {@code null}; this implementation
	 *         returns an empty list
	 */
	default List<PointGroupList> getPointGroups() {
		return List.of();
	}

	/**
	 * Get a map of all available point values, using the
	 * {@link SunSpecUtils#pointKey(String)} keys.
	 *
	 * @param mode
	 *        how to add nested groups to the map
	 * @return the map, never {@code null}
	 * @throws IllegalStateException
	 *         if two points map to the same key
	 * @see #toPointMap(PointMapMode, Function)
	 */
	default Map<String, Object> toPointMap(PointMapMode mode) {
		return toPointMap(mode, SunSpecUtils::pointKey);
	}

	/**
	 * Get a map of all available point values.
	 *
	 * <p>
	 * Each point is mapped to the key that {@code keyMapper} returns for its
	 * {@link ModbusReference#getName()}, with the value
	 * {@link #getPointValue(ModbusReference)} returns. Scale factor points, and
	 * points without a value, are left out. Group names are also mapped with
	 * {@code keyMapper}.
	 * </p>
	 *
	 * <p>
	 * In {@link PointMapMode#Flat} mode, the points of nested groups are added
	 * to the map, with a suffix added to their keys for each nested group: an
	 * underscore and the instance number, starting from {@literal 1}, for a
	 * repeating group, or an underscore and the group key for a non-repeating
	 * group. For example, {@code pointVoltage_2_mustTrip_3}.
	 * </p>
	 *
	 * <p>
	 * In {@link PointMapMode#Nested} mode, each repeating group is added as a
	 * list of maps, and each non-repeating group as a map, under the group key.
	 * Groups without any instances or values are left out.
	 * </p>
	 *
	 * @param mode
	 *        how to add nested groups to the map
	 * @param keyMapper
	 *        a function to map point and group names to keys
	 * @return a new mutable map, in point order, never {@code null}
	 * @throws IllegalStateException
	 *         if two points map to the same key
	 */
	default Map<String, Object> toPointMap(PointMapMode mode, Function<String, String> keyMapper) {
		if ( mode == PointMapMode.Nested ) {
			return nestedPointMap(this, keyMapper);
		}
		final Map<String, Object> result = new LinkedHashMap<>(32);
		addFlatPoints(this, keyMapper, "", result, new HashMap<>(32));
		return result;
	}

	/**
	 * Add the points of a group, and its nested groups, to a flat map.
	 *
	 * @param group
	 *        the group
	 * @param keyMapper
	 *        the key mapper
	 * @param suffix
	 *        the key suffix for the points of the group
	 * @param result
	 *        the map to add to
	 * @param sources
	 *        a mapping of added keys to their point names with suffix, to
	 *        report key collisions with
	 */
	private static void addFlatPoints(PointGroup group, Function<String, String> keyMapper,
			String suffix, Map<String, Object> result, Map<String, String> sources) {
		addPoints(group, keyMapper, suffix, result, sources);
		for ( PointGroupList list : group.getPointGroups() ) {
			final List<? extends PointGroup> groups = list.groups();
			for ( int i = 0, len = groups.size(); i < len; i++ ) {
				final String groupSuffix = suffix + '_'
						+ (list.repeating() ? String.valueOf(i + 1) : keyMapper.apply(list.name()));
				addFlatPoints(groups.get(i), keyMapper, groupSuffix, result, sources);
			}
		}
	}

	/**
	 * Create a map of the points of a group, with nested maps for its nested
	 * groups.
	 *
	 * @param group
	 *        the group
	 * @param keyMapper
	 *        the key mapper
	 * @return the map
	 */
	private static Map<String, Object> nestedPointMap(PointGroup group,
			Function<String, String> keyMapper) {
		final Map<String, Object> result = new LinkedHashMap<>(32);
		final Map<String, String> sources = new HashMap<>(32);
		addPoints(group, keyMapper, "", result, sources);
		for ( PointGroupList list : group.getPointGroups() ) {
			final List<? extends PointGroup> groups = list.groups();
			if ( groups.isEmpty() ) {
				continue;
			}
			final Object value;
			if ( list.repeating() ) {
				final List<Map<String, Object>> maps = new ArrayList<>(groups.size());
				for ( PointGroup g : groups ) {
					maps.add(nestedPointMap(g, keyMapper));
				}
				value = maps;
			} else {
				final Map<String, Object> map = nestedPointMap(groups.get(0), keyMapper);
				if ( map.isEmpty() ) {
					continue;
				}
				value = map;
			}
			putValue(result, sources, keyMapper.apply(list.name()), value, list.name());
		}
		return result;
	}

	/**
	 * Add the points of a group to a map.
	 *
	 * @param group
	 *        the group
	 * @param keyMapper
	 *        the key mapper
	 * @param suffix
	 *        the key suffix
	 * @param result
	 *        the map to add to
	 * @param sources
	 *        a mapping of added keys to their sources
	 */
	private static void addPoints(PointGroup group, Function<String, String> keyMapper, String suffix,
			Map<String, Object> result, Map<String, String> sources) {
		for ( ModbusReference ref : group.getPointReferences() ) {
			if ( ref.getClassification() == DataClassification.ScaleFactor ) {
				continue;
			}
			final Object value = group.getPointValue(ref);
			if ( value == null ) {
				continue;
			}
			putValue(result, sources, keyMapper.apply(ref.getName()) + suffix, value,
					ref.getName() + suffix);
		}
	}

	/**
	 * Add a value to a map.
	 *
	 * @param result
	 *        the map to add to
	 * @param sources
	 *        a mapping of added keys to their sources
	 * @param key
	 *        the key
	 * @param value
	 *        the value
	 * @param source
	 *        the source of the value, such as the point name
	 * @throws IllegalStateException
	 *         if {@code key} has already been added
	 */
	private static void putValue(Map<String, Object> result, Map<String, String> sources, String key,
			Object value, String source) {
		final String existing = sources.putIfAbsent(key, source);
		if ( existing != null ) {
			throw new IllegalStateException(
					String.format("Both %s and %s map to the key [%s].", existing, source, key));
		}
		result.put(key, value);
	}

}
