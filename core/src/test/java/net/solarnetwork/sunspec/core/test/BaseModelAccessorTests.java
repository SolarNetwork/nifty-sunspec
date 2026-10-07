/* ==================================================================
 * BaseModelAccessorTests.java - 5/10/2026 6:57:32 am
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

import static net.solarnetwork.sunspec.api.DataClassification.Accumulator;
import static net.solarnetwork.sunspec.api.DataClassification.Bitfield;
import static net.solarnetwork.sunspec.api.DataClassification.Enumeration;
import static net.solarnetwork.sunspec.api.DataClassification.ScaleFactor;
import static net.solarnetwork.sunspec.api.PointAccess.ReadWrite;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.Int32;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt16;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt32;
import static net.solarnetwork.sunspec.modbus.ModbusDataType.UInt64;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.GenericModelId;
import net.solarnetwork.sunspec.api.PointAccess;
import net.solarnetwork.sunspec.api.der.DerAcWiringType;
import net.solarnetwork.sunspec.api.der.DerOperationalCharacteristic;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.core.GenericModelAccessor;
import net.solarnetwork.sunspec.core.support.IntShortMap;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;
import net.solarnetwork.sunspec.modbus.support.StaticDataMapModbusConnection;

/**
 * Test cases for the {@link BaseModelAccessor} class.
 *
 * @author matt
 * @version 1.0
 */
public class BaseModelAccessorTests {

	/** Test registers, relative to the model block address. */
	private enum TestRegister implements ModbusReference {

		UInt32Value(0, UInt32, null),

		Enum32Value(2, UInt32, Enumeration),

		Acc32Value(4, UInt32, Accumulator),

		UInt64Value(6, UInt64, null),

		Acc16Value(10, UInt16, Accumulator),

		Acc64Value(11, UInt64, Accumulator),

		ScaleFactorValue(15, Int16, ScaleFactor),

		RwUInt16Value(16, UInt16, null, ReadWrite),

		RwInt16Value(17, Int16, null, ReadWrite),

		RwUInt32Value(18, UInt32, null, ReadWrite),

		RwInt32Value(20, Int32, null, ReadWrite),

		RwBitfield16Value(22, UInt16, Bitfield, ReadWrite),

		Enum16Value(23, UInt16, Enumeration),

		Bitfield16Value(24, UInt16, Bitfield),

		Bitfield32Value(25, UInt32, Bitfield),

		Bitfield32Value2(27, UInt32, Bitfield),

		;

		private final int address;
		private final ModbusDataType dataType;
		private final @Nullable DataClassification classification;
		private final PointAccess access;

		private TestRegister(int address, ModbusDataType dataType,
				@Nullable DataClassification classification) {
			this(address, dataType, classification, PointAccess.ReadOnly);
		}

		private TestRegister(int address, ModbusDataType dataType,
				@Nullable DataClassification classification, PointAccess access) {
			this.address = address;
			this.dataType = dataType;
			this.classification = classification;
			this.access = access;
		}

		@Override
		public int getAddress() {
			return address;
		}

		@Override
		public ModbusDataType getDataType() {
			return dataType;
		}

		@Override
		public int getWordLength() {
			return dataType.getWordLength();
		}

		@Override
		public @Nullable DataClassification getClassification() {
			return classification;
		}

		@Override
		public PointAccess getAccess() {
			return access;
		}

	}

	/** A write action. */
	@FunctionalInterface
	private interface WriteAction {

		void write() throws IOException;

	}

	private ModelData data;
	private BaseModelAccessor accessor;
	private IntShortMap deviceData;
	private StaticDataMapModbusConnection conn;

	@BeforeEach
	public void setup() {
		data = new ModelData(0);
		accessor = new GenericModelAccessor(data, 0, new GenericModelId(64000));
		deviceData = new IntShortMap();
		conn = new StaticDataMapModbusConnection(deviceData);
	}

