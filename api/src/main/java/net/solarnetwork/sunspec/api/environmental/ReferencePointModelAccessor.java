/* ==================================================================
 * ReferencePointModelAccessor.java - 9/07/2023 4:28:44 pm
 *
 * Copyright 2023 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.api.environmental;

import java.util.List;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;

/**
 * API for accessing reference point model data.
 *
 * @author matt
 * @version 1.0
 * @since 4.2
 */
public interface ReferencePointModelAccessor extends ModelAccessor {

	/**
	 * Get the list of available reference points.
	 *
	 * @return the reference points, never {@code null}
	 */
	List<ReferencePoint> getReferencePoints();

	/**
	 * Get the first available reference point element.
	 *
	 * @return the first available reference point, or {@code null}
	 */
	default @Nullable ReferencePoint getReferencePoint() {
		List<ReferencePoint> points = getReferencePoints();
		return (points != null && !points.isEmpty() ? points.get(0) : null);
	}

}
