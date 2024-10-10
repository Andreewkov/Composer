package ru.andreewkov.animations.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.andreewkov.animations.ui.screen.Screen
import ru.andreewkov.animations.ui.screen.roundprogress.RoundProgressScreenUI
import ru.andreewkov.animations.ui.MainAppViewModel.ScreenState
import ru.andreewkov.animations.ui.theme.AnimationsColor
import ru.andreewkov.animations.ui.utils.AnimationsPreview
import ru.andreewkov.animations.ui.utils.Preview

@Composable
fun MainAppUI(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: MainAppViewModel = viewModel()
    val screenState by viewModel.screenState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val blurRadius by animateDpAsState(
            targetValue = if (screenState is ScreenState.SelectorExpand) {
                30.dp
            } else {
                0.dp
            },
            animationSpec = tween(durationMillis = 400),
            label = "blur_radius",
        )

        Scaffold(
            topBar = {
                AppDar(screenState.currentScreen.title)
            },
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
                .blur(radius = blurRadius, edgeTreatment = BlurredEdgeTreatment.Unbounded),
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
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppDar(title: String) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = AnimationsColor.Peach,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
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
