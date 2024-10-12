package ru.andreewkov.animations.ui.screen

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.andreewkov.animations.ui.theme.AnimationsColor
import ru.andreewkov.animations.ui.utils.AnimationsPreview
import ru.andreewkov.animations.ui.utils.Preview
import ru.andreewkov.animations.ui.widgets.GridContentWidget
import ru.andreewkov.animations.ui.widgets.RoundProgressWidget

private enum class RoundProgressItem {
    RoundProgress,
    RoundSmoothProgress,
    RoundLoader,
    RoundSpinningLoader,
}

@Composable
fun RoundProgressScreenUI() {
    GridContentWidget(
        items = RoundProgressItem.entries.toList(),
    ) { item ->
        when (item) {
            RoundProgressItem.RoundProgress -> {
                RoundProgressWidget(
                    colors = listOf(AnimationsColor.Peach, AnimationsColor.LightPeach, AnimationsColor.Peach),
                    progressWidth = 30.dp,
                    modifier = Modifier.size(100.dp)
                )
            }
            else -> {
                RoundProgressWidget(
                    colors = listOf(Color.Cyan, Color.Cyan, Color.Cyan),
                    progressWidth = 30.dp,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}

@Composable
@AnimationsPreview
private fun RoundProgressScreenUIPreview() {
    Preview {
        RoundProgressScreenUI()
    }
}
