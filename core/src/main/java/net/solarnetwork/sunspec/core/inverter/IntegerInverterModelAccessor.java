/* ==================================================================
 * IntegerInverterModelAccessor.java - 5/10/2018 5:13:14 PM
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

package net.solarnetwork.sunspec.core.inverter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.BitSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.AcPhase;
import net.solarnetwork.sunspec.api.IntRange;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.OperatingState;
import net.solarnetwork.sunspec.api.inverter.IntegerInverterModelRegister;
import net.solarnetwork.sunspec.api.inverter.InverterModelAccessor;
import net.solarnetwork.sunspec.api.inverter.InverterModelEvent;
import net.solarnetwork.sunspec.api.inverter.InverterModelId;
import net.solarnetwork.sunspec.api.inverter.InverterOperatingState;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Data access object for an integer inverter model.
 *
 * @author matt
 * @version 1.0
 */
public class IntegerInverterModelAccessor extends BaseModelAccessor implements InverterModelAccessor {

	/** The integer inverter model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 50;

	/**
	 * A metadata key prefix used to hold a {@code Boolean} flag that, when
	 * {@literal true} signifies that the power factor values are encoded as
	 * integer percentage values (from -100...100) rather than the decimal form
	 * of the specification (-1...1).
	 */
	public static final String INTEGER_PF_PCT = "IntPfPct";

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
	public IntegerInverterModelAccessor(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@link InverterModelId} class will be used as the {@code ModelId}
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
	public IntegerInverterModelAccessor(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, InverterModelId.forId(modelId));
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
		Number n = getScaledValue(ref, IntegerInverterModelRegister.ScaleFactorFrequency);
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
		Number n = getScaledValue(ref, IntegerInverterModelRegister.ScaleFactorCurrent);
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
		Number n = getScaledValue(ref, IntegerInverterModelRegister.ScaleFactorVoltage);
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
		Number n = getScaledValue(ref, IntegerInverterModelRegister.ScaleFactorPowerFactor);
		if ( n == null ) {
			return null;
		}
		// check if we've seen this data as integer percentage before
		Object intPct = getData().getMetadataValue(INTEGER_PF_PCT);
		boolean integerForm = false;
		if ( intPct instanceof Boolean b && b ) {
			integerForm = true;
		} else if ( n.intValue() < -1 || n.intValue() > 1 ) {
			// the data looks like it must be an integer percent, not decimal
			getData().putMetadataValue(INTEGER_PF_PCT, true);
			integerForm = true;
		}
		float f = n.floatValue();
		if ( integerForm ) {
			f /= 100.0f;
		}
		return f;
	}

	/**
	 * Get an active power register value.
	 *
	 * @param ref
	 *        the register reference
	 * @return the register value, interpreted as an active power value
	 */
	public @Nullable BigDecimal getActivePowerValue(ModbusReference ref) {
		return getScaledValue(ref, IntegerInverterModelRegister.ScaleFactorActivePower);
	}

	/**
	 * Get an apparent power register value.
	 *
	 * @param ref
	 *        the register reference
	 * @return the register value, interpreted as an apparent power value
	 */
	public @Nullable BigDecimal getApparentPowerValue(ModbusReference ref) {
		return getScaledValue(ref, IntegerInverterModelRegister.ScaleFactorApparentPower);
	}

	/**
	 * Get an reactive power register value.
	 *
	 * @param ref
	 *        the register reference
	 * @return the register value, interpreted as an reactive power value
	 */
	public @Nullable BigDecimal getReactivePowerValue(ModbusReference ref) {
		return getScaledValue(ref, IntegerInverterModelRegister.ScaleFactorReactivePower);
	}

	/**
	 * Get an active energy register value.
	 *
	 * @param ref
	 *        the register reference
	 * @return the register value, interpreted as an active energy value
	 */
	public @Nullable BigDecimal getActiveEnergyValue(ModbusReference ref) {
		return getScaledValue(ref, IntegerInverterModelRegister.ScaleFactorActiveEnergy);
	}

	/**
	 * Get a temperature register value.
	 *
	 * @param ref
	 *        the register reference
	 * @return the register value, interpreted as a temperature value
	 */
	public @Nullable Float getTemperatureValue(ModbusReference ref) {
		Number n = getScaledValue(ref, IntegerInverterModelRegister.ScaleFactorTemperature);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public InverterModelAccessor accessorForPhase(AcPhase phase) {
		if ( phase == AcPhase.Total ) {
			return this;
		}
		return new PhaseInverterModelAccessor(phase);
	}

	@Override
	public @Nullable Float getFrequency() {
		return getFrequencyValue(IntegerInverterModelRegister.Frequency);
	}

	@Override
	public @Nullable Float getCurrent() {
		return getCurrentValue(IntegerInverterModelRegister.CurrentTotal);
	}

	@Override
	public @Nullable Float getVoltage() {
		final int modelId = (getModelId() != null ? getModelId().getId() : -1);
		int count = 0;
		float total = 0;
		Float f = getVoltageValue(IntegerInverterModelRegister.VoltagePhaseANeutral);
		if ( f != null ) {
			total = f.floatValue();
			count++;
		}
		if ( modelId > InverterModelId.SinglePhaseInverterInteger.getId() ) {
			Float f2 = getVoltageValue(IntegerInverterModelRegister.VoltagePhaseBNeutral);
			if ( f2 != null ) {
				total += f2.floatValue();
				count++;
			}
		}
		if ( modelId > InverterModelId.SplitPhaseInverterInteger.getId() ) {
			Float f3 = getVoltageValue(IntegerInverterModelRegister.VoltagePhaseCNeutral);
			if ( f3 != null ) {
				total += f3.floatValue();
				count++;
			}
		}

		return (count > 0 ? total / count : null);
	}

	@Override
	public @Nullable Float getPowerFactor() {
		return getPowerFactorValue(IntegerInverterModelRegister.PowerFactorAverage);
	}

	@Override
	public @Nullable BigDecimal getActivePower() {
		return getActivePowerValue(IntegerInverterModelRegister.ActivePowerTotal);
	}

	@Override
	public @Nullable BigDecimal getApparentPower() {
		return getApparentPowerValue(IntegerInverterModelRegister.ApparentPowerTotal);
	}

	@Override
	public @Nullable BigDecimal getReactivePower() {
		return getReactivePowerValue(IntegerInverterModelRegister.ReactivePowerTotal);
	}

	@Override
	public @Nullable BigDecimal getActiveEnergyExported() {
		return getActiveEnergyValue(IntegerInverterModelRegister.ActiveEnergyExportedTotal);
	}

	@Override
	public @Nullable BigDecimal getActiveEnergyImported() {
		return null;
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyExported() {
		return null;
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyImported() {
		return null;
	}

	@Override
	public @Nullable Float getDcCurrent() {
		Number n = getScaledValue(IntegerInverterModelRegister.DcCurrentTotal,
				IntegerInverterModelRegister.ScaleFactorDcCurrent);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public @Nullable Float getDcVoltage() {
		Number n = getScaledValue(IntegerInverterModelRegister.DcVoltageTotal,
				IntegerInverterModelRegister.ScaleFactorDcVoltage);
		return (n != null ? n.floatValue() : null);
	}

	@Override
	public @Nullable BigDecimal getDcPower() {
		return getScaledValue(IntegerInverterModelRegister.DcPowerTotal,
				IntegerInverterModelRegister.ScaleFactorDcPower);
	}

	@Override
	public @Nullable Float getCabinetTemperature() {
		return getTemperatureValue(IntegerInverterModelRegister.TemperatureCabinet);
	}

	@Override
	public @Nullable Float getHeatSinkTemperature() {
		return getTemperatureValue(IntegerInverterModelRegister.TemperatureHeatSink);
	}

	@Override
	public @Nullable Float getTransformerTemperature() {
		return getTemperatureValue(IntegerInverterModelRegister.TemperatureTransformer);
	}

	@Override
	public @Nullable Float getOtherTemperature() {
		return getTemperatureValue(IntegerInverterModelRegister.TemperatureOther);
	}

	@Override
	public @Nullable OperatingState getOperatingState() {
		Number n = getData().getNumber(IntegerInverterModelRegister.OperatingState, getBlockAddress());
		if ( n == null ) {
			return null;
		}
		return InverterOperatingState.forCode(n.intValue());
	}

	@Override
	public @Nullable Integer getVendorOperatingState() {
		Number n = getData().getNumber(IntegerInverterModelRegister.OperatingStateVendor,
				getBlockAddress());
		return (n != null ? n.intValue() : null);
	}

	@Override
	public Set<? extends ModelEvent> getEvents() {
		Number n = getBitfield(IntegerInverterModelRegister.EventsBitmask);
		return InverterModelEvent.forBitmask(n != null ? n.longValue() : 0L);
	}

	@Override
	public @Nullable BitSet getVendorEvents() {
		BitSet s = getBitfieldBits(IntegerInverterModelRegister.EventsVendorBitmask,
				IntegerInverterModelRegister.Events2VendorBitmask,
				IntegerInverterModelRegister.Events3VendorBitmask,
				IntegerInverterModelRegister.Events4VendorBitmask);
		return (s.length() > 0 ? s : null);
	}

	@Override
	public @Nullable Float getLineVoltage() {
		final int modelId = (getModelId() != null ? getModelId().getId() : -1);
		int count = 0;
		float total = 0;
		Float f = getVoltageValue(IntegerInverterModelRegister.VoltagePhaseAPhaseB);
		if ( f != null ) {
			total = f.floatValue();
			count++;
		}
		if ( modelId > InverterModelId.SinglePhaseInverterInteger.getId() ) {
			Float f2 = getVoltageValue(IntegerInverterModelRegister.VoltagePhaseBPhaseC);
			if ( f2 != null ) {
				total += f2.floatValue();
				count++;
			}
		}
		if ( modelId > InverterModelId.SplitPhaseInverterInteger.getId() ) {
			Float f3 = getVoltageValue(IntegerInverterModelRegister.VoltagePhaseCPhaseA);
			if ( f3 != null ) {
				total += f3.floatValue();
				count++;
			}
		}

		return (count > 0 ? total / count : null);
	}

	private class PhaseInverterModelAccessor implements InverterModelAccessor {

		private final AcPhase phase;

		private PhaseInverterModelAccessor(AcPhase phase) {
			super();
			this.phase = phase;
		}

		@Override
		public IntRange[] getAddressRanges(int maxRangeLength) {
			return IntegerInverterModelAccessor.this.getAddressRanges(maxRangeLength);
		}

		@Override
		public IntRange getAddressRange(int address, int maxRangeLength) {
			return IntegerInverterModelAccessor.this.getAddressRange(address, maxRangeLength);
		}

		@Override
		public List<IntRange> getUnsplittableAddressRanges() {
			return IntegerInverterModelAccessor.this.getUnsplittableAddressRanges();
		}

		@Override
		public @Nullable Instant getDataTimestamp() {
			return IntegerInverterModelAccessor.this.getDataTimestamp();
		}

		@Override
		public int getBaseAddress() {
			return IntegerInverterModelAccessor.this.getBaseAddress();
		}

		@Override
		public int getFixedBlockLength() {
			return IntegerInverterModelAccessor.this.getFixedBlockLength();
		}

		@Override
		public int getBlockAddress() {
			return IntegerInverterModelAccessor.this.getBlockAddress();
		}

		@Override
		public ModelId getModelId() {
			return IntegerInverterModelAccessor.this.getModelId();
		}

		@Override
		public int getModelLength() {
			return IntegerInverterModelAccessor.this.getModelLength();
		}

		@Override
		public int getRepeatingBlockInstanceLength() {
			return IntegerInverterModelAccessor.this.getRepeatingBlockInstanceLength();
		}

		@Override
		public InverterModelAccessor accessorForPhase(AcPhase phase) {
			return IntegerInverterModelAccessor.this.accessorForPhase(phase);
		}

		@Override
		public int getRepeatingBlockInstanceCount() {
			return IntegerInverterModelAccessor.this.getRepeatingBlockInstanceCount();
		}

		@Override
		public @Nullable Float getFrequency() {
			return IntegerInverterModelAccessor.this.getFrequency();
		}

		@Override
		public @Nullable Float getCurrent() {
			return switch (phase) {
				case PhaseA -> getCurrentValue(IntegerInverterModelRegister.CurrentPhaseA);
				case PhaseB -> getCurrentValue(IntegerInverterModelRegister.CurrentPhaseB);
				case PhaseC -> getCurrentValue(IntegerInverterModelRegister.CurrentPhaseC);
				default -> IntegerInverterModelAccessor.this.getCurrent();
			};
		}

		@Override
		public @Nullable Float getVoltage() {
			return switch (phase) {
				case PhaseA -> getVoltageValue(IntegerInverterModelRegister.VoltagePhaseANeutral);
				case PhaseB -> getVoltageValue(IntegerInverterModelRegister.VoltagePhaseBNeutral);
				case PhaseC -> getVoltageValue(IntegerInverterModelRegister.VoltagePhaseCNeutral);
				default -> IntegerInverterModelAccessor.this.getVoltage();
			};
		}

		@Override
		public @Nullable Float getPowerFactor() {
			return switch (phase) {
				case PhaseA, PhaseB, PhaseC -> null;
				default -> IntegerInverterModelAccessor.this.getPowerFactor();
			};
		}

		@Override
		public @Nullable BigDecimal getActivePower() {
			return switch (phase) {
				case PhaseA, PhaseB, PhaseC -> null;
				default -> IntegerInverterModelAccessor.this.getActivePower();
			};
		}

		@Override
		public @Nullable BigDecimal getApparentPower() {
			return switch (phase) {
				case PhaseA, PhaseB, PhaseC -> null;
				default -> IntegerInverterModelAccessor.this.getApparentPower();
			};
		}

		@Override
		public @Nullable BigDecimal getReactivePower() {
			return switch (phase) {
				case PhaseA, PhaseB, PhaseC -> null;
				default -> IntegerInverterModelAccessor.this.getReactivePower();
			};
		}

		@Override
		public @Nullable BigDecimal getActiveEnergyExported() {
			return switch (phase) {
				case PhaseA, PhaseB, PhaseC -> null;
				default -> IntegerInverterModelAccessor.this.getActiveEnergyExported();
			};
		}

		@Override
		public @Nullable BigDecimal getActiveEnergyImported() {
			return switch (phase) {
				case PhaseA, PhaseB, PhaseC -> null;
				default -> IntegerInverterModelAccessor.this.getActiveEnergyImported();
			};
		}

		@Override
		public @Nullable BigDecimal getReactiveEnergyExported() {
			return switch (phase) {
				case PhaseA, PhaseB, PhaseC -> null;
				default -> IntegerInverterModelAccessor.this.getReactiveEnergyExported();
			};
		}

		@Override
		public @Nullable BigDecimal getReactiveEnergyImported() {
			return switch (phase) {
				case PhaseA, PhaseB, PhaseC -> null;
				default -> IntegerInverterModelAccessor.this.getReactiveEnergyImported();
			};
		}

		@Override
		public @Nullable Float getDcCurrent() {
			return switch (phase) {
				case PhaseA, PhaseB, PhaseC -> null;
				default -> IntegerInverterModelAccessor.this.getDcCurrent();
			};
		}

		@Override
		public @Nullable Float getDcVoltage() {
			return switch (phase) {
				case PhaseA, PhaseB, PhaseC -> null;
				default -> IntegerInverterModelAccessor.this.getDcVoltage();
			};
		}

		@Override
		public @Nullable BigDecimal getDcPower() {
			return switch (phase) {
				case PhaseA, PhaseB, PhaseC -> null;
				default -> IntegerInverterModelAccessor.this.getDcPower();
			};
		}

		@Override
		public @Nullable Float getCabinetTemperature() {
			return IntegerInverterModelAccessor.this.getCabinetTemperature();
		}

		@Override
		public @Nullable Float getHeatSinkTemperature() {
			return IntegerInverterModelAccessor.this.getHeatSinkTemperature();
		}

		@Override
		public @Nullable Float getTransformerTemperature() {
			return IntegerInverterModelAccessor.this.getTransformerTemperature();
		}

		@Override
		public @Nullable Float getOtherTemperature() {
			return IntegerInverterModelAccessor.this.getOtherTemperature();
		}

		@Override
		public @Nullable OperatingState getOperatingState() {
			return IntegerInverterModelAccessor.this.getOperatingState();
		}

		@Override
		public @Nullable Integer getVendorOperatingState() {
			return IntegerInverterModelAccessor.this.getVendorOperatingState();
		}

		@Override
		public Set<? extends ModelEvent> getEvents() {
			return IntegerInverterModelAccessor.this.getEvents();
		}

		@Override
		public @Nullable BitSet getVendorEvents() {
			return IntegerInverterModelAccessor.this.getVendorEvents();
		}

		@Override
		public @Nullable Float getLineVoltage() {
			return switch (phase) {
				case PhaseA -> getVoltageValue(IntegerInverterModelRegister.VoltagePhaseAPhaseB);
				case PhaseB -> getVoltageValue(IntegerInverterModelRegister.VoltagePhaseBPhaseC);
				case PhaseC -> getVoltageValue(IntegerInverterModelRegister.VoltagePhaseCPhaseA);
				default -> IntegerInverterModelAccessor.this.getLineVoltage();
			};
		}

	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(IntegerInverterModelRegister.class);
	}

}
