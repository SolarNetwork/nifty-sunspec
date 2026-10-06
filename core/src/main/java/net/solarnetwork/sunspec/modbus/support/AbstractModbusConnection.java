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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.BitSet;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReadFunction;
import net.solarnetwork.sunspec.modbus.ModbusWriteFunction;

/**
 * Supporting class for {@link ModbusConnection} implementations to extend.
 *
 * <p>
 * This class has been created to help with Modbus testing. All write methods
 * throw an {@link UnsupportedOperationException} and all read methods return
 * {@code null}. The {@link #open()} and {@link #close()} methods do nothing.
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
	public void open() throws IOException {
		// nothing to do
	}

	@Override
	public void close() {
		// nothing to do
	}

	@Override
	public void writeWords(ModbusWriteFunction function, int address, int[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeString(ModbusWriteFunction function, int address, String value, Charset charset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeWords(ModbusWriteFunction function, int address, short[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeBytes(ModbusWriteFunction function, int address, byte[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int[] readWordsUnsigned(ModbusReadFunction function, int address, int count) {
		return new int[0];
	}

	@Override
	public @Nullable String readString(ModbusReadFunction function, int address, int count, boolean trim,
			Charset charset) {
		return null;
	}

	@Override
	public short[] readWords(ModbusReadFunction function, int address, int count) {
		return new short[0];
	}

	@Override
	public BitSet readDiscreteValues(int address, int count) throws IOException {
		return new BitSet();
	}

	@Override
	public BitSet readDiscreteValues(int[] addresses, int count) throws IOException {
		return new BitSet();
	}

	@Override
	public void writeDiscreteValues(int[] addresses, BitSet bits) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeDiscreteValues(ModbusWriteFunction function, int address, int count, BitSet bits)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public BitSet readInputDiscreteValues(final int address, final int count) {
		return new BitSet();
	}

	@Override
	public byte[] readBytes(ModbusReadFunction function, int address, int count) {
		return new byte[0];
	}

}
