/* ==================================================================
 * ModbusData.java - 20/12/2017 7:12:16 AM
 *
 * Copyright 2017 SolarNetwork.net Dev Team
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

package net.solarnetwork.sunspec.modbus.support;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.core.support.IntShortMap;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;

/**
 * Object to hold raw data extracted from a Modbus device.
 *
 * <p>
 * This class is designed to operate as a cache of data read from a Modbus
 * device. The data is modeled as a sparse array of register address keys with
 * associated 16-bit values. It supports thread-safe write access to the saved
 * data and thread-safe read access if {@link #ModbusData(ModbusData)} or
 * {@link #copy()} are invoked to get a copy of the data.
 * </p>
 *
 * @author matt
 * @version 1.0
 */
public class ModbusData {

	private final IntShortMap dataRegisters;
	private long dataTimestamp = 0;

	/**
	 * Default constructor.
	 */
	public ModbusData() {
		this(false);
	}

	/**
	 * Constructor.
	 *
	 * @param strictAddresses
	 *        if {@code true} then throw {@code NoSuchElementException} when
	 *        attempting to read from an address that does not already have an
	 *        associated value
	 */
	public ModbusData(boolean strictAddresses) {
		super();
		this.dataRegisters = new IntShortMap(64,
				strictAddresses ? IntShortMap.VALUE_NO_SUCH_ELEMENT : (short) 0);
	}

	/**
	 * Copy constructor.
	 *
	 * <p>
	 * This method provides a thread-safe way to get a copy of the current data.
	 * </p>
	 *
	 * @param other
	 *        the object to copy
	 */
	public ModbusData(ModbusData other) {
		synchronized ( other.dataRegisters ) {
			this.dataRegisters = other.dataRegisters.clone();
			this.dataTimestamp = other.dataTimestamp;
		}
	}

	/**
	 * Gets the time stamp of the data.
	 *
	 * @return the data time stamp, or {@code null} if no data has been
	 *         collected yet
	 */
	public @Nullable Instant getDataTimestamp() {
		return dataTimestamp > 0 ? Instant.ofEpochMilli(dataTimestamp) : null;
	}

	/**
	 * Create a copy of this object.
	 *
	 * <p>
	 * This method provides a thread-safe way to get a copy of the current data.
	 * </p>
	 *
	 * @return the new instance
	 * @see #ModbusData(ModbusData)
	 */
	public ModbusData copy() {
		return new ModbusData(this);
	}

	/**
	 * Get a number value from a relative reference.
	 *
	 * @param ref
	 *        the relative reference to get the number value for
	 * @param offset
	 *        the address offset to add to {@link ModbusReference#getAddress()}
	 * @return the value, or {@code null} if {@code ref} is {@code null}
	 * @throws IllegalArgumentException
	 *         if the reference data type is not numeric
	 */
	public final @Nullable Number getNumber(@Nullable ModbusReference ref, int offset) {
		if ( ref == null ) {
			return null;
		}
		ModbusDataType type = ref.getDataType();
		if ( type == null ) {
			type = ModbusDataType.UInt16;
		}
		final int addr = ref.getAddress() + offset;
		return switch (type) {
			case Boolean -> getBoolean(addr) ? 1 : 0;
			case Float32 -> getFloat32(addr);
			case Float64 -> getFloat64(addr);
			case Int16 -> getInt16(addr);
			case Int32 -> getInt32(addr);
			case Int64 -> getInt64(addr);
			case UInt16 -> getUnsignedInt16(addr);
			case UInt32 -> getUnsignedInt32(addr);
			case UInt64 -> getUnsignedInt64(addr);
			default -> throw new IllegalArgumentException(
					"Cannot get number for " + type + " type reference");
		};
	}

	/**
	 * Construct a 1-bit boolean from a data register address.
	 *
	 * @param addr
	 *        the address
	 * @return the boolean, never {@code null}
	 */
	public final Boolean getBoolean(final int addr) {
		short s = dataRegisters.getValue(addr);
		return (s != 0);
	}

	/**
	 * Construct an unsigned 16-bit integer from a data register address.
	 *
	 * @param addr
	 *        the register address
	 * @return the integer, never {@code null}
	 */
	public final Integer getUnsignedInt16(final int addr) {
		short s = dataRegisters.getValue(addr);
		return s & 0xFFFF;
	}

