/* ==================================================================
 * InverterNameplateRatingsModelAccessorImpl_120_01Tests.java - 8/10/2018 7:06:15 AM
 *
 * Copyright 2018 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.core.inverter.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.inverter.InverterControlModelId;
import net.solarnetwork.sunspec.api.inverter.InverterDerType;
import net.solarnetwork.sunspec.api.inverter.InverterNameplateRatingsModelAccessor;
import net.solarnetwork.sunspec.core.inverter.InverterNameplateRatingsModelAccessorImpl;
import net.solarnetwork.sunspec.core.meter.test.IntegerMeterModelAccessorTests;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link InverterNameplateRatingsModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class InverterNameplateRatingsModelAccessorImpl_120_01Tests {

	private static final Logger log = LoggerFactory.getLogger(IntegerMeterModelAccessorTests.class);

	private static final String TEST_DATA = "test-data-101-01.txt";

	/** The model block address in the test data. */
	private static final int BLOCK_ADDRESS = 123;

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA);
	}

	private InverterNameplateRatingsModelAccessor getTestModel(int address, int... words) {
		return ModelDataUtils.getModelDataInstanceWithRegisters(getClass(), TEST_DATA, address, words)
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);
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
		InverterNameplateRatingsModelAccessor meterAccessor = data
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(meterAccessor)
			.as("Model found by accessor type")
			.isInstanceOf(InverterNameplateRatingsModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(121, from(InverterNameplateRatingsModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(123, from(InverterNameplateRatingsModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(InverterControlModelId.NameplateRatings,
					from(InverterNameplateRatingsModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(26, from(InverterNameplateRatingsModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(InverterNameplateRatingsModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(26, from(InverterNameplateRatingsModelAccessor::getModelLength))
			.as("Model length")
			.returns(0, from(InverterNameplateRatingsModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void derType() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getDerType())
			.as("DER type")
			.isEqualTo(InverterDerType.PV)
			;
		// @formatter:on
	}

	@Test
	public void activePowerRating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getActivePowerRating())
			.as("Active power rating")
			.isEqualTo(new BigDecimal("11400"))
			;
		// @formatter:on
	}

	@Test
	public void apparentPowerRating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getApparentPowerRating())
			.as("Apparent power rating")
			.isEqualTo(new BigDecimal("11400"))
			;
		// @formatter:on
	}

	@Test
	public void reactivePowerQ1Rating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getReactivePowerQ1Rating())
			.as("Reactive power Q1 rating")
			.isEqualTo(new BigDecimal("6000"))
			;
		// @formatter:on
	}

	@Test
	public void reactivePowerQ2Rating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getReactivePowerQ2Rating())
			.as("Reactive power Q2 rating")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void reactivePowerQ3Rating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getReactivePowerQ3Rating())
			.as("Reactive power Q3 rating")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void reactivePowerQ4Rating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getReactivePowerQ4Rating())
			.as("Reactive power Q4 rating")
			.isEqualTo(new BigDecimal("-6000"))
			;
		// @formatter:on
	}

	@Test
	public void currentRating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getCurrentRating())
			.as("Current rating")
			.isEqualTo(47.50f)
			;
		// @formatter:on
	}

	@Test
	public void powerFactorQ1Rating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getPowerFactorQ1Rating())
			.as("Power factor Q1 rating")
			.isEqualTo(-0.850f)
			;
		// @formatter:on
	}

	@Test
	public void powerFactorQ2Rating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getPowerFactorQ2Rating())
			.as("Power factor Q1 rating")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void powerFactorQ3Rating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getPowerFactorQ3Rating())
			.as("Power factor Q3 rating")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void powerFactorQ4Rating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getPowerFactorQ4Rating())
			.as("Power factor Q4 rating")
			.isEqualTo(0.850f)
			;
		// @formatter:on
	}

	@Test
	public void storedEnergyRating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getStoredEnergyRating())
			.as("Stored energy rating")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void storedChargeCapcity() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getStoredChargeCapacity())
			.as("Stored charge capcity")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void storedEnergyImportPowerRating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getStoredEnergyImportPowerRating())
			.as("Stored energy import power rating")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void storedEnergyExportPowerRating() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getStoredEnergyExportPowerRating())
			.as("Stored energy export power rating")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void storedEnergyExportPowerRating_negativeScaleFactor() {
		// GIVEN
		// MaxDisChaRte and MaxDisChaRte_SF
		InverterNameplateRatingsModelAccessor model = getTestModel(BLOCK_ADDRESS + 23, 0xC350, 0xFFFF);

		// THEN
		// @formatter:off
		then(model.getStoredEnergyExportPowerRating())
			.as("Stored energy export power rating")
			.isEqualTo(new BigDecimal("5000"))
			;
		// @formatter:on
	}

	@Test
	public void derType_pvAndStorage() {
		// @formatter:off
		then(getTestModel(BLOCK_ADDRESS, 82).getDerType())
			.as("DER type")
			.isEqualTo(InverterDerType.PVAndStorage)
			;
		// @formatter:on
	}

	@Test
	public void derType_undefinedCode() {
		// @formatter:off
		then(getTestModel(BLOCK_ADDRESS, 5).getDerType())
			.as("Undefined DER type not available")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void infoMap() {
		// GIVEN
		InverterNameplateRatingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterNameplateRatingsModelAccessor.class);

		// WHEN
		Map<String, Object> result = model.nameplateRatingsInfo();

		// THEN
		// @formatter:off
		then(result.keySet())
			.as("Info map keys populated")
			.containsExactly(
				InverterNameplateRatingsModelAccessor.INFO_KEY_DER_TYPE,
				InverterNameplateRatingsModelAccessor.INFO_KEY_DER_TYPE_CODE,
				InverterNameplateRatingsModelAccessor.INFO_KEY_ACTIVE_POWER_RATING,
				InverterNameplateRatingsModelAccessor.INFO_KEY_APPARENT_POWER_RATING,
				InverterNameplateRatingsModelAccessor.INFO_KEY_REACTIVE_POWER_Q1_RATING,
				InverterNameplateRatingsModelAccessor.INFO_KEY_REACTIVE_POWER_Q4_RATING,
				InverterNameplateRatingsModelAccessor.INFO_KEY_CURRENT_RATING,
				InverterNameplateRatingsModelAccessor.INFO_KEY_POWER_FACTOR_Q1_RATING,
				InverterNameplateRatingsModelAccessor.INFO_KEY_POWER_FACTOR_Q4_RATING
			)
			;
		then(result.values())
			.as("Info map values populated")
			.containsExactly(
				InverterDerType.PV.toString(),
				InverterDerType.PV.getCode(),
				new BigDecimal("11400"),
				new BigDecimal("11400"),
				new BigDecimal("6000"),
				new BigDecimal("-6000"),
				47.50f,
				-0.850f,
				0.850f
			)
			;
		// @formatter:on

	}

	@Test
	public void infoMap_wholeNumberRating() {
		// GIVEN
		// ARtg and ARtg_SF, for 47.0 A
		InverterNameplateRatingsModelAccessor model = getTestModel(BLOCK_ADDRESS + 10, 470, 0xFFFF);

		// WHEN
		Map<String, Object> result = model.nameplateRatingsInfo();

		// THEN
		// @formatter:off
		then(result)
			.as("Whole number rating provided as integer")
			.containsEntry(InverterNameplateRatingsModelAccessor.INFO_KEY_CURRENT_RATING, 47)
			;
		// @formatter:on
	}

	@Test
	public void infoMap_roundedDownRating() {
		// GIVEN
		// ARtg and ARtg_SF, for 47.59 A
		InverterNameplateRatingsModelAccessor model = getTestModel(BLOCK_ADDRESS + 10, 4759, 0xFFFE);

		// WHEN
		Map<String, Object> result = model.nameplateRatingsInfo();

		// THEN
		// @formatter:off
		then(result)
			.as("Rating rounded down to one decimal place")
			.containsEntry(InverterNameplateRatingsModelAccessor.INFO_KEY_CURRENT_RATING, 47.5f)
			;
		// @formatter:on
	}
}
