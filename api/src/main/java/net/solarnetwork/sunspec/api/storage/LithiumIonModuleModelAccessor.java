/* ==================================================================
 * LithiumIonModuleModelAccessor.java - 5/10/2026 9:05:44 pm
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

package net.solarnetwork.sunspec.api.storage;

import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelAccessor;

/**
 * API for accessing SunSpec lithium-ion module model data.
 *
 * <p>
 * This API corresponds to the SunSpec model number <b>805</b>.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public interface LithiumIonModuleModelAccessor extends ModelAccessor {

	/**
	 * API for a single cell in the module.
	 */
	interface BatteryCell {

		/**
		 * Get the cell index.
		 *
		 * @return the index, starting from {@literal 1}
		 */
		int getIndex();

		/**
		 * Get the cell voltage.
		 *
		 * @return the voltage, in V, or {@code null} if not available
		 */
		@Nullable
		Float getVoltage();

		/**
		 * Get the cell temperature.
		 *
		 * @return the temperature, in degrees Celsius, or {@code null} if not
		 *         available
		 */
		@Nullable
		Float getTemperature();

		/**
		 * Get the cell status.
		 *
		 * @return the status flags, never {@code null}
		 */
		Set<LithiumIonCellStatus> getStatus();

	}

	/**
	 * Get the index of the string containing the module.
	 *
	 * @return the index, starting from {@literal 1}, or {@code null} if not
	 *         available
	 */
	@Nullable
	Integer getStringIndex();

	/**
	 * Get the index of the module within the string.
	 *
	 * @return the index, starting from {@literal 1}, or {@code null} if not
	 *         available
	 */
	@Nullable
	Integer getModuleIndex();

	/**
	 * Get the number of cells in the module.
	 *
	 * @return the number of cells, or {@code null} if not available
	 */
	@Nullable
	Integer getCellCount();

	/**
	 * Get the module state of charge.
	 *
	 * @return the state of charge, as a percentage, or {@code null} if not
	 *         available
	 */
	@Nullable
	Float getStateOfCharge();

	/**
	 * Get the module depth of discharge.
	 *
	 * @return the depth of discharge, as a percentage, or {@code null} if not
	 *         available
	 */
	@Nullable
	Float getDepthOfDischarge();

	/**
	 * Get the module state of health.
	 *
	 * @return the state of health, as a percentage, or {@code null} if not
	 *         available
	 */
	@Nullable
	Float getStateOfHealth();

	/**
	 * Get the number of cycles executed.
	 *
	 * @return the cycle count, or {@code null} if not available
	 */
	@Nullable
	Long getCycleCount();

	/**
	 * Get the module voltage.
	 *
	 * @return the voltage, in V, or {@code null} if not available
	 */
	@Nullable
	Float getDCVoltage();

	/**
	 * Get the maximum cell voltage in the module.
	 *
	 * @return the voltage, in V, or {@code null} if not available
	 */
	@Nullable
	Float getMaximumCellVoltage();

	/**
	 * Get the index of the cell with the maximum cell voltage.
	 *
	 * @return the cell index, or {@code null} if not available
	 */
	@Nullable
	Integer getMaximumCellVoltageCellIndex();

	/**
	 * Get the minimum cell voltage in the module.
	 *
	 * @return the voltage, in V, or {@code null} if not available
	 */
	@Nullable
	Float getMinimumCellVoltage();

	/**
	 * Get the index of the cell with the minimum cell voltage.
	 *
	 * @return the cell index, or {@code null} if not available
	 */
	@Nullable
	Integer getMinimumCellVoltageCellIndex();

	/**
	 * Get the average cell voltage in the module.
	 *
	 * @return the voltage, in V, or {@code null} if not available
	 */
	@Nullable
	Float getAverageCellVoltage();

	/**
	 * Get the maximum cell temperature in the module.
	 *
	 * @return the temperature, in degrees Celsius, or {@code null} if not
	 *         available
	 */
	@Nullable
	Float getMaximumCellTemperature();

	/**
	 * Get the index of the cell with the maximum cell temperature.
	 *
	 * @return the cell index, or {@code null} if not available
	 */
	@Nullable
	Integer getMaximumCellTemperatureCellIndex();

	/**
	 * Get the minimum cell temperature in the module.
	 *
	 * @return the temperature, in degrees Celsius, or {@code null} if not
	 *         available
	 */
	@Nullable
	Float getMinimumCellTemperature();

	/**
	 * Get the index of the cell with the minimum cell temperature.
	 *
	 * @return the cell index, or {@code null} if not available
	 */
	@Nullable
	Integer getMinimumCellTemperatureCellIndex();

	/**
	 * Get the average cell temperature in the module.
	 *
	 * @return the temperature, in degrees Celsius, or {@code null} if not
	 *         available
	 */
	@Nullable
	Float getAverageCellTemperature();

	/**
	 * Get the number of cells in the module currently being balanced.
	 *
	 * @return the number of cells, or {@code null} if not available
	 */
	@Nullable
	Integer getBalancingCellCount();

	/**
	 * Get the module serial number.
	 *
	 * @return the serial number, or {@code null} if not available
	 */
	@Nullable
	String getSerialNumber();

	/**
	 * Get the cells.
	 *
	 * <p>
	 * The number of cells is determined by the model length.
	 * </p>
	 *
	 * @return the cells, never {@code null}
	 */
	List<BatteryCell> getCells();

}
