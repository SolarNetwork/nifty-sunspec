/* ==================================================================
 * InverterPricingSignalModelAccessorImplTests.java - 6/10/2026 10:58:21 am
 *
 * Copyright 2026 SolarNetwork.net Dev Team
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
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.inverter.InverterControlModelId;
import net.solarnetwork.sunspec.api.inverter.InverterPricingSignalModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterPricingSignalType;
import net.solarnetwork.sunspec.core.inverter.InverterPricingSignalModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * Test cases for the {@link InverterPricingSignalModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class InverterPricingSignalModelAccessorImplTests {

	/** Synthetic data, as there is no capture from a device. */
	private static final String TEST_DATA = "test-data-125-01.txt";

	/** The model block address in the test data. */
	private static final int BLOCK_ADDRESS = 72;

	private InverterPricingSignalModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(InverterPricingSignalModelAccessor.class);
	}

	private InverterPricingSignalModelAccessor getTestModel(int address, int... words) {
		return ModelDataUtils.getModelDataInstanceWithRegisters(getClass(), TEST_DATA, address, words)
				.findTypedModel(InverterPricingSignalModelAccessor.class);
	}

	private static InverterPricingSignalModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn)
				.findTypedModel(InverterPricingSignalModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(InverterPricingSignalModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		InverterPricingSignalModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(InverterPricingSignalModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BLOCK_ADDRESS, from(InverterPricingSignalModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(InverterControlModelId.PricingSignal,
					from(InverterPricingSignalModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(8, from(InverterPricingSignalModelAccessor::getFixedBlockLength))
			.as("Model length")
			.returns(8, from(InverterPricingSignalModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void values() {
		// GIVEN
		InverterPricingSignalModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Pricing enabled")
			.returns(true, from(InverterPricingSignalModelAccessor::isPricingEnabled))
			.as("Pricing signal type")
			.returns(InverterPricingSignalType.Absolute,
					from(InverterPricingSignalModelAccessor::getPricingSignalType))
			.as("Pricing signal")
			.returns(23.5f, from(InverterPricingSignalModelAccessor::getPricingSignal))
			.as("Time window")
			.returns(60, from(InverterPricingSignalModelAccessor::getPricingTimeWindow))
			.as("Reversion time")
			.returns(3600, from(InverterPricingSignalModelAccessor::getPricingReversionTime))
			.as("Ramp time")
			.returns(120, from(InverterPricingSignalModelAccessor::getPricingRampTime))
			;
		// @formatter:on
	}

	@Test
	public void pricingEnabled_notImplemented() {
		// @formatter:off
		then(getTestModel(BLOCK_ADDRESS, 0xFFFF).isPricingEnabled())
			.as("Pricing enabled not implemented")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void pricingEnabled_mostSignificantBit() {
		// @formatter:off
		then(getTestModel(BLOCK_ADDRESS, 0x8001).isPricingEnabled())
			.as("Pricing enabled with MSB set not implemented")
			.isNull()
			;
		// @formatter:on
	}

	@Test
	public void pricingEnabled_undefinedBits() {
		// @formatter:off
		then(getTestModel(BLOCK_ADDRESS, 0x0002).isPricingEnabled())
			.as("Undefined bits ignored")
			.isFalse()
			;
		// @formatter:on
	}

	@Test
	public void writeValues() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		InverterPricingSignalModelAccessor model = discoverModel(conn);

		// WHEN
		model.setPricingEnabled(conn, false);
		model.setPricingSignalType(conn, InverterPricingSignalType.Relative);
		model.setPricingSignal(conn, -5.25f);
		model.setPricingTimeWindow(conn, 30);
		model.setPricingReversionTime(conn, 900);
		model.setPricingRampTime(conn, 15);

		// THEN
		// @formatter:off
		then(conn.getWrites())
			.as("Each point written to its own register, in order")
			.isEqualTo(IntStream.rangeClosed(72, 77).mapToObj(a -> List.of(a, 1)).toList())
			;
		then(model.isPricingEnabled())
			.as("Model data updated")
			.isFalse()
			;
		// @formatter:on

		InverterPricingSignalModelAccessor device = discoverModel(conn);
		// @formatter:off
		then(device)
			.as("Pricing enabled")
			.returns(false, from(InverterPricingSignalModelAccessor::isPricingEnabled))
			.as("Pricing signal type")
			.returns(InverterPricingSignalType.Relative,
					from(InverterPricingSignalModelAccessor::getPricingSignalType))
			.as("Pricing signal")
			.returns(-5.25f, from(InverterPricingSignalModelAccessor::getPricingSignal))
			.as("Time window")
			.returns(30, from(InverterPricingSignalModelAccessor::getPricingTimeWindow))
			.as("Reversion time")
			.returns(900, from(InverterPricingSignalModelAccessor::getPricingReversionTime))
			.as("Ramp time")
			.returns(15, from(InverterPricingSignalModelAccessor::getPricingRampTime))
			;
		// @formatter:on

		// WHEN
		device.setPricingEnabled(conn, true);

		// THEN
		// @formatter:off
		then(discoverModel(conn).isPricingEnabled())
			.as("Pricing enabled again")
			.isTrue()
			;
		// @formatter:on
	}

}