	/**
	 * Construct a signed 16-bit integer from a data register address.
	 *
	 * @param addr
	 *        the register address
	 * @return the short, never {@code null}
	 */
	public final Short getInt16(final int addr) {
		return dataRegisters.getValue(addr);
	}

	/**
	 * Construct an unsigned 32-bit integer from data register addresses.
	 *
	 * @param hiAddr
	 *        the address of the high 16 bits
	 * @param loAddr
	 *        the address of the low 16 bits
	 * @return the parsed value, or {@code null} if not available
	 */
	public final @Nullable Long getUnsignedInt32(final int hiAddr, final int loAddr) {
		return ModbusDataUtils.parseUnsignedInt32(dataRegisters.getValue(hiAddr),
				dataRegisters.getValue(loAddr));
	}

	/**
	 * Construct a signed 32-bit integer from data register addresses.
	 *
	 * @param hiAddr
	 *        the address of the high 16 bits
	 * @param loAddr
	 *        the address of the low 16 bits
	 * @return the parsed value, or {@code null} if not available
	 */
	public final @Nullable Integer getInt32(final int hiAddr, final int loAddr) {
		return ModbusDataUtils.parseInt32(dataRegisters.getValue(hiAddr),
				dataRegisters.getValue(loAddr));
	}

	/**
	 * Construct a signed 32-bit integer from data register addresses.
	 *
	 * <p>
	 * The first register holds the most significant bits.
	 * </p>
	 *
	 * @param addr
	 *        the address of the first register; the second register is assumed
	 *        to be {@code addr + 1}
	 * @return the parsed value, or {@code null} if not available
	 */
	public final @Nullable Integer getInt32(final int addr) {
		return getInt32(addr, addr + 1);
	}

	/**
	 * Construct an unsigned 32-bit integer from a starting data register
	 * address.
	 *
	 * <p>
	 * The first register holds the most significant bits.
	 * </p>
	 *
	 * @param addr
	 *        the address of the first register; the second register is assumed
	 *        to be {@code addr + 1}
	 * @return the parsed value, or {@code null} if not available
	 */
	public final @Nullable Long getUnsignedInt32(final int addr) {
		return getUnsignedInt32(addr, addr + 1);
	}

	/**
	 * Construct a 32-bit float from data register addresses.
	 *
	 * @param hiAddr
	 *        the address of the high 16 bits
	 * @param loAddr
	 *        the address of the low 16 bits
	 * @return the parsed value, or {@code null} if not available.
	 */
	public final @Nullable Float getFloat32(final int hiAddr, final int loAddr) {
		return ModbusDataUtils.parseFloat32(dataRegisters.getValue(hiAddr),
				dataRegisters.getValue(loAddr));
	}

	/**
	 * Construct a 32-bit float from a starting data register address.
	 *
	 * <p>
	 * The first register holds the most significant bits.
	 * </p>
	 *
	 * @param addr
	 *        the address of the first register; the second register is assumed
	 *        to be {@code addr + 1}
	 * @return The parsed value, or {@code null} if not available.
	 */
	public final @Nullable Float getFloat32(final int addr) {
		return getFloat32(addr, addr + 1);
	}

	/**
	 * Construct a signed 64-bit long value from data register addresses.
	 *
	 * @param h1Addr
	 *        the address of bits 63-48
	 * @param h2Addr
	 *        the address of bits 47-32
	 * @param l1Addr
	 *        the address of bits 31-16
	 * @param l2Addr
	 *        the address of bits 15-0
	 * @return the parsed long
	 */
	public final Long getInt64(final int h1Addr, final int h2Addr, final int l1Addr, final int l2Addr) {
		return ModbusDataUtils.parseInt64(dataRegisters.getValue(h1Addr), dataRegisters.getValue(h2Addr),
				dataRegisters.getValue(l1Addr), dataRegisters.getValue(l2Addr));
	}

	/**
	 * Construct a signed 64-bit integer from a starting data register address.
	 *
	 * <p>
	 * The first register holds the most significant bits.
	 * </p>
	 *
	 * @param addr
	 *        the address of the first register; the remaining three registers
	 *        are assumed to be {@code addr + 1}, {@code addr + 2}, and
	 *        {@code addr + 3}
	 * @return the parsed value, or {@code null} if not available
	 */
	public final Long getInt64(final int addr) {
		return getInt64(addr, addr + 1, addr + 2, addr + 3);
	}

