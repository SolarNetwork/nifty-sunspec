/* ==================================================================
 * InverterPricingSignalModelAccessor.java - 6/10/2026 9:21:47 am
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

import java.io.IOException;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * API for accessing SunSpec pricing signal model data.
 *
 * <p>
 * This API corresponds to the SunSpec model number <b>125</b>, which supports
 * price-based charging and discharging. Setter methods write to the device
 * immediately, and throw {@link IllegalArgumentException} if the value is not
 * valid for the point, or {@link IllegalStateException} if the model scale
 * factor has not been read from the device or is not implemented.
 * </p>
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public interface InverterPricingSignalModelAccessor extends ModelAccessor {

	/**
	 * Get the price-based charge and discharge mode enable setting.
	 *
	 * @return {@literal true} if the price-based mode is enabled, or
	 *         {@code null} if not available
	 */
	@Nullable
	Boolean isPricingEnabled();

	/**
	 * Set the price-based charge and discharge mode enable setting.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param enabled
	 *        {@literal true} to enable the price-based mode
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setPricingEnabled(ModbusConnection conn, boolean enabled) throws IOException;

	/**
	 * Get the pricing signal type, which defines the meaning of the pricing
	 * signal.
	 *
	 * @return the type, or {@code null} if not available
	 */
	@Nullable
	InverterPricingSignalType getPricingSignalType();

	/**
	 * Set the pricing signal type, which defines the meaning of the pricing
	 * signal.
	 *
	 * <p>
	 * When a price schedule is used, the type must match the schedule's range
	 * description.
	 * </p>
	 *
	 * @param conn
	 *        the connection to write to
	 * @param type
	 *        the type
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setPricingSignalType(ModbusConnection conn, InverterPricingSignalType type) throws IOException;

	/**
	 * Get the utility or energy service provider specific pricing signal.
	 *
	 * @return the signal, whose meaning depends on the pricing signal type, or
	 *         {@code null} if not available
	 */
	@Nullable
	Float getPricingSignal();

	/**
	 * Set the utility or energy service provider specific pricing signal.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param signal
	 *        the signal, whose meaning depends on the pricing signal type
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setPricingSignal(ModbusConnection conn, float signal) throws IOException;

	/**
	 * Get the time window for charge and discharge pricing changes.
	 *
	 * @return the time window, in seconds, or {@code null} if not available
	 */
	@Nullable
	Integer getPricingTimeWindow();

	/**
	 * Set the time window for charge and discharge pricing changes.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param seconds
	 *        the time window, in seconds
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setPricingTimeWindow(ModbusConnection conn, int seconds) throws IOException;

	/**
	 * Get the charge and discharge pricing reversion timeout.
	 *
	 * @return the timeout, in seconds, or {@code null} if not available
	 */
	@Nullable
	Integer getPricingReversionTime();

	/**
	 * Set the charge and discharge pricing reversion timeout.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param seconds
	 *        the timeout, in seconds
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setPricingReversionTime(ModbusConnection conn, int seconds) throws IOException;

	/**
	 * Get the ramp time, for moving from the current charge or discharge level
	 * to a new level.
	 *
	 * @return the ramp time, in seconds, or {@code null} if not available
	 */
	@Nullable
	Integer getPricingRampTime();

	/**
	 * Set the ramp time, for moving from the current charge or discharge level
	 * to a new level.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param seconds
	 *        the ramp time, in seconds
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setPricingRampTime(ModbusConnection conn, int seconds) throws IOException;

}
