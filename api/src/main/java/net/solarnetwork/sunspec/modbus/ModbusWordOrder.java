/* ==================================================================
 * ModbusWordOrder.java - 5/05/2018 11:53:55 AM
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

/**
 * A word-ordering for multi-register data types in Modbus.
 * 
 * @author matt
 * @version 1.0
 * @since 2.7
 */
public enum ModbusWordOrder {

	/** Most to least word order (high 16 bits followed by low 16 bits). */
	MostToLeastSignificant('m'),

	/** Least to most word order (low 16 bits followed by high 16 bits). */
	LeastToMostSignificant('l');

	private final char key;

	private ModbusWordOrder(char key) {
		this.key = key;
	}

	/**
	 * Get the key value for this enum.
	 * 
	 * @return the key
	 */
	public char getKey() {
		return key;
	}

	/**
	 * Get an enum instance for a key value.
	 * 
	 * @param key
	 *        the key
	 * @return the enum
	 * @throws IllegalArgumentException
	 *         if {@code key} is not a valid value
	 */
	public static ModbusWordOrder forKey(char key) {
		for ( ModbusWordOrder e : ModbusWordOrder.values() ) {
			if ( key == e.key ) {
				return e;
			}
		}
		throw new IllegalArgumentException("Unknown ModbusWordOrder key [" + key + "]");
	}

	/**
	 * Get a friendly display string for this enum.
	 * 
	 * @return the display string
	 */
	public String toDisplayString() {
		return switch (this) {
			case MostToLeastSignificant -> "Most significant word first";
			case LeastToMostSignificant -> "Least significant word first";
			default -> this.toString();
		};
	}

}
