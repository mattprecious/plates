package com.mattprecious.plates.ui.calculator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.mattprecious.plates.weight.Pound
import com.mattprecious.plates.weight.lbs

@Composable
fun rememberCalculatorState(): CalculatorState {
  return remember { CalculatorState() }
}

class CalculatorState internal constructor() {
  private val barWeight = 45.lbs
  private val availableWeightsPerSide =
    arrayOf(45.lbs, 35.lbs, 25.lbs, 10.lbs, 5.lbs, 5.lbs, 2.5.lbs)

  private val stepSize = availableWeightsPerSide.last() * 2

  /** Do not mutate directly. Mutate through [updateWeight]. */
  private var weight by mutableStateOf<Pound?>(barWeight)

  private var _textFieldValue by mutableStateOf(TextFieldValue())
  val textFieldValue: TextFieldValue
    get() = _textFieldValue

  init {
    updateWeight(barWeight)
  }

  fun onValueChange(value: TextFieldValue) {
    _textFieldValue = if (value.text == _textFieldValue.text) {
      value
    } else {
      updateWeight(value.text.toIntOrNull()?.lbs)
      _textFieldValue.copy(
        selection = value.selection,
        composition = value.composition,
      )
    }
  }

  fun selectAll() {
    _textFieldValue = _textFieldValue.copy(selection = TextRange(0, _textFieldValue.text.length))
  }

  fun increase() {
    val weight = weight
    val nextStep = when {
      weight == null -> barWeight
      weight % stepSize == 0.lbs -> weight + stepSize
      else -> weight + stepSize - (weight % stepSize)
    }

    updateWeight(nextStep.coerceValid())
  }

  fun decrease() {
    val weight = weight
    val nextStep = when {
      weight == null -> barWeight
      weight % stepSize == 0.lbs -> weight - stepSize
      else -> weight - (weight % stepSize)
    }

    updateWeight(nextStep.coerceValid())
  }

  fun validate() {
    updateWeight(weight.coerceValid())
  }

  private fun updateWeight(weight: Pound?) {
    this.weight = weight
    _textFieldValue = _textFieldValue.copy(
      text = weight?.editableString ?: "",
    )
  }

  private fun Pound?.coerceValid() = this?.coerceAtLeast(barWeight) ?: barWeight

  @Composable
  fun platesPerSide(): PlatesPerSide {
    return remember(weight) {
      val plateWeight = weight?.minus(barWeight)?.coerceAtLeast(0.lbs) ?: 0.lbs

      var remainingWeight = plateWeight / 2
      val plates = buildMap {
        // Greedy algorithm. Will not be optimal or correct for all configurations of plates.
        availableWeightsPerSide.forEach {
          if (remainingWeight >= it) {
            put(it, getOrDefault(it, 0) as Int + 1)
            remainingWeight -= it
          }
        }
      }

      PlatesPerSide(
        plates = plates,
        outstanding = remainingWeight * 2,
      )
    }
  }

  data class PlatesPerSide(
    val plates: Map<Pound, Int>,
    val outstanding: Pound,
  )
}
