package ru.andreewkov.animations.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.andreewkov.animations.ui.theme.AnimationsColor
import ru.andreewkov.animations.ui.utils.WidgetPreviewBox

@Composable
fun RoundLoaderWidget() {
    Loader()
}

@Composable
private fun Loader() {
    var size by remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged {
                size = it
            },
        contentAlignment = Alignment.Center,
    ) {
        for (i: Int in 0..11) {
            LoaderItem(modifier = Modifier
                .size(size.width.dp / 12)
                .rotate(30f * i)
                .offset(y = -size.height.dp / 8)
            )
        }
    }
}

@Composable
private fun LoaderItem(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val path = Path().apply {
            moveTo(size.width / 8f, 0f)
            lineTo(size.width / 4, size.height)
            lineTo(size.width - size.width / 9, size.height)
            lineTo(size.width - size.width / 2.5f, 0f)
            close()
        }
        val paint =  Paint().apply {
            color = AnimationsColor.Peach
            pathEffect = PathEffect.cornerPathEffect(60f)
        }

        drawIntoCanvas { canvas ->
            canvas.drawOutline(
                outline = Outline.Generic(path),
                paint = paint
            )
        }
    }
}

@Composable
@Preview
private fun RoundLoaderWidgetPreview() {
    WidgetPreviewBox {
        RoundLoaderWidget()
    }
}

@Composable
@Preview
private fun LoaderItemPreview() {
    WidgetPreviewBox {
        LoaderItem()
    }
}
