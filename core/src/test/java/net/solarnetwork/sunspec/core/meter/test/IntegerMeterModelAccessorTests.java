/* ==================================================================
 * IntegerMeterModelAccessorTests.java - 22/05/2018 1:36:13 PM
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

package net.solarnetwork.sunspec.core.meter.test;

import static net.solarnetwork.sunspec.api.AcPhase.PhaseA;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseB;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseC;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.ModelRegister;
import net.solarnetwork.sunspec.api.meter.IntegerMeterModelRegister;
import net.solarnetwork.sunspec.api.meter.MeterModelAccessor;
import net.solarnetwork.sunspec.api.meter.MeterModelId;
import net.solarnetwork.sunspec.core.meter.IntegerMeterModelAccessor;
import net.solarnetwork.sunspec.modbus.support.ModbusData.ModbusDataUpdateAction;
import net.solarnetwork.sunspec.modbus.support.ModbusData.MutableModbusData;
import net.solarnetwork.sunspec.modbus.support.ModelData;
import net.solarnetwork.sunspec.test.DataUtils;

/**
 * Test cases for the {@link IntegerMeterModelAccessor} class.
 *
 * @author matt
 * @version 1.0
 */
public class IntegerMeterModelAccessorTests {

	// @formatter:off
	public static final short[] INT_METER_MODEL_HEADER_69 = new short[] {
			0x00CB,
			0x0069,
	};

	public static final short[] INT_METER_MODEL_71 = new short[] {
			0x0038,
			0x0013,
			0x0012,
			0x0012,
			(short)0xFFFF,
			0x04D0,
			0x0531,
			0x03E6,
			0x055A,
			0x0847,
			0x0848,
			0x0840,
			0x084E,
			(short)0xFFFF,
			0x1768,
			(short)0xFFFE,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0002,
			0x0007,
			0x0003,
			0x0002,
			0x0003,
			0x0002,
			0x0007,
			0x0002,
			0x0002,
			0x0003,
			0x0002,
			0x000A,
			(short)0xFAC2,
			0x00FC,
			0x0497,
			(short)0xFFFC,
			0x0000,
			0x2A94,
			0x0000,
			0x276A,
			0x0000,
			0x0150,
			0x0000,
			0x01D9,
			0x0098,
			(short)0xD172,
			0x0035,
			0x7C10,
			0x0029,
			(short)0xAB62,
			0x0039,
			(short)0xAA00,
			0x0002,
			0x0000,
			(short)0x9C67,
			0x0001,
			0x3324,
			0x0000,
			0x02A4,
			0x0000,
			0x0315,
			0x009F,
			(short)0xE2B1,
			0x0038,
			(short)0x8E20,
			0x002B,
			(short)0x9D4B,
			0x003C,
			0x45DC,
			0x0002,
			0x0018,
			(short)0xBDFF,
			0x000E,
			0x7389,
			0x0005,
			0x37F4,
			0x0005,
			0x1281,
			0x0001,
			0x31B4,
			0x0001,
			0x2EC0,
			0x0000,
			0x0171,
			0x0000,
			0x0182,
			0x0000,
			0x0051,
			0x0000,
			0x0050,
			0x0000,
			0x0000,
			0x0000,
			0x0000,
			0x0006,
			(short)0xC73F,
			0x0000,
			0x0000,
			0x0001,
			(short)0x9307,
			0x0005,
			0x3437,
			0x0002,
			0x0000,
			0x0018,
	};

	public static final short[] END_OF_MODEL_176 = new short[] {
			(short)0xFFFF,
			0x0000,
	};

	// @formatter:on

	private static final Logger log = LoggerFactory.getLogger(IntegerMeterModelAccessorTests.class);

