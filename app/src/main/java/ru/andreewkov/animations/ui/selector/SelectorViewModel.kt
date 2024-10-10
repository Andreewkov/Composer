package ru.andreewkov.animations.ui.selector

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.andreewkov.animations.ui.screen.Screen
import ru.andreewkov.animations.ui.screen.ScreenId

class SelectorViewModel : ViewModel() {

    private val _screenState = MutableStateFlow(
        if (Screen.iaSelectorExpandOnStart()) {
            ScreenState.Expand(Screen.getStartScreen(), Screen.getAll())
        } else {
            ScreenState.Compact(Screen.getStartScreen())
        }
    )
    val screenState get() = _screenState.asStateFlow()

    fun onSelectorItemClick(id: ScreenId) {
        _screenState.update {
            ScreenState.Compact(currentScreen = Screen.findScreen(id))
        }
    }

    fun onSelectorClick() {
        _screenState.update {
            ScreenState.Expand(
                currentScreen = screenState.value.currentScreen,
                items = Screen.getAll(),
            )
        }
    }

    fun onBackClick(): Boolean {
        return when (_screenState.value) {
            is ScreenState.Expand -> {
                _screenState.update {
                    ScreenState.Compact(currentScreen = screenState.value.currentScreen)
                }
                true
            }
            is ScreenState.Compact -> false
        }
    }

    sealed class ScreenState {

        abstract val currentScreen: Screen

        data class Compact(
            override val currentScreen: Screen,
        ) : ScreenState()

        data class Expand(
            override val currentScreen: Screen,
            val items: List<Screen>,
        ) : ScreenState()
    }
}