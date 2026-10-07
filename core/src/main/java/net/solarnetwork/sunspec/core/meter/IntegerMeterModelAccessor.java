/* ==================================================================
 * IntegerMeterModelAccessor.java - 22/05/2018 6:31:57 AM
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
import net.solarnetwork.sunspec.api.meter.IntegerMeterModelRegister;
import net.solarnetwork.sunspec.api.meter.MeterModelAccessor;
import net.solarnetwork.sunspec.api.meter.MeterModelEvent;
import net.solarnetwork.sunspec.api.meter.MeterModelId;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Data object for an integer meter model.
 *
 * @author matt
 * @version 1.0
 */
public class IntegerMeterModelAccessor extends BaseModelAccessor implements MeterModelAccessor {

	/** The integer meter model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 105;

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
	public IntegerMeterModelAccessor(ModelData data, int baseAddress, ModelId modelId) {
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
	public IntegerMeterModelAccessor(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, MeterModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	/**
	 * Get a frequency register value.
	 *
	 * @param ref
	 *        the register reference to read
	 * @return the register value, interpreted as a frequency value
	 */
	public @Nullable Float getFrequencyValue(ModbusReference ref) {
		Number n = getScaledValue(ref, IntegerMeterModelRegister.ScaleFactorFrequency);
		return (n != null ? n.floatValue() : null);
	}

	/**
	 * Get a current register value.
	 *
	 * @param ref
	 *        the register reference to read
	 * @return the register value, interpreted as a current value
	 */
	public @Nullable Float getCurrentValue(ModbusReference ref) {
		Number n = getScaledValue(ref, IntegerMeterModelRegister.ScaleFactorCurrent);
		return (n != null ? n.floatValue() : null);
	}

	/**
	 * Get a voltage register value.
	 *
	 * @param ref
	 *        the register reference to read
	 * @return the register value, interpreted as a voltage value
	 */
	public @Nullable Float getVoltageValue(ModbusReference ref) {
		Number n = getScaledValue(ref, IntegerMeterModelRegister.ScaleFactorVoltage);
		return (n != null ? n.floatValue() : null);
	}

	/**
	 * Get a power factor register value.
	 *
	 * @param ref
	 *        the register reference to read
	 * @return the register value, interpreted as a power factor value
	 */
	public @Nullable Float getPowerFactorValue(ModbusReference ref) {
		Number n = getScaledValue(ref, IntegerMeterModelRegister.ScaleFactorPowerFactor);
		return (n != null ? n.floatValue() : null);
	}

	/**
	 * Get an active power register value.
	 *
	 * @param ref
	 *        the register reference to read
	 * @return the register value, interpreted as an active power value
	 */
	public @Nullable BigDecimal getActivePowerValue(ModbusReference ref) {
		return getScaledValue(ref, IntegerMeterModelRegister.ScaleFactorActivePower);
	}

	/**
	 * Get an apparent power register value.
	 *
	 * @param ref
	 *        the register reference to read
	 * @return the register value, interpreted as an apparent power value
	 */
	public @Nullable BigDecimal getApparentPowerValue(ModbusReference ref) {
		return getScaledValue(ref, IntegerMeterModelRegister.ScaleFactorApparentPower);
	}

	/**
	 * Get an reactive power register value.
	 *
	 * @param ref
	 *        the register reference to read
	 * @return the register value, interpreted as an reactive power value
	 */
	public @Nullable BigDecimal getReactivePowerValue(ModbusReference ref) {
		return getScaledValue(ref, IntegerMeterModelRegister.ScaleFactorReactivePower);
	}

	/**
	 * Get an active energy register value.
	 *
	 * @param ref
	 *        the register reference to read
	 * @return the register value, interpreted as an active energy value
	 */
	public @Nullable BigDecimal getActiveEnergyValue(ModbusReference ref) {
		return getScaledValue(ref, IntegerMeterModelRegister.ScaleFactorActiveEnergy);
	}

	/**
	 * Get an apparent energy register value.
	 *
	 * @param ref
	 *        the register reference to read
	 * @return the register value, interpreted as an apparent energy value
	 */
	public @Nullable BigDecimal getApparentEnergyValue(ModbusReference ref) {
		return getScaledValue(ref, IntegerMeterModelRegister.ScaleFactorApparentEnergy);
	}

	/**
	 * Get an reactive energy register value.
	 *
	 * @param ref
	 *        the register reference to read
	 * @return the register value, interpreted as an reactive energy value
	 */
	public @Nullable BigDecimal getReactiveEnergyValue(ModbusReference ref) {
		return getScaledValue(ref, IntegerMeterModelRegister.ScaleFactorReactiveEnergy);
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
		return getFrequencyValue(IntegerMeterModelRegister.Frequency);
	}

