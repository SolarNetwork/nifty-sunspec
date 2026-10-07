/* ==================================================================
 * IrradianceModelAccessorImpl_302_01Tests.java - 5/07/2023 8:38:52 am
 *
 * Copyright 2023 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.core.environmental.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.PointMapMode;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.api.environmental.IrradianceModelAccessor;
import net.solarnetwork.sunspec.api.environmental.IrradianceModelAccessor.Irradiance;
import net.solarnetwork.sunspec.core.environmental.IrradianceModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link IrradianceModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class IrradianceModelAccessorImpl_302_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(IrradianceModelAccessorImpl_302_01Tests.class);

	/** Synthetic data, as there is no capture from a device. */
	private static final String TEST_DATA = "test-data-302-01.txt";

	/** The model base address in the test data. */
	private static final int BASE_ADDRESS = 70;

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA);
	}

	private IrradianceModelAccessor getTestModel() {
		return getTestDataInstance().findTypedModel(IrradianceModelAccessor.class);
	}

	private IrradianceModelAccessor getTestModel(int address, int... words) {
		return ModelDataUtils.getModelDataInstanceWithRegisters(getClass(), TEST_DATA, address, words)
				.findTypedModel(IrradianceModelAccessor.class);
	}

	@Test
	public void dataDebugString() {
		ModelData data = getTestDataInstance();
		log.debug("Got test data: " + data.dataDebugString());
	}

	@Test
	public void findTypedModel() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		IrradianceModelAccessor accessor = data.findTypedModel(IrradianceModelAccessor.class);

		// THEN
		// @formatter:off
		then(accessor)
			.as("Model found by accessor type")
			.isInstanceOf(IrradianceModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		IrradianceModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(BASE_ADDRESS, from(IrradianceModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BASE_ADDRESS + 2, from(IrradianceModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(EnvironmentalModelId.Irradiance, from(IrradianceModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(0, from(IrradianceModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(5, from(IrradianceModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(10, from(IrradianceModelAccessor::getModelLength))
			.as("Model repeating instance count")
			.returns(2, from(IrradianceModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void irradiances() {
		// WHEN
		List<Irradiance> irradiances = getTestModel().getIrradiances();

		// THEN
		// @formatter:off
		then(irradiances)
			.as("One irradiance for each repeating block instance")
			.hasSize(2)
			;
		then(irradiances.get(0))
			.as("Instance 1 GHI")
			.returns(950, from(Irradiance::getGlobalHorizontalIrradiance))
			.as("Instance 1 POAI")
			.returns(1020, from(Irradiance::getPlaneOfArrayIrradiance))
			.as("Instance 1 DFI")
			.returns(110, from(Irradiance::getDiffuseIrradiance))
			.as("Instance 1 DNI")
			.returns(870, from(Irradiance::getDirectNormalIrradiance))
			.as("Instance 1 OTI not implemented")
			.returns(null, from(Irradiance::getOtherIrradiance))
			;
		then(irradiances.get(1))
			.as("Instance 2 GHI")
			.returns(945, from(Irradiance::getGlobalHorizontalIrradiance))
			.as("Instance 2 POAI")
			.returns(985, from(Irradiance::getPlaneOfArrayIrradiance))
			.as("Instance 2 DFI")
			.returns(105, from(Irradiance::getDiffuseIrradiance))
			.as("Instance 2 DNI")
			.returns(865, from(Irradiance::getDirectNormalIrradiance))
			.as("Instance 2 OTI")
			.returns(125, from(Irradiance::getOtherIrradiance))
			;
		// @formatter:on
	}

	@Test
	public void irradiance() {
		// WHEN
		Irradiance irradiance = getTestModel().getIrradiance();

		// THEN
		// @formatter:off
		then(irradiance)
			.as("First instance GHI")
			.returns(950, from(Irradiance::getGlobalHorizontalIrradiance))
			.as("First instance POAI")
			.returns(1020, from(Irradiance::getPlaneOfArrayIrradiance))
			;
		// @formatter:on
	}

	@Test
	public void irradiances_none() {
		// GIVEN
		// a model with no repeating block instances, followed by the end marker
		IrradianceModelAccessor model = getTestModel(BASE_ADDRESS, 0x012E, 0x0000, 0xFFFF, 0x0000);

		// THEN
		// @formatter:off
		then(model.getIrradiances())
			.as("No irradiances without repeating block instances")
			.isEmpty()
			;
		then(model.getIrradiance())
			.as("No first irradiance without repeating block instances")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void pointMap_flat() {
		// WHEN
		Map<String, Object> result = getTestModel().toPointMap(PointMapMode.Flat);

		// THEN
		// @formatter:off
		then(result)
			.as("Points of each irradiance mapped with instance suffix, without unavailable values")
			.containsExactly(
					entry("ghi_1", 950),
					entry("poai_1", 1020),
					entry("dfi_1", 110),
					entry("dni_1", 870),
					entry("ghi_2", 945),
					entry("poai_2", 985),
					entry("dfi_2", 105),
					entry("dni_2", 865),
					entry("oti_2", 125))
			;
		// @formatter:on
	}

	@Test
	public void pointMap_nested() {
		// WHEN
		Map<String, Object> result = getTestModel().toPointMap(PointMapMode.Nested);

		// THEN
		// @formatter:off
		then(result)
			.as("Irradiances mapped as list")
			.containsOnlyKeys("irradiances")
			.extractingByKey("irradiances", LIST)
			.as("Map for each irradiance")
			.containsExactly(
					Map.of("ghi", 950, "poai", 1020, "dfi", 110, "dni", 870),
					Map.of("ghi", 945, "poai", 985, "dfi", 105, "dni", 865, "oti", 125))
			;
		// @formatter:on
	}

	@Test
	public void pointMap_none() {
		// GIVEN
		// a model with no repeating block instances, followed by the end marker
		IrradianceModelAccessor model = getTestModel(BASE_ADDRESS, 0x012E, 0x0000, 0xFFFF, 0x0000);

		// THEN
		// @formatter:off
		then(model.toPointMap(PointMapMode.Flat))
			.as("No points without repeating block instances")
			.isEmpty()
			;
		then(model.toPointMap(PointMapMode.Nested))
			.as("No irradiances list without repeating block instances")
			.isEmpty()
			;
		// @formatter:on
	}

}
