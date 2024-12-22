package ru.andreewkov.composer.ui.widgets

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.andreewkov.composer.ui.slider.SliderManager
import ru.andreewkov.composer.ui.theme.AppColor

@Composable
fun <T> SliderWidget(
    nonActiveProgress: Float,
    backgroundColor: Color,
    progressColor: Color,
    indicatorColor: Color,
    manager: SliderManager<T>,
    modifier: Modifier = Modifier,
    onValueChanged: (T) -> Unit = { },
) {
    val coroutineScope = rememberCoroutineScope()
    val progress = remember { Animatable(nonActiveProgress) }

    var sliderWidth by remember {
        mutableIntStateOf(0)
    }
    var cutoffPositions by remember {
        mutableStateOf(emptyList<Int>())
    }
    var actualValue by remember {
        mutableStateOf(manager.findActualValue(progress.value, sliderWidth))
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isDragged by interactionSource.collectIsDraggedAsState()

    val indicatorSElevation by animateIndicatorElevation(isActive = isDragged)

    Box(modifier = modifier) {
        Track(
            backgroundColor = backgroundColor,
            progressColor = progressColor,
            progress = progress.value,
            modifier = Modifier
                .padding(
                    vertical = 10.dp,
                    horizontal = 15.dp,
                )
                .onSizeChanged {
                    sliderWidth = it.width
                    manager.onSliderWidthChanged(it.width)
                    cutoffPositions = manager.findValuesPositions(it.width)
                }
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                }
                .drawWithContent {
                    drawContent()
                    cutoffPositions.forEach { position ->
                        makeCutoff(position.toFloat())
                    }
                }
                .align(Alignment.BottomStart)
        )
        IndicatorTrack(
            color = indicatorColor,
            progress = progress.value,
            value = if (manager.isActualValueShowed) {
                actualValue?.toString()
            } else {
                null
            },
            isDragged = isDragged,
            modifier = Modifier
                .draggable(
                    state = rememberDraggableState { delta ->
                        coroutineScope.launch {
                            val roundedProgress = progress.value.bound(sliderWidth, delta)
                            progress.snapTo(roundedProgress)

                            actualValue = manager.findActualValue(progress.value, sliderWidth)
                            onValueChanged(actualValue)
                        }
                    },
                    orientation = Orientation.Horizontal,
                    interactionSource = interactionSource,
                    startDragImmediately = true,
                    onDragStopped = {
                        progress.animateTo(
                            targetValue = manager.findActualProgress(progress.value, sliderWidth)
                        )

                        actualValue = manager.findActualValue(progress.value, sliderWidth)
                        onValueChanged(actualValue)
                    }
                ),
            shadowModifier = Modifier
                .shadow(
                    elevation = indicatorSElevation,
                    shape = CircleShape,
                )
        )
    }
}

@Composable
fun Track(
    backgroundColor: Color,
    progressColor: Color,
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.clip(CircleShape)) {
        TrackLine(
            color = backgroundColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
        TrackLine(
            color = progressColor,
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(10.dp)
        )
    }
}

@Composable
fun TrackLine(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color),
    )
}

@Composable
fun IndicatorTrack(
    color: Color,
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    modifier: Modifier = Modifier,
    shadowModifier: Modifier = Modifier,
    value: String? = null,
    isDragged: Boolean = false,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Indicator(
            color = color,
            value = value,
            isDragged = isDragged,
            modifier = modifier
                .align(
                    BiasAlignment(
                        horizontalBias = 2 * (progress - 0.5f),
                        verticalBias = 0f,
                    )
                ),
            shadowModifier = shadowModifier,
        )
    }
}

@Composable
fun Indicator(
    color: Color,
    value: String?,
    isDragged: Boolean,
    modifier: Modifier = Modifier,
    shadowModifier: Modifier = Modifier,
) {
    val scale by animateIndicatorScale(isActive = isDragged)
    Box(modifier.height(48.dp)) {
        if (value != null) {
            val verticalBias by animateIndicatorValueBias(isActive = isDragged || LocalInspectionMode.current)

            Text(
                text = "$value",
                color = AppColor.DarkPeach,
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AppColor.Peach_a6)
                    .padding(
                        start = 5.dp,
                        top = 1.dp,
                        end = 6.dp,
                        bottom = 2.dp,
                    ).align(
                        BiasAlignment(
                            horizontalBias = 0f,
                            verticalBias = verticalBias,
                        )
                    )
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(30.dp)
                .scale(scale)
                .then(shadowModifier)
                .clip(CircleShape)
                .background(color)
        )
    }
}

@Composable
private fun animateIndicatorScale(isActive: Boolean): State<Float> {
    return animateFloatAsState(
        targetValue = if (isActive) 0.9f else 0.7f,
        label = "indicatorScale"
    )
}

@Composable
private fun animateIndicatorValueBias(isActive: Boolean): State<Float> {
    return animateFloatAsState(
        targetValue = if (isActive) -1f else 0.5f,
        label = "indicatorScale"
    )
}

@Composable
private fun animateIndicatorElevation(isActive: Boolean): State<Dp> {
    return animateDpAsState(
        targetValue = if (isActive) 8.dp else 2.dp,
        label = "indicatorScale"
    )
}

@FloatRange(from = 0.0, to = 1.0)
fun Float.bound(
    width: Int,
    delta: Float,
): Float {
    val result = this + delta / width
    return when {
        result < 0 -> 0f
        result > 1 -> 1f
        else -> result
    }
}

@FloatRange(from = 0.0, to = 1.0)
fun calculateProgress(value: Int, values: IntRange): Float {
    val first = values.first
    val last = values.last
    return ((value.toFloat() - first) / (last - first))
}

private fun ContentDrawScope.makeCutoff(position: Float) {
    drawLine(
        blendMode = BlendMode.DstOut,
        color = Color.White,
        start = Offset(x = position, y = 0f),
        end = Offset(x = position, y = 100f),
        strokeWidth = 2f
    )
}

@Composable
@Preview
private fun ValuesSliderWidgetPreview() {
    ValuesSliderWidget(
        title = "Количество элементов:",
        values = 2..10,
        currentValue = 5,
    )
}

@Composable
@Preview
private fun SliderWidgetPreview() {
    ProgressSliderWidget(
        title = "Скорость:",
        progress = 0.3f,
    )
}
