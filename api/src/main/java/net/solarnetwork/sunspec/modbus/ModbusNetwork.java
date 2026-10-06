/* ==================================================================
 * ModbusNetwork.java - Jul 29, 2014 11:17:48 AM
 *
 * Copyright 2007-2014 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.modbus;

import java.io.IOException;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.service.Identifiable;

/**
 * High level Modbus API.
 *
 * <p>
 * This API aims to simplify accessing Modbus capable devices without having any
 * direct dependency on any specific Modbus implementation.
 * </p>
 *
 * @author matt
 * @version 2.0
 * @since 2.0
 */
public interface ModbusNetwork extends Identifiable {

	/**
	 * Perform some action that requires a {@link ModbusConnection}, returning
	 * the result.
	 *
	 * <p>
	 * The {@link ModbusConnectionAction#doWithConnection(ModbusConnection)}
	 * method will be called and the result returned by this method. The
	 * {@link ModbusConnection} passed will already be opened, and it will be
	 * closed automatically after the action is complete.
	 * </p>
	 *
	 * @param <T>
	 *        the result type
	 * @param unitId
	 *        the Modbus unit ID to address
	 * @param action
	 *        the callback whose result to return
	 * @return the result of calling
	 *         {@link ModbusConnectionAction#doWithConnection(ModbusConnection)}
	 * @throws IOException
	 *         if any IO error occurs
	 */
	<T> @Nullable T performAction(int unitId, ModbusConnectionAction<T> action) throws IOException;

	/**
	 * Create a connection to a specific Modbus device.
	 *
	 * <p>
	 * The returned connection will not be opened and must be closed when
	 * finished being used.
	 * </p>
	 *
	 * @param unitId
	 *        the Modbus unit ID to connect with
	 * @return a new connection, or {@code null} if the connection cannot be
	 *         created
	 */
	@Nullable
	ModbusConnection createConnection(int unitId);

}
