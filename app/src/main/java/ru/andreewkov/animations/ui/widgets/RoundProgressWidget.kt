package ru.andreewkov.animations.ui.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.andreewkov.animations.ui.theme.AnimationsColor

private const val ROUND_PROGRESS_DEFAULT_DURATION = 1000

@Composable
fun RoundProgressWidget(
    progressWidth: Dp,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    duration: Int = ROUND_PROGRESS_DEFAULT_DURATION,
) {
    val transition = rememberInfiniteTransition(label = "progress")

    val startValue by transition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
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
        initialValue = 200f,
        targetValue = 20f,
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
        progressWidth = progressWidth,
        colors = colors,
        startAngle = startValue,
        sweepAngle = sweepValue,
        modifier = modifier,
    )
}

@Composable
fun RoundProgressContent(
    progressWidth: Dp,
    colors: List<Color>,
    startAngle: Float,
    sweepAngle: Float,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .padding(progressWidth / 2)
            .aspectRatio(1f)
    ) {
        drawArc(
            brush = Brush.sweepGradient(colors),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(
                progressWidth.toPx(),
                cap = StrokeCap.Round,
                miter = 0f
            )
        )
    }
}

private fun createBrush(): Brush {
    return Brush.horizontalGradient()
}

@Preview
@Composable
private fun RoundProgressContentPreview() {
/*    RoundProgressContent(
        radius = 50.dp,
        progressWidth = 10.dp,
        color = AnimationsColor.Peach,
    )*/
}

@Preview
@Composable
private fun RoundProgressWidgetPreview() {
    Box(modifier = Modifier.size(300.dp)) {
        RoundProgressWidget(
            progressWidth = 10.dp,
            colors = listOf(AnimationsColor.Peach),
        )
    }
}

@Preview
@Composable
private fun CircleLoaderPreview() {
    CircleLoader(
        color = Color(0xFF1F79FF),
        secondColor = Color(0xFFFFE91F),
        modifier = Modifier
            .size(200.dp)
            .padding(bottom = 200.dp)
        ,
        isVisible = true,
    )
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
){
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