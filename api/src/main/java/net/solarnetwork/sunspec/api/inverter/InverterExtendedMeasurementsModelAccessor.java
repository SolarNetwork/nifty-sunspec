/* ==================================================================
 * InverterExtendedMeasurementsModelAccessor.java - 6/10/2026 7:41:03 am
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

package net.solarnetwork.sunspec.api.inverter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;

/**
 * API for accessing SunSpec inverter controls extended measurements and status
 * model data.
 *
 * <p>
 * This API corresponds to the SunSpec model number <b>122</b>.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public interface InverterExtendedMeasurementsModelAccessor extends ModelAccessor {

	/**
	 * Get the PV inverter connection status.
	 *
	 * @return the status flags, never {@code null}
	 */
	Set<InverterConnectionStatus> getPvConnectionStatus();

	/**
	 * Get the storage inverter connection status.
	 *
	 * @return the status flags, never {@code null}
	 */
	Set<InverterConnectionStatus> getStorageConnectionStatus();

	/**
	 * Test if the inverter is connected at the electrical connection point
	 * (ECP).
	 *
	 * @return {@literal true} if connected, {@literal false} if disconnected,
	 *         or {@code null} if not available
	 */
	@Nullable
	Boolean isEcpConnected();

	/**
	 * Get the lifetime active energy output.
	 *
	 * @return the energy, in Wh, or {@code null} if not available
	 */
	@Nullable
	Long getActiveEnergyExported();

	/**
	 * Get the lifetime apparent energy output.
	 *
	 * @return the energy, in VAh, or {@code null} if not available
	 */
	@Nullable
	Long getApparentEnergyExported();

	/**
	 * Get the lifetime reactive energy output in quadrant 1.
	 *
	 * @return the energy, in VARh, or {@code null} if not available
	 */
	@Nullable
	Long getReactiveEnergyQ1();

	/**
	 * Get the lifetime reactive energy output in quadrant 2.
	 *
	 * @return the energy, in VARh, or {@code null} if not available
	 */
	@Nullable
	Long getReactiveEnergyQ2();

	/**
	 * Get the lifetime reactive energy output in quadrant 3.
	 *
	 * @return the energy, in VARh, or {@code null} if not available
	 */
	@Nullable
	Long getReactiveEnergyQ3();

	/**
	 * Get the lifetime reactive energy output in quadrant 4.
	 *
	 * @return the energy, in VARh, or {@code null} if not available
	 */
	@Nullable
	Long getReactiveEnergyQ4();

	/**
	 * Get the reactive power available without affecting the active power
	 * output.
	 *
	 * @return the reactive power, in VAR, or {@code null} if not available
	 */
	@Nullable
	BigDecimal getReactivePowerAvailable();

	/**
	 * Get the active power available.
	 *
	 * @return the active power, in W, or {@code null} if not available
	 */
	@Nullable
	BigDecimal getActivePowerAvailable();

	/**
	 * Get the setpoint limits that have been reached.
	 *
	 * <p>
	 * The SunSpec model notes that the device clears these flags when they are
	 * read.
	 * </p>
	 *
	 * @return the limits, never {@code null}
	 */
	Set<InverterSetpointLimit> getSetpointLimitsReached();

	/**
	 * Get the inverter controls that are currently active.
	 *
	 * @return the controls, never {@code null}
	 */
	Set<InverterControlFunction> getActiveControls();

	/**
	 * Get the source of time synchronization.
	 *
	 * @return the source, or {@code null} if not available
	 */
	@Nullable
	String getTimeSource();

	/**
	 * Get the device time.
	 *
	 * @return the time, or {@code null} if not available
	 */
	@Nullable
	Instant getDeviceTime();

	/**
	 * Get the ride-throughs that are currently active.
	 *
	 * @return the ride-throughs, never {@code null}
	 */
	Set<InverterRideThrough> getActiveRideThroughs();

	/**
	 * Get the isolation resistance.
	 *
	 * @return the resistance, in ohms, or {@code null} if not available
	 */
	@Nullable
	Float getIsolationResistance();

}
