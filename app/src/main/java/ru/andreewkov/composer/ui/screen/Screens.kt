package ru.andreewkov.composer.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.andreewkov.composer.ui.theme.AppColor
import ru.andreewkov.composer.ui.utils.ComposerPreview
import ru.andreewkov.composer.ui.utils.Preview
import ru.andreewkov.composer.ui.widgets.ProgressSliderWidget
import ru.andreewkov.composer.ui.widgets.RoundLoaderWidget
import ru.andreewkov.composer.ui.widgets.ValuesSliderWidget


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

@Composable
fun SliderScreenUI() {
    var first by remember { mutableStateOf(4) }
    var second by remember { mutableStateOf(0.4f) }
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "$first", color = AppColor.Peach)
        Spacer(modifier = Modifier.height(40.dp))
        ValuesSliderWidget(
            title = "Количество элементов:",
            values = 2..10,
            currentValue = first,
            modifier = Modifier.padding(horizontal = 8.dp),
            onValueChanged = { first = it }
        )
        Spacer(modifier = Modifier.height(100.dp))
        Text(text = "$second", color = AppColor.Peach)
        Spacer(modifier = Modifier.height(40.dp))
        ProgressSliderWidget(
            title = "Скорость:",
            progress = second,
            modifier = Modifier.padding(horizontal = 8.dp),
            onProgressChanged = { second = it }
        )
    }
}

@ComposerPreview
@Composable
private fun RoundLoaderScreenUIPreview() {
    Preview {
        RoundLoaderScreenUI()
    }
}

@ComposerPreview
@Composable
private fun SliderScreenUIPreview() {
    Preview {
        SliderScreenUI()
    }
}