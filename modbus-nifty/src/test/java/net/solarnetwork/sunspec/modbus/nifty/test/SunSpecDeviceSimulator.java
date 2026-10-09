/* ==================================================================
 * SunSpecDeviceSimulator.java - 7/10/2026 8:31:09 pm
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

package net.solarnetwork.sunspec.modbus.nifty.test;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.solarnetwork.io.modbus.ModbusErrorCode;
import net.solarnetwork.io.modbus.ModbusFunctionCodes;
import net.solarnetwork.io.modbus.ModbusMessage;
import net.solarnetwork.io.modbus.RegistersModbusMessage;
import net.solarnetwork.io.modbus.netty.msg.BaseModbusMessage;
import net.solarnetwork.sunspec.test.DataUtils;

/**
 * A simulated SunSpec device, serving holding registers loaded from an
 * {@code mbpoll}-style register dump.
 *
 * <p>
 * This can be used as the message handler of a Nifty Modbus server. Reads of
 * any register not in the dump get an {@code IllegalDataAddress} exception
 * response, and writes update the registers. All unit IDs are answered.
 * </p>
 *
 * <p>
 * The {@link #main(String...)} method runs a Modbus TCP or RTU server for the
 * simulated device, to try the shell with.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public final class SunSpecDeviceSimulator implements BiConsumer<ModbusMessage, Consumer<ModbusMessage>> {

	private final Map<Integer, Integer> registers;
	private int requestCount;

	/**
	 * Constructor.
	 *
	 * @param registers
	 *        the register values, as unsigned 16-bit values mapped to their
	 *        addresses
	 */
	public SunSpecDeviceSimulator(Map<Integer, Integer> registers) {
		super();
		this.registers = new HashMap<>(registers);
	}

	/**
	 * Create a simulator from a register dump.
	 *
	 * @param in
	 *        the register dump to read
	 * @return the simulator
	 * @throws IOException
	 *         if the dump cannot be read
	 */
	public static SunSpecDeviceSimulator forDump(InputStream in) throws IOException {
		try (BufferedReader r = new BufferedReader(new InputStreamReader(in, UTF_8))) {
			return new SunSpecDeviceSimulator(DataUtils.parseModbusHexRegisterMappingLines(r));
		}
	}

	/**
	 * Create a simulator from a register dump file, or a class-path resource
	 * relative to this class.
	 *
	 * @param name
	 *        the file path or resource name, such as
	 *        {@code test-data-der-01.txt}
	 * @return the simulator
	 * @throws IOException
	 *         if the dump cannot be read
	 */
	public static SunSpecDeviceSimulator forDump(String name) throws IOException {
		final Path path = Path.of(name);
		if ( Files.isReadable(path) ) {
			return forDump(Files.newInputStream(path));
		}
		final InputStream in = SunSpecDeviceSimulator.class.getResourceAsStream(name);
		if ( in == null ) {
			throw new IOException("Register dump [" + name + "] not found.");
		}
		return forDump(in);
	}

	/**
	 * Get a register value.
	 *
	 * @param address
	 *        the register address
	 * @return the unsigned 16-bit value, or {@code null} if the register does
	 *         not exist
	 */
	public synchronized Integer getRegister(int address) {
		return registers.get(address);
	}

	/**
	 * Get the number of requests handled.
	 *
	 * @return the request count
	 */
	public synchronized int getRequestCount() {
		return requestCount;
	}

	@Override
	public void accept(ModbusMessage req, Consumer<ModbusMessage> sender) {
		sender.accept(handle(req));
	}

	private synchronized ModbusMessage handle(ModbusMessage req) {
		requestCount++;
		final RegistersModbusMessage r = req.unwrap(RegistersModbusMessage.class);
		if ( r == null ) {
			return error(req, ModbusErrorCode.IllegalFunction);
		}
		return switch (req.getFunction().getCode()) {
			case ModbusFunctionCodes.READ_HOLDING_REGISTERS -> readHoldings(req, r);
			case ModbusFunctionCodes.WRITE_HOLDING_REGISTER, ModbusFunctionCodes.WRITE_HOLDING_REGISTERS -> writeHoldings(
					req, r);
			default -> error(req, ModbusErrorCode.IllegalFunction);
		};
	}

	private ModbusMessage readHoldings(ModbusMessage req, RegistersModbusMessage r) {
		final int address = r.getAddress();
		final short[] values = new short[r.getCount()];
		for ( int i = 0; i < values.length; i++ ) {
			final Integer v = registers.get(address + i);
			if ( v == null ) {
				return error(req, ModbusErrorCode.IllegalDataAddress);
			}
			values[i] = v.shortValue();
		}
		return net.solarnetwork.io.modbus.netty.msg.RegistersModbusMessage
				.readHoldingsResponse(req.getUnitId(), address, values);
	}

	private ModbusMessage writeHoldings(ModbusMessage req, RegistersModbusMessage r) {
		final int address = r.getAddress();
		final int[] values = r.dataDecodeUnsigned();
		if ( values == null ) {
			return error(req, ModbusErrorCode.IllegalDataValue);
		}
		for ( int i = 0; i < values.length; i++ ) {
			if ( !registers.containsKey(address + i) ) {
				return error(req, ModbusErrorCode.IllegalDataAddress);
			}
		}
		for ( int i = 0; i < values.length; i++ ) {
			registers.put(address + i, values[i]);
		}
		if ( req.getFunction().getCode() == ModbusFunctionCodes.WRITE_HOLDING_REGISTER ) {
			return net.solarnetwork.io.modbus.netty.msg.RegistersModbusMessage
					.writeHoldingResponse(req.getUnitId(), address, values[0]);
		}
		return net.solarnetwork.io.modbus.netty.msg.RegistersModbusMessage
				.writeHoldingsResponse(req.getUnitId(), address, values.length);
	}

	private static ModbusMessage error(ModbusMessage req, ModbusErrorCode code) {
		return new BaseModbusMessage(req.getUnitId(), req.getFunction(), code);
	}

}
