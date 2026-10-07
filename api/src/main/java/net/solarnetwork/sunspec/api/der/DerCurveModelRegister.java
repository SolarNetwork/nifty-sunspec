/* ==================================================================
 * DerCurveModelRegister.java - 5/10/2026 4:58:20 pm
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

import static net.solarnetwork.sunspec.api.DataClassification.Enumeration;
import static net.solarnetwork.sunspec.api.PointAccess.ReadWrite;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt32;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.PointAccess;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Enumeration of the Modbus register mappings shared by the SunSpec DER curve
 * models.
 *
 * <p>
 * These mappings are common to the SunSpec model numbers <b>705</b>,
 * <b>706</b>, and <b>712</b>. The registers specific to each model are defined
 * in {@link DerVoltVarModelRegister}, {@link DerVoltWattModelRegister}, and
 * {@link DerWattVarModelRegister}.
 * </p>
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block, except for the {@code Curve*} registers, which
 * are encoded as an offset from the start of each curve block.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public enum DerCurveModelRegister implements ModbusReference {

	/** The function enable setting. */
	Enabled(0, UInt16, Enumeration, ReadWrite),

	/** The index of the curve to adopt as the active curve. */
	AdoptCurveRequest(1, UInt16, ReadWrite),

	/** The result of the last adopt curve request. */
	AdoptCurveResult(2, UInt16, Enumeration),

	/** The number of points in each curve. */
	NumberOfPoints(3, UInt16),

	/** The number of curves. */
	NumberOfCurves(4, UInt16),

	/** The reversion timeout, in seconds. */
	ReversionTime(5, UInt32, ReadWrite),

	/** The reversion time remaining, in seconds. */
	ReversionTimeRemaining(7, UInt32),

	/** The index of the curve to adopt when the reversion timeout expires. */
	ReversionCurve(9, UInt16, ReadWrite),

	// curves

	/** The number of active points in a curve. */
	CurveActivePointCount(0, UInt16, ReadWrite),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final @Nullable DataClassification classification;
	private final PointAccess access;

	private DerCurveModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, null, PointAccess.ReadOnly);
	}

	private DerCurveModelRegister(int address, ModbusDataType dataType,
			DataClassification classification) {
		this(address, dataType, classification, PointAccess.ReadOnly);
	}

	private DerCurveModelRegister(int address, ModbusDataType dataType, PointAccess access) {
		this(address, dataType, null, access);
	}

	private DerCurveModelRegister(int address, ModbusDataType dataType,
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
	public PointAccess getAccess() {
		return access;
	}

}
