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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.andreewkov.animations.ui.screen.RoundLoaderScreenUI
import ru.andreewkov.animations.ui.screen.RoundProgressScreenUI
import ru.andreewkov.animations.ui.screen.Screen
import ru.andreewkov.animations.ui.theme.AnimationsColor
import ru.andreewkov.animations.ui.utils.AnimationsPreview
import ru.andreewkov.animations.ui.utils.Preview
import ru.andreewkov.animations.ui.utils.observe

private const val BLUR_COMPACT_DP = 0
private const val BLUR_EXPAND_DP = 50

@Composable
fun MainAppUI(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: MainAppViewModel = viewModel()
    val selectorState by viewModel.selectorState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember { derivedStateOf { backStackEntry?.destination?.route } }

    LaunchedEffect(Unit) {
        viewModel.navigationScreenId.observe { id ->
            if (currentRoute != id) {
                navController.navigate(id)
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val blurRadius by animateDpAsState(
            targetValue = if (selectorState.isExpand()) {
                BLUR_EXPAND_DP.dp
            } else {
                BLUR_COMPACT_DP.dp
            },
            animationSpec = tween(durationMillis = 400),
            label = "blur_radius",
        )

        Scaffold(
            topBar = {
                AppDar(Screen.findScreen(currentRoute).title)
            },
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
                .blur(radius = blurRadius, edgeTreatment = BlurredEdgeTreatment.Unbounded),
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.getStartScreen().id,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Screen.RoundProgress.id) {
                    RoundProgressScreenUI()
                }
                composable(route = Screen.RoundLoader.id) {
                    RoundLoaderScreenUI()
                }
            }
        }
        SelectorWidget(
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
