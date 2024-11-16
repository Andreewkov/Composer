package ru.andreewkov.composer.ui.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

context(CoroutineScope)
fun <T> Flow<T>.observe(action: suspend (T) -> Unit) {
    onEach { action(it) }.launchIn(this@CoroutineScope)
}