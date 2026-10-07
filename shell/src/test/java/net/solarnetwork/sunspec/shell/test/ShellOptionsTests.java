/* ==================================================================
 * ShellOptionsTests.java - 7/10/2026 9:12:40 pm
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

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import net.solarnetwork.io.modbus.serial.SerialParameters;
import net.solarnetwork.io.modbus.serial.SerialParity;
import net.solarnetwork.io.modbus.serial.SerialStopBits;
import net.solarnetwork.sunspec.shell.ShellOptions;
import net.solarnetwork.sunspec.shell.ShellOptions.Mode;

/**
 * Test cases for the {@link ShellOptions} class.
 *
 * @author matt
 * @version 1.0
 */
public class ShellOptionsTests {

	@Test
	public void defaults() {
		// WHEN
		ShellOptions opts = ShellOptions.parse("192.168.1.10");

		// THEN
		// @formatter:off
		then(opts)
			.as("Host is device")
			.returns("192.168.1.10", from(ShellOptions::getDevice))
			.as("Host is TCP")
			.returns(Mode.Tcp, from(ShellOptions::getMode))
			.as("Default unit ID")
			.returns(1, from(ShellOptions::getUnitId))
			.as("Default port")
			.returns(502, from(ShellOptions::getPort))
			.as("Base address is discovered")
			.returns(null, from(ShellOptions::getBaseAddress))
			.as("Default max read count")
			.returns(100, from(ShellOptions::getMaxReadCount))
			.as("Default timeout")
			.returns(Duration.ofSeconds(1), from(ShellOptions::getTimeout))
			.as("Not verbose")
			.returns(false, from(ShellOptions::isVerbose))
			.as("Not help")
			.returns(false, from(ShellOptions::isHelp))
			.as("Not version")
			.returns(false, from(ShellOptions::isVersion))
			;
		then(opts.getSerialParameters())
			.as("Default baud like mbpoll")
			.returns(19200, from(SerialParameters::getBaudRate))
			.as("Default data bits")
			.returns(8, from(SerialParameters::getDataBits))
			.as("Default parity like mbpoll")
			.returns(SerialParity.Even, from(SerialParameters::getParity))
			.as("Default stop bits")
			.returns(SerialStopBits.One, from(SerialParameters::getStopBits))
			.as("RS-485 not enabled")
			.returns(null, from(SerialParameters::getRs485ModeEnabled))
			;
		// @formatter:on
	}

	@Test
	public void tcpOptions() {
		// WHEN
		ShellOptions opts = ShellOptions.parse("-a", "3", "-p", "5020", "-o", "2.5", "-c", "50", "-v",
				"host.example.com");

		// THEN
		// @formatter:off
		then(opts)
			.as("Host is device")
			.returns("host.example.com", from(ShellOptions::getDevice))
			.as("Host is TCP")
			.returns(Mode.Tcp, from(ShellOptions::getMode))
			.as("Unit ID from -a")
			.returns(3, from(ShellOptions::getUnitId))
			.as("Port from -p")
			.returns(5020, from(ShellOptions::getPort))
			.as("Timeout from -o seconds")
			.returns(Duration.ofMillis(2500), from(ShellOptions::getTimeout))
			.as("Max read count from -c")
			.returns(50, from(ShellOptions::getMaxReadCount))
			.as("Verbose from -v")
			.returns(true, from(ShellOptions::isVerbose))
			;
		// @formatter:on
	}

	@Test
	public void attachedValues() {
		// WHEN
		ShellOptions opts = ShellOptions.parse("-a3", "-p5020", "-mrtu", "-b9600", "host");

		// THEN
		// @formatter:off
		then(opts)
			.as("Unit ID from -a")
			.returns(3, from(ShellOptions::getUnitId))
			.as("Port from -p")
			.returns(5020, from(ShellOptions::getPort))
			.as("Mode from -m")
			.returns(Mode.Rtu, from(ShellOptions::getMode))
			;
		then(opts.getSerialParameters())
			.as("Baud from -b")
			.returns(9600, from(SerialParameters::getBaudRate))
			;
		// @formatter:on
	}

	@Test
	public void serialOptions() {
		// WHEN
		ShellOptions opts = ShellOptions.parse("-b", "9600", "-d", "7", "-P", "odd", "-s", "2",
				"/dev/ttyUSB0");

		// THEN
		// @formatter:off
		then(opts)
			.as("Serial port is device")
			.returns("/dev/ttyUSB0", from(ShellOptions::getDevice))
			.as("Serial port is RTU")
			.returns(Mode.Rtu, from(ShellOptions::getMode))
			;
		then(opts.getSerialParameters())
			.as("Baud from -b")
			.returns(9600, from(SerialParameters::getBaudRate))
			.as("Data bits from -d")
			.returns(7, from(SerialParameters::getDataBits))
			.as("Parity from -P")
			.returns(SerialParity.Odd, from(SerialParameters::getParity))
			.as("Stop bits from -s")
			.returns(SerialStopBits.Two, from(SerialParameters::getStopBits))
			;
		// @formatter:on
	}

	@ParameterizedTest
	@CsvSource({ "8N1, 8, None, One", "8e2, 8, Even, Two", "7O1, 7, Odd, One" })
	public void bits(String bits, int dataBits, SerialParity parity, SerialStopBits stopBits) {
		// WHEN
		ShellOptions opts = ShellOptions.parse("--bits", bits, "/dev/ttyUSB0");

		// THEN
		// @formatter:off
		then(opts.getSerialParameters())
			.as("Data bits from --bits")
			.returns(dataBits, from(SerialParameters::getDataBits))
			.as("Parity from --bits")
			.returns(parity, from(SerialParameters::getParity))
			.as("Stop bits from --bits")
			.returns(stopBits, from(SerialParameters::getStopBits))
			;
		// @formatter:on
	}

