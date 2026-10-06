/* ==================================================================
 * DerWattVarModelAccessorImpl_712_01Tests.java - 5/10/2026 5:31:07 pm
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

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.der.DerCurvePoint;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerReactivePowerPriority;
import net.solarnetwork.sunspec.api.der.DerReactivePowerReference;
import net.solarnetwork.sunspec.api.der.DerWattVarModelAccessor;
import net.solarnetwork.sunspec.api.der.DerWattVarModelAccessor.WattVarCurve;
import net.solarnetwork.sunspec.core.der.DerWattVarModelAccessorImpl;
import net.solarnetwork.sunspec.core.test.ModelDataUtils;
import net.solarnetwork.sunspec.core.test.RecordingModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * Test cases for the {@link DerWattVarModelAccessorImpl} class.
 *
 * @author matt
 * @version 1.0
 */
public class DerWattVarModelAccessorImpl_712_01Tests {

	private static final String TEST_DATA = "test-data-der-01.txt";

	private static final List<DerCurvePoint> NEW_POINTS = List.of(new DerCurvePoint(-100.0f, 20.0f),
			new DerCurvePoint(-50.0f, 10.0f), new DerCurvePoint(-10.0f, 0.0f),
			new DerCurvePoint(10.0f, 0.0f), new DerCurvePoint(50.0f, -10.0f),
			new DerCurvePoint(100.0f, -20.0f));

	private DerWattVarModelAccessor getTestModel() {
		return ModelDataUtils.getModelDataInstance(getClass(), TEST_DATA)
				.findTypedModel(DerWattVarModelAccessor.class);
	}

	private static DerWattVarModelAccessor discoverModel(ModbusConnection conn) {
		return ModelDataUtils.getModelDataInstance(conn).findTypedModel(DerWattVarModelAccessor.class);
	}

	@Test
	public void findTypedModel() {
		// @formatter:off
		then(getTestModel())
			.as("Model found by accessor type")
			.isInstanceOf(DerWattVarModelAccessorImpl.class)
			;
		// @formatter:on
	}

	@Test
	public void block() {
		// GIVEN
		DerWattVarModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Model base address")
			.returns(1149, from(DerWattVarModelAccessor::getBaseAddress))
			.as("Model block address")
			.returns(1151, from(DerWattVarModelAccessor::getBlockAddress))
			.as("Model ID")
			.returns(DerModelId.WattVar, from(DerWattVarModelAccessor::getModelId))
			.as("Model fixed length")
			.returns(12, from(DerWattVarModelAccessor::getFixedBlockLength))
			.as("Model repeating instance length")
			.returns(16, from(DerWattVarModelAccessor::getRepeatingBlockInstanceLength))
			.as("Model length")
			.returns(60, from(DerWattVarModelAccessor::getModelLength))
			;
		// @formatter:on
	}

	@Test
	public void curves() {
		// GIVEN
		DerWattVarModelAccessor model = getTestModel();

		// THEN
		// @formatter:off
		then(model)
			.as("Enabled")
			.returns(false, from(DerWattVarModelAccessor::isEnabled))
			.as("Curve count")
			.returns(3, from(DerWattVarModelAccessor::getCurveCount))
			.as("Curve point count")
			.returns(6, from(DerWattVarModelAccessor::getCurvePointCount))
			;
		// @formatter:on

		List<WattVarCurve> curves = model.getCurves();
		// @formatter:off
		then(curves)
			.as("Curves")
			.hasSize(3)
			;
		// @formatter:on
		WattVarCurve curve = curves.get(0);
		// @formatter:off
		then(curve)
			.as("Read-only")
			.returns(true, from(WattVarCurve::isReadOnly))
			.as("Dependent reference")
			.returns(DerReactivePowerReference.MaximumActivePowerPercent,
					from(WattVarCurve::getDependentReference))
			.as("Power priority")
			.returns(DerReactivePowerPriority.ActivePower, from(WattVarCurve::getPowerPriority))
			.as("Points")
			.returns(List.of(new DerCurvePoint(-100.0f, 0.0f), new DerCurvePoint(-50.0f, 0.0f),
					new DerCurvePoint(-20.0f, 0.0f), new DerCurvePoint(20.0f, 0.0f),
					new DerCurvePoint(50.0f, 0.0f), new DerCurvePoint(100.0f, -25.0f)),
					from(WattVarCurve::getPoints))
			;
		// @formatter:on
	}

	@Test
	public void writeCurve() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		WattVarCurve curve = discoverModel(conn).getCurves().get(1);

		// WHEN
		curve.setDependentReference(conn, DerReactivePowerReference.AvailableReactivePowerPercent);
		curve.setPowerPriority(conn, DerReactivePowerPriority.ReactivePower);
		curve.setPoints(conn, NEW_POINTS);

		// THEN
		WattVarCurve device = discoverModel(conn).getCurves().get(1);
		// @formatter:off
		then(device)
			.as("Dependent reference")
			.returns(DerReactivePowerReference.AvailableReactivePowerPercent,
					from(WattVarCurve::getDependentReference))
			.as("Power priority")
			.returns(DerReactivePowerPriority.ReactivePower, from(WattVarCurve::getPowerPriority))
			.as("Points")
			.returns(NEW_POINTS, from(WattVarCurve::getPoints))
			;
		// @formatter:on
	}

	@Test
	public void setPowerPriority_vendor() throws IOException {
		// GIVEN
		RecordingModbusConnection conn = ModelDataUtils.getWritableModbusConnection(getClass(),
				TEST_DATA);
		WattVarCurve curve = discoverModel(conn).getCurves().get(1);

		// WHEN
		Throwable t = catchThrowable(
				() -> curve.setPowerPriority(conn, DerReactivePowerPriority.Vendor));

		// THEN
		// @formatter:off
		then(t)
			.as("Vendor priority not supported by the watt-var model")
			.isInstanceOf(IllegalArgumentException.class)
			;
		then(conn.getWrites())
			.as("Nothing written")
			.isEmpty()
			;
		// @formatter:on
	}

}
