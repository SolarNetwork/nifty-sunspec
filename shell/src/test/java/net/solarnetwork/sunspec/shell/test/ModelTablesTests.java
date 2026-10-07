/* ==================================================================
 * ModelTablesTests.java - 7/10/2026 9:38:52 pm
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

import static net.solarnetwork.sunspec.shell.ModelTables.formatValue;
import static net.solarnetwork.sunspec.shell.ModelTables.modelName;
import static net.solarnetwork.sunspec.shell.test.TestDevices.DER_DUMP;
import static net.solarnetwork.sunspec.shell.test.TestDevices.INVERTER_DUMP;
import static net.solarnetwork.sunspec.shell.test.TestDevices.connection;
import static net.solarnetwork.sunspec.shell.test.TestDevices.modelData;
import static org.assertj.core.api.BDDAssertions.then;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.AcPhase;
import net.solarnetwork.sunspec.api.CommonModelId;
import net.solarnetwork.sunspec.api.GenericModelId;
import net.solarnetwork.sunspec.api.MeasurementUnits;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.PointGroup;
import net.solarnetwork.sunspec.api.PointGroupList;
import net.solarnetwork.sunspec.api.PointMapMode;
import net.solarnetwork.sunspec.api.SunSpecUtils;
import net.solarnetwork.sunspec.api.der.DerVoltVarModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterMpptExtensionModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;
import net.solarnetwork.sunspec.shell.ModelTables;

/**
 * Test cases for the {@link ModelTables} class.
 *
 * @author matt
 * @version 1.0
 */
public class ModelTablesTests {

	private static String lines(String... lines) {
		return String.join(System.lineSeparator(), lines);
	}

	@Test
	public void formatValue_numbers() {
		// @formatter:off
		then(formatValue(230.0f))
			.as("Float without trailing zeros")
			.isEqualTo("230")
			;
		then(formatValue(119.96f))
			.as("Float with fraction")
			.isEqualTo("119.96")
			;
		then(formatValue(0.0001f))
			.as("Small float without exponent")
			.isEqualTo("0.0001")
			;
		then(formatValue(1.0E7f))
			.as("Large float without exponent")
			.isEqualTo("10000000")
			;
		then(formatValue(-0.001f))
			.as("Negative float")
			.isEqualTo("-0.001")
			;
		then(formatValue(new BigDecimal("1E+3")))
			.as("BigDecimal without exponent")
			.isEqualTo("1000")
			;
		then(formatValue(65535))
			.as("Integer")
			.isEqualTo("65535")
			;
		then(formatValue(new BigInteger("18446744073709551615")))
			.as("BigInteger")
			.isEqualTo("18446744073709551615")
			;
		// @formatter:on
	}

	@Test
	public void formatValue_other() {
		// GIVEN
		Set<AcPhase> phases = new LinkedHashSet<>(List.of(AcPhase.PhaseA, AcPhase.PhaseB));

		// THEN
		// @formatter:off
		then(formatValue(null))
			.as("Null is empty")
			.isEqualTo("")
			;
		then(formatValue("OutBack Power"))
			.as("String")
			.isEqualTo("OutBack Power")
			;
		then(formatValue(AcPhase.PhaseA))
			.as("Enum by name")
			.isEqualTo("PhaseA")
			;
		then(formatValue(phases))
			.as("Set as list of names")
			.isEqualTo("PhaseA, PhaseB")
			;
		then(formatValue(Set.of()))
			.as("Empty set")
			.isEqualTo(ModelTables.EMPTY_COLLECTION_VALUE)
			;
		then(formatValue(Instant.parse("2026-10-07T08:00:00Z")))
			.as("Instant in ISO 8601 form")
			.isEqualTo("2026-10-07T08:00:00Z")
			;
		then(formatValue(true))
			.as("Boolean")
			.isEqualTo("true")
			;
		// @formatter:on
	}

