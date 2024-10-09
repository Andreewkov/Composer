package ru.andreewkov.animations.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.andreewkov.animations.ui.screen.Screen

class MainAppViewModel : ViewModel() {

    private val _currentTitle = MutableStateFlow("")
    val currentTitle get() = _currentTitle.asStateFlow()

    fun onCurrentBackStackCChanged(route: String?) {
        _currentTitle.update {
            Screen.findScreen(route)?.title ?: ""
        }
    }
}