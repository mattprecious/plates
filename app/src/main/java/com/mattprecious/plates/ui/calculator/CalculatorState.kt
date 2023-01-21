package com.mattprecious.plates.ui.calculator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

  private var weight by mutableStateOf<Pound?>(barWeight)

  val textFieldValue: String
    get() = weight?.editableString ?: ""

  fun setWeight(weightString: String) {
    weight = weightString.toIntOrNull()?.lbs
  }

  fun increase() {
    weight = weight?.plus(stepSize).coerceValid()
  }

  fun decrease() {
    weight = weight?.minus(stepSize).coerceValid()
  }

  fun validate() {
    weight = weight.coerceValid()
  }

  private fun Pound?.coerceValid() = this?.coerceAtLeast(barWeight) ?: barWeight

  @Composable
  fun platesPerSide(): PlatesPerSide {
    return remember(weight) {
      val plateWeight = weight?.minus(barWeight)?.coerceAtLeast(0.lbs) ?: 0.lbs

      var remainingWeight = plateWeight / 2
      val plates = buildList {
        // Greedy algorithm. Will not be optimal or correct for all configurations of plates.
        availableWeightsPerSide.forEach {
          if (remainingWeight >= it) {
            add(it)
            remainingWeight -= it
          }
        }
      }

      PlatesPerSide(
        plates = plates,
        outstanding = remainingWeight,
      )
    }
  }

  data class PlatesPerSide(
    val plates: List<Pound>,
    val outstanding: Pound,
  )
}
