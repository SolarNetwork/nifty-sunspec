/* ==================================================================
 * BaseModelAccessor.java - 22/05/2018 10:39:06 AM
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

package net.solarnetwork.sunspec.core;

import static net.solarnetwork.sunspec.core.support.NumberUtils.bigDecimalForNumber;
import static net.solarnetwork.sunspec.core.support.NumberUtils.bigIntegerForNumber;
import static net.solarnetwork.sunspec.core.support.NumberUtils.maximumDecimalScale;
import static net.solarnetwork.sunspec.core.support.ObjectUtils.nonnull;
import static net.solarnetwork.sunspec.core.support.ObjectUtils.requireNonNullArgument;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.jspecify.annotations.Nullable;
import net.solarnetwork.sunspec.api.Bitmaskable;
import net.solarnetwork.sunspec.api.CodedValue;
import net.solarnetwork.sunspec.api.DataClassification;
import net.solarnetwork.sunspec.api.IntRange;
import net.solarnetwork.sunspec.api.ModelAccessor;
import net.solarnetwork.sunspec.api.ModelId;
import net.solarnetwork.sunspec.api.ModelRegister;
import net.solarnetwork.sunspec.api.PointAccess;
import net.solarnetwork.sunspec.api.SunSpecUtils;
import net.solarnetwork.sunspec.modbus.ModbusConnection;
import net.solarnetwork.sunspec.modbus.ModbusConstants;
import net.solarnetwork.sunspec.modbus.ModbusDataType;
import net.solarnetwork.sunspec.modbus.ModbusReference;
import net.solarnetwork.sunspec.modbus.ModbusWriteFunction;
import net.solarnetwork.sunspec.modbus.support.ModbusDataUtils;
import net.solarnetwork.sunspec.modbus.support.ModbusUtils;
import net.solarnetwork.sunspec.modbus.support.ModelData;

/**
 * Base class for {@link ModelAccessor} implementations.
 *
 * @author matt
 * @version 1.0
 */
public abstract class BaseModelAccessor implements ModelAccessor {

	/** Cached "not implemented" value for a SunSpec "uint64" data type. */
	private static final BigInteger NAN_UINT64 = new BigInteger(
			Long.toUnsignedString(ModbusConstants.NAN_UINT64));

	/** The largest valid SunSpec "uint64" value. */
	private static final BigInteger UINT64_MAX = new BigInteger("FFFFFFFFFFFFFFFE", 16);

	/** The smallest valid SunSpec "sunssf" value. */
	private static final int SCALE_FACTOR_MIN = -10;

	/** The largest valid SunSpec "sunssf" value. */
	private static final int SCALE_FACTOR_MAX = 10;

	private final ModelData data;
	private final int baseAddress;
	private final int blockAddress;
	private final ModelId modelId;

	/**
	 * Constructor.
	 *
	 * @param data
	 *        the overall data object
	 * @param baseAddress
	 *        the base address for this model's data
	 * @param modelId
	 *        the model ID
	 */
	public BaseModelAccessor(ModelData data, int baseAddress, ModelId modelId) {
		super();
		this.baseAddress = baseAddress;
		this.blockAddress = baseAddress + 2;
		this.data = requireNonNullArgument(data, "data");
		this.modelId = requireNonNullArgument(modelId, "modelId");
	}

	@Override
	public String toString() {
		return modelId.getDescription();
	}

	@Override
	public @Nullable Instant getDataTimestamp() {
		return data.getDataTimestamp();
	}

	@Override
	public int getBaseAddress() {
		return baseAddress;
	}

	@Override
	public int getBlockAddress() {
		return blockAddress;
	}

	@Override
	public ModelId getModelId() {
		return modelId;
	}

