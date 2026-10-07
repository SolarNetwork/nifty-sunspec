/* ==================================================================
 * InverterNameplateRatingsModelAccessor.java - 15/10/2018 9:32:08 AM
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;

/**
 * API for accessing inverter nameplate ratings model data.
 *
 * @author matt
 * @version 1.0
 */
public interface InverterNameplateRatingsModelAccessor extends ModelAccessor {

	/**
	 * Key for the {@link DistributedEnergyResourceType} name, as a String.
	 *
	 */
	String INFO_KEY_DER_TYPE = "derType";

	/**
	 * Key for the {@link DistributedEnergyResourceType} code, as an Integer.
	 *
	 */
	String INFO_KEY_DER_TYPE_CODE = "derTypeCode";

	/**
	 * Key for the active power rating in W, as a BigDecimal.
	 *
	 */
	String INFO_KEY_ACTIVE_POWER_RATING = "activePowerRating";

	/**
	 * Key for the apparent power rating in VA, as a BigDecimal.
	 *
	 */
	String INFO_KEY_APPARENT_POWER_RATING = "apparentPowerRating";

	/**
	 * Key for the reactive power Q1 rating in VAR, as a BigDecimal.
	 *
	 */
	String INFO_KEY_REACTIVE_POWER_Q1_RATING = "reactivePowerQ1Rating";

	/**
	 * Key for the reactive power Q2 rating in VAR, as a BigDecimal.
	 *
	 */
	String INFO_KEY_REACTIVE_POWER_Q2_RATING = "reactivePowerQ2Rating";

	/**
	 * Key for the reactive power Q3 rating in VAR, as a BigDecimal.
	 *
	 */
	String INFO_KEY_REACTIVE_POWER_Q3_RATING = "reactivePowerQ3Rating";

	/**
	 * Key for the reactive power Q4 rating in VAR, as a BigDecimal.
	 *
	 */
	String INFO_KEY_REACTIVE_POWER_Q4_RATING = "reactivePowerQ4Rating";

	/**
	 * Key for the current rating in A, as a Float.
	 *
	 */
	String INFO_KEY_CURRENT_RATING = "currentRating";

	/**
	 * Key for the power factor Q1 rating, as a Float.
	 *
	 */
	String INFO_KEY_POWER_FACTOR_Q1_RATING = "powerFactorQ1Rating";

	/**
	 * Key for the power factor Q2 rating, as a Float.
	 *
	 */
	String INFO_KEY_POWER_FACTOR_Q2_RATING = "powerFactorQ2Rating";

	/**
	 * Key for the power factor Q3 rating, as a Float.
	 *
	 */
	String INFO_KEY_POWER_FACTOR_Q3_RATING = "powerFactorQ3Rating";

	/**
	 * Key for the power factor Q4 rating, as a Float.
	 *
	 */
	String INFO_KEY_POWER_FACTOR_Q4_RATING = "powerFactorQ4Rating";

	/**
	 * Key for the stored energy rating in Wh, as a BigDecimal.
	 *
	 */
	String INFO_KEY_STORED_ENERGY_RATING = "storedEnergyRating";

	/**
	 * Key for the stored charge capacity rating in Ah, as a BigDecimal.
	 *
	 */
	String INFO_KEY_STORED_CHARGE_CAPACITY = "storedChargeCapacity";

	/**
	 * Key for the stored energy import power rating in W, as a BigDecimal.
	 *
	 */
	String INFO_KEY_STORED_ENERGY_IMPORT_POWER_RATING = "storedEnergyImportPowerRating";

	/**
	 * Key for the stored energy export power rating in W, as a BigDecimal.
	 *
	 */
	String INFO_KEY_STORED_ENERGY_EXPORT_POWER_RATING = "storedEnergyExportPowerRating";

	/**
	 * Get the DER type.
	 *
	 * @return the type
	 */
	@Nullable
	DistributedEnergyResourceType getDerType();

	/**
	 * Get the continuous active power capability, in W.
	 *
	 * @return the active power rating
	 */
	@Nullable
	BigDecimal getActivePowerRating();

	/**
	 * Get the continuous apparent power capability, in VA.
	 *
	 * @return the apparent power rating
	 */
	@Nullable
	BigDecimal getApparentPowerRating();

	/**
	 * Get the continuous reactive power capability for EEI quadrant 1 (lagging,
	 * inductive), in VAR.
	 *
	 * @return the reactive power rating
	 */
	@Nullable
	BigDecimal getReactivePowerQ1Rating();

	/**
	 * Get the continuous reactive power capability for EEI quadrant 2 (leading,
	 * capacitive), in VAR.
	 *
	 * @return the reactive power rating
	 */
	@Nullable
	BigDecimal getReactivePowerQ2Rating();

	/**
	 * Get the continuous reactive power capability for EEI quadrant 3 (lagging,
	 * inductive), in VAR.
	 *
	 * @return the reactive power rating
	 */
	@Nullable
	BigDecimal getReactivePowerQ3Rating();

	/**
	 * Get the continuous reactive power capability for EEI quadrant 4 (leading,
	 * capacitive), in VAR.
	 *
	 * @return the reactive power rating
	 */
	@Nullable
	BigDecimal getReactivePowerQ4Rating();

	/**
	 * Get the maximum RMS AC current capability of the inverter, in A.
	 *
	 * @return the current rating
	 */
	@Nullable
	Float getCurrentRating();

