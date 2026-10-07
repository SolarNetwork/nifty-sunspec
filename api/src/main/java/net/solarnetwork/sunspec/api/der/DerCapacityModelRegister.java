/* ==================================================================
 * DerCapacityModelRegister.java - 5/10/2026 9:32:29 am
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

import static net.solarnetwork.sunspec.api.DataClassification.Bitfield;
import static net.solarnetwork.sunspec.api.DataClassification.Enumeration;
import static net.solarnetwork.sunspec.api.DataClassification.ScaleFactor;
import static net.solarnetwork.sunspec.api.PointAccess.ReadWrite;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt32;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.PointAccess;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec DER capacity model.
 *
 * <p>
 * These mappings correspond to the SunSpec model number <b>702</b>. The
 * {@code PFOvrExtRtg}, {@code PFUndExtRtg}, {@code PFOvrExt}, and
 * {@code PFUndExt} points are not mapped, as SunSpec defines them as unused
 * duplicates of other points.
 * </p>
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public enum DerCapacityModelRegister implements ModbusReference {

	// Nameplate ratings

	/** Maximum active power rating at unity power factor, in W. */
	ActivePowerMaximumRating(0, UInt16),

	/** Active power rating at the over-excited power factor rating, in W. */
	ActivePowerOverExcitedRating(1, UInt16),

	/** Over-excited power factor rating. */
	OverExcitedPowerFactorRating(2, UInt16),

	/** Active power rating at the under-excited power factor rating, in W. */
	ActivePowerUnderExcitedRating(3, UInt16),

	/** Under-excited power factor rating. */
	UnderExcitedPowerFactorRating(4, UInt16),

	/** Maximum apparent power rating, in VA. */
	ApparentPowerMaximumRating(5, UInt16),

	/** Maximum injected reactive power rating, in VAR. */
	ReactivePowerInjectedMaximumRating(6, UInt16),

	/** Maximum absorbed reactive power rating, in VAR. */
	ReactivePowerAbsorbedMaximumRating(7, UInt16),

	/** Maximum active power charge rate rating, in W. */
	ActivePowerChargeRateMaximumRating(8, UInt16),

	/** Maximum active power discharge rate rating, in W. */
	ActivePowerDischargeRateMaximumRating(9, UInt16),

	/** Maximum apparent power charge rate rating, in VA. */
	ApparentPowerChargeRateMaximumRating(10, UInt16),

	/** Maximum apparent power discharge rate rating, in VA. */
	ApparentPowerDischargeRateMaximumRating(11, UInt16),

	/** Nominal AC voltage rating, in V. */
	VoltageNominalRating(12, UInt16),

	/** Maximum AC voltage rating, in V. */
	VoltageMaximumRating(13, UInt16),

	/** Minimum AC voltage rating, in V. */
	VoltageMinimumRating(14, UInt16),

	/** Maximum AC current rating, in A. */
	CurrentMaximumRating(15, UInt16),

	/**
	 * Reactive susceptance that remains connected to the area electric power
	 * system in the cease to energize and trip state, in S.
	 */
	ReactiveSusceptanceRating(18, UInt16),

	/**
	 * Normal operating performance category, see
	 * {@link DerNormalOperatingCategory}.
	 */
	NormalOperatingCategoryRating(19, UInt16, Enumeration),

	/**
	 * Abnormal operating performance category, see
	 * {@link DerAbnormalOperatingCategory}.
	 */
	AbnormalOperatingCategoryRating(20, UInt16, Enumeration),

	/** Supported control modes bitmask, see {@link DerControlMode}. */
	ControlModesBitmask(21, UInt32, Bitfield),

	/**
	 * Intentional island categories rating bitmask, see
	 * {@link DerIntentionalIslandCategory}.
	 */
	IntentionalIslandCategoriesRatingBitmask(23, UInt16, Bitfield),

	// Settings

	/** Maximum active power setting, in W. */
	ActivePowerMaximum(24, UInt16, ReadWrite),

	/** Active power setting at the over-excited power factor, in W. */
	ActivePowerOverExcited(25, UInt16, ReadWrite),

	/** Over-excited power factor setting. */
	OverExcitedPowerFactor(26, UInt16, ReadWrite),

	/** Active power setting at the under-excited power factor, in W. */
	ActivePowerUnderExcited(27, UInt16, ReadWrite),

	/** Under-excited power factor setting. */
	UnderExcitedPowerFactor(28, UInt16, ReadWrite),

	/** Maximum apparent power setting, in VA. */
	ApparentPowerMaximum(29, UInt16, ReadWrite),

	/** Maximum injected reactive power setting, in VAR. */
	ReactivePowerInjectedMaximum(30, UInt16, ReadWrite),

	/** Maximum absorbed reactive power setting, in VAR. */
	ReactivePowerAbsorbedMaximum(31, UInt16, ReadWrite),

	/** Maximum active power charge rate setting, in W. */
	ActivePowerChargeRateMaximum(32, UInt16, ReadWrite),

	/** Maximum active power discharge rate setting, in W. */
	ActivePowerDischargeRateMaximum(33, UInt16, ReadWrite),

	/** Maximum apparent power charge rate setting, in VA. */
	ApparentPowerChargeRateMaximum(34, UInt16, ReadWrite),

	/** Maximum apparent power discharge rate setting, in VA. */
	ApparentPowerDischargeRateMaximum(35, UInt16, ReadWrite),

	/** Nominal AC voltage setting, in V. */
	VoltageNominal(36, UInt16, ReadWrite),

	/** Maximum AC voltage setting, in V. */
	VoltageMaximum(37, UInt16, ReadWrite),

	/** Minimum AC voltage setting, in V. */
	VoltageMinimum(38, UInt16, ReadWrite),

	/** Maximum AC current setting, in A. */
	CurrentMaximum(39, UInt16, ReadWrite),

	/**
	 * Intentional island categories bitmask, see
	 * {@link DerIntentionalIslandCategory}.
	 */
	IntentionalIslandCategoriesBitmask(42, UInt16, Bitfield, ReadWrite),

	// Scale factors

	/** Active power scale factor, as *10^X. */
	ScaleFactorActivePower(43, Int16, ScaleFactor),

	/** Power factor scale factor, as *10^X. */
	ScaleFactorPowerFactor(44, Int16, ScaleFactor),

	/** Apparent power scale factor, as *10^X. */
	ScaleFactorApparentPower(45, Int16, ScaleFactor),

	/** Reactive power scale factor, as *10^X. */
	ScaleFactorReactivePower(46, Int16, ScaleFactor),

	/** Voltage scale factor, as *10^X. */
	ScaleFactorVoltage(47, Int16, ScaleFactor),

	/** Current scale factor, as *10^X. */
	ScaleFactorCurrent(48, Int16, ScaleFactor),

	/** Susceptance scale factor, as *10^X. */
	ScaleFactorSusceptance(49, Int16, ScaleFactor),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final @Nullable DataClassification classification;
	private final PointAccess access;

	private DerCapacityModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, null, PointAccess.ReadOnly);
	}

	private DerCapacityModelRegister(int address, ModbusDataType dataType,
			DataClassification classification) {
		this(address, dataType, classification, PointAccess.ReadOnly);
	}

	private DerCapacityModelRegister(int address, ModbusDataType dataType, PointAccess access) {
		this(address, dataType, null, access);
	}

	private DerCapacityModelRegister(int address, ModbusDataType dataType,
			@Nullable DataClassification classification, PointAccess access) {
		this.address = address;
		this.dataType = dataType;
		this.classification = classification;
		this.access = access;
	}

	@Override
	public int getAddress() {
		return address;
	}

	@Override
	public ModbusDataType getDataType() {
		return dataType;
	}

	@Override
	public int getWordLength() {
		return dataType.getWordLength();
	}

	@Override
	public @Nullable DataClassification getClassification() {
		return classification;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This is the enumeration constant name, without the {@code Bitmask} suffix
	 * of bitfield points.
	 * </p>
	 */
	@Override
	public String getName() {
		return switch (this) {
			case ControlModesBitmask -> "ControlModes";
			case IntentionalIslandCategoriesRatingBitmask -> "IntentionalIslandCategoriesRating";
			case IntentionalIslandCategoriesBitmask -> "IntentionalIslandCategories";
			default -> name();
		};
	}

	@Override
	public PointAccess getAccess() {
		return access;
	}

}