	@Test
	public void bits_laterOptionWins() {
		// WHEN
		ShellOptions opts = ShellOptions.parse("--bits", "8N1", "-P", "even", "/dev/ttyUSB0");

		// THEN
		// @formatter:off
		then(opts.getSerialParameters())
			.as("Parity from -P after --bits")
			.returns(SerialParity.Even, from(SerialParameters::getParity))
			.as("Data bits from --bits")
			.returns(8, from(SerialParameters::getDataBits))
			;
		// @formatter:on
	}

	@ParameterizedTest
	@CsvSource({ "-R, false", "-F, true" })
	public void rs485(String opt, boolean rtsHigh) {
		// WHEN
		ShellOptions opts = ShellOptions.parse(opt, "/dev/ttyUSB0");

		// THEN
		// @formatter:off
		then(opts.getSerialParameters())
			.as("RS-485 enabled")
			.returns(true, from(SerialParameters::getRs485ModeEnabled))
			.as("RTS level when sending")
			.returns(rtsHigh, from(SerialParameters::isRs485RtsHighEnabled))
			;
		// @formatter:on
	}

	@ParameterizedTest
	@CsvSource({ "/dev/ttyUSB0, Rtu", "/dev/tty.usbserial-FTYS9FWO, Rtu", "COM3, Rtu", "com12, Rtu",
			"\\\\.\\COM10, Rtu", "192.168.1.10, Tcp", "localhost, Tcp", "computer.local, Tcp" })
	public void modeFromDevice(String device, Mode mode) {
		// WHEN
		ShellOptions opts = ShellOptions.parse(device);

		// THEN
		// @formatter:off
		then(opts)
			.as("Mode detected from device")
			.returns(mode, from(ShellOptions::getMode))
			;
		// @formatter:on
	}

	@Test
	public void modeOption() {
		// WHEN
		ShellOptions tcp = ShellOptions.parse("-m", "TCP", "/dev/ttyUSB0");
		ShellOptions rtu = ShellOptions.parse("-m", "rtu", "serial-port");

		// THEN
		// @formatter:off
		then(tcp)
			.as("Mode from -m overrides device detection")
			.returns(Mode.Tcp, from(ShellOptions::getMode))
			;
		then(rtu)
			.as("Mode from -m overrides device detection")
			.returns(Mode.Rtu, from(ShellOptions::getMode))
			;
		// @formatter:on
	}

	@ParameterizedTest
	@CsvSource({ "40001, false, 40000", "1, false, 0", "65536, false, 65535", "40000, true, 40000",
			"0, true, 0" })
	public void reference(int reference, boolean zeroBased, int address) {
		// WHEN
		ShellOptions opts = (zeroBased
				? ShellOptions.parse("-r", String.valueOf(reference), "-0", "host")
				: ShellOptions.parse("-r", String.valueOf(reference), "host"));

		// THEN
		// @formatter:off
		then(opts)
			.as("Base address from reference, 1-based unless -0 given")
			.returns(address, from(ShellOptions::getBaseAddress))
			;
		// @formatter:on
	}

	@Test
	public void helpWithoutDevice() {
		// WHEN
		ShellOptions opts = ShellOptions.parse("-h");

		// THEN
		// @formatter:off
		then(opts)
			.as("Help from -h")
			.returns(true, from(ShellOptions::isHelp))
			.as("No device")
			.returns(null, from(ShellOptions::getDevice))
			;
		// @formatter:on
	}

	@Test
	public void helpLongOption() {
		// WHEN
		ShellOptions opts = ShellOptions.parse("--help");

		// THEN
		// @formatter:off
		then(opts)
			.as("Help from --help")
			.returns(true, from(ShellOptions::isHelp))
			;
		// @formatter:on
	}

	@Test
	public void versionWithoutDevice() {
		// WHEN
		ShellOptions opts = ShellOptions.parse("-V");

		// THEN
		// @formatter:off
		then(opts)
			.as("Version from -V")
			.returns(true, from(ShellOptions::isVersion))
			;
		// @formatter:on
	}

	@Test
	public void missingDevice() {
		// @formatter:off
		thenThrownBy(() -> ShellOptions.parse("-a", "1"))
			.as("Device is required")
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("device or host")
			;
		// @formatter:on
	}

	@Test
	public void extraArgument() {
		// @formatter:off
		thenThrownBy(() -> ShellOptions.parse("host", "other"))
			.as("Only one device allowed")
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("[other]")
			;
		// @formatter:on
	}

	@Test
	public void unknownOption() {
		// @formatter:off
		thenThrownBy(() -> ShellOptions.parse("-x", "host"))
			.as("Unknown option rejected")
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("[-x]")
			;
		// @formatter:on
	}

	@Test
	public void missingValue() {
		// @formatter:off
		thenThrownBy(() -> ShellOptions.parse("host", "-a"))
			.as("Option value required")
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("-a")
			;
		// @formatter:on
	}

	@ParameterizedTest
	@CsvSource({ "-a, 256", "-a, x", "-p, 0", "-c, 126", "-c, 0", "-d, 6", "-s, 3", "-P, mark",
			"-m, udp", "-o, 0", "-o, x", "-r, 0", "--bits, 8X1", "--bits, 9N1" })
	public void invalidValue(String opt, String value) {
		// @formatter:off
		thenThrownBy(() -> ShellOptions.parse(opt, value, "host"))
			.as("Invalid value for %s rejected", opt)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining(value)
			;
		// @formatter:on
	}

}
