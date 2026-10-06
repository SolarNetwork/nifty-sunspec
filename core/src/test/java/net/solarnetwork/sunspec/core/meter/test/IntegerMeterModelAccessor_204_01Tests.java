/* ==================================================================
 * IntegerMeterModelAccessor_204_01Tests.java - 28/02/2019 4:12:24 pm
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

package net.solarnetwork.sunspec.core.meter.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.meter.MeterModelAccessor;
import net.solarnetwork.sunspec.api.meter.MeterModelId;
import net.solarnetwork.sunspec.core.meter.IntegerMeterModelAccessor;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link IntegerMeterModelAccessor} class.
 * 
 * @author matt
 * @version 1.1
 */
public class IntegerMeterModelAccessor_204_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(IntegerMeterModelAccessor_203_01Tests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-204-01.txt");
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
			.returns("ACCUENERGY", from(CommonModelAccessor::getManufacturer))
			.as("Model name")
			.returns("Acuvim II", from(CommonModelAccessor::getModelName))
			.as("Options")
			.returns("Acuvim II", from(CommonModelAccessor::getOptions))
			.as("Version")
			.returns("H:2.32 S:3.66", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("AH17122035", from(CommonModelAccessor::getSerialNumber))
			.as("Device address")
			.returns(100, from(CommonModelAccessor::getDeviceAddress))
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
			.returns(69, from(MeterModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(71, from(MeterModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(MeterModelId.DeltaConnectThreePhaseMeterInteger,
					from(MeterModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(105, from(MeterModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(MeterModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(81, from(MeterModelAccessor::getModelLength))
			;
		// @formatter:on // SHOULD BE 105!
		// @formatter:off
		then(model.getRepeatingBlockInstanceCount())
			.as("Model length")
			.isEqualTo(0)
			;
		// @formatter:on
	}

	@Test
	public void events() {
		// GIVEN
		MeterModelAccessor model = getTestDataInstance().getTypedModel();

		// WHEN
		Set<? extends ModelEvent> events = model.getEvents();

		// THEN
		// @formatter:off
		then(events)
			.as("No events")
			.isEmpty()
			;
		// @formatter:on
	}

}
