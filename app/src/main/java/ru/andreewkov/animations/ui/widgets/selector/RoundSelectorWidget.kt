package ru.andreewkov.animations.ui.widgets.selector

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.andreewkov.animations.ui.screen.ScreenId
import ru.andreewkov.animations.ui.theme.AnimationsColor
import ru.andreewkov.animations.ui.utils.AnimationScopePreview
import ru.andreewkov.animations.ui.utils.AnimationsPreview
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.sin

private const val CENTER_CIRCLE_WEIGHT = 2F
private const val CIRCLE_IMAGE_FRACTION = 0.3f
private const val CIRCLE_DEGREES = 360F
private const val CIRCLE_DEFAULT_COEFFICIENT = 0f
private const val CIRCLE_CENTER_PRESSED_COEFFICIENT = 0.04f
private const val CIRCLE_PRESSED_COEFFICIENT = 0.08f
private const val ANIMATION_ANGLE_DURATION_MS = 120L

private typealias OnItemClick = (ScreenId) -> Unit
private typealias OnClick = () -> Unit
private typealias OnOutsideClick = () -> Unit
private typealias OnAnimationStarted = () -> Unit
private typealias OnSizeChanged = (IntSize) -> Unit

context(SharedTransitionScope, AnimatedVisibilityScope)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RoundSelectorWidget(
    centerColor: Color,
    items: List<CircleItem>,
    minCount: Int,
    onItemClick: OnItemClick,
    modifier: Modifier = Modifier,
    onOutsideClick: OnOutsideClick,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var centerCircleRadiusDp by remember { mutableStateOf(0.dp) }
    var extremeCircleRadiusDp by remember { mutableStateOf(0.dp) }
    val isStartAnimation = remember { mutableStateOf(false) }

    val density = LocalDensity.current.density

    val count = remember { max(items.size, minCount) }
    val angle = remember { CIRCLE_DEGREES / count }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onOutsideClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        CenterCircleContainer { modifierFactory ->
            Spacer(modifier = modifierFactory(1f))
            CenterCircle(
                radius = centerCircleRadiusDp,
                background = centerColor,
                onClick = {
                    isStartAnimation.value = true
                },
                modifier = modifierFactory(CENTER_CIRCLE_WEIGHT),
                onSizeChanged = { size ->
                    centerCircleRadiusDp = (size.width / density).dp / 2
                    extremeCircleRadiusDp = getExtremeCircleRadius(angle, centerCircleRadiusDp)
                },
            )
            Spacer(modifier = modifierFactory(1f))
        }
        ExtremeCircles(
            items = items,
            centerRadiusDp = centerCircleRadiusDp,
            extremeRadiusDp = extremeCircleRadiusDp,
            angle = angle,
            isAnimateCircles = isStartAnimation.value,
            onAnimationStarted = { isStartAnimation.value = false },
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun CenterCircleContainer(
    content: @Composable (modifierFactory: (Float) -> Modifier) -> Unit,
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            fun factory(weight: Float) = Modifier.weight(weight)
            content(::factory)
        }
    } else {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            fun factory(weight: Float) = Modifier.weight(weight)
            content(::factory)
        }
    }
}

@Composable
private fun ExtremeCircles(
    items: List<CircleItem>,
    centerRadiusDp: Dp,
    extremeRadiusDp: Dp,
    angle: Float,
    isAnimateCircles: Boolean,
    onAnimationStarted: OnAnimationStarted,
    onItemClick: OnItemClick,
) {
    val circleStates = remember { hashMapOf<Int, MutableStateFlow<CircleState>>() }
    val coroutineScope = rememberCoroutineScope()
    val animator = RoundSelectorAnimator(circleStates)

    if (isAnimateCircles) {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                animator.blink()
            }
            onAnimationStarted()
        }
    }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            animator.show()
        }
    }

    items.forEachIndexed { index, item ->
        val state = if (LocalInspectionMode.current) {
            remember { MutableStateFlow(CircleState.Open) }
        } else {
            remember { MutableStateFlow(CircleState.Hide) }
        }

        Circle(
            radius = extremeRadiusDp,
            backgroundColor = item.color,
            onClick = { onItemClick(item.id) },
            iconRes = item.icon,
            modifier = Modifier
                .size(extremeRadiusDp * 2)
                .rotate(angle * index)
                .absoluteOffset(x = -centerRadiusDp - extremeRadiusDp * 1.3f),
            angle = angle * index,
            stateFlow = state,
        )

        circleStates[index] = state
    }
}

