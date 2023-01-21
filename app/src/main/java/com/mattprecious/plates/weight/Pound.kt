package com.mattprecious.plates.weight

import android.icu.number.NumberFormatter
import android.icu.number.NumberFormatter.DecimalSeparatorDisplay
import android.icu.util.MeasureUnit
import java.util.Locale
import kotlin.math.roundToInt

/**
 * A naive weight implementation that makes lots of assumptions specific to this use case. Supports
 * weights with up to one decimal place with questionable precision.
 */
@JvmInline
value class Pound private constructor(
  /** Weight in tenths of a pound. */
  private val value: Int,
) : Comparable<Pound> {
  val editableString: String
    get() = editableFormat.locale(Locale.getDefault()).format(value / 10f).toString()

  override fun toString(): String {
    return displayFormat.locale(Locale.getDefault()).format(value / 10f).toString()
  }

  override fun compareTo(other: Pound): Int {
    return this.value - other.value
  }

  operator fun plus(other: Pound) = Pound(value + other.value)

  operator fun minus(other: Pound) = Pound(value - other.value)

  operator fun times(other: Int) = Pound(value * other)

  operator fun div(other: Int) = Pound(value / other)

  companion object {
    private val displayFormat = NumberFormatter.with()
      .decimal(DecimalSeparatorDisplay.AUTO)
      .unit(MeasureUnit.POUND)

    private val editableFormat = displayFormat.unit(null)

    fun fromInt(weight: Int) = Pound(weight * 10)

    /** Supports only one decimal place. Could be wrong due to double precision. */
    fun fromDouble(weight: Double) = Pound((weight * 10).roundToInt())
  }
}

inline val Int.lbs
  get() = Pound.fromInt(this)

/** @see [Pound.fromDouble] */
inline val Double.lbs
  get() = Pound.fromDouble(this)
