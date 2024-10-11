package ru.andreewkov.animations.ui.selector

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import ru.andreewkov.animations.R
import ru.andreewkov.animations.ui.screen.Screen
import kotlin.random.Random

data class CircleItem(
    val id: String,
    @DrawableRes val icon: Int,
    val color: Color,
)

fun List<Screen>.mapToCircleItems(): List<CircleItem> {
    return map { it.mapToCircleItem() }
}

fun Screen.mapToCircleItem(): CircleItem {
    return CircleItem(id, selectorStyle.icon, selectorStyle.color)
}

fun generateCircleItems(count: Int, background: Color): List<CircleItem> {
    return mutableListOf<CircleItem>().also { list ->
        for (i in 0 until count) {
            list.add(generateCircleItem(background))
        }
    }
}

private fun generateCircleItem(background: Color): CircleItem {
    return CircleItem(
        id = Random.nextInt().toString(),
        color = background,
        icon = mockedIcons[Random.nextInt(from = 0, until = mockedIcons.size)]
    )
}

private val mockedIcons = arrayOf(
    R.drawable.ic_burger,
    R.drawable.ic_cross,
    R.drawable.ic_play,
    R.drawable.ic_globe,
)
