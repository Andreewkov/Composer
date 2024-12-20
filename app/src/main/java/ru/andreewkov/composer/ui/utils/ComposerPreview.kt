package ru.andreewkov.composer.ui.utils

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.andreewkov.composer.ui.theme.AppColor
import ru.andreewkov.composer.ui.theme.ComposerTheme

@Preview(
    device = "spec:width=411dp,height=891dp,dpi=420,orientation=portrait",
)
@Preview(
    device = "spec:width=891dp,height=411dp,dpi=420,orientation=landscape",
)
annotation class ComposerPreview

@Composable
fun Preview(content: @Composable BoxScope.() -> Unit) {
    ComposerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.Dark)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AnimationScopePreview(
    content: @Composable context(SharedTransitionScope, AnimatedVisibilityScope, BoxScope) () -> Unit,
) {
    ComposerTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = true) { targetState ->
                if (targetState) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AppColor.Dark)
                    ) {
                        content(
                            this@SharedTransitionLayout,
                            this@AnimatedContent,
                            this@Box,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WidgetPreviewBox(content: @Composable BoxScope.() -> Unit) {
    Box(modifier = Modifier.size(200.dp)) {
        content()
    }
}