	/**
	 * Construct an unsigned 64-bit integer from data register addresses.
	 *
	 * @param h1Addr
	 *        the address of bits 63-48
	 * @param h2Addr
	 *        the address of bits 47-32
	 * @param l1Addr
	 *        the address of bits 31-16
	 * @param l2Addr
	 *        the address of bits 15-0
	 * @return the parsed value, or {@code null} if not available
	 */
	public final BigInteger getUnsignedInt64(final int h1Addr, final int h2Addr, final int l1Addr,
			final int l2Addr) {
		return ModbusDataUtils.parseUnsignedInt64(dataRegisters.getValue(h1Addr),
				dataRegisters.getValue(h2Addr), dataRegisters.getValue(l1Addr),
				dataRegisters.getValue(l2Addr));
	}

	/**
	 * Construct an unsigned 64-bit integer from data register addresses.
	 *
	 * <p>
	 * The first register holds the most significant bits.
	 * </p>
	 *
	 * @param addr
	 *        the address of the first register; the remaining three registers
	 *        are assumed to be {@code addr + 1}, {@code addr + 2}, and
	 *        {@code addr + 3}
	 * @return the parsed value, or {@code null} if not available
	 */
	public final BigInteger getUnsignedInt64(final int addr) {
		return getUnsignedInt64(addr, addr + 1, addr + 2, addr + 3);
	}

	/**
	 * Construct a 32-bit float from data register addresses.
	 *
	 * @param h1Addr
	 *        the address of bits 63-48
	 * @param h2Addr
	 *        the address of bits 47-32
	 * @param l1Addr
	 *        the address of bits 31-16
	 * @param l2Addr
	 *        the address of bits 15-0
	 * @return the parsed value, or {@code null} if not available
	 */
	public final @Nullable Double getFloat64(final int h1Addr, final int h2Addr, final int l1Addr,
			final int l2Addr) {
		return ModbusDataUtils.parseFloat64(dataRegisters.getValue(h1Addr),
				dataRegisters.getValue(h2Addr), dataRegisters.getValue(l1Addr),
				dataRegisters.getValue(l2Addr));
	}

	/**
	 * Construct a 32-bit float from a starting data register address.
	 *
	 * <p>
	 * The first register holds the most significant bits.
	 * </p>
	 *
	 * @param addr
	 *        the address of the first register; the remaining three registers
	 *        are assumed to be {@code addr + 1}, {@code addr + 2}, and
	 *        {@code addr + 3}
	 * @return The parsed value, or {@code null} if not available.
	 */
	public final @Nullable Double getFloat64(final int addr) {
		return getFloat64(addr, addr + 1, addr + 2, addr + 3);
	}

	/**
	 * Construct a byte array out of a data address range.
	 *
	 * <p>
	 * The registers are read in address order, with the high byte of each
	 * register first.
	 * </p>
	 *
	 * @param addr
	 *        the starting address of the 16-bit register to read
	 * @param count
	 *        the number of 16-bit registers to read
	 * @return the byte array, which will have a length of {@code count * 2}
	 */
	public byte[] getBytes(final int addr, final int count) {
		byte[] result = new byte[count * 2];
		for ( int i = addr, end = addr + count, index = 0; i < end; i++, index += 2 ) {
			short word = dataRegisters.getValue(i);
			result[index] = (byte) ((word >> 8) & 0xFF);
			result[index + 1] = (byte) (word & 0xFF);
		}
		return result;
	}

	/**
	 * Perform a set of updates to saved register data.
	 *
	 * @param action
	 *        the callback to perform the updates on
	 * @return this object to allow method chaining
	 * @throws IOException
	 *         if any communication error occurs
	 */
	public final ModbusData performUpdates(ModbusDataUpdateAction action) throws IOException {
		synchronized ( dataRegisters ) {
			final long now = System.currentTimeMillis();
			if ( action.updateModbusData(new MutableModbusDataView(dataRegisters)) ) {
				dataTimestamp = now;
			}
		}
		return this;
	}

	/**
	 * API for performing updates to the data.
	 */
	public static interface MutableModbusData {

