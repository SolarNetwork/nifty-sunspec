/* ==================================================================
 * AbstractModbusConnection.java - 24/03/2018 9:11:50 AM
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

package net.solarnetwork.sunspec.modbus.support;

import java.nio.charset.Charset;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReadingFunction;
import net.solarnetwork.sunspec.modbus.ModbusWritingFunction;

/**
 * Supporting class for {@link ModbusConnection} implementations to extend.
 *
 * <p>
 * This class has been created to help with Modbus testing. All write methods
 * throw an {@link UnsupportedOperationException} and all read methods return
 * {@code null}.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public abstract class AbstractModbusConnection implements ModbusConnection {

	/** The unit ID. */
	protected int unitId;

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@code unitId} will be set to {@code 0}.
	 */
	public AbstractModbusConnection() {
		this(0);
	}

	/**
	 * Constructor.
	 *
	 * @param unitId
	 *        the unit ID to use
	 */
	public AbstractModbusConnection(int unitId) {
		super();
		this.unitId = unitId;
	}

	@Override
	public int getUnitId() {
		return unitId;
	}

	@Override
	public void writeWords(ModbusWritingFunction function, int address, short[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public @Nullable String readString(ModbusReadingFunction function, int address, int count,
			boolean trim, Charset charset) {
		return null;
	}

	@Override
	public short[] readWords(ModbusReadingFunction function, int address, int count) {
		return new short[0];
	}

}
