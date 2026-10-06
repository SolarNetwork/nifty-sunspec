/* ==================================================================
 * DerAcMeasurementModelAccessorImpl.java - 5/10/2026 8:27:22 am
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

package net.solarnetwork.sunspec.core.der;

import java.time.Instant;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.domain.AcPhase;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.OperatingState;
import net.solarnetwork.sunspec.api.der.DerAcMeasurementModelAccessor;
import net.solarnetwork.sunspec.api.der.DerAcMeasurementModelRegister;
import net.solarnetwork.sunspec.api.der.DerAcWiringType;
import net.solarnetwork.sunspec.api.der.DerAlarm;
import net.solarnetwork.sunspec.api.der.DerGridConnectionState;
import net.solarnetwork.sunspec.api.der.DerInverterState;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerOperatingState;
import net.solarnetwork.sunspec.api.der.DerOperationalCharacteristic;
import net.solarnetwork.sunspec.api.der.DerThrottleSource;
import net.solarnetwork.sunspec.api.inverter.InverterModelAccessor;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link DerAcMeasurementModelAccessor}.
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public class DerAcMeasurementModelAccessorImpl extends BaseModelAccessor
		implements DerAcMeasurementModelAccessor {

	/** The DER AC measurement model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 153;

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
	public DerAcMeasurementModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@link DerModelId} class will be used as the {@code ModelId}
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
	public DerAcMeasurementModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, DerModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.allOf(DerAcMeasurementModelRegister.class);
	}

	@Override
	public InverterModelAccessor accessorForPhase(AcPhase phase) {
		return switch (phase) {
			case PhaseA, PhaseB, PhaseC -> new PhaseAccessor(phase);
			default -> this;
		};
	}

	@Override
	public @Nullable DerAcWiringType getAcWiringType() {
		return getCodedValue(DerAcMeasurementModelRegister.AcWiringType, DerAcWiringType.class);
	}

	@Override
	public @Nullable DerOperatingState getDerOperatingState() {
		return getCodedValue(DerAcMeasurementModelRegister.OperatingState, DerOperatingState.class);
	}

	@Override
	public @Nullable DerInverterState getInverterState() {
		return getCodedValue(DerAcMeasurementModelRegister.InverterState, DerInverterState.class);
	}

	@Override
	public @Nullable OperatingState getOperatingState() {
		DerInverterState state = getInverterState();
		return (state != null ? state.asInverterOperatingState() : null);
	}

	@Override
	public @Nullable DerGridConnectionState getGridConnectionState() {
		return getCodedValue(DerAcMeasurementModelRegister.GridConnectionState,
				DerGridConnectionState.class);
	}

	@Override
	public Set<? extends ModelEvent> getEvents() {
		Number n = getBitfield(DerAcMeasurementModelRegister.AlarmsBitmask);
		return DerAlarm.forBitmask(n != null ? n.longValue() : 0L);
	}

	@Override
	public Set<DerOperationalCharacteristic> getOperationalCharacteristics() {
		return getBitmaskableValues(DerAcMeasurementModelRegister.OperationalCharacteristicsBitmask,
				DerOperationalCharacteristic.class);
	}

	@Override
	public @Nullable Integer getActivePower() {
		return getScaledIntegerValue(DerAcMeasurementModelRegister.ActivePowerTotal,
				DerAcMeasurementModelRegister.ScaleFactorActivePower);
	}

	@Override
	public @Nullable Integer getApparentPower() {
		return getScaledIntegerValue(DerAcMeasurementModelRegister.ApparentPowerTotal,
				DerAcMeasurementModelRegister.ScaleFactorApparentPower);
	}

	@Override
	public @Nullable Integer getReactivePower() {
		return getScaledIntegerValue(DerAcMeasurementModelRegister.ReactivePowerTotal,
				DerAcMeasurementModelRegister.ScaleFactorReactivePower);
	}

	@Override
	public @Nullable Float getPowerFactor() {
		return getScaledFloatValue(DerAcMeasurementModelRegister.PowerFactorTotal,
				DerAcMeasurementModelRegister.ScaleFactorPowerFactor);
	}

	@Override
	public @Nullable Float getCurrent() {
		return getScaledFloatValue(DerAcMeasurementModelRegister.CurrentTotal,
				DerAcMeasurementModelRegister.ScaleFactorCurrent);
	}

	@Override
	public @Nullable Float getVoltage() {
		return getScaledFloatValue(DerAcMeasurementModelRegister.VoltageLineNeutralAverage,
				DerAcMeasurementModelRegister.ScaleFactorVoltage);
	}

	@Override
	public @Nullable Float getLineVoltage() {
		return getScaledFloatValue(DerAcMeasurementModelRegister.VoltageLineLineAverage,
				DerAcMeasurementModelRegister.ScaleFactorVoltage);
	}

	@Override
	public @Nullable Float getFrequency() {
		return getScaledFloatValue(DerAcMeasurementModelRegister.Frequency,
				DerAcMeasurementModelRegister.ScaleFactorFrequency);
	}

	@Override
	public @Nullable Long getActiveEnergyExported() {
		return getScaledLongValue(DerAcMeasurementModelRegister.ActiveEnergyInjectedTotal,
				DerAcMeasurementModelRegister.ScaleFactorActiveEnergy);
	}

	@Override
	public @Nullable Long getActiveEnergyImported() {
		return getScaledLongValue(DerAcMeasurementModelRegister.ActiveEnergyAbsorbedTotal,
				DerAcMeasurementModelRegister.ScaleFactorActiveEnergy);
	}

	@Override
	public @Nullable Long getReactiveEnergyExported() {
		return getScaledLongValue(DerAcMeasurementModelRegister.ReactiveEnergyInjectedTotal,
				DerAcMeasurementModelRegister.ScaleFactorReactiveEnergy);
	}

	@Override
	public @Nullable Long getReactiveEnergyImported() {
		return getScaledLongValue(DerAcMeasurementModelRegister.ReactiveEnergyAbsorbedTotal,
				DerAcMeasurementModelRegister.ScaleFactorReactiveEnergy);
	}

	@Override
	public @Nullable Float getDcCurrent() {
		return null;
	}

	@Override
	public @Nullable Float getDcVoltage() {
		return null;
	}

	@Override
	public @Nullable Integer getDcPower() {
		return null;
	}

	@Override
	public @Nullable Float getAmbientTemperature() {
		return getScaledFloatValue(DerAcMeasurementModelRegister.TemperatureAmbient,
				DerAcMeasurementModelRegister.ScaleFactorTemperature);
	}

	@Override
	public @Nullable Float getCabinetTemperature() {
		return getScaledFloatValue(DerAcMeasurementModelRegister.TemperatureCabinet,
				DerAcMeasurementModelRegister.ScaleFactorTemperature);
	}

	@Override
	public @Nullable Float getHeatSinkTemperature() {
		return getScaledFloatValue(DerAcMeasurementModelRegister.TemperatureHeatSink,
				DerAcMeasurementModelRegister.ScaleFactorTemperature);
	}

	@Override
	public @Nullable Float getTransformerTemperature() {
		return getScaledFloatValue(DerAcMeasurementModelRegister.TemperatureTransformer,
				DerAcMeasurementModelRegister.ScaleFactorTemperature);
	}

	@Override
	public @Nullable Float getSwitchTemperature() {
		return getScaledFloatValue(DerAcMeasurementModelRegister.TemperatureSwitch,
				DerAcMeasurementModelRegister.ScaleFactorTemperature);
	}

	@Override
	public @Nullable Float getOtherTemperature() {
		return getScaledFloatValue(DerAcMeasurementModelRegister.TemperatureOther,
				DerAcMeasurementModelRegister.ScaleFactorTemperature);
	}

	@Override
	public @Nullable Integer getThrottlePercent() {
		return getIntegerValue(DerAcMeasurementModelRegister.ThrottlePercent);
	}

	@Override
	public Set<DerThrottleSource> getThrottleSources() {
		return getBitmaskableValues(DerAcMeasurementModelRegister.ThrottleSourcesBitmask,
				DerThrottleSource.class);
	}

	@Override
	public @Nullable String getManufacturerAlarmInfo() {
		return getStringValue(DerAcMeasurementModelRegister.ManufacturerAlarmInfo);
	}

	/**
	 * Phase-specific accessor.
	 */
	private class PhaseAccessor implements InverterModelAccessor {

		private final DerAcMeasurementModelRegister activePower;
		private final DerAcMeasurementModelRegister apparentPower;
		private final DerAcMeasurementModelRegister reactivePower;
		private final DerAcMeasurementModelRegister powerFactor;
		private final DerAcMeasurementModelRegister current;
		private final DerAcMeasurementModelRegister lineVoltage;
		private final DerAcMeasurementModelRegister voltage;
		private final DerAcMeasurementModelRegister activeEnergyInjected;
		private final DerAcMeasurementModelRegister activeEnergyAbsorbed;
		private final DerAcMeasurementModelRegister reactiveEnergyInjected;
		private final DerAcMeasurementModelRegister reactiveEnergyAbsorbed;

		private PhaseAccessor(AcPhase phase) {
			super();
			switch (phase) {
				case PhaseA -> {
					activePower = DerAcMeasurementModelRegister.ActivePowerPhaseA;
					apparentPower = DerAcMeasurementModelRegister.ApparentPowerPhaseA;
					reactivePower = DerAcMeasurementModelRegister.ReactivePowerPhaseA;
					powerFactor = DerAcMeasurementModelRegister.PowerFactorPhaseA;
					current = DerAcMeasurementModelRegister.CurrentPhaseA;
					lineVoltage = DerAcMeasurementModelRegister.VoltagePhaseAPhaseB;
					voltage = DerAcMeasurementModelRegister.VoltagePhaseANeutral;
					activeEnergyInjected = DerAcMeasurementModelRegister.ActiveEnergyInjectedPhaseA;
					activeEnergyAbsorbed = DerAcMeasurementModelRegister.ActiveEnergyAbsorbedPhaseA;
					reactiveEnergyInjected = DerAcMeasurementModelRegister.ReactiveEnergyInjectedPhaseA;
					reactiveEnergyAbsorbed = DerAcMeasurementModelRegister.ReactiveEnergyAbsorbedPhaseA;
				}
				case PhaseB -> {
					activePower = DerAcMeasurementModelRegister.ActivePowerPhaseB;
					apparentPower = DerAcMeasurementModelRegister.ApparentPowerPhaseB;
					reactivePower = DerAcMeasurementModelRegister.ReactivePowerPhaseB;
					powerFactor = DerAcMeasurementModelRegister.PowerFactorPhaseB;
					current = DerAcMeasurementModelRegister.CurrentPhaseB;
					lineVoltage = DerAcMeasurementModelRegister.VoltagePhaseBPhaseC;
					voltage = DerAcMeasurementModelRegister.VoltagePhaseBNeutral;
					activeEnergyInjected = DerAcMeasurementModelRegister.ActiveEnergyInjectedPhaseB;
					activeEnergyAbsorbed = DerAcMeasurementModelRegister.ActiveEnergyAbsorbedPhaseB;
					reactiveEnergyInjected = DerAcMeasurementModelRegister.ReactiveEnergyInjectedPhaseB;
					reactiveEnergyAbsorbed = DerAcMeasurementModelRegister.ReactiveEnergyAbsorbedPhaseB;
				}
				default -> {
					activePower = DerAcMeasurementModelRegister.ActivePowerPhaseC;
					apparentPower = DerAcMeasurementModelRegister.ApparentPowerPhaseC;
					reactivePower = DerAcMeasurementModelRegister.ReactivePowerPhaseC;
					powerFactor = DerAcMeasurementModelRegister.PowerFactorPhaseC;
					current = DerAcMeasurementModelRegister.CurrentPhaseC;
					lineVoltage = DerAcMeasurementModelRegister.VoltagePhaseCPhaseA;
					voltage = DerAcMeasurementModelRegister.VoltagePhaseCNeutral;
					activeEnergyInjected = DerAcMeasurementModelRegister.ActiveEnergyInjectedPhaseC;
					activeEnergyAbsorbed = DerAcMeasurementModelRegister.ActiveEnergyAbsorbedPhaseC;
					reactiveEnergyInjected = DerAcMeasurementModelRegister.ReactiveEnergyInjectedPhaseC;
					reactiveEnergyAbsorbed = DerAcMeasurementModelRegister.ReactiveEnergyAbsorbedPhaseC;
				}
			}
		}

		@Override
		public @Nullable Instant getDataTimestamp() {
			return DerAcMeasurementModelAccessorImpl.this.getDataTimestamp();
		}

		@Override
		public int getBaseAddress() {
			return DerAcMeasurementModelAccessorImpl.this.getBaseAddress();
		}

		@Override
		public int getBlockAddress() {
			return DerAcMeasurementModelAccessorImpl.this.getBlockAddress();
		}

		@Override
		public ModelId getModelId() {
			return DerAcMeasurementModelAccessorImpl.this.getModelId();
		}

		@Override
		public int getFixedBlockLength() {
			return DerAcMeasurementModelAccessorImpl.this.getFixedBlockLength();
		}

		@Override
		public int getModelLength() {
			return DerAcMeasurementModelAccessorImpl.this.getModelLength();
		}

		@Override
		public InverterModelAccessor accessorForPhase(AcPhase phase) {
			return DerAcMeasurementModelAccessorImpl.this.accessorForPhase(phase);
		}

		@Override
		public @Nullable Integer getActivePower() {
			return getScaledIntegerValue(activePower,
					DerAcMeasurementModelRegister.ScaleFactorActivePower);
		}

		@Override
		public @Nullable Integer getApparentPower() {
			return getScaledIntegerValue(apparentPower,
					DerAcMeasurementModelRegister.ScaleFactorApparentPower);
		}

		@Override
		public @Nullable Integer getReactivePower() {
			return getScaledIntegerValue(reactivePower,
					DerAcMeasurementModelRegister.ScaleFactorReactivePower);
		}

		@Override
		public @Nullable Float getPowerFactor() {
			return getScaledFloatValue(powerFactor,
					DerAcMeasurementModelRegister.ScaleFactorPowerFactor);
		}

		@Override
		public @Nullable Float getCurrent() {
			return getScaledFloatValue(current, DerAcMeasurementModelRegister.ScaleFactorCurrent);
		}

		@Override
		public @Nullable Float getVoltage() {
			return getScaledFloatValue(voltage, DerAcMeasurementModelRegister.ScaleFactorVoltage);
		}

		@Override
		public @Nullable Float getLineVoltage() {
			return getScaledFloatValue(lineVoltage, DerAcMeasurementModelRegister.ScaleFactorVoltage);
		}

		@Override
		public @Nullable Float getFrequency() {
			return DerAcMeasurementModelAccessorImpl.this.getFrequency();
		}

		@Override
		public @Nullable Long getActiveEnergyExported() {
			return getScaledLongValue(activeEnergyInjected,
					DerAcMeasurementModelRegister.ScaleFactorActiveEnergy);
		}

		@Override
		public @Nullable Long getActiveEnergyImported() {
			return getScaledLongValue(activeEnergyAbsorbed,
					DerAcMeasurementModelRegister.ScaleFactorActiveEnergy);
		}

		@Override
		public @Nullable Long getReactiveEnergyExported() {
			return getScaledLongValue(reactiveEnergyInjected,
					DerAcMeasurementModelRegister.ScaleFactorReactiveEnergy);
		}

		@Override
		public @Nullable Long getReactiveEnergyImported() {
			return getScaledLongValue(reactiveEnergyAbsorbed,
					DerAcMeasurementModelRegister.ScaleFactorReactiveEnergy);
		}

		@Override
		public @Nullable Float getDcCurrent() {
			return null;
		}

		@Override
		public @Nullable Float getDcVoltage() {
			return null;
		}

		@Override
		public @Nullable Integer getDcPower() {
			return null;
		}

		@Override
		public @Nullable Float getCabinetTemperature() {
			return DerAcMeasurementModelAccessorImpl.this.getCabinetTemperature();
		}

		@Override
		public @Nullable Float getHeatSinkTemperature() {
			return DerAcMeasurementModelAccessorImpl.this.getHeatSinkTemperature();
		}

		@Override
		public @Nullable Float getTransformerTemperature() {
			return DerAcMeasurementModelAccessorImpl.this.getTransformerTemperature();
		}

		@Override
		public @Nullable Float getOtherTemperature() {
			return DerAcMeasurementModelAccessorImpl.this.getOtherTemperature();
		}

		@Override
		public @Nullable OperatingState getOperatingState() {
			return DerAcMeasurementModelAccessorImpl.this.getOperatingState();
		}

		@Override
		public Set<? extends ModelEvent> getEvents() {
			return DerAcMeasurementModelAccessorImpl.this.getEvents();
		}

	}

}
