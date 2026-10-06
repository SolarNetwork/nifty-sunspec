/* ==================================================================
 * DerTripCurveSet.java - 5/10/2026 6:24:51 pm
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

package net.solarnetwork.sunspec.api.der;

import org.jspecify.annotations.Nullable;

/**
 * API for a set of trip curves in a DER trip model.
 *
 * <p>
 * Each curve set has three curves, which define the region boundaries the DER
 * behaves differently in: when the must trip region is entered the DER must
 * trip, when the momentary cessation region is entered the DER must cease to
 * energize but not trip, and when the may trip region is entered the DER may
 * either continue operating or trip. Each curve reports the index and read-only
 * setting of its curve set, and can have no active points, as the may trip and
 * momentary cessation curves are optional.
 * </p>
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public interface DerTripCurveSet {

	/**
	 * Get the curve set index.
	 *
	 * @return the index, starting from {@literal 1}, which is the active curve
	 *         set
	 */
	int getIndex();

	/**
	 * Get the read-only setting.
	 *
	 * @return {@literal true} if the curve set is read-only, or {@code null} if
	 *         not available
	 */
	@Nullable
	Boolean isReadOnly();

	/**
	 * Get the must trip curve.
	 *
	 * @return the curve, never {@code null}
	 */
	DerCurve getMustTripCurve();

	/**
	 * Get the may trip curve.
	 *
	 * @return the curve, never {@code null}
	 */
	DerCurve getMayTripCurve();

	/**
	 * Get the momentary cessation curve.
	 *
	 * @return the curve, never {@code null}
	 */
	DerCurve getMomentaryCessationCurve();

}
