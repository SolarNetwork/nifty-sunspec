/* ==================================================================
 * ModbusConstants.java - 6 Oct 2026 7:10:01 pm
 *
 * Copyright 2026 SolarNetwork.net Dev Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * ==================================================================
 */

package net.solarnetwork.sunspec.modbus;

/**
 * SunSpec Modbus constant values.
 *
 * @author matt
 * @version 1.0
 */
public final class ModbusConstants {

	/** The "not implemented" value for a SunSpec "int16" data type. */
	public static final int NAN_INT16 = 0x8000;
	/** The "not implemented" value for a SunSpec "uint16" data type. */
	public static final int NAN_UINT16 = 0xFFFF;
	/** The "not accumulated" value for a SunSpec "acc16" data type. */
	public static final int NAN_ACC16 = 0x0000;
	/** The "not implemented" value for a SunSpec "enum16" data type. */
	public static final int NAN_ENUM16 = 0xFFFF;
	/** The "not implemented" value for a SunSpec "bitfield16" data type. */
	public static final int NAN_BITFIELD16 = 0xFFFF;
	/** The "not implemented" value for a SunSpec "int32" data type. */
	public static final int NAN_INT32 = 0x80000000;
	/** The "not implemented" value for a SunSpec "uint32" data type. */
	public static final long NAN_UINT32 = 0xFFFFFFFFL;
	/** The "not accumulated" value for a SunSpec "acc32" data type. */
	public static final long NAN_ACC32 = 0x00000000;
	/** The "not implemented" value for a SunSpec "enum32" data type. */
	public static final long NAN_ENUM32 = 0xFFFFFFFFL;
	/** The "not implemented" value for a SunSpec "bitfield32" data type. */
	public static final long NAN_BITFIELD32 = 0xFFFFFFFFL;
	/** The "not implemented" value for a SunSpec "int64" data type. */
	public static final long NAN_INT64 = 0x8000000000000000L;
	/** The "not accumulated" value for a SunSpec "acc64" data type. */
	public static final long NAN_ACC64 = 0x0000000000000000L;
	/** The "not implemented" value for a SunSpec "float32" data type. */
	public static final float NAN_FLOAT32 = Float.NaN;
	/**
	 * The "not implemented" value for a SunSpec "sunssf" (scale factor) data
	 * type.
	 */
	public static final int NAN_SUNSSF16 = 0x8000;
	/**
	 * The "not implemented" value for a SunSpec "uint64" data type.
	 *
	 * @since 2.5
	 */
	public static final long NAN_UINT64 = 0xFFFFFFFFFFFFFFFFL;

	private ModbusConstants() {
		// not available
	}

}
