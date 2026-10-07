/* ==================================================================
 * ObjectUtils.java - 7/10/2021 10:14:12 AM
 *
 * Copyright 2021 SolarNetwork.net Dev Team
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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Utilities for dealing with objects.
 *
 * @author matt
 * @version 1.0
 */
public final class ObjectUtils {

	private ObjectUtils() {
		// not available
	}

	/**
	 * Require a non-null method argument.
	 *
	 * <p>
	 * This is similar to
	 * {@link java.util.Objects#requireNonNull(Object, String)} except
	 * {@code argumentName} is just the name of the required argument and an
	 * {@link IllegalArgumentException} is thrown instead of a
	 * {@code NullPointerException}. Example use:
	 * </p>
	 *
	 * <!-- @formatter:off -->
	 * <blockquote><pre>
	 * public Foo(Bar bar, Baz baz) {
	 * 	this.bar = ObjectUtils.requireNonNullArgument(bar, "bar");
	 * 	this.baz = ObjectUtils.requireNonNullArgument(baz, "baz");
	 * }
	 * </pre></blockquote>
	 * <!-- @formatter:on -->
	 *
	 * @param <T>
	 *        the argument type
	 * @param arg
	 *        the argument to require to be non-null
	 * @param argumentName
	 *        the name of {@code arg} to report in the
	 *        {@link IllegalArgumentException} if {@code arg} is {@code null}
	 * @return {@code arg}
	 * @throws IllegalArgumentException
	 *         if {@code arg} is {@code null}
	 */
	public static <T> @NonNull T requireNonNullArgument(final @Nullable T arg, final String argumentName)
			throws IllegalArgumentException {
		if ( arg == null ) {
			throw new IllegalArgumentException(
					String.format("The %s argument must not be null.", argumentName));
		}
		return arg;
	}

	/**
	 * Assert a property is non-null.
	 *
	 * <p>
	 * This is similar to {@link #requireNonNullArgument(Object, String)} but an
	 * {@link IllegalStateException} is thrown instead of a
	 * {@link IllegalArgumentException}. Example use:
	 * </p>
	 *
	 * <!-- @formatter:off -->
	 * <blockquote><pre>
	 * ObjectUtils.nonnull(bar, "BarService").tap();
	 * </pre></blockquote>
	 * <!-- @formatter:on -->
	 *
	 * <p>
	 * The exception message would be {@code "BarService is not available."}.
	 * </p>
	 *
	 * <p>
	 * This is intended to help with null static analysis where we can reason
	 * that a property can not be null but static analysis is unable to (and
	 * would raise an error).
	 * </p>
	 *
	 * @param <T>
	 *        the argument type
	 * @param prop
	 *        the value to require to be non-null
	 * @param name
	 *        the name of {@code prop} to report in the
	 *        {@link IllegalStateException} if {@code prop} is {@code null}
	 * @return {@code prop}
	 * @throws IllegalStateException
	 *         if {@code prop} is {@code null}
	 */
	public static <T> @NonNull T nonnull(final @Nullable T prop, final String name)
			throws IllegalStateException {
		if ( prop == null ) {
			throw new IllegalStateException("%s is not available.".formatted(name));
		}
		return prop;
	}

}
