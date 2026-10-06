/* ==================================================================
 * DerVoltWattModelAccessorImpl.java - 5/10/2026 4:58:20 pm
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
import net.solarnetwork.sunspec.api.der.DerActivePowerReference;
import net.solarnetwork.sunspec.api.der.DerModelId;
import net.solarnetwork.sunspec.api.der.DerVoltWattModelAccessor;
import net.solarnetwork.sunspec.api.der.DerVoltWattModelRegister;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.SunspecModbusReference;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Implementation of {@link DerVoltWattModelAccessor}.
 *
 * @author matt
 * @version 1.0
 * @since 5.2
 */
public class DerVoltWattModelAccessorImpl extends BaseDerCurveModelAccessor
		implements DerVoltWattModelAccessor {

	/** The DER volt-watt model fixed block length. */
	public static final int FIXED_BLOCK_LENGTH = 13;

	/** The DER volt-watt model curve settings length. */
	public static final int CURVE_SETTINGS_LENGTH = 5;

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
	public DerVoltWattModelAccessorImpl(ModelData data, int baseAddress, ModelId modelId) {
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
	public DerVoltWattModelAccessorImpl(ModelData data, int baseAddress, int modelId) {
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
		return EnumSet.range(DerVoltWattModelRegister.CurveDependentReference,
				DerVoltWattModelRegister.CurveReadOnly);
	}

	@Override
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return EnumSet.range(DerVoltWattModelRegister.ScaleFactorVoltage,
				DerVoltWattModelRegister.ScaleFactorResponseTime);
	}

	@Override
	protected SunspecModbusReference getCurveReadOnlyRegister() {
		return DerVoltWattModelRegister.CurveReadOnly;
	}

	@Override
	protected SunspecModbusReference getPointXRegister() {
		return DerVoltWattModelRegister.PointVoltage;
	}

	@Override
	protected ModbusReference getPointXScaleFactorRegister() {
		return DerVoltWattModelRegister.ScaleFactorVoltage;
	}

	@Override
	protected SunspecModbusReference getPointYRegister() {
		return DerVoltWattModelRegister.PointActivePower;
	}

	@Override
	protected ModbusReference getPointYScaleFactorRegister() {
		return DerVoltWattModelRegister.ScaleFactorActivePower;
	}

	@Override
	public List<VoltWattCurve> getCurves() {
		return curves(VoltWattCurveImpl::new);
	}

	private final class VoltWattCurveImpl extends BaseDerCurve implements VoltWattCurve {

		private VoltWattCurveImpl(int index) {
			super(index);
		}

		@Override
		public @Nullable DerActivePowerReference getDependentReference() {
			return getCodedValue(DerVoltWattModelRegister.CurveDependentReference, curveAddress,
					DerActivePowerReference.class);
		}

		@Override
		public void setDependentReference(ModbusConnection conn, DerActivePowerReference reference)
				throws IOException {
			requireWritable();
			writeValue(conn, DerVoltWattModelRegister.CurveDependentReference, curveAddress,
					reference.getCode());
		}

		@Override
		public @Nullable Float getOpenLoopResponseTime() {
			return getScaledFloatValue(DerVoltWattModelRegister.CurveOpenLoopResponseTime,
					DerVoltWattModelRegister.ScaleFactorResponseTime, curveAddress, getBlockAddress());
		}

		@Override
		public void setOpenLoopResponseTime(ModbusConnection conn, float seconds) throws IOException {
			requireWritable();
			writeScaledValue(conn, DerVoltWattModelRegister.CurveOpenLoopResponseTime,
					DerVoltWattModelRegister.ScaleFactorResponseTime, curveAddress, getBlockAddress(),
					seconds);
		}

	}

}
