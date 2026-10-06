/* ==================================================================
 * InverterModelId.java - 5/10/2018 4:21:00 PM
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

package net.solarnetwork.sunspec.api.inverter;

import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelId;

/**
 * Enumeration of SunSpec inverter model IDs.
 * 
 * @author matt
 * @version 1.1
 */
public enum InverterModelId implements ModelId {

	/** Single phase inverter. */
	SinglePhaseInverterInteger(101, "Single phase inverter"),

	/** Split phase inverter. */
	SplitPhaseInverterInteger(102, "Split phase inverter"),

	/** 3-phase inverter. */
	ThreePhaseInverterInteger(103, "3-phase inverter"),

	/** Single phase inverter (floating point). */
	SinglePhaseInverterFloatingPoint(111, "Single phase inverter (floating point)"),

	/** Split phase inverter (floating point). */
	SplitPhaseInverterFloatingPoint(112, "Split phase inverter (floating point)"),

	/** 3-phase inverter (floating point). */
	ThreePhaseInverterFloatingPoint(113, "3-phase inverter (floating point)"),

	/** Multiple MPPT Inverter Extension Model. */
	MultipleMpptInverterExtension(
			160,
			"Multiple MPPT Inverter Extension Model",
			InverterMpptExtensionModelAccessor.class);

	private final int id;
	private final String description;
	private final Class<? extends ModelAccessor> accessorType;

	private InverterModelId(int id, String description) {
		this(id, description, InverterModelAccessor.class);
	}

	private InverterModelId(int id, String description, Class<? extends ModelAccessor> accessorType) {
		this.id = id;
		this.description = description;
		this.accessorType = accessorType;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Class<? extends ModelAccessor> getModelAccessorType() {
		return accessorType;
	}

	/**
	 * Get an enumeration for an ID value.
	 * 
	 * @param id
	 *        the ID to get the enum value for
	 * @return the enumeration value
	 * @throws IllegalArgumentException
	 *         if {@code id} is not supported
	 */
	public static InverterModelId forId(int id) {
		for ( InverterModelId e : InverterModelId.values() ) {
			if ( e.id == id ) {
				return e;
			}
		}
		throw new IllegalArgumentException("ID [" + id + "] not supported");
	}

}
