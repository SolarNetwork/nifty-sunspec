/* ==================================================================
 * ReversedInverterModelAccessor.java - 15/09/2022 9:26:08 am
 *
 * Copyright 2022 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.api.inverter;

import java.time.Instant;
import java.util.BitSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.domain.AcPhase;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.OperatingState;
import net.solarnetwork.util.IntRange;

/**
 * * A "reversed" inverter model accessor that swaps import/export values.
 *
 *
 * @author matt
 * @version 1.2
 * @since 3.1
 */
public class ReversedInverterModelAccessor implements InverterModelAccessor {

	private final InverterModelAccessor delegate;

	/**
	 * Constructor.
	 *
	 * @param delegate
	 *        the accessor to reverse
	 */
	public ReversedInverterModelAccessor(InverterModelAccessor delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public InverterModelAccessor accessorForPhase(AcPhase phase) {
		return new ReversedInverterModelAccessor(delegate.accessorForPhase(phase));
	}

	@Override
	public InverterModelAccessor reversed() {
		return delegate;
	}

	@Override
	public @Nullable Instant getDataTimestamp() {
		return delegate.getDataTimestamp();
	}

	@Override
	public @Nullable Float getFrequency() {
		return delegate.getFrequency();
	}

	@Override
	public @Nullable Float getCurrent() {
		return delegate.getCurrent();
	}

	@Override
	public @Nullable Float getVoltage() {
		return delegate.getVoltage();
	}

	@Override
	public @Nullable Float getLineVoltage() {
		return delegate.getLineVoltage();
	}

	@Override
	public @Nullable Float getPowerFactor() {
		return delegate.getPowerFactor();
	}

	@Override
	public @Nullable Integer getActivePower() {
		Integer n = delegate.getActivePower();
		return (n != null ? n * -1 : null);
	}

	@Override
	public @Nullable Integer getApparentPower() {
		return delegate.getApparentPower();
	}

	@Override
	public @Nullable Integer getReactivePower() {
		Integer n = delegate.getReactivePower();
		return (n != null ? n * -1 : null);
	}

	@Override
	public int getBaseAddress() {
		return delegate.getBaseAddress();
	}

	@Override
	public int getBlockAddress() {
		return delegate.getBlockAddress();
	}

	@Override
	public ModelId getModelId() {
		return delegate.getModelId();
	}

	@Override
	public int getFixedBlockLength() {
		return delegate.getFixedBlockLength();
	}

	@Override
	public IntRange[] getAddressRanges(int maxRangeLength) {
		return delegate.getAddressRanges(maxRangeLength);
	}

	@Override
	public IntRange getAddressRange(int address, int maxRangeLength) {
		return delegate.getAddressRange(address, maxRangeLength);
	}

	@Override
	public List<IntRange> getUnsplittableAddressRanges() {
		return delegate.getUnsplittableAddressRanges();
	}

	@Override
	public @Nullable Long getActiveEnergyExported() {
		Long v = delegate.getActiveEnergyExported();
		return (v != null ? -v : null);
	}

	@Override
	public @Nullable Long getActiveEnergyImported() {
		Long v = delegate.getActiveEnergyImported();
		return (v != null ? -v : null);
	}

	@Override
	public @Nullable Long getReactiveEnergyExported() {
		Long v = delegate.getReactiveEnergyExported();
		return (v != null ? -v : null);
	}

	@Override
	public @Nullable Long getReactiveEnergyImported() {
		Long v = delegate.getReactiveEnergyImported();
		return (v != null ? -v : null);
	}

	@Override
	public int getModelLength() {
		return delegate.getModelLength();
	}

	@Override
	public @Nullable Float getDcCurrent() {
		Float v = delegate.getDcCurrent();
		return (v != null ? -v : null);
	}

	@Override
	public @Nullable Float getDcVoltage() {
		return delegate.getDcVoltage();
	}

	@Override
	public @Nullable Integer getDcPower() {
		Integer v = delegate.getDcPower();
		return (v != null ? -v : null);
	}

	@Override
	public @Nullable Float getCabinetTemperature() {
		return delegate.getCabinetTemperature();
	}

	@Override
	public @Nullable Float getHeatSinkTemperature() {
		return delegate.getHeatSinkTemperature();
	}

	@Override
	public @Nullable Float getTransformerTemperature() {
		return delegate.getTransformerTemperature();
	}

	@Override
	public @Nullable Float getOtherTemperature() {
		return delegate.getOtherTemperature();
	}

	@Override
	public @Nullable OperatingState getOperatingState() {
		return delegate.getOperatingState();
	}

	@Override
	public @Nullable Integer getVendorOperatingState() {
		return delegate.getVendorOperatingState();
	}

	@Override
	public Set<? extends ModelEvent> getEvents() {
		return delegate.getEvents();
	}

	@Override
	public @Nullable BitSet getVendorEvents() {
		return delegate.getVendorEvents();
	}

}
