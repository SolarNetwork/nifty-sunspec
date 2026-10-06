/* ==================================================================
 * DataClassification.java - 8/10/2018 12:16:49 PM
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

/**
 * A classification applied to Modbus data types in the SunSpec data model.
 * 
 * @author matt
 * @version 1.0
 */
public enum DataClassification {

	/** An accumulator type. */
	Accumulator,

	/** A bitfield type. */
	Bitfield,

	/** An enumeration type. */
	Enumeration,

	/** A scale factor. */
	ScaleFactor;

}
