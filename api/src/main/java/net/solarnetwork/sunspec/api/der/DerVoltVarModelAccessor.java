/* ==================================================================
 * DerVoltVarModelAccessor.java - 5/10/2026 4:58:20 pm
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
 * API for accessing DER volt-var model data.
 *
 * <p>
 * This API corresponds to the SunSpec model number <b>705</b>. Each curve point
 * {@link DerCurvePoint#x()} is a voltage, as a percentage of nominal voltage,
 * and {@link DerCurvePoint#y()} is a reactive power, as a percentage of the
 * curve's {@link VoltVarCurve#getDependentReference()}.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public interface DerVoltVarModelAccessor extends DerCurveModelAccessor {

	/**
	 * API for a single volt-var curve.
	 */
	interface VoltVarCurve extends DerCurve {

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
		 *        the priority to set
		 * @throws IOException
		 *         if any communication error occurs
		 */
		void setPowerPriority(ModbusConnection conn, DerReactivePowerPriority priority)
				throws IOException;

		/**
		 * Get the voltage reference adjustment.
		 *
		 * @return the voltage reference, as a percentage of nominal voltage, or
		 *         {@code null} if not available
		 */
		@Nullable
		Float getVoltageReference();

		/**
		 * Set the voltage reference adjustment.
		 *
		 * @param conn
		 *        the connection to write to
		 * @param percent
		 *        the voltage reference, as a percentage of nominal voltage
		 * @throws IOException
		 *         if any communication error occurs
		 */
		void setVoltageReference(ModbusConnection conn, float percent) throws IOException;

		/**
		 * Get the current autonomous voltage reference.
		 *
		 * @return the voltage reference, as a percentage of nominal voltage, or
		 *         {@code null} if not available
		 */
		@Nullable
		Float getAutonomousVoltageReference();

		/**
		 * Get the autonomous voltage reference enable setting.
		 *
		 * @return {@literal true} if the autonomous voltage reference is
		 *         enabled, or {@code null} if not available
		 */
		@Nullable
		Boolean isAutonomousVoltageReferenceEnabled();

		/**
		 * Set the autonomous voltage reference enable setting.
		 *
		 * @param conn
		 *        the connection to write to
		 * @param enabled
		 *        {@literal true} to enable the autonomous voltage reference
		 * @throws IOException
		 *         if any communication error occurs
		 */
		void setAutonomousVoltageReferenceEnabled(ModbusConnection conn, boolean enabled)
				throws IOException;

		/**
		 * Get the autonomous voltage reference time constant.
		 *
		 * @return the time constant, in seconds, or {@code null} if not
		 *         available
		 */
		@Nullable
		Integer getAutonomousVoltageReferenceTimeConstant();

		/**
		 * Set the autonomous voltage reference time constant.
		 *
		 * @param conn
		 *        the connection to write to
		 * @param seconds
		 *        the time constant, in seconds
		 * @throws IOException
		 *         if any communication error occurs
		 */
		void setAutonomousVoltageReferenceTimeConstant(ModbusConnection conn, int seconds)
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
	List<VoltVarCurve> getCurves();

}
