/* ==================================================================
 * ModelDataFactoryTests.java - 22/05/2018 5:19:37 PM
 *
 * Copyright 2018 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.core.test;

import static net.solarnetwork.domain.AcPhase.PhaseA;
import static net.solarnetwork.domain.AcPhase.PhaseB;
import static net.solarnetwork.domain.AcPhase.PhaseC;
import static net.solarnetwork.sunspec.modbus.ModbusReadFunction.ReadHoldingRegister;
import static org.assertj.core.api.BDDAssertions.and;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.from;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import net.solarnetwork.sunspec.api.ModelRegister;
import net.solarnetwork.sunspec.api.meter.MeterModelAccessor;
import net.solarnetwork.sunspec.core.ModelDataFactory;
import net.solarnetwork.sunspec.core.meter.test.IntegerMeterModelAccessorTests;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.support.ModelData;
import net.solarnetwork.sunspec.modbus.support.StaticDataMapReadonlyModbusConnection;
import net.solarnetwork.sunspec.test.DataUtils;
import net.solarnetwork.util.ByteUtils;
import net.solarnetwork.util.IntShortMap;

/**
 * Test cases for the {@link ModelDataFactory} class.
 *
 * @author matt
 * @version 1.1
 */
@SuppressWarnings("static-access")
@ExtendWith(MockitoExtension.class)
public class ModelDataFactoryTests {

	public static final int[] SUNSPEC_START_00 = new int[] { 0x5375, 0x6E53 };

	@Mock
	private ModbusConnection conn;

	@Test
	public void createIntegerMeterModel() throws IOException {
		// GIVEN
		// find base address
		given(conn.readString(ReadHoldingRegister, 40000, 2, true, ByteUtils.ASCII))
				.willReturn(ModelRegister.BASE_ADDRESS_MAGIC_STRING);

		// common model
		given(conn.readWords(ReadHoldingRegister, 40002, 2)).willReturn(new short[] { 1, 65 });
		given(conn.readWords(ReadHoldingRegister, 40004, 65)).willReturn(DataUtils.commonModel02());

		// meter model header, followed by the end marker
		given(conn.readWords(ReadHoldingRegister, 40069, 2))
				.willReturn(IntegerMeterModelAccessorTests.INT_METER_MODEL_HEADER_69);
		given(conn.readWords(ReadHoldingRegister, 40176, 2))
				.willReturn(new short[] { (short) 0xFFFF, 0x0000 });

		// meter model
		given(conn.readWords(ReadHoldingRegister, 40071, 105))
				.willReturn(IntegerMeterModelAccessorTests.INT_METER_MODEL_71);

		// WHEN
		ModelData data = ModelDataFactory.getInstance().getModelData(conn, Integer.MAX_VALUE);

		// THEN
		// @formatter:off
		// strict stubs require every stubbed read, so these counts verify each was made
		// exactly once, and no others were made
		then(conn).should().readString(any(), anyInt(), anyInt(), anyBoolean(), any());
		then(conn).should(times(5)).readWords(any(), anyInt(), anyInt());

		and.then(data.getModels())
			.as("Model count")
			.hasSize(1)
			;
		and.then(data.getModel())
			.as("Meter model")
			.isInstanceOf(MeterModelAccessor.class)
			;

		final MeterModelAccessor model = data.getTypedModel();
		and.then(model)
			.as("Energy export Total")
			.returns(1090000L, from(MeterModelAccessor::getActiveEnergyExported))
			.as("Energy export Phase A")
			.returns(1009000L, from(m -> m.accessorForPhase(PhaseA).getActiveEnergyExported()))
			.as("Energy export Phase B")
			.returns(33600L, from(m -> m.accessorForPhase(PhaseB).getActiveEnergyExported()))
			.as("Energy export Phase C")
			.returns(47300L, from(m -> m.accessorForPhase(PhaseC).getActiveEnergyExported()))
			;

		and.then(model)
			.as("Energy import Total")
			.returns(1001509000L, from(MeterModelAccessor::getActiveEnergyImported))
			.as("Energy import Phase A")
			.returns(350516800L, from(m -> m.accessorForPhase(PhaseA).getActiveEnergyImported()))
			.as("Energy import Phase B")
			.returns(273085000L, from(m -> m.accessorForPhase(PhaseB).getActiveEnergyImported()))
			.as("Energy import Phase C")
			.returns(377907200L, from(m -> m.accessorForPhase(PhaseC).getActiveEnergyImported()))
			;
		// @formatter:on
	}

