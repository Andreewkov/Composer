package ru.andreewkov.animations.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.andreewkov.animations.ui.screen.Screen
import ru.andreewkov.animations.ui.screen.ScreenId

class MainAppViewModel : ViewModel() {

    private val _currentTitle = MutableStateFlow(Screen.getStartScreen().title)
    val currentTitle get() = _currentTitle.asStateFlow()

    fun onCurrentBackStackCChanged(id: ScreenId?) {
        _currentTitle.update {
            Screen.findScreen(id).title
        }
    }
}
