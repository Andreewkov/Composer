package ru.andreewkov.animations.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColorScheme = lightColorScheme(
    primary = AnimationsColor.Purple40,
    secondary = AnimationsColor.PurpleGrey40,
    tertiary = AnimationsColor.Pink40,
    background = AnimationsColor.Dark,
)

@Composable
fun AnimationsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}