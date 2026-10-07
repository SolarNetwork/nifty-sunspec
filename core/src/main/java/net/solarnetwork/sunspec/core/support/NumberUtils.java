/* ==================================================================
 * NumberUtils.java - 15/03/2018 2:49:15 PM
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

package net.solarnetwork.sunspec.core.support;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.jspecify.annotations.Nullable;

/**
 * Utilities for dealing with numbers.
 *
 * @author matt
 * @version 1.0
 */
public final class NumberUtils {

	private NumberUtils() {
		// not available
	}

	/**
	 * Get a {@link BigDecimal} for a number.
	 *
	 * <p>
	 * If {@code value} is already a {@link BigDecimal} it will be returned
	 * directly. Otherwise a new {@link BigDecimal} instance will be created out
	 * of {@code value}.
	 * </p>
	 *
	 * @param value
	 *        the number to get a {@code BigDecimal} version of
	 * @return the {@code BigDecimal} version of {@code value}, or {@code null}
	 *         if {@code value} is {@code null}
	 */
	public static @Nullable BigDecimal bigDecimalForNumber(@Nullable Number value) {
		BigDecimal v = null;
		if ( value == null ) {
			return null;
		} else if ( value instanceof BigDecimal d ) {
			v = d;
		} else if ( value instanceof Long ) {
			v = new BigDecimal(value.longValue());
		} else if ( value instanceof Integer || value instanceof Short ) {
			v = new BigDecimal(value.intValue());
		} else if ( value instanceof Double ) {
			v = BigDecimal.valueOf(value.doubleValue());
		} else if ( value instanceof BigInteger i ) {
			v = new BigDecimal(i);
		} else {
			// note Float falls through to here per recommended way of converting that to BigDecimal
			v = new BigDecimal(value.toString());
		}
		return v;
	}

	/**
	 * Get a {@link BigInteger} for a number.
	 *
	 * <p>
	 * If {@code value} is already a {@link BigInteger} it will be returned
	 * directly. Otherwise a new {@link BigInteger} instance will be created out
	 * of {@code value}.
	 * </p>
	 *
	 * @param value
	 *        the number to get a {@code BigInteger} version of
	 * @return the {@code BigInteger} version of {@code value}, or {@code null}
	 *         if {@code value} is {@code null}
	 */
	public static @Nullable BigInteger bigIntegerForNumber(@Nullable Number value) {
		BigInteger v = null;
		if ( value == null ) {
			return null;
		} else if ( value instanceof BigInteger i ) {
			v = i;
		} else if ( value instanceof BigDecimal d ) {
			v = d.toBigInteger();
		} else {
			v = new BigDecimal(value.toString()).toBigInteger();
		}
		return v;
	}

	/**
	 * Scale a number by a power of 10.
	 *
	 * @param num
	 *        the number to scale
	 * @param scale
	 *        the power of 10 to scale by; a negative value shifts the decimal
	 *        point left this many places; a positive value shifts the decimal
	 *        point right this many places
	 * @return the scaled value, or {@code null} if {@code num} is {@code null}
	 */
	public static @Nullable BigDecimal scaled(@Nullable final Number num, int scale) {
		if ( num == null ) {
			return null;
		}
		final BigDecimal n = bigDecimalForNumber(num);
		if ( n == null || scale == 0 ) {
			return n;
		} else if ( scale < 0 ) {
			return n.movePointLeft(-scale);
		} else {
			return n.movePointRight(scale);
		}
	}

	/**
	 * Apply a maximum decimal scale to a number value.
	 *
	 * @param value
	 *        the number to apply the maximum scale to
	 * @param maxDecimalScale
	 *        the maximum scale, or {@literal -1} for no maximum
	 * @return the value, rounded half-up to {@code maxDecimalScale} decimal
	 *         digits if necessary, or {@code null} if {@code value} is
	 *         {@code null}
	 */
	public static @Nullable BigDecimal maximumDecimalScale(@Nullable Number value, int maxDecimalScale) {
		BigDecimal d = bigDecimalForNumber(value);
		if ( d != null && maxDecimalScale >= 0 && d.scale() > maxDecimalScale ) {
			d = d.setScale(maxDecimalScale, RoundingMode.HALF_UP);
		}
		return d;
	}

}
