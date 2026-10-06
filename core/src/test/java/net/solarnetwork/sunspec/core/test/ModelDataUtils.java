/* ==================================================================
 * ModelDataUtils.java - 9/10/2018 7:08:14 AM
 *
 * Copyright 2018 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.core.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.core.ModelDataFactory;
import net.solarnetwork.sunspec.core.meter.test.IntegerMeterModelAccessorTests;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.support.ModelData;
import net.solarnetwork.sunspec.modbus.support.StaticDataMapReadonlyModbusConnection;
import net.solarnetwork.sunspec.test.DataUtils;
import net.solarnetwork.util.IntShortMap;

/**
 * Helper utility methods for model data testing.
 *
 * @author matt
 * @version 2.3
 */
public final class ModelDataUtils {

	private static final Logger log = LoggerFactory.getLogger(IntegerMeterModelAccessorTests.class);

	/**
	 * Parse modbus test data.
	 *
	 * <p>
	 * This calls {@link DataUtils#parseModbusHexRegisterLines} so the address
	 * offsets are ignored and the returned array's data will always start at
	 * index {@literal 0}.
	 * </p>
	 *
	 * @param clazz
	 *        the class to load the resource from
	 * @param resource
	 *        the data resource to load
	 * @return the parsed data
	 */
	public static int[] parseTestData(Class<?> clazz, String resource) {
		try {
			return DataUtils.parseModbusHexRegisterLines(
					new BufferedReader(new InputStreamReader(clazz.getResourceAsStream(resource))));
		} catch ( IOException e ) {
			log.error("Error reading modbus data resource [{}]", resource, e);
			return new int[0];
		}
	}

	/**
	 * Parse modbus test data.
	 *
	 * <p>
	 * This calls {@link DataUtils#parseModbusHexRegisterLines} so the address
	 * offsets are ignored and the returned array's data will always start at
	 * index {@literal 0}.
	 * </p>
	 *
	 * @param clazz
	 *        the class to load the resource from
	 * @param resource
	 *        the data resource to load
	 * @return the parsed data
	 */
	public static IntShortMap parseOffsetTestData(Class<?> clazz, String resource) {
		IntShortMap result = new IntShortMap();
		try {
			Map<Integer, Integer> m = DataUtils.parseModbusHexRegisterMappingLines(
					new BufferedReader(new InputStreamReader(clazz.getResourceAsStream(resource))));
			if ( m != null ) {
				for ( Map.Entry<Integer, Integer> e : m.entrySet() ) {
					result.put(e.getKey(), e.getValue().shortValue());
				}
			}
		} catch ( IOException e ) {
			log.error("Error reading modbus data resource [{}]", resource, e);
		}
		return result;
	}

