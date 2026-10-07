/* ==================================================================
 * ModbusReference.java - 15/05/2018 11:04:04 AM
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
import net.solarnetwork.sunspec.api.PointGroup;
import net.solarnetwork.sunspec.api.PointMapMode;

/**
 * A reference to a Modbus register (or registers).
 *
 * @author matt
 * @version 1.0
 */
public interface ModbusReference {

	/**
	 * Get the register address.
	 *
	 * @return the address
	 */
	int getAddress();

	/**
	 * Get the data type.
	 *
	 * @return the data type
	 */
	ModbusDataType getDataType();

	/**
	 * Get the number of Modbus words to include.
	 *
	 * @return the word length
	 */
	int getWordLength();

	/**
	 * Return the classification of this modbus reference.
	 *
	 * @return the classification, or {@code null} if none
	 */
	default @Nullable DataClassification getClassification() {
		return null;
	}

	/**
	 * Get the name of the point this reference is associated with.
	 *
	 * <p>
	 * The name is unique within the points of a model, and is used to derive
	 * the keys of {@link PointGroup#toPointMap(PointMapMode)}. Names are upper
	 * camel case, and are usually the register enumeration constant name.
	 * Points of a single AC phase end with the phase, such as
	 * {@code CurrentPhaseA} or {@code VoltagePhaseANeutral}, and points between
	 * two phases end with both, such as {@code VoltagePhaseAPhaseB}. Points for
	 * all phases, such as totals and averages, have no suffix, such as
	 * {@code Current} or {@code LineVoltage}.
	 * </p>
	 *
	 * @return the point name
	 */
	String getName();

	/**
	 * Get the access level of this modbus reference.
	 *
	 * @return the access level, never {@code null}; this implementation returns
	 *         {@link PointAccess#ReadOnly}
	 */
	default PointAccess getAccess() {
		return PointAccess.ReadOnly;
	}

}
