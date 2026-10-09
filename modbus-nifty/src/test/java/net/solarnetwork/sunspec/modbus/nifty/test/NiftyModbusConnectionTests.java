/* ==================================================================
 * NiftyModbusConnectionTests.java - 7/10/2026 9:58:27 pm
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

import static java.nio.charset.StandardCharsets.US_ASCII;
import static net.solarnetwork.sunspec.modbus.ModbusReadFunction.ReadCoil;
import static net.solarnetwork.sunspec.modbus.ModbusReadFunction.ReadHoldingRegister;
import static net.solarnetwork.sunspec.modbus.ModbusWriteFunction.WriteHoldingRegister;
import static net.solarnetwork.sunspec.modbus.ModbusWriteFunction.WriteMultipleHoldingRegisters;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.netty.channel.EventLoopGroup;
import net.solarnetwork.io.modbus.netty.channel.MultiThreadIoEventLoopGroupFactory;
import net.solarnetwork.io.modbus.tcp.netty.NettyTcpModbusClientConfig;
import net.solarnetwork.io.modbus.tcp.netty.NettyTcpModbusServer;
import net.solarnetwork.io.modbus.tcp.netty.TcpNettyModbusClient;
import net.solarnetwork.sunspec.modbus.nifty.NiftyModbusConnection;

/**
 * Test cases for the {@link NiftyModbusConnection} class, with a simulated
 * device on a Modbus TCP server.
 *
 * @author matt
 * @version 1.0
 */
public class NiftyModbusConnectionTests {

	private int port;
	private SunSpecDeviceSimulator device;
	private NettyTcpModbusServer server;
	private EventLoopGroup eventLoopGroup;
	private TcpNettyModbusClient client;
	private NiftyModbusConnection conn;

	/**
	 * Get a free TCP port.
	 *
	 * @return the port
	 * @throws IOException
	 *         if no port is available
	 */
	public static int freePort() throws IOException {
		try (ServerSocket s = new ServerSocket(0)) {
			return s.getLocalPort();
		}
	}

	/**
	 * Start a Modbus TCP server for a simulated device.
	 *
	 * @param port
	 *        the port to listen on
	 * @param device
	 *        the device
	 * @return the started server
	 * @throws IOException
	 *         if the server cannot be started
	 */
	public static NettyTcpModbusServer startServer(int port, SunSpecDeviceSimulator device)
			throws IOException {
		NettyTcpModbusServer server = new NettyTcpModbusServer("127.0.0.1", port);
		server.setMessageHandler(device);
		server.start();
		return server;
	}

	/**
	 * Stop a Modbus TCP server, and wait for its port to be closed.
	 *
	 * <p>
	 * The server socket is closed asynchronously, so it can accept connections
	 * for a short time after the server is stopped.
	 * </p>
	 *
	 * @param server
	 *        the server to stop
	 * @param port
	 *        the port the server listens on
	 * @throws InterruptedException
	 *         if interrupted while waiting
	 */
	public static void stopServer(NettyTcpModbusServer server, int port) throws InterruptedException {
		server.stop();
		final long deadline = System.currentTimeMillis() + 10_000L;
		while ( System.currentTimeMillis() < deadline ) {
			try (@SuppressWarnings("unused")
			Socket s = new Socket(InetAddress.getLoopbackAddress(), port)) {
				Thread.sleep(50);
			} catch ( IOException e ) {
				// port closed
				return;
			}
		}
	}

	@BeforeEach
	public void setup() throws IOException {
		port = freePort();
		device = SunSpecDeviceSimulator.forDump(TestDevices.DER_DUMP);
		server = startServer(port, device);
		NettyTcpModbusClientConfig config = new NettyTcpModbusClientConfig("127.0.0.1", port);
		config.setAutoReconnect(false);
		// like the shell, provide the event loop group so it can be stopped without a quiet period
		eventLoopGroup = MultiThreadIoEventLoopGroupFactory.INSTANCE.apply(this, false);
		client = new TcpNettyModbusClient(config, eventLoopGroup, null);
		client.setReplyTimeout(2000);
		conn = new NiftyModbusConnection(client, 1);
	}

