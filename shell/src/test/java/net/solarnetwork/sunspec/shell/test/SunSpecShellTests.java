/* ==================================================================
 * SunSpecShellTests.java - 7/10/2026 10:14:05 pm
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

package net.solarnetwork.sunspec.shell.test;

import static net.solarnetwork.sunspec.modbus.ModbusReadFunction.ReadHoldingRegister;
import static net.solarnetwork.sunspec.shell.SunSpecShell.PROMPT;
import static net.solarnetwork.sunspec.shell.test.TestDevices.DER_DUMP;
import static net.solarnetwork.sunspec.shell.test.TestDevices.INVERTER_DUMP;
import static net.solarnetwork.sunspec.shell.test.TestDevices.connection;
import static org.assertj.core.api.BDDAssertions.and;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.spy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import org.junit.jupiter.api.Test;
import net.solarnetwork.io.modbus.tcp.netty.NettyTcpModbusServer;
import net.solarnetwork.sunspec.api.inverter.InverterMpptExtensionModelAccessor;
import net.solarnetwork.sunspec.core.ModelDataFactory;
import net.solarnetwork.sunspec.core.inverter.InverterMpptExtensionModelAccessorImpl;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.support.ModelData;
import net.solarnetwork.sunspec.shell.ModelTables;
import net.solarnetwork.sunspec.shell.SunSpecShell;

/**
 * Test cases for the {@link SunSpecShell} class.
 *
 * @author matt
 * @version 1.0
 */
@SuppressWarnings("static-access")
public class SunSpecShellTests {

	private static final String NL = System.lineSeparator();

	private ModbusConnection conn;
	private ModelData data;
	private final StringWriter out = new StringWriter();

	private void discover(String dump) throws IOException {
		conn = spy(connection(dump));
		data = ModelDataFactory.getInstance().discoverModels(conn);
		clearInvocations(conn);
	}

	private SunSpecShell shell(String input) {
		return new SunSpecShell(conn, data, new BufferedReader(new StringReader(input)),
				new PrintWriter(out, true));
	}

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

	private static NettyTcpModbusServer startServer(int port, SunSpecDeviceSimulator device)
			throws IOException {
		NettyTcpModbusServer server = new NettyTcpModbusServer("127.0.0.1", port);
		server.setMessageHandler(device);
		server.start();
		return server;
	}

	@Test
	public void printSummary() throws IOException {
		// GIVEN
		discover(DER_DUMP);
		SunSpecShell shell = shell("");

		// WHEN
		shell.printSummary();

		// THEN
		// @formatter:off
		and.then(out.toString())
			.as("Common model table, then the model list, then a hint")
			.isEqualTo(ModelTables.pointTable("Model 1: Common model", data) + NL
					+ NL
					+ ModelTables.modelListTable(shell.getModels()) + NL
					+ NL
					+ "Enter help for the available commands." + NL)
			;
		and.then(shell.getModels())
			.as("Common model followed by discovered models")
			.hasSize(18)
			.first().isSameAs(data)
			;
		// @formatter:on
	}

	@Test
	public void help() throws IOException {
		// GIVEN
		discover(DER_DUMP);

		// WHEN
		shell("help\n?\n").run();

		// THEN
		// @formatter:off
		and.then(out.toString())
			.as("Help printed for help and ?, then end of input")
			.isEqualTo(PROMPT + SunSpecShell.HELP + PROMPT + SunSpecShell.HELP + PROMPT + NL)
			;
		// @formatter:on
	}

	@Test
	public void modelList() throws IOException {
		// GIVEN
		discover(DER_DUMP);
		SunSpecShell shell = shell("MODEL-LIST\n");

		// WHEN
		shell.run();

		// THEN
		// @formatter:off
		and.then(out.toString())
			.as("Model list printed, with case-insensitive command")
			.isEqualTo(PROMPT + ModelTables.modelListTable(shell.getModels()) + NL + PROMPT + NL)
			;
		// @formatter:on
	}

	@Test
	public void modelView() throws IOException {
		// GIVEN
		discover(DER_DUMP);

		// WHEN
		shell("model-view 701\n").run();

		// THEN
		// @formatter:off
		then(conn).should(atLeastOnce()).readWords(eq(ReadHoldingRegister), anyInt(), anyInt());

		and.then(out.toString().lines())
			.as("Model point table printed")
			.contains(
				PROMPT + "╔═════════════════════════════════════════════════════════════════════════════╗",
				"║ Model 701: DER AC measurement                                               ║",
				"║ frequency                  │                                          60 Hz ║",
				"║ alarms                     │ GridDisconnect, UnderFrequency, AcUnderVoltage ║")
			;
		// @formatter:on
	}

