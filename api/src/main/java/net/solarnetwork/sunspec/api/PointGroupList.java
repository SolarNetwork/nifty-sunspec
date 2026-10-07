/* ==================================================================
 * PointGroupList.java - 7/10/2026 5:03:42 pm
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

import java.util.List;

/**
 * A named list of nested point groups.
 *
 * @param name
 *        the group name, in upper camel case like point names, such as
 *        {@code Curves}
 * @param repeating
 *        {@literal true} if the group repeats, with any number of instances, or
 *        {@literal false} if it has at most one instance
 * @param groups
 *        the group instances, in order
 * @author matt
 * @version 1.0
 */
public record PointGroupList(String name, boolean repeating, List<? extends PointGroup> groups) {

	/**
	 * Constructor.
	 *
	 * @param name
	 *        the group name, in upper camel case like point names, such as
	 *        {@code Curves}
	 * @param repeating
	 *        {@literal true} if the group repeats, with any number of
	 *        instances, or {@literal false} if it has at most one instance
	 * @param groups
	 *        the group instances, in order
	 * @throws IllegalArgumentException
	 *         if {@code repeating} is {@literal false} and {@code groups} has
	 *         more than one element
	 */
	public PointGroupList {
		if ( !repeating && groups.size() > 1 ) {
			throw new IllegalArgumentException(
					String.format("The non-repeating group %s has %d instances.", name, groups.size()));
		}
	}

	/**
	 * Create a repeating group list.
	 *
	 * @param name
	 *        the group name
	 * @param groups
	 *        the group instances
	 * @return the new list
	 */
	public static PointGroupList repeating(String name, List<? extends PointGroup> groups) {
		return new PointGroupList(name, true, groups);
	}

	/**
	 * Create a non-repeating group list.
	 *
	 * @param name
	 *        the group name
	 * @param group
	 *        the group instance
	 * @return the new list
	 */
	public static PointGroupList single(String name, PointGroup group) {
		return new PointGroupList(name, false, List.of(group));
	}

}
