# Nifty SunSpec: Interactive Shell

This project provides a command-line interactive shell application for interrogating SunSpec
devices, over Modbus TCP or Modbus RTU (serial). It uses [Nifty Modbus][nifty-modbus] for Modbus
communication, with [jSerialComm][jsc] for serial ports, and can be compiled into a native
executable with [GraalVM][graalvm].

When the shell starts, it connects to the device, finds the SunSpec `SunS` marker, prints the
common model points, and lists the available models:

```
$ sunspec-shell -a 1 192.168.1.10
Connected to 192.168.1.10:502 unit 1.
Found SunSpec data at address 40000.

╔══════════════════════════════════╗
║ Model 1: Common model            ║
╠═══════════════╤══════════════════╣
║ Point         │            Value ║
╠═══════════════╪══════════════════╣
║ manufacturer  │    OutBack Power ║
║ model         │        OGHI8048A ║
║ options       │        prototype ║
║ version       │      1.0.20.3812 ║
║ serialNumber  │ OGHI2232F0100079 ║
║ deviceAddress │              255 ║
╚═══════════════╧══════════════════╝

╔═══════╤══════════════════════════════════╗
║ Model │ Name                             ║
╠═══════╪══════════════════════════════════╣
║     1 │ Common model                     ║
║   202 │ Split single phase (A-B-N) meter ║
║   701 │ DER AC measurement               ║
║   702 │ DER capacity                     ║
║   ... │ ...                              ║
║ 64122 │ Unknown                          ║
╚═══════╧══════════════════════════════════╝

Enter help for the available commands.
SnnS>
```

The shell then reads commands at the `SnnS>` prompt, until the `exit` command or the end of input.

# Commands

| Command | Description |
|:--------|:------------|
| `help`, `?` | Print the list of commands. |
| `model-list` | Print the available models. |
| `model-view <num>` | Read the points of model `<num>` from the device, and print their values. |
| `exit`, `quit` | Exit the shell. |

The `model-view` command reads the latest data from the device each time, and prints the points
returned by the model's `toPointMap()` method, with their measurement units. The points of
repeating blocks and curves have a suffix with their instance number, such as `moduleDcPower_2`
for the second MPPT module. If a device has more than one instance of a model, each is printed.

```
SnnS> model-view 160
╔═══════════════════════════════════════════════════╗
║ Model 160: Multiple MPPT Inverter Extension Model ║
╠═══════════════════╤═══════════════════════════════╣
║ Point             │                         Value ║
╠═══════════════════╪═══════════════════════════════╣
║ events            │                        (none) ║
║ moduleCount       │                             2 ║
║ moduleInputId_1   │                             1 ║
║ moduleDcCurrent_1 │                         3.3 A ║
║ moduleDcVoltage_1 │                         602 V ║
║ moduleDcPower_1   │                        1960 W ║
║ moduleEvents_1    │                        (none) ║
║ moduleInputId_2   │                             2 ║
║ moduleDcCurrent_2 │                         6.2 A ║
║ moduleDcVoltage_2 │                         396 V ║
║ moduleDcPower_2   │                        2460 W ║
║ moduleEvents_2    │                        (none) ║
╚═══════════════════╧═══════════════════════════════╝
```

Models without a SunSpec accessor in this library, such as vendor models, are listed, but their
points cannot be printed.

# Application arguments

The arguments are modelled on those of the [`mbpoll`][mbpoll] Modbus tool:

```
sunspec-shell [options] device|host
```

Give a serial port, such as `/dev/ttyUSB0` or `COM1`, for Modbus RTU, or a host name or IP address
for Modbus TCP. Option values can be given as a separate argument, like `-b 9600`, or attached to
the option, like `-b9600`.

