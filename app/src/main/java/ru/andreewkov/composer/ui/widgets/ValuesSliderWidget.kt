package ru.andreewkov.composer.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.andreewkov.composer.ui.slider.SliderWidget
import ru.andreewkov.composer.ui.slider.ValuesSliderManager
import ru.andreewkov.composer.ui.slider.calculateProgress
import ru.andreewkov.composer.ui.theme.AppColor

@Composable
fun ValuesSliderWidget(
    title: String,
    values: IntRange,
    currentValue: Int,
    modifier: Modifier = Modifier,
    onValueChanged: (Int) -> Unit = { },
) {
    val nonActiveProgress = remember { calculateProgress(currentValue, values) }
    val manager = remember {
        ValuesSliderManager(
            values = values,
            startProgress = nonActiveProgress,
            startValue = currentValue,
        )
    }

    TitledWidget(title, AppColor.Peach) {
        SliderWidget(
            nonActiveProgress = nonActiveProgress,
            backgroundColor = AppColor.Peach_a6,
            progressColor = AppColor.LightPeach,
            indicatorColor = AppColor.DarkPeach,
            manager = manager,
            modifier = modifier,
            onValueChanged = onValueChanged,
        )
    }
}

@Composable
@Preview
private fun ValuesSliderWidgetPreview() {
    ValuesSliderWidget(
        title = "Количество элементов:",
        values = 2..10,
        currentValue = 5,
    )
}