	@Override
	public @Nullable Float getCurrent() {
		return getCurrentValue(IntegerMeterModelRegister.CurrentTotal);
	}

	@Override
	public @Nullable Float getVoltage() {
		return getVoltageValue(IntegerMeterModelRegister.VoltageLineNeutralAverage);
	}

	@Override
	public @Nullable Float getLineVoltage() {
		return getVoltageValue(IntegerMeterModelRegister.VoltageLineLineAverage);
	}

	@Override
	public @Nullable Float getPowerFactor() {
		return getPowerFactorValue(IntegerMeterModelRegister.PowerFactorAverage);
	}

	@Override
	public @Nullable BigDecimal getActivePower() {
		return getActivePowerValue(IntegerMeterModelRegister.ActivePowerTotal);
	}

	@Override
	public @Nullable BigDecimal getApparentPower() {
		return getApparentPowerValue(IntegerMeterModelRegister.ApparentPowerTotal);
	}

	@Override
	public @Nullable BigDecimal getReactivePower() {
		return getReactivePowerValue(IntegerMeterModelRegister.ReactivePowerTotal);
	}

	@Override
	public @Nullable BigDecimal getActiveEnergyImported() {
		return getActiveEnergyValue(IntegerMeterModelRegister.ActiveEnergyImportedTotal);
	}