	private ModelData getTestDataInstance() {
		final int baseAddress = ModelRegister.BaseAddress.getAddress();
		ModelData data = new ModelData(baseAddress + 2);
		try {
			data.performUpdates(new ModbusDataUpdateAction() {

				@Override
				public boolean updateModbusData(MutableModbusData m) {
					m.saveDataArray(DataUtils.commonModel02(), baseAddress + 2);
					m.saveDataArray(INT_METER_MODEL_HEADER_69, baseAddress + 69);
					m.saveDataArray(INT_METER_MODEL_71, baseAddress + 71);
					m.saveDataArray(END_OF_MODEL_176, baseAddress + 176);
					return true;
				}
			});
			data.addModel(IntegerMeterModelAccessor.FIXED_BLOCK_LENGTH, new IntegerMeterModelAccessor(
					data, 40069, MeterModelId.WyeConnectThreePhaseMeterInteger));
		} catch ( IOException e ) {
			throw new RuntimeException(e);
		}
		return data;
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
			.returns("Veris Industries", from(CommonModelAccessor::getManufacturer))
			.as("Model name")
			.returns("E51C2", from(CommonModelAccessor::getModelName))
			.as("Options")
			.returns("None", from(CommonModelAccessor::getOptions))
			.as("Version")
			.returns("2.103", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("4E390476", from(CommonModelAccessor::getSerialNumber))
			.as("Device address")
			.returns(10, from(CommonModelAccessor::getDeviceAddress))
			;
		// @formatter:on
	}

	@Test
	public void findTypedModel() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		MeterModelAccessor meterAccessor = data.findTypedModel(MeterModelAccessor.class);

		// THEN
		// @formatter:off
		then(meterAccessor)
			.as("Model found by accessor type")
			.isInstanceOf(IntegerMeterModelAccessor.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(40069, from(MeterModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(40071, from(MeterModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(MeterModelId.WyeConnectThreePhaseMeterInteger, from(MeterModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(105, from(MeterModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(MeterModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(105, from(MeterModelAccessor::getModelLength))
			.as("Model length")
			.returns(0, from(MeterModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void events() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// WHEN
		Set<? extends ModelEvent> events = model.getEvents();
		BitSet bitset = new BitSet();
		events.stream().mapToInt(ModelEvent::getIndex).forEach(i -> bitset.set(i));

		// THEN
		// @formatter:off
		then(bitset.cardinality())
			.as("Event count")
			.isEqualTo(2)
			;
		then(bitset.get(3))
			.as("Under voltage event")
			.isTrue()
			;
		then(bitset.get(4))
			.as("Low power factor event")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void current() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(5.6f, from(MeterModelAccessor::getCurrent))
			.as("Phase A")
			.returns(1.9f, from(m -> m.accessorForPhase(PhaseA).getCurrent()))
			.as("Phase B")
			.returns(1.8f, from(m -> m.accessorForPhase(PhaseB).getCurrent()))
			.as("Phase C")
			.returns(1.8f, from(m -> m.accessorForPhase(PhaseC).getCurrent()))
			;
		// @formatter:on
	}

	@Test
	public void voltageLN() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Average")
			.returns(123.2f, from(MeterModelAccessor::getVoltage))
			.as("Phase A")
			.returns(132.9f, from(m -> m.accessorForPhase(PhaseA).getVoltage()))
			.as("Phase B")
			.returns(99.8f, from(m -> m.accessorForPhase(PhaseB).getVoltage()))
			.as("Phase C")
			.returns(137.0f, from(m -> m.accessorForPhase(PhaseC).getVoltage()))
			;
		// @formatter:on
	}

	@Test
	public void voltageLL() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();
		IntegerMeterModelAccessor imm = (IntegerMeterModelAccessor) model;

		// THEN
		// @formatter:off
		then(imm.getVoltageValue(IntegerMeterModelRegister.VoltageLineLineAverage))
			.as("Average")
			.isEqualTo(211.9f)
			;
		then(imm.getVoltageValue(IntegerMeterModelRegister.VoltagePhaseAPhaseB))
			.as("Phase A")
			.isEqualTo(212.0f)
			;
		then(imm.getVoltageValue(IntegerMeterModelRegister.VoltagePhaseBPhaseC))
			.as("Phase B")
			.isEqualTo(211.2f)
			;
		then(imm.getVoltageValue(IntegerMeterModelRegister.VoltagePhaseCPhaseA))
			.as("Phase C")
			.isEqualTo(212.6f)
			;
		// @formatter:on
	}

	@Test
	public void frequency() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model.getFrequency())
			.as("Frequency")
			.isEqualTo(59.92f)
			;
		// @formatter:on
	}

	@Test
	public void activePower() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(BigDecimal.ZERO, from(MeterModelAccessor::getActivePower))
			.as("Phase A")
			.returns(BigDecimal.ZERO, from(m -> m.accessorForPhase(PhaseA).getActivePower()))
			.as("Phase B")
			.returns(BigDecimal.ZERO, from(m -> m.accessorForPhase(PhaseB).getActivePower()))
			.as("Phase C")
			.returns(BigDecimal.ZERO, from(m -> m.accessorForPhase(PhaseC).getActivePower()))
			;
		// @formatter:on
	}

	@Test
	public void apparentPower() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("700"), from(MeterModelAccessor::getApparentPower))
			.as("Phase A")
			.returns(new BigDecimal("300"), from(m -> m.accessorForPhase(PhaseA).getApparentPower()))
			.as("Phase B")
			.returns(new BigDecimal("200"), from(m -> m.accessorForPhase(PhaseB).getApparentPower()))
			.as("Phase C")
			.returns(new BigDecimal("300"), from(m -> m.accessorForPhase(PhaseC).getApparentPower()))
			;
		// @formatter:on
	}

	@Test
	public void reactivePower() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("700"), from(MeterModelAccessor::getReactivePower))
			.as("Phase A")
			.returns(new BigDecimal("200"), from(m -> m.accessorForPhase(PhaseA).getReactivePower()))
			.as("Phase B")
			.returns(new BigDecimal("200"), from(m -> m.accessorForPhase(PhaseB).getReactivePower()))
			.as("Phase C")
			.returns(new BigDecimal("300"), from(m -> m.accessorForPhase(PhaseC).getReactivePower()))
			;
		// @formatter:on
	}

	@Test
	public void powerFactor() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Average")
			.returns(0.001f, from(MeterModelAccessor::getPowerFactor))
			.as("Phase A")
			.returns(-0.1342f, from(m -> m.accessorForPhase(PhaseA).getPowerFactor()))
			.as("Phase B")
			.returns(0.0252f, from(m -> m.accessorForPhase(PhaseB).getPowerFactor()))
			.as("Phase C")
			.returns(0.1175f, from(m -> m.accessorForPhase(PhaseC).getPowerFactor()))
			;
		// @formatter:on
	}

	@Test
	public void activeEnergyExport() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("1090000"), from(MeterModelAccessor::getActiveEnergyExported))
			.as("Phase A")
			.returns(new BigDecimal("1009000"), from(m -> m.accessorForPhase(PhaseA).getActiveEnergyExported()))
			.as("Phase B")
			.returns(new BigDecimal("33600"), from(m -> m.accessorForPhase(PhaseB).getActiveEnergyExported()))
			.as("Phase C")
			.returns(new BigDecimal("47300"), from(m -> m.accessorForPhase(PhaseC).getActiveEnergyExported()))
			;
		// @formatter:on
	}

