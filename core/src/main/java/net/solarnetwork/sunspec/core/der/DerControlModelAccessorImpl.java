/* ==================================================================
 * DerControlModelAccessorImpl.java - 5/10/2026 10:31:52 am
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

package net.solarnetwork.sunspec.core.der;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.der.DerControlModelAccessor;
import net.solarnetwork.sunspec.api.der.DerControlModelRegister;
import net.solarnetwork.sunspec.api.der.DerLocalRemoteControl;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerOperationCommand;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link DerControlModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class DerControlModelAccessorImpl extends BaseModelAccessor implements DerControlModelAccessor {

	/** The DER control model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 7;

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
	public DerControlModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
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
	public DerControlModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, DerModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(DerControlModelRegister.class);
	}

	@Override
	public @Nullable DerLocalRemoteControl getLocalRemoteControl() {
		return getCodedValue(DerControlModelRegister.LocalRemoteControl, DerLocalRemoteControl.class);
	}

	@Override
	public @Nullable Long getDerHeartbeat() {
		return getLongValue(DerControlModelRegister.DerHeartbeat);
	}

	@Override
	public @Nullable Long getControllerHeartbeat() {
		return getLongValue(DerControlModelRegister.ControllerHeartbeat);
	}

	@Override
	public void setControllerHeartbeat(ModbusConnection conn, long heartbeat) throws IOException {
		writeValue(conn, DerControlModelRegister.ControllerHeartbeat, heartbeat);
	}

	@Override
	public void resetAlarms(ModbusConnection conn) throws IOException {
		writeValue(conn, DerControlModelRegister.AlarmReset, 1);
	}

	@Override
	public @Nullable DerOperationCommand getOperationCommand() {
		return getCodedValue(DerControlModelRegister.OperationCommand, DerOperationCommand.class);
	}

	@Override
	public void setOperationCommand(ModbusConnection conn, DerOperationCommand command)
			throws IOException {
		writeValue(conn, DerControlModelRegister.OperationCommand, command.getCode());
	}

	@Override
	public @Nullable Object getPointValue(ModbusReference point) {
		if ( !(point instanceof DerControlModelRegister r) ) {
			return null;
		}
		return switch (r) {
			case LocalRemoteControl -> getLocalRemoteControl();
			case DerHeartbeat -> getDerHeartbeat();
			case ControllerHeartbeat -> getControllerHeartbeat();
			case AlarmReset -> null; // a write-only command, see resetAlarms()
			case OperationCommand -> getOperationCommand();
		};
	}

}
