/* ==================================================================
 * InverterPricingSignalModelRegister.java - 6/10/2026 9:18:02 am
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

package net.solarnetwork.sunspec.api.inverter;

import static net.solarnetwork.sunspec.api.DataClassification.Bitfield;
import static net.solarnetwork.sunspec.api.DataClassification.Enumeration;
import static net.solarnetwork.sunspec.api.DataClassification.ScaleFactor;
import static net.solarnetwork.sunspec.api.PointAccess.ReadWrite;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.PointAccess;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec pricing signal model.
 *
 * <p>
 * These mappings correspond to the SunSpec model number <b>125</b>.
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
public enum InverterPricingSignalModelRegister implements ModbusReference {

	/**
	 * Price-based charge and discharge mode enable setting, with bit 0 set when
	 * enabled.
	 */
	PricingEnabled(0, UInt16, Bitfield, ReadWrite),

	/** Pricing signal type, see {@link InverterPricingSignalType}. */
	PricingSignalType(1, UInt16, Enumeration, ReadWrite),

	/** Pricing signal, whose meaning depends on the pricing signal type. */
	PricingSignal(2, Int16, ReadWrite),

	/** Time window for pricing changes, in seconds. */
	PricingTimeWindow(3, UInt16, ReadWrite),

	/** Pricing reversion timeout, in seconds. */
	PricingReversionTime(4, UInt16, ReadWrite),

	/** Pricing ramp time, in seconds. */
	PricingRampTime(5, UInt16, ReadWrite),

	/** Pricing signal scale factor, as *10^X. */
	ScaleFactorPricingSignal(6, Int16, ScaleFactor),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final @Nullable DataClassification classification;
	private final PointAccess access;

	private InverterPricingSignalModelRegister(int address, ModbusDataType dataType,
			DataClassification classification) {
		this(address, dataType, classification, PointAccess.ReadOnly);
	}

	private InverterPricingSignalModelRegister(int address, ModbusDataType dataType,
			PointAccess access) {
		this(address, dataType, null, access);
	}

	private InverterPricingSignalModelRegister(int address, ModbusDataType dataType,
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

	@Override
	public String getName() {
		return name();
	}

	@Override
	public PointAccess getAccess() {
		return access;
	}

}
