/* ==================================================================
 * InverterModelAccessor.java - 5/10/2018 3:59:35 PM
 *
 * Copyright 2018 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.api.inverter;

import static net.solarnetwork.sunspec.api.AcPhase.PhaseA;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseB;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseC;
import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.AcPhase;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.OperatingState;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * API for accessing inverter model data.
 *
 * @author matt
 * @version 1.0
 */
public interface InverterModelAccessor extends ModelAccessor {

	/**
	 * Get the current, in A.
	 *
	 * @return the current
	 */
	@Nullable
	Float getCurrent();

	/**
	 * Get the voltage, in V.
	 *
	 * @return the voltage
	 */
	@Nullable
	Float getVoltage();

	/**
	 * Get the line voltage, in V.
	 *
	 * @return the voltage
	 */
	@Nullable
	Float getLineVoltage();

	/**
	 * Get the active (real) power, in W.
	 *
	 * @return the active power
	 */
	@Nullable
	BigDecimal getActivePower();

	/**
	 * Get the AC frequency value, in Hz.
	 *
	 * @return the frequency
	 */
	@Nullable
	Float getFrequency();

	/**
	 * Get the apparent power, in VA.
	 *
	 * @return the apparent power
	 */
	@Nullable
	BigDecimal getApparentPower();

	/**
	 * Get the reactive power, in VAR.
	 *
	 * @return the reactive power
	 */
	@Nullable
	BigDecimal getReactivePower();

	/**
	 * Get the power factor, as a decimal from -1.0 to 1.0.
	 *
	 * @return the power factor
	 */
	@Nullable
	Float getPowerFactor();

	/**
	 * Get the active energy exported, in Wh.
	 *
	 * @return the exported active energy
	 */
	@Nullable
	BigDecimal getActiveEnergyExported();

	/**
	 * Get the active energy imported, in Wh.
	 *
	 * @return the imported active energy
	 */
	@Nullable
	BigDecimal getActiveEnergyImported();

	/**
	 * Get the reactive energy exported, in VARh.
	 *
	 * @return the delivered reactive energy
	 */
	@Nullable
	BigDecimal getReactiveEnergyExported();

	/**
	 * Get the reactive energy imported in VARh.
	 *
	 * @return the received reactive energy
	 */
	@Nullable
	BigDecimal getReactiveEnergyImported();

	/**
	 * Get the DC current, in A.
	 *
	 * @return the DC current
	 */
	@Nullable
	Float getDcCurrent();

	/**
	 * Get the DC voltage, in V.
	 *
	 * @return the DC voltage
	 */
	@Nullable
	Float getDcVoltage();

	/**
	 * Get the DC power, in W.
	 *
	 * @return the DC power
	 */
	@Nullable
	BigDecimal getDcPower();

	/**
	 * Get the cabinet temperature, in degrees Celsius.
	 *
	 * @return the cabinet temperature
	 */
	@Nullable
	Float getCabinetTemperature();

	/**
	 * Get the heat sink temperature, in degrees Celsius.
	 *
	 * @return the heat sink temperature
	 */
	@Nullable
	Float getHeatSinkTemperature();

	/**
	 * Get the transformer temperature, in degrees Celsius.
	 *
	 * @return the transformer temperature
	 */
	@Nullable
	Float getTransformerTemperature();

	/**
	 * Get the vendor-specific "other" temperature, in degrees Celsius.
	 *
	 * @return the "other" temperature
	 */
	@Nullable
	Float getOtherTemperature();

	/**
	 * Get the operating state.
	 *
	 * @return the state
	 */
	@Nullable
	OperatingState getOperatingState();

	/**
	 * Get an optional vendor-specific operating state value.
	 *
	 * @return the vendor operating state value, or {@code null} if not
	 *         supported or known
	 */
	default @Nullable Integer getVendorOperatingState() {
		return null;
	}

	/**
	 * Get the active events.
	 *
	 * @return the events, never {@code null}
	 */
	Set<? extends ModelEvent> getEvents();

