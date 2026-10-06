/* ==================================================================
 * DerVoltWattModelRegister.java - 5/10/2026 4:58:20 pm
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
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt32;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.PointAccess;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReadFunction;
import net.solarnetwork.sunspec.modbus.SunspecModbusReference;

/**
 * Enumeration of Modbus register mappings for the SunSpec DER volt-watt model.
 *
 * <p>
 * These mappings correspond to the SunSpec model number <b>706</b>, along with
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
public enum DerVoltWattModelRegister implements SunspecModbusReference {

	/** The curve voltage scale factor. */
	ScaleFactorVoltage(10, Int16, ScaleFactor),

	/** The curve active power scale factor. */
	ScaleFactorActivePower(11, Int16, ScaleFactor),

	/** The open loop response time scale factor. */
	ScaleFactorResponseTime(12, Int16, ScaleFactor),

	// curves

	/** The dependent reference, see {@link DerActivePowerReference}. */
	CurveDependentReference(1, UInt16, Enumeration, ReadWrite),

	/** The open loop response time, in seconds. */
	CurveOpenLoopResponseTime(2, UInt32, ReadWrite),

	/** The curve read-only setting. */
	CurveReadOnly(4, UInt16, Enumeration),

	// curve points

	/** The point voltage, as a percentage of nominal voltage. */
	PointVoltage(0, UInt16, ReadWrite),

	/** The point active power, as a percentage of the dependent reference. */
	PointActivePower(1, Int16, ReadWrite),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final @Nullable DataClassification classification;
	private final PointAccess access;

	private DerVoltWattModelRegister(int address, ModbusDataType dataType,
			DataClassification classification) {
		this(address, dataType, classification, PointAccess.ReadOnly);
	}

	private DerVoltWattModelRegister(int address, ModbusDataType dataType, PointAccess access) {
		this(address, dataType, null, access);
	}

	private DerVoltWattModelRegister(int address, ModbusDataType dataType,
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