	@Test
	public void formatValue_unit() {
		// @formatter:off
		then(formatValue(12.1f, MeasurementUnits.AMPERE))
			.as("Value followed by unit")
			.isEqualTo("12.1 A")
			;
		then(formatValue(new BigDecimal("1960"), MeasurementUnits.WATT))
			.as("BigDecimal followed by unit")
			.isEqualTo("1960 W")
			;
		then(formatValue(57.0f, MeasurementUnits.DEGREE_CELSIUS))
			.as("Celsius with a space")
			.isEqualTo("57 °C")
			;
		then(formatValue(45.0f, MeasurementUnits.DEGREE))
			.as("Angle degrees without a space")
			.isEqualTo("45°")
			;
		then(formatValue(12.1f, null))
			.as("Value without unit")
			.isEqualTo("12.1")
			;
		then(formatValue(null, MeasurementUnits.AMPERE))
			.as("No unit without a value")
			.isEqualTo("")
			;
		// @formatter:on
	}

	@Test
	public void modelName_known() {
		// @formatter:off
		then(modelName(CommonModelId.CommonModel))
			.as("Model ID description")
			.isEqualTo("Common model")
			;
		then(modelName(new GenericModelId(126)))
			.as("Model without an accessor named after the known model")
			.isEqualTo("Static volt-VAR arrays")
			;
		then(modelName(new GenericModelId(64122)))
			.as("Unknown model")
			.isEqualTo(ModelTables.UNKNOWN_MODEL_NAME)
			;
		// @formatter:on
	}

	@Test
	public void modelTitle() {
		// @formatter:off
		then(ModelTables.modelTitle(CommonModelId.CommonModel))
			.as("Title with model ID and name")
			.isEqualTo("Model 1: Common model")
			;
		// @formatter:on
	}

	@Test
	public void pointTable_common() {
		// GIVEN
		ModelData data = modelData(connection(DER_DUMP));

		// WHEN
		String table = ModelTables.pointTable(ModelTables.modelTitle(data.getModelId()), data);

		// THEN
		// @formatter:off
		then(table)
			.as("Title row above left-aligned points and right-aligned values")
			.isEqualTo(lines(
				"╔══════════════════════════════════╗",
				"║ Model 1: Common model            ║",
				"╠═══════════════╤══════════════════╣",
				"║ Point         │            Value ║",
				"╠═══════════════╪══════════════════╣",
				"║ manufacturer  │    OutBack Power ║",
				"║ model         │        OGHI8048A ║",
				"║ options       │        prototype ║",
				"║ version       │      1.0.20.3812 ║",
				"║ serialNumber  │ OGHI2232F0100079 ║",
				"║ deviceAddress │              255 ║",
				"╚═══════════════╧══════════════════╝"
			))
			;
		// @formatter:on
	}

	@Test
	public void pointTable_units() {
		// GIVEN
		ModelData data = modelData(connection(INVERTER_DUMP));
		InverterMpptExtensionModelAccessor mppt = data
				.findTypedModel(InverterMpptExtensionModelAccessor.class);

		// WHEN
		String table = ModelTables.pointTable("MPPT", mppt);

		// THEN
		// @formatter:off
		then(table.lines())
			.as("Repeating block point values with units")
			.contains(
				"║ moduleDcCurrent_1 │  3.3 A ║",
				"║ moduleDcVoltage_1 │  602 V ║",
				"║ moduleDcPower_1   │ 1960 W ║",
				"║ moduleEvents_6    │ (none) ║")
			;
		// @formatter:on
	}

	@Test
	public void pointTable_wideTitle() {
		// GIVEN
		ModelData data = modelData(connection(DER_DUMP));
		String title = "A title that is much wider than the point and value columns";

		// WHEN
		String table = ModelTables.pointTable(title, data);

		// THEN
		List<String> lines = table.lines().toList();
		// @formatter:off
		then(lines)
			.as("All lines have the same width")
			.allSatisfy(l -> then(l).hasSameSizeAs(lines.get(0)))
			.as("Title fits on its line")
			.element(1).isEqualTo("║ " + title + " ║")
			;
		then(lines.get(5))
			.as("Values right-aligned in the widened value column")
			.startsWith("║ manufacturer  │ ")
			.endsWith(" OutBack Power ║")
			;
		// @formatter:on
	}

