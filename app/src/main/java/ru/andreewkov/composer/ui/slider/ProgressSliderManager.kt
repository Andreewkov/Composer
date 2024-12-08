package ru.andreewkov.composer.ui.slider

import kotlin.math.round

class ProgressSliderManager : SliderManager<Float> {

    override val isActualValueShowed = false

    override fun onSliderWidthChanged(sliderWidth: Int) = Unit

    override fun findActualProgress(progress: SliderProgress, sliderWidth: Int) = progress

    override fun findActualValue(progress: SliderProgress, sliderWidth: Int) = round(progress * 100) / 100

    override fun findValuesPositions(sliderWidth: Int) = emptyList<SliderPosition>()
}
