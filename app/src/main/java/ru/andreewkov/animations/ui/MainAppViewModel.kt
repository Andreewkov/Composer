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
            ScreenState.SelectorExpand(isExpanded = false, Screen.getAll())
        } else {
            ScreenState.SelectorCompact
        }
    )
    private val _currentScreen = MutableStateFlow<Screen>(Screen.getStartScreen())
    val screenState get() = _screenState.asStateFlow()
    val currentScreen get() = _currentScreen.asStateFlow()

    fun isSelectorExpanded(): Boolean {
        val expandState = _screenState.value as? ScreenState.SelectorExpand ?: return false
        return expandState.isExpanded
    }

    fun onSelectorItemClick(id: ScreenId) {
        _screenState.update { ScreenState.SelectorCompact }
        if (currentScreen.value.id != id) {
            _currentScreen.update { Screen.findScreen(id) }
        }
    }

    fun onSelectorClick() {
        _screenState.update {
            ScreenState.SelectorExpand(
                isExpanded = true,
                items = Screen.getAll(),
            )
        }
    }

    fun onBackClick(): Boolean {
        return when (_screenState.value) {
            is ScreenState.SelectorExpand -> {
                _screenState.update {
                    ScreenState.SelectorCompact
                }
                true
            }
            is ScreenState.SelectorCompact -> false
        }
    }

    sealed class ScreenState {

        data object SelectorCompact: ScreenState()

        data class SelectorExpand(
            val isExpanded: Boolean,
            val items: List<Screen>,
        ) : ScreenState()
    }
}