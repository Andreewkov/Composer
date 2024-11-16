package ru.andreewkov.composer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andreewkov.composer.ui.screen.Screen
import ru.andreewkov.composer.ui.screen.ScreenId

class MainAppViewModel : ViewModel() {

    private val _selectorState = MutableStateFlow(
        if (Screen.iaSelectorExpandOnStart()) {
            SelectorState.Expand(Screen.getAll(), isExpandedFromCompact = false,)
        } else {
            SelectorState.Compact
        }
    )
    private val _navigationScreenId = MutableSharedFlow<String>()
    val selectorState get() = _selectorState.asStateFlow()
    val navigationScreenId get() = _navigationScreenId.asSharedFlow()

    fun onSelectorItemClick(id: ScreenId) {
        _selectorState.update { SelectorState.Compact }
        viewModelScope.launch {
            _navigationScreenId.emit(id)
        }
    }

    fun onSelectorClick() {
        _selectorState.update {
            SelectorState.Expand(
                items = Screen.getAll(),
                isExpandedFromCompact = true,
            )
        }
    }

    fun onBackClick(): Boolean {
        val isExpand = selectorState.value.isExpand()
        if (isExpand) {
            _selectorState.update {
                SelectorState.Compact
            }
        }
        return isExpand
    }

    sealed class SelectorState {

        data object Compact: SelectorState()

        data class Expand(
            val items: List<Screen>,
            val isExpandedFromCompact: Boolean,
        ) : SelectorState()

        fun isExpand() = this is Expand

        fun isExpandFromCompact() = this is Expand && isExpandedFromCompact
    }
}