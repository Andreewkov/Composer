package ru.andreewkov.animations.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.andreewkov.animations.ui.screen.Screen
import ru.andreewkov.animations.ui.screen.roundprogress.RoundProgressScreenUI
import ru.andreewkov.animations.ui.selector.SelectorWidget
import ru.andreewkov.animations.ui.utils.AnimationsPreview
import ru.andreewkov.animations.ui.utils.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppUI(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        var isSelectorExpand by remember { mutableStateOf(false) }
        val blurRadius by animateDpAsState(
            targetValue = if (isSelectorExpand) 30.dp else 0.dp,
            animationSpec = tween(durationMillis = 400),
            label = "blur_radius",
        )

        Scaffold(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
                .blur(radius = blurRadius, edgeTreatment = BlurredEdgeTreatment.Unbounded),
            topBar = {

            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Animation.id,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Screen.Animation.id) {
                    RoundProgressScreenUI(title = "anim 1", color = Color.Green)
                }
                composable(route = Screen.Animation2.id) {

                }
            }
        }
        SelectorWidget(
            navController = navController,
            onSelectorExpand = { isExpand ->
                isSelectorExpand = isExpand
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppDar(title: String) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        colors = TopAppBarDefaults.topAppBarColors(

        )
    )
}

@AnimationsPreview
@Composable
private fun MainScreenPreview() {
    Preview {
        MainAppUI()
    }
}
