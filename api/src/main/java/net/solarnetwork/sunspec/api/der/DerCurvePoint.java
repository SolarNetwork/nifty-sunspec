/* ==================================================================
 * DerCurvePoint.java - 5/10/2026 4:58:20 pm
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

package net.solarnetwork.sunspec.api.der;

/**
 * A point on a DER piece-wise linear curve.
 *
 * <p>
 * The meaning and units of each coordinate depend on the curve model, as
 * described by each model's accessor API. A coordinate that is not available is
 * {@link Float#NaN}.
 * </p>
 *
 * @param x
 *        the x coordinate
 * @param y
 *        the y coordinate
 * @author matt
 * @version 1.0
 */
public record DerCurvePoint(float x, float y) {

}
