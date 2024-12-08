package ru.andreewkov.composer.ui.slider

import androidx.annotation.FloatRange

typealias SliderValue = Int
typealias SliderPosition = Int
typealias SliderProgress = Float

interface SliderManager<T> {

    val isActualValueShowed: Boolean

    fun onSliderWidthChanged(sliderWidth: Int)

    fun findActualProgress(
        @FloatRange(from = 0.0, to = 1.0) progress: SliderProgress,
        sliderWidth: Int,
    ): SliderProgress

    fun findActualValue(
        @FloatRange(from = 0.0, to = 1.0) progress: SliderProgress,
        sliderWidth: Int,
    ): T

    fun findValuesPositions(sliderWidth: Int): List<SliderPosition>
}
