/* ==================================================================
 * DerDcMeasurementModelAccessorImpl_714_01Tests.java - 5/10/2026 10:42:15 am
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

package net.solarnetwork.sunspec.core.der.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.der.DerDcMeasurementModelAccessor;
import net.solarnetwork.sunspec.api.der.DerDcPortAlarm;
import net.solarnetwork.sunspec.api.der.DerDcPortStatus;
import net.solarnetwork.sunspec.api.der.DerDcPortType;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerDcMeasurementModelAccessor.DcPort;
import net.solarnetwork.sunspec.core.der.DerDcMeasurementModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;

/**
 * Test cases for the {@link DerDcMeasurementModelAccessorImpl} class.
 *
 * <p>
 * The device that produced the test data writes {@code uint64} values least
 * significant word first, contrary to SunSpec, so the energy values decoded
 * here are not the energy the device meant to report.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public class DerDcMeasurementModelAccessorImpl_714_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	/** The model block address. */
	private static final int BLOCK_ADDRESS = 1222;

	/** The first port block address. */
	private static final int PORT_ADDRESS = BLOCK_ADDRESS + 18;

	private DerDcMeasurementModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerDcMeasurementModelAccessor.class);
	}

	private DerDcMeasurementModelAccessor getTestModel(int address, int... words) {
		return ModelDataUtils.getModelDataInstanceWithRegisters(getClass(), TEST_DATA, address, words)
				.findTypedModel(DerDcMeasurementModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerDcMeasurementModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerDcMeasurementModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(1220, from(DerDcMeasurementModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(BLOCK_ADDRESS, from(DerDcMeasurementModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.DcMeasurement, from(DerDcMeasurementModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(18, from(DerDcMeasurementModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(25, from(DerDcMeasurementModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model repeating instance count")
			.returns(1, from(DerDcMeasurementModelAccessor::getRepeatingBlockInstanceCount))
			.as("Model length")
			.returns(43, from(DerDcMeasurementModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void values() {
		// GIVEN
		DerDcMeasurementModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("No ports with alarms")
			.returns(Set.of(), from(DerDcMeasurementModelAccessor::getAlarmedPortIndexes))
			.as("Port count")
			.returns(1, from(DerDcMeasurementModelAccessor::getPortCount))
			.as("DC current")
			.returns(0.0f, from(DerDcMeasurementModelAccessor::getDCCurrent))
			.as("DC power")
			.returns(0, from(DerDcMeasurementModelAccessor::getDCPower))
			.as("DC energy injected, from words 08A1 0000 0000 0000")
			.returns(0x08A1000000000000L, from(DerDcMeasurementModelAccessor::getDCEnergyInjected))
			.as("DC energy absorbed, from words FF42 FFFF FFFF FFFF, larger than a long")
			.returns(null, from(DerDcMeasurementModelAccessor::getDCEnergyAbsorbed))
			;
		// @formatter:on
	}

	@Test
	public void ports() {
		// WHEN
		List<DcPort> ports = getTestModel().getDcPorts();

		// THEN
		// @formatter:off
		then(ports)
			.as("Port count")
			.hasSize(1)
			;
		// @formatter:on

		DcPort port = ports.get(0);
		// @formatter:off
		then(port)
			.as("Port type")
			.returns(DerDcPortType.EnergyStorageSystem, from(DcPort::getPortType))
			.as("Port ID")
			.returns(0, from(DcPort::getPortId))
			.as("Port name")
			.returns("Battery", from(DcPort::getPortName))
			.as("DC current")
			.returns(0.0f, from(DcPort::getDCCurrent))
			.as("DC voltage")
			.returns(56.09f, from(DcPort::getDCVoltage))
			.as("DC power")
			.returns(0, from(DcPort::getDCPower))
			.as("DC energy injected, from words 08A1 0000 0000 0000")
			.returns(0x08A1000000000000L, from(DcPort::getDCEnergyInjected))
			.as("DC energy absorbed, from words FF42 FFFF FFFF FFFF, larger than a long")
			.returns(null, from(DcPort::getDCEnergyAbsorbed))
			.as("Temperature, as reported with a 0 scale factor")
			.returns(199.0f, from(DcPort::getTemperature))
			.as("Status")
			.returns(DerDcPortStatus.On, from(DcPort::getPortStatus))
			.as("No alarms")
			.returns(Set.of(), from(DcPort::getEvents))
			;
		// @formatter:on
	}

	@Test
	public void alarmedPortIndexes() {
		// GIVEN
		DerDcMeasurementModelAccessor model = getTestModel(BLOCK_ADDRESS, 0x0000, 0x0005);

		// THEN
		// @formatter:off
		then(model.getAlarmedPortIndexes())
			.as("Bits 0 and 2 set")
			.isEqualTo(Set.of(0, 2))
			;
		// @formatter:on
	}

	@Test
	public void alarmedPortIndexes_mostSignificantBit() {
		// GIVEN
		DerDcMeasurementModelAccessor model = getTestModel(BLOCK_ADDRESS, 0x8000, 0x0001);

		// THEN
		// @formatter:off
		then(model.getAlarmedPortIndexes())
			.as("Bitfield with MSB set not implemented")
			.isEmpty()
			;
		// @formatter:on
	}

	@Test
	public void portEvents() {
		// GIVEN
		// bits 0, 2, 7, 12, 19; bit 2 is not defined by SunSpec
		DerDcMeasurementModelAccessor model = getTestModel(PORT_ADDRESS + 23, 0x0008, 0x1085);

		// THEN
		// @formatter:off
		then(model.getDcPorts().get(0).getEvents())
			.as("Alarms")
			.isEqualTo(Set.of(DerDcPortAlarm.GroundFault, DerDcPortAlarm.OverTemperature,
					DerDcPortAlarm.BlownFuse, DerDcPortAlarm.Reserved))
			;
		// @formatter:on
	}

	@Test
	public void ports_countLimitedByModelLength() {
		// GIVEN
		DerDcMeasurementModelAccessor model = getTestModel(BLOCK_ADDRESS + 2, 3);

		// THEN
		// @formatter:off
		then(model.getPortCount())
			.as("Port count")
			.isEqualTo(3)
			;
		then(model.getDcPorts())
			.as("Ports limited to model length")
			.hasSize(1)
			;
		// @formatter:on
	}

	@Test
	public void ports_countNotImplemented() {
		// GIVEN
		DerDcMeasurementModelAccessor model = getTestModel(BLOCK_ADDRESS + 2, 0xFFFF);

		// THEN
		// @formatter:off
		then(model.getPortCount())
			.as("Port count not implemented")
			.isNull()
			;
		then(model.getDcPorts())
			.as("Ports from model length")
			.hasSize(1)
			;
		// @formatter:on
	}

}
