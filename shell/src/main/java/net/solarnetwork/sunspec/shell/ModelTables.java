/* ==================================================================
 * ModelTables.java - 7/10/2026 7:38:05 pm
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

import static java.util.stream.Collectors.joining;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.ColumnData;
import com.github.freva.asciitable.HorizontalAlign;
import net.solarnetwork.sunspec.api.CommonModelId;
import net.solarnetwork.sunspec.api.GenericModelId;
import net.solarnetwork.sunspec.api.MeasurementUnits;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.PointGroup;
import net.solarnetwork.sunspec.api.PointGroupList;
import net.solarnetwork.sunspec.api.PointMapMode;
import net.solarnetwork.sunspec.api.SunSpecUtils;
import net.solarnetwork.sunspec.api.combiner.StringCombinerModelId;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.api.inverter.InverterControlModelId;
import net.solarnetwork.sunspec.api.inverter.InverterModelId;
import net.solarnetwork.sunspec.api.meter.MeterModelId;
import net.solarnetwork.sunspec.api.storage.StorageModelId;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Text tables of SunSpec model information.
 *
 * @author matt
 * @version 1.0
 */
public final class ModelTables {

	/**
	 * The table border: {@link AsciiTable#FANCY_ASCII} without the lines
	 * between data rows.
	 */
	private static final @Nullable Character[] BORDER = fancyAsciiNoDataSeparators();

	/** The maximum width of the point value column, including padding. */
	public static final int MAX_VALUE_COLUMN_WIDTH = 62;

	/** The name of a model that is not known. */
	public static final String UNKNOWN_MODEL_NAME = "Unknown";

	/** The value shown for an empty collection, such as a set of events. */
	public static final String EMPTY_COLLECTION_VALUE = "(none)";

	private static final Map<Integer, String> MODEL_NAMES = modelNames();

	private ModelTables() {
		// not available
	}

	private static @Nullable Character[] fancyAsciiNoDataSeparators() {
		final @Nullable Character[] result = AsciiTable.FANCY_ASCII.clone();
		for ( int i = 14; i < 18; i++ ) {
			result[i] = null;
		}
		return result;
	}

	private static Map<Integer, String> modelNames() {
		final Map<Integer, String> result = new HashMap<>(128);
		for ( ModelId[] ids : List.<ModelId[]> of(CommonModelId.values(), InverterModelId.values(),
				InverterControlModelId.values(), MeterModelId.values(), EnvironmentalModelId.values(),
				StringCombinerModelId.values(), DerModelId.values(), StorageModelId.values()) ) {
			for ( ModelId id : ids ) {
				result.putIfAbsent(id.getId(), id.getDescription());
			}
		}
		return result;
	}

	/**
	 * Get the name of a model.
	 *
	 * <p>
	 * A {@link GenericModelId}, used for models without an accessor, is named
	 * after the known SunSpec model with the same ID, if there is one.
	 * </p>
	 *
	 * @param modelId
	 *        the model ID
	 * @return the name, or {@link #UNKNOWN_MODEL_NAME} if the model is not
	 *         known
	 */
	public static String modelName(ModelId modelId) {
		if ( !(modelId instanceof GenericModelId) ) {
			return modelId.getDescription();
		}
		return MODEL_NAMES.getOrDefault(modelId.getId(), UNKNOWN_MODEL_NAME);
	}

	/**
	 * Get the title of a model, with its ID and name.
	 *
	 * @param modelId
	 *        the model ID
	 * @return the title, for example {@code Model 1: Common model}
	 */
	public static String modelTitle(ModelId modelId) {
		return "Model " + modelId.getId() + ": " + modelName(modelId);
	}

	/**
	 * Get a table of the available models.
	 *
	 * @param models
	 *        the models
	 * @return the table, with a model ID column and a name column
	 */
	public static String modelListTable(Collection<ModelAccessor> models) {
		// @formatter:off
		final List<ColumnData<ModelAccessor>> columns = List.of(
				new Column().header("Model").headerAlign(HorizontalAlign.RIGHT)
						.dataAlign(HorizontalAlign.RIGHT)
						.with(m -> String.valueOf(m.getModelId().getId())),
				new Column().header("Name").headerAlign(HorizontalAlign.LEFT)
						.dataAlign(HorizontalAlign.LEFT)
						.with(m -> modelName(m.getModelId()))
		);
		// @formatter:on
		return table(models, columns);
	}

	/**
	 * Get a table of the point values of a point group.
	 *
	 * <p>
	 * The points are those of {@link PointGroup#toPointMap(PointMapMode)} in
	 * {@link PointMapMode#Flat} mode, each with its value and measurement unit.
	 * </p>
	 *
	 * @param title
	 *        the title to show above the point table, such as from
	 *        {@link #modelTitle(ModelId)}
	 * @param group
	 *        the group, such as a model
	 * @return the table, with a title row, a point name column, and a value
	 *         column
	 */
	public static String pointTable(String title, PointGroup group) {
		final Map<String, Object> points = group.toPointMap(PointMapMode.Flat);
		final Map<String, String> units = pointUnits(group);
		// @formatter:off
		final List<ColumnData<Map.Entry<String, Object>>> columns = List.of(
				new Column().header("Point").headerAlign(HorizontalAlign.LEFT)
						.dataAlign(HorizontalAlign.LEFT)
						.with(Map.Entry::getKey),
				new Column().header("Value").headerAlign(HorizontalAlign.RIGHT)
						.dataAlign(HorizontalAlign.RIGHT).maxWidth(MAX_VALUE_COLUMN_WIDTH)
						.with(e -> formatValue(e.getValue(), units.get(e.getKey())))
		);
		// @formatter:on
		return titledTable(title, points.entrySet(), columns);
	}

