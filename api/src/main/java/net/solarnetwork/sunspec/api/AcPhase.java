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
	PhaseA(1),

	/** The second phase. */
	PhaseB(2),

	/** The third phase. */
	PhaseC(3),

	/** An aggregate of all phases. */
	Total(0);

	private final int number;

	private AcPhase(int n) {
		this.number = n;
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

}
