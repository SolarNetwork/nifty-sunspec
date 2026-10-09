# Nifty SunSpec

Nifty SunSpec is a delightful little SunSpec client library written in Java. It aims to be easy to
use, reliable, and cover the most common SunSpec use cases around integrating with
SunSpec-compatible devices. It requires a minimum of Java 17 to use.

This code was spun out of the [SolarNode](https://github.com/SolarNetwork/solarnetwork-node)
project after many years of happy use collecting data from many, many SunSpec devices over many,
many, many moons.

The [shell](shell/) project is an interactive command-line application for interrogating SunSpec
devices over Modbus TCP or RTU, which can be compiled into a native executable with GraalVM.

[![API JavaDoc](https://javadoc.io/badge2/net.solarnetwork.common/nifty-sunspec-api/JavaDoc%20API.svg)](https://javadoc.io/doc/net.solarnetwork.common/nifty-sunspec-api)
[![Core JavaDoc](https://javadoc.io/badge2/net.solarnetwork.common/nifty-sunspec-core/JavaDoc%20Core.svg)](https://javadoc.io/doc/net.solarnetwork.common/nifty-sunspec-core)


## Modbus connection

This library does not provide a modbus client library. Instead it defines a core `ModbusConnection`
interface, and you must provide an implementation of that API at runtime. The
[modbus-nifty](./modbus-nifty) project provides a Nifty Modbus implementation you can use, or you
can provide your own.


## Example use

To get started, you use the `ModelDataFactory` to create a `ModelData` instance with the
`discoverModels()` method, followed by `readModelData()` method to load the data for a given model:

```java
// obtain a ModbusConnection somehow
ModbusConnection conn = getModbusConnection();

// then discover the available models as a ModelData object
ModelDataFactory factory = ModelDataFactory.getInstance();
ModelData data = factory.discoverModels(conn);

// look for a specific model type if you know what you need
InverterModelAccessor model = data.findTypedModel(InverterModelAccessor.class);

// load the data for this model (read from the device)
data.readModelData(conn, model);

// use the accessor API to access the data
Float current = model.getCurrent();
Float currentPhaseA = model.accessorForPhase(PhaseA).getCurrent();
Float currentPhaseB = model.accessorForPhase(PhaseB).getCurrent();
Float currentPhaseC = model.accessorForPhase(PhaseC).getCurrent();
```

Alternatively you can discover all available SunSpec models _and_ load their property values in
one go (be aware that devices that support many models might take a little while to load):

```java
// obtain a ModbusConnection somehow
ModbusConnection conn = getModbusConnection();

// load all available model data
ModelDataFactory factory = ModelDataFactory.getInstance();
ModelData data = factory.getModelData(conn);

// look for a specific model type if you know what you need
InverterModelAccessor model = data.findTypedModel(InverterModelAccessor.class);

// use the accessor API to access the data
Float current = model.getCurrent();
Float currentPhaseA = model.accessorForPhase(PhaseA).getCurrent();
Float currentPhaseB = model.accessorForPhase(PhaseB).getCurrent();
Float currentPhaseC = model.accessorForPhase(PhaseC).getCurrent();
```


## ModelAccessor API

Once you have a `ModelData` object, to read a specific SunSpec model you must obtain a
`ModelAccessor` sub-interface which will provide methods to read the model properties. This was
shown in the previous section with the `InverterModelAccessor`. For example:

```java
// if you have an inverter
InverterModelAccessor model = data.findTypedModel(InverterModelAccessor.class);
Float current = model.getCurrent();

// or maybe you have an energy meter
MeterModelAccessor model = data.findTypedModel(MeterModelAccessor.class);
BigDecimal power = model.getActivePower();
```

The core API has this basic shape, to give you access to the data timestamp and the model ID:

```java
public interface ModelAccessor extends PointGroup {

	/**
	 * Gets the time stamp of the data.
	 */
	@Nullable
	Instant getDataTimestamp();

	/**
	 * Get the model ID.
	 */
	ModelId getModelId();

}
```

### Supported models

| Model | Accessor API |
|:------|:-------------|
| 101, 102, 103, 111, 112, 113 | `net.solarnetwork.sunspec.api.inverter.InverterModelAccessor` |
| 120 | `net.solarnetwork.sunspec.api.inverter.InverterNameplateRatingsModelAccessor` |
| 121 | `net.solarnetwork.sunspec.api.inverter.InverterBasicSettingsModelAccessor` |
| 160 | `net.solarnetwork.sunspec.api.inverter.InverterMpptExtensionModelAccessor` |
| 201, 202, 203, 204, 211, 212, 213, 214 | `net.solarnetwork.sunspec.api.meter.MeterModelAccessor` |
| 302 | `net.solarnetwork.sunspec.api.environmental.IrradianceModelAccessor` |
| 303 | `net.solarnetwork.sunspec.api.environmental.BomTemperatureModelAccessor` |
| 304 | `net.solarnetwork.sunspec.api.environmental.InclinometerModelAccessor` |
| 305 | `net.solarnetwork.sunspec.api.environmental.GpsModelAccessor` |
| 306 | `net.solarnetwork.sunspec.api.environmental.ReferencePointModelAccessor` |
| 307 | `net.solarnetwork.sunspec.api.environmental.MeteorologicalModelAccessor` |
| 308 | `net.solarnetwork.sunspec.api.environmental.MiniMeteorologicalModelAccessor` |
| 401, 403 | `net.solarnetwork.sunspec.api.combiner.StringCombinerModelAccessor` |
| 402, 404 | `net.solarnetwork.sunspec.api.combiner.StringCombinerAdvancedModelAccessor` |
| 701 | `net.solarnetwork.sunspec.api.der.DerAcMeasurementModelAccessor` |
| 702 | `net.solarnetwork.sunspec.api.der.DerCapacityModelAccessor` |
| 703 | `net.solarnetwork.sunspec.api.der.DerEnterServiceModelAccessor` |
| 704 | `net.solarnetwork.sunspec.api.der.DerAcControlsModelAccessor` |
| 705 | `net.solarnetwork.sunspec.api.der.DerVoltVarModelAccessor` |
| 706 | `net.solarnetwork.sunspec.api.der.DerVoltWattModelAccessor` |
| 707 | `net.solarnetwork.sunspec.api.der.DerTripLowVoltageModelAccessor` |
| 708 | `net.solarnetwork.sunspec.api.der.DerTripHighVoltageModelAccessor` |
| 709 | `net.solarnetwork.sunspec.api.der.DerTripLowFrequencyModelAccessor` |
| 710 | `net.solarnetwork.sunspec.api.der.DerTripHighFrequencyModelAccessor` |
| 711 | `net.solarnetwork.sunspec.api.der.DerFrequencyDroopModelAccessor` |
| 712 | `net.solarnetwork.sunspec.api.der.DerWattVarModelAccessor` |
| 713 | `net.solarnetwork.sunspec.api.der.DerStorageCapacityModelAccessor` |
| 714 | `net.solarnetwork.sunspec.api.der.DerDcMeasurementModelAccessor` |
| 715 | `net.solarnetwork.sunspec.api.der.DerControlModelAccessor` |
| 802 | `net.solarnetwork.sunspec.api.storage.BatteryBaseModelAccessor` |
| 803 | `net.solarnetwork.sunspec.api.storage.LithiumIonBankModelAccessor` |
| 804 | `net.solarnetwork.sunspec.api.storage.LithiumIonStringModelAccessor` |
| 805 | `net.solarnetwork.sunspec.api.storage.LithiumIonModuleModelAccessor` |


## PointGroup API

The `ModelAccessor` API actually extends a lower-level `PointGroup` API, so you can view the data
(points) available in the model without known its more specific `ModelAccessor` extension. If a
model has any repeating blocks, those will show up as nested `PointGroup` objects, available via the
`getPointGroups()` method.

```java
public interface PointGroup {

	/**
	 * Get the points of this group.
	 */
	Collection<? extends ModbusReference> getPointReferences();

	/**
	 * Get the value of a point of this group.
	 */
	@Nullable
	Object getPointValue(ModbusReference point);

	/**
	 * Get the nested groups of this group.
	 */
	default List<PointGroupList> getPointGroups() {
		return List.of();
	}

}
```

`PointGroup` also provides a `toPointMap()` method is an easy way to get a simple `Map<String,
Object>` of all the available points in the model. The map keys will use a default naming strategy,
but you can use the `toPointMap(mode, keyMapper)` variant to resolve your own key names.


## Maven Central Repository coordinates

Nifty SunSpec can be integrated into your project using the following coordinates,
all of which use the `net.solarnetwork.common` Group identifier:

| Artifact | Notes |
|:---------|:------|
| `nifty-sunspec-api`  | The high-level interfaces. |
| `nifty-sunspec-core` | The core implementation. |
| `nifty-sunspec-modbus-nifty` | A Nifty Modbus implementation of `ModbusConnection`. |

Typically it is sufficient to declare just the `nifty-sunspec-core` in your project. For example in
a Gradle project:

```gradle
dependencies {
	implementation 'net.solarnetwork.common:nifty-sunspec-core:1.0.0'
}
```


## Building from source

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
 * `shell/build/libs/nifty-sunspec-shell-X.Y.Z.jar`
