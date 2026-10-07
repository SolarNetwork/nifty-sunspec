/* ==================================================================
 * MeterModelAccessor.java - 22/05/2018 6:26:19 AM
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

package net.solarnetwork.sunspec.api.meter;

import static net.solarnetwork.sunspec.api.AcPhase.PhaseA;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseB;
import static net.solarnetwork.sunspec.api.AcPhase.PhaseC;
import java.math.BigDecimal;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.AcPhase;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * API for accessing meter model data.
 *
 * @author matt
 * @version 1.0
 */
public interface MeterModelAccessor extends ModelAccessor {

	/**
	 * Get the AC frequency value, in Hz.
	 *
	 * @return the frequency
	 */
	@Nullable
	Float getFrequency();

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
	 * Get the power factor, as a decimal from -1.0 to 1.0.
	 *
	 * @return the power factor
	 */
	@Nullable
	Float getPowerFactor();

	/**
	 * Get the active (real) power, in W.
	 *
	 * @return the active power
	 */
	@Nullable
	BigDecimal getActivePower();

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
	 * Get the active energy imported (delivered), in Wh.
	 *
	 * @return the imported active energy
	 */
	@Nullable
	BigDecimal getActiveEnergyImported();

	/**
	 * Get the active energy exported (received), in Wh.
	 *
	 * @return the exported active energy
	 */
	@Nullable
	BigDecimal getActiveEnergyExported();

	/**
	 * Get the reactive energy imported (delivered), in VARh.
	 *
	 * <p>
	 * This is the sum of the quadrant 1 and 2 values.
	 * </p>
	 *
	 * @return the imported reactive energy
	 */
	@Nullable
	BigDecimal getReactiveEnergyImported();

	/**
	 * Get the reactive energy exported (received), in VARh.
	 *
	 * <p>
	 * This is the sum of the quadrant 3 and 4 values.
	 * </p>
	 *
	 * @return the exported reactive energy
	 */
	@Nullable
	BigDecimal getReactiveEnergyExported();

	/**
	 * Get the reactive energy imported (delivered) in quadrant 1, in VARh.
	 *
	 * @return the quadrant 1 imported reactive energy
	 */
	@Nullable
	BigDecimal getReactiveEnergyImportedQ1();

	/**
	 * Get the reactive energy imported (delivered) in quadrant 2, in VARh.
	 *
	 * @return the quadrant 2 imported reactive energy
	 */
	@Nullable
	BigDecimal getReactiveEnergyImportedQ2();

	/**
	 * Get the reactive energy exported (received) in quadrant 3, in VARh.
	 *
	 * @return the quadrant 3 exported reactive energy
	 */
	@Nullable
	BigDecimal getReactiveEnergyExportedQ3();

	/**
	 * Get the reactive energy exported (received) in quadrant 4, in VARh.
	 *
	 * @return the quadrant 4 exported reactive energy
	 */
	@Nullable
	BigDecimal getReactiveEnergyExportedQ4();

	/**
	 * Get the apparent energy imported (delivered), in VAh.
	 *
	 * @return the imported apparent energy
	 */
	@Nullable
	BigDecimal getApparentEnergyImported();

