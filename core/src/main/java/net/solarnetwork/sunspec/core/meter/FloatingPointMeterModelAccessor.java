/* ==================================================================
 * FloatingPointMeterModelAccessor.java - 6/10/2026 4:31:05 pm
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

package net.solarnetwork.sunspec.core.meter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.AcPhase;
import net.solarnetwork.sunspec.api.IntRange;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.meter.FloatingPointMeterModelRegister;
import net.solarnetwork.sunspec.api.meter.MeterModelAccessor;
import net.solarnetwork.sunspec.api.meter.MeterModelEvent;
import net.solarnetwork.sunspec.api.meter.MeterModelId;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Data object for a floating point meter model.
 *
 * @author matt
 * @version 1.0
 */
public class FloatingPointMeterModelAccessor extends BaseModelAccessor implements MeterModelAccessor {

	/** The floating point meter model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 124;

	/**
	 * Constructor.
	 *
	 * @param data
	 *        the overall data object
	 * @param baseAddress
	 *        the base address for this model's data
	 * @param modelId
	 *        the model ID
	 */
	public FloatingPointMeterModelAccessor(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@link MeterModelId} class will be used as the {@code ModelId}
	 * instance.
	 * </p>
	 *
	 * @param data
	 *        the overall data object
	 * @param baseAddress
	 *        the base address for this model's data
	 * @param modelId
	 *        the model ID
	 */
	public FloatingPointMeterModelAccessor(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, MeterModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(FloatingPointMeterModelRegister.class);
	}

	@Override
	public MeterModelAccessor accessorForPhase(AcPhase phase) {
		if ( phase == AcPhase.Total ) {
			return this;
		}
		return new PhaseMeterModelAccessor(phase);
	}

	@Override
	public @Nullable Float getFrequency() {
		return getFloatValue(FloatingPointMeterModelRegister.Frequency);
	}

	@Override
	public @Nullable Float getCurrent() {
		return getFloatValue(FloatingPointMeterModelRegister.CurrentTotal);
	}

	@Override
	public @Nullable Float getVoltage() {
		return getFloatValue(FloatingPointMeterModelRegister.VoltageLineNeutralAverage);
	}

	@Override
	public @Nullable Float getLineVoltage() {
		return getFloatValue(FloatingPointMeterModelRegister.VoltageLineLineAverage);
	}

	@Override
	public @Nullable Float getPowerFactor() {
		return getFloatValue(FloatingPointMeterModelRegister.PowerFactorAverage);
	}

	@Override
	public @Nullable BigDecimal getActivePower() {
		return getDecimalValue(FloatingPointMeterModelRegister.ActivePowerTotal);
	}

	@Override
	public @Nullable BigDecimal getApparentPower() {
		return getDecimalValue(FloatingPointMeterModelRegister.ApparentPowerTotal);
	}

	@Override
	public @Nullable BigDecimal getReactivePower() {
		return getDecimalValue(FloatingPointMeterModelRegister.ReactivePowerTotal);
	}

	@Override
	public @Nullable BigDecimal getActiveEnergyImported() {
		return getDecimalValue(FloatingPointMeterModelRegister.ActiveEnergyImportedTotal);
	}

