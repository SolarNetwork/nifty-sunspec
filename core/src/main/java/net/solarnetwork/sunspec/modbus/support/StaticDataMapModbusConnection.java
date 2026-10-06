/* ==================================================================
 * StaticDataMapModbusConnection.java - 8/11/2019 11:28:16 am
 *
 * Copyright 2019 SolarNetwork.net Dev Team
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
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusWriteFunction;
import net.solarnetwork.util.IntShortMap;

/**
 * {@link ModbusConnection} for reading/writing static data.
 *
 * <p>
 * This class can be useful in tests working with Modbus connections.
 * </p>
 *
 * @author matt
 * @version 3.0
 * @since 2.16
 */
public class StaticDataMapModbusConnection extends StaticDataMapReadonlyModbusConnection {

	/**
	 * Constructor.
	 *
	 * @param data
	 *        the starting data
	 */
	public StaticDataMapModbusConnection(IntShortMap data) {
		super(data);
	}

	@Override
	public void writeWords(ModbusWriteFunction function, int address, int[] values) {
		final IntShortMap data = getData();
		for ( int i = 0, len = values.length; i < len; i++ ) {
			data.putValue(address + i, values[i]);
		}
	}

	@Override
	public void writeString(ModbusWriteFunction function, int address, String value, Charset charset) {
		if ( value == null || value.isEmpty() ) {
			return;
		}
		byte[] data = value.getBytes(charset);
		writeBytes(function, address, data);
	}

	@Override
	public void writeWords(ModbusWriteFunction function, int address, short[] values) {
		final IntShortMap data = getData();
		for ( int i = 0, len = values.length; i < len; i++ ) {
			data.putValue(address + i, (int) values[i]);
		}
	}

	@Override
	public void writeDiscreteValues(final int[] addresses, final BitSet bits) {
		final IntShortMap data = getData();
		for ( int i = 0; i < addresses.length; i++ ) {
			data.putValue(addresses[i], bits.get(i) ? (short) 1 : (short) 0);
		}
	}

	@Override
	public void writeDiscreteValues(ModbusWriteFunction function, int address, int count, BitSet bits)
			throws IOException {
		final IntShortMap data = getData();
		for ( int i = 0; i < count; i++ ) {
			data.putValue(address + i, bits.get(i) ? (short) 1 : (short) 0);
		}
	}

	@Override
	public void writeBytes(ModbusWriteFunction function, int address, byte[] values) {
		int[] unsigned = new int[(int) Math.ceil(values.length / 2.0)];
		for ( int i = 0; i < values.length; i += 2 ) {
			int v = ((values[i] & 0xFF) << 8);
			if ( i + 1 < values.length ) {
				v |= (values[i + 1] & 0xFF);
			}
			unsigned[i / 2] = v;
		}
		writeWords(function, address, unsigned);
	}

}