		/**
		 * Store an array of 16-bit integer register data values, starting at a
		 * given address.
		 *
		 * @param data
		 *        the data array to save, as shorts
		 * @param addr
		 *        the starting address of the data
		 */
		public void saveDataArray(final short @Nullable [] data, int addr);

		/**
		 * Store an array of 16-bit integer register data values, starting at a
		 * given address.
		 *
		 * <p>
		 * Note that the data values will be treated as 16-bit unsigned values.
		 * </p>
		 *
		 * @param data
		 *        the data array to save, as ints
		 * @param addr
		 *        the starting address of the data
		 */
		public void saveDataArray(final int @Nullable [] data, int addr);
	}

	/**
	 * API for performing updates to the saved data.
	 */
	public static interface ModbusDataUpdateAction {

		/**
		 * Perform updates to the data.
		 *
		 * @param m
		 *        a mutable version of the data to update
		 * @return {@literal true} if {@code dataTimestamp} should be updated to
		 *         the current time
		 * @throws IOException
		 *         if any communication error occurs
		 */
		public boolean updateModbusData(MutableModbusData m) throws IOException;
	}

	/**
	 * Mutable view of Modbus data registers, meant to be used for thread-safe
	 * writes.
	 *
	 * <p>
	 * All methods are assumed to be synchronized on {@code dataRegsiters}.
	 * </p>
	 */
	public static class MutableModbusDataView implements MutableModbusData {

		private final IntShortMap dataRegisters;

		/**
		 * Construct with data registers to mutate.
		 *
		 * @param dataRegisters
		 *        the registers to mutate; calling code should by synchronized
		 *        on this instance
		 */
		public MutableModbusDataView(IntShortMap dataRegisters) {
			super();
			this.dataRegisters = dataRegisters;
		}

		@Override
		public final void saveDataArray(final short @Nullable [] data, int addr) {
			if ( data == null || data.length < 1 ) {
				return;
			}
			for ( short v : data ) {
				dataRegisters.putValue(addr, v);
				addr++;
			}
		}

		@Override
		public final void saveDataArray(final int @Nullable [] data, int addr) {
			if ( data == null || data.length < 1 ) {
				return;
			}
			for ( int v : data ) {
				dataRegisters.putValue(addr, (short) (v & 0xFFFF));
				addr++;
			}
		}

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModbusData{dataTimestamp=");
		builder.append(dataTimestamp);
		builder.append(", ");
		if ( dataRegisters != null ) {
			builder.append("dataRegisters=");
			builder.append(dataRegisters);
		}
		builder.append("}");
		return builder.toString();
	}

	/**
	 * Get a string of data values, useful for debugging.
	 *
	 * <p>
	 * The generated string will contain a register address followed by two
	 * register values per line, printed as hexidecimal integers, with a prefix
	 * and suffix line. For example:
	 * </p>
	 *
	 * <pre>
	 * ModbusData{
	 *      30000: 0x4141, 0x727E
	 *      30006: 0xFFC0, 0x0000
	 *      ...
	 *      30344: 0x0000, 0x0000
	 * }
	 * </pre>
	 *
	 * @return debug string
	 */
	public final String dataDebugString() {
		final StringBuilder buf = new StringBuilder(getClass().getSimpleName()).append("{");
		if ( !dataRegisters.isEmpty() ) {
			final int[] last = new int[] { -2 };
			dataRegisters.forEachOrdered((k, v) -> {
				boolean odd = k % 2 == 1 ? true : false;
				if ( k > last[0] + 1 ) {
					int rowAddr = odd ? k - 1 : k;
					buf.append("\n\t").append(String.format("%5d", rowAddr)).append(": ");
					if ( odd ) {
						// fill in empty space for start of row
						buf.append("      , ");
					}
					last[0] = k;
					if ( odd ) {
						last[0] -= 1;
					}
				} else if ( odd ) {
					buf.append(", ");
				}
				buf.append(String.format("0x%04X", v));
			});
			buf.append("\n");
		}
		buf.append("}");
		return buf.toString();
	}

	/**
	 * Get direct access to all modbus registers.
	 *
	 * @return the data map, never {@code null}
	 */
	public final IntShortMap dataRegisters() {
		return dataRegisters;
	}

}
