/* ==================================================================
 * StaticDataReadonlyModbusConnection.java - 8/10/2018 8:25:30 AM
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
import java.util.BitSet;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReadFunction;
import net.solarnetwork.util.IntShortMap;
import net.solarnetwork.util.ObjectUtils;

/**
 * {@link ModbusConnection} for reading static data.
 *
 * <p>
 * This class can be useful in tests working with Modbus connections.
 * </p>
 *
 * @author matt
 * @version 3.0
 * @since 2.16
 */
public class StaticDataMapReadonlyModbusConnection extends AbstractModbusConnection {

	private final IntShortMap data;

	/**
	 * Construct with the data.
	 *
	 * @param data
	 *        the starting data
	 * @throws IllegalArgumentException
	 *         if any argument is {@code null}
	 */
	public StaticDataMapReadonlyModbusConnection(IntShortMap data) {
		super();
		this.data = ObjectUtils.requireNonNullArgument(data, "data");
	}

	/**
	 * Constructor.
	 *
	 * @param data
	 *        the starting data, starting from Modbus register address
	 *        {@literal 0}
	 */
	public StaticDataMapReadonlyModbusConnection(int @Nullable [] data) {
		this(data, 0);

	}

	/**
	 * Constructor.
	 *
	 * @param data
	 *        the starting data
	 * @param address
	 *        the starting Modbus register address for {@code data}
	 */
	public StaticDataMapReadonlyModbusConnection(int @Nullable [] data, int address) {
		this(ModbusDataUtils.shortArray(data), address);

	}

	/**
	 * Constructor.
	 *
	 * @param data
	 *        the starting data
	 * @param address
	 *        the starting Modbus register address for {@code data}
	 */
	public StaticDataMapReadonlyModbusConnection(short @Nullable [] data, int address) {
		this(new IntShortMap());
		if ( data != null ) {
			for ( int i = 0; i < data.length; i++ ) {
				this.data.putValue(address + i, data[i]);
			}
		}
	}

	/**
	 * Get the data map.
	 *
	 * @return the data
	 */
	protected IntShortMap getData() {
		return data;
	}

	@Override
	public int[] readWordsUnsigned(ModbusReadFunction function, int address, int count) {
		int[] out = new int[count];
		data.forEachOrdered(address, address + count, (k, v) -> {
			out[k - address] = v & 0xFFFF;
		});
		return out;
	}

	@Override
	public @Nullable String readString(ModbusReadFunction function, int address, int count, boolean trim,
			Charset charset) {
		final byte[] bytes = readBytes(function, address, count);
		String result = null;
		if ( bytes != null ) {
			result = new String(bytes, charset);
			if ( trim ) {
				result = result.trim();
			}
		}
		return result;
	}

	@Override
	public short[] readWords(ModbusReadFunction function, int address, int count) {
		short[] out = new short[count];
		data.forEachOrdered(address, address + count, (k, v) -> {
			out[k - address] = v;
		});
		return out;
	}

	@Override
	public BitSet readInputDiscreteValues(final int address, final int count) {
		return readDiscreteValues(address, count);
	}

	@Override
	public BitSet readDiscreteValues(final int address, final int count) {
		return readDiscreteValues(new int[] { address }, count);
	}

	@Override
	public BitSet readDiscreteValues(final int[] addresses, final int count) {
		BitSet out = new BitSet();
		for ( int i = 0, w = 0; i < addresses.length; i++ ) {
			final int d = data.getValue(addresses[i]);
			for ( int j = 0; j < count; j++, w++ ) {
				if ( ((d >> j) & 0x1) == 1 ) {
					out.set(w);
				}
			}
		}
		return out;
	}

	@Override
	public byte[] readBytes(ModbusReadFunction function, int address, int count) {
		byte[] result = new byte[count * 2];
		for ( int i = 0; i < count; i++ ) {
			final int d = data.getValue(address + i);
			result[i * 2] = (byte) ((d >> 8) & 0xFF);
			result[i * 2 + 1] = (byte) (d & 0xFF);
		}
		return result;
	}

}
