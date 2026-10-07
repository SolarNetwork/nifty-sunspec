/* ==================================================================
 * DerModelId.java - 5/10/2026 8:27:22 am
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

package net.solarnetwork.sunspec.api.der;

import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelId;

/**
 * Enumeration of SunSpec distributed energy resource (DER) model IDs.
 *
 * @author matt
 * @version 1.0
 */
public enum DerModelId implements ModelId {

	/** DER AC measurement. */
	AcMeasurement(701, "DER AC measurement", DerAcMeasurementModelAccessor.class),

	/** DER capacity. */
	Capacity(702, "DER capacity", DerCapacityModelAccessor.class),

	/** DER enter service. */
	EnterService(703, "DER enter service", DerEnterServiceModelAccessor.class),

	/** DER AC controls. */
	AcControls(704, "DER AC controls", DerAcControlsModelAccessor.class),

	/** DER volt-var. */
	VoltVar(705, "DER volt-var", DerVoltVarModelAccessor.class),

	/** DER volt-watt. */
	VoltWatt(706, "DER volt-watt", DerVoltWattModelAccessor.class),

	/** DER trip low voltage. */
	TripLowVoltage(707, "DER trip low voltage", DerTripLowVoltageModelAccessor.class),

	/** DER trip high voltage. */
	TripHighVoltage(708, "DER trip high voltage", DerTripHighVoltageModelAccessor.class),

	/** DER trip low frequency. */
	TripLowFrequency(709, "DER trip low frequency", DerTripLowFrequencyModelAccessor.class),

	/** DER trip high frequency. */
	TripHighFrequency(710, "DER trip high frequency", DerTripHighFrequencyModelAccessor.class),

	/** DER frequency droop. */
	FrequencyDroop(711, "DER frequency droop", DerFrequencyDroopModelAccessor.class),

	/** DER watt-var. */
	WattVar(712, "DER watt-var", DerWattVarModelAccessor.class),

	/** DER storage capacity. */
	StorageCapacity(713, "DER storage capacity", DerStorageCapacityModelAccessor.class),

	/** DER DC measurement. */
	DcMeasurement(714, "DER DC measurement", DerDcMeasurementModelAccessor.class),

	/** DER control. */
	Control(715, "DER control", DerControlModelAccessor.class),

	;

	private final int id;
	private final String description;
	private final Class<? extends ModelAccessor> accessorType;

	private DerModelId(int id, String description, Class<? extends ModelAccessor> accessorType) {
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
	public static DerModelId forId(int id) {
		for ( DerModelId e : DerModelId.values() ) {
			if ( e.id == id ) {
				return e;
			}
		}
		throw new IllegalArgumentException("ID [" + id + "] not supported");
	}

}
