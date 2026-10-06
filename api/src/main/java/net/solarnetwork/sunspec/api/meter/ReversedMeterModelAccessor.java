/* ==================================================================
 * ReversedMeterModelAccessor.java - 14/03/2019 10:33:42 am
 *
 * Copyright 2019 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.api.meter;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.domain.AcPhase;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.util.IntRange;

/**
 * A "reversed" meter model accessor that swaps import/export values.
 *
 * @author matt
 * @version 2.1
 * @since 1.3
 */
public class ReversedMeterModelAccessor implements MeterModelAccessor {

	private final MeterModelAccessor delegate;

	/**
	 * Constructor.
	 *
	 * @param delegate
	 *        the accessor to reverse
	 */
	public ReversedMeterModelAccessor(MeterModelAccessor delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public MeterModelAccessor accessorForPhase(AcPhase phase) {
		return new ReversedMeterModelAccessor(delegate.accessorForPhase(phase));
	}

	@Override
	public MeterModelAccessor reversed() {
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
	public @Nullable Long getActiveEnergyImported() {
		return delegate.getActiveEnergyExported();
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
		return delegate.getActiveEnergyImported();
	}

	@Override
	public @Nullable Long getReactiveEnergyImported() {
		return delegate.getReactiveEnergyExported();
	}

	@Override
	public int getModelLength() {
		return delegate.getModelLength();
	}

	@Override
	public @Nullable Long getReactiveEnergyExported() {
		return delegate.getReactiveEnergyImported();
	}

	@Override
	public @Nullable Long getApparentEnergyImported() {
		return delegate.getApparentEnergyExported();
	}

	@Override
	public @Nullable Long getApparentEnergyExported() {
		return delegate.getApparentEnergyImported();
	}

	@Override
	public Set<? extends ModelEvent> getEvents() {
		return delegate.getEvents();
	}

}
