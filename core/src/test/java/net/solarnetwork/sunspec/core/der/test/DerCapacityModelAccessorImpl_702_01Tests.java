/* ==================================================================
 * DerCapacityModelAccessorImpl_702_01Tests.java - 5/10/2026 9:32:29 am
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

package net.solarnetwork.sunspec.core.der.test;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.der.DerAbnormalOperatingCategory;
import net.solarnetwork.sunspec.api.der.DerCapacityModelAccessor;
import net.solarnetwork.sunspec.api.der.DerControlMode;
import net.solarnetwork.sunspec.api.der.DerIntentionalIslandCategory;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerNormalOperatingCategory;
import net.solarnetwork.sunspec.core.der.DerCapacityModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.support.StaticDataMapModbusConnection;

/**
 * Test cases for the {@link DerCapacityModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class DerCapacityModelAccessorImpl_702_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	private DerCapacityModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerCapacityModelAccessor.class);
	}

	private static DerCapacityModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn).findTypedModel(DerCapacityModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerCapacityModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerCapacityModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(332, from(DerCapacityModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(334, from(DerCapacityModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.Capacity, from(DerCapacityModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(50, from(DerCapacityModelAccessor::getFixedBlockLength))
			.as("Model length")
			.returns(50, from(DerCapacityModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void powerRatings() {
		// GIVEN
		DerCapacityModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Active power")
			.returns(new BigDecimal("7680"), from(DerCapacityModelAccessor::getActivePowerMaximumRating))
			.as("Active power over-excited")
			.returns(new BigDecimal("3072"), from(DerCapacityModelAccessor::getActivePowerOverExcitedRating))
			.as("Over-excited power factor")
			.returns(0.4f, from(DerCapacityModelAccessor::getOverExcitedPowerFactorRating))
			.as("Active power under-excited")
			.returns(new BigDecimal("3072"), from(DerCapacityModelAccessor::getActivePowerUnderExcitedRating))
			.as("Under-excited power factor")
			.returns(0.4f, from(DerCapacityModelAccessor::getUnderExcitedPowerFactorRating))
			.as("Apparent power")
			.returns(new BigDecimal("7680"), from(DerCapacityModelAccessor::getApparentPowerMaximumRating))
			.as("Reactive power injected")
			.returns(new BigDecimal("4070"), from(DerCapacityModelAccessor::getReactivePowerInjectedMaximumRating))
			.as("Reactive power absorbed")
			.returns(new BigDecimal("4070"), from(DerCapacityModelAccessor::getReactivePowerAbsorbedMaximumRating))
			.as("Active power charge rate")
			.returns(new BigDecimal("7680"), from(DerCapacityModelAccessor::getActivePowerChargeRateMaximumRating))
			.as("Active power discharge rate")
			.returns(new BigDecimal("7680"), from(DerCapacityModelAccessor::getActivePowerDischargeRateMaximumRating))
			.as("Apparent power charge rate")
			.returns(new BigDecimal("7680"), from(DerCapacityModelAccessor::getApparentPowerChargeRateMaximumRating))
			.as("Apparent power discharge rate")
			.returns(new BigDecimal("7680"), from(DerCapacityModelAccessor::getApparentPowerDischargeRateMaximumRating))
			;
		// @formatter:on
	}

	@Test
	public void otherRatings() {
		// GIVEN
		DerCapacityModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Voltage nominal")
			.returns(240.0f, from(DerCapacityModelAccessor::getVoltageNominalRating))
			.as("Voltage maximum")
			.returns(264.0f, from(DerCapacityModelAccessor::getVoltageMaximumRating))
			.as("Voltage minimum")
			.returns(211.0f, from(DerCapacityModelAccessor::getVoltageMinimumRating))
			.as("Current maximum")
			.returns(32.0f, from(DerCapacityModelAccessor::getCurrentMaximumRating))
			.as("Reactive susceptance")
			.returns(0.0f, from(DerCapacityModelAccessor::getReactiveSusceptanceRating))
			.as("Normal operating category")
			.returns(DerNormalOperatingCategory.CategoryB,
					from(DerCapacityModelAccessor::getNormalOperatingCategory))
			.as("Abnormal operating category")
			.returns(DerAbnormalOperatingCategory.CategoryIII,
					from(DerCapacityModelAccessor::getAbnormalOperatingCategory))
			.as("Supported control modes")
			.returns(Set.of(DerControlMode.MaxActivePower, DerControlMode.FixedActivePower,
					DerControlMode.FixedReactivePower, DerControlMode.FixedPowerFactor,
					DerControlMode.VoltVar, DerControlMode.FrequencyWatt, DerControlMode.LowVoltageTrip,
					DerControlMode.HighVoltageTrip, DerControlMode.WattVar, DerControlMode.VoltWatt,
					DerControlMode.LowFrequencyTrip, DerControlMode.HighFrequencyTrip),
					from(DerCapacityModelAccessor::getSupportedControlModes))
			.as("Intentional island categories")
			.returns(Set.of(DerIntentionalIslandCategory.IntentionalIslandCapable,
					DerIntentionalIslandCategory.BlackStartCapable,
					DerIntentionalIslandCategory.IsochronousCapable),
					from(DerCapacityModelAccessor::getIntentionalIslandCategoriesRating))
			;
		// @formatter:on
	}

	@Test
	public void settings() {
		// GIVEN
		DerCapacityModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Active power")
			.returns(null, from(DerCapacityModelAccessor::getActivePowerMaximum))
			.as("Over-excited power factor")
			.returns(null, from(DerCapacityModelAccessor::getOverExcitedPowerFactor))
			.as("Apparent power")
			.returns(null, from(DerCapacityModelAccessor::getApparentPowerMaximum))
			.as("Voltage nominal")
			.returns(null, from(DerCapacityModelAccessor::getVoltageNominal))
			.as("Current maximum")
			.returns(null, from(DerCapacityModelAccessor::getCurrentMaximum))
			.as("Intentional island categories")
			.returns(Set.of(), from(DerCapacityModelAccessor::getIntentionalIslandCategories))
			;
		// @formatter:on
	}

	@Test
	public void writeSettings() throws IOException {
		// GIVEN
		StaticDataMapModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		DerCapacityModelAccessor model = discoverModel(conn);

		// WHEN
		model.setActivePowerMaximum(conn, new BigDecimal("7000"));
		model.setActivePowerOverExcited(conn, new BigDecimal("3000"));
		model.setOverExcitedPowerFactor(conn, 0.9f);
		model.setActivePowerUnderExcited(conn, new BigDecimal("2900"));
		model.setUnderExcitedPowerFactor(conn, 0.85f);
		model.setApparentPowerMaximum(conn, new BigDecimal("7500"));
		model.setReactivePowerInjectedMaximum(conn, new BigDecimal("4000"));
		model.setReactivePowerAbsorbedMaximum(conn, new BigDecimal("3900"));
		model.setActivePowerChargeRateMaximum(conn, new BigDecimal("6000"));
		model.setActivePowerDischargeRateMaximum(conn, new BigDecimal("6500"));
		model.setApparentPowerChargeRateMaximum(conn, new BigDecimal("6100"));
		model.setApparentPowerDischargeRateMaximum(conn, new BigDecimal("6600"));
		model.setVoltageNominal(conn, 240f);
		model.setVoltageMaximum(conn, 260f);
		model.setVoltageMinimum(conn, 210f);
		model.setCurrentMaximum(conn, 30f);
		model.setIntentionalIslandCategories(conn,
				Set.of(DerIntentionalIslandCategory.IntentionalIslandCapable,
						DerIntentionalIslandCategory.BlackStartCapable));

		// THEN
		// @formatter:off
		then(model.getActivePowerMaximum())
			.as("Model data updated")
			.isEqualTo(new BigDecimal("7000"))
			;
		// @formatter:on

		DerCapacityModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Active power")
			.returns(new BigDecimal("7000"), from(DerCapacityModelAccessor::getActivePowerMaximum))
			.as("Active power over-excited")
			.returns(new BigDecimal("3000"), from(DerCapacityModelAccessor::getActivePowerOverExcited))
			.as("Over-excited power factor")
			.returns(0.9f, from(DerCapacityModelAccessor::getOverExcitedPowerFactor))
			.as("Active power under-excited")
			.returns(new BigDecimal("2900"), from(DerCapacityModelAccessor::getActivePowerUnderExcited))
			.as("Under-excited power factor")
			.returns(0.85f, from(DerCapacityModelAccessor::getUnderExcitedPowerFactor))
			.as("Apparent power")
			.returns(new BigDecimal("7500"), from(DerCapacityModelAccessor::getApparentPowerMaximum))
			.as("Reactive power injected")
			.returns(new BigDecimal("4000"), from(DerCapacityModelAccessor::getReactivePowerInjectedMaximum))
			.as("Reactive power absorbed")
			.returns(new BigDecimal("3900"), from(DerCapacityModelAccessor::getReactivePowerAbsorbedMaximum))
			.as("Active power charge rate")
			.returns(new BigDecimal("6000"), from(DerCapacityModelAccessor::getActivePowerChargeRateMaximum))
			.as("Active power discharge rate")
			.returns(new BigDecimal("6500"), from(DerCapacityModelAccessor::getActivePowerDischargeRateMaximum))
			.as("Apparent power charge rate")
			.returns(new BigDecimal("6100"), from(DerCapacityModelAccessor::getApparentPowerChargeRateMaximum))
			.as("Apparent power discharge rate")
			.returns(new BigDecimal("6600"), from(DerCapacityModelAccessor::getApparentPowerDischargeRateMaximum))
			.as("Voltage nominal")
			.returns(240.0f, from(DerCapacityModelAccessor::getVoltageNominal))
			.as("Voltage maximum")
			.returns(260.0f, from(DerCapacityModelAccessor::getVoltageMaximum))
			.as("Voltage minimum")
			.returns(210.0f, from(DerCapacityModelAccessor::getVoltageMinimum))
			.as("Current maximum")
			.returns(30.0f, from(DerCapacityModelAccessor::getCurrentMaximum))
			.as("Intentional island categories")
			.returns(Set.of(DerIntentionalIslandCategory.IntentionalIslandCapable,
					DerIntentionalIslandCategory.BlackStartCapable),
					from(DerCapacityModelAccessor::getIntentionalIslandCategories))
			;
		// @formatter:on
	}

	@Test
	public void writeSetting_outOfRange() throws IOException {
		// GIVEN
		StaticDataMapModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		DerCapacityModelAccessor model = discoverModel(conn);

		// WHEN
		Throwable t = catchThrowable(() -> model.setActivePowerMaximum(conn, new BigDecimal("70000")));

		// THEN
		// @formatter:off
		then(t)
			.as("Value larger than uint16 rejected")
			.isInstanceOf(IllegalArgumentException.class)
			;
		then(discoverModel(conn).getActivePowerMaximum())
			.as("Device not updated")
			.isNull()
			;
		// @formatter:on
	}

}
