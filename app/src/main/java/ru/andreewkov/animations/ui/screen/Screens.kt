package ru.andreewkov.animations.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import ru.andreewkov.animations.R
import ru.andreewkov.animations.ui.theme.AnimationsColor

typealias ScreenId = String

sealed class Screen(
    val id: ScreenId,
    val title: String,
    val selectorStyle: SelectorStyle,
) {

    data object Animation : Screen(
        id = "anim",
        title = "Round Progress",
        selectorStyle = SelectorStyle(
            icon = R.drawable.ic_play,
            color = AnimationsColor.LightPeach,
        )
    )

    data object Animation2 : Screen(
        id = "anim2",
        title = "Round Progress",
        selectorStyle = SelectorStyle(
            icon = R.drawable.ic_globe,
            color = AnimationsColor.LightPeach,
        )
    )

    companion object {

        fun getStartScreen() = Animation

        fun iaSelectorExpandOnStart() = true

        fun findScreen(id: ScreenId?): Screen {
            return getAll().find { it.id == id } ?: getStartScreen()
        }

        fun getAll() = listOf(
            Animation,
            Animation2,
            Animation,
            Animation2,
            Animation,
            Animation2,
            Animation,
            Animation2,
        )
    }
}

data class SelectorStyle(
    @DrawableRes val icon: Int,
    val color: Color,
)
