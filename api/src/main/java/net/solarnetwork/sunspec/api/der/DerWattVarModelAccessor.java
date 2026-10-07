/* ==================================================================
 * DerWattVarModelAccessor.java - 5/10/2026 4:58:20 pm
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
 * API for accessing DER watt-var model data.
 *
 * <p>
 * This API corresponds to the SunSpec model number <b>712</b>. Each curve point
 * {@link DerCurvePoint#x()} is an active power, as a percentage of maximum
 * active power, and {@link DerCurvePoint#y()} is a reactive power, as a
 * percentage of the curve's {@link WattVarCurve#getDependentReference()}.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public interface DerWattVarModelAccessor extends DerCurveModelAccessor {

	/**
	 * API for a single watt-var curve.
	 */
	interface WattVarCurve extends DerCurve {

		/**
		 * Get the dependent reference, which the reactive power of each point
		 * is a percentage of.
		 *
		 * @return the reference, or {@code null} if not available
		 */
		@Nullable
		DerReactivePowerReference getDependentReference();

		/**
		 * Set the dependent reference, which the reactive power of each point
		 * is a percentage of.
		 *
		 * @param conn
		 *        the connection to write to
		 * @param reference
		 *        the reference to set
		 * @throws IOException
		 *         if any communication error occurs
		 */
		void setDependentReference(ModbusConnection conn, DerReactivePowerReference reference)
				throws IOException;

		/**
		 * Get the power priority.
		 *
		 * <p>
		 * The watt-var model supports only the
		 * {@link DerReactivePowerPriority#ActivePower} and
		 * {@link DerReactivePowerPriority#ReactivePower} priorities.
		 * </p>
		 *
		 * @return the priority, or {@code null} if not available
		 */
		@Nullable
		DerReactivePowerPriority getPowerPriority();

		/**
		 * Set the power priority.
		 *
		 * @param conn
		 *        the connection to write to
		 * @param priority
		 *        the priority to set, either
		 *        {@link DerReactivePowerPriority#ActivePower} or
		 *        {@link DerReactivePowerPriority#ReactivePower}
		 * @throws IllegalArgumentException
		 *         if {@code priority} is not supported by the watt-var model
		 * @throws IOException
		 *         if any communication error occurs
		 */
		void setPowerPriority(ModbusConnection conn, DerReactivePowerPriority priority)
				throws IOException;

	}

	@Override
	List<WattVarCurve> getCurves();

}