	@Test
	public void activeEnergyExportReversed() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// WHEN
		model = model.reversed();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("1090000"), from(MeterModelAccessor::getActiveEnergyImported))
			.as("Phase A")
			.returns(new BigDecimal("1009000"), from(m -> m.accessorForPhase(PhaseA).getActiveEnergyImported()))
			.as("Phase B")
			.returns(new BigDecimal("33600"), from(m -> m.accessorForPhase(PhaseB).getActiveEnergyImported()))
			.as("Phase C")
			.returns(new BigDecimal("47300"), from(m -> m.accessorForPhase(PhaseC).getActiveEnergyImported()))
			;
		// @formatter:on
	}

	@Test
	public void activeEnergyImport() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("1001509000"), from(MeterModelAccessor::getActiveEnergyImported))
			.as("Phase A")
			.returns(new BigDecimal("350516800"), from(m -> m.accessorForPhase(PhaseA).getActiveEnergyImported()))
			.as("Phase B")
			.returns(new BigDecimal("273085000"), from(m -> m.accessorForPhase(PhaseB).getActiveEnergyImported()))
			.as("Phase C")
			.returns(new BigDecimal("377907200"), from(m -> m.accessorForPhase(PhaseC).getActiveEnergyImported()))
			;
		// @formatter:on
	}

	@Test
	public void activeEnergyImportReversed() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// WHEN
		model = model.reversed();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("1001509000"), from(MeterModelAccessor::getActiveEnergyExported))
			.as("Phase A")
			.returns(new BigDecimal("350516800"), from(m -> m.accessorForPhase(PhaseA).getActiveEnergyExported()))
			.as("Phase B")
			.returns(new BigDecimal("273085000"), from(m -> m.accessorForPhase(PhaseB).getActiveEnergyExported()))
			.as("Phase C")
			.returns(new BigDecimal("377907200"), from(m -> m.accessorForPhase(PhaseC).getActiveEnergyExported()))
			;
		// @formatter:on
	}

	@Test
	public void apparentEnergyExport() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("4003900"), from(MeterModelAccessor::getApparentEnergyExported))
			.as("Phase A")
			.returns(new BigDecimal("7862800"), from(m -> m.accessorForPhase(PhaseA).getApparentEnergyExported()))
			.as("Phase B")
			.returns(new BigDecimal("67600"), from(m -> m.accessorForPhase(PhaseB).getApparentEnergyExported()))
			.as("Phase C")
			.returns(new BigDecimal("78900"), from(m -> m.accessorForPhase(PhaseC).getApparentEnergyExported()))
			;
		// @formatter:on
	}

	@Test
	public void apparentEnergyExportReversed() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// WHEN
		model = model.reversed();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("4003900"), from(MeterModelAccessor::getApparentEnergyImported))
			.as("Phase A")
			.returns(new BigDecimal("7862800"), from(m -> m.accessorForPhase(PhaseA).getApparentEnergyImported()))
			.as("Phase B")
			.returns(new BigDecimal("67600"), from(m -> m.accessorForPhase(PhaseB).getApparentEnergyImported()))
			.as("Phase C")
			.returns(new BigDecimal("78900"), from(m -> m.accessorForPhase(PhaseC).getApparentEnergyImported()))
			;
		// @formatter:on
	}

	@Test
	public void apparentEnergyImport() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("1047825700"), from(MeterModelAccessor::getApparentEnergyImported))
			.as("Phase A")
			.returns(new BigDecimal("370640000"), from(m -> m.accessorForPhase(PhaseA).getApparentEnergyImported()))
			.as("Phase B")
			.returns(new BigDecimal("285831500"), from(m -> m.accessorForPhase(PhaseB).getApparentEnergyImported()))
			.as("Phase C")
			.returns(new BigDecimal("395004400"), from(m -> m.accessorForPhase(PhaseC).getApparentEnergyImported()))
			;
		// @formatter:on
	}

	@Test
	public void apparentEnergyImportReversed() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// WHEN
		model = model.reversed();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(new BigDecimal("1047825700"), from(MeterModelAccessor::getApparentEnergyExported))
			.as("Phase A")
			.returns(new BigDecimal("370640000"), from(m -> m.accessorForPhase(PhaseA).getApparentEnergyExported()))
			.as("Phase B")
			.returns(new BigDecimal("285831500"), from(m -> m.accessorForPhase(PhaseB).getApparentEnergyExported()))
			.as("Phase C")
			.returns(new BigDecimal("395004400"), from(m -> m.accessorForPhase(PhaseC).getApparentEnergyExported()))
			;
		// @formatter:on
	}

	@Test
	public void reactiveEnergyImport() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(BigDecimal.valueOf((0x18BDFFL + 0x131B4L) * 100L), from(MeterModelAccessor::getReactiveEnergyImported))
			.as("Phase A")
			.returns(BigDecimal.valueOf((0xE7389L + 0x12EC0L) * 100L),
					from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyImported()))
			.as("Phase B")
			.returns(BigDecimal.valueOf((0x537F4L + 0x171L) * 100L),
					from(m -> m.accessorForPhase(PhaseB).getReactiveEnergyImported()))
			.as("Phase C")
			.returns(BigDecimal.valueOf((0x51281L + 0x182) * 100L),
					from(m -> m.accessorForPhase(PhaseC).getReactiveEnergyImported()))
			;
		// @formatter:on
	}

	@Test
	public void reactiveEnergyImportReversed() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// WHEN
		model = model.reversed();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(BigDecimal.valueOf((0x18BDFFL + 0x131B4L) * 100L), from(MeterModelAccessor::getReactiveEnergyExported))
			.as("Phase A")
			.returns(BigDecimal.valueOf((0xE7389L + 0x12EC0L) * 100L),
					from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyExported()))
			.as("Phase B")
			.returns(BigDecimal.valueOf((0x537F4L + 0x171L) * 100L),
					from(m -> m.accessorForPhase(PhaseB).getReactiveEnergyExported()))
			.as("Phase C")
			.returns(BigDecimal.valueOf((0x51281L + 0x182) * 100L),
					from(m -> m.accessorForPhase(PhaseC).getReactiveEnergyExported()))
			;
		// @formatter:on
	}

	@Test
	public void reactiveEnergyExport() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(BigDecimal.valueOf((0x51L + 0x6C73FL) * 100L), from(MeterModelAccessor::getReactiveEnergyExported))
			.as("Phase A")
			.returns(BigDecimal.valueOf((0x50L + 0x0L) * 100L),
					from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyExported()))
			.as("Phase B")
			.returns(BigDecimal.valueOf((0x0L + 0x19307L) * 100L),
					from(m -> m.accessorForPhase(PhaseB).getReactiveEnergyExported()))
			.as("Phase C")
			.returns(BigDecimal.valueOf((0x0L + 0x53437L) * 100L),
					from(m -> m.accessorForPhase(PhaseC).getReactiveEnergyExported()))
			;
		// @formatter:on
	}

	@Test
	public void reactiveEnergyExportReversed() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// WHEN
		model = model.reversed();

		// THEN
		// @formatter:off
		then(model)
			.as("Total")
			.returns(BigDecimal.valueOf((0x51L + 0x6C73FL) * 100L), from(MeterModelAccessor::getReactiveEnergyImported))
			.as("Phase A")
			.returns(BigDecimal.valueOf((0x50L + 0x0L) * 100L),
					from(m -> m.accessorForPhase(PhaseA).getReactiveEnergyImported()))
			.as("Phase B")
			.returns(BigDecimal.valueOf((0x0L + 0x19307L) * 100L),
					from(m -> m.accessorForPhase(PhaseB).getReactiveEnergyImported()))
			.as("Phase C")
			.returns(BigDecimal.valueOf((0x0L + 0x53437L) * 100L),
					from(m -> m.accessorForPhase(PhaseC).getReactiveEnergyImported()))
			;
		// @formatter:on
	}
}
