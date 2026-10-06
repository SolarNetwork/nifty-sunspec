/* ==================================================================
 * MeteorologicalModelAccessorImpl_307_01Tests.java - 10/07/2023 9:40:35 am
 * 
 * Copyright 2023 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.core.environmental.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.solarnetwork.sunspec.api.CommonModelAccessor;
import net.solarnetwork.sunspec.api.environmental.EnvironmentalModelId;
import net.solarnetwork.sunspec.api.environmental.MeteorologicalModelAccessor;
import net.solarnetwork.sunspec.api.environmental.PrecipitationType;
import net.solarnetwork.sunspec.core.environmental.MeteorologicalModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Test cases for the {@link MeteorologicalModelAccessorImpl} class.
 * 
 * @author matt
 * @version 1.0
 */
public class MeteorologicalModelAccessorImpl_307_01Tests {

	private static final Logger log = LoggerFactory
			.getLogger(IrradianceModelAccessorImpl_302_01Tests.class);

	private ModelData getTestDataInstance() {
		return ModelDataUtils.getModelDataInstance(getClass(), "test-data-307-01.txt");
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
			.returns("Rainwise_Inc", from(CommonModelAccessor::getManufacturer))
			.as("Model name")
			.returns("PVmet 500", from(CommonModelAccessor::getModelName))
			.as("Options")
			.returns("0", from(CommonModelAccessor::getOptions))
			.as("Version")
			.returns("1.2", from(CommonModelAccessor::getVersion))
			.as("Serial number")
			.returns("123456", from(CommonModelAccessor::getSerialNumber))
			.as("Device address")
			.returns(60, from(CommonModelAccessor::getDeviceAddress))
			;
		// @formatter:on
	}

	@Test
	public void findTypedModel() {
		// GIVEN
		ModelData data = getTestDataInstance();

		// WHEN
		MeteorologicalModelAccessor accessor = data.findTypedModel(MeteorologicalModelAccessor.class);

		// THEN
		// @formatter:off
		then(accessor)
			.as("Model found by accessor type")
			.isInstanceOf(MeteorologicalModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		MeteorologicalModelAccessor model = getTestDataInstance()
				.findTypedModel(MeteorologicalModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(70, from(MeteorologicalModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(72, from(MeteorologicalModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(EnvironmentalModelId.BaseMeteorolgical,
					from(MeteorologicalModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(11, from(MeteorologicalModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(0, from(MeteorologicalModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(11, from(MeteorologicalModelAccessor::getModelLength))
			.as("Model length")
			.returns(0, from(MeteorologicalModelAccessor::getRepeatingBlockInstanceCount))
			;
		// @formatter:on
	}

	@Test
	public void data() {
		// GIVEN
		MeteorologicalModelAccessor model = getTestDataInstance()
				.findTypedModel(MeteorologicalModelAccessor.class);

		// THEN
		// @formatter:off
		then(model)
			.as("Ambient temperature")
			.returns(328.5f, from(MeteorologicalModelAccessor::getAmbientTemperature))
			.as("Relative humidity")
			.returns(1097, from(MeteorologicalModelAccessor::getRelativeHumidity))
			.as("Atmospheric pressure")
			.returns(110600, from(MeteorologicalModelAccessor::getAtmosphericPressure))
			.as("Wind speed")
			.returns(1082, from(MeteorologicalModelAccessor::getWindSpeed))
			.as("Wind direciton")
			.returns(-1, from(MeteorologicalModelAccessor::getWindDirection))
			.as("Rain")
			.returns(2070, from(MeteorologicalModelAccessor::getRainAccumulation))
			.as("Snow")
			.returns(2090, from(MeteorologicalModelAccessor::getSnowAccumulation))
			.as("Precipitation type")
			.returns(PrecipitationType.PatchyFog,
					from(MeteorologicalModelAccessor::getPrecipitationType))
			.as("Electric field")
			.returns(1172, from(MeteorologicalModelAccessor::getElectricField))
			.as("Surface wetness")
			.returns(1182000, from(MeteorologicalModelAccessor::getSurfaceWetness))
			.as("Soil moisture")
			.returns(1176, from(MeteorologicalModelAccessor::getSoilMoisture))
			;
		// @formatter:on
	}

}
