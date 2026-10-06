/* ==================================================================
 * StorageModelId.java - 5/10/2026 7:58:02 pm
 *
 * Copyright 2026 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.api.storage;

import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelId;

/**
 * SunSpec energy storage model IDs (800 series).
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum StorageModelId implements ModelId {

	/** Energy storage base, deprecated by SunSpec. */
	EnergyStorageBase(801, "Energy storage base (deprecated)"),

	/** Battery base. */
	BatteryBase(802, "Battery base", BatteryBaseModelAccessor.class),

	/** Lithium-ion battery bank. */
	LithiumIonBank(803, "Lithium-ion battery bank", LithiumIonBankModelAccessor.class),

	/** Lithium-ion string. */
	LithiumIonString(804, "Lithium-ion string", LithiumIonStringModelAccessor.class),

	/** Lithium-ion module. */
	LithiumIonModule(805, "Lithium-ion module", LithiumIonModuleModelAccessor.class),

	/** Flow battery. */
	FlowBattery(806, "Flow battery"),

	/** Flow battery string. */
	FlowBatteryString(807, "Flow battery string"),

	/** Flow battery module. */
	FlowBatteryModule(808, "Flow battery module"),

	/** Flow battery stack. */
	FlowBatteryStack(809, "Flow battery stack"),

	;

	private final int id;
	private final String description;
	private final Class<? extends ModelAccessor> accessorType;

	private StorageModelId(int id, String description) {
		this(id, description, ModelAccessor.class);
	}

	private StorageModelId(int id, String description, Class<? extends ModelAccessor> accessorType) {
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
	public static StorageModelId forId(int id) {
		for ( StorageModelId e : StorageModelId.values() ) {
			if ( e.id == id ) {
				return e;
			}
		}
		throw new IllegalArgumentException("ID [" + id + "] not supported");
	}

}
