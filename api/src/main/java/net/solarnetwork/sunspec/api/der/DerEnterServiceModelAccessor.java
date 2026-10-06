/* ==================================================================
 * DerEnterServiceModelAccessor.java - 5/10/2026 9:32:29 am
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
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * API for accessing DER enter service model data.
 *
 * <p>
 * Setter methods write to the device immediately, and throw
 * {@link IllegalArgumentException} if the value is not valid for the point, or
 * {@link IllegalStateException} if the model scale factors have not been read
 * from the device.
 * </p>
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public interface DerEnterServiceModelAccessor extends ModelAccessor {

	/**
	 * Get the permit enter service setting.
	 *
	 * @return {@literal true} if entering service is permitted
	 */
	@Nullable
	Boolean isEnterServicePermitted();

	/**
	 * Set the permit enter service setting.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param permitted
	 *        {@literal true} to permit entering service
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setEnterServicePermitted(ModbusConnection conn, boolean permitted) throws IOException;

	/**
	 * Get the enter service voltage high threshold, as a percentage of nominal
	 * voltage.
	 *
	 * @return the threshold
	 */
	@Nullable
	Float getVoltageHigh();

	/**
	 * Set the enter service voltage high threshold.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param percent
	 *        the threshold, as a percentage of nominal voltage
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setVoltageHigh(ModbusConnection conn, float percent) throws IOException;

	/**
	 * Get the enter service voltage low threshold, as a percentage of nominal
	 * voltage.
	 *
	 * @return the threshold
	 */
	@Nullable
	Float getVoltageLow();

	/**
	 * Set the enter service voltage low threshold.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param percent
	 *        the threshold, as a percentage of nominal voltage
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setVoltageLow(ModbusConnection conn, float percent) throws IOException;

	/**
	 * Get the enter service frequency high threshold, in Hz.
	 *
	 * @return the threshold
	 */
	@Nullable
	Float getFrequencyHigh();

	/**
	 * Set the enter service frequency high threshold.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param hertz
	 *        the threshold, in Hz
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setFrequencyHigh(ModbusConnection conn, float hertz) throws IOException;

	/**
	 * Get the enter service frequency low threshold, in Hz.
	 *
	 * @return the threshold
	 */
	@Nullable
	Float getFrequencyLow();

	/**
	 * Set the enter service frequency low threshold.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param hertz
	 *        the threshold, in Hz
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setFrequencyLow(ModbusConnection conn, float hertz) throws IOException;

	/**
	 * Get the enter service delay time, in seconds.
	 *
	 * @return the delay
	 */
	@Nullable
	Long getDelay();

	/**
	 * Set the enter service delay time.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param seconds
	 *        the delay, in seconds
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setDelay(ModbusConnection conn, long seconds) throws IOException;

	/**
	 * Get the enter service random delay, in seconds.
	 *
	 * @return the random delay
	 */
	@Nullable
	Long getRandomDelay();

	/**
	 * Set the enter service random delay.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param seconds
	 *        the random delay, in seconds
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setRandomDelay(ModbusConnection conn, long seconds) throws IOException;

	/**
	 * Get the enter service ramp time, in seconds.
	 *
	 * @return the ramp time
	 */
	@Nullable
	Long getRampTime();

	/**
	 * Set the enter service ramp time.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param seconds
	 *        the ramp time, in seconds
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setRampTime(ModbusConnection conn, long seconds) throws IOException;

	/**
	 * Get the enter service delay time remaining, in seconds.
	 *
	 * @return the delay remaining
	 */
	@Nullable
	Long getDelayRemaining();

}
