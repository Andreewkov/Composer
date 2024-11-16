package ru.andreewkov.composer.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.andreewkov.composer.ui.theme.AppColor
import ru.andreewkov.composer.ui.utils.ComposerPreview
import ru.andreewkov.composer.ui.utils.Preview
import ru.andreewkov.composer.ui.widgets.RoundLoaderWidget
import ru.andreewkov.composer.ui.widgets.RoundProgressWidget

@Composable
fun RoundProgressScreenUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        RoundProgressWidget(
            colors = listOf(AppColor.Peach, AppColor.LightPeach, AppColor.Peach),
            duration = 1400,
        )
    }
}

@Composable
fun RoundLoaderScreenUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        RoundLoaderWidget(
            itemCount = 12,
            color = AppColor.Peach,
        )
    }
}

@ComposerPreview
@Composable
private fun RoundProgressScreenUIPreview() {
    Preview {
        RoundProgressScreenUI()
    }
}

@ComposerPreview
@Composable
private fun RoundLoaderScreenUIPreview() {
    Preview {
        RoundLoaderScreenUI()
    }
}