	/**
	 * Get an optional vendor-specific bit set of event codes.
	 *
	 * <p>
	 * Note that all SunSpec "vendor event" fields are presented as a single bit
	 * set, with each 32-bit event group offset by 32. For example if a model
	 * defines {@code EvtVnd1} and {@code EvtVnd2} 32-bit properties, there are
	 * 64 possible bits where {@code EvtVnd1}'s first bit would be index
	 * {@code 0} and {@code EvtVnd2}'s first bit would be index {@code 32}. A
	 * field with its most significant bit set is not implemented, as SunSpec
	 * bitfields never set that bit, and contributes no events.
	 * </p>
	 *
	 * @return the vendor events, or {@code null} if not supported or known
	 */
	default @Nullable BitSet getVendorEvents() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This implementation supports the {@link IntegerInverterModelRegister} and
	 * {@link FloatingPointInverterModelRegister} points, and returns the value
	 * of the corresponding accessor method, from
	 * {@link #accessorForPhase(AcPhase)} for phase points.
	 * </p>
	 */
	@Override
	default @Nullable Object getPointValue(ModbusReference point) {
		final IntegerInverterModelRegister r;
		if ( point instanceof IntegerInverterModelRegister i ) {
			r = i;
		} else if ( point instanceof FloatingPointInverterModelRegister f ) {
			// the floating point registers are named like the integer ones, without scale factors
			r = IntegerInverterModelRegister.valueOf(f.name());
		} else {
			return null;
		}
		return switch (r) {
			case CurrentTotal -> getCurrent();
			case CurrentPhaseA -> accessorForPhase(PhaseA).getCurrent();
			case CurrentPhaseB -> accessorForPhase(PhaseB).getCurrent();
			case CurrentPhaseC -> accessorForPhase(PhaseC).getCurrent();
			case VoltagePhaseAPhaseB -> accessorForPhase(PhaseA).getLineVoltage();
			case VoltagePhaseBPhaseC -> accessorForPhase(PhaseB).getLineVoltage();
			case VoltagePhaseCPhaseA -> accessorForPhase(PhaseC).getLineVoltage();
			case VoltagePhaseANeutral -> accessorForPhase(PhaseA).getVoltage();
			case VoltagePhaseBNeutral -> accessorForPhase(PhaseB).getVoltage();
			case VoltagePhaseCNeutral -> accessorForPhase(PhaseC).getVoltage();
			case ActivePowerTotal -> getActivePower();
			case Frequency -> getFrequency();
			case ApparentPowerTotal -> getApparentPower();
			case ReactivePowerTotal -> getReactivePower();
			case PowerFactorAverage -> getPowerFactor();
			case ActiveEnergyExportedTotal -> getActiveEnergyExported();
			case DcCurrentTotal -> getDcCurrent();
			case DcVoltageTotal -> getDcVoltage();
			case DcPowerTotal -> getDcPower();
			case TemperatureCabinet -> getCabinetTemperature();
			case TemperatureHeatSink -> getHeatSinkTemperature();
			case TemperatureTransformer -> getTransformerTemperature();
			case TemperatureOther -> getOtherTemperature();
			case OperatingState -> getOperatingState();
			case OperatingStateVendor -> getVendorOperatingState();
			case EventsBitmask -> getEvents();
			case Events2Bitmask -> null; // reserved for future use
			case EventsVendorBitmask -> getVendorEvents();
			case Events2VendorBitmask, Events3VendorBitmask -> null; // included in the vendor events
			case Events4VendorBitmask -> null; // included in the vendor events
			case ScaleFactorCurrent, ScaleFactorVoltage, ScaleFactorActivePower -> null;
			case ScaleFactorFrequency -> null;
			case ScaleFactorApparentPower, ScaleFactorReactivePower, ScaleFactorPowerFactor -> null;
			case ScaleFactorActiveEnergy, ScaleFactorDcCurrent, ScaleFactorDcVoltage -> null;
			case ScaleFactorDcPower -> null;
			case ScaleFactorTemperature -> null;
		};
	}

	/**
	 * Get an accessor for phase-specific measurements.
	 *
	 * @param phase
	 *        the phase to get an accessor for
	 * @return the accessor
	 */
	InverterModelAccessor accessorForPhase(AcPhase phase);

	/**
	 * Get a "reversed" model accessor, where import/export directions are
	 * switched.
	 *
	 * @return the reversed accessor
	 */
	default InverterModelAccessor reversed() {
		return new ReversedInverterModelAccessor(this);
	}

}
