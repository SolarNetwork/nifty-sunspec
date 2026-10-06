/* ==================================================================
 * IntegerInverterModelAccessor_103_04Tests.java - 8/10/2018 7:06:15 AM
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

package net.solarnetwork.sunspec.core.inverter.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterModelId;
import net.solarnetwork.sunspec.core.GenericModelAccessor;
import net.solarnetwork.sunspec.core.inverter.IntegerInverterModelAccessor;
import net.solarnetwork.sunspec.core.meter.test.IntegerMeterModelAccessorTests;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link IntegerInverterModelAccessor} class.
 * 
 * @author matt
 * @version 1.0
 */
public class IntegerInverterModelAccessor_103_05Tests {

	private static final Logger log = LoggerFactory.getLogger(IntegerMeterModelAccessorTests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-103-05.txt", true);
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
			.returns("SMA", from(CommonModelAccessor::getManufacturer))
			.as("Model name")
			.returns("Solar Inverter", from(CommonModelAccessor::getModelName))
			.as("Options")
			.returns("9338", from(CommonModelAccessor::getOptions))
			.as("Version")
			.returns("3.11.02.R", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("3009060251", from(CommonModelAccessor::getSerialNumber))
			.as("Device address")
			.returns(65535, from(CommonModelAccessor::getDeviceAddress))
			;
		// @formatter:on
	}

	@Test
	public void findTypedModel() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		InverterModelAccessor meterAccessor = data.findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(meterAccessor)
			.as("Model found by accessor type")
			.isInstanceOf(IntegerInverterModelAccessor.class)
			;
		// @formatter:on
	}

	@Test
	public void getTypedModel() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		ModelAccessor accessor = data.getTypedModel();

		// THEN
		// @formatter:off
		then(accessor)
			.as("First model as typed accessor")
			.isInstanceOf(GenericModelAccessor.class)
			;
		then(accessor.getModelId().getId())
			.as("First model ID")
			.isEqualTo(11)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		InverterModelAccessor model = getTestDataInstance().findTypedModel(InverterModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(40185, from(InverterModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(40187, from(InverterModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(InverterModelId.ThreePhaseInverterInteger, from(InverterModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(50, from(InverterModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(InverterModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(50, from(InverterModelAccessor::getModelLength))
			.as("Model length")
			.returns(0, from(InverterModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

}
