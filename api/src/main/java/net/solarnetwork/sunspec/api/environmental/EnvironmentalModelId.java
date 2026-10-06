/* ==================================================================
 * EnvironmentalModelId.java - 5/07/2023 8:24:25 am
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

package net.solarnetwork.sunspec.api.environmental;

import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelId;

/**
 * Enumeration of SunSpec environmental model IDs.
 * 
 * @author matt
 * @version 1.0
 * @since 4.2
 */
public enum EnvironmentalModelId implements ModelId {

	/** Irradiance. */
	Irradiance(302, "Irradiance", IrradianceModelAccessor.class),

	/** Back of module temperature. */
	BackOfModuleTemperature(303, "Back of module temperature", BomTemperatureModelAccessor.class),

	/** Inclinometer. */
	Inclinometer(304, "Inclinometer", InclinometerModelAccessor.class),

	/** Global positioning system. */
	GPS(305, "GPS", GpsModelAccessor.class),

	/** Reference point. */
	ReferencePoint(306, "Reference Point", ReferencePointModelAccessor.class),

	/** Base meteorolgical. */
	BaseMeteorolgical(307, "Base meteorolgical", MeteorologicalModelAccessor.class),

	/** Mini meteorolgical. */
	MiniMeteorolgical(308, "Mini meteorolgical", MiniMeteorologicalModelAccessor.class),

	;

	private final int id;
	private final String description;
	private final Class<? extends ModelAccessor> accessorType;

	private EnvironmentalModelId(int id, String description,
			Class<? extends ModelAccessor> accessorType) {
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
	public static EnvironmentalModelId forId(int id) {
		for ( EnvironmentalModelId e : EnvironmentalModelId.values() ) {
			if ( e.id == id ) {
				return e;
			}
		}
		throw new IllegalArgumentException("ID [" + id + "] not supported");
	}

}
