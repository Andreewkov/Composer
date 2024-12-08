package ru.andreewkov.composer.ui.slider

import androidx.annotation.FloatRange
import kotlin.math.abs
import kotlin.math.roundToInt

class ValuesSliderManager(
    private val values: IntRange,
    private val startValue: SliderValue,
    private val startProgress: SliderProgress,
) : SliderManager<SliderValue> {

    override val isActualValueShowed = true

    private var points = emptyList<SliderPoint>()

    override fun onSliderWidthChanged(sliderWidth: Int) {
        val count = values.last - values.first + 1
        val elementWidth = (sliderWidth.toFloat() / (count - 1)).roundToInt()
        this.points = List(count) { index: Int ->
            val value = index + values.first
            val position = when (index) {
                0 -> 0
                count - 1 -> sliderWidth
                else -> elementWidth * index
            }

            SliderPoint(
                value = value,
                progress = position.toFloat() / sliderWidth,
                position = position,
            )
        }
    }

    override fun findActualValue(
        @FloatRange(from = 0.0, to = 1.0) progress: SliderProgress,
        sliderWidth: Int,
    ): SliderValue {
        if (points.isEmpty()) return startValue
        return progress.roundToSliderValue(sliderWidth).value
    }

    override fun findActualProgress(
        @FloatRange(from = 0.0, to = 1.0) progress: SliderProgress,
        sliderWidth: Int,
    ): SliderProgress {
        if (points.isEmpty()) return startProgress
        return progress.roundToSliderValue(sliderWidth).progress
    }

    override fun findValuesPositions(sliderWidth: Int): List<SliderPosition> {
        return points
            .map { it.position }
            .filterNot { it == 0 || it == sliderWidth }
    }

    private fun Float.roundToSliderValue(sliderWidth: Int): SliderPoint {
        val currentWidth = (sliderWidth * this).roundToInt()

        return points.minBy { abs(currentWidth - it.position) }
    }

    private data class SliderPoint(
        val value: SliderValue,
        val progress: SliderProgress,
        val position: SliderPosition,
    )
}
