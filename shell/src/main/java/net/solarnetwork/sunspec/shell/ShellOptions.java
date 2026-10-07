/* ==================================================================
 * ShellOptions.java - 7/10/2026 7:04:51 pm
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

import java.time.Duration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.io.modbus.serial.BasicSerialParameters;
import net.solarnetwork.io.modbus.serial.SerialParity;
import net.solarnetwork.io.modbus.serial.SerialStopBits;
import net.solarnetwork.sunspec.core.ModelDataFactory;

/**
 * Command-line options for the SunSpec shell.
 *
 * <p>
 * The options are modelled on those of the {@code mbpoll} Modbus tool, such as
 * {@code -a} for the unit ID and {@code -b} for the baud rate. Option values
 * can be given as a separate argument, like {@code -b 9600}, or attached to the
 * option, like {@code -b9600}.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public final class ShellOptions {

	/** The Modbus connection modes. */
	public enum Mode {
		/** Modbus RTU, over a serial port. */
		Rtu,

		/** Modbus TCP. */
		Tcp,
	}

	/** The default unit ID. */
	public static final int DEFAULT_UNIT_ID = 1;

	/** The default TCP port. */
	public static final int DEFAULT_PORT = 502;

	/** The default serial baud rate. */
	public static final int DEFAULT_BAUD_RATE = 19200;

	/** The default serial data bits. */
	public static final int DEFAULT_DATA_BITS = 8;

	/** The default serial stop bits. */
	public static final SerialStopBits DEFAULT_STOP_BITS = SerialStopBits.One;

	/** The default serial parity. */
	public static final SerialParity DEFAULT_PARITY = SerialParity.Even;

	/** The default response timeout. */
	public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(1);

	/** The maximum number of registers Modbus allows in one read request. */
	public static final int MAX_READ_COUNT = 125;

	/** The command-line usage text. */
	// @formatter:off
	public static final String USAGE = """
			usage: sunspec-shell [options] device|host

			Interactive shell for SunSpec devices, connected by serial (Modbus RTU) or TCP.

			Arguments:
			  device        Serial port, when using Modbus RTU, for example /dev/ttyUSB0
			                or COM1
			  host          Host name or IP address, when using Modbus TCP

			General options:
			  -m #          Mode (rtu or tcp); rtu is the default for a device that starts
			                with /dev/ or COM, otherwise tcp
			  -a #          Unit ID (slave address) (0-255, 1 is default)
			  -r #          Reference of the SunSpec base address, where the SunS marker
			                starts; the default is to look at the standard SunSpec base
			                addresses 40000, 50000, and 0 (references 40001, 50001,
			                and 1)
			  -0            First reference is 0 (PDU addressing) instead of 1
			  -c #          Maximum number of registers to read in one request (1-125,
			                100 is default)
			  -o #          Response timeout in seconds (1.00 s is default)
			  -v            Verbose mode: log Modbus messages and debug information
			  -h, --help    Print this help and exit
			  -V            Print the version and exit

			Options for Modbus TCP:
			  -p #          TCP port number (502 is default)

			Options for Modbus RTU:
			  -b #          Baud rate (19200 is default)
			  -d #          Data bits (7 or 8, 8 is default)
			  -s #          Stop bits (1 or 2, 1 is default)
			  -P #          Parity (none, even, or odd, even is default)
			  --bits #      Data bits, parity, and stop bits together, for example 8N1
			  -R            RS-485 mode (RTS low when sending)
			  -F            RS-485 mode (RTS high when sending)
			""";
	// @formatter:on

	private static final String FLAG_OPTIONS = "0FhRVv";

	private static final String VALUE_OPTIONS = "Pabcdmoprs";

	private static final Pattern BITS_REGEX = Pattern.compile("([78])([EON])([12])",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern SERIAL_DEVICE_REGEX = Pattern
			.compile("(?:/dev/.*|(?:\\\\\\\\\\.\\\\)?COM\\d+)", Pattern.CASE_INSENSITIVE);

	private @Nullable Mode mode;
	private @Nullable String device;
	private int unitId = DEFAULT_UNIT_ID;
	private @Nullable Integer reference;
	private boolean zeroBasedReference;
	private int maxReadCount = ModelDataFactory.DEFAULT_MAX_READ_WORDS_COUNT;
	private Duration timeout = DEFAULT_TIMEOUT;
	private int port = DEFAULT_PORT;
	private final BasicSerialParameters serialParameters;
	private boolean verbose;
	private boolean help;
	private boolean version;

	private ShellOptions() {
		super();
		serialParameters = new BasicSerialParameters();
		serialParameters.setBaudRate(DEFAULT_BAUD_RATE);
		serialParameters.setDataBits(DEFAULT_DATA_BITS);
		serialParameters.setStopBits(DEFAULT_STOP_BITS);
		serialParameters.setParity(DEFAULT_PARITY);
	}

	/**
	 * Parse command-line arguments.
	 *
	 * <p>
	 * When the help or version option is given, the other arguments are not
	 * validated, and the device can be omitted.
	 * </p>
	 *
	 * @param args
	 *        the arguments to parse
	 * @return the options
	 * @throws IllegalArgumentException
	 *         if the arguments are not valid
	 */
	public static ShellOptions parse(String... args) {
		final ShellOptions opts = new ShellOptions();
		for ( int i = 0, len = args.length; i < len; i++ ) {
			final String arg = args[i];
			if ( "--help".equals(arg) ) {
				opts.help = true;
			} else if ( "--bits".equals(arg) ) {
				if ( i + 1 >= len ) {
					throw new IllegalArgumentException("Missing value for the --bits option.");
				}
				opts.parseBits(args[++i]);
			} else if ( arg.length() > 1 && arg.charAt(0) == '-' ) {
				final char opt = arg.charAt(1);
				if ( arg.length() == 2 && FLAG_OPTIONS.indexOf(opt) >= 0 ) {
					opts.parseFlag(opt);
				} else if ( VALUE_OPTIONS.indexOf(opt) >= 0 ) {
					final String value;
					if ( arg.length() > 2 ) {
						value = arg.substring(2);
					} else if ( i + 1 < len ) {
						value = args[++i];
					} else {
						throw new IllegalArgumentException(
								String.format("Missing value for the -%c option.", opt));
					}
					opts.parseValue(opt, value);
				} else {
					throw new IllegalArgumentException(String.format("Unknown option [%s].", arg));
				}
			} else if ( opts.device == null ) {
				opts.device = arg;
			} else {
				throw new IllegalArgumentException(String.format("Unexpected argument [%s].", arg));
			}
		}
		if ( opts.help || opts.version ) {
			return opts;
		}
		if ( opts.device == null || opts.device.isBlank() ) {
			throw new IllegalArgumentException("The device or host argument is missing.");
		}
		if ( opts.reference != null ) {
			final int min = (opts.zeroBasedReference ? 0 : 1);
			final int ref = opts.reference;
			if ( ref < min || ref > 0xFFFF + min ) {
				throw new IllegalArgumentException(String.format(
						"Invalid reference %d for the -r option: must be between %d and %d.", ref, min,
						0xFFFF + min));
			}
		}
		return opts;
	}

	private void parseFlag(char opt) {
		switch (opt) {
			case '0' -> zeroBasedReference = true;
			case 'F' -> {
				serialParameters.setRs485ModeEnabled(true);
				serialParameters.setRs485RtsHighEnabled(true);
			}
			case 'R' -> {
				serialParameters.setRs485ModeEnabled(true);
				serialParameters.setRs485RtsHighEnabled(false);
			}
			case 'h' -> help = true;
			case 'V' -> version = true;
			case 'v' -> verbose = true;
			default -> throw new IllegalArgumentException(String.format("Unknown option [-%c].", opt));
		}
	}

	private void parseValue(char opt, String value) {
		switch (opt) {
			case 'a' -> unitId = intValue(opt, value, 0, 255);
			case 'b' -> serialParameters.setBaudRate(intValue(opt, value, 1, Integer.MAX_VALUE));
			case 'c' -> maxReadCount = intValue(opt, value, 1, MAX_READ_COUNT);
			case 'd' -> serialParameters.setDataBits(intValue(opt, value, 7, 8));
			case 'm' -> mode = parseMode(value);
			case 'o' -> timeout = parseTimeout(value);
			case 'P' -> serialParameters.setParity(parseParity(value));
			case 'p' -> port = intValue(opt, value, 1, 0xFFFF);
			case 'r' -> reference = intValue(opt, value, 0, Integer.MAX_VALUE);
			case 's' -> serialParameters.setStopBits(SerialStopBits.forCode(intValue(opt, value, 1, 2)));
			default -> throw new IllegalArgumentException(String.format("Unknown option [-%c].", opt));
		}
	}

	private void parseBits(String value) {
		final Matcher m = BITS_REGEX.matcher(value);
		if ( !m.matches() ) {
			throw new IllegalArgumentException(
					String.format(
							"Invalid value [%s] for the --bits option: must be data bits (7 or 8), "
									+ "parity (N, E, or O), and stop bits (1 or 2), for example 8N1.",
							value));
		}
		serialParameters.setDataBits(Integer.parseInt(m.group(1)));
		serialParameters.setParity(parseParity(m.group(2)));
		serialParameters.setStopBits(SerialStopBits.forCode(Integer.parseInt(m.group(3))));
	}

	private static int intValue(char opt, String value, int min, int max) {
		final int result;
		try {
			result = Integer.parseInt(value);
		} catch ( NumberFormatException e ) {
			throw new IllegalArgumentException(String
					.format("Invalid value [%s] for the -%c option: must be a number.", value, opt));
		}
		if ( result < min || result > max ) {
			throw new IllegalArgumentException(
					String.format("Invalid value %d for the -%c option: must be between %d and %d.",
							result, opt, min, max));
		}
		return result;
	}

	private static Mode parseMode(String value) {
		return switch (value.toLowerCase(Locale.ENGLISH)) {
			case "rtu" -> Mode.Rtu;
			case "tcp" -> Mode.Tcp;
			default -> throw new IllegalArgumentException(
					String.format("Invalid value [%s] for the -m option: must be rtu or tcp.", value));
		};
	}

	private static Duration parseTimeout(String value) {
		final double secs;
		try {
			secs = Double.parseDouble(value);
		} catch ( NumberFormatException e ) {
			throw new IllegalArgumentException(
					String.format("Invalid value [%s] for the -o option: must be a number.", value));
		}
		final long ms = Math.round(secs * 1000.0);
		if ( !(ms > 0) ) {
			throw new IllegalArgumentException(String.format(
					"Invalid value [%s] for the -o option: must be at least 0.001 seconds.", value));
		}
		return Duration.ofMillis(ms);
	}

	private static SerialParity parseParity(String value) {
		return switch (value.toLowerCase(Locale.ENGLISH)) {
			case "n", "none" -> SerialParity.None;
			case "e", "even" -> SerialParity.Even;
			case "o", "odd" -> SerialParity.Odd;
			default -> throw new IllegalArgumentException(String
					.format("Invalid value [%s] for the -P option: must be none, even, or odd.", value));
		};
	}

	/**
	 * Get the connection mode.
	 *
	 * @return the mode given by the {@code -m} option, or else {@link Mode#Rtu}
	 *         if the device looks like a serial port, such as
	 *         {@code /dev/ttyUSB0} or {@code COM1}, or {@link Mode#Tcp}
	 */
	public Mode getMode() {
		if ( mode != null ) {
			return mode;
		}
		final String d = device;
		return (d != null && SERIAL_DEVICE_REGEX.matcher(d).matches() ? Mode.Rtu : Mode.Tcp);
	}

	/**
	 * Get the device (serial port) or host.
	 *
	 * @return the device or host, or {@code null} if only the help or version
	 *         option was given
	 */
	public @Nullable String getDevice() {
		return device;
	}

	/**
	 * Get the unit ID.
	 *
	 * @return the unit ID
	 */
	public int getUnitId() {
		return unitId;
	}

	/**
	 * Get the SunSpec base address.
	 *
	 * @return the 0-based Modbus register address of the {@code SunS} marker
	 *         derived from the {@code -r} and {@code -0} options, or
	 *         {@code null} to look for the marker at the standard addresses
	 */
	public @Nullable Integer getBaseAddress() {
		final Integer ref = reference;
		if ( ref == null ) {
			return null;
		}
		return (zeroBasedReference ? ref : ref - 1);
	}

	/**
	 * Get the maximum number of registers to read in one request.
	 *
	 * @return the maximum count
	 */
	public int getMaxReadCount() {
		return maxReadCount;
	}

	/**
	 * Get the response timeout.
	 *
	 * @return the timeout
	 */
	public Duration getTimeout() {
		return timeout;
	}

	/**
	 * Get the TCP port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Get the serial parameters.
	 *
	 * @return the serial parameters
	 */
	public BasicSerialParameters getSerialParameters() {
		return serialParameters;
	}

	/**
	 * Get the verbose mode.
	 *
	 * @return {@code true} to log Modbus messages and debug information
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Get the help mode.
	 *
	 * @return {@code true} to print the usage text and exit
	 */
	public boolean isHelp() {
		return help;
	}

	/**
	 * Get the version mode.
	 *
	 * @return {@code true} to print the version and exit
	 */
	public boolean isVersion() {
		return version;
	}

}
