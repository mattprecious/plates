package com.mattprecious.plates.ui.calculator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mattprecious.plates.R
import com.mattprecious.plates.ui.calculator.CalculatorState.PlatesPerSide
import com.mattprecious.plates.weight.Pound
import com.mattprecious.plates.weight.lbs

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PlateList(
  modifier: Modifier = Modifier,
  platesPerSide: PlatesPerSide,
) {
  LazyVerticalGrid(
    modifier = modifier,
    columns = GridCells.Adaptive(minSize = 160.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    if (platesPerSide.outstanding > 0.lbs) {
      item(
        key = "outstanding",
        span = { GridItemSpan(maxLineSpan) },
      ) {
        Outstanding(
          modifier = Modifier.animateItemPlacement(),
          weight = platesPerSide.outstanding,
        )
      }
    }

    if (!platesPerSide.plates.isEmpty()) {
      item(
        key = "per-side",
        span = { GridItemSpan(maxLineSpan) },
      ) {
        Text(
          modifier = Modifier.animateItemPlacement(),
          text = stringResource(id = R.string.plate_list_per_side),
          style = MaterialTheme.typography.labelLarge,
        )
      }
    }

    items(
      items = platesPerSide.plates.toList(),
      key = { it.first.value },
    ) {
      Plate(
        modifier = Modifier.animateItemPlacement(),
        weight = it.first,
        count = it.second,
      )
    }
  }
}

@Composable
private fun Plate(
  modifier: Modifier = Modifier,
  weight: Pound,
  count: Int,
) {
  Card(
    modifier = modifier.height(64.dp),
    shape = RoundedCornerShape(percent = 50),
  ) {
    Row {
      val (countBackground, countText) = when (count) {
        1 -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
        2 -> MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
        else -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
      }

      Box(
        modifier = Modifier
          .fillMaxHeight()
          .aspectRatio(1f, matchHeightConstraintsFirst = true)
          .background(color = countBackground, shape = CircleShape),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = "$count",
          style = MaterialTheme.typography.headlineMedium,
          color = countText,
        )
      }
      Text(
        modifier = Modifier
          .align(Alignment.CenterVertically)
          .padding(horizontal = 16.dp),
        text = "$weight",
        style = MaterialTheme.typography.headlineMedium,
      )
    }
  }
}

@Composable
private fun Outstanding(
  modifier: Modifier = Modifier,
  weight: Pound,
) {
  Card(
    modifier = modifier.height(64.dp),
    shape = RoundedCornerShape(percent = 50),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
  ) {
    Box(
      modifier = Modifier
        .fillMaxHeight()
        .padding(horizontal = 16.dp),
      contentAlignment = Alignment.CenterStart,
    ) {
      Text(
        text = stringResource(id = R.string.plate_list_outstanding, formatArgs = arrayOf(weight)),
        style = MaterialTheme.typography.headlineMedium,
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PlateListPreview() {
  PlateList(
    platesPerSide = PlatesPerSide(
      plates = mapOf(45.lbs to 1, 25.lbs to 1, 5.lbs to 2, 2.5.lbs to 1, 1.lbs to 3),
      outstanding = 30.lbs,
    ),
  )
}
