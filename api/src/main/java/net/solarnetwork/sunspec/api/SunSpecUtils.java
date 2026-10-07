/* ==================================================================
 * SunSpecUtils.java - 6 Oct 2026 10:09:54 am
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

package net.solarnetwork.sunspec.api;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;

/**
 * General SunSpec helper methods.
 *
 * @author matt
 * @version 1.0
 */
public final class SunSpecUtils {

	/**
	 * A pattern for a name that ends with a phase, such as {@code PhaseA},
	 * {@code PhaseANeutral}, or {@code PhaseAPhaseB}.
	 */
	private static final Pattern PHASE_SUFFIX = Pattern.compile("Phase([ABC])(Neutral|Phase([ABC]))?$");

	private SunSpecUtils() {
		// not available
	}

	/**
	 * Convert a point name to lower camel case.
	 *
	 * <p>
	 * The leading upper case letters of {@code name} are changed to lower case,
	 * except for the last one when it starts a word, so {@code ActivePower}
	 * becomes {@code activePower}, {@code GHI} becomes {@code ghi}, and
	 * {@code DCVoltage} becomes {@code dcVoltage}.
	 * </p>
	 *
	 * @param name
	 *        the name to convert
	 * @return the name in lower camel case
	 */
	public static String lowerCamelCase(String name) {
		final int len = name.length();
		int upper = 0;
		while ( upper < len && Character.isUpperCase(name.charAt(upper)) ) {
			upper++;
		}
		if ( upper == 0 ) {
			return name;
		}
		if ( upper > 1 && upper < len && Character.isLowerCase(name.charAt(upper)) ) {
			// keep the start of the next word
			upper--;
		}
		return name.substring(0, upper).toLowerCase(Locale.ROOT) + name.substring(upper);
	}

	/**
	 * Replace a phase at the end of a point name with an {@link AcPhase} key
	 * suffix.
	 *
	 * <p>
	 * A name that ends with {@code PhaseA} or {@code PhaseANeutral} gets the
	 * {@link AcPhase#withKey(String)} suffix, so {@code CurrentPhaseA} becomes
	 * {@code Current_a}. A name that ends with a line phase, such as
	 * {@code PhaseAPhaseB}, gets the {@link AcPhase#withLineKey(String)}
	 * suffix, so {@code VoltagePhaseAPhaseB} becomes {@code Voltage_ab}. Other
	 * names are returned unchanged.
	 * </p>
	 *
	 * @param name
	 *        the name to convert
	 * @return the name with any phase replaced by a key suffix
	 */
	public static String phaseSuffix(String name) {
		final Matcher m = PHASE_SUFFIX.matcher(name);
		if ( !m.find() ) {
			return name;
		}
		final AcPhase phase = AcPhase.forKey(Character.toLowerCase(m.group(1).charAt(0)));
		final String prefix = name.substring(0, m.start());
		final String linePhase = m.group(3);
		if ( linePhase == null ) {
			return phase.withKey(prefix);
		}
		final String lineKey = phase.getLineKey();
		if ( lineKey.charAt(1) != Character.toLowerCase(linePhase.charAt(0)) ) {
			// not a line phase pair with a key
			return name;
		}
		return phase.withLineKey(prefix);
	}

	/**
	 * Get the default point map key for a point name.
	 *
	 * <p>
	 * This applies {@link #lowerCamelCase(String)} and then
	 * {@link #phaseSuffix(String)}, so {@code VoltagePhaseAPhaseB} becomes
	 * {@code voltage_ab}.
	 * </p>
	 *
	 * @param name
	 *        the point name
	 * @return the key
	 */
	public static String pointKey(String name) {
		return phaseSuffix(lowerCamelCase(name));
	}

	/**
	 * Get a bitfield data property value as a set of enumeration values.
	 *
	 * <p>
	 * SunSpec bitfields never have their most significant bit set, so a value
	 * with that bit set, including the SunSpec "not implemented" value, is
	 * returned as an empty set. Bits without a corresponding enumeration value
	 * are ignored.
	 * </p>
	 *
	 * @param <T>
	 *        the enumeration type
	 * @param n
	 *        the bitmask number value
	 * @param wordLength
	 *        the number of 16-bit words the bitfield is comprised of
	 * @param type
	 *        the enumeration type
	 * @return the values, never {@code null}
	 */
	public static <T extends Enum<T> & Bitmaskable> Set<T> bitfieldValues(final @Nullable Number n,
			final int wordLength, final Class<T> type) {
		if ( n == null ) {
			return Collections.emptySet();
		}
		final long v = n.longValue();
		if ( (v & (1L << (wordLength * 16 - 1))) != 0 ) {
			return Collections.emptySet();
		}
		return Bitmaskable.setForBitmask((int) v, type);
	}

}