	@Override
	public @Nullable BigDecimal getActiveEnergyExported() {
		return getDecimalValue(FloatingPointMeterModelRegister.ActiveEnergyExportedTotal);
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyImported() {
		return sum(getReactiveEnergyImportedQ1(), getReactiveEnergyImportedQ2());
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyExported() {
		return sum(getReactiveEnergyExportedQ3(), getReactiveEnergyExportedQ4());
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyImportedQ1() {
		return getDecimalValue(FloatingPointMeterModelRegister.ReactiveEnergyImportedQ1Total);
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyImportedQ2() {
		return getDecimalValue(FloatingPointMeterModelRegister.ReactiveEnergyImportedQ2Total);
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyExportedQ3() {
		return getDecimalValue(FloatingPointMeterModelRegister.ReactiveEnergyExportedQ3Total);
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyExportedQ4() {
		return getDecimalValue(FloatingPointMeterModelRegister.ReactiveEnergyExportedQ4Total);
	}

	@Override
	public @Nullable BigDecimal getApparentEnergyImported() {
		return getDecimalValue(FloatingPointMeterModelRegister.ApparentEnergyImportedTotal);
	}

	@Override
	public @Nullable BigDecimal getApparentEnergyExported() {
		return getDecimalValue(FloatingPointMeterModelRegister.ApparentEnergyExportedTotal);
	}

	@Override
	public Set<? extends ModelEvent> getEvents() {
		Number n = getBitfield(FloatingPointMeterModelRegister.EventsBitmask);
		return MeterModelEvent.forBitmask(n != null ? n.longValue() : 0L);
	}

	/**
	 * Add two reactive energy quadrant values.
	 *
	 * @param a
	 *        the first value
	 * @param b
	 *        the second value
	 * @return the sum, or {@code null} if both values are {@code null}
	 */
	private static @Nullable BigDecimal sum(@Nullable BigDecimal a, @Nullable BigDecimal b) {
		if ( a == null ) {
			return b;
		}
		return (b != null ? a.add(b) : a);
	}

	/**
	 * Accessor for the measurements of a single phase.
	 */
	private class PhaseMeterModelAccessor implements MeterModelAccessor {

		private final AcPhase phase;

		/**
		 * Constructor.
		 *
		 * @param phase
		 *        the phase, one of {@code PhaseA}, {@code PhaseB}, or
		 *        {@code PhaseC}
		 */
		private PhaseMeterModelAccessor(AcPhase phase) {
			super();
			this.phase = phase;
		}

		/**
		 * Get the register for this accessor's phase.
		 *
		 * @param phaseA
		 *        the phase A register
		 * @param phaseB
		 *        the phase B register
		 * @param phaseC
		 *        the phase C register
		 * @return the register
		 */
		private FloatingPointMeterModelRegister register(FloatingPointMeterModelRegister phaseA,
				FloatingPointMeterModelRegister phaseB, FloatingPointMeterModelRegister phaseC) {
			return switch (phase) {
				case PhaseA -> phaseA;
				case PhaseB -> phaseB;
				default -> phaseC;
			};
		}

		@Override
		public IntRange[] getAddressRanges(int maxRangeLength) {
			return FloatingPointMeterModelAccessor.this.getAddressRanges(maxRangeLength);
		}

		@Override
		public IntRange getAddressRange(int address, int maxRangeLength) {
			return FloatingPointMeterModelAccessor.this.getAddressRange(address, maxRangeLength);
		}

		@Override
		public List<IntRange> getUnsplittableAddressRanges() {
			return FloatingPointMeterModelAccessor.this.getUnsplittableAddressRanges();
		}

		@Override
		public @Nullable Instant getDataTimestamp() {
			return FloatingPointMeterModelAccessor.this.getDataTimestamp();
		}

		@Override
		public int getBaseAddress() {
			return FloatingPointMeterModelAccessor.this.getBaseAddress();
		}

		@Override
		public int getFixedBlockLength() {
			return FloatingPointMeterModelAccessor.this.getFixedBlockLength();
		}

		@Override
		public int getBlockAddress() {
			return FloatingPointMeterModelAccessor.this.getBlockAddress();
		}

		@Override
		public ModelId getModelId() {
			return FloatingPointMeterModelAccessor.this.getModelId();
		}

		@Override
		public int getModelLength() {
			return FloatingPointMeterModelAccessor.this.getModelLength();
		}

		@Override
		public int getRepeatingBlockInstanceLength() {
			return FloatingPointMeterModelAccessor.this.getRepeatingBlockInstanceLength();
		}

		@Override
		public int getRepeatingBlockInstanceCount() {
			return FloatingPointMeterModelAccessor.this.getRepeatingBlockInstanceCount();
		}

		@Override
		public MeterModelAccessor accessorForPhase(AcPhase phase) {
			return FloatingPointMeterModelAccessor.this.accessorForPhase(phase);
		}

		@Override
		public @Nullable Float getFrequency() {
			return FloatingPointMeterModelAccessor.this.getFrequency();
		}

		@Override
		public @Nullable Float getCurrent() {
			return getFloatValue(register(FloatingPointMeterModelRegister.CurrentPhaseA,
					FloatingPointMeterModelRegister.CurrentPhaseB,
					FloatingPointMeterModelRegister.CurrentPhaseC));
		}

		@Override
		public @Nullable Float getVoltage() {
			return getFloatValue(register(FloatingPointMeterModelRegister.VoltagePhaseANeutral,
					FloatingPointMeterModelRegister.VoltagePhaseBNeutral,
					FloatingPointMeterModelRegister.VoltagePhaseCNeutral));
		}

		@Override
		public @Nullable Float getLineVoltage() {
			return getFloatValue(register(FloatingPointMeterModelRegister.VoltagePhaseAPhaseB,
					FloatingPointMeterModelRegister.VoltagePhaseBPhaseC,
					FloatingPointMeterModelRegister.VoltagePhaseCPhaseA));
		}

		@Override
		public @Nullable Float getPowerFactor() {
			return getFloatValue(register(FloatingPointMeterModelRegister.PowerFactorPhaseA,
					FloatingPointMeterModelRegister.PowerFactorPhaseB,
					FloatingPointMeterModelRegister.PowerFactorPhaseC));
		}

		@Override
		public @Nullable BigDecimal getActivePower() {
			return getDecimalValue(register(FloatingPointMeterModelRegister.ActivePowerPhaseA,
					FloatingPointMeterModelRegister.ActivePowerPhaseB,
					FloatingPointMeterModelRegister.ActivePowerPhaseC));
		}

		@Override
		public @Nullable BigDecimal getApparentPower() {
			return getDecimalValue(register(FloatingPointMeterModelRegister.ApparentPowerPhaseA,
					FloatingPointMeterModelRegister.ApparentPowerPhaseB,
					FloatingPointMeterModelRegister.ApparentPowerPhaseC));
		}

		@Override
		public @Nullable BigDecimal getReactivePower() {
			return getDecimalValue(register(FloatingPointMeterModelRegister.ReactivePowerPhaseA,
					FloatingPointMeterModelRegister.ReactivePowerPhaseB,
					FloatingPointMeterModelRegister.ReactivePowerPhaseC));
		}

		@Override
		public @Nullable BigDecimal getActiveEnergyImported() {
			return getDecimalValue(register(FloatingPointMeterModelRegister.ActiveEnergyImportedPhaseA,
					FloatingPointMeterModelRegister.ActiveEnergyImportedPhaseB,
					FloatingPointMeterModelRegister.ActiveEnergyImportedPhaseC));
		}

		@Override
		public @Nullable BigDecimal getActiveEnergyExported() {
			return getDecimalValue(register(FloatingPointMeterModelRegister.ActiveEnergyExportedPhaseA,
					FloatingPointMeterModelRegister.ActiveEnergyExportedPhaseB,
					FloatingPointMeterModelRegister.ActiveEnergyExportedPhaseC));
		}

		@Override
		public @Nullable BigDecimal getReactiveEnergyImported() {
			return sum(getReactiveEnergyImportedQ1(), getReactiveEnergyImportedQ2());
		}

		@Override
		public @Nullable BigDecimal getReactiveEnergyExported() {
			return sum(getReactiveEnergyExportedQ3(), getReactiveEnergyExportedQ4());
		}

		@Override
		public @Nullable BigDecimal getReactiveEnergyImportedQ1() {
			return getDecimalValue(
					register(FloatingPointMeterModelRegister.ReactiveEnergyImportedQ1PhaseA,
							FloatingPointMeterModelRegister.ReactiveEnergyImportedQ1PhaseB,
							FloatingPointMeterModelRegister.ReactiveEnergyImportedQ1PhaseC));
		}

		@Override
		public @Nullable BigDecimal getReactiveEnergyImportedQ2() {
			return getDecimalValue(
					register(FloatingPointMeterModelRegister.ReactiveEnergyImportedQ2PhaseA,
							FloatingPointMeterModelRegister.ReactiveEnergyImportedQ2PhaseB,
							FloatingPointMeterModelRegister.ReactiveEnergyImportedQ2PhaseC));
		}

		@Override
		public @Nullable BigDecimal getReactiveEnergyExportedQ3() {
			return getDecimalValue(
					register(FloatingPointMeterModelRegister.ReactiveEnergyExportedQ3PhaseA,
							FloatingPointMeterModelRegister.ReactiveEnergyExportedQ3PhaseB,
							FloatingPointMeterModelRegister.ReactiveEnergyExportedQ3PhaseC));
		}

		@Override
		public @Nullable BigDecimal getReactiveEnergyExportedQ4() {
			return getDecimalValue(
					register(FloatingPointMeterModelRegister.ReactiveEnergyExportedQ4PhaseA,
							FloatingPointMeterModelRegister.ReactiveEnergyExportedQ4PhaseB,
							FloatingPointMeterModelRegister.ReactiveEnergyExportedQ4PhaseC));
		}

		@Override
		public @Nullable BigDecimal getApparentEnergyImported() {
			return getDecimalValue(register(FloatingPointMeterModelRegister.ApparentEnergyImportedPhaseA,
					FloatingPointMeterModelRegister.ApparentEnergyImportedPhaseB,
					FloatingPointMeterModelRegister.ApparentEnergyImportedPhaseC));
		}

		@Override
		public @Nullable BigDecimal getApparentEnergyExported() {
			return getDecimalValue(register(FloatingPointMeterModelRegister.ApparentEnergyExportedPhaseA,
					FloatingPointMeterModelRegister.ApparentEnergyExportedPhaseB,
					FloatingPointMeterModelRegister.ApparentEnergyExportedPhaseC));
		}

		@Override
		public Set<? extends ModelEvent> getEvents() {
			return FloatingPointMeterModelAccessor.this.getEvents();
		}

		@Override
		public Collection<? extends ModbusReference> getPointReferences() {
			return FloatingPointMeterModelAccessor.this.getPointReferences();
		}

		@Override
		public @Nullable Object getPointValue(ModbusReference point) {
			return FloatingPointMeterModelAccessor.this.getPointValue(point);
		}

	}

}
