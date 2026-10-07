/* ==================================================================
 * NativeImageMetadataTests.java - 7/10/2026 10:41:23 pm
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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.core.ModelDataFactory;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the GraalVM native image metadata of this bundle.
 *
 * @author matt
 * @version 1.0
 */
public class NativeImageMetadataTests {

	private static final String METADATA_RESOURCE = "META-INF/native-image/net.solarnetwork.common/nifty-sunspec-core/reachability-metadata.json";

	private static final Pattern TYPE_REGEX = Pattern.compile("\"type\"\\s*:\\s*\"([^\"]+)\"");

	private static final String CONSTRUCTOR_REGISTRATION = "\"name\": \"<init>\", \"parameterTypes\": [ \"net.solarnetwork.sunspec.modbus.support.ModelData\", \"int\", \"int\" ]";

	private static String resourceText(String name) throws IOException {
		try (InputStream in = ModelDataFactory.class.getClassLoader().getResourceAsStream(name)) {
			then(in).as("Resource %s exists", name).isNotNull();
			return new String(in.readAllBytes(), UTF_8);
		}
	}

	@Test
	public void modelAccessorsRegistered() throws Exception {
		// GIVEN
		final Properties mapping = new Properties();
		try (InputStream in = ModelDataFactory.class.getClassLoader()
				.getResourceAsStream(ModelDataFactory.DEFAULT_MODEL_ACCESSOR_PROPERTIES_RESOURCE_NAME)) {
			mapping.load(in);
		}
		final Set<String> accessorClassNames = new TreeSet<>();
		for ( Object className : mapping.values() ) {
			accessorClassNames.add(className.toString());
		}

		// WHEN
		final String metadata = resourceText(METADATA_RESOURCE);
		final Set<String> registered = new TreeSet<>();
		final Matcher m = TYPE_REGEX.matcher(metadata);
		while ( m.find() ) {
			registered.add(m.group(1));
		}

		// THEN
		// @formatter:off
		then(registered)
			.as("Every model accessor in the default mapping, and nothing else, is registered")
			.isEqualTo(accessorClassNames)
			;
		then(metadata.split(Pattern.quote(CONSTRUCTOR_REGISTRATION), -1))
			.as("The factory constructor of every model accessor is registered")
			.hasSize(accessorClassNames.size() + 1)
			;
		then(metadata)
			.as("Model accessor mappings registered")
			.contains("\"glob\": \"" + ModelDataFactory.DEFAULT_MODEL_ACCESSOR_PROPERTIES_RESOURCE_NAME + "\"")
			.contains("\"glob\": \"" + ModelDataFactory.MODEL_ACCESSOR_PROPERTIES_RESOURCE_NAME + "\"")
			;
		for ( String className : accessorClassNames ) {
			then(Class.forName(className).getConstructor(ModelData.class, int.class, int.class))
				.as("Model accessor %s has the factory constructor", className)
				.isNotNull()
				;
		}
		// @formatter:on
	}

}
