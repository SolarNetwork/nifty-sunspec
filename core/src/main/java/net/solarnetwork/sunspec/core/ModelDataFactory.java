/* ==================================================================
 * ModelDataFactory.java - 22/05/2018 9:40:11 AM
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

package net.solarnetwork.sunspec.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Properties;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.GenericModelId;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.ModelRegister;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReadFunction;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * A factory for creating concrete {@link ModelData} instances based on
 * discovery of SunSpec properties on a device.
 *
 * <p>
 * This factory looks for a
 * {@literal META-INF/sunspec/model-accessors.properties} resource that contains
 * a mapping of SunSpec model IDs to associated {@link ModelAccessor} classes.
 * The classes are expected to provide a public constructor that accepts the
 * following arguments:
 * </p>
 *
 * <ol>
 * <li>a {@link ModelData} instance</li>
 * <li>an {@code int} base Modbus address for the associated model data</li>
 * <li>an {@code int} model ID value</li>
 * </ol>
 *
 * <p>
 * If no {@literal model-accessors.properties} resource is available, the
 * factory falls back to using the
 * {@literal net/solarnetwork/sunspec/core/internal/model-accessors-default.properties}
 * resource provided by this bundle.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public class ModelDataFactory {

	/**
	 * The default value for the maximum number of Modbus words to read at one
	 * time.
	 *
	 */
	public static final int DEFAULT_MAX_READ_WORDS_COUNT = 100;

	/**
	 * The name of the class-path resource with the {@code ModelAccessor}
	 * properties mapping.
	 *
	 */
	public static final String MODEL_ACCESSOR_PROPERTIES_RESOURCE_NAME = "META-INF/sunspec/model-accessors.properties";

	/**
	 * The name of the class-path resource with the built-in default
	 * {@code ModelAccessor} properties mapping.
	 *
	 */
	public static final String DEFAULT_MODEL_ACCESSOR_PROPERTIES_RESOURCE_NAME = "net/solarnetwork/sunspec/core/internal/model-accessors-default.properties";

	private static final Logger log = LoggerFactory.getLogger(ModelDataFactory.class);

	private @Nullable Properties accessorProperties;

	/**
	 * Get a factory instance.
	 *
	 * <p>
	 * This will trigger the loading of the model accessor properties resource,
	 * as described in {@link #getModelAccessorProperties()}.
	 * </p>
	 *
	 * @return the factory
	 */
	public static ModelDataFactory getInstance() {
		ModelDataFactory factory = new ModelDataFactory();
		factory.loadModelAccessorProperties();
		return factory;
	}

	/**
	 * Default constructor.
	 */
	protected ModelDataFactory() {
		super();
	}

	/**
	 * Find the base SunSpec register address.
	 *
	 * <p>
	 * This will search the {@link ModelRegister#BASE_ADDRESSES} list and return
	 * the first that contain the expected SunSpec magic bytes.
	 * </p>
	 *
	 * @param conn
	 *        the Modbus connection to use
	 * @return the discovered address
	 * @throws IOException
	 *         if the address is not discovered or any IO error occurs
	 */
	public int findSunSpecBaseAddress(ModbusConnection conn) throws IOException {
		for ( ModelRegister r : ModelRegister.BASE_ADDRESSES ) {
			if ( isSunSpecBaseAddress(conn, r.getAddress()) ) {
				return r.getAddress();
			}
		}
		throw new IOException("SunSpec ID 'SunS' not found at any known base address.");
	}

	private boolean isSunSpecBaseAddress(ModbusConnection conn, final int address) {
		try {
			String s = conn.readString(ModbusReadFunction.ReadHoldingRegister, address,
					ModelRegister.BaseAddress.getWordLength(), true, StandardCharsets.US_ASCII);
			if ( ModelRegister.BASE_ADDRESS_MAGIC_STRING.equals(s) ) {
				return true;
			}
			if ( log.isDebugEnabled() ) {
				String hex = null;
				if ( s != null ) {
					hex = HexFormat.of().withUpperCase()
							.formatHex(s.getBytes(StandardCharsets.US_ASCII));
				}
				log.debug("SunSpec ID 'SunS' not found at base address {}; found [{}]", address, hex);
			}
		} catch ( Exception e ) {
			// in case device throws error reading from address that is not base address, fail
			log.warn("Error looking for SunSpec ID 'SunS' at base address {}: {}", address,
					e.toString());
		}
		return false;
	}

	/**
	 * Create a new model data instance by discovering the model from a device
	 * via a Modbus connection.
	 *
	 * <p>
	 * This method calls {@link #getModelData(ModbusConnection, int)} with a
	 * {@link #DEFAULT_MAX_READ_WORDS_COUNT} maximum read word count.
	 * </p>
	 *
	 * @param conn
	 *        the modbus connection
	 * @return the data, with all model properties loaded
	 * @throws IOException
	 *         if any communication error occurs or no supported model data can
	 *         be discovered
	 */
	public ModelData getModelData(ModbusConnection conn) throws IOException {
		return getModelData(conn, DEFAULT_MAX_READ_WORDS_COUNT);
	}

	/**
	 * Create a new model data instance by discovering the model from a device
	 * via a Modbus connection.
	 *
	 * <p>
	 * This method calls {@link #getModelData(ModbusConnection, int)} with a
	 * {@link #DEFAULT_MAX_READ_WORDS_COUNT} maximum read word count.
	 * </p>
	 *
	 * @param conn
	 *        the modbus connection
	 * @param load
	 *        if {@literal true} then read all model properties, otherwise
	 *        {@link ModelData#readModelData(ModbusConnection)} or
	 *        {@link ModelData#readModelData(ModbusConnection, java.util.List)}
	 *        must be called to load the data for any discovered model(s)
	 * @return the data, with all model properties loaded
	 * @throws IOException
	 *         if any communication error occurs or no supported model data can
	 *         be discovered
	 */
	public ModelData getModelData(ModbusConnection conn, boolean load) throws IOException {
		final int sunSpecBaseAddress = findSunSpecBaseAddress(conn);
		return getModelData(conn, DEFAULT_MAX_READ_WORDS_COUNT, sunSpecBaseAddress, load);
	}

	/**
	 * Create a new model data instance by discovering the model from a device
	 * via a Modbus connection.
	 *
	 * <p>
	 * This method will look for the Modbus base address at any of the SunSpec
	 * defined base addresses, as defined in
	 * {@link ModelRegister#BASE_ADDRESSES}.
	 * </p>
	 *
	 * @param conn
	 *        the modbus connection
	 * @param maxReadWordsCount
	 *        the maxReadWordsCount to set; anything less than {@literal 1} is
	 *        ignored; pass {@link Integer#MAX_VALUE} for no limit
	 * @return the data, with all model properties loaded
	 * @throws IOException
	 *         if any communication error occurs or no supported model data can
	 *         be discovered
	 */
	public ModelData getModelData(ModbusConnection conn, int maxReadWordsCount) throws IOException {
		final int sunSpecBaseAddress = findSunSpecBaseAddress(conn);
		return readModelData(conn, maxReadWordsCount, sunSpecBaseAddress, true);
	}

	/**
	 * Create a new model data instance by discovering the model from a device
	 * via a Modbus connection.
	 *
	 * @param conn
	 *        the modbus connection
	 * @param maxReadWordsCount
	 *        the maxReadWordsCount to set; anything less than {@literal 1} is
	 *        ignored; pass {@link Integer#MAX_VALUE} for no limit
	 * @param baseAddress
	 *        the Modbus address where the {@literal SunS} magic bytes start
	 * @return the data, with all model properties loaded
	 * @throws IOException
	 *         if any communication error occurs or no supported model data can
	 *         be discovered
	 */
	public ModelData getModelData(ModbusConnection conn, int maxReadWordsCount, int baseAddress)
			throws IOException {
		if ( !isSunSpecBaseAddress(conn, baseAddress) ) {
			throw new IOException(String
					.format("SunSpec ID 'SunS' not found at base address 0x%1$04x (%1$d)", baseAddress));
		}
		return readModelData(conn, maxReadWordsCount, baseAddress, true);
	}

	/**
	 * Create a new model data instance by discovering the model from a device
	 * via a Modbus connection.
	 *
	 * @param conn
	 *        the modbus connection
	 * @param maxReadWordsCount
	 *        the maxReadWordsCount to set; anything less than {@literal 1} is
	 *        ignored; pass {@link Integer#MAX_VALUE} for no limit
	 * @param baseAddress
	 *        the Modbus address where the {@literal SunS} magic bytes start
	 * @param load
	 *        if {@literal true} then read all model properties, otherwise
	 *        {@link ModelData#readModelData(ModbusConnection)} or
	 *        {@link ModelData#readModelData(ModbusConnection, java.util.List)}
	 *        must be called to load the data for any discovered model(s)
	 * @return the data, will all model properties loaded
	 * @throws IOException
	 *         if any communication error occurs or no supported model data can
	 *         be discovered
	 */
	public ModelData getModelData(ModbusConnection conn, int maxReadWordsCount, int baseAddress,
			boolean load) throws IOException {
		if ( !isSunSpecBaseAddress(conn, baseAddress) ) {
			throw new IOException(String
					.format("SunSpec ID 'SunS' not found at base address 0x%1$04x (%1$d)", baseAddress));
		}
		return readModelData(conn, maxReadWordsCount, baseAddress, load);
	}

	/**
	 * Create a new model data instance by discovering the model from a device
	 * via a Modbus connection.
	 *
	 * <p>
	 * This method calls {@link #discoverModels(ModbusConnection, int)} with a
	 * {@link #DEFAULT_MAX_READ_WORDS_COUNT} maximum read word count.
	 * </p>
	 *
	 * @param conn
	 *        the modbus connection
	 * @return the data, without loading any model properties
	 * @throws IOException
	 *         if any communication error occurs or no supported model data can
	 *         be discovered
	 */
	public ModelData discoverModels(ModbusConnection conn) throws IOException {
		return discoverModels(conn, DEFAULT_MAX_READ_WORDS_COUNT);
	}

	/**
	 * Create a new model data instance by discovering the model from a device
	 * via a Modbus connection.
	 *
	 * <p>
	 * This method will look for the Modbus base address at any of the SunSpec
	 * defined base addresses, as defined in
	 * {@link ModelRegister#BASE_ADDRESSES}.
	 * </p>
	 *
	 * @param conn
	 *        the modbus connection
	 * @param maxReadWordsCount
	 *        the maxReadWordsCount to set; anything less than {@literal 1} is
	 *        ignored; pass {@link Integer#MAX_VALUE} for no limit
	 * @return the data, without loading any model properties
	 * @throws IOException
	 *         if any communication error occurs or no supported model data can
	 *         be discovered
	 */
	public ModelData discoverModels(ModbusConnection conn, int maxReadWordsCount) throws IOException {
		final int sunSpecBaseAddress = findSunSpecBaseAddress(conn);
		return readModelData(conn, maxReadWordsCount, sunSpecBaseAddress, false);
	}

	/**
	 * Create a new model data instance by discovering the model from a device
	 * via a Modbus connection.
	 *
	 * @param conn
	 *        the modbus connection
	 * @param maxReadWordsCount
	 *        the maxReadWordsCount to set; anything less than {@literal 1} is
	 *        ignored; pass {@link Integer#MAX_VALUE} for no limit
	 * @param baseAddress
	 *        the Modbus address where the {@literal SunS} magic bytes start
	 * @return the data, without loading any model properties
	 * @throws IOException
	 *         if any communication error occurs or no supported model data can
	 *         be discovered
	 */
	public ModelData discoverModels(ModbusConnection conn, int maxReadWordsCount, int baseAddress)
			throws IOException {
		if ( !isSunSpecBaseAddress(conn, baseAddress) ) {
			throw new IOException(String
					.format("SunSpec ID 'SunS' not found at base address 0x%1$04x (%1$d)", baseAddress));
		}
		return readModelData(conn, maxReadWordsCount, baseAddress, false);
	}

	private ModelData readModelData(ModbusConnection conn, int maxReadWordsCount, int sunSpecBaseAddress,
			boolean load) throws IOException {
		ModelData data = new ModelData(sunSpecBaseAddress + 2);
		data.setMaxReadWordsCount(maxReadWordsCount);
		data.readCommonModelData(conn);

		ModelAccessor model = data;
		do {
			int nextModelAddress = model.getBlockAddress() + model.getModelLength();
			short[] words = conn.readWords(ModbusReadFunction.ReadHoldingRegister, nextModelAddress, 2);
			model = null;
			if ( words != null && words.length > 1 ) {
				final int modelId = Short.toUnsignedInt(words[0]);
				if ( modelId != ModelId.SUN_SPEC_END_ID ) {
					final int modelLen = Short.toUnsignedInt(words[1]);
					ModelAccessor accessor = createAccessor(data, nextModelAddress, modelId);
					data.addModel(modelLen, accessor);
					model = accessor;
				}
			}
		} while ( model != null );
		if ( load ) {
			data.readModelData(conn);
		}
		return data;
	}

	private void loadModelAccessorProperties() {
		accessorProperties = getModelAccessorProperties();
	}

	/**
	 * Load the {@link ModelAccessor} properties.
	 *
	 * <p>
	 * This factory looks for a
	 * {@literal META-INF/sunspec/model-accessors.properties} resource that
	 * contains a mapping of SunSpec model IDs to associated
	 * {@link ModelAccessor} classes. If that resource is not available, then
	 * the
	 * {@literal net/solarnetwork/sunspec/core/internal/model-accessors-default.properties}
	 * resource provided by this bundle will be used. Here's an example of the
	 * format of this resource:
	 * </p>
	 *
	 * <pre>
	 * 101 = net.solarnetwork.sunspec.core.inverter.IntegerInverterModelAccessor
	 * 201 = net.solarnetwork.sunspec.core.meter.IntegerMeterModelAccessor
	 * </pre>
	 *
	 * @return the model accessor properties
	 */
	protected @Nullable Properties getModelAccessorProperties() {
		Properties props = null;

		// try to load mapping with context class loader, if available
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if ( cl != null ) {
			props = propertiesWithClassLoader(cl);
		}

		// fall back to this class' class loader if not found
		if ( props == null ) {
			cl = getClass().getClassLoader();
			props = propertiesWithClassLoader(cl);
		}

		return props;
	}

	private @Nullable Properties propertiesWithClassLoader(ClassLoader cl) {
		for ( String rsrcName : new String[] { MODEL_ACCESSOR_PROPERTIES_RESOURCE_NAME,
				DEFAULT_MODEL_ACCESSOR_PROPERTIES_RESOURCE_NAME } ) {
			try (InputStream in = cl.getResourceAsStream(rsrcName)) {
				if ( in != null ) {
					Properties props = new Properties();
					props.load(in);
					log.debug("Loaded SunSpec ModelAccessor mappings from {}", rsrcName);
					return props;
				}
			} catch ( IOException e ) {
				// ignore
			}
		}
		return null;
	}

	private ModelAccessor createAccessor(ModelData data, int baseAddress, int modelId) {
		Properties props = accessorProperties;
		if ( props != null ) {
			String accessorClassName = props.getProperty(String.valueOf(modelId));
			if ( accessorClassName != null ) {
				Throwable t = null;
				for ( ClassLoader cl : new ClassLoader[] {
						Thread.currentThread().getContextClassLoader(),
						data.getClass().getClassLoader() } ) {
					if ( cl == null ) {
						continue;
					}
					try {
						Class<?> clazz = cl.loadClass(accessorClassName);
						if ( clazz != null ) {
							Class<? extends ModelAccessor> maClass = clazz
									.asSubclass(ModelAccessor.class);
							Constructor<? extends ModelAccessor> constr = maClass
									.getConstructor(ModelData.class, Integer.TYPE, Integer.TYPE);
							return constr.newInstance(data, baseAddress, modelId);
						}
					} catch ( ClassNotFoundException | NoSuchMethodException | SecurityException
							| InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e ) {
						t = e;
					}
				}
				if ( t != null ) {
					log.warn("Error loading SunSpec ModelAccessor class {} for model {} {}: {}",
							accessorClassName, modelId, t.toString());
				}
			}
		}

		// fall back to generic
		return new GenericModelAccessor(data, baseAddress, new GenericModelId(modelId));
	}

}
