/* ==================================================================
 * PointMapMode.java - 7/10/2026 5:02:11 pm
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

/**
 * The ways nested point groups are added to a point map.
 *
 * @author matt
 * @version 1.0
 * @see PointGroup#toPointMap(PointMapMode)
 */
public enum PointMapMode {

	/**
	 * Add the points of nested groups directly to the map, with the group
	 * indexes and names added as key suffixes, such as {@code ghi_1}.
	 */
	Flat,

	/**
	 * Add each nested group as its own map, with a list of maps for each
	 * repeating group, without any key suffixes.
	 */
	Nested,

}
