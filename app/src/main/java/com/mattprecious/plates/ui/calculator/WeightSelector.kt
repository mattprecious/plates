package com.mattprecious.plates.ui.calculator

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mattprecious.plates.R
import com.mattprecious.plates.R.string
import com.mattprecious.plates.ui.calculator.Direction.Decrease
import com.mattprecious.plates.ui.calculator.Direction.Increase
import com.mattprecious.plates.weight.lbs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WeightSelector(
  modifier: Modifier = Modifier,
  state: CalculatorState,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(24.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    val focusManager = LocalFocusManager.current

    WeightChangeButton(
      direction = Decrease,
      onClick = {
        state.decrease()
        focusManager.clearFocus()
      },
    )

    Box(
      modifier = Modifier.weight(1f)
    ) {
      TextField(
        modifier = Modifier
          .align(Alignment.Center)
          .onFocusChanged {
            if (!it.hasFocus) {
              state.validate()
            }
          },
        value = state.textFieldValue,
        onValueChange = { state.onValueChange(it) },
        textStyle = MaterialTheme.typography.displayLarge.copy(
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Medium,
        ),
        colors = TextFieldDefaults.textFieldColors(
          unfocusedIndicatorColor = Color.Transparent,
          containerColor = Color.Transparent,
        ),
        isError = state.platesPerSide().outstanding > 0.lbs,
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Number,
          imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
          onDone = { focusManager.clearFocus() }
        ),
      )
    }

    WeightChangeButton(
      direction = Increase,
      onClick = {
        state.increase()
        focusManager.clearFocus()
      },
    )
  }
}

private enum class Direction {
  Increase,
  Decrease
}

@Composable
private fun WeightChangeButton(
  direction: Direction,
  onClick: () -> Unit,
) {
  OutlinedButton(
    modifier = Modifier.size(64.dp),
    shape = CircleShape,
    onClick = onClick,
  ) {
    Image(
      painter = painterResource(
        when (direction) {
          Increase -> R.drawable.weight_increase
          Decrease -> R.drawable.weight_decrease
        }
      ),
      contentDescription = when (direction) {
        Increase -> stringResource(id = string.calculator_increase)
        Decrease -> stringResource(id = string.calculator_decrease)
      },
      colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
    )
  }
}

@Preview(showBackground = true)
@Composable
fun WeightSelectorPreview() {
  WeightSelector(state = rememberCalculatorState())
}
