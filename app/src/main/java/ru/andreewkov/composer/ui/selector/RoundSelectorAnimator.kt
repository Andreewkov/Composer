package ru.andreewkov.composer.ui.selector

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

private const val ANIMATION_BLINK_START_DELAY_MS = 100L
private const val ANIMATION_SHOW_START_DELAY_MS = 300L
private const val ANIMATION_BLINK_DURATION_MS = 120L
private const val ANIMATION_SHOW_DELAY_MS = 60L

class RoundSelectorAnimator(var startState: CircleState) {

    private var states: MutableMap<Int, MutableStateFlow<CircleState>> = mutableMapOf()

    fun addCircle(index: Int, state: MutableStateFlow<CircleState>) {
        states[index] = state
    }

    fun hideCircles() {
        startState = CircleState.Hide
        states = mutableMapOf()
    }

    suspend fun blink() {
        delay(ANIMATION_BLINK_START_DELAY_MS)
        for (index in states.values.indices) {
            val currentState = states[index]
            currentState?.value = CircleState.Blink
            delay(ANIMATION_BLINK_DURATION_MS)
            currentState?.value = CircleState.Open
        }
    }

    suspend fun show() {
        delay(ANIMATION_SHOW_START_DELAY_MS)
        for (index in states.values.indices) {
            val currentState = states[index]
            currentState?.value = CircleState.Open
            delay(ANIMATION_SHOW_DELAY_MS)
        }
    }
}
