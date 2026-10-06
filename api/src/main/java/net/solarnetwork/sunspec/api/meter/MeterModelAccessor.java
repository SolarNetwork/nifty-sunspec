/* ==================================================================
 * MeterModelAccessor.java - 22/05/2018 6:26:19 AM
 *
 * Copyright 2018 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.api.meter;

import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.domain.AcPhase;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;

/**
 * API for accessing meter model data.
 *
 * @author matt
 * @version 1.2
 */
public interface MeterModelAccessor extends ModelAccessor {

	/**
	 * Get the AC frequency value, in Hz.
	 *
	 * @return the frequency
	 */
	@Nullable
	Float getFrequency();

	/**
	 * Get the current, in A.
	 *
	 * @return the current
	 */
	@Nullable
	Float getCurrent();

	/**
	 * Get the voltage, in V.
	 *
	 * @return the voltage
	 */
	@Nullable
	Float getVoltage();

	/**
	 * Get the line voltage, in V.
	 *
	 * @return the voltage
	 * @since 1.1
	 */
	@Nullable
	Float getLineVoltage();

	/**
	 * Get the power factor, as a decimal from -1.0 to 1.0.
	 *
	 * @return the power factor
	 */
	@Nullable
	Float getPowerFactor();

	/**
	 * Get the active (real) power, in W.
	 *
	 * @return the active power
	 */
	@Nullable
	Integer getActivePower();

	/**
	 * Get the apparent power, in VA.
	 *
	 * @return the apparent power
	 */
	@Nullable
	Integer getApparentPower();

	/**
	 * Get the reactive power, in VAR.
	 *
	 * @return the reactive power
	 */
	@Nullable
	Integer getReactivePower();

	/**
	 * Get the active energy imported (delivered), in Wh.
	 *
	 * @return the imported active energy
	 */
	@Nullable
	Long getActiveEnergyImported();

	/**
	 * Get the active energy exported (received), in Wh.
	 *
	 * @return the exported active energy
	 */
	@Nullable
	Long getActiveEnergyExported();

	/**
	 * Get the reactive energy imported (delivered), in VARh.
	 *
	 * @return the imported reactive energy
	 */
	@Nullable
	Long getReactiveEnergyImported();

	/**
	 * Get the reactive energy exported (received), in VARh.
	 *
	 * @return the exported reactive energy
	 */
	@Nullable
	Long getReactiveEnergyExported();

	/**
	 * Get the apparent energy imported (delivered), in VAh.
	 *
	 * @return the imported apparent energy
	 */
	@Nullable
	Long getApparentEnergyImported();

	/**
	 * Get the apparent energy exported (received), in VAh.
	 *
	 * @return the exported apparent energy
	 */
	@Nullable
	Long getApparentEnergyExported();

	/**
	 * Get an accessor for phase-specific measurements.
	 *
	 * @param phase
	 *        the phase to get an accessor for
	 * @return the accessor
	 */
	MeterModelAccessor accessorForPhase(AcPhase phase);

	/**
	 * Get a "reversed" model accessor, where import/export directions are
	 * switched.
	 *
	 * @return the reversed accessor
	 * @since 1.1
	 */
	default MeterModelAccessor reversed() {
		return new ReversedMeterModelAccessor(this);
	}

	/**
	 * Get the active events.
	 *
	 * @return the events (never {@code null})
	 */
	Set<? extends ModelEvent> getEvents();

}
