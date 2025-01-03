package ru.andreewkov.composer.ui.widgets.util

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val UPDATE_TICK_MS = 100L

class UpdateTickerManager {

    private var lastLaunchedEffectTimeMs = 0L
    private var deferredUpdateJob: Job? = null

    suspend fun updateAtNextTick(update: () -> Unit) = coroutineScope {
        val now = System.currentTimeMillis()
        if (now - lastLaunchedEffectTimeMs < UPDATE_TICK_MS) {
            deferredUpdateJob = launch {
                delay(100)
                lastLaunchedEffectTimeMs = now
                update()
                deferredUpdateJob = null
            }
        } else {
            deferredUpdateJob?.cancel()
            deferredUpdateJob = null

            lastLaunchedEffectTimeMs = now
            update()
        }
    }
}