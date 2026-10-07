/* ==================================================================
 * NiftyModbusConnection.java - 7/10/2026 7:21:37 pm
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

package net.solarnetwork.sunspec.shell;

import static net.solarnetwork.io.modbus.netty.msg.RegistersModbusMessage.readHoldingsRequest;
import static net.solarnetwork.io.modbus.netty.msg.RegistersModbusMessage.readInputsRequest;
import static net.solarnetwork.io.modbus.netty.msg.RegistersModbusMessage.writeHoldingRequest;
import static net.solarnetwork.io.modbus.netty.msg.RegistersModbusMessage.writeHoldingsRequest;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.io.modbus.ModbusByteUtils;
import net.solarnetwork.io.modbus.ModbusClient;
import net.solarnetwork.io.modbus.ModbusMessage;
import net.solarnetwork.io.modbus.ModbusTimeoutException;
import net.solarnetwork.io.modbus.RegistersModbusMessage;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReadFunction;
import net.solarnetwork.sunspec.modbus.ModbusReadingFunction;
import net.solarnetwork.sunspec.modbus.ModbusWriteFunction;
import net.solarnetwork.sunspec.modbus.ModbusWritingFunction;

/**
 * {@link ModbusConnection} implementation using a Nifty Modbus
 * {@link ModbusClient}.
 *
 * <p>
 * The client is started by {@link #open()}, and is restarted before a request
 * if it has disconnected, for example after a device closed an idle TCP
 * connection.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public class NiftyModbusConnection implements ModbusConnection, AutoCloseable {

	/** The maximum number of seconds to wait for the client to connect. */
	public static final int CONNECT_TIMEOUT_SECS = 30;

	private final ModbusClient client;
	private final int unitId;

	/**
	 * Constructor.
	 *
	 * @param client
	 *        the client to use
	 * @param unitId
	 *        the unit ID of the device
	 */
	public NiftyModbusConnection(ModbusClient client, int unitId) {
		super();
		this.client = client;
		this.unitId = unitId;
	}

	/**
	 * Get a description of the connection.
	 *
	 * @return the description, such as {@code 192.168.1.10:502 unit 1}
	 */
	public String getDescription() {
		return client.getClientConfig().getDescription() + " unit " + unitId;
	}

	/**
	 * Start the client, if it is not already connected.
	 *
	 * <p>
	 * A client that has been started but is no longer connected is stopped and
	 * then started again.
	 * </p>
	 *
	 * @throws IOException
	 *         if the connection cannot be established
	 */
	public void open() throws IOException {
		if ( client.isConnected() ) {
			return;
		}
		try {
			if ( client.isStarted() ) {
				client.stop().get(CONNECT_TIMEOUT_SECS, TimeUnit.SECONDS);
			}
			client.start().get(CONNECT_TIMEOUT_SECS, TimeUnit.SECONDS);
		} catch ( InterruptedException e ) {
			Thread.currentThread().interrupt();
			throw new InterruptedIOException(
					String.format("Interrupted connecting to %s.", getDescription()));
		} catch ( TimeoutException e ) {
			throw new IOException(String.format("Timeout connecting to %s.", getDescription()), e);
		} catch ( ExecutionException e ) {
			final Throwable t = (e.getCause() != null ? e.getCause() : e);
			throw new IOException(String.format("Error connecting to %s: %s", getDescription(),
					t.getMessage() != null ? t.getMessage() : t.toString()), t);
		}
	}

	/**
	 * Stop the client.
	 */
	@Override
	public void close() {
		try {
			client.stop().get(CONNECT_TIMEOUT_SECS, TimeUnit.SECONDS);
		} catch ( InterruptedException e ) {
			Thread.currentThread().interrupt();
		} catch ( ExecutionException | TimeoutException e ) {
			// ignore
		}
	}

	@Override
	public int getUnitId() {
		return unitId;
	}

	@Override
	public short[] readWords(ModbusReadingFunction function, int address, int count) throws IOException {
		final ModbusMessage req;
		if ( function.getCode() == ModbusReadFunction.ReadHoldingRegister.getCode() ) {
			req = readHoldingsRequest(unitId, address, count);
		} else if ( function.getCode() == ModbusReadFunction.ReadInputRegister.getCode() ) {
			req = readInputsRequest(unitId, address, count);
		} else {
			throw new IOException(String.format("Unsupported read function %d.", function.getCode()));
		}
		final String action = String.format("reading %d registers at %d", count, address);
		final ModbusMessage res = send(req, action);
		final RegistersModbusMessage regs = res.unwrap(RegistersModbusMessage.class);
		final short[] result = (regs != null ? regs.dataDecode() : null);
		if ( result == null || result.length != count ) {
			throw new IOException(
					String.format("Unexpected response %s on %s: %s", action, getDescription(), res));
		}
		return result;
	}

	@Override
	public void writeWords(ModbusWritingFunction function, int address, short[] values)
			throws IOException {
		final ModbusMessage req;
		if ( function.getCode() == ModbusWriteFunction.WriteHoldingRegister.getCode()
				&& values.length == 1 ) {
			req = writeHoldingRequest(unitId, address, Short.toUnsignedInt(values[0]));
		} else if ( function.getCode() == ModbusWriteFunction.WriteMultipleHoldingRegisters.getCode() ) {
			req = writeHoldingsRequest(unitId, address, values);
		} else {
			throw new IOException(String.format("Unsupported write function %d for %d values.",
					function.getCode(), values.length));
		}
		send(req, String.format("writing %d registers at %d", values.length, address));
	}

	@Override
	public @Nullable String readString(ModbusReadingFunction function, int address, int count,
			boolean trim, Charset charset) throws IOException {
		final byte[] bytes = ModbusByteUtils.encode(readWords(function, address, count));
		if ( bytes == null ) {
			return null;
		}
		final String result = new String(bytes, charset);
		return (trim ? result.trim() : result);
	}

	private ModbusMessage send(ModbusMessage req, String action) throws IOException {
		open();
		final ModbusMessage res;
		try {
			res = client.send(req).validate();
		} catch ( ModbusTimeoutException e ) {
			throw new IOException(String.format("Timeout %s on %s.", action, getDescription()), e);
		} catch ( RuntimeException e ) {
			throw new IOException(String.format("Error %s on %s: %s", action, getDescription(),
					e.getMessage() != null ? e.getMessage() : e.toString()), e);
		}
		if ( res.isException() ) {
			throw new IOException(String.format("Modbus exception %s %s on %s.", res.getError(), action,
					getDescription()));
		}
		return res;
	}

}
