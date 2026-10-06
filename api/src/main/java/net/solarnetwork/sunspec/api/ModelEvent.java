/* ==================================================================
 * ModelEvent.java - 22/05/2018 6:09:33 AM
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

package net.solarnetwork.sunspec.api;

import java.util.Set;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.domain.Bitmaskable;

/**
 * API for a model event.
 *
 * @author matt
 * @version 1.2
 */
public interface ModelEvent extends Bitmaskable {

	/**
	 * Get the event bitmask index.
	 *
	 * @return the bitmask index
	 */
	int getIndex();

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This implementation returns {@link #getIndex()}.
	 * </p>
	 *
	 * @since 1.2
	 */
	@Override
	default int bitmaskBitOffset() {
		return getIndex();
	}

	/**
	 * Get a description of the event.
	 *
	 * @return a description
	 */
	String getDescription();

	/**
	 * Get a SunSpec "bitfield16" value from a set of events.
	 *
	 * @param events
	 *        the events to get the bit field value for; can be {@code null}
	 * @return the bit field value
	 * @since 1.1
	 */
	static int bitField16Value(@Nullable Set<? extends ModelEvent> events) {
		return (int) (bitField32Value(events) & 0xFFFF);
	}

	/**
	 * Get a SunSpec "bitfield32" value from a set of events.
	 *
	 * @param events
	 *        the events to get the bit field value for; can be {@code null}
	 * @return the bit field value
	 * @since 1.1
	 */
	static long bitField32Value(@Nullable Set<? extends ModelEvent> events) {
		long b = 0;
		if ( events != null ) {
			for ( ModelEvent event : events ) {
				int idx = event.getIndex();
				b |= (1 << idx);
			}
		}
		return b;
	}

}
