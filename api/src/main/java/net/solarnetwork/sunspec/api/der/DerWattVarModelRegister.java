/* ==================================================================
 * DerWattVarModelRegister.java - 5/10/2026 4:58:20 pm
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
import static net.solarnetwork.sunspec.api.DataClassification.ScaleFactor;
import static net.solarnetwork.sunspec.api.PointAccess.ReadWrite;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.PointAccess;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReadFunction;
import net.solarnetwork.sunspec.modbus.SunspecModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec DER watt-var model.
 *
 * <p>
 * These mappings correspond to the SunSpec model number <b>712</b>, along with
 * the shared {@link DerCurveModelRegister} mappings.
 * </p>
 *
 * <p>
 * Note that all register addresses are encoded as an offset from the block
 * address of the model block, except for the {@code Curve*} registers, which
 * are encoded as an offset from the start of each curve block, and the
 * {@code Point*} registers, which are encoded as an offset from the start of
 * each curve point.
 * </p>
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public enum DerWattVarModelRegister implements SunspecModbusReference {

	/** The curve active power scale factor. */
	ScaleFactorActivePower(10, Int16, ScaleFactor),

	/** The curve reactive power scale factor. */
	ScaleFactorReactivePower(11, Int16, ScaleFactor),

	// curves

	/** The dependent reference, see {@link DerReactivePowerReference}. */
	CurveDependentReference(1, UInt16, Enumeration, ReadWrite),

	/** The power priority, see {@link DerReactivePowerPriority}. */
	CurvePowerPriority(2, UInt16, Enumeration, ReadWrite),

	/** The curve read-only setting. */
	CurveReadOnly(3, UInt16, Enumeration),

	// curve points

	/** The point active power, as a percentage of maximum active power. */
	PointActivePower(0, Int16, ReadWrite),

	/** The point reactive power, as a percentage of the dependent reference. */
	PointReactivePower(1, Int16, ReadWrite),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final @Nullable DataClassification classification;
	private final PointAccess access;

	private DerWattVarModelRegister(int address, ModbusDataType dataType,
			DataClassification classification) {
		this(address, dataType, classification, PointAccess.ReadOnly);
	}

	private DerWattVarModelRegister(int address, ModbusDataType dataType, PointAccess access) {
		this(address, dataType, null, access);
	}

	private DerWattVarModelRegister(int address, ModbusDataType dataType,
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
	public ModbusReadFunction getFunction() {
		return ModbusReadFunction.ReadHoldingRegister;
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
