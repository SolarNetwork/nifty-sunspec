/* ==================================================================
 * MeterModelId.java - 21/05/2018 8:00:22 PM
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

package net.solarnetwork.sunspec.api.meter;

import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelId;

/**
 * Enumeration of SunSpec meter model IDs.
 * 
 * @author matt
 * @version 1.1
 */
public enum MeterModelId implements ModelId {

	/** Single phase (A-N or A-B) meter. */
	SinglePhaseMeterInteger(201, "Single phase (A-N or A-B) meter"),

	/** Split single phase (A-B-N) meter. */
	SplitSinglePhaseMeterInteger(202, "Split single phase (A-B-N) meter"),

	/** WYE connect 3-phase (ABCN) meter. */
	WyeConnectThreePhaseMeterInteger(203, "WYE connect 3-phase (ABCN) meter"),

	/** Delta connect 3-phase (ABC) meter. */
	DeltaConnectThreePhaseMeterInteger(204, "Delta connect 3-phase (ABC) meter"),

	/** Single phase (A-N or A-B) meter. */
	SinglePhaseMeterFloat(211, "Single phase (A-N or A-B) meter"),

	/** Split single phase (A-B-N) meter. */
	SplitSinglePhaseMeterFLoat(212, "Split single phase (A-B-N) meter"),

	/** WYE connect 3-phase (ABCN) meter. */
	WyeConnectThreePhaseMeterFloat(213, "WYE connect 3-phase (ABCN) meter"),

	/** Delta connect 3-phase (ABC) meter. */
	DeltaConnectThreePhaseMeterFloat(214, "Delta connect 3-phase (ABC) meter");

	private final int id;
	private final String description;
	private final Class<? extends ModelAccessor> accessorType;

	private MeterModelId(int id, String description) {
		this(id, description, MeterModelAccessor.class);
	}

	private MeterModelId(int id, String description, Class<? extends ModelAccessor> accessorType) {
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
	public static MeterModelId forId(int id) {
		for ( MeterModelId e : MeterModelId.values() ) {
			if ( e.id == id ) {
				return e;
			}
		}
		throw new IllegalArgumentException("ID [" + id + "] not supported");
	}

}
