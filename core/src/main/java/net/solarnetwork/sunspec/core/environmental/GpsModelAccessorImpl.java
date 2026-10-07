/* ==================================================================
 * GpsModelAccessorImpl.java - 8/07/2023 9:38:58 am
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

package net.solarnetwork.sunspec.core.environmental;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.EnumSet;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.api.environmental.GpsModelAccessor;
import net.solarnetwork.sunspec.api.environmental.GpsModelRegister;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.core.support.NumberUtils;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link GpsModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class GpsModelAccessorImpl extends BaseModelAccessor implements GpsModelAccessor {

	/** The GPS model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 36;

	private static final int COORDINATES_SCALE = -7;

	/** The GPS date + time pattern. */
	public static final DateTimeFormatter GPS_TIMESTAMP_FORMATTER = DateTimeFormatter
			.ofPattern("yyyyMMddHHmmss.SSS'Z'").withZone(ZoneOffset.UTC)
			.withChronology(IsoChronology.INSTANCE);

	/**
	 * Constructor.
	 *
	 * @param data
	 *        the overall data object
	 * @param baseAddress
	 *        the base address for this model's data
	 * @param modelId
	 *        the model ID
	 */
	public GpsModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@link EnvironmentalModelId} class will be used as the
	 * {@code ModelId} instance.
	 * </p>
	 *
	 * @param data
	 *        the overall data object
	 * @param baseAddress
	 *        the base address for this model's data
	 * @param modelId
	 *        the model ID
	 */
	public GpsModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, EnvironmentalModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(GpsModelRegister.class);
	}

	@Override
	public @Nullable Instant getGpsTimestamp() {
		String time = getStringValue(GpsModelRegister.Time);
		String date = getStringValue(GpsModelRegister.Date);
		if ( time == null || date == null ) {
			return null;
		}
		try {
			return GPS_TIMESTAMP_FORMATTER.parse(date + time, Instant::from);
		} catch ( DateTimeParseException e ) {
			// ignore
		}
		return null;
	}

	@Override
	public @Nullable String getLocationName() {
		return getStringValue(GpsModelRegister.Location);
	}

	@Override
	public @Nullable BigDecimal getLatitude() {
		return NumberUtils.scaled(getValue(GpsModelRegister.Latitude), COORDINATES_SCALE);
	}

	@Override
	public @Nullable BigDecimal getLongitude() {
		return NumberUtils.scaled(getValue(GpsModelRegister.Longitude), COORDINATES_SCALE);
	}

	@Override
	public @Nullable Integer getAltitude() {
		return getIntegerValue(GpsModelRegister.Altitude);
	}

	@Override
	public @Nullable Object getPointValue(ModbusReference point) {
		if ( !(point instanceof GpsModelRegister r) ) {
			return null;
		}
		return switch (r) {
			case Time -> getGpsTimestamp();
			case Date -> null; // included in the Timestamp point
			case Location -> getLocationName();
			case Latitude -> getLatitude();
			case Longitude -> getLongitude();
			case Altitude -> getAltitude();
		};
	}

}
