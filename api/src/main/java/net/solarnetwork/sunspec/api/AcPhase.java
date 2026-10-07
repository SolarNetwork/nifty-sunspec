/* ==================================================================
 * AcPhase.java - Apr 2, 2014 10:05:08 AM
 *
 * Copyright 2007-2014 SolarNetwork.net Dev Team
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

/**
 * Enumeration of AC phase values.
 *
 * @author matt
 * @version 1.0
 */
public enum AcPhase {

	/** The first phase. */
	PhaseA(1, 'a'),

	/** The second phase. */
	PhaseB(2, 'b'),

	/** The third phase. */
	PhaseC(3, 'c'),

	/** An aggregate of all phases. */
	Total(0, 't');

	private final int number;
	private final char key;

	private AcPhase(int n, char key) {
		this.number = n;
		this.key = key;
	}

	/**
	 * Get the integer based value of the phase.
	 *
	 * <p>
	 * The {@code PhaseA}, {@code PhaseB}, and {@code PhaseC} phases are
	 * numbered <em>1</em>, <em>2</em>, and <em>3</em>. The {@code Total} phase
	 * is numbered <em>0</em>.
	 * </p>
	 *
	 * @return the phase number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Get the key value of the phase.
	 *
	 * <p>
	 * The keys are {@literal a}, {@literal b}, {@literal c}, and {@literal t}.
	 * </p>
	 *
	 * @return the key value
	 */
	public char getKey() {
		return key;
	}

	/**
	 * Get a string with a key suffix added.
	 *
	 * <p>
	 * This will take {@code value} and append <i>_P</i>, where {@literal P} is
	 * the key.
	 * </p>
	 *
	 * @param value
	 *        the value to append the key to
	 * @return the value with a key suffix added
	 */
	public String withKey(String value) {
		return value + '_' + key;
	}

	/**
	 * Get a key value for a line phase, with this phase as the leading phase.
	 *
	 * <p>
	 * The keys are {@literal ab}, {@literal bc}, {@literal ca}, and
	 * {@literal t}.
	 * </p>
	 *
	 * @return the line key
	 */
	public String getLineKey() {
		return switch (this) {
			case PhaseA -> "ab";
			case PhaseB -> "bc";
			case PhaseC -> "ca";
			case Total -> "t";
		};
	}

	/**
	 * Get a string with a line key suffix added.
	 *
	 * <p>
	 * This will take {@code value} and append <i>_P</i>, where {@literal P} is
	 * the line key.
	 * </p>
	 *
	 * @param value
	 *        the value to append the line key to
	 * @return the value with a line key suffix added
	 */
	public String withLineKey(String value) {
		return value + '_' + getLineKey();
	}

	/**
	 * Get an AcPhase for a given key.
	 *
	 * @param key
	 *        the key
	 * @return the AcPhase
	 * @see #getKey()
	 * @throws IllegalArgumentException
	 *         if the key is not a valid phase value
	 */
	public static AcPhase forKey(final char key) {
		return switch (key) {
			case 't' -> Total;
			case 'a' -> PhaseA;
			case 'b' -> PhaseB;
			case 'c' -> PhaseC;
			default -> throw new IllegalArgumentException("Key " + key + " is not a valid AcPhase");
		};
	}

}
