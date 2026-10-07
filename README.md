# Nifty SunSpec

Nifty SunSpec is a delightful little SunSpec client library written in Java. It aims to be easy to
use, reliable, and cover the most common SunSpec use cases around integrating with
SunSpec-compatible devices. It requires a minimum of Java 17 to use.

This code was spun out of the [SolarNode](https://github.com/SolarNetwork/solarnetwork-node)
project after many years of happy use collecting data from many, many SunSpec devices over many,
many, many moons.


# Modbus connection

This library does not provide a modbus client library. Instead it defines a core `ModbusConnection`
interface, and you must provide an implementation of that API at runtime.


# Maven Central Repository coordinates

Nifty SunSpec can be integrated into your project using the following coordinates,
all of which use the `net.solarnetwork.common` Group identifier:

| Artifact | Notes |
|:---------|:------|
| `nifty-sunspec-api` | The high-level interfaces. |
| `nifty-sunspec-core` | The core implementation. |

Typically it is sufficient to declare just the `nifty-sunspec-core` in your project. For example in
a Gradle project:

```gradle
dependencies {
	implementation 'net.solarnetwork.common:nifty-sunspec-core:1.0.0'
}
```


# Building from source

To build Nifty SunSpec yourself, clone or download this repository. **Note** that Java 25+
is required for building. Then:

```sh
# Linux/macOS/etc
./gradlew build -x test

# Or Windows
.\gradlew.bat build -x test
```

The component artifacts will be created within the `build/libs` directory of each component:

 * `api/build/libs/nifty-sunspec-api-X.Y.Z.jar`
 * `core/build/libs/nifty-sunspec-core-X.Y.Z.jar`