	@Test
	public void pointUnits_nestedGroups() {
		// GIVEN
		ModelData der = modelData(connection(DER_DUMP));
		ModelData inverter = modelData(connection(INVERTER_DUMP));

		// WHEN
		Map<String, String> curveUnits = ModelTables
				.pointUnits(der.findTypedModel(DerVoltVarModelAccessor.class));
		Map<String, String> mpptUnits = ModelTables
				.pointUnits(inverter.findTypedModel(InverterMpptExtensionModelAccessor.class));

		// THEN
		// @formatter:off
		then(curveUnits)
			.as("Units of points in repeating curves")
			.containsEntry("curveVoltageReference_2", MeasurementUnits.PERCENT)
			.containsEntry("curveOpenLoopResponseTime_3", MeasurementUnits.SECOND)
			.as("Units of points in repeating curve points")
			.containsEntry("pointVoltage_1_1", MeasurementUnits.PERCENT)
			.containsEntry("pointReactivePower_3_4", MeasurementUnits.PERCENT)
			.as("No units for unitless points")
			.doesNotContainKeys("enabled", "numberOfCurves", "curveReadOnly_1")
			;
		then(mpptUnits)
			.as("Units of points in repeating modules")
			.containsEntry("moduleDcCurrent_1", MeasurementUnits.AMPERE)
			.containsEntry("moduleDcPower_6", MeasurementUnits.WATT)
			;
		// @formatter:on
	}

	private static void collectUnitsByName(PointGroup group, Map<String, String> result) {
		for ( ModbusReference ref : group.getPointReferences() ) {
			if ( ref.getMeasurementUnit() != null ) {
				result.put(ref.getName(), ref.getMeasurementUnit());
			}
		}
		for ( PointGroupList list : group.getPointGroups() ) {
			for ( PointGroup g : list.groups() ) {
				collectUnitsByName(g, result);
			}
		}
	}

	@Test
	public void pointUnits_keysMatchPointMap() {
		// GIVEN
		ModelData der = modelData(connection(DER_DUMP));
		ModelData inverter = modelData(connection(INVERTER_DUMP));
		List<ModelAccessor> models = new ArrayList<>();
		for ( ModelData data : List.of(der, inverter) ) {
			models.add(data);
			models.addAll(data.getModels());
		}
		Pattern markedName = Pattern.compile("<([^>]+)>");

		for ( ModelAccessor model : models ) {
			Map<String, String> unitsByName = new HashMap<>();
			collectUnitsByName(model, unitsByName);

			// WHEN
			Map<String, String> units = ModelTables.pointUnits(model);

			// THEN
			// mark the names in the point map keys, to find each point's name and key
			for ( String marked : model.toPointMap(PointMapMode.Flat, n -> "<" + n + ">").keySet() ) {
				String name = marked.substring(1, marked.indexOf('>'));
				String key = markedName.matcher(marked)
						.replaceAll(m -> SunSpecUtils.pointKey(m.group(1)));
				// @formatter:off
				then(units.get(key))
					.as("Unit of model %d point %s", model.getModelId().getId(), key)
					.isEqualTo(unitsByName.get(name))
					;
				// @formatter:on
			}
		}
	}

	@Test
	public void modelListTable() {
		// GIVEN
		ModelData data = modelData(connection(DER_DUMP));
		List<ModelAccessor> models = List.of(data, data.getModels().get(0),
				data.getModels().get(data.getModels().size() - 1));

		// WHEN
		String table = ModelTables.modelListTable(models);

		// THEN
		// @formatter:off
		then(table)
			.as("Right-aligned model IDs and left-aligned names")
			.isEqualTo(lines(
				"╔═══════╤══════════════════════════════════╗",
				"║ Model │ Name                             ║",
				"╠═══════╪══════════════════════════════════╣",
				"║     1 │ Common model                     ║",
				"║   202 │ Split single phase (A-B-N) meter ║",
				"║ 64122 │ Unknown                          ║",
				"╚═══════╧══════════════════════════════════╝"
			))
			;
		// @formatter:on
	}

}