	@Test
	public void modelView_repeatedModel() throws IOException {
		// GIVEN
		discover(INVERTER_DUMP);
		InverterMpptExtensionModelAccessor mppt = data
				.findTypedModel(InverterMpptExtensionModelAccessor.class);
		data.addModel(mppt.getModelLength(),
				new InverterMpptExtensionModelAccessorImpl(data, mppt.getBaseAddress(), 160));

		// WHEN
		shell("model-view 160\n").run();

		// THEN
		// @formatter:off
		and.then(out.toString().lines())
			.as("Each instance of the model printed")
			.contains(
				"║ Model 160: Multiple MPPT Inverter Extension Model (1 of 2) ║",
				"║ Model 160: Multiple MPPT Inverter Extension Model (2 of 2) ║")
			;
		// @formatter:on
	}

	@Test
	public void modelView_notAvailable() throws IOException {
		// GIVEN
		discover(DER_DUMP);

		// WHEN
		shell("model-view 999\n").run();

		// THEN
		// @formatter:off
		and.then(out.toString())
			.as("Model not available")
			.isEqualTo(PROMPT + "Model 999 is not available." + NL + PROMPT + NL)
			;
		// @formatter:on
	}

	@Test
	public void modelView_notSupported() throws IOException {
		// GIVEN
		discover(DER_DUMP);

		// WHEN
		shell("model-view 64122\n").run();

		// THEN
		// @formatter:off
		then(conn).shouldHaveNoInteractions();

		and.then(out.toString())
			.as("Model without an accessor not read")
			.isEqualTo(PROMPT + "The points of model 64122 are not supported." + NL + PROMPT + NL)
			;
		// @formatter:on
	}

	@Test
	public void modelView_invalidNumber() throws IOException {
		// GIVEN
		discover(DER_DUMP);

		// WHEN
		shell("model-view abc\nmodel-view\nmodel-view 1.5\n").run();

		// THEN
		// @formatter:off
		then(conn).shouldHaveNoInteractions();

		and.then(out.toString())
			.as("Invalid model numbers ignored")
			.isEqualTo(PROMPT + PROMPT + PROMPT + PROMPT + NL)
			;
		// @formatter:on
	}

	@Test
	public void modelView_readError() throws IOException {
		// GIVEN
		discover(DER_DUMP);
		willThrow(new IOException("Timeout reading.")).given(conn).readWords(eq(ReadHoldingRegister),
				anyInt(), anyInt());

		// WHEN
		shell("model-view 701\n").run();

		// THEN
		// @formatter:off
		and.then(out.toString())
			.as("Read error printed")
			.isEqualTo(PROMPT + "Error reading model 701: Timeout reading." + NL + PROMPT + NL)
			;
		// @formatter:on
	}

	@Test
	public void modelView_unexpectedError() throws IOException {
		// GIVEN
		discover(DER_DUMP);
		SunSpecShell shell = shell("model-view 701\nmodel-list\n");
		willThrow(new IllegalStateException("Bad data.")).given(conn).readWords(eq(ReadHoldingRegister),
				anyInt(), anyInt());

		// WHEN
		shell.run();

		// THEN
		// @formatter:off
		and.then(out.toString())
			.as("Unexpected error printed, then the next command run")
			.isEqualTo(PROMPT + "Error running command [model-view]: "
					+ "java.lang.IllegalStateException: Bad data." + NL
					+ PROMPT + ModelTables.modelListTable(shell.getModels()) + NL
					+ PROMPT + NL)
			;
		// @formatter:on
	}

	@Test
	public void unknownCommand() throws IOException {
		// GIVEN
		discover(DER_DUMP);

		// WHEN
		shell("foo bar\n").run();

		// THEN
		// @formatter:off
		and.then(out.toString())
			.as("Unknown command")
			.isEqualTo(PROMPT + "Unknown command [foo]; enter help for the available commands." + NL
					+ PROMPT + NL)
			;
		// @formatter:on
	}

	@Test
	public void blankLines() throws IOException {
		// GIVEN
		discover(DER_DUMP);

		// WHEN
		shell("\n   \n\t\n").run();

		// THEN
		// @formatter:off
		and.then(out.toString())
			.as("Blank lines ignored")
			.isEqualTo(PROMPT + PROMPT + PROMPT + PROMPT + NL)
			;
		// @formatter:on
	}

	@Test
	public void exit() throws IOException {
		// GIVEN
		discover(DER_DUMP);

		// WHEN
		shell("exit\nhelp\n").run();

		// THEN
		// @formatter:off
		and.then(out.toString())
			.as("Commands after exit not run")
			.isEqualTo(PROMPT)
			;
		// @formatter:on
	}

