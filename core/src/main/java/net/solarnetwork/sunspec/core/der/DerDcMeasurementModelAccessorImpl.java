/* ==================================================================
 * DerDcMeasurementModelAccessorImpl.java - 5/10/2026 10:24:03 am
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.PointGroup;
import net.solarnetwork.sunspec.api.PointGroupList;
import net.solarnetwork.sunspec.api.der.DerDcMeasurementModelAccessor;
import net.solarnetwork.sunspec.api.der.DerDcMeasurementModelRegister;
import net.solarnetwork.sunspec.api.der.DerDcPortAlarm;
import net.solarnetwork.sunspec.api.der.DerDcPortStatus;
import net.solarnetwork.sunspec.api.der.DerDcPortType;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link DerDcMeasurementModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class DerDcMeasurementModelAccessorImpl extends BaseModelAccessor
		implements DerDcMeasurementModelAccessor {

	/** The DER DC measurement model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 18;

	/** The DER DC measurement model port repeating block length. */
	public static final int REPEATING_BLOCK_LENGTH = 25;

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
	public DerDcMeasurementModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
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
	public DerDcMeasurementModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, DerModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	public int getRepeatingBlockInstanceLength() {
		return REPEATING_BLOCK_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.range(DerDcMeasurementModelRegister.AlarmedPortsBitmask,
				DerDcMeasurementModelRegister.ScaleFactorTemperature);
	}

	@Override
	protected Collection<? extends ModbusReference> getRepeatingBlockRegisters() {
		return EnumSet.range(DerDcMeasurementModelRegister.PortType,
				DerDcMeasurementModelRegister.PortAlarmsBitmask);
	}

	@Override
	public Set<Integer> getAlarmedPortIndexes() {
		return getBitfieldIndexes(DerDcMeasurementModelRegister.AlarmedPortsBitmask);
	}

	@Override
	public @Nullable Integer getPortCount() {
		return getIntegerValue(DerDcMeasurementModelRegister.NumberOfPorts);
	}

	@Override
	public @Nullable Float getDCCurrent() {
		return getScaledFloatValue(DerDcMeasurementModelRegister.DcCurrent,
				DerDcMeasurementModelRegister.ScaleFactorDcCurrent);
	}

	@Override
	public @Nullable BigDecimal getDCPower() {
		return getScaledValue(DerDcMeasurementModelRegister.DcPower,
				DerDcMeasurementModelRegister.ScaleFactorDcPower);
	}

	@Override
	public @Nullable BigDecimal getDCEnergyInjected() {
		return getScaledValue(DerDcMeasurementModelRegister.DcEnergyInjected,
				DerDcMeasurementModelRegister.ScaleFactorDcEnergy);
	}

	@Override
	public @Nullable BigDecimal getDCEnergyAbsorbed() {
		return getScaledValue(DerDcMeasurementModelRegister.DcEnergyAbsorbed,
				DerDcMeasurementModelRegister.ScaleFactorDcEnergy);
	}

	@Override
	public List<DcPort> getDcPorts() {
		final int instanceCount = getRepeatingBlockInstanceCount();
		final Integer portCount = getPortCount();
		final int count = (portCount != null ? Math.min(portCount, instanceCount) : instanceCount);
		if ( count < 1 ) {
			return List.of();
		}
		final List<DcPort> result = new ArrayList<>(count);
		for ( int i = 0; i < count; i++ ) {
			result.add(new DerDcPort(i));
		}
		return result;
	}

	@Override
	public @Nullable Object getPointValue(ModbusReference point) {
		if ( !(point instanceof DerDcMeasurementModelRegister r) ) {
			return null;
		}
		return switch (r) {
			case AlarmedPortsBitmask -> getAlarmedPortIndexes();
			case NumberOfPorts -> getPortCount();
			case DcCurrent -> getDCCurrent();
			case DcPower -> getDCPower();
			case DcEnergyInjected -> getDCEnergyInjected();
			case DcEnergyAbsorbed -> getDCEnergyAbsorbed();
			case ScaleFactorDcCurrent, ScaleFactorDcVoltage, ScaleFactorDcPower -> null;
			case ScaleFactorDcEnergy, ScaleFactorTemperature, PortType, PortId, PortName -> null;
			case PortDcCurrent, PortDcVoltage, PortDcPower, PortDcEnergyInjected -> null;
			case PortDcEnergyAbsorbed, PortTemperature, PortStatus, PortAlarmsBitmask -> null;
		};
	}

	@Override
	public List<PointGroupList> getPointGroups() {
		return List.of(PointGroupList.repeating("DcPorts",
				getDcPorts().stream().map(PointGroup.class::cast).toList()));
	}

	private final class DerDcPort implements DcPort, PointGroup {

		private final int portAddress;

		private DerDcPort(int index) {
			super();
			this.portAddress = getBlockAddress() + FIXED_BLOCK_LENGTH + index * REPEATING_BLOCK_LENGTH;
		}

		@Override
		public @Nullable DerDcPortType getPortType() {
			return getCodedValue(DerDcMeasurementModelRegister.PortType, portAddress,
					DerDcPortType.class);
		}

		@Override
		public @Nullable Integer getPortId() {
			return getIntegerValue(DerDcMeasurementModelRegister.PortId, portAddress);
		}

		@Override
		public @Nullable String getPortName() {
			return getStringValue(DerDcMeasurementModelRegister.PortName, portAddress);
		}

		@Override
		public @Nullable Float getDCCurrent() {
			return getScaledFloatValue(DerDcMeasurementModelRegister.PortDcCurrent,
					DerDcMeasurementModelRegister.ScaleFactorDcCurrent, portAddress, getBlockAddress());
		}

		@Override
		public @Nullable Float getDCVoltage() {
			return getScaledFloatValue(DerDcMeasurementModelRegister.PortDcVoltage,
					DerDcMeasurementModelRegister.ScaleFactorDcVoltage, portAddress, getBlockAddress());
		}

		@Override
		public @Nullable BigDecimal getDCPower() {
			return getScaledValue(DerDcMeasurementModelRegister.PortDcPower,
					DerDcMeasurementModelRegister.ScaleFactorDcPower, portAddress, getBlockAddress());
		}

		@Override
		public @Nullable BigDecimal getDCEnergyInjected() {
			return getScaledValue(DerDcMeasurementModelRegister.PortDcEnergyInjected,
					DerDcMeasurementModelRegister.ScaleFactorDcEnergy, portAddress, getBlockAddress());
		}

		@Override
		public @Nullable BigDecimal getDCEnergyAbsorbed() {
			return getScaledValue(DerDcMeasurementModelRegister.PortDcEnergyAbsorbed,
					DerDcMeasurementModelRegister.ScaleFactorDcEnergy, portAddress, getBlockAddress());
		}

		@Override
		public @Nullable Float getTemperature() {
			return getScaledFloatValue(DerDcMeasurementModelRegister.PortTemperature,
					DerDcMeasurementModelRegister.ScaleFactorTemperature, portAddress,
					getBlockAddress());
		}

		@Override
		public @Nullable DerDcPortStatus getPortStatus() {
			return getCodedValue(DerDcMeasurementModelRegister.PortStatus, portAddress,
					DerDcPortStatus.class);
		}

		@Override
		public Set<? extends ModelEvent> getEvents() {
			Number n = getBitfield(DerDcMeasurementModelRegister.PortAlarmsBitmask, portAddress);
			return DerDcPortAlarm.forBitmask(n != null ? n.longValue() : 0L);
		}

		@Override
		public Collection<? extends ModbusReference> getPointReferences() {
			return getRepeatingBlockRegisters();
		}

		@Override
		public @Nullable Object getPointValue(ModbusReference point) {
			if ( !(point instanceof DerDcMeasurementModelRegister r) ) {
				return null;
			}
			return switch (r) {
				case PortType -> getPortType();
				case PortId -> getPortId();
				case PortName -> getPortName();
				case PortDcCurrent -> getDCCurrent();
				case PortDcVoltage -> getDCVoltage();
				case PortDcPower -> getDCPower();
				case PortDcEnergyInjected -> getDCEnergyInjected();
				case PortDcEnergyAbsorbed -> getDCEnergyAbsorbed();
				case PortTemperature -> getTemperature();
				case PortStatus -> getPortStatus();
				case PortAlarmsBitmask -> getEvents();
				case AlarmedPortsBitmask, NumberOfPorts, DcCurrent, DcPower, DcEnergyInjected -> null;
				case DcEnergyAbsorbed, ScaleFactorDcCurrent, ScaleFactorDcVoltage -> null;
				case ScaleFactorDcPower, ScaleFactorDcEnergy, ScaleFactorTemperature -> null;
			};
		}

	}

}