	private void saveRegisters(TestRegister ref, int... words) throws IOException {
		data.performUpdates(m -> {
			m.saveDataArray(words, accessor.getBlockAddress() + ref.getAddress());
			return true;
		});
	}

	private int[] deviceRegisters(TestRegister ref) {
		int[] result = new int[ref.getWordLength()];
		for ( int i = 0; i < result.length; i++ ) {
			result[i] = deviceData.getValue(accessor.getBlockAddress() + ref.getAddress() + i) & 0xFFFF;
		}
		return result;
	}

	private void assertWriteRejected(String message, Class<? extends RuntimeException> errorType,
			WriteAction action) {
		// @formatter:off
		thenThrownBy(action::write)
			.as(message)
			.isInstanceOf(errorType)
			;
		then(deviceData.size())
			.as("Nothing written to device")
			.isEqualTo(0)
			;
		// @formatter:on
	}

	@Test
	public void uint32_notImplemented() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.UInt32Value, 0xFFFF, 0xFFFF);

		// WHEN
		Number result = accessor.getValue(TestRegister.UInt32Value);

		// THEN
		// @formatter:off
		then(result)
			.as("uint32 0xFFFFFFFF is not implemented")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void uint32_maximum() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.UInt32Value, 0xFFFF, 0xFFFE);

		// WHEN
		Number result = accessor.getValue(TestRegister.UInt32Value);

		// THEN
		// @formatter:off
		then(result)
			.as("uint32 0xFFFFFFFE is a value")
			.isEqualTo(0xFFFFFFFEL)
			;
		// @formatter:on
	}

	@Test
	public void enum32_notImplemented() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Enum32Value, 0xFFFF, 0xFFFF);

		// WHEN
		Number result = accessor.getValue(TestRegister.Enum32Value);

		// THEN
		// @formatter:off
		then(result)
			.as("enum32 0xFFFFFFFF is not implemented")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void acc32_notAccumulated() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Acc32Value, 0x0000, 0x0000);

		// WHEN
		Number result = accessor.getValue(TestRegister.Acc32Value);

		// THEN
		// @formatter:off
		then(result)
			.as("acc32 0 is not accumulated")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void acc32_maximum() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Acc32Value, 0xFFFF, 0xFFFF);

		// WHEN
		Number result = accessor.getValue(TestRegister.Acc32Value);

		// THEN
		// @formatter:off
		then(result)
			.as("acc32 0xFFFFFFFF is a value")
			.isEqualTo(0xFFFFFFFFL)
			;
		// @formatter:on
	}

	@Test
	public void acc16_maximum() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Acc16Value, 0xFFFF);

		// WHEN
		Number result = accessor.getValue(TestRegister.Acc16Value);

		// THEN
		// @formatter:off
		then(result)
			.as("acc16 0xFFFF is a value")
			.isEqualTo(0xFFFF)
			;
		// @formatter:on
	}

	@Test
	public void acc64_notAccumulated() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Acc64Value, 0x0000, 0x0000, 0x0000, 0x0000);

		// WHEN
		Number result = accessor.getValue(TestRegister.Acc64Value);

		// THEN
		// @formatter:off
		then(result)
			.as("acc64 0 is not accumulated")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void acc64_lowWordsZero() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Acc64Value, 0x0000, 0x0001, 0x0000, 0x0000);

		// WHEN
		Number result = accessor.getValue(TestRegister.Acc64Value);

		// THEN
		// @formatter:off
		then(result)
			.as("acc64 0x100000000 is a value")
			.isEqualTo(new BigInteger("100000000", 16))
			;
		// @formatter:on
	}

	@Test
	public void acc64_maximum() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Acc64Value, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF);

		// WHEN
		Number result = accessor.getValue(TestRegister.Acc64Value);

		// THEN
		// @formatter:off
		then(result)
			.as("acc64 0xFFFFFFFFFFFFFFFF is a value")
			.isEqualTo(new BigInteger("FFFFFFFFFFFFFFFF", 16))
			;
		// @formatter:on
	}

	@Test
	public void uint64_notImplemented() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.UInt64Value, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF);

		// WHEN
		Number result = accessor.getValue(TestRegister.UInt64Value);

		// THEN
		// @formatter:off
		then(result)
			.as("uint64 0xFFFFFFFFFFFFFFFF is not implemented")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void uint64_maximum() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.UInt64Value, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFE);

		// WHEN
		Number result = accessor.getValue(TestRegister.UInt64Value);

		// THEN
		// @formatter:off
		then(result)
			.as("uint64 0xFFFFFFFFFFFFFFFE is a value")
			.isEqualTo(new BigInteger("FFFFFFFFFFFFFFFE", 16))
			;
		// @formatter:on
	}

	@Test
	public void write_uint16() throws IOException {
		// WHEN
		accessor.writeValue(conn, TestRegister.RwUInt16Value, 1234);

		// THEN
		// @formatter:off
		then(deviceRegisters(TestRegister.RwUInt16Value))
			.as("Device register written")
			.containsExactly(1234)
			;
		then(accessor.getIntegerValue(TestRegister.RwUInt16Value))
			.as("Model data updated")
			.isEqualTo(1234)
			;
		// @formatter:on
	}

	@Test
	public void write_int16_negative() throws IOException {
		// WHEN
		accessor.writeValue(conn, TestRegister.RwInt16Value, -5);

		// THEN
		// @formatter:off
		then(deviceRegisters(TestRegister.RwInt16Value))
			.as("Device register written")
			.containsExactly(0xFFFB)
			;
		then(accessor.getIntegerValue(TestRegister.RwInt16Value))
			.as("Model data updated")
			.isEqualTo(-5)
			;
		// @formatter:on
	}

	@Test
	public void write_uint32() throws IOException {
		// WHEN
		accessor.writeValue(conn, TestRegister.RwUInt32Value, 70000);

		// THEN
		// @formatter:off
		then(deviceRegisters(TestRegister.RwUInt32Value))
			.as("Device registers written")
			.containsExactly(0x0001, 0x1170)
			;
		then(accessor.getLongValue(TestRegister.RwUInt32Value))
			.as("Model data updated")
			.isEqualTo(70000L)
			;
		// @formatter:on
	}

	@Test
	public void write_int32_negative() throws IOException {
		// WHEN
		accessor.writeValue(conn, TestRegister.RwInt32Value, -70000);

		// THEN
		// @formatter:off
		then(deviceRegisters(TestRegister.RwInt32Value))
			.as("Device registers written")
			.containsExactly(0xFFFE, 0xEE90)
			;
		then(accessor.getIntegerValue(TestRegister.RwInt32Value))
			.as("Model data updated")
			.isEqualTo(-70000)
			;
		// @formatter:on
	}

	@Test
	public void write_rounded() throws IOException {
		// WHEN
		accessor.writeValue(conn, TestRegister.RwUInt16Value, 2.5f);

		// THEN
		// @formatter:off
		then(deviceRegisters(TestRegister.RwUInt16Value))
			.as("Value rounded half up")
			.containsExactly(3)
			;
		// @formatter:on
	}

	@Test
	public void write_offset() throws IOException {
		// WHEN
		accessor.writeValue(conn, TestRegister.RwUInt16Value, accessor.getBlockAddress() + 100, 7);

		// THEN
		// @formatter:off
		then(deviceData.getValue(accessor.getBlockAddress() + 100
				+ TestRegister.RwUInt16Value.getAddress()) & 0xFFFF)
			.as("Device register written at offset")
			.isEqualTo(7)
			;
		// @formatter:on
	}

	@Test
	public void write_notWritable() throws IOException {
		assertWriteRejected("Read-only point not writable", IllegalArgumentException.class,
				() -> accessor.writeValue(conn, TestRegister.UInt32Value, 1));
	}

	@Test
	public void write_uint16_notImplementedValue() throws IOException {
		assertWriteRejected("uint16 0xFFFF not writable", IllegalArgumentException.class,
				() -> accessor.writeValue(conn, TestRegister.RwUInt16Value, 0xFFFF));
	}

	@Test
	public void write_uint16_negative() throws IOException {
		assertWriteRejected("uint16 -1 not writable", IllegalArgumentException.class,
				() -> accessor.writeValue(conn, TestRegister.RwUInt16Value, -1));
	}

	@Test
	public void write_int16_notImplementedValue() throws IOException {
		assertWriteRejected("int16 -32768 not writable", IllegalArgumentException.class,
				() -> accessor.writeValue(conn, TestRegister.RwInt16Value, -32768));
	}

	@Test
	public void write_int16_tooLarge() throws IOException {
		assertWriteRejected("int16 40000 not writable", IllegalArgumentException.class,
				() -> accessor.writeValue(conn, TestRegister.RwInt16Value, 40000));
	}

	@Test
	public void write_uint32_notImplementedValue() throws IOException {
		assertWriteRejected("uint32 0xFFFFFFFF not writable", IllegalArgumentException.class,
				() -> accessor.writeValue(conn, TestRegister.RwUInt32Value, 0xFFFFFFFFL));
	}

	@Test
	public void write_bitfield16_mostSignificantBit() throws IOException {
		assertWriteRejected("bitfield16 0x8000 not writable", IllegalArgumentException.class,
				() -> accessor.writeValue(conn, TestRegister.RwBitfield16Value, 0x8000));
	}

	@Test
	public void write_notFinite() throws IOException {
		assertWriteRejected("NaN not writable", IllegalArgumentException.class,
				() -> accessor.writeValue(conn, TestRegister.RwUInt16Value, Float.NaN));
	}

	@Test
	public void writeScaled() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.ScaleFactorValue, 0xFFFE); // -2

		// WHEN
		accessor.writeScaledValue(conn, TestRegister.RwUInt32Value, TestRegister.ScaleFactorValue,
				599.95);

		// THEN
		// @formatter:off
		then(deviceRegisters(TestRegister.RwUInt32Value))
			.as("Device registers written")
			.containsExactly(0x0000, 0xEA5B)
			;
		then(accessor.getScaledValue(TestRegister.RwUInt32Value, TestRegister.ScaleFactorValue))
			.as("Model data updated")
			.isEqualTo(new BigDecimal("599.95"))
			;
		// @formatter:on
	}

	@Test
	public void writeScaled_rounded() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.ScaleFactorValue, 0xFFFF); // -1

		// WHEN
		accessor.writeScaledValue(conn, TestRegister.RwUInt16Value, TestRegister.ScaleFactorValue,
				new BigDecimal("12.35"));

		// THEN
		// @formatter:off
		then(deviceRegisters(TestRegister.RwUInt16Value))
			.as("Value rounded half up")
			.containsExactly(124)
			;
		// @formatter:on
	}

	@Test
	public void writeScaled_positiveScaleFactor() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.ScaleFactorValue, 2);

		// WHEN
		accessor.writeScaledValue(conn, TestRegister.RwInt16Value, TestRegister.ScaleFactorValue, -1250);

		// THEN
		// @formatter:off
		then(deviceRegisters(TestRegister.RwInt16Value))
			.as("Value divided by 100 and rounded half up")
			.containsExactly(0xFFF3)
			;
		// @formatter:on
	}

	@Test
	public void writeScaled_scaleFactorNotRead() throws IOException {
		assertWriteRejected("Unread scale factor rejected", IllegalStateException.class, () -> accessor
				.writeScaledValue(conn, TestRegister.RwUInt16Value, TestRegister.ScaleFactorValue, 1));
	}

	@Test
	public void writeScaled_scaleFactorNotImplemented() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.ScaleFactorValue, 0x8000);

		// THEN
		assertWriteRejected("Not implemented scale factor rejected", IllegalStateException.class,
				() -> accessor.writeScaledValue(conn, TestRegister.RwUInt16Value,
						TestRegister.ScaleFactorValue, 1));
	}

	@Test
	public void writeScaled_scaleFactorOutOfRange() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.ScaleFactorValue, 11);

		// THEN
		assertWriteRejected("Out of range scale factor rejected", IllegalStateException.class,
				() -> accessor.writeScaledValue(conn, TestRegister.RwUInt16Value,
						TestRegister.ScaleFactorValue, 1));
	}

	@Test
	public void codedValue() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Enum16Value, 1);

		// THEN
		// @formatter:off
		then(accessor.getCodedValue(TestRegister.Enum16Value, DerAcWiringType.class))
			.as("Code resolved")
			.isEqualTo(DerAcWiringType.SplitPhase)
			;
		// @formatter:on
	}

	@Test
	public void codedValue_unknownCode() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Enum16Value, 9);

		// THEN
		// @formatter:off
		then(accessor.getCodedValue(TestRegister.Enum16Value, DerAcWiringType.class))
			.as("Unknown code is null")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void codedValue_notImplemented() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Enum16Value, 0xFFFF);

		// THEN
		// @formatter:off
		then(accessor.getCodedValue(TestRegister.Enum16Value, DerAcWiringType.class))
			.as("Not implemented is null")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void bitmaskableValues() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield16Value, 0x0003);

		// THEN
		// @formatter:off
		then(accessor.getBitmaskableValues(TestRegister.Bitfield16Value,
				DerOperationalCharacteristic.class))
			.as("Bits resolved")
			.isEqualTo(Set.of(DerOperationalCharacteristic.GridFollowing,
					DerOperationalCharacteristic.GridForming))
			;
		// @formatter:on
	}

	@Test
	public void bitmaskableValues_unknownBits() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield16Value, 0x000C);

		// THEN
		// @formatter:off
		then(accessor.getBitmaskableValues(TestRegister.Bitfield16Value,
				DerOperationalCharacteristic.class))
			.as("Unknown bit 3 ignored")
			.isEqualTo(Set.of(DerOperationalCharacteristic.PvClipped))
			;
		// @formatter:on
	}

	@Test
	public void bitmaskableValues_mostSignificantBit16() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield16Value, 0x8001);

		// THEN
		// @formatter:off
		then(accessor.getBitmaskableValues(TestRegister.Bitfield16Value,
				DerOperationalCharacteristic.class))
			.as("Bitfield16 with MSB set not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void bitmaskableValues_mostSignificantBit32() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield32Value, 0x8000, 0x0001);

		// THEN
		// @formatter:off
		then(accessor.getBitmaskableValues(TestRegister.Bitfield32Value,
				DerOperationalCharacteristic.class))
			.as("Bitfield32 with MSB set not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void bitmaskableValues_notImplemented() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield32Value, 0xFFFF, 0xFFFF);

		// THEN
		// @formatter:off
		then(accessor.getBitmaskableValues(TestRegister.Bitfield32Value,
				DerOperationalCharacteristic.class))
			.as("Bitfield32 not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void booleanValue_false() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Enum16Value, 0);

		// THEN
		// @formatter:off
		then(accessor.getBooleanValue(TestRegister.Enum16Value))
			.as("0 is false")
			.isFalse()
			;
		// @formatter:on
	}

	@Test
	public void booleanValue_true() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Enum16Value, 1);

		// THEN
		// @formatter:off
		then(accessor.getBooleanValue(TestRegister.Enum16Value))
			.as("1 is true")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void booleanValue_unknownCode() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Enum16Value, 2);

		// THEN
		// @formatter:off
		then(accessor.getBooleanValue(TestRegister.Enum16Value))
			.as("Unknown code is null")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void booleanValue_notImplemented() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Enum16Value, 0xFFFF);

		// THEN
		// @formatter:off
		then(accessor.getBooleanValue(TestRegister.Enum16Value))
			.as("Not implemented is null")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void scaledFloatValue() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.RwUInt16Value, 1234);
		saveRegisters(TestRegister.ScaleFactorValue, 0xFFFE); // -2

		// THEN
		// @formatter:off
		then(accessor.getScaledFloatValue(TestRegister.RwUInt16Value, TestRegister.ScaleFactorValue))
			.as("Value scaled")
			.isEqualTo(12.34f)
			;
		// @formatter:on
	}

	@Test
	public void scaledFloatValue_notImplemented() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.RwUInt16Value, 0xFFFF);
		saveRegisters(TestRegister.ScaleFactorValue, 0xFFFE); // -2

		// THEN
		// @formatter:off
		then(accessor.getScaledFloatValue(TestRegister.RwUInt16Value, TestRegister.ScaleFactorValue))
			.as("Not implemented is null")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void scaledIntegerValue_fractionDiscarded() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.RwInt16Value, 0xFB29); // -1239
		saveRegisters(TestRegister.ScaleFactorValue, 0xFFFF); // -1

		// THEN
		// @formatter:off
		then(accessor.getScaledIntegerValue(TestRegister.RwInt16Value, TestRegister.ScaleFactorValue))
			.as("Fraction discarded")
			.isEqualTo(-123)
			;
		// @formatter:on
	}

	@Test
	public void scaledLongValue() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.RwUInt32Value, 0x0001, 0x0000);
		saveRegisters(TestRegister.ScaleFactorValue, 3);

		// THEN
		// @formatter:off
		then(accessor.getScaledLongValue(TestRegister.RwUInt32Value, TestRegister.ScaleFactorValue))
			.as("Value scaled")
			.isEqualTo(65536000L)
			;
		// @formatter:on
	}

	@Test
	public void scaledIntegerValue_maximum() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.RwUInt32Value, 0x7FFF, 0xFFFF);
		saveRegisters(TestRegister.ScaleFactorValue, 0);

		// THEN
		// @formatter:off
		then(accessor.getScaledIntegerValue(TestRegister.RwUInt32Value, TestRegister.ScaleFactorValue))
			.as("Largest integer value")
			.isEqualTo(Integer.MAX_VALUE)
			;
		// @formatter:on
	}

	@Test
	public void scaledIntegerValue_outOfRange() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.RwUInt32Value, 0x8000, 0x0000);
		saveRegisters(TestRegister.ScaleFactorValue, 0);

		// THEN
		// @formatter:off
		then(accessor.getScaledIntegerValue(TestRegister.RwUInt32Value, TestRegister.ScaleFactorValue))
			.as("Value larger than an integer is not available")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void scaledLongValue_maximum() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.UInt64Value, 0x7FFF, 0xFFFF, 0xFFFF, 0xFFFF);
		saveRegisters(TestRegister.ScaleFactorValue, 0);

		// THEN
		// @formatter:off
		then(accessor.getScaledLongValue(TestRegister.UInt64Value, TestRegister.ScaleFactorValue))
			.as("Largest long value")
			.isEqualTo(Long.MAX_VALUE)
			;
		// @formatter:on
	}

	@Test
	public void scaledLongValue_outOfRange() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.UInt64Value, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFE);
		saveRegisters(TestRegister.ScaleFactorValue, 0);

		// THEN
		// @formatter:off
		then(accessor.getScaledLongValue(TestRegister.UInt64Value, TestRegister.ScaleFactorValue))
			.as("Value larger than a long is not available")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void scaledValue_scaleFactorNotImplemented() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.RwUInt16Value, 1234);
		saveRegisters(TestRegister.ScaleFactorValue, 0x8000);

		// THEN
		// @formatter:off
		then(accessor.getScaledValue(TestRegister.RwUInt16Value, TestRegister.ScaleFactorValue))
			.as("Value with not implemented scale factor is not available")
			.isNull()
			;
		then(accessor.getScaledIntegerValue(TestRegister.RwUInt16Value, TestRegister.ScaleFactorValue))
			.as("Integer value with not implemented scale factor is not available")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void scaledValue_zero_scaleFactorNotImplemented() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.RwUInt16Value, 0);
		saveRegisters(TestRegister.ScaleFactorValue, 0x8000);

		// THEN
		// @formatter:off
		then(accessor.getScaledValue(TestRegister.RwUInt16Value, TestRegister.ScaleFactorValue))
			.as("Zero value with not implemented scale factor is not available")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void scaledValue_scaleFactorAboveRange() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.RwUInt16Value, 1234);
		saveRegisters(TestRegister.ScaleFactorValue, 11);

		// THEN
		// @formatter:off
		then(accessor.getScaledValue(TestRegister.RwUInt16Value, TestRegister.ScaleFactorValue))
			.as("Value with scale factor above 10 is not available")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void scaledValue_scaleFactorBelowRange() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.RwUInt16Value, 1234);
		saveRegisters(TestRegister.ScaleFactorValue, 0xFFF5); // -11

		// THEN
		// @formatter:off
		then(accessor.getScaledValue(TestRegister.RwUInt16Value, TestRegister.ScaleFactorValue))
			.as("Value with scale factor below -10 is not available")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void scaledValue_scaleFactorRangeLimits() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.RwUInt16Value, 1234);
		saveRegisters(TestRegister.ScaleFactorValue, 10);

		// THEN
		// @formatter:off
		then(accessor.getScaledValue(TestRegister.RwUInt16Value, TestRegister.ScaleFactorValue))
			.as("Scale factor 10 applied")
			.isEqualByComparingTo(new BigDecimal("1234E10"))
			;
		// @formatter:on

		// AND
		saveRegisters(TestRegister.ScaleFactorValue, 0xFFF6); // -10
		// @formatter:off
		then(accessor.getScaledValue(TestRegister.RwUInt16Value, TestRegister.ScaleFactorValue))
			.as("Scale factor -10 applied")
			.isEqualByComparingTo(new BigDecimal("1234E-10"))
			;
		// @formatter:on
	}

	@Test
	public void bitfieldIndexes() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield32Value, 0x0000, 0x0105);

		// THEN
		// @formatter:off
		then(accessor.getBitfieldIndexes(TestRegister.Bitfield32Value))
			.as("Indexes of the set bits")
			.isEqualTo(Set.of(0, 2, 8))
			;
		// @formatter:on
	}

	@Test
	public void bitfieldIndexes_bitfield16() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield16Value, 0x4001);

		// THEN
		// @formatter:off
		then(accessor.getBitfieldIndexes(TestRegister.Bitfield16Value))
			.as("Indexes of the set bits, up to the bit before the most significant bit")
			.isEqualTo(Set.of(0, 14))
			;
		// @formatter:on
	}

	@Test
	public void bitfieldIndexes_mostSignificantBit() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield16Value, 0x8001);
		saveRegisters(TestRegister.Bitfield32Value, 0x8000, 0x0001);

		// THEN
		// @formatter:off
		then(accessor.getBitfieldIndexes(TestRegister.Bitfield16Value))
			.as("Bitfield16 with MSB set not implemented")
			.isEmpty()
			;
		then(accessor.getBitfieldIndexes(TestRegister.Bitfield32Value))
			.as("Bitfield32 with MSB set not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void bitfieldBits() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield32Value, 0x0000, 0x0005);
		saveRegisters(TestRegister.Bitfield32Value2, 0x4000, 0x0001);

		// THEN
		BitSet expected = new BitSet();
		expected.set(0);
		expected.set(2);
		expected.set(32);
		expected.set(62);
		// @formatter:off
		then(accessor.getBitfieldBits(TestRegister.Bitfield32Value, TestRegister.Bitfield32Value2))
			.as("Second bitfield bits follow the first")
			.isEqualTo(expected)
			;
		// @formatter:on
	}

	@Test
	public void bitfieldBits_mostSignificantBit() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield32Value, 0x8000, 0x0001);
		saveRegisters(TestRegister.Bitfield32Value2, 0x0000, 0x0002);

		// THEN
		BitSet expected = new BitSet();
		expected.set(33);
		// @formatter:off
		then(accessor.getBitfieldBits(TestRegister.Bitfield32Value, TestRegister.Bitfield32Value2))
			.as("Not implemented bitfield contributes no bits")
			.isEqualTo(expected)
			;
		// @formatter:on
	}

	@Test
	public void bitfieldBits_mixedLengths() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield16Value, 0x0001);
		saveRegisters(TestRegister.Bitfield32Value, 0x0000, 0x0001);

		// THEN
		BitSet expected = new BitSet();
		expected.set(0);
		expected.set(16);
		// @formatter:off
		then(accessor.getBitfieldBits(TestRegister.Bitfield16Value, TestRegister.Bitfield32Value))
			.as("Bitfield32 bits follow the 16 bitfield16 bits")
			.isEqualTo(expected)
			;
		// @formatter:on
	}

	@Test
	public void bitfieldBit() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield16Value, 0x0005);

		// THEN
		// @formatter:off
		then(accessor.getBitfieldBit(TestRegister.Bitfield16Value, 0))
			.as("Bit 0 set")
			.isTrue()
			;
		then(accessor.getBitfieldBit(TestRegister.Bitfield16Value, 1))
			.as("Bit 1 not set")
			.isFalse()
			;
		then(accessor.getBitfieldBit(TestRegister.Bitfield16Value, 2))
			.as("Bit 2 set")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void bitfieldBit_bitfield32() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield32Value, 0x0001, 0x0000);

		// THEN
		// @formatter:off
		then(accessor.getBitfieldBit(TestRegister.Bitfield32Value, 0))
			.as("Bit 0 not set")
			.isFalse()
			;
		then(accessor.getBitfieldBit(TestRegister.Bitfield32Value, 16))
			.as("Bit 16 set")
			.isTrue()
			;
		// @formatter:on
	}

	@Test
	public void bitfieldBit_mostSignificantBit() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield16Value, 0x8001);
		saveRegisters(TestRegister.Bitfield32Value, 0x8000, 0x0001);

		// THEN
		// @formatter:off
		then(accessor.getBitfieldBit(TestRegister.Bitfield16Value, 0))
			.as("Bitfield16 with MSB set not implemented")
			.isNull()
			;
		then(accessor.getBitfieldBit(TestRegister.Bitfield32Value, 0))
			.as("Bitfield32 with MSB set not implemented")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void bitfieldBit_notImplemented() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.Bitfield16Value, 0xFFFF);
		saveRegisters(TestRegister.Bitfield32Value, 0xFFFF, 0xFFFF);

		// THEN
		// @formatter:off
		then(accessor.getBitfieldBit(TestRegister.Bitfield16Value, 0))
			.as("Bitfield16 not implemented")
			.isNull()
			;
		then(accessor.getBitfieldBit(TestRegister.Bitfield32Value, 0))
			.as("Bitfield32 not implemented")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void longValue_maximum() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.UInt64Value, 0x7FFF, 0xFFFF, 0xFFFF, 0xFFFF);

		// THEN
		// @formatter:off
		then(accessor.getLongValue(TestRegister.UInt64Value))
			.as("Largest long value")
			.isEqualTo(Long.MAX_VALUE)
			;
		// @formatter:on
	}

	@Test
	public void longValue_outOfRange() throws IOException {
		// GIVEN
		saveRegisters(TestRegister.UInt64Value, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFE);

		// THEN
		// @formatter:off
		then(accessor.getLongValue(TestRegister.UInt64Value))
			.as("Value larger than a long is not available")
			.isNull()
			;
		// @formatter:on
	}

}
