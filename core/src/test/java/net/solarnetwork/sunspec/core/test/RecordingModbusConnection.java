/* ==================================================================
 * RecordingModbusConnection.java - 5/10/2026 5:31:07 pm
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

package net.solarnetwork.sunspec.core.test;

import java.util.ArrayList;
import java.util.List;
import net.solarnetwork.sunspec.core.support.IntShortMap;
import net.solarnetwork.sunspec.modbus.ModbusReadingFunction;
import net.solarnetwork.sunspec.modbus.ModbusWritingFunction;
import net.solarnetwork.sunspec.modbus.support.StaticDataMapModbusConnection;

/**
 * A writable static data Modbus connection that records the read and write
 * requests made to it.
 *
 * @author matt
 * @version 1.0
 */
public class RecordingModbusConnection extends StaticDataMapModbusConnection {

	private final List<List<Integer>> reads = new ArrayList<>();
	private final List<List<Integer>> writes = new ArrayList<>();

	/**
	 * Constructor.
	 *
	 * @param data
	 *        the data
	 */
	public RecordingModbusConnection(IntShortMap data) {
		super(data);
	}

	@Override
	public short[] readWords(ModbusReadingFunction function, int address, int count) {
		reads.add(List.of(address, count));
		return super.readWords(function, address, count);
	}

	@Override
	public void writeWords(ModbusWritingFunction function, int address, short[] values) {
		writes.add(List.of(address, values.length));
		super.writeWords(function, address, values);
	}

	/**
	 * Get the read requests made to this connection.
	 *
	 * @return the address and register count of each read request, in the order
	 *         they were made
	 */
	public List<List<Integer>> getReads() {
		return reads;
	}

	/**
	 * Get the write requests made to this connection.
	 *
	 * @return the address and register count of each write request, in the
	 *         order they were made
	 */
	public List<List<Integer>> getWrites() {
		return writes;
	}

}