	/**
	 * Get the power factor rating for EEI quadrant 1 (lagging, inductive), as a
	 * decimal from -1.0 to 1.0.
	 *
	 * @return the power factor
	 */
	@Nullable
	Float getPowerFactorQ1Rating();

	/**
	 * Get the power factor rating for EEI quadrant 2 (leading, capacitive), as
	 * a decimal from -1.0 to 1.0.
	 *
	 * @return the power factor
	 */
	@Nullable
	Float getPowerFactorQ2Rating();

	/**
	 * Get the power factor rating for EEI quadrant 3 (lagging, inductive), as a
	 * decimal from -1.0 to 1.0.
	 *
	 * @return the power factor
	 */
	@Nullable
	Float getPowerFactorQ3Rating();

	/**
	 * Get the power factor rating for EEI quadrant 4 (leading, capacitive), as
	 * a decimal from -1.0 to 1.0.
	 *
	 * @return the power factor
	 */
	@Nullable
	Float getPowerFactorQ4Rating();

	/**
	 * Get the maximum rated stored energy of the battery storage system, in Wh.
	 *
	 * @return the maximum rated energy of the battery storage
	 */
	@Nullable
	BigDecimal getStoredEnergyRating();

	/**
	 * Get the maximum rated stored charge of the battery storage system, in Ah.
	 *
	 * @return the maximum rated charge of the battery storage
	 */
	@Nullable
	BigDecimal getStoredChargeCapacity();

	/**
	 * Get the maximum rate of charge for the battery storage system, in W.
	 *
	 * @return the maximum charge rate power
	 */
	@Nullable
	BigDecimal getStoredEnergyImportPowerRating();

	/**
	 * Get the maximum rate of discharge for the battery storage system, in W.
	 *
	 * @return the minimum discharge rate power
	 */
	@Nullable
	BigDecimal getStoredEnergyExportPowerRating();

	/**
	 * Get an information mapping if the nameplate ratings.
	 *
	 * @return the information mapping
	 */
	default Map<String, Object> nameplateRatingsInfo() {
		Map<String, Object> result = new LinkedHashMap<>(17);

		DistributedEnergyResourceType derType = getDerType();
		if ( derType != null ) {
			result.put(INFO_KEY_DER_TYPE, derType.toString());
			result.put(INFO_KEY_DER_TYPE_CODE, derType.getCode());
		}

		putInfo(result, INFO_KEY_ACTIVE_POWER_RATING, getActivePowerRating());
		putInfo(result, INFO_KEY_APPARENT_POWER_RATING, getApparentPowerRating());
		putInfo(result, INFO_KEY_REACTIVE_POWER_Q1_RATING, getReactivePowerQ1Rating());
		putInfo(result, INFO_KEY_REACTIVE_POWER_Q2_RATING, getReactivePowerQ2Rating());
		putInfo(result, INFO_KEY_REACTIVE_POWER_Q3_RATING, getReactivePowerQ3Rating());
		putInfo(result, INFO_KEY_REACTIVE_POWER_Q4_RATING, getReactivePowerQ4Rating());
		putInfo(result, INFO_KEY_CURRENT_RATING, roundDown(getCurrentRating(), 1));
		putInfo(result, INFO_KEY_POWER_FACTOR_Q1_RATING, roundDown(getPowerFactorQ1Rating(), 3));
		putInfo(result, INFO_KEY_POWER_FACTOR_Q2_RATING, roundDown(getPowerFactorQ2Rating(), 3));
		putInfo(result, INFO_KEY_POWER_FACTOR_Q3_RATING, roundDown(getPowerFactorQ3Rating(), 3));
		putInfo(result, INFO_KEY_POWER_FACTOR_Q4_RATING, roundDown(getPowerFactorQ4Rating(), 3));
		putInfo(result, INFO_KEY_STORED_ENERGY_RATING, getStoredEnergyRating());
		putInfo(result, INFO_KEY_STORED_CHARGE_CAPACITY, getStoredChargeCapacity());
		putInfo(result, INFO_KEY_STORED_ENERGY_IMPORT_POWER_RATING, getStoredEnergyImportPowerRating());
		putInfo(result, INFO_KEY_STORED_ENERGY_EXPORT_POWER_RATING, getStoredEnergyExportPowerRating());

		return result;
	}

	/**
	 * Add a value to an information mapping, unless the value is {@code null}.
	 *
	 * @param info
	 *        the mapping to add to
	 * @param key
	 *        the key to add
	 * @param value
	 *        the value to add
	 */
	private static void putInfo(Map<String, Object> info, String key, @Nullable Object value) {
		if ( value != null ) {
			info.put(key, value);
		}
	}

	/**
	 * Round a value down to a maximum number of decimal digits.
	 *
	 * @param value
	 *        the value to round
	 * @param scale
	 *        the maximum number of decimal digits
	 * @return the rounded value, as an {@code Integer} if it is a whole number
	 *         or a {@code Float} otherwise, or {@code null} if {@code value} is
	 *         {@code null}
	 */
	private static @Nullable Number roundDown(@Nullable Float value, int scale) {
		if ( value == null ) {
			return null;
		}
		BigDecimal d = new BigDecimal(value.toString());
		if ( d.scale() > scale ) {
			d = d.setScale(scale, RoundingMode.DOWN);
		}
		try {
			return d.intValueExact();
		} catch ( ArithmeticException e ) {
			// not a whole number
			return d.floatValue();
		}
	}
}
