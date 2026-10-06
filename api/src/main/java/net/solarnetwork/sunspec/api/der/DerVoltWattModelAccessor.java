/* ==================================================================
 * DerVoltWattModelAccessor.java - 5/10/2026 4:58:20 pm
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

package net.solarnetwork.sunspec.api.der;

import java.io.IOException;
import java.util.List;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.modbus.ModbusConnection;

/**
 * API for accessing DER volt-watt model data.
 *
 * <p>
 * This API corresponds to the SunSpec model number <b>706</b>. Each curve point
 * {@link DerCurvePoint#x()} is a voltage, as a percentage of nominal voltage,
 * and {@link DerCurvePoint#y()} is an active power, as a percentage of the
 * curve's {@link VoltWattCurve#getDependentReference()}.
 * </p>
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public interface DerVoltWattModelAccessor extends DerCurveModelAccessor {

	/**
	 * API for a single volt-watt curve.
	 */
	interface VoltWattCurve extends DerCurve {

		/**
		 * Get the dependent reference, which the active power of each point is
		 * a percentage of.
		 *
		 * @return the reference, or {@code null} if not available
		 */
		@Nullable
		DerActivePowerReference getDependentReference();

		/**
		 * Set the dependent reference, which the active power of each point is
		 * a percentage of.
		 *
		 * @param conn
		 *        the connection to write to
		 * @param reference
		 *        the reference to set
		 * @throws IOException
		 *         if any communication error occurs
		 */
		void setDependentReference(ModbusConnection conn, DerActivePowerReference reference)
				throws IOException;

		/**
		 * Get the open loop response time.
		 *
		 * @return the response time, in seconds, or {@code null} if not
		 *         available
		 */
		@Nullable
		Float getOpenLoopResponseTime();

		/**
		 * Set the open loop response time.
		 *
		 * @param conn
		 *        the connection to write to
		 * @param seconds
		 *        the response time, in seconds
		 * @throws IOException
		 *         if any communication error occurs
		 */
		void setOpenLoopResponseTime(ModbusConnection conn, float seconds) throws IOException;

	}

	@Override
	List<VoltWattCurve> getCurves();

}
