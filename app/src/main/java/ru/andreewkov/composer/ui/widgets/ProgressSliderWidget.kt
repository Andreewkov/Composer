package ru.andreewkov.composer.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.andreewkov.composer.ui.slider.ProgressSliderManager
import ru.andreewkov.composer.ui.theme.AppColor

@Composable
fun ProgressSliderWidget(
    title: String,
    progress: Float,
    modifier: Modifier = Modifier,
    onProgressChanged: (Float) -> Unit = { },
) {
    val manager = remember { ProgressSliderManager() }
    TitledWidget(title, AppColor.Peach, modifier){
        SliderWidget(
            nonActiveProgress = progress,
            backgroundColor = AppColor.Peach_a6,
            progressColor = AppColor.LightPeach,
            indicatorColor = AppColor.DarkPeach,
            manager = manager,
            onValueChanged = onProgressChanged,
        )
    }
}

@Composable
@Preview
private fun ProgressSliderWidgetPreview() {
    ProgressSliderWidget(
        title = "Скорость:",
        progress = 0.3f,
    )
}
