/* ==================================================================
 * DerAcMeasurementModelAccessor.java - 5/10/2026 8:27:22 am
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

import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.OperatingState;
import net.solarnetwork.sunspec.api.inverter.InverterModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterOperatingState;

/**
 * API for accessing DER AC measurement model data.
 *
 * <p>
 * This API extends {@link InverterModelAccessor}, which the DER models
 * supersede, with these details:
 * </p>
 *
 * <ul>
 * <li>Energy "delivered" and "exported" values are the energy injected into the
 * grid, and energy "received" values are the energy absorbed from the
 * grid.</li>
 * <li>{@link #getOperatingState()} returns the {@link InverterOperatingState}
 * equivalent of {@link #getInverterState()}.</li>
 * <li>{@link #getEvents()} returns {@link DerAlarm} values.</li>
 * <li>DC values are not available, as they are provided by the DER DC
 * measurement model.</li>
 * </ul>
 *
 * @author matt
 * @version 1.0
 */
public interface DerAcMeasurementModelAccessor extends InverterModelAccessor {

	/**
	 * Get the AC wiring type.
	 *
	 * @return the wiring type
	 */
	@Nullable
	DerAcWiringType getAcWiringType();

	/**
	 * Get the DER operating state.
	 *
	 * @return the operating state
	 */
	@Nullable
	DerOperatingState getDerOperatingState();

	/**
	 * Get the inverter state.
	 *
	 * @return the inverter state
	 * @see #getOperatingState()
	 */
	@Nullable
	DerInverterState getInverterState();

	/**
	 * Get the inverter operating state.
	 *
	 * @return the {@link InverterOperatingState} equivalent of
	 *         {@link #getInverterState()}
	 */
	@Override
	@Nullable
	OperatingState getOperatingState();

	/**
	 * Get the grid connection state.
	 *
	 * @return the grid connection state
	 */
	@Nullable
	DerGridConnectionState getGridConnectionState();

	/**
	 * Get the current operational characteristics.
	 *
	 * @return the characteristics, never {@code null}
	 */
	Set<DerOperationalCharacteristic> getOperationalCharacteristics();

	/**
	 * Get the ambient temperature, in degrees Celsius.
	 *
	 * @return the ambient temperature
	 */
	@Nullable
	Float getAmbientTemperature();

	/**
	 * Get the IGBT/MOSFET temperature, in degrees Celsius.
	 *
	 * @return the switch temperature
	 */
	@Nullable
	Float getSwitchTemperature();

	/**
	 * Get the active power throttling, as a percentage of the maximum active
	 * power.
	 *
	 * @return the throttling percentage, from 0 - 100
	 */
	@Nullable
	Integer getThrottlePercent();

	/**
	 * Get the active power throttling sources.
	 *
	 * @return the sources, never {@code null}
	 */
	Set<DerThrottleSource> getThrottleSources();

	/**
	 * Get the manufacturer alarm information.
	 *
	 * <p>
	 * This information is valid when the {@link DerAlarm#ManufacturerAlarm}
	 * alarm is active.
	 * </p>
	 *
	 * @return the manufacturer alarm information
	 */
	@Nullable
	String getManufacturerAlarmInfo();

}
