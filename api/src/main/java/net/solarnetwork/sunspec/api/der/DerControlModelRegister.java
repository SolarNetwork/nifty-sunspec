/* ==================================================================
 * DerControlModelRegister.java - 5/10/2026 10:31:52 am
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
 * Enumeration of Modbus register mappings for the SunSpec DER control model.
 *
 * <p>
 * These mappings correspond to the SunSpec model number <b>715</b>.
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
public enum DerControlModelRegister implements ModbusReference {

	/** Local or remote control mode, see {@link DerLocalRemoteControl}. */
	LocalRemoteControl(0, UInt16, Enumeration),

	/** DER heartbeat, incremented every second by the DER. */
	DerHeartbeat(1, UInt32),

	/** Controller heartbeat, incremented every second by the controller. */
	ControllerHeartbeat(3, UInt32, ReadWrite),

	/** Alarm reset, written as {@literal 1} to reset latched alarms. */
	AlarmReset(5, UInt16, ReadWrite),

	/** Operation command, see {@link DerOperationCommand}. */
	OperationCommand(6, UInt16, Enumeration, ReadWrite),

	;

	private final int address;
	private final ModbusDataType dataType;
	private final @Nullable DataClassification classification;
	private final PointAccess access;

	private DerControlModelRegister(int address, ModbusDataType dataType) {
		this(address, dataType, null, PointAccess.ReadOnly);
	}

	private DerControlModelRegister(int address, ModbusDataType dataType,
			DataClassification classification) {
		this(address, dataType, classification, PointAccess.ReadOnly);
	}

	private DerControlModelRegister(int address, ModbusDataType dataType, PointAccess access) {
		this(address, dataType, null, access);
	}

	private DerControlModelRegister(int address, ModbusDataType dataType,
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
