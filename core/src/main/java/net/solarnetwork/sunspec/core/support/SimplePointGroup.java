/* ==================================================================
 * SimplePointGroup.java - 8/10/2026 9:12:40 am
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

package net.solarnetwork.sunspec.core.support;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.PointGroup;
import net.solarnetwork.sunspec.api.PointGroupList;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * A {@link PointGroup} that gets its point values from a function.
 *
 * @author matt
 * @version 1.0
 */
public final class SimplePointGroup implements PointGroup {

	private final Collection<? extends ModbusReference> points;
	private final Function<ModbusReference, @Nullable Object> values;
	private final List<PointGroupList> groups;

	/**
	 * Constructor.
	 *
	 * @param points
	 *        the points
	 * @param values
	 *        a function to get the value of a point
	 */
	public SimplePointGroup(Collection<? extends ModbusReference> points,
			Function<ModbusReference, @Nullable Object> values) {
		this(points, values, List.of());
	}

	/**
	 * Constructor.
	 *
	 * @param points
	 *        the points
	 * @param values
	 *        a function to get the value of a point
	 * @param groups
	 *        the nested groups
	 */
	public SimplePointGroup(Collection<? extends ModbusReference> points,
			Function<ModbusReference, @Nullable Object> values, List<PointGroupList> groups) {
		super();
		this.points = points;
		this.values = values;
		this.groups = groups;
	}

	@Override
	public Collection<? extends ModbusReference> getPointReferences() {
		return points;
	}

	@Override
	public @Nullable Object getPointValue(ModbusReference point) {
		return values.apply(point);
	}

	@Override
	public List<PointGroupList> getPointGroups() {
		return groups;
	}

}