	@Override
	public int getModelLength() {
		Number n = data.getNumber(ModelRegister.ModelLength, baseAddress);
		return (n != null ? n.intValue() : 0);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This implementation returns the ranges of the multi-register
	 * {@link #getFixedBlockRegisters()} relative to the block address, and of
	 * the multi-register {@link #getRepeatingBlockRegisters()} relative to each
	 * repeating block instance.
	 * </p>
	 *
	 */
	@Override
	public List<IntRange> getUnsplittableAddressRanges() {
		final List<IntRange> result = new ArrayList<>(8);
		ModbusUtils.addMultiRegisterAddressRanges(result, blockAddress, getFixedBlockRegisters());
		final Collection<? extends ModbusReference> instanceRegisters = getRepeatingBlockRegisters();
		final int instanceLength = getRepeatingBlockInstanceLength();
		if ( !instanceRegisters.isEmpty() && instanceLength > 0 ) {
			final int count = getRepeatingBlockInstanceCount();
			int address = blockAddress + getFixedBlockLength();
			for ( int i = 0; i < count; i++, address += instanceLength ) {
				ModbusUtils.addMultiRegisterAddressRanges(result, address, instanceRegisters);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This implementation returns {@link #getFixedBlockRegisters()}.
	 * </p>
	 */
	@Override
	public Collection<? extends ModbusReference> getPointReferences() {
		return getFixedBlockRegisters();
	}

	/**
	 * Get the registers of the model fixed block.
	 *
	 * <p>
	 * These are used by {@link #getUnsplittableAddressRanges()}.
	 * </p>
	 *
	 * @return the registers, relative to the block address, never {@code null};
	 *         this implementation returns an empty list
	 */
	protected Collection<? extends ModbusReference> getFixedBlockRegisters() {
		return Collections.emptyList();
	}

	/**
	 * Get the registers of each model repeating block instance.
	 *
	 * <p>
	 * These are used by {@link #getUnsplittableAddressRanges()}, with the
	 * {@link #getRepeatingBlockInstanceLength()} and
	 * {@link #getRepeatingBlockInstanceCount()} values.
	 * </p>
	 *
	 * @return the registers, relative to the start of a repeating block
	 *         instance, never {@code null}; this implementation returns an
	 *         empty list
	 */
	protected Collection<? extends ModbusReference> getRepeatingBlockRegisters() {
		return Collections.emptyList();
	}

	/**
	 * Get the data.
	 *
	 * @return the data
	 */
	protected ModelData getData() {
		return data;
	}

	/**
	 * Get a scale factor's power of ten exponent.
	 *
	 * <p>
	 * SunSpec scale factors range from -10 to 10. Any other value, including
	 * the SunSpec "not implemented" value {@code 0x8000}, means the scale
	 * factor is not implemented.
	 * </p>
	 *
	 * @param ref
	 *        the block address relative reference to the scale factor register
	 * @param offset
	 *        the address offset to add to {@link ModbusReference#getAddress()}
	 * @return the exponent, or {@code null} if the scale factor is not
	 *         available or not implemented
	 */
	private @Nullable Integer scaleFactorExponent(ModbusReference ref, int offset) {
		final Number n = data.getNumber(ref, offset);
		if ( n == null ) {
			return null;
		}
		final int factor = n.intValue();
		return (factor < SCALE_FACTOR_MIN || factor > SCALE_FACTOR_MAX ? null : factor);
	}

	/**
	 * Get a bitfield register value.
	 *
	 * @param ref
	 *        the block address relative reference to the bitfield register(s)
	 * @return the value, never {@code null}
	 */
	protected @Nullable Number getBitfield(ModbusReference ref) {
		return getBitfield(ref, blockAddress);
	}

	/**
	 * Get a bitfield register value.
	 *
	 * @param ref
	 *        the block address relative reference to the bitfield register(s)
	 * @param offset
	 *        the address offset to add to {@link ModbusReference#getAddress()}
	 * @return the value, never {@code null}
	 */
	protected @Nullable Number getBitfield(ModbusReference ref, int offset) {
		Number v = data.getNumber(ref, offset);
		if ( v == null ) {
			return 0;
		}

		DataClassification classification = ref.getClassification();
		if ( DataClassification.Bitfield == classification ) {
			// for bit fields, if the most significant bit is set, it is NaN
			if ( ref.getWordLength() == 1 && (v.intValue()
					& ModbusConstants.NAN_BITFIELD16) == ModbusConstants.NAN_BITFIELD16 ) {
				return 0;
			} else if ( ref.getWordLength() == 2 && (v.intValue()
					& ModbusConstants.NAN_BITFIELD32) == ModbusConstants.NAN_BITFIELD32 ) {
				return 0;
			}
		}

		return v;
	}

	/**
	 * Get a scaled data property value.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param scaleRef
	 *        the block address relative reference to the scale factor
	 * @return the scaled value, or {@code null} if not available
	 */
	public @Nullable BigDecimal getScaledValue(ModbusReference dataRef, ModbusReference scaleRef) {
		return getScaledValue(dataRef, scaleRef, blockAddress, blockAddress);
	}

	/**
	 * Get a scaled data property value.
	 *
	 * <p>
	 * The value is not available if the scale factor is not implemented.
	 * SunSpec scale factors range from -10 to 10, and any other value,
	 * including the SunSpec "not implemented" value {@code 0x8000}, means the
	 * scale factor is not implemented. The value is exact for any data type and
	 * scale factor, and is normalized as described in
	 * {@link #normalized(BigDecimal)}.
	 * </p>
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param scaleRef
	 *        the block address relative reference to the scale factor
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @param scaleOffset
	 *        the scale address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the scaled value, or {@code null} if not available
	 */
	public @Nullable BigDecimal getScaledValue(ModbusReference dataRef, ModbusReference scaleRef,
			int dataOffset, int scaleOffset) {
		Number v = getValue(dataRef, dataOffset);
		if ( v == null ) {
			return null;
		}

		final Integer sf = scaleFactorExponent(scaleRef, scaleOffset);
		if ( sf == null ) {
			return null;
		}
		return normalized(nonnull(bigDecimalForNumber(v), "Decimal value").scaleByPowerOfTen(sf));
	}

	/**
	 * Normalize a decimal value.
	 *
	 * <p>
	 * Trailing zeros are removed, and the scale is never negative, so equal
	 * values are also {@link BigDecimal#equals(Object)} and whole numbers print
	 * without an exponent: for example {@code 1200} with a scale factor of
	 * {@literal -2} is {@literal 12}, and {@code 1234} with a scale factor of
	 * {@literal 2} is {@literal 123400}.
	 * </p>
	 *
	 * @param value
	 *        the value to normalize
	 * @return the normalized value
	 */
	protected static BigDecimal normalized(BigDecimal value) {
		final BigDecimal result = value.stripTrailingZeros();
		return (result.scale() < 0 ? result.setScale(0) : result);
	}

	/**
	 * Get an non-scaled data property value.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @return the value, or {@code null} if not available
	 */
	public @Nullable Number getValue(ModbusReference dataRef) {
		return getValue(dataRef, blockAddress);
	}

	/**
	 * Get a non-scaled data property value.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the value, or {@code null} if not available
	 */
	public @Nullable Number getValue(ModbusReference dataRef, int dataOffset) {
		Number v = data.getNumber(dataRef, dataOffset);
		if ( v == null ) {
			return null;
		}

		DataClassification classification = dataRef.getClassification();

		// check for NaN
		if ( DataClassification.Accumulator == classification ) {
			// zero means "not accumulated", and an acc64 value outside the positive int64 range is
			// invalid; all other values are valid
			final boolean invalid = (v.longValue() == 0
					|| (v instanceof BigInteger i && i.bitLength() >= Long.SIZE));
			return (invalid ? null : v);
		} else if ( DataClassification.Bitfield == classification ) {
			// for bit fields, if the most significant bit is set, it is NaN
			if ( dataRef.getWordLength() == 1 && (v.intValue()
					& ModbusConstants.NAN_BITFIELD16) == ModbusConstants.NAN_BITFIELD16 ) {
				return null;
			} else if ( dataRef.getWordLength() == 2 && (v.intValue()
					& ModbusConstants.NAN_BITFIELD32) == ModbusConstants.NAN_BITFIELD32 ) {
				return null;
			}
		}

		final boolean nan = switch (dataRef.getDataType()) {
			case Int16 -> (v.intValue() & 0xFFFF) == ModbusConstants.NAN_INT16;
			case Int32 -> (v.intValue() & 0xFFFFFFFF) == ModbusConstants.NAN_INT32;
			case Int64 -> (v.longValue() & 0xFFFFFFFFFFFFFFFFL) == ModbusConstants.NAN_INT64;
			case UInt16 -> v.intValue() == ModbusConstants.NAN_UINT16;
			case UInt32 -> v.longValue() == ModbusConstants.NAN_UINT32;
			case UInt64 -> NAN_UINT64.equals(v);
			// NaN is the "not implemented" value; infinite values are not valid either
			case Float32 -> !Float.isFinite(v.floatValue());
			default -> false;
		};
		return (nan ? null : v);
	}

	/**
	 * Get a float data property value.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @return the value, or {@code null} if not available
	 */
	public @Nullable Float getFloatValue(ModbusReference dataRef) {
		return getFloatValue(dataRef, blockAddress);
	}

	/**
	 * Get a float data property value.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the value, or {@code null} if not available
	 */
	public @Nullable Float getFloatValue(ModbusReference dataRef, int dataOffset) {
		Number n = getValue(dataRef, dataOffset);
		return (n != null ? n.floatValue() : null);
	}

	/**
	 * Get a decimal data property value.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @return the value, or {@code null} if not available
	 * @see #getDecimalValue(ModbusReference, int)
	 */
	public @Nullable BigDecimal getDecimalValue(ModbusReference dataRef) {
		return getDecimalValue(dataRef, blockAddress);
	}

	/**
	 * Get a decimal data property value.
	 *
	 * <p>
	 * Integer values are converted exactly. Floating point values are converted
	 * from their shortest decimal representation, so a {@code float32} value of
	 * {@literal 0.1} is returned as {@literal 0.1}. The value is normalized as
	 * described in {@link #normalized(BigDecimal)}.
	 * </p>
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the value, or {@code null} if not available
	 */
	public @Nullable BigDecimal getDecimalValue(ModbusReference dataRef, int dataOffset) {
		final BigDecimal d = bigDecimalForNumber(getValue(dataRef, dataOffset));
		return (d != null ? normalized(d) : null);
	}

	/**
	 * Get an integer data property value.
	 *
	 * <p>
	 * The value will be rounded, if necessary.
	 * </p>
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @return the value, or {@code null} if not available
	 */
	public @Nullable Integer getIntegerValue(ModbusReference dataRef) {
		return getIntegerValue(dataRef, blockAddress);
	}

	/**
	 * Get an integer data property value.
	 *
	 * <p>
	 * The value will be rounded, if necessary. A value that does not fit in an
	 * {@code int}, such as a {@code uint32} value larger than
	 * {@link Integer#MAX_VALUE}, is returned as {@code null}.
	 * </p>
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the value, or {@code null} if not available
	 */
	public @Nullable Integer getIntegerValue(ModbusReference dataRef, int dataOffset) {
		BigInteger n = bigIntegerForNumber(maximumDecimalScale(getValue(dataRef, dataOffset), 0));
		return (n != null && n.bitLength() < Integer.SIZE ? n.intValue() : null);
	}

	/**
	 * Get a long data property value.
	 *
	 * <p>
	 * The value will be rounded, if necessary.
	 * </p>
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @return the value, or {@code null} if not available
	 */
	public @Nullable Long getLongValue(ModbusReference dataRef) {
		return getLongValue(dataRef, blockAddress);
	}

	/**
	 * Get a long data property value.
	 *
	 * <p>
	 * The value will be rounded, if necessary. A value that does not fit in a
	 * {@code long}, such as a {@code uint64} value larger than
	 * {@link Long#MAX_VALUE}, is returned as {@code null}.
	 * </p>
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the value, or {@code null} if not available
	 */
	public @Nullable Long getLongValue(ModbusReference dataRef, int dataOffset) {
		BigInteger n = bigIntegerForNumber(maximumDecimalScale(getValue(dataRef, dataOffset), 0));
		return (n != null && n.bitLength() < Long.SIZE ? n.longValue() : null);
	}

	/**
	 * Get a scaled data property value as a float.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param scaleRef
	 *        the block address relative reference to the scale factor
	 * @return the value, or {@code null} if not available
	 * @see #getScaledValue(ModbusReference, ModbusReference)
	 */
	public @Nullable Float getScaledFloatValue(ModbusReference dataRef, ModbusReference scaleRef) {
		return getScaledFloatValue(dataRef, scaleRef, blockAddress, blockAddress);
	}

	/**
	 * Get a scaled data property value as a float.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param scaleRef
	 *        the block address relative reference to the scale factor
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @param scaleOffset
	 *        the scale address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the value, or {@code null} if not available
	 * @see #getScaledValue(ModbusReference, ModbusReference, int, int)
	 */
	public @Nullable Float getScaledFloatValue(ModbusReference dataRef, ModbusReference scaleRef,
			int dataOffset, int scaleOffset) {
		Number n = getScaledValue(dataRef, scaleRef, dataOffset, scaleOffset);
		return (n != null ? n.floatValue() : null);
	}

	/**
	 * Get a scaled data property value as an integer.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param scaleRef
	 *        the block address relative reference to the scale factor
	 * @return the value, or {@code null} if not available
	 * @see #getScaledValue(ModbusReference, ModbusReference)
	 */
	public @Nullable Integer getScaledIntegerValue(ModbusReference dataRef, ModbusReference scaleRef) {
		return getScaledIntegerValue(dataRef, scaleRef, blockAddress, blockAddress);
	}

	/**
	 * Get a scaled data property value as an integer.
	 *
	 * <p>
	 * Any fractional part of the scaled value is discarded. The value is not
	 * available if the result does not fit in an integer.
	 * </p>
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param scaleRef
	 *        the block address relative reference to the scale factor
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @param scaleOffset
	 *        the scale address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the value, or {@code null} if not available
	 * @see #getScaledValue(ModbusReference, ModbusReference, int, int)
	 */
	public @Nullable Integer getScaledIntegerValue(ModbusReference dataRef, ModbusReference scaleRef,
			int dataOffset, int scaleOffset) {
		BigInteger n = scaledWholeValue(dataRef, scaleRef, dataOffset, scaleOffset);
		return (n != null && n.bitLength() < Integer.SIZE ? n.intValue() : null);
	}

	/**
	 * Get a scaled data property value as a long.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param scaleRef
	 *        the block address relative reference to the scale factor
	 * @return the value, or {@code null} if not available
	 * @see #getScaledValue(ModbusReference, ModbusReference)
	 */
	public @Nullable Long getScaledLongValue(ModbusReference dataRef, ModbusReference scaleRef) {
		return getScaledLongValue(dataRef, scaleRef, blockAddress, blockAddress);
	}

	/**
	 * Get a scaled data property value as a long.
	 *
	 * <p>
	 * Any fractional part of the scaled value is discarded. The value is not
	 * available if the result does not fit in a long, which is possible for
	 * {@code uint64} points.
	 * </p>
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param scaleRef
	 *        the block address relative reference to the scale factor
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @param scaleOffset
	 *        the scale address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the value, or {@code null} if not available
	 * @see #getScaledValue(ModbusReference, ModbusReference, int, int)
	 */
	public @Nullable Long getScaledLongValue(ModbusReference dataRef, ModbusReference scaleRef,
			int dataOffset, int scaleOffset) {
		BigInteger n = scaledWholeValue(dataRef, scaleRef, dataOffset, scaleOffset);
		return (n != null && n.bitLength() < Long.SIZE ? n.longValue() : null);
	}

	/**
	 * Get a scaled data property value with any fractional part discarded.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param scaleRef
	 *        the block address relative reference to the scale factor
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @param scaleOffset
	 *        the scale address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the value, or {@code null} if not available
	 */
	private @Nullable BigInteger scaledWholeValue(ModbusReference dataRef, ModbusReference scaleRef,
			int dataOffset, int scaleOffset) {
		BigDecimal d = getScaledValue(dataRef, scaleRef, dataOffset, scaleOffset);
		return (d != null ? d.toBigInteger() : null);
	}

	/**
	 * Get a string data property value.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @return the value, or {@code null} if not available
	 * @see ModelData#getStringValue(ModbusReference, int)
	 */
	public @Nullable String getStringValue(ModbusReference dataRef) {
		return getStringValue(dataRef, blockAddress);
	}

	/**
	 * Get a string data property value.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the value, or {@code null} if not available
	 * @see ModelData#getStringValue(ModbusReference, int)
	 */
	public @Nullable String getStringValue(ModbusReference dataRef, int dataOffset) {
		return data.getStringValue(dataRef, dataOffset);
	}

	/**
	 * Get a boolean data property value.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @return the value, or {@code null} if not available
	 * @see #getBooleanValue(ModbusReference, int)
	 */
	public @Nullable Boolean getBooleanValue(ModbusReference dataRef) {
		return getBooleanValue(dataRef, blockAddress);
	}

	/**
	 * Get a boolean data property value.
	 *
	 * <p>
	 * This is for SunSpec enumerations with two values, such as
	 * {@code DISABLED (0)} and {@code ENABLED (1)}: {@code 0} is returned as
	 * {@code false}, {@code 1} as {@code true}, and any other value, including
	 * the SunSpec "not implemented" value, as {@code null}.
	 * </p>
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the value, or {@code null} if not available
	 */
	public @Nullable Boolean getBooleanValue(ModbusReference dataRef, int dataOffset) {
		Number n = getValue(dataRef, dataOffset);
		if ( n == null ) {
			return null;
		}
		return switch (n.intValue()) {
			case 0 -> Boolean.FALSE;
			case 1 -> Boolean.TRUE;
			default -> null;
		};
	}

	/**
	 * Get an enumerated data property value.
	 *
	 * @param <T>
	 *        the enumeration type
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param type
	 *        the enumeration type
	 * @return the value, or {@code null} if not available
	 * @see #getCodedValue(ModbusReference, int, Class)
	 */
	public <T extends Enum<T> & CodedValue> @Nullable T getCodedValue(ModbusReference dataRef,
			Class<T> type) {
		return getCodedValue(dataRef, blockAddress, type);
	}

	/**
	 * Get an enumerated data property value.
	 *
	 * <p>
	 * A value that is not one of the codes of the enumeration, including the
	 * SunSpec "not implemented" value, is returned as {@code null}.
	 * </p>
	 *
	 * @param <T>
	 *        the enumeration type
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @param type
	 *        the enumeration type
	 * @return the value, or {@code null} if not available
	 */
	public <T extends Enum<T> & CodedValue> @Nullable T getCodedValue(ModbusReference dataRef,
			int dataOffset, Class<T> type) {
		Number n = getValue(dataRef, dataOffset);
		return (n != null ? CodedValue.forCodeValue(n.intValue(), type, null) : null);
	}

	/**
	 * Get a bitfield data property value as a set of enumeration values.
	 *
	 * @param <T>
	 *        the enumeration type
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param type
	 *        the enumeration type
	 * @return the values, never {@code null}
	 * @see #getBitmaskableValues(ModbusReference, int, Class)
	 */
	public <T extends Enum<T> & Bitmaskable> Set<T> getBitmaskableValues(ModbusReference dataRef,
			Class<T> type) {
		return getBitmaskableValues(dataRef, blockAddress, type);
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
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @param type
	 *        the enumeration type
	 * @return the values, never {@code null}
	 */
	public <T extends Enum<T> & Bitmaskable> Set<T> getBitmaskableValues(ModbusReference dataRef,
			int dataOffset, Class<T> type) {
		return SunSpecUtils.bitfieldValues(data.getNumber(dataRef, dataOffset), dataRef.getWordLength(),
				type);
	}

	/**
	 * Get the indexes of the bits set in a bitfield data property.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @return the bit indexes, in ascending order, never {@code null}
	 * @see #getBitfieldIndexes(ModbusReference, int)
	 */
	public Set<Integer> getBitfieldIndexes(ModbusReference dataRef) {
		return getBitfieldIndexes(dataRef, blockAddress);
	}

	/**
	 * Get the indexes of the bits set in a bitfield data property.
	 *
	 * <p>
	 * This is for bitfields whose bits are numbered things, such as ports or
	 * contactors, rather than flags. SunSpec bitfields never have their most
	 * significant bit set, so a bitfield with that bit set, including the
	 * SunSpec "not implemented" value, results in an empty set.
	 * </p>
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @return the bit indexes, in ascending order, never {@code null}
	 */
	public Set<Integer> getBitfieldIndexes(ModbusReference dataRef, int dataOffset) {
		Number n = data.getNumber(dataRef, dataOffset);
		if ( n == null ) {
			return Collections.emptySet();
		}
		final long v = n.longValue();
		final int bitCount = dataRef.getWordLength() * 16;
		if ( v == 0 || (v & (1L << (bitCount - 1))) != 0 ) {
			return Collections.emptySet();
		}
		final Set<Integer> result = new TreeSet<>();
		for ( int i = 0; i < bitCount - 1; i++ ) {
			if ( ((v >> i) & 1L) == 1L ) {
				result.add(i);
			}
		}
		return result;
	}

	/**
	 * Get the bits set in a sequence of bitfield data properties, as a single
	 * bit set.
	 *
	 * @param dataRefs
	 *        the block address relative references to the data properties
	 * @return the bit set, never {@code null}
	 * @see #getBitfieldBits(int, ModbusReference...)
	 */
	public BitSet getBitfieldBits(ModbusReference... dataRefs) {
		return getBitfieldBits(blockAddress, dataRefs);
	}

	/**
	 * Get the bits set in a sequence of bitfield data properties, as a single
	 * bit set.
	 *
	 * <p>
	 * The bits of each bitfield follow the bits of the bitfields before it, so
	 * for a sequence of 32-bit bitfields, such as the SunSpec vendor event
	 * fields, the first bit of the second bitfield is index {@literal 32}. Each
	 * bitfield is read as with
	 * {@link #getBitfieldIndexes(ModbusReference, int)}, so a bitfield with its
	 * most significant bit set contributes no bits.
	 * </p>
	 *
	 * @param dataOffset
	 *        the data address offset to add to each
	 *        {@link ModbusReference#getAddress()}
	 * @param dataRefs
	 *        the block address relative references to the data properties
	 * @return the bit set, never {@code null}
	 */
	public BitSet getBitfieldBits(int dataOffset, ModbusReference... dataRefs) {
		final BitSet result = new BitSet();
		int start = 0;
		for ( ModbusReference ref : dataRefs ) {
			for ( Integer i : getBitfieldIndexes(ref, dataOffset) ) {
				result.set(start + i);
			}
			start += ref.getWordLength() * 16;
		}
		return result;
	}

	/**
	 * Test if a bit is set in a bitfield data property.
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param index
	 *        the index of the bit to test, where {@literal 0} is the least
	 *        significant bit
	 * @return {@literal true} if the bit is set, {@literal false} if it is not,
	 *         or {@code null} if not available
	 * @see #getBitfieldBit(ModbusReference, int, int)
	 */
	public @Nullable Boolean getBitfieldBit(ModbusReference dataRef, int index) {
		return getBitfieldBit(dataRef, blockAddress, index);
	}

	/**
	 * Test if a bit is set in a bitfield data property.
	 *
	 * <p>
	 * This is for bitfields that define a single flag, such as an enabled or
	 * connected state. SunSpec bitfields never have their most significant bit
	 * set, so a bitfield with that bit set, including the SunSpec "not
	 * implemented" value, results in {@code null}.
	 * </p>
	 *
	 * @param dataRef
	 *        the block address relative reference to the data property
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @param index
	 *        the index of the bit to test, where {@literal 0} is the least
	 *        significant bit
	 * @return {@literal true} if the bit is set, {@literal false} if it is not,
	 *         or {@code null} if not available
	 */
	public @Nullable Boolean getBitfieldBit(ModbusReference dataRef, int dataOffset, int index) {
		Number n = data.getNumber(dataRef, dataOffset);
		if ( n == null ) {
			return null;
		}
		final long v = n.longValue();
		if ( (v & (1L << (dataRef.getWordLength() * 16 - 1))) != 0 ) {
			return null;
		}
		return ((v >> index) & 1L) == 1L;
	}

	/**
	 * Write a point value to a device.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param dataRef
	 *        the block address relative reference to the point to write
	 * @param value
	 *        the value to write
	 * @throws IllegalArgumentException
	 *         if {@code dataRef} is not writable or {@code value} is not valid
	 *         for the point
	 * @throws IOException
	 *         if any communication error occurs
	 * @see #writeValue(ModbusConnection, ModbusReference, int, Number)
	 */
	public void writeValue(ModbusConnection conn, ModbusReference dataRef, Number value)
			throws IOException {
		writeValue(conn, dataRef, blockAddress, value);
	}

	/**
	 * Write a point value to a device.
	 *
	 * <p>
	 * The value is encoded with {@link #encodeValue(ModbusReference, Number)}
	 * and written with {@link #writeWords(ModbusConnection, int, short[])}.
	 * </p>
	 *
	 * @param conn
	 *        the connection to write to
	 * @param dataRef
	 *        the block address relative reference to the point to write
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @param value
	 *        the value to write
	 * @throws IllegalArgumentException
	 *         if {@code dataRef} is not writable or {@code value} is not valid
	 *         for the point
	 * @throws IOException
	 *         if any communication error occurs
	 */
	public void writeValue(ModbusConnection conn, ModbusReference dataRef, int dataOffset, Number value)
			throws IOException {
		requireWritable(dataRef);
		writeWords(conn, dataRef.getAddress() + dataOffset, encodeValue(dataRef, value));
	}

	/**
	 * Write a scaled point value to a device.
	 *
	 * @param conn
	 *        the connection to write to
	 * @param dataRef
	 *        the block address relative reference to the point to write
	 * @param scaleRef
	 *        the block address relative reference to the scale factor
	 * @param value
	 *        the value to write
	 * @throws IllegalArgumentException
	 *         if {@code dataRef} is not writable or {@code value} is not valid
	 *         for the point
	 * @throws IllegalStateException
	 *         if the scale factor has not been read or is not implemented
	 * @throws IOException
	 *         if any communication error occurs
	 * @see #writeScaledValue(ModbusConnection, ModbusReference,
	 *      ModbusReference, int, int, Number)
	 */
	public void writeScaledValue(ModbusConnection conn, ModbusReference dataRef,
			ModbusReference scaleRef, Number value) throws IOException {
		writeScaledValue(conn, dataRef, scaleRef, blockAddress, blockAddress, value);
	}

	/**
	 * Write a scaled point value to a device.
	 *
	 * <p>
	 * The value is encoded with
	 * {@link #encodeScaledValue(ModbusReference, ModbusReference, int, Number)}
	 * and written with {@link #writeWords(ModbusConnection, int, short[])}.
	 * </p>
	 *
	 * @param conn
	 *        the connection to write to
	 * @param dataRef
	 *        the block address relative reference to the point to write
	 * @param scaleRef
	 *        the block address relative reference to the scale factor
	 * @param dataOffset
	 *        the data address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @param scaleOffset
	 *        the scale address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @param value
	 *        the value to write
	 * @throws IllegalArgumentException
	 *         if {@code dataRef} is not writable or {@code value} is not valid
	 *         for the point
	 * @throws IllegalStateException
	 *         if the scale factor has not been read or is not implemented
	 * @throws IOException
	 *         if any communication error occurs
	 */
	public void writeScaledValue(ModbusConnection conn, ModbusReference dataRef,
			ModbusReference scaleRef, int dataOffset, int scaleOffset, Number value) throws IOException {
		requireWritable(dataRef);
		writeWords(conn, dataRef.getAddress() + dataOffset,
				encodeScaledValue(dataRef, scaleRef, scaleOffset, value));
	}

	/**
	 * Encode a point value as Modbus register values.
	 *
	 * <p>
	 * Values for integer data types are rounded to an integer, using the
	 * {@link RoundingMode#HALF_UP} mode, and must be within the SunSpec range
	 * of the data type, which excludes the "not implemented" value.
	 * </p>
	 *
	 * @param ref
	 *        the reference to the point to encode the value for
	 * @param value
	 *        the value to encode
	 * @return the encoded register values
	 * @throws IllegalArgumentException
	 *         if {@code value} is not valid for the point, or the point data
	 *         type is not supported
	 */
	protected short[] encodeValue(ModbusReference ref, Number value) {
		final ModbusDataType type = ref.getDataType();
		final Number n;
		if ( type == ModbusDataType.Float32 ) {
			if ( !Float.isFinite(value.floatValue()) ) {
				throw new IllegalArgumentException(
						String.format("The value %s is not valid for the %s point.", value, ref));
			}
			n = value;
		} else {
			final BigInteger i = decimalValue(value, ref).setScale(0, RoundingMode.HALF_UP)
					.toBigInteger();
			final BigInteger min = minimumIntegerValue(ref);
			final BigInteger max = maximumIntegerValue(ref);
			if ( i.compareTo(min) < 0 || i.compareTo(max) > 0 ) {
				throw new IllegalArgumentException(String.format(
						"The value %s is outside the %s point range %d - %d.", value, ref, min, max));
			}
			n = i;
		}
		return nonnull(ModbusDataUtils.encodeNumber(type, n), "Encoded value");
	}

	/**
	 * Encode a scaled point value as Modbus register values.
	 *
	 * <p>
	 * The value is divided by the scale factor and then encoded with
	 * {@link #encodeValue(ModbusReference, Number)}. The scale factor must have
	 * been read from the device, and be implemented as described in
	 * {@link #getScaledValue(ModbusReference, ModbusReference, int, int)}.
	 * </p>
	 *
	 * @param ref
	 *        the reference to the point to encode the value for
	 * @param scaleRef
	 *        the block address relative reference to the scale factor
	 * @param scaleOffset
	 *        the scale address offset to add to
	 *        {@link ModbusReference#getAddress()}
	 * @param value
	 *        the value to encode
	 * @return the encoded register values
	 * @throws IllegalArgumentException
	 *         if {@code value} is not valid for the point, or the point data
	 *         type is not supported
	 * @throws IllegalStateException
	 *         if the scale factor has not been read or is not implemented
	 */
	protected short[] encodeScaledValue(ModbusReference ref, ModbusReference scaleRef, int scaleOffset,
			Number value) {
		if ( !data.dataRegisters().containsKey(scaleRef.getAddress() + scaleOffset) ) {
			throw new IllegalStateException(String
					.format("The %s scale factor for the %s point has not been read.", scaleRef, ref));
		}
		final Integer sf = scaleFactorExponent(scaleRef, scaleOffset);
		if ( sf == null ) {
			throw new IllegalStateException(String
					.format("The %s scale factor for the %s point is not implemented.", scaleRef, ref));
		}
		return encodeValue(ref, decimalValue(value, ref).movePointLeft(sf));
	}

	/**
	 * Write Modbus register values to a device.
	 *
	 * <p>
	 * The values are written with a single "write multiple holding registers"
	 * request, so the registers of a SunSpec synchronization group can be
	 * written atomically. After a successful write the values are also saved to
	 * the model data, as SunSpec requires a subsequent read of the registers to
	 * return the written values.
	 * </p>
	 *
	 * @param conn
	 *        the connection to write to
	 * @param address
	 *        the Modbus address of the first register to write
	 * @param words
	 *        the register values to write
	 * @throws IOException
	 *         if any communication error occurs
	 */
	protected void writeWords(ModbusConnection conn, int address, short[] words) throws IOException {
		conn.writeWords(ModbusWriteFunction.WriteMultipleHoldingRegisters, address, words);
		data.performUpdates(m -> {
			m.saveDataArray(words, address);
			return true;
		});
	}

	private static void requireWritable(ModbusReference ref) {
		if ( ref.getAccess() != PointAccess.ReadWrite ) {
			throw new IllegalArgumentException(String.format("The %s point is not writable.", ref));
		}
	}

	private static BigDecimal decimalValue(Number value, ModbusReference ref) {
		if ( !Double.isFinite(value.doubleValue()) ) {
			throw new IllegalArgumentException(
					String.format("The value %s is not valid for the %s point.", value, ref));
		}
		return nonnull(bigDecimalForNumber(value), "Decimal value");
	}

	private static BigInteger minimumIntegerValue(ModbusReference ref) {
		return switch (ref.getDataType()) {
			case Int16 -> BigInteger.valueOf(-0x7FFF);
			case Int32 -> BigInteger.valueOf(-0x7FFFFFFFL);
			case Int64 -> BigInteger.valueOf(-Long.MAX_VALUE);
			case UInt16, UInt32, UInt64 -> BigInteger.ZERO;
			default -> throw new IllegalArgumentException(String
					.format("The %s point data type %s is not supported.", ref, ref.getDataType()));
		};
	}

	private static BigInteger maximumIntegerValue(ModbusReference ref) {
		final DataClassification classification = ref.getClassification();
		return switch (ref.getDataType()) {
			case Int16 -> BigInteger.valueOf(0x7FFF);
			case Int32 -> BigInteger.valueOf(0x7FFFFFFFL);
			case Int64 -> BigInteger.valueOf(Long.MAX_VALUE);
			case UInt16 -> BigInteger.valueOf(DataClassification.Accumulator == classification ? 0xFFFF
					: DataClassification.Bitfield == classification ? 0x7FFF : 0xFFFE);
			case UInt32 -> BigInteger
					.valueOf(DataClassification.Accumulator == classification ? 0xFFFFFFFFL
							: DataClassification.Bitfield == classification ? 0x7FFFFFFFL : 0xFFFFFFFEL);
			case UInt64 -> (DataClassification.Accumulator == classification
					? BigInteger.valueOf(Long.MAX_VALUE)
					: UINT64_MAX);
			default -> throw new IllegalArgumentException(String
					.format("The %s point data type %s is not supported.", ref, ref.getDataType()));
		};
	}

}