	/**
	 * Get the measurement units of the points of a point group, including its
	 * nested groups.
	 *
	 * <p>
	 * The units are mapped to the same keys as
	 * {@link PointGroup#toPointMap(PointMapMode)} in {@link PointMapMode#Flat}
	 * mode uses for the point values.
	 * </p>
	 *
	 * @param group
	 *        the group
	 * @return the units, never {@code null}; points without a unit are left out
	 */
	public static Map<String, String> pointUnits(PointGroup group) {
		final Map<String, String> result = new HashMap<>(32);
		addPointUnits(group, "", result);
		return result;
	}

	private static void addPointUnits(PointGroup group, String suffix, Map<String, String> result) {
		for ( ModbusReference ref : group.getPointReferences() ) {
			final String unit = ref.getMeasurementUnit();
			if ( unit != null ) {
				result.put(SunSpecUtils.pointKey(ref.getName()) + suffix, unit);
			}
		}
		for ( PointGroupList list : group.getPointGroups() ) {
			final List<? extends PointGroup> groups = list.groups();
			for ( int i = 0, len = groups.size(); i < len; i++ ) {
				final String groupSuffix = suffix + '_' + (list.repeating() ? String.valueOf(i + 1)
						: SunSpecUtils.pointKey(list.name()));
				addPointUnits(groups.get(i), groupSuffix, result);
			}
		}
	}

	/**
	 * Format a point value, with a measurement unit.
	 *
	 * @param value
	 *        the value
	 * @param unit
	 *        the unit, or {@code null} for none
	 * @return the formatted value, followed by the unit if {@code unit} is not
	 *         {@code null}
	 * @see #formatValue(Object)
	 */
	public static String formatValue(@Nullable Object value, @Nullable String unit) {
		final String result = formatValue(value);
		if ( unit == null || unit.isEmpty() || result.isEmpty() ) {
			return result;
		}
		return (MeasurementUnits.DEGREE.equals(unit) ? result + unit : result + ' ' + unit);
	}

	/**
	 * Format a point value.
	 *
	 * <p>
	 * Decimal numbers are shown without exponents or trailing zeros,
	 * enumeration values by name, and collections as a comma-delimited list of
	 * their formatted elements, or {@link #EMPTY_COLLECTION_VALUE} if empty.
	 * Other values are shown as their string value.
	 * </p>
	 *
	 * @param value
	 *        the value
	 * @return the formatted value, or an empty string if {@code value} is
	 *         {@code null}
	 */
	public static String formatValue(@Nullable Object value) {
		if ( value == null ) {
			return "";
		} else if ( value instanceof Float || value instanceof Double ) {
			return new BigDecimal(value.toString()).stripTrailingZeros().toPlainString();
		} else if ( value instanceof BigDecimal d ) {
			return d.toPlainString();
		} else if ( value instanceof Enum<?> e ) {
			return e.name();
		} else if ( value instanceof Collection<?> c ) {
			return (c.isEmpty() ? EMPTY_COLLECTION_VALUE
					: c.stream().map(ModelTables::formatValue).collect(joining(", ")));
		}
		return value.toString();
	}

	private static <T> String table(Collection<T> data, List<ColumnData<T>> columns) {
		return AsciiTable.builder().border(BORDER).data(data, columns).asString();
	}

	private static <T> String titledTable(String title, Collection<T> data,
			List<ColumnData<T>> columns) {
		String table = table(data, columns);
		String top = firstLine(table);
		int innerWidth = top.length() - 4;
		if ( title.length() > innerWidth ) {
			// widen the last column to fit the title
			final int lastWidth = top.length() - top.lastIndexOf(borderChar(2)) - 2;
			columns.get(columns.size() - 1).minWidth(lastWidth + title.length() - innerWidth);
			table = table(data, columns);
			top = firstLine(table);
			innerWidth = top.length() - 4;
		}
		final String nl = System.lineSeparator();
		final StringBuilder buf = new StringBuilder(table.length() + 3 * top.length());
		buf.append(borderChar(0)).append(String.valueOf(borderChar(1)).repeat(top.length() - 2))
				.append(borderChar(3)).append(nl);
		buf.append(borderChar(4)).append(' ').append(title)
				.append(" ".repeat(innerWidth - title.length())).append(' ').append(borderChar(6))
				.append(nl);
		buf.append(borderChar(7)).append(top, 1, top.length() - 1).append(borderChar(10));
		buf.append(table, top.length(), table.length());
		return buf.toString();
	}

	private static char borderChar(int idx) {
		final Character c = BORDER[idx];
		return (c != null ? c : ' ');
	}

	private static String firstLine(String s) {
		final int idx = s.indexOf(System.lineSeparator());
		return (idx < 0 ? s : s.substring(0, idx));
	}

}
