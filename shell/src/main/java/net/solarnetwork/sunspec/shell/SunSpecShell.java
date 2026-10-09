/* ==================================================================
 * SunSpecShell.java - 7/10/2026 7:56:42 pm
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

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.solarnetwork.sunspec.shell.ModelTables.modelListTable;
import static net.solarnetwork.sunspec.shell.ModelTables.modelTitle;
import static net.solarnetwork.sunspec.shell.ModelTables.pointTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import io.netty.channel.EventLoopGroup;
import net.solarnetwork.io.modbus.netty.channel.LocalIoEventLoopGroupFactory;
import net.solarnetwork.io.modbus.netty.channel.MultiThreadIoEventLoopGroupFactory;
import net.solarnetwork.io.modbus.netty.handler.NettyModbusClient;
import net.solarnetwork.io.modbus.rtu.jsc.JscSerialPortProvider;
import net.solarnetwork.io.modbus.rtu.netty.NettyRtuModbusClientConfig;
import net.solarnetwork.io.modbus.rtu.netty.RtuNettyModbusClient;
import net.solarnetwork.io.modbus.tcp.netty.NettyTcpModbusClientConfig;
import net.solarnetwork.io.modbus.tcp.netty.TcpNettyModbusClient;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.core.GenericModelAccessor;
import net.solarnetwork.sunspec.core.ModelDataFactory;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.nifty.NiftyModbusConnection;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Interactive shell for SunSpec devices.
 *
 * <p>
 * The {@link #main(String...)} method connects to a device, as configured by
 * {@link ShellOptions}, discovers its SunSpec models, prints a summary of them,
 * and then reads commands until the {@code exit} command or the end of input.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public class SunSpecShell {

	/** The command prompt. */
	public static final String PROMPT = "SnnS> ";

	/** The command help text. */
	// @formatter:off
	public static final String HELP = """
			Commands:
			  help, ?             Print this list of commands
			  model-list          Print the available models
			  model-view <num>    Print the point values of model <num>
			  exit, quit          Exit the shell
			""";
	// @formatter:on

	private static final Pattern WHITESPACE = Pattern.compile("\\s+");

	private final ModbusConnection conn;
	private final ModelData data;
	private final BufferedReader in;
	private final PrintWriter out;

	/**
	 * Constructor.
	 *
	 * @param conn
	 *        the connection to read model data with
	 * @param data
	 *        the discovered model data
	 * @param in
	 *        the command input
	 * @param out
	 *        the output
	 */
	public SunSpecShell(ModbusConnection conn, ModelData data, BufferedReader in, PrintWriter out) {
		super();
		this.conn = conn;
		this.data = data;
		this.in = in;
		this.out = out;
	}

	/**
	 * Get all available models.
	 *
	 * @return the common model followed by the other discovered models
	 */
	public List<ModelAccessor> getModels() {
		final List<ModelAccessor> models = data.getModels();
		final List<ModelAccessor> result = new ArrayList<>(models.size() + 1);
		result.add(data);
		result.addAll(models);
		return result;
	}

	/**
	 * Print the common model points, followed by the available models.
	 */
	public void printSummary() {
		out.println(pointTable(modelTitle(data.getModelId()), data));
		out.println();
		out.println(modelListTable(getModels()));
		out.println();
		out.println("Enter help for the available commands.");
		out.flush();
	}

	/**
	 * Read and execute commands, until the {@code exit} command or the end of
	 * input.
	 *
	 * @throws IOException
	 *         if the input cannot be read
	 */
	public void run() throws IOException {
		while ( true ) {
			out.print(PROMPT);
			out.flush();
			final String line = in.readLine();
			if ( line == null ) {
				out.println();
				out.flush();
				return;
			}
			final String[] args = WHITESPACE.split(line.trim());
			if ( args[0].isEmpty() ) {
				continue;
			}
			if ( !execute(args) ) {
				return;
			}
		}
	}

	/**
	 * Execute a command.
	 *
	 * <p>
	 * An unexpected error, such as from interpreting device data, is printed
	 * rather than thrown, so the shell can carry on.
	 * </p>
	 *
	 * @param args
	 *        the command name followed by its arguments
	 * @return {@code false} if the command is to exit the shell
	 */
	public boolean execute(String... args) {
		try {
			switch (args[0].toLowerCase(Locale.ENGLISH)) {
				case "help", "?" -> out.print(HELP);
				case "model-list" -> out.println(modelListTable(getModels()));
				case "model-view" -> viewModel(args);
				case "exit", "quit" -> {
					return false;
				}
				default -> out.printf("Unknown command [%s]; enter help for the available commands.%n",
						args[0]);
			}
		} catch ( RuntimeException e ) {
			out.printf("Error running command [%s]: %s%n", args[0], e);
		}
		out.flush();
		return true;
	}

	private void viewModel(String[] args) {
		if ( args.length < 2 ) {
			return;
		}
		final int modelId;
		try {
			modelId = Integer.parseInt(args[1]);
		} catch ( NumberFormatException e ) {
			return;
		}
		final List<ModelAccessor> models = getModels().stream()
				.filter(m -> m.getModelId().getId() == modelId).toList();
		if ( models.isEmpty() ) {
			out.printf("Model %d is not available.%n", modelId);
			return;
		}
		if ( models.get(0) instanceof GenericModelAccessor ) {
			// there is no accessor for this model ID
			out.printf("The points of model %d are not supported.%n", modelId);
			return;
		}
		try {
			data.readModelData(conn, models);
		} catch ( IOException e ) {
			out.printf("Error reading model %d: %s%n", modelId, e.getMessage());
			return;
		}
		for ( int i = 0, len = models.size(); i < len; i++ ) {
			final ModelAccessor model = models.get(i);
			String title = modelTitle(model.getModelId());
			if ( len > 1 ) {
				title += String.format(" (%d of %d)", i + 1, len);
			}
			out.println(pointTable(title, model));
		}
	}

	/**
	 * Get the application version.
	 *
	 * @return the version
	 */
	public static String version() {
		final Properties props = new Properties();
		try (InputStream in = SunSpecShell.class.getResourceAsStream("shell.properties")) {
			if ( in != null ) {
				props.load(in);
			}
		} catch ( IOException e ) {
			// ignore
		}
		return props.getProperty("version", "unknown");
	}

	/**
	 * Configure logging.
	 *
	 * <p>
	 * This must be called before any logger is created. Only errors are logged,
	 * unless {@code verbose} is {@code true}. Any logging system property that
	 * is already set, such as from a {@code -D} argument, is not changed.
	 * </p>
	 *
	 * @param verbose
	 *        {@code true} to log Modbus messages and debug information
	 */
	private static void configureLogging(boolean verbose) {
		final String prefix = "org.slf4j.simpleLogger.";
		setPropertyIfMissing(prefix + "defaultLogLevel", verbose ? "info" : "error");
		setPropertyIfMissing(prefix + "showDateTime", "true");
		setPropertyIfMissing(prefix + "dateTimeFormat", "HH:mm:ss.SSS");
		setPropertyIfMissing(prefix + "showThreadName", "false");
		if ( verbose ) {
			// Nifty Modbus logs messages at TRACE
			setPropertyIfMissing(prefix + "log.net.solarnetwork.io.modbus", "trace");
			setPropertyIfMissing(prefix + "log.net.solarnetwork.sunspec", "debug");
		}
	}

	private static void setPropertyIfMissing(String key, String value) {
		if ( System.getProperty(key) == null ) {
			System.setProperty(key, value);
		}
	}

	private static NettyModbusClient<?> createClient(ShellOptions opts, String device,
			EventLoopGroup eventLoopGroup) {
		final NettyModbusClient<?> client;
		if ( opts.getMode() == ShellOptions.Mode.Rtu ) {
			final NettyRtuModbusClientConfig config = new NettyRtuModbusClientConfig(device,
					opts.getSerialParameters());
			config.setAutoReconnect(false);
			client = new RtuNettyModbusClient(config, eventLoopGroup, new JscSerialPortProvider());
		} else {
			final NettyTcpModbusClientConfig config = new NettyTcpModbusClientConfig(device,
					opts.getPort());
			config.setAutoReconnect(false);
			client = new TcpNettyModbusClient(config, eventLoopGroup, null);
		}
		client.setReplyTimeout(opts.getTimeout().toMillis());
		client.setWireLogging(opts.isVerbose());
		return client;
	}

	/**
	 * Run the shell.
	 *
	 * @param args
	 *        the command-line arguments
	 * @param in
	 *        the command input
	 * @param out
	 *        the output
	 * @param err
	 *        the error output
	 * @return the exit status: {@literal 0} for success, {@literal 1} for an
	 *         invalid argument, or {@literal 2} if the device could not be
	 *         connected to or its SunSpec models discovered
	 */
	public static int run(String[] args, BufferedReader in, PrintWriter out, PrintWriter err) {
		final ShellOptions opts;
		try {
			opts = ShellOptions.parse(args);
		} catch ( IllegalArgumentException e ) {
			err.println(e.getMessage());
			err.println("Use -h for help.");
			err.flush();
			return 1;
		}
		if ( opts.isHelp() ) {
			out.print(ShellOptions.USAGE);
			out.flush();
			return 0;
		}
		if ( opts.isVersion() ) {
			out.println("sunspec-shell " + version());
			out.flush();
			return 0;
		}
		final String device = opts.getDevice();
		if ( device == null ) {
			return 1;
		}

		configureLogging(opts.isVerbose());

		// the client would stop its own event loop group only after a 2 second quiet period
		final EventLoopGroup eventLoopGroup = (opts.getMode() == ShellOptions.Mode.Rtu
				? LocalIoEventLoopGroupFactory.INSTANCE
				: MultiThreadIoEventLoopGroupFactory.INSTANCE).apply(opts, false);
		try (NiftyModbusConnection conn = new NiftyModbusConnection(
				createClient(opts, device, eventLoopGroup), opts.getUnitId())) {
			final ModelData data;
			try {
				conn.open();
				out.printf("Connected to %s.%n", conn.getDescription());
				out.flush();
				final ModelDataFactory factory = ModelDataFactory.getInstance();
				final Integer baseAddress = opts.getBaseAddress();
				data = (baseAddress != null
						? factory.discoverModels(conn, opts.getMaxReadCount(), baseAddress)
						: factory.discoverModels(conn, opts.getMaxReadCount()));
			} catch ( IOException e ) {
				err.println(e.getMessage());
				err.flush();
				return 2;
			}
			out.printf("Found SunSpec data at address %d.%n%n", data.getBaseAddress() - 2);
			final SunSpecShell shell = new SunSpecShell(conn, data, in, out);
			shell.printSummary();
			try {
				shell.run();
			} catch ( IOException e ) {
				err.println("Error reading input: " + e.getMessage());
				err.flush();
			}
		} finally {
			eventLoopGroup.shutdownGracefully(0, 1, TimeUnit.SECONDS).awaitUninterruptibly(2,
					TimeUnit.SECONDS);
		}
		return 0;
	}

	/**
	 * Command-line entry point.
	 *
	 * @param args
	 *        the arguments, as described in {@link ShellOptions#USAGE}
	 */
	public static void main(String... args) {
		final int status = run(args, new BufferedReader(new InputStreamReader(System.in, UTF_8)),
				new PrintWriter(new OutputStreamWriter(System.out, UTF_8)),
				new PrintWriter(new OutputStreamWriter(System.err, UTF_8)));
		System.exit(status);
	}

}
