/* ==================================================================
 * ModelData.java - 22/05/2018 6:40:27 AM
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

package net.solarnetwork.sunspec.modbus.support;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.CommonModelId;
import net.solarnetwork.sunspec.api.CommonModelRegister;
import net.solarnetwork.sunspec.api.IntRange;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.ModelRegister;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReadFunction;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Base object for model data.
 *
 * @author matt
 * @version 1.0
 */
public class ModelData extends ModbusData implements CommonModelAccessor {

	private static final Logger LOG = LoggerFactory.getLogger(ModelData.class);

	private final int baseAddress;
	private final int blockAddress;
	private int maxReadWordsCount;
	private List<ModelAccessor> models;

	private volatile @Nullable ConcurrentMap<String, Object> metadata;

	/**
	 * Constructor.
	 *
	 * @param baseAddress
	 *        the base Modbus address
	 */
	public ModelData(int baseAddress) {
		super();
		this.maxReadWordsCount = Integer.MAX_VALUE;
		this.baseAddress = baseAddress;
		this.blockAddress = baseAddress + 2;
		this.models = new ArrayList<>(1);
		this.metadata = null;
	}

	/**
	 * Copy constructor.
	 *
	 * @param other
	 *        the data to copy
	 */
	public ModelData(ModbusData other) {
		super(other);
		if ( other instanceof ModelData md ) {
			this.maxReadWordsCount = md.maxReadWordsCount;
			this.baseAddress = md.baseAddress;
			this.blockAddress = md.blockAddress;
			this.models = new ArrayList<>(md.models);
			if ( md.metadata != null ) {
				this.metadata = new ConcurrentHashMap<>(8, 0.9f, 1);
				this.metadata.putAll(md.metadata);
			} else {
				this.metadata = null;
			}
		} else {
			this.maxReadWordsCount = Integer.MAX_VALUE;
			this.baseAddress = 0;
			this.blockAddress = 2;
			this.models = new ArrayList<>(1);
			this.metadata = null;
		}
	}

	@Override
	public ModbusData copy() {
		return new ModelData(this);
	}

	/**
	 * Get a snapshot copy of the model.
	 *
	 * <p>
	 * This is essentially the same as {@link #copy()} but cast to
	 * {@code ModelData}.
	 * </p>
	 *
	 * @return the snapshot
	 * @see #copy()
	 */
	public ModelData getSnapshot() {
		return (ModelData) this.copy();
	}

	/**
	 * Get the first-available model instance.
	 *
	 * @return the first available model, or {@code null}
	 */
	public @Nullable ModelAccessor getModel() {
		return (models != null && !models.isEmpty() ? models.get(0) : null);
	}

	/**
	 * Get the first-available model as a specific type.
	 *
	 * @param <T>
	 *        the model type
	 * @return the model
	 * @throws ClassCastException
	 *         if the model is not of the requested type
	 */
	@SuppressWarnings("TypeParameterUnusedInFormals")
	public <T extends ModelAccessor> @Nullable T getTypedModel() {
		@SuppressWarnings("unchecked")
		T result = (T) getModel();
		return result;
	}

	/**
	 * Find the first-available model of a specific type.
	 *
	 * @param <T>
	 *        the model type
	 * @param type
	 *        the type of model to get
	 * @return the found model, or {@code null} if not found
	 */
	public <T extends ModelAccessor> @Nullable T findTypedModel(Class<T> type) {
		if ( CommonModelAccessor.class.isAssignableFrom(type) ) {
			@SuppressWarnings("unchecked")
			T result = (T) this;
			return result;
		}
		List<ModelAccessor> list = getModels();
		if ( list != null ) {
			for ( ModelAccessor ma : list ) {
				if ( type.isAssignableFrom(ma.getClass()) ) {
					@SuppressWarnings("unchecked")
					T result = (T) ma;
					return result;
				}
			}
		}
		return null;
	}

	/**
	 * Get the list of model instances.
	 *
	 * @return the model instances
	 */
	public List<ModelAccessor> getModels() {
		return models;
	}

	/**
	 * Get the maximum number of Modbus registers to read in one request.
	 *
	 * @return the maximum read word count; defaults to
	 *         {@link Integer#MAX_VALUE}
	 */
	public int getMaxReadWordsCount() {
		return maxReadWordsCount;
	}

	/**
	 * Set the maximum number of Modbus registers to read in one request.
	 *
	 * @param maxReadWordsCount
	 *        the maxReadWordsCount to set; anything less than {@literal 1} is
	 *        ignored; set to {@link Integer#MAX_VALUE} for no limit
	 */
	public void setMaxReadWordsCount(int maxReadWordsCount) {
		if ( maxReadWordsCount < 1 ) {
			return;
		}
		this.maxReadWordsCount = maxReadWordsCount;
	}

	@Override
	public int getBaseAddress() {
		return baseAddress;
	}

	@Override
	public int getBlockAddress() {
		return blockAddress;
	}

	@Override
	public ModelId getModelId() {
		return CommonModelId.CommonModel;
	}