	@Test
	public void quit() throws IOException {
		// GIVEN
		discover(DER_DUMP);

		// WHEN
		shell("  Quit  \nhelp\n").run();

		// THEN
		// @formatter:off
		and.then(out.toString())
			.as("Commands after quit not run")
			.isEqualTo(PROMPT)
			;
		// @formatter:on
	}

	private static int run(StringWriter out, StringWriter err, String input, String... args) {
		return SunSpecShell.run(args, new BufferedReader(new StringReader(input)),
				new PrintWriter(out, true), new PrintWriter(err, true));
	}

	@Test
	public void run_tcp() throws IOException {
		// GIVEN
		int port = freePort();
		NettyTcpModbusServer server = startServer(port,
				SunSpecDeviceSimulator.forDump(TestDevices.INVERTER_DUMP));
		StringWriter err = new StringWriter();

		try {
			// WHEN
			int status = run(out, err, "model-view 160\nexit\n", "-p", String.valueOf(port), "-c", "50",
					"127.0.0.1");

			// THEN
			// @formatter:off
			and.then(status)
				.as("Success status")
				.isZero()
				;
			and.then(err.toString())
				.as("No errors")
				.isEmpty()
				;
			and.then(out.toString().lines())
				.as("Connected and discovered")
				.startsWith(
					"Connected to 127.0.0.1:" + port + " unit 1.",
					"Found SunSpec data at address 40000.",
					"",
					"╔════════════════════════════════╗",
					"║ Model 1: Common model          ║")
				.as("Model list printed")
				.contains("║   160 │ Multiple MPPT Inverter Extension Model             ║")
				.as("Model points read from device and printed")
				.contains("║ moduleDcPower_6   │                        2460 W ║")
				;
			// @formatter:on
		} finally {
			server.stop();
		}
	}

	@Test
	public void run_baseAddressNotFound() throws IOException {
		// GIVEN
		int port = freePort();
		NettyTcpModbusServer server = startServer(port,
				SunSpecDeviceSimulator.forDump(TestDevices.DER_DUMP));
		StringWriter err = new StringWriter();

		try {
			// WHEN
			int status = run(out, err, "", "-p", String.valueOf(port), "-r", "50001", "127.0.0.1");

			// THEN
			// @formatter:off
			and.then(status)
				.as("Discovery failure status")
				.isEqualTo(2)
				;
			and.then(err.toString())
				.as("Discovery error printed")
				.isEqualTo("SunSpec ID 'SunS' not found at base address 0xc350 (50000)" + NL)
				;
			// @formatter:on
		} finally {
			server.stop();
		}
	}

	@Test
	public void run_connectionRefused() throws IOException {
		// GIVEN
		int port = freePort();
		StringWriter err = new StringWriter();

		// WHEN
		int status = run(out, err, "", "-p", String.valueOf(port), "127.0.0.1");

		// THEN
		// @formatter:off
		and.then(status)
			.as("Connection failure status")
			.isEqualTo(2)
			;
		and.then(err.toString())
			.as("Connection error printed")
			.startsWith("Error connecting to 127.0.0.1:" + port + " unit 1: ")
			;
		// @formatter:on
	}

	@Test
	public void run_invalidArgument() {
		// GIVEN
		StringWriter err = new StringWriter();

		// WHEN
		int status = run(out, err, "", "-a", "300", "host");

		// THEN
		// @formatter:off
		and.then(status)
			.as("Invalid argument status")
			.isEqualTo(1)
			;
		and.then(err.toString())
			.as("Argument error and hint printed")
			.isEqualTo("Invalid value 300 for the -a option: must be between 0 and 255." + NL
					+ "Use -h for help." + NL)
			;
		and.then(out.toString())
			.as("Nothing printed to output")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void run_help() {
		// GIVEN
		StringWriter err = new StringWriter();

		// WHEN
		int status = run(out, err, "", "-h");

		// THEN
		// @formatter:off
		and.then(status)
			.as("Success status")
			.isZero()
			;
		and.then(out.toString())
			.as("Usage printed")
			.isEqualTo(net.solarnetwork.sunspec.shell.ShellOptions.USAGE)
			;
		// @formatter:on
	}

	@Test
	public void run_version() {
		// GIVEN
		StringWriter err = new StringWriter();

		// WHEN
		int status = run(out, err, "", "-V");

		// THEN
		// @formatter:off
		and.then(status)
			.as("Success status")
			.isZero()
			;
		and.then(out.toString())
			.as("Version from build printed")
			.matches("sunspec-shell \\d+\\.\\d+\\.\\d+.*" + NL)
			;
		// @formatter:on
	}

}
