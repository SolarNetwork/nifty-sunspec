/* ==================================================================
 * IntegerInverterModelAccessor_103_02Tests.java - 8/10/2018 7:06:15 AM
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

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.AcPhase;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterApparentPowerCalculationMethod;
import net.solarnetwork.sunspec.api.inverter.InverterBasicSettingsModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterControlModelId;
import net.solarnetwork.sunspec.api.inverter.InverterReactivePowerAction;
import net.solarnetwork.sunspec.core.inverter.InverterBasicSettingsModelAccessorImpl;
import net.solarnetwork.sunspec.core.meter.test.IntegerMeterModelAccessorTests;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link InverterBasicSettingsModelAccessor} class.
 * 
 * @author matt
 * @version 1.0
 */
public class InverterBasicSettingsModelAccessorImpl_101_01Tests {

	private static final Logger log = LoggerFactory.getLogger(IntegerMeterModelAccessorTests.class);

	private static final String TEST_DATA = "test-data-101-01.txt";

	/** The model block address in the test data. */
	private static final int BLOCK_ADDRESS = 151;

	/** Synthetic values for the whole model block. */
	// @formatter:off
	private static final int[] SYNTHETIC_BLOCK = new int[] {
			0x0474, // WMax
			0x0960, // VRef
			0xFFE7, // VRefOfs
			0x0A50, // VMax
			0x0840, // VMin
			0x0474, // VAMax
			0x0258, // VArMaxQ1
			0x8000, // VArMaxQ2
			0xFED4, // VArMaxQ3
			0xFDA8, // VArMaxQ4
			0x03E8, // WGra
			0xFCAE, // PFMinQ1
			0x8000, // PFMinQ2
			0xFC7C, // PFMinQ3
			0x0352, // PFMinQ4
			0x0002, // VArAct
			0x0001, // ClcTotVA
			0x01F4, // MaxRmpRte
			0x1770, // ECPNomHz
			0x0002, // ConnPh
			0x0001, // WMax_SF
			0xFFFF, // VRef_SF
			0xFFFF, // VRefOfs_SF
			0xFFFF, // VMinMax_SF
			0x0001, // VAMax_SF
			0x0001, // VArMax_SF
			0xFFFE, // WGra_SF
			0xFFFD, // PFMin_SF
			0xFFFF, // MaxRmpRte_SF
			0xFFFE, // ECPNomHz_SF
	};
	// @formatter:on

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA);
	}

	private InverterBasicSettingsModelAccessor getTestModel(int address, int... words) {
		return ModelDataUtils.getModelDataInstanceWithRegisters(getClass(), TEST_DATA, address, words)
				.findTypedModel(InverterBasicSettingsModelAccessor.class);
	}

	private static InverterBasicSettingsModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(InverterBasicSettingsModelAccessor.class);
	}

	@Test
	public void dataDebugString() {
		ModelData data = getTestDataInstance();
		log.debug("Got test data: " + data.dataDebugString());
	}

	@Test
	public void commonModelProperties() {
		// GIVEN
		CommonModelAccessor data = getTestDataInstance();

		// THEN
		// @formatter:off
		then(data)
			.as("Manufacturer")
			.returns("Fronius", from(CommonModelAccessor::getManufacturer))
			.as("Model name")
			.returns("IG+V11.4", from(CommonModelAccessor::getModelName))
			.as("Options")
			.returns("2.1.18", from(CommonModelAccessor::getOptions))
			.as("Version")
			.returns("5.10.0", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("50.213262", from(CommonModelAccessor::getSerialNumber))
			.as("Device address")
			.returns(5, from(CommonModelAccessor::getDeviceAddress))
			;
		// @formatter:on
	}

	@Test
	public void findTypedModel() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		InverterBasicSettingsModelAccessor accessor = data
				.findTypedModel(InverterBasicSettingsModelAccessor.class);

		// THEN
		// @formatter:off
		then(accessor)
			.as("Model found by accessor type")
			.isInstanceOf(InverterBasicSettingsModelAccessorImpl.class)
			;
		// @formatter:on
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
			.returns(149, from(InverterBasicSettingsModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(151, from(InverterBasicSettingsModelAccessor::getBlockAddress))
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
			.isEqualTo(new BigDecimal("11400"))
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
			.isEqualTo(269.0f)
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
			.isEqualTo(206.0f)
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
			.isEqualTo(new BigDecimal("11400"))
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
			.isEqualTo(new BigDecimal("6000"))
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
			.isEqualTo(new BigDecimal("-6000"))
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
			.as("ECP frequency")
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

	@Test
	public void syntheticValues() {
		// GIVEN
		InverterBasicSettingsModelAccessor model = getTestModel(BLOCK_ADDRESS, SYNTHETIC_BLOCK);

		// THEN
		// @formatter:off
		then(model)
			.as("Active power max")
			.returns(new BigDecimal("11400"), from(InverterBasicSettingsModelAccessor::getActivePowerMaximum))
			.as("PCC voltage")
			.returns(240.0f, from(InverterBasicSettingsModelAccessor::getPccVoltage))
			.as("Negative PCC voltage offset")
			.returns(-2.5f, from(InverterBasicSettingsModelAccessor::getPccVoltageOffset))
			.as("Voltage max")
			.returns(264.0f, from(InverterBasicSettingsModelAccessor::getVoltageMaximum))
			.as("Voltage min")
			.returns(211.2f, from(InverterBasicSettingsModelAccessor::getVoltageMinimum))
			.as("VA max")
			.returns(new BigDecimal("11400"), from(InverterBasicSettingsModelAccessor::getApparentPowerMaximum))
			.as("VAR Q1 max")
			.returns(new BigDecimal("6000"), from(InverterBasicSettingsModelAccessor::getReactivePowerQ1Maximum))
			.as("VAR Q2 max not implemented")
			.returns(null, from(InverterBasicSettingsModelAccessor::getReactivePowerQ2Maximum))
			.as("VAR Q3 max")
			.returns(new BigDecimal("-3000"), from(InverterBasicSettingsModelAccessor::getReactivePowerQ3Maximum))
			.as("VAR Q4 max")
			.returns(new BigDecimal("-6000"), from(InverterBasicSettingsModelAccessor::getReactivePowerQ4Maximum))
			.as("Active power ramp rate")
			.returns(10.0f, from(InverterBasicSettingsModelAccessor::getActivePowerRampRate))
			.as("Power factor Q1 minimum")
			.returns(-0.85f, from(InverterBasicSettingsModelAccessor::getPowerFactorQ1Minimum))
			.as("Power factor Q2 minimum not implemented")
			.returns(null, from(InverterBasicSettingsModelAccessor::getPowerFactorQ2Minimum))
			.as("Power factor Q3 minimum")
			.returns(-0.9f, from(InverterBasicSettingsModelAccessor::getPowerFactorQ3Minimum))
			.as("Power factor Q4 minimum")
			.returns(0.85f, from(InverterBasicSettingsModelAccessor::getPowerFactorQ4Minimum))
			.as("Import export reactive power action")
			.returns(InverterReactivePowerAction.Maintain,
					from(InverterBasicSettingsModelAccessor::getImportExportChangeReactivePowerAction))
			.as("Apparent power calculation method")
			.returns(InverterApparentPowerCalculationMethod.Vector,
					from(InverterBasicSettingsModelAccessor::getApparentPowerCalculationMethod))
			;
		then(model.getApparentPowerCalculationMethod().getDescription())
			.as("Apparent power calculation method description")
			.isEqualTo("Vector")
			;
		then(model)
			.as("Active power ramp rate max with negative scale factor")
			.returns(50.0f, from(InverterBasicSettingsModelAccessor::getActivePowerRampRateMaximum))
			.as("ECP frequency with negative scale factor")
			.returns(60.0f, from(InverterBasicSettingsModelAccessor::getEcpFrequency))
			.as("Connected phase read from the model block")
			.returns(AcPhase.PhaseB, from(InverterBasicSettingsModelAccessor::getConnectedPhase))
			;
		// @formatter:on
	}

	@Test
	public void pccVoltageOffset_notImplemented() {
		// @formatter:off
		then(getTestModel(BLOCK_ADDRESS + 2, 0x8000).getPccVoltageOffset())
			.as("PCC voltage offset not implemented")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void enumValues_undefinedCodes() {
		// GIVEN
		// VArAct and ClcTotVA codes SunSpec does not define
		InverterBasicSettingsModelAccessor model = getTestModel(BLOCK_ADDRESS + 15, 0x0000, 0x0003);

		// THEN
		// @formatter:off
		then(model)
			.as("Undefined reactive power action not available")
			.returns(null,
					from(InverterBasicSettingsModelAccessor::getImportExportChangeReactivePowerAction))
			.as("Undefined apparent power calculation method not available")
			.returns(null, from(InverterBasicSettingsModelAccessor::getApparentPowerCalculationMethod))
			;
		// @formatter:on
	}

	@Test
	public void writeValues() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnectionWithRegisters(
				getClass(), TEST_DATA, BLOCK_ADDRESS, SYNTHETIC_BLOCK);
		InverterBasicSettingsModelAccessor model = discoverModel(conn);

		// WHEN
		model.setActivePowerMaximum(conn, new BigDecimal("9000"));
		model.setPccVoltage(conn, 230.5f);
		model.setPccVoltageOffset(conn, -1.5f);
		model.setVoltageMaximum(conn, 253.0f);
		model.setVoltageMinimum(conn, 207.0f);
		model.setApparentPowerMaximum(conn, new BigDecimal("10000"));
		model.setReactivePowerQ1Maximum(conn, new BigDecimal("5000"));
		model.setReactivePowerQ2Maximum(conn, new BigDecimal("4000"));
		model.setReactivePowerQ3Maximum(conn, new BigDecimal("-4000"));
		model.setReactivePowerQ4Maximum(conn, new BigDecimal("-5000"));
		model.setActivePowerRampRate(conn, 12.5f);
		model.setPowerFactorQ1Minimum(conn, -0.9f);
		model.setPowerFactorQ2Minimum(conn, 0.95f);
		model.setPowerFactorQ3Minimum(conn, -0.95f);
		model.setPowerFactorQ4Minimum(conn, 0.9f);
		model.setImportExportChangeReactivePowerAction(conn, InverterReactivePowerAction.Switch);
		model.setApparentPowerCalculationMethod(conn, InverterApparentPowerCalculationMethod.Arithmetic);
		model.setActivePowerRampRateMaximum(conn, 75.0f);
		model.setEcpFrequency(conn, 50.0f);
		model.setConnectedPhase(conn, AcPhase.PhaseC);

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Each point written to its own register, in order")
			.isEqualTo(IntStream.rangeClosed(BLOCK_ADDRESS, BLOCK_ADDRESS
					+ 19).mapToObj(a -> List.of(a, 1)).toList())
			;
		// @formatter:on

		InverterBasicSettingsModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Active power max")
			.returns(new BigDecimal("9000"), from(InverterBasicSettingsModelAccessor::getActivePowerMaximum))
			.as("PCC voltage")
			.returns(230.5f, from(InverterBasicSettingsModelAccessor::getPccVoltage))
			.as("PCC voltage offset")
			.returns(-1.5f, from(InverterBasicSettingsModelAccessor::getPccVoltageOffset))
			.as("Voltage max")
			.returns(253.0f, from(InverterBasicSettingsModelAccessor::getVoltageMaximum))
			.as("Voltage min")
			.returns(207.0f, from(InverterBasicSettingsModelAccessor::getVoltageMinimum))
			.as("VA max")
			.returns(new BigDecimal("10000"), from(InverterBasicSettingsModelAccessor::getApparentPowerMaximum))
			.as("VAR Q1 max")
			.returns(new BigDecimal("5000"), from(InverterBasicSettingsModelAccessor::getReactivePowerQ1Maximum))
			.as("VAR Q2 max")
			.returns(new BigDecimal("4000"), from(InverterBasicSettingsModelAccessor::getReactivePowerQ2Maximum))
			.as("VAR Q3 max")
			.returns(new BigDecimal("-4000"), from(InverterBasicSettingsModelAccessor::getReactivePowerQ3Maximum))
			.as("VAR Q4 max")
			.returns(new BigDecimal("-5000"), from(InverterBasicSettingsModelAccessor::getReactivePowerQ4Maximum))
			.as("Active power ramp rate")
			.returns(12.5f, from(InverterBasicSettingsModelAccessor::getActivePowerRampRate))
			.as("Power factor Q1 minimum")
			.returns(-0.9f, from(InverterBasicSettingsModelAccessor::getPowerFactorQ1Minimum))
			.as("Power factor Q2 minimum")
			.returns(0.95f, from(InverterBasicSettingsModelAccessor::getPowerFactorQ2Minimum))
			.as("Power factor Q3 minimum")
			.returns(-0.95f, from(InverterBasicSettingsModelAccessor::getPowerFactorQ3Minimum))
			.as("Power factor Q4 minimum")
			.returns(0.9f, from(InverterBasicSettingsModelAccessor::getPowerFactorQ4Minimum))
			.as("Import export reactive power action")
			.returns(InverterReactivePowerAction.Switch,
					from(InverterBasicSettingsModelAccessor::getImportExportChangeReactivePowerAction))
			.as("Apparent power calculation method")
			.returns(InverterApparentPowerCalculationMethod.Arithmetic,
					from(InverterBasicSettingsModelAccessor::getApparentPowerCalculationMethod))
			.as("Active power ramp rate max")
			.returns(75.0f, from(InverterBasicSettingsModelAccessor::getActivePowerRampRateMaximum))
			.as("ECP frequency")
			.returns(50.0f, from(InverterBasicSettingsModelAccessor::getEcpFrequency))
			.as("Connected phase")
			.returns(AcPhase.PhaseC, from(InverterBasicSettingsModelAccessor::getConnectedPhase))
			;
		// @formatter:on
	}

	@Test
	public void writeValue_scaleFactorNotImplemented() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		InverterBasicSettingsModelAccessor model = discoverModel(conn);

		// WHEN
		Throwable t = catchThrowable(() -> model.setEcpFrequency(conn, 60.0f));

		// THEN
		// @formatter:off
		then(t)
			.as("Scaled value without an implemented scale factor rejected")
			.isInstanceOf(IllegalStateException.class)
			;
		then(conn.getWrites())
			.as("Nothing written")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void writeConnectedPhase_total() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		InverterBasicSettingsModelAccessor model = discoverModel(conn);

		// WHEN
		Throwable t = catchThrowable(() -> model.setConnectedPhase(conn, AcPhase.Total));

		// THEN
		// @formatter:off
		then(t)
			.as("Total is not a connected phase")
			.isInstanceOf(IllegalArgumentException.class)
			;
		then(conn.getWrites())
			.as("Nothing written")
			.isEmpty()
			;
		// @formatter:on
	}

}
