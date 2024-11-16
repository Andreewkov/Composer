package ru.andreewkov.composer.ui.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.andreewkov.composer.ui.theme.AppColor
import ru.andreewkov.composer.ui.utils.WidgetPreviewBox

@Composable
fun RoundLoaderWidget(
    itemCount: Int,
    color: Color,
) {
    val itemDegrees = remember { 360f / itemCount }
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition()
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = itemDegrees,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rotation"
    )
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .rotate(rotation)
            .onSizeChanged {
                size = it
            },
    ) {
        Loader(
            itemCount = itemCount,
            itemSize =  size.width / 4f,
            itemDegrees = itemDegrees,
            color = color,
        )
    }
}

@Composable
private fun Loader(itemCount: Int, itemSize: Float, itemDegrees: Float, color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val path = createLoaderPath(itemSize)
        val paint = createLoaderPaint(itemSize, color)

        for (i: Int in 0 until itemCount) {
            drawItem(i, itemSize, itemDegrees, path, paint)
        }
    }
}

private fun createLoaderPath(size: Float): Path {
    return Path().apply {
        moveTo(size / 4f, 0f)
        lineTo(size / 2.7f, size)
        lineTo(size - size / 2.7f, size)
        lineTo(size - size / 4, 0f)
        close()
    }
}

private fun createLoaderPaint(size: Float, color: Color): Paint {
    return Paint().apply {
        this.color = color
        this.pathEffect = PathEffect.cornerPathEffect(size / 6)
    }
}

fun DrawScope.drawItem(index: Int, itemSize: Float, itemDegrees: Float, path: Path, paint: Paint) {
    withTransform({
        rotate(degrees = itemDegrees * index)
        translate(left = size.width / 2f - itemSize / 2f)
    }) {
        drawIntoCanvas { canvas ->
            canvas.drawOutline(
                outline = Outline.Generic(path),
                paint = paint,
            )
        }
    }
}

@Composable
@Preview
private fun RoundLoaderWidgetPreview() {
    WidgetPreviewBox {
        RoundLoaderWidget(12, AppColor.Peach)
    }
}

@Composable
@Preview
private fun LoaderItemPreview() {
    var size by remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = Modifier.onSizeChanged { size = it },
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val path = createLoaderPath(size.width.toFloat())
            val paint = createLoaderPaint(size.width.toFloat(), AppColor.Peach)
            drawIntoCanvas { canvas ->
                canvas.drawOutline(
                    outline = Outline.Generic(path),
                    paint = paint,
                )
            }
        }
    }
}
