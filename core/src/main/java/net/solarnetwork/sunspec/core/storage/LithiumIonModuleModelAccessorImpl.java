/* ==================================================================
 * LithiumIonModuleModelAccessorImpl.java - 5/10/2026 9:05:44 pm
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.PointGroup;
import net.solarnetwork.sunspec.api.PointGroupList;
import net.solarnetwork.sunspec.api.storage.LithiumIonCellStatus;
import net.solarnetwork.sunspec.api.storage.LithiumIonModuleModelAccessor;
import net.solarnetwork.sunspec.api.storage.LithiumIonModuleModelRegister;
import net.solarnetwork.sunspec.api.storage.StorageModelId;
import net.solarnetwork.sunspec.core.BaseModelAccessor;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link LithiumIonModuleModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class LithiumIonModuleModelAccessorImpl extends BaseModelAccessor
		implements LithiumIonModuleModelAccessor {

	/** The lithium-ion module model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 42;

	/** The lithium-ion module model cell repeating block length. */
	public static final int REPEATING_BLOCK_LENGTH = 4;

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
	public LithiumIonModuleModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
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
	public LithiumIonModuleModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
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
		return EnumSet.range(LithiumIonModuleModelRegister.StringIndex,
				LithiumIonModuleModelRegister.ScaleFactorTemperature);
	}

	@Override
	protected Collection<? extends ModbusReference> getRepeatingBlockRegisters() {
		return EnumSet.range(LithiumIonModuleModelRegister.CellVoltage,
				LithiumIonModuleModelRegister.CellStatus);
	}

	@Override
	public @Nullable Integer getStringIndex() {
		return getIntegerValue(LithiumIonModuleModelRegister.StringIndex);
	}

	@Override
	public @Nullable Integer getModuleIndex() {
		return getIntegerValue(LithiumIonModuleModelRegister.ModuleIndex);
	}

	@Override
	public @Nullable Integer getCellCount() {
		return getIntegerValue(LithiumIonModuleModelRegister.NumberOfCells);
	}

	@Override
	public @Nullable Float getStateOfCharge() {
		return getScaledFloatValue(LithiumIonModuleModelRegister.StateOfCharge,
				LithiumIonModuleModelRegister.ScaleFactorStateOfCharge);
	}

	@Override
	public @Nullable Float getDepthOfDischarge() {
		return getScaledFloatValue(LithiumIonModuleModelRegister.DepthOfDischarge,
				LithiumIonModuleModelRegister.ScaleFactorDepthOfDischarge);
	}

	@Override
	public @Nullable Float getStateOfHealth() {
		return getScaledFloatValue(LithiumIonModuleModelRegister.StateOfHealth,
				LithiumIonModuleModelRegister.ScaleFactorStateOfHealth);
	}

	@Override
	public @Nullable Long getCycleCount() {
		return getLongValue(LithiumIonModuleModelRegister.CycleCount);
	}

	@Override
	public @Nullable Float getDCVoltage() {
		return getScaledFloatValue(LithiumIonModuleModelRegister.DcVoltage,
				LithiumIonModuleModelRegister.ScaleFactorVoltage);
	}

	@Override
	public @Nullable Float getMaximumCellVoltage() {
		return getScaledFloatValue(LithiumIonModuleModelRegister.MaximumCellVoltage,
				LithiumIonModuleModelRegister.ScaleFactorCellVoltage);
	}

	@Override
	public @Nullable Integer getMaximumCellVoltageCellIndex() {
		return getIntegerValue(LithiumIonModuleModelRegister.MaximumCellVoltageCellIndex);
	}

	@Override
	public @Nullable Float getMinimumCellVoltage() {
		return getScaledFloatValue(LithiumIonModuleModelRegister.MinimumCellVoltage,
				LithiumIonModuleModelRegister.ScaleFactorCellVoltage);
	}

	@Override
	public @Nullable Integer getMinimumCellVoltageCellIndex() {
		return getIntegerValue(LithiumIonModuleModelRegister.MinimumCellVoltageCellIndex);
	}

	@Override
	public @Nullable Float getAverageCellVoltage() {
		return getScaledFloatValue(LithiumIonModuleModelRegister.AverageCellVoltage,
				LithiumIonModuleModelRegister.ScaleFactorCellVoltage);
	}

	@Override
	public @Nullable Float getMaximumCellTemperature() {
		return getScaledFloatValue(LithiumIonModuleModelRegister.MaximumCellTemperature,
				LithiumIonModuleModelRegister.ScaleFactorTemperature);
	}

	@Override
	public @Nullable Integer getMaximumCellTemperatureCellIndex() {
		return getIntegerValue(LithiumIonModuleModelRegister.MaximumCellTemperatureCellIndex);
	}

	@Override
	public @Nullable Float getMinimumCellTemperature() {
		return getScaledFloatValue(LithiumIonModuleModelRegister.MinimumCellTemperature,
				LithiumIonModuleModelRegister.ScaleFactorTemperature);
	}

	@Override
	public @Nullable Integer getMinimumCellTemperatureCellIndex() {
		return getIntegerValue(LithiumIonModuleModelRegister.MinimumCellTemperatureCellIndex);
	}

	@Override
	public @Nullable Float getAverageCellTemperature() {
		return getScaledFloatValue(LithiumIonModuleModelRegister.AverageCellTemperature,
				LithiumIonModuleModelRegister.ScaleFactorTemperature);
	}

	@Override
	public @Nullable Integer getBalancingCellCount() {
		return getIntegerValue(LithiumIonModuleModelRegister.BalancingCellCount);
	}

	@Override
	public @Nullable String getSerialNumber() {
		return getStringValue(LithiumIonModuleModelRegister.SerialNumber);
	}

	@Override
	public List<BatteryCell> getCells() {
		final int count = getRepeatingBlockInstanceCount();
		if ( count < 1 ) {
			return List.of();
		}
		final List<BatteryCell> result = new ArrayList<>(count);
		for ( int i = 1; i <= count; i++ ) {
			result.add(new BatteryCellImpl(i));
		}
		return result;
	}

	@Override
	public @Nullable Object getPointValue(ModbusReference point) {
		if ( !(point instanceof LithiumIonModuleModelRegister r) ) {
			return null;
		}
		return switch (r) {
			case StringIndex -> getStringIndex();
			case ModuleIndex -> getModuleIndex();
			case NumberOfCells -> getCellCount();
			case StateOfCharge -> getStateOfCharge();
			case DepthOfDischarge -> getDepthOfDischarge();
			case StateOfHealth -> getStateOfHealth();
			case CycleCount -> getCycleCount();
			case DcVoltage -> getDCVoltage();
			case MaximumCellVoltage -> getMaximumCellVoltage();
			case MaximumCellVoltageCellIndex -> getMaximumCellVoltageCellIndex();
			case MinimumCellVoltage -> getMinimumCellVoltage();
			case MinimumCellVoltageCellIndex -> getMinimumCellVoltageCellIndex();
			case AverageCellVoltage -> getAverageCellVoltage();
			case MaximumCellTemperature -> getMaximumCellTemperature();
			case MaximumCellTemperatureCellIndex -> getMaximumCellTemperatureCellIndex();
			case MinimumCellTemperature -> getMinimumCellTemperature();
			case MinimumCellTemperatureCellIndex -> getMinimumCellTemperatureCellIndex();
			case AverageCellTemperature -> getAverageCellTemperature();
			case BalancingCellCount -> getBalancingCellCount();
			case SerialNumber -> getSerialNumber();
			case ScaleFactorStateOfCharge, ScaleFactorStateOfHealth, ScaleFactorDepthOfDischarge -> null;
			case ScaleFactorVoltage -> null;
			case ScaleFactorCellVoltage, ScaleFactorTemperature, CellVoltage, CellTemperature -> null;
			case CellStatus -> null;
		};
	}

	@Override
	public List<PointGroupList> getPointGroups() {
		return List.of(PointGroupList.repeating("Cells",
				getCells().stream().map(PointGroup.class::cast).toList()));
	}

	private final class BatteryCellImpl implements BatteryCell, PointGroup {

		private final int index;
		private final int groupAddress;

		private BatteryCellImpl(int index) {
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
		public @Nullable Float getVoltage() {
			return getScaledFloatValue(LithiumIonModuleModelRegister.CellVoltage,
					LithiumIonModuleModelRegister.ScaleFactorCellVoltage, groupAddress,
					getBlockAddress());
		}

		@Override
		public @Nullable Float getTemperature() {
			return getScaledFloatValue(LithiumIonModuleModelRegister.CellTemperature,
					LithiumIonModuleModelRegister.ScaleFactorTemperature, groupAddress,
					getBlockAddress());
		}

		@Override
		public Set<LithiumIonCellStatus> getStatus() {
			return getBitmaskableValues(LithiumIonModuleModelRegister.CellStatus, groupAddress,
					LithiumIonCellStatus.class);
		}

		@Override
		public Collection<? extends ModbusReference> getPointReferences() {
			return getRepeatingBlockRegisters();
		}

		@Override
		public @Nullable Object getPointValue(ModbusReference point) {
			if ( !(point instanceof LithiumIonModuleModelRegister r) ) {
				return null;
			}
			return switch (r) {
				case CellVoltage -> getVoltage();
				case CellTemperature -> getTemperature();
				case CellStatus -> getStatus();
				case StringIndex, ModuleIndex, NumberOfCells, StateOfCharge -> null;
				case DepthOfDischarge, StateOfHealth, CycleCount, DcVoltage -> null;
				case MaximumCellVoltage, MaximumCellVoltageCellIndex, MinimumCellVoltage -> null;
				case MinimumCellVoltageCellIndex -> null;
				case AverageCellVoltage, MaximumCellTemperature, MaximumCellTemperatureCellIndex -> null;
				case MinimumCellTemperature -> null;
				case MinimumCellTemperatureCellIndex, AverageCellTemperature, BalancingCellCount -> null;
				case SerialNumber -> null;
				case ScaleFactorStateOfCharge, ScaleFactorStateOfHealth -> null;
				case ScaleFactorDepthOfDischarge, ScaleFactorVoltage -> null;
				case ScaleFactorCellVoltage, ScaleFactorTemperature -> null;
			};
		}

	}

}
