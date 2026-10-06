/* ==================================================================
 * ModbusConnectionAction.java - Jul 29, 2014 12:44:57 PM
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

/**
 * Callback API for performing an action with a {@link ModbusConnection}.
 *
 * <p>
 * If no result object is needed, simply use {@link Object} as the parameter
 * type and return {@code null} from
 * {@link #doWithConnection(ModbusConnection)}.
 * </p>
 *
 * @param <T>
 *        the action return type
 * @author matt
 * @version 1.0
 * @since 2.0
 */
@FunctionalInterface
public interface ModbusConnectionAction<T> {

	/**
	 * Perform an action with a {@link ModbusConnection}.
	 *
	 * <p>
	 * If no result object is needed, simply return {@code null}.
	 * </p>
	 *
	 * @param conn
	 *        the connection
	 * @return the result
	 * @throws IOException
	 *         if any IO error occurs
	 */
	@Nullable
	T doWithConnection(ModbusConnection conn) throws IOException;

}
