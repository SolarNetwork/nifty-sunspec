/* ==================================================================
 * DerWattVarModelAccessorImpl.java - 5/10/2026 4:58:20 pm
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

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerReactivePowerPriority;
import net.solarnetwork.sunspec.api.der.DerReactivePowerReference;
import net.solarnetwork.sunspec.api.der.DerWattVarModelAccessor;
import net.solarnetwork.sunspec.api.der.DerWattVarModelRegister;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link DerWattVarModelAccessor}.
 *
 * @author matt
 * @version 1.0
 */
public class DerWattVarModelAccessorImpl extends BaseDerCurveModelAccessor
		implements DerWattVarModelAccessor {

	/** The DER watt-var model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 12;

	/** The DER watt-var model curve settings length. */
	public static final int CURVE_SETTINGS_LENGTH = 4;

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
	public DerWattVarModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
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
	public DerWattVarModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
		this(data, baseAddress, DerModelId.forId(modelId));
	}

	@Override
	public int getFixedBlockLength() {
		return FIXED_BLOCK_LENGTH;
	}

	@Override
	protected int getCurveSettingsLength() {
		return CURVE_SETTINGS_LENGTH;
	}

	@Override
	protected Collection<? extends ModbusReference> getCurveSettingsRegisters() {
		return EnumSet.range(DerWattVarModelRegister.CurveDependentReference,
				DerWattVarModelRegister.CurveReadOnly);
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.range(DerWattVarModelRegister.ScaleFactorActivePower,
				DerWattVarModelRegister.ScaleFactorReactivePower);
	}

	@Override
	protected ModbusReference getCurveReadOnlyRegister() {
		return DerWattVarModelRegister.CurveReadOnly;
	}

	@Override
	protected ModbusReference getPointXRegister() {
		return DerWattVarModelRegister.PointActivePower;
	}

	@Override
	protected ModbusReference getPointXScaleFactorRegister() {
		return DerWattVarModelRegister.ScaleFactorActivePower;
	}

	@Override
	protected ModbusReference getPointYRegister() {
		return DerWattVarModelRegister.PointReactivePower;
	}

	@Override
	protected ModbusReference getPointYScaleFactorRegister() {
		return DerWattVarModelRegister.ScaleFactorReactivePower;
	}

	@Override
	public List<WattVarCurve> getCurves() {
		return curves(WattVarCurveImpl::new);
	}

	private final class WattVarCurveImpl extends BaseDerCurve implements WattVarCurve {

		private WattVarCurveImpl(int index) {
			super(index);
		}

		@Override
		public @Nullable DerReactivePowerReference getDependentReference() {
			return getCodedValue(DerWattVarModelRegister.CurveDependentReference, curveAddress,
					DerReactivePowerReference.class);
		}

		@Override
		public void setDependentReference(ModbusConnection conn, DerReactivePowerReference reference)
				throws IOException {
			requireWritable();
			writeValue(conn, DerWattVarModelRegister.CurveDependentReference, curveAddress,
					reference.getCode());
		}

		@Override
		public @Nullable DerReactivePowerPriority getPowerPriority() {
			return getCodedValue(DerWattVarModelRegister.CurvePowerPriority, curveAddress,
					DerReactivePowerPriority.class);
		}

		@Override
		public void setPowerPriority(ModbusConnection conn, DerReactivePowerPriority priority)
				throws IOException {
			if ( priority == DerReactivePowerPriority.Vendor ) {
				throw new IllegalArgumentException(
						"The watt-var model does not support the vendor power priority.");
			}
			requireWritable();
			writeValue(conn, DerWattVarModelRegister.CurvePowerPriority, curveAddress,
					priority.getCode());
		}

	}

}
