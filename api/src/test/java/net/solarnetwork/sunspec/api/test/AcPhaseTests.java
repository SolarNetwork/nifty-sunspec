/* ==================================================================
 * AcPhaseTests.java - 7/10/2026 5:44:52 pm
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

package net.solarnetwork.sunspec.api.test;

import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import org.junit.jupiter.api.Test;
import net.solarnetwork.sunspec.api.AcPhase;

/**
 * Test cases for the {@link AcPhase} class.
 *
 * @author matt
 * @version 1.0
 */
public class AcPhaseTests {

	@Test
	public void keys() {
		// @formatter:off
		then(AcPhase.PhaseA)
			.as("Phase A key")
			.returns('a', from(AcPhase::getKey))
			.as("Phase A line key")
			.returns("ab", from(AcPhase::getLineKey))
			.as("Phase A key suffix")
			.returns("voltage_a", from(p -> p.withKey("voltage")))
			.as("Phase A line key suffix")
			.returns("voltage_ab", from(p -> p.withLineKey("voltage")))
			;
		then(AcPhase.PhaseB)
			.as("Phase B key")
			.returns('b', from(AcPhase::getKey))
			.as("Phase B line key")
			.returns("bc", from(AcPhase::getLineKey))
			;
		then(AcPhase.PhaseC)
			.as("Phase C key")
			.returns('c', from(AcPhase::getKey))
			.as("Phase C line key")
			.returns("ca", from(AcPhase::getLineKey))
			;
		then(AcPhase.Total)
			.as("Total key")
			.returns('t', from(AcPhase::getKey))
			.as("Total line key")
			.returns("t", from(AcPhase::getLineKey))
			;
		// @formatter:on
	}

	@Test
	public void forKey() {
		for ( AcPhase phase : AcPhase.values() ) {
			then(AcPhase.forKey(phase.getKey())).as("Phase for key %s", phase.getKey()).isSameAs(phase);
		}
	}

	@Test
	public void forKey_invalid() {
		// @formatter:off
		thenThrownBy(() -> AcPhase.forKey('x'))
			.as("Invalid key rejected")
			.isInstanceOf(IllegalArgumentException.class)
			;
		// @formatter:on
	}

}
