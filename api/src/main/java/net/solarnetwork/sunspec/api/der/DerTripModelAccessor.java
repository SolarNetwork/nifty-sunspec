/* ==================================================================
 * DerTripModelAccessor.java - 5/10/2026 6:24:51 pm
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

import java.io.IOException;
import java.util.List;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * API for accessing DER trip model data.
 *
 * <p>
 * DER trip models store a number of trip curve sets. The first curve set is the
 * read-only active curve set. The other curve sets are stored settings: to
 * change the active curve set, update one of the stored curve sets and then
 * adopt it with {@link #adoptCurveSet(ModbusConnection, int)}, which copies the
 * stored curve set to the active curve set.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public interface DerTripModelAccessor extends ModelAccessor {

	/**
	 * Get the function enable setting.
	 *
	 * @return {@literal true} if the function is enabled, or {@code null} if
	 *         not available
	 */
	@Nullable
	Boolean isEnabled();

	/**
	 * Set the function enable setting.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param enabled
	 *        {@literal true} to enable the function
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setEnabled(ModbusConnection conn, boolean enabled) throws IOException;

	/**
	 * Get the number of curve sets.
	 *
	 * @return the number of curve sets, including the active curve set, or
	 *         {@code null} if not available
	 */
	@Nullable
	Integer getCurveSetCount();

	/**
	 * Get the number of points in each curve.
	 *
	 * @return the number of points, or {@code null} if not available
	 */
	@Nullable
	Integer getCurvePointCount();

	/**
	 * Get the last adopt curve request.
	 *
	 * @return the index of the last curve set requested to be adopted, or
	 *         {@code null} if not available
	 */
	@Nullable
	Integer getAdoptCurveRequest();

	/**
	 * Adopt a stored curve set as the active curve set.
	 *
	 * <p>
	 * The device copies the curve set to the active curve set, and reports the
	 * result with {@link #getAdoptCurveResult()}.
	 * </p>
	 *
	 * @param conn
	 *        the connection to write to
	 * @param index
	 *        the index of the curve set to adopt, from {@literal 2} to the
	 *        curve set count
	 * @throws IllegalArgumentException
	 *         if {@code index} is outside the allowed range
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void adoptCurveSet(ModbusConnection conn, int index) throws IOException;

	/**
	 * Get the result of the last adopt curve request.
	 *
	 * @return the result, or {@code null} if not available
	 */
	@Nullable
	DerAdoptResult getAdoptCurveResult();

	/**
	 * Get the curve sets.
	 *
	 * <p>
	 * The number of curve sets is the curve set count, limited to the number of
	 * curve sets the model length allows for.
	 * </p>
	 *
	 * @return the curve sets, starting with the active curve set, never
	 *         {@code null}
	 */
	List<DerTripCurveSet> getCurveSets();

}
