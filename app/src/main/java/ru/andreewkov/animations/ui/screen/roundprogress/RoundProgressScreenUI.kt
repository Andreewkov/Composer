package ru.andreewkov.animations.ui.screen.roundprogress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.andreewkov.animations.ui.theme.AnimationsColor
import ru.andreewkov.animations.ui.utils.AnimationsPreview
import ru.andreewkov.animations.ui.utils.Preview
import ru.andreewkov.animations.ui.widgets.GridContentWidget
import ru.andreewkov.animations.ui.widgets.GridWidget
import ru.andreewkov.animations.ui.widgets.RoundProgressWidget

private enum class RoundProgressItem {
    RoundProgress,
    RoundSmoothProgress,
    RoundLoader,
    RoundSpinningLoader,
}

@Composable
fun RoundProgressScreenUI(title: String, color: Color) {
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
                    colors = listOf(AnimationsColor.Peach, AnimationsColor.LightPeach, AnimationsColor.Peach),
                    progressWidth = 30.dp,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
    GridWidget(
        columns = 2,
        itemCount = 4,
        modifier = Modifier.fillMaxSize()
    ) { index ->
        Box(modifier = Modifier.height(200.dp)) {
            when (index) {
                0 -> {
                    RoundProgressWidget(
                        colors = listOf(AnimationsColor.Peach, AnimationsColor.LightPeach, AnimationsColor.Peach),
                        progressWidth = 30.dp,
                        modifier = Modifier.size(100.dp)
                    )
                }
                2 -> {
                    RoundProgressWidget(
                        colors = listOf(AnimationsColor.Peach, AnimationsColor.LightPeach, AnimationsColor.Peach),
                        progressWidth = 30.dp,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        }
    }
}

@Composable
@AnimationsPreview
private fun RoundProgressScreenUIPreview() {
    Preview {
        RoundProgressScreenUI(title = "anim", color = Color.Green)
    }
}
