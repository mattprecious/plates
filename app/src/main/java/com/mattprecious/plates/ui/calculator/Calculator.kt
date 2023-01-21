package com.mattprecious.plates.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mattprecious.plates.ui.theme.PlatesTheme

@Composable
fun Calculator() {
  val focusManager = LocalFocusManager.current

  PlatesTheme {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
        .pointerInput(Unit) {
          detectTapGestures {
            focusManager.clearFocus()
          }
        }
        .systemBarsPadding(),
    ) {
      val state = rememberCalculatorState()

      Column(
        modifier = Modifier.padding(24.dp),
      ) {
        WeightSelector(state = state)
        Spacer(modifier = Modifier.height(24.dp))
        PlateList(
          modifier = Modifier.fillMaxHeight(),
          platesPerSide = state.platesPerSide(),
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
  Calculator()
}
