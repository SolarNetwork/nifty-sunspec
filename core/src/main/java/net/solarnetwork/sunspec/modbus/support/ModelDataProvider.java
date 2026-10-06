/* ==================================================================
 * ModelDataProvider.java - 10/07/2023 10:25:25 am
 *
 * Copyright 2023 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.modbus.support;

import org.jspecify.annotations.Nullable;
import net.solarnetwork.service.Identifiable;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * API for an identifiable provider of {@link ModelData} instances.
 *
 * @author matt
 * @version 1.0
 * @since 4.2
 */
public interface ModelDataProvider extends Identifiable {

	/**
	 * Get the model data.
	 *
	 * @return the model data
	 */
	@Nullable
	ModelData modelData();

	/**
	 * Get a {@link ModbusConnection} suitable for refreshing model data.
	 *
	 * @return a modbus connection
	 */
	@Nullable
	ModbusConnection modelDataModbusConnection();

}
