/* ==================================================================
 * SunspecModbusReference.java - 8/10/2018 12:13:04 PM
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

package net.solarnetwork.sunspec.modbus;

import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.PointAccess;

/**
 * Extension of {@link ModbusReference} to add additional SunSpec data type
 * support.
 *
 * @author matt
 * @version 1.1
 */
public interface SunspecModbusReference extends ModbusReference {

	/**
	 * Return the classification of this modbus reference.
	 *
	 * @return the classification, or {@code null} if none
	 */
	default @Nullable DataClassification getClassification() {
		return null;
	}

	/**
	 * Get the access level of this modbus reference.
	 *
	 * @return the access level, never {@code null}; this implementation returns
	 *         {@link PointAccess#ReadOnly}
	 * @since 1.1
	 */
	default PointAccess getAccess() {
		return PointAccess.ReadOnly;
	}

}
