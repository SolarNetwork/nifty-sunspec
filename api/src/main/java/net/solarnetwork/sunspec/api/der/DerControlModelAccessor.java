/* ==================================================================
 * DerControlModelAccessor.java - 5/10/2026 10:31:52 am
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
 * API for accessing DER control model data.
 *
 * <p>
 * This API corresponds to the SunSpec model number <b>715</b>. Setter methods
 * write to the device immediately, and throw {@link IllegalArgumentException}
 * if the value is not valid for the point.
 * </p>
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public interface DerControlModelAccessor extends ModelAccessor {

	/**
	 * Get the local or remote control mode.
	 *
	 * <p>
	 * Local mode is used for manual or maintenance operations, and must be
	 * explicitly exited for the DER to be controlled remotely.
	 * </p>
	 *
	 * @return the mode, or {@code null} if not available
	 */
	@Nullable
	DerLocalRemoteControl getLocalRemoteControl();

	/**
	 * Get the DER heartbeat.
	 *
	 * <p>
	 * The DER increments this value every second, with periodic resets to zero.
	 * </p>
	 *
	 * @return the heartbeat, or {@code null} if not available
	 */
	@Nullable
	Long getDerHeartbeat();

	/**
	 * Get the controller heartbeat.
	 *
	 * @return the heartbeat, or {@code null} if not available
	 */
	@Nullable
	Long getControllerHeartbeat();

	/**
	 * Set the controller heartbeat.
	 *
	 * <p>
	 * The controller is expected to increment this value every second, with
	 * periodic resets to zero.
	 * </p>
	 *
	 * @param conn
	 *        the connection to write to
	 * @param heartbeat
	 *        the heartbeat value to set
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setControllerHeartbeat(ModbusConnection conn, long heartbeat) throws IOException;

	/**
	 * Reset any latched alarms.
	 *
	 * @param conn
	 *        the connection to write to
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void resetAlarms(ModbusConnection conn) throws IOException;

	/**
	 * Get the operation command.
	 *
	 * @return the command, or {@code null} if not available
	 */
	@Nullable
	DerOperationCommand getOperationCommand();

	/**
	 * Set the operation command.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param command
	 *        the command to send
	 * @throws IOException
	 *         if any communication error occurs
	 */
	void setOperationCommand(ModbusConnection conn, DerOperationCommand command) throws IOException;

}
