/* ==================================================================
 * MeasurementUnits.java - 7/10/2026 6:11:16 pm
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

package net.solarnetwork.sunspec.api;

import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Abbreviations of the units of measurement of SunSpec points.
 *
 * <p>
 * The SunSpec model definitions spell some units in several ways, such as
 * {@code var}, {@code Var}, and {@code VAr}, or {@code Pct} and {@code %}. The
 * abbreviations here are used for all of them, and describe the values the
 * model accessors return, which in a few cases are converted from the SunSpec
 * unit. Percentages of a reference value, such as a percentage of nominal
 * voltage, are all {@link #PERCENT}.
 * </p>
 *
 * @author matt
 * @version 1.0
 * @see ModbusReference#getMeasurementUnit()
 */
public final class MeasurementUnits {

	/** Current, in amperes. */
	public static final String AMPERE = "A";

	/** Charge, in ampere-hours. */
	public static final String AMPERE_HOUR = "Ah";

	/** An angle, in degrees. */
	public static final String DEGREE = "°";

	/** Temperature, in degrees Celsius. */
	public static final String DEGREE_CELSIUS = "°C";

	/** Frequency, in hertz. */
	public static final String HERTZ = "Hz";

	/** Length, in metres. */
	public static final String METRE = "m";

	/** Speed, in metres per second. */
	public static final String METRE_PER_SECOND = "m/s";

	/** Length, in millimetres. */
	public static final String MILLIMETRE = "mm";

	/** Resistance, in ohms. */
	public static final String OHM = "Ω";

	/** Pressure, in pascals. */
	public static final String PASCAL = "Pa";

	/** A percentage. */
	public static final String PERCENT = "%";

	/** A rate of change, in percent per day. */
	public static final String PERCENT_PER_DAY = "%/d";

	/** A rate of change, in percent per second. */
	public static final String PERCENT_PER_SECOND = "%/s";

	/** Time, in seconds. */
	public static final String SECOND = "s";

	/** Susceptance, in siemens. */
	public static final String SIEMENS = "S";

	/** Electric potential, in volts. */
	public static final String VOLT = "V";

	/** Apparent power, in volt-amperes. */
	public static final String VOLT_AMPERE = "VA";

	/** Apparent energy, in volt-ampere-hours. */
	public static final String VOLT_AMPERE_HOUR = "VAh";

	/** Reactive power, in volt-amperes reactive. */
	public static final String VOLT_AMPERE_REACTIVE = "VAr";

	/** Reactive energy, in volt-ampere-reactive-hours. */
	public static final String VOLT_AMPERE_REACTIVE_HOUR = "VArh";

	/** Electric field strength, in volts per metre. */
	public static final String VOLT_PER_METRE = "V/m";

	/** Active power, in watts. */
	public static final String WATT = "W";

	/** Active energy, in watt-hours. */
	public static final String WATT_HOUR = "Wh";

	/** Irradiance, in watts per square metre. */
	public static final String WATT_PER_SQUARE_METRE = "W/m²";

	private MeasurementUnits() {
		// not available
	}

}
