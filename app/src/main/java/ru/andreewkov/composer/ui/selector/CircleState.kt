package ru.andreewkov.composer.ui.selector

enum class CircleState(val sizeOffset: Float) {
    Open(0f),
    Blink(0.15f),
    Hide(1f),
    ;

    fun isHidden() = this == Hide
}
