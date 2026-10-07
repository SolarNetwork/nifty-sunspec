/* ==================================================================
 * CommonModelAccessor.java - 22/05/2018 9:13:28 AM
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

package net.solarnetwork.sunspec.api;

import java.util.Collection;
import java.util.EnumSet;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * API for accessing common model data.
 *
 * @author matt
 * @version 1.0
 */
public interface CommonModelAccessor extends ModelAccessor {

	@Override
	default int getFixedBlockLength() {
		return getModelLength();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This implementation returns all the {@link CommonModelRegister} points.
	 * </p>
	 */
	@Override
	default Collection<? extends ModbusReference> getPointReferences() {
		return EnumSet.allOf(CommonModelRegister.class);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This implementation supports the {@link CommonModelRegister} points.
	 * </p>
	 */
	@Override
	default @Nullable Object getPointValue(ModbusReference point) {
		if ( !(point instanceof CommonModelRegister r) ) {
			return null;
		}
		return switch (r) {
			case Manufacturer -> getManufacturer();
			case Model -> getModelName();
			case Options -> getOptions();
			case Version -> getVersion();
			case SerialNumber -> getSerialNumber();
			case DeviceAddress -> getDeviceAddress();
		};
	}

	/**
	 * Get the device manufacturer.
	 *
	 * @return the manufacturer
	 */
	@Nullable
	String getManufacturer();

	/**
	 * Get the device model name.
	 *
	 * @return the device model
	 */
	@Nullable
	String getModelName();

	/**
	 * Get the device options.
	 *
	 * @return the options
	 */
	@Nullable
	String getOptions();

	/**
	 * Get the device version.
	 *
	 * @return the version
	 */
	@Nullable
	String getVersion();

	/**
	 * Get the serial number.
	 *
	 * @return the serial number
	 */
	@Nullable
	String getSerialNumber();

	/**
	 * Get the device ID (the Modbus unit ID).
	 *
	 * @return the device address
	 */
	@Nullable
	Integer getDeviceAddress();

}
