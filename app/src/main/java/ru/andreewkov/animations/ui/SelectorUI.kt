package ru.andreewkov.animations.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.andreewkov.animations.ui.screen.Screen
import ru.andreewkov.animations.ui.screen.ScreenId
import ru.andreewkov.animations.ui.selector.CircleState
import ru.andreewkov.animations.ui.selector.RoundSelectorAnimator
import ru.andreewkov.animations.ui.selector.RoundSelectorWidget
import ru.andreewkov.animations.ui.selector.mapToCircleItems
import ru.andreewkov.animations.ui.theme.AnimationsColor
import ru.andreewkov.animations.ui.utils.AnimationsPreview
import ru.andreewkov.animations.ui.utils.Preview

private typealias OnSelectorClick = () -> Unit
private typealias OnItemClick = (ScreenId) -> Unit
private typealias OnOutsideClick = () -> Unit
private typealias OnBackClick = () -> Unit

@Composable
fun SelectorWidget(modifier: Modifier = Modifier) {
    val viewModel: MainAppViewModel = viewModel()

    SelectorContent(
        selectorStateFlow = viewModel.selectorState,
        modifier = modifier,
        onItemClick = viewModel::onSelectorItemClick,
        onBackClick = viewModel::onBackClick,
        onSelectorClick = viewModel::onSelectorClick,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SelectorContent(
    selectorStateFlow: StateFlow<MainAppViewModel.SelectorState>,
    modifier: Modifier = Modifier,
    onItemClick: OnItemClick = { },
    onBackClick: OnBackClick = { },
    onSelectorClick: OnSelectorClick = { },
) {
    val selectorState by selectorStateFlow.collectAsState()
    BackHandler(enabled = selectorState is MainAppViewModel.SelectorState.Expand) {
        onBackClick()
    }

    val animator = remember {
        val state = if (selectorState.isExpandFromCompact()) {
            CircleState.Open
        } else {
            CircleState.Hide
        }
        RoundSelectorAnimator(state)
    }
    SharedTransitionLayout {
        AnimatedContent(
            targetState = selectorState,
            label = "selector",
        ) { targetState ->
            Box(modifier = modifier.fillMaxSize()) {
                when (targetState) {
                    is MainAppViewModel.SelectorState.Compact -> {
                        animator.hideCircles()
                        SelectorCompactContent(
                            onSelectorClick = onSelectorClick,
                        )
                    }

                    is MainAppViewModel.SelectorState.Expand -> {
                        SelectorExpandContent(
                            onItemClick = onItemClick,
                            items = Screen.getAll(),
                            onOutsideClick = onBackClick,
                            animator = animator,
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
    animator: RoundSelectorAnimator,
    modifier: Modifier = Modifier,
) {
    RoundSelectorWidget(
        centerColor = AnimationsColor.Peach,
        items = items.mapToCircleItems(),
        minCount = 10,
        onItemClick = onItemClick,
        modifier = modifier.padding(40.dp),
        onOutsideClick = onOutsideClick,
        animator = animator,
    )
}

@AnimationsPreview
@Composable
private fun SelectorExpandPreview() {
    SelectorPreview(
        startState = MainAppViewModel.SelectorState.Expand(
            isExpandedFromCompact = false,
            items = Screen.getAll()),
        )
}

@AnimationsPreview
@Composable
private fun SelectorCompatPreview() {
    Preview {
        SelectorPreview(
            startState = MainAppViewModel.SelectorState.Compact
        )
    }
}

@Composable
private fun SelectorPreview(
    startState: MainAppViewModel.SelectorState,
) {
    val selectorState = MutableStateFlow(startState)
    Preview {
        SelectorContent(
            selectorStateFlow = selectorState,
            onItemClick = {
                selectorState.value = MainAppViewModel.SelectorState.Compact
            },
            onBackClick = {
                selectorState.value = MainAppViewModel.SelectorState.Compact
            },
            onSelectorClick = {
                selectorState.value = MainAppViewModel.SelectorState.Expand(
                    isExpandedFromCompact = true,
                    items = Screen.getAll(),
                )
            }
        )
    }
}