	@AfterEach
	public void teardown() {
		conn.close();
		eventLoopGroup.shutdownGracefully(0, 1, TimeUnit.SECONDS).awaitUninterruptibly(2,
				TimeUnit.SECONDS);
		server.stop();
	}

	@Test
	public void description() {
		// @formatter:off
		then(conn.getDescription())
			.as("Description from client and unit ID")
			.isEqualTo("127.0.0.1:" + port + " unit 1")
			;
		// @formatter:on
	}

	@Test
	public void readWords() throws IOException {
		// WHEN
		short[] result = conn.readWords(ReadHoldingRegister, 40000, 4);

		// THEN
		// @formatter:off
		then(result)
			.as("SunS marker and common model header read")
			.containsExactly((short) 0x5375, (short) 0x6E53, (short) 1, (short) 66)
			;
		then(client.isConnected())
			.as("Client started for request")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void readString() throws IOException {
		// WHEN
		String result = conn.readString(ReadHoldingRegister, 40000, 2, true, US_ASCII);

		// THEN
		// @formatter:off
		then(result)
			.as("SunS marker read as string")
			.isEqualTo("SunS")
			;
		// @formatter:on
	}

	@Test
	public void readWords_modbusException() {
		// WHEN
		Throwable t = catchThrowable(() -> conn.readWords(ReadHoldingRegister, 39000, 2));

		// THEN
		// @formatter:off
		then(t)
			.as("Modbus exception response thrown as IOException")
			.isInstanceOf(IOException.class)
			.hasMessage("Modbus exception IllegalDataAddress reading 2 registers at 39000 on "
					+ "127.0.0.1:%d unit 1.", port)
			;
		// @formatter:on
	}

	@Test
	public void readWords_unsupportedFunction() {
		// WHEN
		Throwable t = catchThrowable(() -> conn.readWords(ReadCoil, 0, 1));

		// THEN
		// @formatter:off
		then(t)
			.as("Only register read functions supported")
			.isInstanceOf(IOException.class)
			.hasMessage("Unsupported read function 1.")
			;
		// @formatter:on
	}

	@Test
	public void writeWords() throws IOException {
		// WHEN
		conn.writeWords(WriteMultipleHoldingRegisters, 40100, new short[] { 1, 2 });
		conn.writeWords(WriteHoldingRegister, 40102, new short[] { (short) 0xFFFE });

		// THEN
		// @formatter:off
		then(device.getRegister(40100))
			.as("First register written")
			.isEqualTo(1)
			;
		then(device.getRegister(40101))
			.as("Second register written")
			.isEqualTo(2)
			;
		then(device.getRegister(40102))
			.as("Single register written as unsigned value")
			.isEqualTo(0xFFFE)
			;
		// @formatter:on
	}

	@Test
	public void reconnect() throws Exception {
		// GIVEN
		conn.readWords(ReadHoldingRegister, 40000, 2);

		// the device closes the connection, and then accepts new connections
		stopServer(server, port);
		final long deadline = System.currentTimeMillis() + 10_000L;
		while ( client.isConnected() && System.currentTimeMillis() < deadline ) {
			Thread.sleep(50);
		}
		SunSpecDeviceSimulator restartedDevice = SunSpecDeviceSimulator.forDump(TestDevices.DER_DUMP);
		server = startServer(port, restartedDevice);

		// WHEN
		short[] result = conn.readWords(ReadHoldingRegister, 40000, 2);

		// THEN
		// @formatter:off
		then(result)
			.as("Read after the client reconnected")
			.containsExactly((short) 0x5375, (short) 0x6E53)
			;
		then(restartedDevice.getRequestCount())
			.as("Request sent on a new connection to the restarted device")
			.isEqualTo(1)
			;
		// @formatter:on
	}

	@Test
	public void open_refused() throws InterruptedException {
		// GIVEN
		stopServer(server, port);

		// WHEN
		Throwable t = catchThrowable(() -> conn.open());

		// THEN
		// @formatter:off
		then(t)
			.as("Connection error thrown as IOException")
			.isInstanceOf(IOException.class)
			.hasMessageStartingWith("Error connecting to 127.0.0.1:%d unit 1: ", port)
			;
		// @formatter:on
	}

}
