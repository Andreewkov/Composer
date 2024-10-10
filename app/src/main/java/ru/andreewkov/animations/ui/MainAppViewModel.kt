package ru.andreewkov.animations.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.andreewkov.animations.ui.screen.Screen
import ru.andreewkov.animations.ui.screen.ScreenId

class MainAppViewModel : ViewModel() {

    private val _screenState = MutableStateFlow(
        if (Screen.iaSelectorExpandOnStart()) {
            ScreenState.SelectorExpand(Screen.getStartScreen(), Screen.getAll())
        } else {
            ScreenState.SelectorCompact(Screen.getStartScreen())
        }
    )
    val screenState get() = _screenState.asStateFlow()

    fun onSelectorItemClick(id: ScreenId) {
        _screenState.update {
            ScreenState.SelectorCompact(currentScreen = Screen.findScreen(id))
        }
    }

    fun onSelectorClick() {
        _screenState.update {
            ScreenState.SelectorExpand(
                currentScreen = screenState.value.currentScreen,
                items = Screen.getAll(),
            )
        }
    }

    fun onBackClick(): Boolean {
        return when (_screenState.value) {
            is ScreenState.SelectorExpand -> {
                _screenState.update {
                    ScreenState.SelectorCompact(currentScreen = screenState.value.currentScreen)
                }
                true
            }
            is ScreenState.SelectorCompact -> false
        }
    }

    sealed class ScreenState {

        abstract val currentScreen: Screen

        data class SelectorCompact(
            override val currentScreen: Screen,
        ) : ScreenState()

        data class SelectorExpand(
            override val currentScreen: Screen,
            val items: List<Screen>,
        ) : ScreenState()
    }
}