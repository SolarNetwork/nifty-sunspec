/* ==================================================================
 * TestDevices.java - 7/10/2026 9:31:18 pm
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

package net.solarnetwork.sunspec.modbus.nifty.test;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Map;
import net.solarnetwork.sunspec.core.ModelDataFactory;
import net.solarnetwork.sunspec.core.support.IntShortMap;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.support.ModelData;
import net.solarnetwork.sunspec.modbus.support.StaticDataMapReadonlyModbusConnection;
import net.solarnetwork.sunspec.test.DataUtils;

/**
 * Simulated devices for tests, from the register dumps in this package.
 *
 * @author matt
 * @version 1.0
 */
public final class TestDevices {

	/**
	 * An OutBack device with a split phase meter, the DER models, and a vendor
	 * model.
	 */
	public static final String DER_DUMP = "test-data-der-01.txt";

	/**
	 * An SMA 3-phase inverter with inverter control models, some without
	 * accessors, and the repeating MPPT model.
	 */
	public static final String INVERTER_DUMP = "test-data-103-05.txt";

	private TestDevices() {
		// not available
	}

	/**
	 * Get a read-only connection to the registers of a register dump, at their
	 * dump addresses.
	 *
	 * @param resource
	 *        the dump resource name
	 * @return the connection
	 */
	public static StaticDataMapReadonlyModbusConnection connection(String resource) {
		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(TestDevices.class.getResourceAsStream(resource), UTF_8))) {
			final Map<Integer, Integer> registers = DataUtils.parseModbusHexRegisterMappingLines(in);
			final IntShortMap data = new IntShortMap();
			registers.forEach((k, v) -> data.putValue(k, v.shortValue()));
			return new StaticDataMapReadonlyModbusConnection(data);
		} catch ( IOException e ) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Discover the models of a connection, and read all their data.
	 *
	 * @param conn
	 *        the connection
	 * @return the model data
	 */
	public static ModelData modelData(ModbusConnection conn) {
		try {
			return ModelDataFactory.getInstance().getModelData(conn);
		} catch ( IOException e ) {
			throw new UncheckedIOException(e);
		}
	}

}
