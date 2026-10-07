/* ==================================================================
 * PointMapTests.java - 8/10/2026 11:20:14 am
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

package net.solarnetwork.sunspec.core.test;

import static org.assertj.core.api.BDDAssertions.then;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.PointMapMode;
import net.solarnetwork.sunspec.core.GenericModelAccessor;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the point maps of all the models in the test captures.
 *
 * @author matt
 * @version 1.0
 */
public class PointMapTests {

	/** A flat map key: a point key with index and group suffixes. */
	private static final Pattern FLAT_KEY = Pattern.compile("[a-z][A-Za-z0-9]*(_[a-z0-9][A-Za-z0-9]*)*");

	/**
	 * A nested map key: a point or group key, with an optional phase suffix.
	 */
	private static final Pattern NESTED_KEY = Pattern.compile("[a-z][A-Za-z0-9]*(_(a|b|c|ab|bc|ca))?");

	/**
	 * Get the test capture resources, from the multi-register points file that
	 * lists them.
	 *
	 * @return the resources
	 * @throws IOException
	 *         if the file cannot be read
	 */
	private static Set<String> captures() throws IOException {
		final Set<String> result = new LinkedHashSet<>();
		try (BufferedReader r = new BufferedReader(new InputStreamReader(
				PointMapTests.class.getResourceAsStream("multi-register-points.txt"),
				StandardCharsets.UTF_8))) {
			String line;
			while ( (line = r.readLine()) != null ) {
				if ( !line.isBlank() && !line.startsWith("#") ) {
					result.add(line.substring(0, line.indexOf(' ')));
				}
			}
		}
		return result;
	}

	/**
	 * Collect the leaf values and keys of a nested map.
	 *
	 * @param map
	 *        the map
	 * @param values
	 *        the list to add the leaf values to
	 * @param keys
	 *        the set to add the keys to
	 */
	private static void collect(Map<?, ?> map, List<Object> values, Set<String> keys) {
		for ( Map.Entry<?, ?> e : map.entrySet() ) {
			keys.add((String) e.getKey());
			final Object v = e.getValue();
			if ( v instanceof Map<?, ?> m ) {
				collect(m, values, keys);
			} else if ( v instanceof List<?> l && !l.isEmpty() && l.get(0) instanceof Map ) {
				for ( Object o : l ) {
					collect((Map<?, ?>) o, values, keys);
				}
			} else {
				values.add(v);
			}
		}
	}

	@Test
	public void allCaptures() throws IOException {
		for ( String capture : captures() ) {
			// GIVEN
			final ModelData data = ModelDataUtils.getModelDataInstance(getClass(), capture);
			final List<ModelAccessor> models = new ArrayList<>(data.getModels());
			models.add(0, data);

			for ( ModelAccessor model : models ) {
				final String desc = String.format("Model %s in %s", model.getModelId(), capture);

				// WHEN
				final Map<String, Object> flat = model.toPointMap(PointMapMode.Flat);
				final Map<String, Object> nested = model.toPointMap(PointMapMode.Nested);

				// THEN
				final List<Object> nestedValues = new ArrayList<>();
				final Set<String> nestedKeys = new LinkedHashSet<>();
				collect(nested, nestedValues, nestedKeys);

				if ( !(model instanceof GenericModelAccessor) ) {
					then(flat).as("%s has point values", desc).isNotEmpty();
				}
				// @formatter:off
				then(flat.keySet())
					.as("%s flat keys", desc)
					.allMatch(k -> FLAT_KEY.matcher(k).matches())
					;
				then(nestedKeys)
					.as("%s nested keys", desc)
					.allMatch(k -> NESTED_KEY.matcher(k).matches())
					;
				then(nestedValues)
					.as("%s nested values are the flat values", desc)
					.containsExactlyElementsOf(flat.values())
					;
				// @formatter:on
			}
		}
	}

}