	@Override
	public int getModelLength() {
		Number n = getNumber(ModelRegister.ModelLength, baseAddress);
		return (n != null ? n.intValue() : 0);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This implementation returns the ranges of the common model strings.
	 * </p>
	 *
	 */
	@Override
	public List<IntRange> getUnsplittableAddressRanges() {
		final List<IntRange> result = new ArrayList<>(5);
		ModbusUtils.addMultiRegisterAddressRanges(result, blockAddress,
				EnumSet.allOf(CommonModelRegister.class));
		return result;
	}

	/**
	 * Update a mutable data object with data read from a Modbus connection,
	 * using the {@link ModbusReadFunction#ReadHoldingRegister} function.
	 *
	 * @param conn
	 *        the connection
	 * @param m
	 *        the mutable data
	 * @param ranges
	 *        the list of register addresses to read
	 * @see #updateData(ModbusConnection, MutableModbusData, ModbusReadFunction,
	 *      IntRange[])
	 * @throws IOException
	 *         if any communication error occurs
	 */
	protected static void updateData(ModbusConnection conn, MutableModbusData m, IntRange[] ranges)
			throws IOException {
		updateData(conn, m, ModbusReadFunction.ReadHoldingRegister, ranges);
	}

	/**
	 * Update a mutable data object with data read from a Modbus connection.
	 *
	 * <p>
	 * This method will read a set of Modbus registers, treating them as
	 * unsigned short values and storing them on {@code m} via
	 * {@link MutableModbusData#saveDataArray(int[], int)}.
	 * </p>
	 *
	 * @param conn
	 *        the connection
	 * @param m
	 *        the mutable data
	 * @param function
	 *        the Modbus read function to use
	 * @param ranges
	 *        the list of register addresses to read
	 * @throws IOException
	 *         if any communication error occurs
	 */
	protected static void updateData(ModbusConnection conn, MutableModbusData m,
			ModbusReadFunction function, IntRange[] ranges) throws IOException {
		for ( IntRange r : ranges ) {
			if ( LOG.isDebugEnabled() ) {
				LOG.debug("Reading modbus {} range {}-{} ({})", conn.getUnitId(), r.min(),
						r.min() + r.length(), r.length());
			}
			short[] data = conn.readWords(function, r.min(), r.length());
			m.saveDataArray(data, r.min());
		}
	}

	/**
	 * Update a mutable data object with the data of a model, read from a Modbus
	 * connection one address range at a time.
	 *
	 * <p>
	 * Each range is read before the next one is chosen with
	 * {@link ModelAccessor#getAddressRange(int, int)}, so the ranges can be
	 * based on data read from earlier ranges, such as the number of repeating
	 * block instances or points, and avoid splitting multi-register values
	 * across requests.
	 * </p>
	 *
	 * @param conn
	 *        the connection
	 * @param m
	 *        the mutable data
	 * @param accessor
	 *        the model to read
	 * @throws IOException
	 *         if any communication error occurs
	 */
	private void updateModelData(ModbusConnection conn, MutableModbusData m, ModelAccessor accessor)
			throws IOException {
		final int end = accessor.getBlockAddress() + accessor.getModelLength();
		for ( int address = accessor.getBlockAddress(); address < end; ) {
			final IntRange range = accessor.getAddressRange(address, maxReadWordsCount);
			updateData(conn, m, new IntRange[] { range });
			address = range.max() + 1;
		}
	}

	/**
	 * Read the common model properties from the device.
	 *
	 * @param conn
	 *        the connection
	 * @throws IOException
	 *         if any communication error occurs
	 */
	public final void readCommonModelData(final ModbusConnection conn) throws IOException {
		performUpdates(new ModbusDataUpdateAction() {

			@Override
			public boolean updateModbusData(MutableModbusData m) throws IOException {
				// load in our model header to find the common model length (65/66)
				short[] data = conn.readWords(ModbusReadFunction.ReadHoldingRegister, baseAddress, 2);
				m.saveDataArray(data, baseAddress);
				updateModelData(conn, m, ModelData.this);
				return true;
			}
		});
	}

	/**
	 * Add a model accessor to this model.
	 *
	 * @param modelLength
	 *        the model length
	 * @param accessor
	 *        the accessor to associate with this model
	 * @throws IOException
	 *         if any communication error occurs
	 */
	public final void addModel(int modelLength, ModelAccessor accessor) throws IOException {
		performUpdates(new ModbusDataUpdateAction() {

			@Override
			public boolean updateModbusData(MutableModbusData m) {
				if ( LOG.isDebugEnabled() ) {
					LOG.debug("Discovered {} @ {}, length {}", accessor.getModelId(),
							accessor.getBaseAddress(), modelLength);
				}
				m.saveDataArray(new int[] { accessor.getModelId().getId(), modelLength },
						accessor.getBaseAddress());
				models.add(accessor);
				return true;
			}
		});
	}

	/**
	 * Read the model properties from the device for all configured models.
	 *
	 * <p>
	 * This method will iterate over all {@link ModelAccessor} instances that
	 * have been added via {@link #addModel(int, ModelAccessor)}, and read the
	 * data necessary for all their properties.
	 * </p>
	 *
	 * @param conn
	 *        the connection
	 * @throws IOException
	 *         if any communication error occurs
	 */
	public void readModelData(final ModbusConnection conn) throws IOException {
		readModelData(conn, getModels());
	}

	/**
	 * Read the model properties from the device for specific models.
	 *
	 * <p>
	 * This method will iterate over the provided {@link ModelAccessor}
	 * instances and read the data necessary for each of their properties.
	 * </p>
	 *
	 * @param conn
	 *        the connection
	 * @param accessors
	 *        the models to read data for
	 * @throws IOException
	 *         if any communication error occurs
	 */
	public void readModelData(final ModbusConnection conn, final @Nullable List<ModelAccessor> accessors)
			throws IOException {
		if ( accessors == null ) {
			return;
		}
		performUpdates(new ModbusDataUpdateAction() {

			@Override
			public boolean updateModbusData(MutableModbusData m) throws IOException {
				for ( ModelAccessor accessor : accessors ) {
					updateModelData(conn, m, accessor);
				}
				return true;
			}
		});
	}

	/**
	 * Read the model properties from the device for specific models.
	 *
	 * <p>
	 * This method will iterate over the provided {@link ModelAccessor}
	 * instances and read the data necessary for each of their properties.
	 * </p>
	 *
	 * @param conn
	 *        the connection
	 * @param accessors
	 *        the models to read data for
	 * @throws IOException
	 *         if any communication error occurs
	 */
	public void readModelData(final ModbusConnection conn, final ModelAccessor @Nullable... accessors)
			throws IOException {
		if ( accessors == null ) {
			return;
		}
		performUpdates(new ModbusDataUpdateAction() {

			@Override
			public boolean updateModbusData(MutableModbusData m) throws IOException {
				for ( ModelAccessor accessor : accessors ) {
					updateModelData(conn, m, accessor);
				}
				return true;
			}
		});
	}

	@Override
	public @Nullable String getManufacturer() {
		return getStringValue(CommonModelRegister.Manufacturer, blockAddress);
	}

	@Override
	public @Nullable String getModelName() {
		return getStringValue(CommonModelRegister.Model, blockAddress);
	}

	@Override
	public @Nullable String getOptions() {
		return getStringValue(CommonModelRegister.Options, blockAddress);
	}

	@Override
	public @Nullable String getVersion() {
		return getStringValue(CommonModelRegister.Version, blockAddress);
	}

	@Override
	public @Nullable String getSerialNumber() {
		return getStringValue(CommonModelRegister.SerialNumber, blockAddress);
	}

	/**
	 * Get a SunSpec string point value.
	 *
	 * <p>
	 * SunSpec strings are UTF-8 encoded, and terminated or padded with a NULL
	 * byte. Bytes after the first NULL byte are ignored, and leading and
	 * trailing whitespace is removed.
	 * </p>
	 *
	 * @param ref
	 *        the reference to the string point
	 * @param offset
	 *        the address offset to add to {@link ModbusReference#getAddress()}
	 * @return the string value, or {@code null} if the string is empty, which
	 *         includes the SunSpec "not implemented" value
	 */
	public @Nullable String getStringValue(ModbusReference ref, int offset) {
		final byte[] bytes = getBytes(ref.getAddress() + offset, ref.getWordLength());
		int len = 0;
		while ( len < bytes.length && bytes[len] != 0 ) {
			len++;
		}
		final String s = new String(bytes, 0, len, StandardCharsets.UTF_8).trim();
		return (s.isEmpty() ? null : s);
	}

	@Override
	public @Nullable Integer getDeviceAddress() {
		Number n = getNumber(CommonModelRegister.DeviceAddress, blockAddress);
		return (n != null ? n.intValue() : null);
	}

	/**
	 * Get a metadata value.
	 *
	 * @param key
	 *        the key of the metadata to get
	 * @return the metadata value, or {@code null}
	 */
	public @Nullable Object getMetadataValue(String key) {
		ConcurrentMap<String, Object> m = this.metadata;
		Object result = null;
		if ( m != null ) {
			result = m.get(key);
		}
		return result;
	}

	/**
	 * Set/remove a metadata value.
	 *
	 * @param key
	 *        the key of the value to set/remove
	 * @param value
	 *        the value to set, or {@code null} to remove the value associated
	 *        with {@code key}
	 */
	public void putMetadataValue(String key, @Nullable Object value) {
		ConcurrentMap<String, Object> m = this.metadata;
		if ( value == null ) {
			if ( m != null ) {
				m.remove(key);
			}
		} else {
			if ( m == null ) {
				synchronized ( this ) {
					if ( this.metadata == null ) {
						m = new ConcurrentHashMap<>(8, 0.9f, 1);
						this.metadata = m;
					} else {
						m = this.metadata;
					}
				}
			}
			m.put(key, value);
		}
	}

}