	/**
	 * Get the apparent energy exported (received), in VAh.
	 *
	 * @return the exported apparent energy
	 */
	@Nullable
	BigDecimal getApparentEnergyExported();

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This implementation supports the {@link IntegerMeterModelRegister} and
	 * {@link FloatingPointMeterModelRegister} points, and returns the value of
	 * the corresponding accessor method, from
	 * {@link #accessorForPhase(AcPhase)} for phase points.
	 * </p>
	 */
	@Override
	default @Nullable Object getPointValue(ModbusReference point) {
		final IntegerMeterModelRegister r;
		if ( point instanceof IntegerMeterModelRegister i ) {
			r = i;
		} else if ( point instanceof FloatingPointMeterModelRegister f ) {
			// the floating point registers are named like the integer ones, without scale factors
			r = IntegerMeterModelRegister.valueOf(f.name());
		} else {
			return null;
		}
		return switch (r) {
			case CurrentTotal -> getCurrent();
			case CurrentPhaseA -> accessorForPhase(PhaseA).getCurrent();
			case CurrentPhaseB -> accessorForPhase(PhaseB).getCurrent();
			case CurrentPhaseC -> accessorForPhase(PhaseC).getCurrent();
			case VoltageLineNeutralAverage -> getVoltage();
			case VoltagePhaseANeutral -> accessorForPhase(PhaseA).getVoltage();
			case VoltagePhaseBNeutral -> accessorForPhase(PhaseB).getVoltage();
			case VoltagePhaseCNeutral -> accessorForPhase(PhaseC).getVoltage();
			case VoltageLineLineAverage -> getLineVoltage();
			case VoltagePhaseAPhaseB -> accessorForPhase(PhaseA).getLineVoltage();
			case VoltagePhaseBPhaseC -> accessorForPhase(PhaseB).getLineVoltage();
			case VoltagePhaseCPhaseA -> accessorForPhase(PhaseC).getLineVoltage();
			case Frequency -> getFrequency();
			case ActivePowerTotal -> getActivePower();
			case ActivePowerPhaseA -> accessorForPhase(PhaseA).getActivePower();
			case ActivePowerPhaseB -> accessorForPhase(PhaseB).getActivePower();
			case ActivePowerPhaseC -> accessorForPhase(PhaseC).getActivePower();
			case ApparentPowerTotal -> getApparentPower();
			case ApparentPowerPhaseA -> accessorForPhase(PhaseA).getApparentPower();
			case ApparentPowerPhaseB -> accessorForPhase(PhaseB).getApparentPower();
			case ApparentPowerPhaseC -> accessorForPhase(PhaseC).getApparentPower();
			case ReactivePowerTotal -> getReactivePower();
			case ReactivePowerPhaseA -> accessorForPhase(PhaseA).getReactivePower();
			case ReactivePowerPhaseB -> accessorForPhase(PhaseB).getReactivePower();
			case ReactivePowerPhaseC -> accessorForPhase(PhaseC).getReactivePower();
			case PowerFactorAverage -> getPowerFactor();
			case PowerFactorPhaseA -> accessorForPhase(PhaseA).getPowerFactor();
			case PowerFactorPhaseB -> accessorForPhase(PhaseB).getPowerFactor();
			case PowerFactorPhaseC -> accessorForPhase(PhaseC).getPowerFactor();
			case ActiveEnergyExportedTotal -> getActiveEnergyExported();
			case ActiveEnergyExportedPhaseA -> accessorForPhase(PhaseA).getActiveEnergyExported();
			case ActiveEnergyExportedPhaseB -> accessorForPhase(PhaseB).getActiveEnergyExported();
			case ActiveEnergyExportedPhaseC -> accessorForPhase(PhaseC).getActiveEnergyExported();
			case ActiveEnergyImportedTotal -> getActiveEnergyImported();
			case ActiveEnergyImportedPhaseA -> accessorForPhase(PhaseA).getActiveEnergyImported();
			case ActiveEnergyImportedPhaseB -> accessorForPhase(PhaseB).getActiveEnergyImported();
			case ActiveEnergyImportedPhaseC -> accessorForPhase(PhaseC).getActiveEnergyImported();
			case ApparentEnergyExportedTotal -> getApparentEnergyExported();
			case ApparentEnergyExportedPhaseA -> accessorForPhase(PhaseA).getApparentEnergyExported();
			case ApparentEnergyExportedPhaseB -> accessorForPhase(PhaseB).getApparentEnergyExported();
			case ApparentEnergyExportedPhaseC -> accessorForPhase(PhaseC).getApparentEnergyExported();
			case ApparentEnergyImportedTotal -> getApparentEnergyImported();
			case ApparentEnergyImportedPhaseA -> accessorForPhase(PhaseA).getApparentEnergyImported();
			case ApparentEnergyImportedPhaseB -> accessorForPhase(PhaseB).getApparentEnergyImported();
			case ApparentEnergyImportedPhaseC -> accessorForPhase(PhaseC).getApparentEnergyImported();
			case ReactiveEnergyImportedQ1Total -> getReactiveEnergyImportedQ1();
			case ReactiveEnergyImportedQ1PhaseA -> accessorForPhase(PhaseA)
					.getReactiveEnergyImportedQ1();
			case ReactiveEnergyImportedQ1PhaseB -> accessorForPhase(PhaseB)
					.getReactiveEnergyImportedQ1();
			case ReactiveEnergyImportedQ1PhaseC -> accessorForPhase(PhaseC)
					.getReactiveEnergyImportedQ1();
			case ReactiveEnergyImportedQ2Total -> getReactiveEnergyImportedQ2();
			case ReactiveEnergyImportedQ2PhaseA -> accessorForPhase(PhaseA)
					.getReactiveEnergyImportedQ2();
			case ReactiveEnergyImportedQ2PhaseB -> accessorForPhase(PhaseB)
					.getReactiveEnergyImportedQ2();
			case ReactiveEnergyImportedQ2PhaseC -> accessorForPhase(PhaseC)
					.getReactiveEnergyImportedQ2();
			case ReactiveEnergyExportedQ3Total -> getReactiveEnergyExportedQ3();
			case ReactiveEnergyExportedQ3PhaseA -> accessorForPhase(PhaseA)
					.getReactiveEnergyExportedQ3();
			case ReactiveEnergyExportedQ3PhaseB -> accessorForPhase(PhaseB)
					.getReactiveEnergyExportedQ3();
			case ReactiveEnergyExportedQ3PhaseC -> accessorForPhase(PhaseC)
					.getReactiveEnergyExportedQ3();
			case ReactiveEnergyExportedQ4Total -> getReactiveEnergyExportedQ4();
			case ReactiveEnergyExportedQ4PhaseA -> accessorForPhase(PhaseA)
					.getReactiveEnergyExportedQ4();
			case ReactiveEnergyExportedQ4PhaseB -> accessorForPhase(PhaseB)
					.getReactiveEnergyExportedQ4();
			case ReactiveEnergyExportedQ4PhaseC -> accessorForPhase(PhaseC)
					.getReactiveEnergyExportedQ4();
			case EventsBitmask -> getEvents();
			case ScaleFactorCurrent, ScaleFactorVoltage, ScaleFactorFrequency -> null;
			case ScaleFactorActivePower, ScaleFactorApparentPower, ScaleFactorReactivePower -> null;
			case ScaleFactorPowerFactor, ScaleFactorActiveEnergy, ScaleFactorApparentEnergy -> null;
			case ScaleFactorReactiveEnergy -> null;
		};
	}

	/**
	 * Get an accessor for phase-specific measurements.
	 *
	 * @param phase
	 *        the phase to get an accessor for
	 * @return the accessor
	 */
	MeterModelAccessor accessorForPhase(AcPhase phase);

	/**
	 * Get a "reversed" model accessor, where import/export directions are
	 * switched.
	 *
	 * @return the reversed accessor
	 */
	default MeterModelAccessor reversed() {
		return new ReversedMeterModelAccessor(this);
	}

	/**
	 * Get the active events.
	 *
	 * @return the events (never {@code null})
	 */
	Set<? extends ModelEvent> getEvents();

}
