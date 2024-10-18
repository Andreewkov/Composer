package ru.andreewkov.animations.ui.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.andreewkov.animations.ui.theme.AnimationsColor
import ru.andreewkov.animations.ui.utils.WidgetPreviewBox

private const val ROUND_PROGRESS_DEFAULT_DURATION = 1400
private const val PROGRESS_WIDTH_COEFFICIENT = 0.2f
private const val PROGRESS_PADDING_COEFFICIENT = 0.2f
private const val START_INITIAL_VALUE = 360f
private const val START_TARGET_VALUE = 0F
private const val SWEEP_INITIAL_VALUE = 200f
private const val SWEEP_TARGET_VALUE = 20F

@Composable
fun RoundProgressWidget(
    colors: List<Color>,
    modifier: Modifier = Modifier,
    duration: Int = ROUND_PROGRESS_DEFAULT_DURATION,
) {
    val transition = rememberInfiniteTransition(label = "progress")

    val startValue by transition.animateFloat(
        initialValue = START_INITIAL_VALUE,
        targetValue = START_TARGET_VALUE,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                delayMillis = 0,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "startValue",
    )
    val sweepValue by transition.animateFloat(
        initialValue = SWEEP_INITIAL_VALUE,
        targetValue = SWEEP_TARGET_VALUE,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                delayMillis = 0,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sweepValue",
    )

    RoundProgressContent(
        colors = colors,
        startAngle = startValue,
        sweepAngle = sweepValue,
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
            .onSizeChanged { size = it }
            .aspectRatio(1f)
            .padding(size.width.dp * PROGRESS_WIDTH_COEFFICIENT * PROGRESS_PADDING_COEFFICIENT)
    ) {
        drawArc(
            brush = Brush.sweepGradient(colors),
            startAngle = startAngle,
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
            colors = listOf(AnimationsColor.Peach, AnimationsColor.LightPeach, AnimationsColor.Peach),
        )
    }
}


data class StrokeStyle(
    val width: Dp = 4.dp,
    val strokeCap: StrokeCap = StrokeCap.Round,
    val glowRadius: Dp? = 4.dp
)

@Composable
fun CircleLoader(
    modifier: Modifier,
    isVisible: Boolean,
    color: Color,
    secondColor: Color? = color,
    tailLength: Float = 140f,
    smoothTransition: Boolean = true,
    strokeStyle: StrokeStyle = StrokeStyle(),
    cycleDuration: Int = 1400,
) {
    val transition = rememberInfiniteTransition()
    val spinAngel by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = cycleDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val tailToDisplay = tailLength// { Animatable(0f) }

    /*    LaunchedEffect(isVisible) {
            val targetTail = if (isVisible) tailLength else 0f
            when {
                smoothTransition -> tailToDisplay.animateTo(
                    targetValue = targetTail,
                    animationSpec = tween(cycleDuration, easing = LinearEasing)
                )
                else -> tailToDisplay.snapTo(targetTail)
            }
        }*/

    Canvas(
        modifier
            // Apply rotation animation
            .rotate(spinAngel)
            // Ensure the CircleLoader maintains a square aspect ratio
            .aspectRatio(1f)
    ) {
        // Iterate over non-null colors
        listOfNotNull(color, secondColor).forEachIndexed { index, color ->
            // If it's not a primary color we rotate the canvas for 180 degrees
            rotate(if (index == 0) 0f else 180f) {
                // Create a sweep gradient brush for the loader
                val brush = Brush.sweepGradient(
                    0f to Color.Transparent,
                    tailToDisplay / 360f to color,
                    1f to Color.Transparent
                )
                // Set up paint object
                val paint = setupPaint(strokeStyle, brush)

                // Draw the loader tail
                drawIntoCanvas { canvas ->
                    canvas.drawArc(
                        rect = size.toRect(),
                        startAngle = 0f,
                        sweepAngle = tailToDisplay,
                        useCenter = false,
                        paint = paint
                    )
                }
            }
        }
    }
}

fun DrawScope.setupPaint(style: StrokeStyle, brush: Brush): Paint {
    val paint = Paint().apply paint@{
        this@paint.isAntiAlias = true
        this@paint.style = PaintingStyle.Stroke
        this@paint.strokeWidth = style.width.toPx()
        this@paint.strokeCap = style.strokeCap

        brush.applyTo(size, this@paint, 1f)
    }

    style.glowRadius?.let { radius ->
        paint.asFrameworkPaint().setShadowLayer(
            /* radius = */ radius.toPx(),
            /* dx = */ 0f,
            /* dy = */ 0f,
            /* shadowColor = */ android.graphics.Color.WHITE
        )
    }

    return paint
}