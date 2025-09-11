package io.github.hul0.makautminds.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.hul0.makautminds.data.repository.ContentRepository
import io.github.hul0.makautminds.ui.screens.MainScreen
import io.github.hul0.makautminds.ui.screens.OnboardingScreen
import io.github.hul0.makautminds.viewmodel.GuidanceViewModel
import io.github.hul0.makautminds.viewmodel.LearningViewModel

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Main : Screen("main")
}

@Composable
fun AppNavigation(context: Context) {
    val navController = rememberNavController()
    val repository = ContentRepository(context)

    NavHost(navController = navController, startDestination = Screen.Onboarding.route) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }
        composable(Screen.Main.route) {
            val learningViewModel: LearningViewModel = viewModel(
                factory = LearningViewModel.provideFactory(repository)
            )
            val guidanceViewModel: GuidanceViewModel = viewModel(
                factory = GuidanceViewModel.provideFactory(repository)
            )
            MainScreen(learningViewModel, guidanceViewModel)
        }
    }
}