| Option | Default | Description |
|:-------|:--------|:------------|
| `-m #` | | The mode, `rtu` or `tcp`. The default is `rtu` for a device that starts with `/dev/` or `COM`, otherwise `tcp`. |
| `-a #` | `1` | The unit ID (slave address) of the device, from 0 to 255. |
| `-r #` | | The reference of the SunSpec base address, where the `SunS` marker starts. References start at 1 unless `-0` is given, so `-r 40001` is address 40000. By default the standard SunSpec base addresses 40000, 50000, and 0 are tried. |
| `-0` | | References start at 0 (PDU addressing) instead of 1. |
| `-c #` | `100` | The maximum number of registers to read in one request, from 1 to 125. |
| `-o #` | `1.00` | The response timeout, in seconds. |
| `-v` | | Verbose mode: log Modbus messages and debug information. See [below](#logging). |
| `-h`, `--help` | | Print the help and exit. |
| `-V` | | Print the version and exit. |

Modbus TCP options:

| Option | Default | Description |
|:-------|:--------|:------------|
| `-p #` | `502` | The TCP port. |

Modbus RTU options:

| Option | Default | Description |
|:-------|:--------|:------------|
| `-b #` | `19200` | The baud rate. |
| `-d #` | `8` | The data bits, `7` or `8`. |
| `-s #` | `1` | The stop bits, `1` or `2`. |
| `-P #` | `even` | The parity, `none`, `even`, or `odd`. |
| `--bits #` | `8E1` | The data bits, parity, and stop bits together, for example `8N1`. |
| `-R` | | RS-485 mode, with RTS low when sending. |
| `-F` | | RS-485 mode, with RTS high when sending. |

For example:

```sh
# Modbus TCP, unit 3 on port 5020
sunspec-shell -a 3 -p 5020 192.168.1.10

# Modbus RTU at 9600 baud, no parity
sunspec-shell -a 1 -b 9600 --bits 8N1 /dev/ttyUSB0
```

The exit status is `1` for an invalid argument, and `2` if the device cannot be connected to or
no SunSpec data is found.

# Running

To run the shell from Gradle:

```sh
../gradlew -q --console=plain run --args='-a 1 192.168.1.10'
```

The `installDist` task builds a distribution with a `bin/sunspec-shell` start script under
`build/install/sunspec-shell`.

## Native executable

To compile a native executable with GraalVM, point `GRAALVM_HOME` to a GraalVM for JDK 25
installation and run the `nativeCompile` task:

```sh
GRAALVM_HOME=/path/to/graalvm ../gradlew nativeCompile
```

The executable is written to `build/native/nativeCompile/sunspec-shell`. It is self-contained:
the `libmanagement_ext.so` library written beside it is not needed. The native image metadata
for jSerialComm and Nifty Modbus is in
`src/main/resources/META-INF/native-image/net.solarnetwork.common/nifty-sunspec-shell`, and the
SunSpec API and core bundles include their own metadata.

## Simulated device

To try the shell without a device, the `runSimulator` task serves a SunSpec register dump from
the test resources, over Modbus TCP or RTU. Its arguments are a register dump file or test
resource name, followed by the same options as the shell, where the host is the address to
listen on. For example, to simulate an OutBack device with the DER models on port 5020:

```sh
../gradlew -q --console=plain runSimulator --args='test-data-der-01.txt -p 5020 127.0.0.1'
```

and then connect to it with:

```sh
../gradlew -q --console=plain run --args='-p 5020 127.0.0.1'
```

The `test-data-103-05.txt` resource simulates an SMA inverter. For RTU, give a serial port, for
example one end of a pair of virtual serial ports created with `socat`.

# Logging

Logging is handled via SLF4J, with the SLF4J simple logger. Only errors are logged, unless the
`-v` option is given, which logs debug information, and Modbus messages as hex dumps:

```
19:00:32.929 DEBUG net.solarnetwork.sunspec.modbus.support.ModelData - Reading modbus 1 range 40004-40020 (16)
19:00:32.929 TRACE net.solarnetwork.io.modbus.127.0.0.1:5020 - [id: 0x447fe0ff, L:/127.0.0.1:35186 - R:/127.0.0.1:5020] WRITE: 12B
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 00 03 00 00 00 06 01 03 9c 44 00 10             |.........D..    |
+--------+-------------------------------------------------+----------------+
```

The [simple logger properties][slf4j-simple] can be set with `-D` system properties, for example
`-Dorg.slf4j.simpleLogger.defaultLogLevel=warn`. For the start script, set them in the
`JAVA_OPTS` environment variable.

[graalvm]: https://www.graalvm.org/
[jsc]: https://fazecast.github.io/jSerialComm/
[mbpoll]: https://github.com/epsilonrt/mbpoll
[nifty-modbus]: https://github.com/SolarNetwork/nifty-modbus
[slf4j-simple]: https://www.slf4j.org/api/org/slf4j/simple/SimpleLogger.html
