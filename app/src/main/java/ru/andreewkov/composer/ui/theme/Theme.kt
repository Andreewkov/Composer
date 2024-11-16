package ru.andreewkov.composer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColorScheme = lightColorScheme(
    primary = AppColor.Purple40,
    secondary = AppColor.PurpleGrey40,
    tertiary = AppColor.Pink40,
    background = AppColor.Dark,
)

@Composable
fun ComposerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}