/* ==================================================================
 * LithiumIonStringModelAccessorImpl.java - 5/10/2026 9:05:44 pm
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

package net.solarnetwork.sunspec.core.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.PointGroup;
import net.solarnetwork.sunspec.api.PointGroupList;
import net.solarnetwork.sunspec.api.storage.BatteryConnectionFailure;
import net.solarnetwork.sunspec.api.storage.BatteryConnectionStatus;
import net.solarnetwork.sunspec.api.storage.BatteryEnableOperation;
import net.solarnetwork.sunspec.api.storage.BatteryOperation;
import net.solarnetwork.sunspec.api.storage.LithiumIonStringEvent;
import net.solarnetwork.sunspec.api.storage.LithiumIonStringModelAccessor;
import net.solarnetwork.sunspec.api.storage.LithiumIonStringModelRegister;
import net.solarnetwork.sunspec.api.storage.StorageModelId;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link LithiumIonStringModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class LithiumIonStringModelAccessorImpl extends BaseModelAccessor
		implements LithiumIonStringModelAccessor {

	/** The lithium-ion string model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 46;

	/** The lithium-ion string model module repeating block length. */
	public static final int REPEATING_BLOCK_LENGTH = 16;

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
	public LithiumIonStringModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
		super(data, baseAddress, modelId);
	}

	/**
	 * Constructor.
	 *
	 * <p>
	 * The {@link StorageModelId} class will be used as the {@code ModelId}
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
	public LithiumIonStringModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, StorageModelId.forId(modelId));
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
		return EnumSet.range(LithiumIonStringModelRegister.StringIndex,
				LithiumIonStringModelRegister.ScaleFactorModuleTemperature);
	}

	@Override
	protected Collection<? extends ModbusReference> getRepeatingBlockRegisters() {
		return EnumSet.range(LithiumIonStringModelRegister.ModuleCellCount,
				LithiumIonStringModelRegister.ModuleAverageCellTemperature);
	}

	@Override
	public @Nullable Integer getStringIndex() {
		return getIntegerValue(LithiumIonStringModelRegister.StringIndex);
	}

	@Override
	public @Nullable Integer getModuleCount() {
		return getIntegerValue(LithiumIonStringModelRegister.NumberOfModules);
	}

	@Override
	public Set<BatteryConnectionStatus> getStatus() {
		return getBitmaskableValues(LithiumIonStringModelRegister.Status, BatteryConnectionStatus.class);
	}

	@Override
	public @Nullable BatteryConnectionFailure getConnectionFailure() {
		return getCodedValue(LithiumIonStringModelRegister.ConnectionFailure,
				BatteryConnectionFailure.class);
	}

	@Override
	public @Nullable Integer getBalancingCellCount() {
		return getIntegerValue(LithiumIonStringModelRegister.BalancingCellCount);
	}

	@Override
	public @Nullable Float getStateOfCharge() {
		return getScaledFloatValue(LithiumIonStringModelRegister.StateOfCharge,
				LithiumIonStringModelRegister.ScaleFactorStateOfCharge);
	}

	@Override
	public @Nullable Float getDepthOfDischarge() {
		return getScaledFloatValue(LithiumIonStringModelRegister.DepthOfDischarge,
				LithiumIonStringModelRegister.ScaleFactorDepthOfDischarge);
	}

	@Override
	public @Nullable Long getCycleCount() {
		return getLongValue(LithiumIonStringModelRegister.CycleCount);
	}

	@Override
	public @Nullable Float getStateOfHealth() {
		return getScaledFloatValue(LithiumIonStringModelRegister.StateOfHealth,
				LithiumIonStringModelRegister.ScaleFactorStateOfHealth);
	}

	@Override
	public @Nullable Float getDCCurrent() {
		return getScaledFloatValue(LithiumIonStringModelRegister.DcCurrent,
				LithiumIonStringModelRegister.ScaleFactorCurrent);
	}

	@Override
	public @Nullable Float getDCVoltage() {
		return getScaledFloatValue(LithiumIonStringModelRegister.DcVoltage,
				LithiumIonStringModelRegister.ScaleFactorVoltage);
	}

	@Override
	public @Nullable Float getMaximumCellVoltage() {
		return getScaledFloatValue(LithiumIonStringModelRegister.MaximumCellVoltage,
				LithiumIonStringModelRegister.ScaleFactorCellVoltage);
	}

	@Override
	public @Nullable Integer getMaximumCellVoltageModuleIndex() {
		return getIntegerValue(LithiumIonStringModelRegister.MaximumCellVoltageModuleIndex);
	}

	@Override
	public @Nullable Float getMinimumCellVoltage() {
		return getScaledFloatValue(LithiumIonStringModelRegister.MinimumCellVoltage,
				LithiumIonStringModelRegister.ScaleFactorCellVoltage);
	}

	@Override
	public @Nullable Integer getMinimumCellVoltageModuleIndex() {
		return getIntegerValue(LithiumIonStringModelRegister.MinimumCellVoltageModuleIndex);
	}

	@Override
	public @Nullable Float getAverageCellVoltage() {
		return getScaledFloatValue(LithiumIonStringModelRegister.AverageCellVoltage,
				LithiumIonStringModelRegister.ScaleFactorCellVoltage);
	}

	@Override
	public @Nullable Float getMaximumModuleTemperature() {
		return getScaledFloatValue(LithiumIonStringModelRegister.MaximumModuleTemperature,
				LithiumIonStringModelRegister.ScaleFactorModuleTemperature);
	}

	@Override
	public @Nullable Integer getMaximumModuleTemperatureModuleIndex() {
		return getIntegerValue(LithiumIonStringModelRegister.MaximumModuleTemperatureModuleIndex);
	}

	@Override
	public @Nullable Float getMinimumModuleTemperature() {
		return getScaledFloatValue(LithiumIonStringModelRegister.MinimumModuleTemperature,
				LithiumIonStringModelRegister.ScaleFactorModuleTemperature);
	}

	@Override
	public @Nullable Integer getMinimumModuleTemperatureModuleIndex() {
		return getIntegerValue(LithiumIonStringModelRegister.MinimumModuleTemperatureModuleIndex);
	}

	@Override
	public @Nullable Float getAverageModuleTemperature() {
		return getScaledFloatValue(LithiumIonStringModelRegister.AverageModuleTemperature,
				LithiumIonStringModelRegister.ScaleFactorModuleTemperature);
	}

	@Override
	public Set<Integer> getClosedContactors() {
		return getBitfieldIndexes(LithiumIonStringModelRegister.ContactorStatus);
	}

	@Override
	public Set<? extends ModelEvent> getEvents() {
		Number n = getBitfield(LithiumIonStringModelRegister.EventsBitmask);
		return LithiumIonStringEvent.forBitmask(n != null ? n.longValue() : 0L);
	}

	@Override
	public BitSet getVendorEvents() {
		return getBitfieldBits(LithiumIonStringModelRegister.VendorEventsBitmask,
				LithiumIonStringModelRegister.VendorEvents2Bitmask);
	}

	@Override
	public @Nullable BatteryEnableOperation getEnableOperation() {
		return getCodedValue(LithiumIonStringModelRegister.EnableOperation,
				BatteryEnableOperation.class);
	}

	@Override
	public void setEnableOperation(ModbusConnection conn, BatteryEnableOperation operation)
			throws IOException {
		writeValue(conn, LithiumIonStringModelRegister.EnableOperation, operation.getCode());
	}

	@Override
	public @Nullable BatteryOperation getConnectOperation() {
		return getCodedValue(LithiumIonStringModelRegister.ConnectOperation, BatteryOperation.class);
	}

	@Override
	public void setConnectOperation(ModbusConnection conn, BatteryOperation operation)
			throws IOException {
		writeValue(conn, LithiumIonStringModelRegister.ConnectOperation, operation.getCode());
	}

	@Override
	public List<BatteryModule> getModules() {
		final int instanceCount = getRepeatingBlockInstanceCount();
		final Integer moduleCount = getModuleCount();
		final int count = (moduleCount != null ? Math.min(moduleCount, instanceCount) : instanceCount);
		if ( count < 1 ) {
			return List.of();
		}
		final List<BatteryModule> result = new ArrayList<>(count);
		for ( int i = 1; i <= count; i++ ) {
			result.add(new BatteryModuleImpl(i));
		}
		return result;
	}

	@Override
	public @Nullable Object getPointValue(ModbusReference point) {
		if ( !(point instanceof LithiumIonStringModelRegister r) ) {
			return null;
		}
		return switch (r) {
			case StringIndex -> getStringIndex();
			case NumberOfModules -> getModuleCount();
			case Status -> getStatus();
			case ConnectionFailure -> getConnectionFailure();
			case BalancingCellCount -> getBalancingCellCount();
			case StateOfCharge -> getStateOfCharge();
			case DepthOfDischarge -> getDepthOfDischarge();
			case CycleCount -> getCycleCount();
			case StateOfHealth -> getStateOfHealth();
			case DcCurrent -> getDCCurrent();
			case DcVoltage -> getDCVoltage();
			case MaximumCellVoltage -> getMaximumCellVoltage();
			case MaximumCellVoltageModuleIndex -> getMaximumCellVoltageModuleIndex();
			case MinimumCellVoltage -> getMinimumCellVoltage();
			case MinimumCellVoltageModuleIndex -> getMinimumCellVoltageModuleIndex();
			case AverageCellVoltage -> getAverageCellVoltage();
			case MaximumModuleTemperature -> getMaximumModuleTemperature();
			case MaximumModuleTemperatureModuleIndex -> getMaximumModuleTemperatureModuleIndex();
			case MinimumModuleTemperature -> getMinimumModuleTemperature();
			case MinimumModuleTemperatureModuleIndex -> getMinimumModuleTemperatureModuleIndex();
			case AverageModuleTemperature -> getAverageModuleTemperature();
			case ContactorStatus -> getClosedContactors();
			case EventsBitmask -> getEvents();
			case Events2Bitmask -> null; // SunSpec defines no events
			case VendorEventsBitmask -> getVendorEvents();
			case VendorEvents2Bitmask -> null; // included in the vendor events
			case EnableOperation -> getEnableOperation();
			case ConnectOperation -> getConnectOperation();
			case ScaleFactorStateOfCharge, ScaleFactorStateOfHealth, ScaleFactorDepthOfDischarge -> null;
			case ScaleFactorCurrent -> null;
			case ScaleFactorVoltage, ScaleFactorCellVoltage, ScaleFactorModuleTemperature -> null;
			case ModuleCellCount -> null;
			case ModuleStateOfCharge, ModuleStateOfHealth, ModuleMaximumCellVoltage -> null;
			case ModuleMaximumCellVoltageCellIndex -> null;
			case ModuleMinimumCellVoltage, ModuleMinimumCellVoltageCellIndex -> null;
			case ModuleAverageCellVoltage, ModuleMaximumCellTemperature -> null;
			case ModuleMaximumCellTemperatureCellIndex, ModuleMinimumCellTemperature -> null;
			case ModuleMinimumCellTemperatureCellIndex, ModuleAverageCellTemperature -> null;
		};
	}

	@Override
	public List<PointGroupList> getPointGroups() {
		return List.of(PointGroupList.repeating("Modules",
				getModules().stream().map(PointGroup.class::cast).toList()));
	}

	private final class BatteryModuleImpl implements BatteryModule, PointGroup {

		private final int index;
		private final int groupAddress;

		private BatteryModuleImpl(int index) {
			super();
			this.index = index;
			this.groupAddress = getBlockAddress() + FIXED_BLOCK_LENGTH
					+ (index - 1) * REPEATING_BLOCK_LENGTH;
		}

		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public @Nullable Integer getCellCount() {
			return getIntegerValue(LithiumIonStringModelRegister.ModuleCellCount, groupAddress);
		}

		@Override
		public @Nullable Float getStateOfCharge() {
			return getScaledFloatValue(LithiumIonStringModelRegister.ModuleStateOfCharge,
					LithiumIonStringModelRegister.ScaleFactorStateOfCharge, groupAddress,
					getBlockAddress());
		}

		@Override
		public @Nullable Float getStateOfHealth() {
			return getScaledFloatValue(LithiumIonStringModelRegister.ModuleStateOfHealth,
					LithiumIonStringModelRegister.ScaleFactorStateOfHealth, groupAddress,
					getBlockAddress());
		}

		@Override
		public @Nullable Float getMaximumCellVoltage() {
			return getScaledFloatValue(LithiumIonStringModelRegister.ModuleMaximumCellVoltage,
					LithiumIonStringModelRegister.ScaleFactorCellVoltage, groupAddress,
					getBlockAddress());
		}

		@Override
		public @Nullable Integer getMaximumCellVoltageCellIndex() {
			return getIntegerValue(LithiumIonStringModelRegister.ModuleMaximumCellVoltageCellIndex,
					groupAddress);
		}

		@Override
		public @Nullable Float getMinimumCellVoltage() {
			return getScaledFloatValue(LithiumIonStringModelRegister.ModuleMinimumCellVoltage,
					LithiumIonStringModelRegister.ScaleFactorCellVoltage, groupAddress,
					getBlockAddress());
		}

		@Override
		public @Nullable Integer getMinimumCellVoltageCellIndex() {
			return getIntegerValue(LithiumIonStringModelRegister.ModuleMinimumCellVoltageCellIndex,
					groupAddress);
		}

		@Override
		public @Nullable Float getAverageCellVoltage() {
			return getScaledFloatValue(LithiumIonStringModelRegister.ModuleAverageCellVoltage,
					LithiumIonStringModelRegister.ScaleFactorCellVoltage, groupAddress,
					getBlockAddress());
		}

		@Override
		public @Nullable Float getMaximumCellTemperature() {
			return getScaledFloatValue(LithiumIonStringModelRegister.ModuleMaximumCellTemperature,
					LithiumIonStringModelRegister.ScaleFactorModuleTemperature, groupAddress,
					getBlockAddress());
		}

		@Override
		public @Nullable Integer getMaximumCellTemperatureCellIndex() {
			return getIntegerValue(LithiumIonStringModelRegister.ModuleMaximumCellTemperatureCellIndex,
					groupAddress);
		}

		@Override
		public @Nullable Float getMinimumCellTemperature() {
			return getScaledFloatValue(LithiumIonStringModelRegister.ModuleMinimumCellTemperature,
					LithiumIonStringModelRegister.ScaleFactorModuleTemperature, groupAddress,
					getBlockAddress());
		}

		@Override
		public @Nullable Integer getMinimumCellTemperatureCellIndex() {
			return getIntegerValue(LithiumIonStringModelRegister.ModuleMinimumCellTemperatureCellIndex,
					groupAddress);
		}

		@Override
		public @Nullable Float getAverageCellTemperature() {
			return getScaledFloatValue(LithiumIonStringModelRegister.ModuleAverageCellTemperature,
					LithiumIonStringModelRegister.ScaleFactorModuleTemperature, groupAddress,
					getBlockAddress());
		}

		@Override
		public Collection<? extends ModbusReference> getPointReferences() {
			return getRepeatingBlockRegisters();
		}

		@Override
		public @Nullable Object getPointValue(ModbusReference point) {
			if ( !(point instanceof LithiumIonStringModelRegister r) ) {
				return null;
			}
			return switch (r) {
				case ModuleCellCount -> getCellCount();
				case ModuleStateOfCharge -> getStateOfCharge();
				case ModuleStateOfHealth -> getStateOfHealth();
				case ModuleMaximumCellVoltage -> getMaximumCellVoltage();
				case ModuleMaximumCellVoltageCellIndex -> getMaximumCellVoltageCellIndex();
				case ModuleMinimumCellVoltage -> getMinimumCellVoltage();
				case ModuleMinimumCellVoltageCellIndex -> getMinimumCellVoltageCellIndex();
				case ModuleAverageCellVoltage -> getAverageCellVoltage();
				case ModuleMaximumCellTemperature -> getMaximumCellTemperature();
				case ModuleMaximumCellTemperatureCellIndex -> getMaximumCellTemperatureCellIndex();
				case ModuleMinimumCellTemperature -> getMinimumCellTemperature();
				case ModuleMinimumCellTemperatureCellIndex -> getMinimumCellTemperatureCellIndex();
				case ModuleAverageCellTemperature -> getAverageCellTemperature();
				case StringIndex, NumberOfModules, Status, ConnectionFailure -> null;
				case BalancingCellCount, StateOfCharge, DepthOfDischarge, CycleCount -> null;
				case StateOfHealth, DcCurrent, DcVoltage, MaximumCellVoltage -> null;
				case MaximumCellVoltageModuleIndex, MinimumCellVoltage -> null;
				case MinimumCellVoltageModuleIndex, AverageCellVoltage -> null;
				case MaximumModuleTemperature, MaximumModuleTemperatureModuleIndex -> null;
				case MinimumModuleTemperature, MinimumModuleTemperatureModuleIndex -> null;
				case AverageModuleTemperature, ContactorStatus, EventsBitmask, Events2Bitmask -> null;
				case VendorEventsBitmask, VendorEvents2Bitmask, EnableOperation -> null;
				case ConnectOperation -> null;
				case ScaleFactorStateOfCharge, ScaleFactorStateOfHealth -> null;
				case ScaleFactorDepthOfDischarge, ScaleFactorCurrent -> null;
				case ScaleFactorVoltage, ScaleFactorCellVoltage, ScaleFactorModuleTemperature -> null;
			};
		}

	}

}
