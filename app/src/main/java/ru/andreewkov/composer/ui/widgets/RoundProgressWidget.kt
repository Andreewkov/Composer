package ru.andreewkov.composer.ui.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.andreewkov.composer.ui.theme.AppColor
import ru.andreewkov.composer.ui.utils.WidgetPreviewBox
import ru.andreewkov.composer.ui.utils.runCircleAnimation
import ru.andreewkov.composer.ui.widgets.util.UpdateTickerManager

private const val PROGRESS_DEFAULT_DURATION = 4000L
private const val PROGRESS_WIDTH_COEFFICIENT = 0.2f
private const val PROGRESS_PADDING_COEFFICIENT = 0.2f
private const val PROGRESS_MIN_SPEED = 0.01f
private const val CIRCLE_DEGREES = 360F
private const val SWEEP = 360f

@Composable
fun RoundProgressWidget(
    colors: List<Color>,
    modifier: Modifier = Modifier,
    speed: Float = 0.5f,
    sweep: Float = 0.5f,
) {
    var lastRotate by remember { mutableStateOf(0f) }
    var currentSpeed by remember { mutableStateOf(speed) }
    val updateTickManager = remember { UpdateTickerManager() }
    val rotateAnimatable = remember { Animatable(0f) }

    LaunchedEffect(speed) {
        updateTickManager.updateAtNextTick {
            currentSpeed = speed
        }
    }

    LaunchedEffect(currentSpeed) {
        if (currentSpeed < PROGRESS_MIN_SPEED) {
            rotateAnimatable.stop()
        } else {
            rotateAnimatable.runCircleAnimation(
                speed = currentSpeed,
                lastRotate = lastRotate,
                defaultDuration = PROGRESS_DEFAULT_DURATION,
                fullTurnover = CIRCLE_DEGREES,
                block = { lastRotate = it }
            )
        }
    }

    RoundProgressContent(
        colors = colors,
        startAngle = lastRotate,
        sweepAngle = sweep * SWEEP,
        modifier = modifier,
    )
}

@Composable
fun RoundProgressContent(
    colors: List<Color>,
    startAngle: Float,
    sweepAngle: Float,
    modifier: Modifier = Modifier,
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    Canvas(
        modifier = modifier
            .rotate(startAngle)
            .onSizeChanged { size = it }
            .aspectRatio(1f)
            .padding(size.width.dp * PROGRESS_WIDTH_COEFFICIENT * PROGRESS_PADDING_COEFFICIENT)
    ) {
        drawArc(
            brush = Brush.sweepGradient(colors),
            startAngle = 0f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(
                size.width * PROGRESS_WIDTH_COEFFICIENT,
                cap = StrokeCap.Round,
                miter = 0f
            )
        )
    }
}

@Preview
@Composable
private fun RoundProgressWidgetPreview() {
    WidgetPreviewBox {
        RoundProgressWidget(
            colors = listOf(AppColor.Peach, AppColor.LightPeach, AppColor.Peach),
        )
    }
}