	/**
	 * Get a static, read-only model data instance using a class-path resource
	 * of modbus test data.
	 *
	 * <p>
	 * This calls {@link #parseTestData(Class, String)} to parse a modbus data
	 * text file and then
	 * {@link ModelDataFactory#getModelData(ModbusConnection)} to read the data
	 * into a {@link ModelData} instance.
	 * </p>
	 *
	 * @param clazz
	 *        the class to load the resource from
	 * @param resource
	 *        the data resource to load
	 * @return the model data
	 * @see #parseTestData(Class, String)
	 */
	public static ModelData getModelDataInstance(Class<?> clazz, String resource) {
		ModbusConnection conn = new StaticDataMapReadonlyModbusConnection(
				parseTestData(clazz, resource));
		try {
			return ModelDataFactory.getInstance().getModelData(conn);
		} catch ( IOException e ) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get a static, read-only model data instance using a class-path resource
	 * of modbus test data.
	 *
	 * <p>
	 * This calls {@link #parseTestData(Class, String)} to parse a modbus data
	 * text file and then
	 * {@link ModelDataFactory#getModelData(ModbusConnection)} to read the data
	 * into a {@link ModelData} instance.
	 * </p>
	 *
	 * @param clazz
	 *        the class to load the resource from
	 * @param resource
	 *        the data resource to load
	 * @param parseOffsets
	 *        if {@literal true} then parse the test data with
	 *        {@literal [addr]:} prefixes, otherwise ignore address prefixes and
	 *        treat the data as starting from zero
	 * @return the model data
	 * @see #parseTestData(Class, String)
	 */
	public static ModelData getModelDataInstance(Class<?> clazz, String resource, boolean parseOffsets) {
		ModbusConnection conn;
		if ( parseOffsets ) {
			conn = new StaticDataMapReadonlyModbusConnection(parseOffsetTestData(clazz, resource));
		} else {
			conn = new StaticDataMapReadonlyModbusConnection(parseTestData(clazz, resource));
		}
		try {
			return ModelDataFactory.getInstance().getModelData(conn);
		} catch ( IOException e ) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get a static, read-only model data instance using a class-path resource
	 * of modbus test data, with some register values replaced.
	 *
	 * <p>
	 * This calls {@link #parseTestData(Class, String)} to parse a modbus data
	 * text file, so the data starts at address {@literal 0}.
	 * </p>
	 *
	 * @param clazz
	 *        the class to load the resource from
	 * @param resource
	 *        the data resource to load
	 * @param address
	 *        the address of the first register to replace
	 * @param words
	 *        the register values to replace, starting at {@code address}
	 * @return the model data
	 * @since 2.3
	 */
	public static ModelData getModelDataInstanceWithRegisters(Class<?> clazz, String resource,
			int address, int... words) {
		final int[] data = parseTestData(clazz, resource);
		System.arraycopy(words, 0, data, address, words.length);
		return getModelDataInstance(new StaticDataMapReadonlyModbusConnection(data));
	}

	/**
	 * Get a writable Modbus connection to a class-path resource of modbus test
	 * data.
	 *
	 * <p>
	 * This calls {@link #parseTestData(Class, String)} to parse a modbus data
	 * text file, so the data starts at address {@literal 0}. The connection
	 * records the write requests made to it.
	 * </p>
	 *
	 * @param clazz
	 *        the class to load the resource from
	 * @param resource
	 *        the data resource to load
	 * @return the connection
	 * @since 2.3
	 */
	public static RecordingModbusConnection getWritableModbusConnection(Class<?> clazz,
			String resource) {
		return writableModbusConnection(parseTestData(clazz, resource));
	}

	/**
	 * Get a writable Modbus connection to a class-path resource of modbus test
	 * data, with some register values replaced.
	 *
	 * <p>
	 * This calls {@link #parseTestData(Class, String)} to parse a modbus data
	 * text file, so the data starts at address {@literal 0}. The connection
	 * records the write requests made to it, which do not include the replaced
	 * register values.
	 * </p>
	 *
	 * @param clazz
	 *        the class to load the resource from
	 * @param resource
	 *        the data resource to load
	 * @param address
	 *        the address of the first register to replace
	 * @param words
	 *        the register values to replace, starting at {@code address}
	 * @return the connection
	 * @since 2.3
	 */
	public static RecordingModbusConnection getWritableModbusConnectionWithRegisters(Class<?> clazz,
			String resource, int address, int... words) {
		final int[] data = parseTestData(clazz, resource);
		System.arraycopy(words, 0, data, address, words.length);
		return writableModbusConnection(data);
	}

	private static RecordingModbusConnection writableModbusConnection(int[] data) {
		final IntShortMap map = new IntShortMap(data.length);
		for ( int i = 0; i < data.length; i++ ) {
			map.putValue(i, data[i]);
		}
		return new RecordingModbusConnection(map);
	}

	/**
	 * Get a model data instance by discovering the models available on a Modbus
	 * connection.
	 *
	 * @param conn
	 *        the connection
	 * @return the model data
	 * @since 2.3
	 */
	public static ModelData getModelDataInstance(ModbusConnection conn) {
		try {
			return ModelDataFactory.getInstance().getModelData(conn);
		} catch ( IOException e ) {
			throw new RuntimeException(e);
		}
	}

}
