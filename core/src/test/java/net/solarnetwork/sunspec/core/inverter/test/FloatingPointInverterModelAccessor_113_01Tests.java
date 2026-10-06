/* ==================================================================
 * FloatingPointInverterModelAccessor_113_01Tests.java - 11/10/2019 6:01:49 pm
 * 
 * Copyright 2019 SolarNetwork.net Dev Team
 * 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 * 02111-1307 USA
 * ==================================================================
 */

package net.solarnetwork.sunspec.core.inverter.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.inverter.InverterBasicSettingsModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterControlModelId;
import net.solarnetwork.sunspec.core.inverter.FloatingPointInverterModelAccessor;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link InverterBasicSettingsModelAccessor} class on a
 * {@link FloatingPointInverterModelAccessor}.
 * 
 * @author matt
 * @version 1.0
 */
public class FloatingPointInverterModelAccessor_113_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(FloatingPointInverterModelAccessorTests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-113-01.txt");
	}

	@Test
	public void dataDebugString() {
		ModelData data = getTestDataInstance();
		log.debug("Got test data: " + data.dataDebugString());
	}

	@Test
	public void block() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(159, from(InverterBasicSettingsModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(161, from(InverterBasicSettingsModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(InverterControlModelId.BasicSettings,
					from(InverterBasicSettingsModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(30, from(InverterBasicSettingsModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(InverterBasicSettingsModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(30, from(InverterBasicSettingsModelAccessor::getModelLength))
			.as("Model length")
			.returns(0, from(InverterBasicSettingsModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void activePowerMaximum() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getActivePowerMaximum())
			.as("Active power max")
			.isEqualTo(3000)
			;
		// @formatter:on
	}

	@Test
	public void pccVoltage() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getPccVoltage())
			.as("PCC voltage")
			.isEqualTo(240.0f)
			;
		// @formatter:on
	}

	@Test
	public void pccVoltageOffset() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getPccVoltageOffset())
			.as("PCC voltage offset")
			.isEqualTo(0.0f)
			;
		// @formatter:on
	}

	@Test
	public void voltageMax() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getVoltageMaximum())
			.as("Voltage max")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void voltageMin() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getVoltageMinimum())
			.as("Voltage min")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void apparentPowerMax() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getApparentPowerMaximum())
			.as("VA max")
			.isEqualTo(3000)
			;
		// @formatter:on
	}

	@Test
	public void reactivePowerQ1Max() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getReactivePowerQ1Maximum())
			.as("VAR Q1 max")
			.isEqualTo(2140)
			;
		// @formatter:on
	}

	@Test
	public void reactivePowerQ2Max() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getReactivePowerQ2Maximum())
			.as("VAR Q2 max")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void reactivePowerQ3Max() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getReactivePowerQ3Maximum())
			.as("VAR Q3 max")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void reactivePowerQ4Max() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getReactivePowerQ4Maximum())
			.as("VAR Q4 max")
			.isEqualTo(-2140)
			;
		// @formatter:on
	}

	@Test
	public void activePowerRampRate() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getActivePowerRampRate())
			.as("Active power ramp rate")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void powerFactorQ1Minimum() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getPowerFactorQ1Minimum())
			.as("Power factor Q1 minimum")
			.isEqualTo(-0.850f)
			;
		// @formatter:on
	}

	@Test
	public void powerFactorQ2Minimum() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getPowerFactorQ2Minimum())
			.as("Power factor Q2 minimum")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void powerFactorQ3Minimum() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getPowerFactorQ3Minimum())
			.as("Power factor Q3 minimum")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void powerFactorQ4Minimum() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getPowerFactorQ4Minimum())
			.as("Power factor Q4 minimum")
			.isEqualTo(0.850f)
			;
		// @formatter:on
	}

	@Test
	public void importExportChangeReactivePowerAction() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getImportExportChangeReactivePowerAction())
			.as("Import export reactive power action")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void apparentPowerCalculationMethod() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getApparentPowerCalculationMethod())
			.as("Apparent power calculation method")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void ecpFrequency() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getEcpFrequency())
			.as("Frequency min")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void connectedPhase() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestDataInstance()
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(model.getConnectedPhase())
			.as("Connected phase")
			.isNull()
			;
		// @formatter:on
	}

}
