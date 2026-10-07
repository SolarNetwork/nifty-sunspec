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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.AcPhase;
import net.solarnetwork.sunspec.api.IntRange;
import net.solarnetwork.sunspec.api.ModelEvent;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * A "reversed" meter model accessor that swaps import/export values.
 *
 * <p>
 * Active and reactive power values are negated, and imported and exported
 * energy values are swapped, with reactive energy quadrants 1 and 3, and 2 and
 * 4, swapped. The point values of {@link #getPointValue(ModbusReference)} are
 * reversed in the same way.
 * </p>
 *
 * @author matt
 * @version 1.0
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
	public @Nullable BigDecimal getActivePower() {
		BigDecimal n = delegate.getActivePower();
		return (n != null ? n.negate() : null);
	}

	@Override
	public @Nullable BigDecimal getApparentPower() {
		return delegate.getApparentPower();
	}

	@Override
	public @Nullable BigDecimal getReactivePower() {
		BigDecimal n = delegate.getReactivePower();
		return (n != null ? n.negate() : null);
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
	public @Nullable BigDecimal getActiveEnergyImported() {
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
	public @Nullable BigDecimal getActiveEnergyExported() {
		return delegate.getActiveEnergyImported();
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyImported() {
		return delegate.getReactiveEnergyExported();
	}

	@Override
	public int getModelLength() {
		return delegate.getModelLength();
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyExported() {
		return delegate.getReactiveEnergyImported();
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyImportedQ1() {
		return delegate.getReactiveEnergyExportedQ3();
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyImportedQ2() {
		return delegate.getReactiveEnergyExportedQ4();
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyExportedQ3() {
		return delegate.getReactiveEnergyImportedQ1();
	}

	@Override
	public @Nullable BigDecimal getReactiveEnergyExportedQ4() {
		return delegate.getReactiveEnergyImportedQ2();
	}

	@Override
	public @Nullable BigDecimal getApparentEnergyImported() {
		return delegate.getApparentEnergyExported();
	}

	@Override
	public @Nullable BigDecimal getApparentEnergyExported() {
		return delegate.getApparentEnergyImported();
	}

	@Override
	public Set<? extends ModelEvent> getEvents() {
		return delegate.getEvents();
	}

	@Override
	public Collection<? extends ModbusReference> getPointReferences() {
		return delegate.getPointReferences();
	}

}
