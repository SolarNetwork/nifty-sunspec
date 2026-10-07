/* ==================================================================
 * ModelDataTests.java - 22/05/2018 1:47:01 PM
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

package net.solarnetwork.sunspec.modbus.support.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.CommonModelId;
import net.solarnetwork.sunspec.api.CommonModelRegister;
import net.solarnetwork.sunspec.api.ModelRegister;
import net.solarnetwork.sunspec.modbus.support.ModbusData.ModbusDataUpdateAction;
import net.solarnetwork.sunspec.modbus.support.ModbusData.MutableModbusData;
import net.solarnetwork.sunspec.modbus.support.ModelData;
import net.solarnetwork.sunspec.test.DataUtils;

/**
 * Test cases for the {@link ModelData} class.
 *
 * @author matt
 * @version 1.0
 */
public class ModelDataTests {

	// @formatter:on

	private static final Logger log = LoggerFactory.getLogger(ModelData.class);

	private ModelData getTestDataInstance() {
		final int baseAddress = ModelRegister.BaseAddress.getAddress();
		ModelData data = new ModelData(baseAddress + 2);
		try {
			data.performUpdates(new ModbusDataUpdateAction() {

				@Override
				public boolean updateModbusData(MutableModbusData m) {
					m.saveDataArray(DataUtils.commonModel02(), baseAddress + 2);
					return true;
				}
			});
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
	public void findTypedModel() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		CommonModelAccessor commonAccessor = data.findTypedModel(CommonModelAccessor.class);

		// THEN
		// @formatter:off
		then(commonAccessor)
			.as("Model found by accessor type")
			.isInstanceOf(ModelData.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// THEN
		// @formatter:off
		then(data.getBaseAddress())
			.as("Model base address")
			.isEqualTo(40002)
			;
		then(data.getModelId().getId())
			.as("Model ID")
			.isEqualTo(CommonModelId.CommonModel.getId())
			;
		then(data)
			.as("Model fixed length")
			.returns(65, from(ModelData::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(ModelData::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(65, from(ModelData::getModelLength))
			.as("Model length")
			.returns(0, from(ModelData::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
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

	private static ModelData stringData(byte[] bytes) {
		final CommonModelRegister ref = CommonModelRegister.Options;
		final short[] words = new short[ref.getWordLength()];
		ByteBuffer.wrap(Arrays.copyOf(bytes, words.length * 2)).asShortBuffer().get(words);
		ModelData data = new ModelData(0);
		try {
			data.performUpdates(m -> {
				m.saveDataArray(words, ref.getAddress());
				return true;
			});
		} catch ( IOException e ) {
			throw new RuntimeException(e);
		}
		return data;
	}

	@Test
	public void stringValue_utf8() {
		// GIVEN
		ModelData data = stringData("Größe".getBytes(StandardCharsets.UTF_8));

		// THEN
		// @formatter:off
		then(data.getStringValue(CommonModelRegister.Options, 0))
			.as("UTF-8 decoded")
			.isEqualTo("Größe")
			;
		// @formatter:on
	}

	@Test
	public void stringValue_latin1() {
		// GIVEN
		// a Latin-1 encoded e-acute is not valid UTF-8
		ModelData data = stringData(new byte[] { 'C', 'a', 'f', (byte) 0xE9 });

		// THEN
		// @formatter:off
		then(data.getStringValue(CommonModelRegister.Options, 0))
			.as("Invalid UTF-8 replaced")
			.isEqualTo("Caf�")
			;
		// @formatter:on
	}

	@Test
	public void stringValue_nullTerminated() {
		// GIVEN
		ModelData data = stringData(new byte[] { 'A', 'B', 'C', 0, 'x', 'y', 'z' });

		// THEN
		// @formatter:off
		then(data.getStringValue(CommonModelRegister.Options, 0))
			.as("Bytes after NULL ignored")
			.isEqualTo("ABC")
			;
		// @formatter:on
	}

	@Test
	public void stringValue_trimmed() {
		// GIVEN
		ModelData data = stringData("  ABC ".getBytes(StandardCharsets.UTF_8));

		// THEN
		// @formatter:off
		then(data.getStringValue(CommonModelRegister.Options, 0))
			.as("Whitespace removed")
			.isEqualTo("ABC")
			;
		// @formatter:on
	}

	@Test
	public void stringValue_empty() {
		// GIVEN
		// SunSpec recommends 0x0080 in the first register to represent an empty string
		ModelData data = stringData(new byte[] { 0x00, (byte) 0x80 });

		// THEN
		// @formatter:off
		then(data.getStringValue(CommonModelRegister.Options, 0))
			.as("Empty string")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void stringValue_notImplemented() {
		// GIVEN
		ModelData data = stringData(new byte[0]);

		// THEN
		// @formatter:off
		then(data.getStringValue(CommonModelRegister.Options, 0))
			.as("All NULL not implemented")
			.isNull()
			;
		// @formatter:on
	}

}
