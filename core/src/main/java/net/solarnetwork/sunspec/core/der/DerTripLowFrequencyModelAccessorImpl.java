/* ==================================================================
 * DerTripLowFrequencyModelAccessorImpl.java - 5/10/2026 6:24:51 pm
 *
 * Copyright 2026 SolarNetwork.net Dev Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * ==================================================================
 */

package net.solarnetwork.sunspec.core.der;

import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerTripLowFrequencyModelAccessor;
import net.solarnetwork.sunspec.api.der.DerTripModelRegister;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link DerTripLowFrequencyModelAccessor}.
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public class DerTripLowFrequencyModelAccessorImpl extends BaseDerTripModelAccessor
		implements DerTripLowFrequencyModelAccessor {

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
	public DerTripLowFrequencyModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId, DerTripModelRegister.PointFrequency,
				DerTripModelRegister.ScaleFactorFrequency, DerTripModelRegister.PointFrequencyTime);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@link DerModelId} class will be used as the {@code ModelId}
	 * instance.
	 * </p>
	 *
	 * @param data
	 *        the overall data object
	 * @param baseAddress
	 *        the base address for this model's data
	 * @param modelId
	 *        the model ID
	 */
	public DerTripLowFrequencyModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, DerModelId.forId(modelId));
	}

}