	@Override
	public @Nullable BigDecimal getActiveEnergyExported() {
		return getActiveEnergyValue(IntegerMeterModelRegister.ActiveEnergyExportedTotal);
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
		return getReactiveEnergyValue(IntegerMeterModelRegister.ReactiveEnergyImportedQ1Total);
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyImportedQ2() {
		return getReactiveEnergyValue(IntegerMeterModelRegister.ReactiveEnergyImportedQ2Total);
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyExportedQ3() {
		return getReactiveEnergyValue(IntegerMeterModelRegister.ReactiveEnergyExportedQ3Total);
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyExportedQ4() {
		return getReactiveEnergyValue(IntegerMeterModelRegister.ReactiveEnergyExportedQ4Total);
	}

	/**
	 * Add two reactive energy quadrant values.
	 *
	 * @param a
	 *        the first quadrant value
	 * @param b
	 *        the second quadrant value
	 * @return the sum, or {@code null} if neither quadrant value is available
	 */
	private static @Nullable BigDecimal sum(@Nullable BigDecimal a, @Nullable BigDecimal b) {
		if ( a == null ) {
			return b;
		}
		return (b != null ? a.add(b) : a);
	}

	@Override
	public @Nullable BigDecimal getApparentEnergyImported() {
		return getApparentEnergyValue(IntegerMeterModelRegister.ApparentEnergyImportedTotal);
	}

	@Override
	public @Nullable BigDecimal getApparentEnergyExported() {
		return getApparentEnergyValue(IntegerMeterModelRegister.ApparentEnergyExportedTotal);
	}

	@Override
	public Set<? extends ModelEvent> getEvents() {
		Number n = getBitfield(IntegerMeterModelRegister.EventsBitmask);
		return MeterModelEvent.forBitmask(n != null ? n.longValue() : 0L);
	}

	private class PhaseMeterModelAccessor implements MeterModelAccessor {

		private final AcPhase phase;

		private PhaseMeterModelAccessor(AcPhase phase) {
			super();
			this.phase = phase;
		}

		@Override
		public IntRange[] getAddressRanges(int maxRangeLength) {
			return IntegerMeterModelAccessor.this.getAddressRanges(maxRangeLength);
		}

		@Override
		public IntRange getAddressRange(int address, int maxRangeLength) {
			return IntegerMeterModelAccessor.this.getAddressRange(address, maxRangeLength);
		}

		@Override
		public List<IntRange> getUnsplittableAddressRanges() {
			return IntegerMeterModelAccessor.this.getUnsplittableAddressRanges();
		}

		@Override
		public @Nullable Instant getDataTimestamp() {
			return IntegerMeterModelAccessor.this.getDataTimestamp();
		}

		@Override
		public int getBaseAddress() {
			return IntegerMeterModelAccessor.this.getBaseAddress();
		}

		@Override
		public int getFixedBlockLength() {
			return IntegerMeterModelAccessor.this.getFixedBlockLength();
		}

		@Override
		public int getBlockAddress() {
			return IntegerMeterModelAccessor.this.getBlockAddress();
		}

		@Override
		public ModelId getModelId() {
			return IntegerMeterModelAccessor.this.getModelId();
		}

		@Override
		public int getModelLength() {
			return IntegerMeterModelAccessor.this.getModelLength();
		}

		@Override
		public int getRepeatingBlockInstanceLength() {
			return IntegerMeterModelAccessor.this.getRepeatingBlockInstanceLength();
		}

		@Override
		public MeterModelAccessor accessorForPhase(AcPhase phase) {
			return IntegerMeterModelAccessor.this.accessorForPhase(phase);
		}

		@Override
		public int getRepeatingBlockInstanceCount() {
			return IntegerMeterModelAccessor.this.getRepeatingBlockInstanceCount();
		}

		@Override
		public @Nullable Float getFrequency() {
			return IntegerMeterModelAccessor.this.getFrequency();
		}

		@Override
		public @Nullable Float getCurrent() {
			return switch (phase) {
				case PhaseA -> getCurrentValue(IntegerMeterModelRegister.CurrentPhaseA);
				case PhaseB -> getCurrentValue(IntegerMeterModelRegister.CurrentPhaseB);
				case PhaseC -> getCurrentValue(IntegerMeterModelRegister.CurrentPhaseC);
				default -> IntegerMeterModelAccessor.this.getCurrent();
			};
		}

		@Override
		public @Nullable Float getVoltage() {
			return switch (phase) {
				case PhaseA -> getVoltageValue(IntegerMeterModelRegister.VoltagePhaseANeutral);
				case PhaseB -> getVoltageValue(IntegerMeterModelRegister.VoltagePhaseBNeutral);
				case PhaseC -> getVoltageValue(IntegerMeterModelRegister.VoltagePhaseCNeutral);
				default -> IntegerMeterModelAccessor.this.getVoltage();
			};
		}

		@Override
		public @Nullable Float getLineVoltage() {
			return switch (phase) {
				case PhaseA -> getVoltageValue(IntegerMeterModelRegister.VoltagePhaseAPhaseB);
				case PhaseB -> getVoltageValue(IntegerMeterModelRegister.VoltagePhaseBPhaseC);
				case PhaseC -> getVoltageValue(IntegerMeterModelRegister.VoltagePhaseCPhaseA);
				default -> IntegerMeterModelAccessor.this.getLineVoltage();
			};
		}

		@Override
		public @Nullable Float getPowerFactor() {
			return switch (phase) {
				case PhaseA -> getPowerFactorValue(IntegerMeterModelRegister.PowerFactorPhaseA);
				case PhaseB -> getPowerFactorValue(IntegerMeterModelRegister.PowerFactorPhaseB);
				case PhaseC -> getPowerFactorValue(IntegerMeterModelRegister.PowerFactorPhaseC);
				default -> IntegerMeterModelAccessor.this.getPowerFactor();
			};
		}

		@Override
		public @Nullable BigDecimal getActivePower() {
			return switch (phase) {
				case PhaseA -> getActivePowerValue(IntegerMeterModelRegister.ActivePowerPhaseA);
				case PhaseB -> getActivePowerValue(IntegerMeterModelRegister.ActivePowerPhaseB);
				case PhaseC -> getActivePowerValue(IntegerMeterModelRegister.ActivePowerPhaseC);
				default -> IntegerMeterModelAccessor.this.getActivePower();
			};
		}

		@Override
		public @Nullable BigDecimal getApparentPower() {
			return switch (phase) {
				case PhaseA -> getApparentPowerValue(IntegerMeterModelRegister.ApparentPowerPhaseA);
				case PhaseB -> getApparentPowerValue(IntegerMeterModelRegister.ApparentPowerPhaseB);
				case PhaseC -> getApparentPowerValue(IntegerMeterModelRegister.ApparentPowerPhaseC);
				default -> IntegerMeterModelAccessor.this.getApparentPower();
			};
		}

		@Override
		public @Nullable BigDecimal getReactivePower() {
			return switch (phase) {
				case PhaseA -> getReactivePowerValue(IntegerMeterModelRegister.ReactivePowerPhaseA);
				case PhaseB -> getReactivePowerValue(IntegerMeterModelRegister.ReactivePowerPhaseB);
				case PhaseC -> getReactivePowerValue(IntegerMeterModelRegister.ReactivePowerPhaseC);
				default -> IntegerMeterModelAccessor.this.getReactivePower();
			};
		}

		@Override
		public @Nullable BigDecimal getActiveEnergyImported() {
			return switch (phase) {
				case PhaseA -> getActiveEnergyValue(
						IntegerMeterModelRegister.ActiveEnergyImportedPhaseA);
				case PhaseB -> getActiveEnergyValue(
						IntegerMeterModelRegister.ActiveEnergyImportedPhaseB);
				case PhaseC -> getActiveEnergyValue(
						IntegerMeterModelRegister.ActiveEnergyImportedPhaseC);
				default -> IntegerMeterModelAccessor.this.getActiveEnergyImported();
			};
		}

		@Override
		public @Nullable BigDecimal getActiveEnergyExported() {
			return switch (phase) {
				case PhaseA -> getActiveEnergyValue(
						IntegerMeterModelRegister.ActiveEnergyExportedPhaseA);
				case PhaseB -> getActiveEnergyValue(
						IntegerMeterModelRegister.ActiveEnergyExportedPhaseB);
				case PhaseC -> getActiveEnergyValue(
						IntegerMeterModelRegister.ActiveEnergyExportedPhaseC);
				default -> IntegerMeterModelAccessor.this.getActiveEnergyExported();
			};
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
			return switch (phase) {
				case PhaseA -> getReactiveEnergyValue(
						IntegerMeterModelRegister.ReactiveEnergyImportedQ1PhaseA);
				case PhaseB -> getReactiveEnergyValue(
						IntegerMeterModelRegister.ReactiveEnergyImportedQ1PhaseB);
				case PhaseC -> getReactiveEnergyValue(
						IntegerMeterModelRegister.ReactiveEnergyImportedQ1PhaseC);
				default -> IntegerMeterModelAccessor.this.getReactiveEnergyImportedQ1();
			};
		}

		@Override
		public @Nullable BigDecimal getReactiveEnergyImportedQ2() {
			return switch (phase) {
				case PhaseA -> getReactiveEnergyValue(
						IntegerMeterModelRegister.ReactiveEnergyImportedQ2PhaseA);
				case PhaseB -> getReactiveEnergyValue(
						IntegerMeterModelRegister.ReactiveEnergyImportedQ2PhaseB);
				case PhaseC -> getReactiveEnergyValue(
						IntegerMeterModelRegister.ReactiveEnergyImportedQ2PhaseC);
				default -> IntegerMeterModelAccessor.this.getReactiveEnergyImportedQ2();
			};
		}

		@Override
		public @Nullable BigDecimal getReactiveEnergyExportedQ3() {
			return switch (phase) {
				case PhaseA -> getReactiveEnergyValue(
						IntegerMeterModelRegister.ReactiveEnergyExportedQ3PhaseA);
				case PhaseB -> getReactiveEnergyValue(
						IntegerMeterModelRegister.ReactiveEnergyExportedQ3PhaseB);
				case PhaseC -> getReactiveEnergyValue(
						IntegerMeterModelRegister.ReactiveEnergyExportedQ3PhaseC);
				default -> IntegerMeterModelAccessor.this.getReactiveEnergyExportedQ3();
			};
		}

		@Override
		public @Nullable BigDecimal getReactiveEnergyExportedQ4() {
			return switch (phase) {
				case PhaseA -> getReactiveEnergyValue(
						IntegerMeterModelRegister.ReactiveEnergyExportedQ4PhaseA);
				case PhaseB -> getReactiveEnergyValue(
						IntegerMeterModelRegister.ReactiveEnergyExportedQ4PhaseB);
				case PhaseC -> getReactiveEnergyValue(
						IntegerMeterModelRegister.ReactiveEnergyExportedQ4PhaseC);
				default -> IntegerMeterModelAccessor.this.getReactiveEnergyExportedQ4();
			};
		}

		@Override
		public @Nullable BigDecimal getApparentEnergyImported() {
			return switch (phase) {
				case PhaseA -> getApparentEnergyValue(
						IntegerMeterModelRegister.ApparentEnergyImportedPhaseA);
				case PhaseB -> getApparentEnergyValue(
						IntegerMeterModelRegister.ApparentEnergyImportedPhaseB);
				case PhaseC -> getApparentEnergyValue(
						IntegerMeterModelRegister.ApparentEnergyImportedPhaseC);
				default -> IntegerMeterModelAccessor.this.getApparentEnergyImported();
			};
		}

		@Override
		public @Nullable BigDecimal getApparentEnergyExported() {
			return switch (phase) {
				case PhaseA -> getApparentEnergyValue(
						IntegerMeterModelRegister.ApparentEnergyExportedPhaseA);
				case PhaseB -> getApparentEnergyValue(
						IntegerMeterModelRegister.ApparentEnergyExportedPhaseB);
				case PhaseC -> getApparentEnergyValue(
						IntegerMeterModelRegister.ApparentEnergyExportedPhaseC);
				default -> IntegerMeterModelAccessor.this.getApparentEnergyExported();
			};
		}

		@Override
		public Set<? extends ModelEvent> getEvents() {
			return IntegerMeterModelAccessor.this.getEvents();
		}

		@Override
		public Collection<? extends ModbusReference> getPointReferences() {
			return IntegerMeterModelAccessor.this.getPointReferences();
		}

		@Override
		public @Nullable Object getPointValue(ModbusReference point) {
			return IntegerMeterModelAccessor.this.getPointValue(point);
		}

	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(IntegerMeterModelRegister.class);
	}

}
