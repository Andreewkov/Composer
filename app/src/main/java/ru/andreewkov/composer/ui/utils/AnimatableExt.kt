package ru.andreewkov.composer.ui.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

suspend fun Animatable<Float, AnimationVector1D>.runCircleAnimation(
    speed: Float,
    lastRotate: Float,
    defaultDuration: Long,
    fullTurnover: Float,
    block: (Float) -> Unit,
) {
    run {
        animateTo(
            targetValue = fullTurnover + lastRotate,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = calculateAnimationDurationMillis(speed, defaultDuration),
                    easing = LinearEasing,
                )
            )
        ) {
            block(value)
        }
    }
}

private fun calculateAnimationDurationMillis(speed: Float, defaultDuration: Long): Int {
    return ((1 - (sin(speed / 2 * PI))) * defaultDuration).roundToInt() + 100
}