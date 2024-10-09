package ru.andreewkov.animations.ui.selector

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.andreewkov.animations.ui.screen.Screen
import ru.andreewkov.animations.ui.screen.ScreenId

class SelectorViewModel : ViewModel() {

    private val _navigationScreenState = MutableStateFlow<Screen>(Screen.getStartScreen())
    val navigationScreenState get() = _navigationScreenState.asSharedFlow()

    private val _screenState = MutableStateFlow(
        if (Screen.iaSelectorExpandOnStart()) {
            ScreenState.Expand(Screen.getAll())
        } else {
            ScreenState.Compact
        }
    )
    val screenState get() = _screenState.asStateFlow()

    fun onSelectorItemClick(id: ScreenId) {
        _navigationScreenState.update {
            Screen.findScreen(id) ?: Screen.getStartScreen()
        }
        _screenState.update {
            ScreenState.Compact
        }
    }

    fun onSelectorClick() {
        _screenState.update {
            ScreenState.Expand(Screen.getAll())
        }
    }

    fun onBackClick(): Boolean {
        return when (_screenState.value) {
            is ScreenState.Expand -> {
                _screenState.update {
                    ScreenState.Compact
                }
                true
            }
            ScreenState.Compact -> false
        }
    }

    sealed class ScreenState {

        data object Compact : ScreenState()

        data class Expand(val items: List<Screen>) : ScreenState()
    }
}