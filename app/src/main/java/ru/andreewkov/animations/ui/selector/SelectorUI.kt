package ru.andreewkov.animations.ui.selector

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.andreewkov.animations.ui.screen.Screen
import ru.andreewkov.animations.ui.theme.AnimationsColor
import ru.andreewkov.animations.ui.utils.AnimationsPreview
import ru.andreewkov.animations.ui.utils.Preview
import ru.andreewkov.animations.ui.widgets.selector.RoundSelectorWidget
import ru.andreewkov.animations.ui.widgets.selector.mapToCircleItems

typealias OnSelectorExpand = (Boolean) -> Unit
typealias OnSelectorClick = () -> Unit
typealias OnItemClick = (String) -> Unit
typealias OnOutsideClick = () -> Unit

@Composable
fun SelectorWidget(
    navController: NavController,
    onSelectorExpand: OnSelectorExpand,
    modifier: Modifier = Modifier,
) {
    val viewModel: SelectorViewModel = viewModel()
    val screenState by viewModel.screenState.collectAsState()
    val currentScreen by remember { mutableStateOf(screenState.currentScreen) }

    LaunchedEffect(currentScreen) {
        navController.navigate(currentScreen.id)
    }

    BackHandler(enabled = screenState is SelectorViewModel.ScreenState.Expand) {
        viewModel.onBackClick()
    }

    SelectorContent(
        screenState = screenState,
        onItemClick = viewModel::onSelectorItemClick,
        onSelectorExpand = onSelectorExpand,
        onOutsideClick = viewModel::onBackClick,
        onSelectorClick = viewModel::onSelectorClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SelectorContent(
    screenState: SelectorViewModel.ScreenState,
    modifier: Modifier = Modifier,
    onSelectorExpand: OnSelectorExpand = { },
    onItemClick: OnItemClick = { },
    onOutsideClick: OnOutsideClick = { },
    onSelectorClick: OnSelectorClick = { },
) {
    SharedTransitionLayout {
        AnimatedContent(
            targetState = screenState,
            label = "selector",
        ) { targetState ->
            Box(modifier = modifier.fillMaxSize()) {
                when (targetState) {
                    is SelectorViewModel.ScreenState.Compact -> {
                        onSelectorExpand(false)
                        SelectorCompactContent(
                            onSelectorClick = onSelectorClick,
                        )
                    }

                    is SelectorViewModel.ScreenState.Expand -> {
                        onSelectorExpand(true)
                        SelectorExpandContent(
                            onItemClick = onItemClick,
                            items = Screen.getAll(),
                            onOutsideClick = onOutsideClick,
                        )
                    }
                }
            }
        }
    }
}

context(SharedTransitionScope, AnimatedVisibilityScope)
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
private fun BoxScope.SelectorCompactContent(
    onSelectorClick: OnSelectorClick,
) {
    val boundsTransform = BoundsTransform { initialBounds, targetBounds ->
        keyframes {
            durationMillis = 300
            initialBounds at 0 using ArcMode.ArcBelow using FastOutSlowInEasing
            targetBounds at 300
        }
    }
    FloatingActionButton(
        onClick = onSelectorClick,
        shape = CircleShape,
        containerColor = AnimationsColor.Peach,
        modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(all = 32.dp)
            .sharedElement(
                rememberSharedContentState(key = "selector"),
                animatedVisibilityScope = this@AnimatedVisibilityScope,
                boundsTransform = boundsTransform,
            ),
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = null,
            modifier = Modifier.sharedElement(
                rememberSharedContentState(key = "icon"),
                animatedVisibilityScope = this@AnimatedVisibilityScope,
                boundsTransform = boundsTransform,
            )
        )
    }
}

context(SharedTransitionScope, AnimatedVisibilityScope)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SelectorExpandContent(
    onItemClick: OnItemClick,
    onOutsideClick: OnOutsideClick,
    items: List<Screen>,
    modifier: Modifier = Modifier,
) {
    RoundSelectorWidget(
        centerColor = AnimationsColor.Peach,
        items = items.mapToCircleItems(),
        minCount = 10,
        onItemClick = onItemClick,
        modifier = modifier.padding(40.dp),
        onOutsideClick = onOutsideClick,
    )
}

@AnimationsPreview
@Composable
private fun SelectorExpandPreview() {
    SelectorPreview(
        startState = SelectorViewModel.ScreenState.Expand(
            currentScreen = Screen.getStartScreen(),
            items = Screen.getAll()),
        )
}

@AnimationsPreview
@Composable
private fun SelectorCompatPreview() {
    Preview {
        SelectorPreview(
            startState = SelectorViewModel.ScreenState.Compact(
                currentScreen = Screen.getStartScreen(),
            )
        )
    }
}

@Composable
private fun SelectorPreview(
    startState: SelectorViewModel.ScreenState,
) {
    var screenState by remember { mutableStateOf(startState) }
    Preview {
        SelectorContent(
            screenState = screenState,
            onItemClick = {
                screenState = SelectorViewModel.ScreenState.Compact(Screen.getStartScreen())
            },
            onOutsideClick = {
                screenState = SelectorViewModel.ScreenState.Compact(Screen.getStartScreen())
            },
            onSelectorClick = {
                screenState = SelectorViewModel.ScreenState.Expand(Screen.getStartScreen(), Screen.getAll())
            }
        )
    }
}
