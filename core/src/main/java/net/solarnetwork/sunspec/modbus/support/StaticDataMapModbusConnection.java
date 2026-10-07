/* ==================================================================
 * StaticDataMapModbusConnection.java - 8/11/2019 11:28:16 am
 *
 * Copyright 2019 SolarNetwork.net Dev Team
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

import net.solarnetwork.sunspec.core.support.IntShortMap;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusWritingFunction;

/**
 * {@link ModbusConnection} for reading/writing static data.
 *
 * <p>
 * This class can be useful in tests working with Modbus connections.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public class StaticDataMapModbusConnection extends StaticDataMapReadonlyModbusConnection {

	/**
	 * Constructor.
	 *
	 * @param data
	 *        the starting data
	 */
	public StaticDataMapModbusConnection(IntShortMap data) {
		super(data);
	}

	@Override
	public void writeWords(ModbusWritingFunction function, int address, short[] values) {
		final IntShortMap data = getData();
		for ( int i = 0, len = values.length; i < len; i++ ) {
			data.putValue(address + i, (int) values[i]);
		}
	}

}
