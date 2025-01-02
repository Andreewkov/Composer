package ru.andreewkov.composer.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import ru.andreewkov.composer.ui.theme.AppColor
import ru.andreewkov.composer.ui.utils.ComposerPreview
import ru.andreewkov.composer.ui.utils.Preview
import ru.andreewkov.composer.ui.widgets.ProgressSliderWidget
import ru.andreewkov.composer.ui.widgets.RoundProgressWidget

@Composable
fun RoundProgressScreenUI() {
    var speedProgress by remember { mutableStateOf(0.5f) }
    var sweepProgress by remember { mutableStateOf(0.5f) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        RoundProgressScreenContainer {
            RoundProgressWidget(
                colors = listOf(AppColor.Peach, AppColor.LightPeach, AppColor.Peach),
                speed = speedProgress,
                sweep = sweepProgress,
                modifier = Modifier.heightIn(max = 200.dp)
            )
            Spacer(modifier = Modifier.height(60.dp).width(30.dp))
            Column {
                ProgressSliderWidget(
                    title = "Cкорость:",
                    progress = speedProgress,
                    onProgressChanged = { speedProgress = it }
                )
                Spacer(modifier = Modifier.height(20.dp))
                ProgressSliderWidget(
                    title = "Длина:",
                    progress = sweepProgress,
                    onProgressChanged = { sweepProgress = it }
                )
            }
        }
    }
}

@Composable
private fun RoundProgressScreenContainer(content: @Composable () -> Unit) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 100.dp, vertical = 20.dp)
        ) { content() }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) { content() }
    }
}

@ComposerPreview
@Composable
private fun RoundProgressScreenUIPreview() {
    Preview {
        RoundProgressScreenUI()
    }
}