/* ==================================================================
 * InverterNameplateRatingsModelAccessorImpl.java - 15/10/2018 9:23:28 AM
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

package net.solarnetwork.sunspec.core.inverter;

import java.util.Collection;
import java.util.EnumSet;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.inverter.DistributedEnergyResourceType;
import net.solarnetwork.sunspec.api.inverter.InverterControlModelId;
import net.solarnetwork.sunspec.api.inverter.InverterDerType;
import net.solarnetwork.sunspec.api.inverter.InverterNameplateRatingsModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterNameplateRatingsRegister;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Data access object for an inverter nameplate ratings model.
 *
 * @author matt
 * @version 1.0
 */
public class InverterNameplateRatingsModelAccessorImpl extends BaseModelAccessor
		implements InverterNameplateRatingsModelAccessor {

	/** The inverter nameplate ratings model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 26;

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
	public InverterNameplateRatingsModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@link InverterControlModelId} class will be used as the
	 * {@code ModelId} instance.
	 * </p>
	 *
	 * @param data
	 *        the overall data object
	 * @param baseAddress
	 *        the base address for this model's data
	 * @param modelId
	 *        the model ID
	 */
	public InverterNameplateRatingsModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, InverterControlModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(InverterNameplateRatingsRegister.class);
	}

	@Override
	public @Nullable DistributedEnergyResourceType getDerType() {
		return getCodedValue(InverterNameplateRatingsRegister.DerType, InverterDerType.class);
	}

	@Override
	public @Nullable Integer getActivePowerRating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.ActivePowerRating,
				InverterNameplateRatingsRegister.ScaleFactorActivePowerRating);
		return (n != null ? n.intValue() : null);
	}

	@Override
	public @Nullable Integer getApparentPowerRating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.ApparentPowerRating,
				InverterNameplateRatingsRegister.ScaleFactorApparentPowerRating);
		return (n != null ? n.intValue() : null);
	}

	@Override
	public @Nullable Integer getReactivePowerQ1Rating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.ReactivePowerQ1Rating,
				InverterNameplateRatingsRegister.ScaleFactorReactivePowerRating);
		return (n != null ? n.intValue() : null);
	}

	@Override
	public @Nullable Integer getReactivePowerQ2Rating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.ReactivePowerQ2Rating,
				InverterNameplateRatingsRegister.ScaleFactorReactivePowerRating);
		return (n != null ? n.intValue() : null);
	}

	@Override
	public @Nullable Integer getReactivePowerQ3Rating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.ReactivePowerQ3Rating,
				InverterNameplateRatingsRegister.ScaleFactorReactivePowerRating);
		return (n != null ? n.intValue() : null);
	}

	@Override
	public @Nullable Integer getReactivePowerQ4Rating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.ReactivePowerQ4Rating,
				InverterNameplateRatingsRegister.ScaleFactorReactivePowerRating);
		return (n != null ? n.intValue() : null);
	}

	@Override
	public @Nullable Float getCurrentRating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.CurrentRating,
				InverterNameplateRatingsRegister.ScaleFactorCurrentRating);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public @Nullable Float getPowerFactorQ1Rating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.PowerFactorQ1Rating,
				InverterNameplateRatingsRegister.ScaleFactorPowerFactorRating);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public @Nullable Float getPowerFactorQ2Rating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.PowerFactorQ2Rating,
				InverterNameplateRatingsRegister.ScaleFactorPowerFactorRating);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public @Nullable Float getPowerFactorQ3Rating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.PowerFactorQ3Rating,
				InverterNameplateRatingsRegister.ScaleFactorPowerFactorRating);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public @Nullable Float getPowerFactorQ4Rating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.PowerFactorQ4Rating,
				InverterNameplateRatingsRegister.ScaleFactorPowerFactorRating);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public @Nullable Integer getStoredEnergyRating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.StoredEnergyRating,
				InverterNameplateRatingsRegister.ScaleFactorStoredEnergyRating);
		return (n != null ? n.intValue() : null);
	}

	@Override
	public @Nullable Integer getStoredChargeCapacity() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.StoredChargeCapacity,
				InverterNameplateRatingsRegister.ScaleFactorStoredChargeCapacity);
		return (n != null ? n.intValue() : null);
	}

	@Override
	public @Nullable Integer getStoredEnergyImportPowerRating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.StoredEnergyImportPowerRating,
				InverterNameplateRatingsRegister.ScaleFactorStoredEnergyImportPowerRating);
		return (n != null ? n.intValue() : null);
	}

	@Override
	public @Nullable Integer getStoredEnergyExportPowerRating() {
		Number n = getScaledValue(InverterNameplateRatingsRegister.StoredEnergyExportPowerRating,
				InverterNameplateRatingsRegister.ScaleFactorStoredEnergyExportPowerRating);
		return (n != null ? n.intValue() : null);
	}

}
