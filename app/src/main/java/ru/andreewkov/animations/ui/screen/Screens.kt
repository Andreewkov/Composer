package ru.andreewkov.animations.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.andreewkov.animations.ui.theme.AnimationsColor
import ru.andreewkov.animations.ui.utils.AnimationsPreview
import ru.andreewkov.animations.ui.utils.Preview
import ru.andreewkov.animations.ui.widgets.RoundLoaderWidget
import ru.andreewkov.animations.ui.widgets.RoundProgressWidget

@Composable
fun RoundProgressScreenUI() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        contentAlignment = Alignment.Center,
    ) {
        RoundProgressWidget(
            colors = listOf(AnimationsColor.Peach, AnimationsColor.LightPeach, AnimationsColor.Peach),
            duration = 1400,
        )
    }
}

@Composable
fun RoundLoaderScreenUI() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        contentAlignment = Alignment.Center,
    ) {
        RoundLoaderWidget()
    }
}

@AnimationsPreview
@Composable
private fun RoundProgressScreenUIPreview() {
    Preview {
        RoundProgressScreenUI()
    }
}

@AnimationsPreview
@Composable
private fun RoundLoaderScreenUIPreview() {
    Preview {
        RoundLoaderScreenUI()
    }
}