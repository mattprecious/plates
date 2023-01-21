package com.mattprecious.plates.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mattprecious.plates.ui.calculator.Calculator

@Composable
fun PlatesApp() {
  val systemUiController = rememberSystemUiController()
  val useDarkIcons = !isSystemInDarkTheme()

  LaunchedEffect(systemUiController, useDarkIcons) {
    systemUiController.setSystemBarsColor(
      color = Color.Transparent,
      darkIcons = useDarkIcons,
    )
  }

  Calculator()
}