private fun getExtremeCircleRadius(angle: Float, centerCircleRadiusDp: Dp): Dp {
    val centerAngleSin = (sin(angle / 2 * PI / CIRCLE_DEGREES * 2))
    val circleRadius = (centerAngleSin * centerCircleRadiusDp.value) / (1 - centerAngleSin)
    return circleRadius.dp
}

context(SharedTransitionScope, AnimatedVisibilityScope)
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
private fun CenterCircle(
    radius: Dp,
    background: Color,
    onClick: OnClick,
    modifier: Modifier = Modifier,
    onSizeChanged: OnSizeChanged,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val padding by animateDpAsState(
        targetValue = radius * when {
            isPressed -> CIRCLE_CENTER_PRESSED_COEFFICIENT
            else -> CIRCLE_DEFAULT_COEFFICIENT
        },
        animationSpec = TweenSpec(
            durationMillis = ANIMATION_ANGLE_DURATION_MS.toInt()
        ),
        label = "padding",
    )
    val boundsTransform = BoundsTransform { initialBounds, targetBounds ->
        keyframes {
            durationMillis = 300
            initialBounds at 0 using ArcMode.ArcBelow using FastOutSlowInEasing
            targetBounds at 300
        }
    }
    Surface(
        color = background,
        modifier = modifier
            .aspectRatio(1f)
            .onSizeChanged { size ->
                onSizeChanged(size)
            }
            .padding(padding)
            .sharedElement(
                rememberSharedContentState(key = "selector"),
                animatedVisibilityScope = this@AnimatedVisibilityScope,
                boundsTransform = boundsTransform,
            )
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
            )
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        bounded = true,
                        radius = 250.dp,
                        color = Color.DarkGray
                    ),
                ) { onClick() }
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(fraction = CIRCLE_IMAGE_FRACTION)
                    .align(Alignment.Center)
                    .sharedElement(
                        rememberSharedContentState(key = "icon"),
                        animatedVisibilityScope = this@AnimatedVisibilityScope,
                        boundsTransform = boundsTransform,
                    ),
            )
        }
    }
}

@Composable
private fun Circle(
    radius: Dp,
    backgroundColor: Color,
    onClick: OnClick,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    angle: Float = 0f,
    stateFlow: StateFlow<CircleState>,
    pressedCoefficient: Float = CIRCLE_PRESSED_COEFFICIENT,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val state = stateFlow.collectAsState()

    val padding by animateDpAsState(
        targetValue = radius * when {
            isPressed -> pressedCoefficient
            else -> state.value.sizeOffset
        },
        animationSpec = TweenSpec(
            durationMillis = ANIMATION_ANGLE_DURATION_MS.toInt()
        ),
        label = "padding",
    )
    if (!state.value.isHidden()) {
        Box(modifier = modifier.padding(padding)) {
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                    )
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(
                            bounded = true,
                            radius = 250.dp,
                            color = Color.Black
                        ),
                    ) { onClick() }
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(fraction = CIRCLE_IMAGE_FRACTION)
                        .align(Alignment.Center)
                        .rotate(-angle),
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@AnimationsPreview
@Composable
private fun RoundSelectorWidgetPreview() {
    AnimationScopePreview {
        RoundSelectorWidget(
            centerColor = AnimationsColor.Peach,
            items = generateCircleItems(9, AnimationsColor.LightPeach),
            minCount = 12,
            onItemClick = { },
            onOutsideClick = { },
            modifier = Modifier.padding(40.dp),
        )
    }
}