	@Test
	public void createModelForNonStandardAddress() throws IOException {
		// GIVEN
		Map<Integer, Integer> registers = DataUtils
				.parseModbusHexRegisterMappingLines(new BufferedReader(
						new InputStreamReader(getClass().getResourceAsStream("test-data-01.txt"))));
		IntShortMap map = new IntShortMap(registers.size());
		for ( Map.Entry<Integer, Integer> entry : registers.entrySet() ) {
			map.putValue(entry.getKey(), entry.getValue());
		}
		ModbusConnection conn = new StaticDataMapReadonlyModbusConnection(map);

		// WHEN
		ModelData data = ModelDataFactory.getInstance().getModelData(conn,
				ModelDataFactory.DEFAULT_MAX_READ_WORDS_COUNT, 1000);

		// THEN
		// @formatter:off
		and.then(data)
			.as("Manufacturer")
			.returns("Veris Industries", from(ModelData::getManufacturer))
			.as("Model name")
			.returns("E51C2", from(ModelData::getModelName))
			.as("Options")
			.returns("None", from(ModelData::getOptions))
			.as("Version")
			.returns("2.115", from(ModelData::getVersion))
			.as("Serial number")
			.returns("4E4C3699", from(ModelData::getSerialNumber))
			.as("Device address")
			.returns(7, from(ModelData::getDeviceAddress))
			;
		// @formatter:on
	}

	@Test
	public void findBaseAddress_noMagicBytes() {
		// GIVEN
		IntShortMap map = new IntShortMap(8);
		ModbusConnection conn = new StaticDataMapReadonlyModbusConnection(map);

		// WHEN
		Throwable t = catchThrowable(() -> ModelDataFactory.getInstance().getModelData(conn,
				ModelDataFactory.DEFAULT_MAX_READ_WORDS_COUNT));

		// THEN
		// @formatter:off
		and.then(t)
			.as("Base address not found without the SunSpec marker")
			.isInstanceOf(IOException.class)
			;
		// @formatter:on
	}

	@Test
	public void findBaseAddress_nonMagicBytes() {
		// GIVEN
		IntShortMap map = new IntShortMap(8);
		int i = 'a';
		for ( ModelRegister r : ModelRegister.BASE_ADDRESSES ) {
			map.putValue(r.getAddress(), (i++) << 8 | (i++));
			map.putValue(r.getAddress() + 1, (i++) << 8 | (i++));
		}
		ModbusConnection conn = new StaticDataMapReadonlyModbusConnection(map);

		// WHEN
		Throwable t = catchThrowable(() -> ModelDataFactory.getInstance().getModelData(conn,
				ModelDataFactory.DEFAULT_MAX_READ_WORDS_COUNT));

		// THEN
		// @formatter:off
		and.then(t)
			.as("Base address not found when the SunSpec marker does not match")
			.isInstanceOf(IOException.class)
			;
		// @formatter:on
	}

	@Test
	public void fixedBaseAddress_noMagicBytes() {
		// GIVEN
		IntShortMap map = new IntShortMap(8);
		ModbusConnection conn = new StaticDataMapReadonlyModbusConnection(map);

		// WHEN
		Throwable t = catchThrowable(() -> ModelDataFactory.getInstance().getModelData(conn,
				ModelDataFactory.DEFAULT_MAX_READ_WORDS_COUNT, 0));

		// THEN
		// @formatter:off
		and.then(t)
			.as("SunSpec marker required at the fixed base address")
			.isInstanceOf(IOException.class)
			;
		// @formatter:on
	}

}
