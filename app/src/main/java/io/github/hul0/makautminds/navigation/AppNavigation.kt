package io.github.hul0.makautminds.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.hul0.makautminds.data.repository.ContentRepository
import io.github.hul0.makautminds.data.repository.UserPreferencesRepository
import io.github.hul0.makautminds.ui.screens.LearningPathDetailScreen
import io.github.hul0.makautminds.ui.screens.MainScreen
import io.github.hul0.makautminds.ui.screens.OnboardingScreen
import io.github.hul0.makautminds.viewmodel.GuidanceViewModel
import io.github.hul0.makautminds.viewmodel.LearningViewModel
import io.github.hul0.makautminds.viewmodel.ProfileViewModel

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Main : Screen("main")
    object LearningPathDetail : Screen("learningPathDetail/{pathId}") {
        fun createRoute(pathId: String) = "learningPathDetail/$pathId"
    }
}

@Composable
fun AppNavigation(context: Context, startDestination: String) {
    val navController = rememberNavController()
    val contentRepository = ContentRepository(context)
    val userPreferencesRepository = UserPreferencesRepository(context)

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                navController = navController,
                userPreferencesRepository = userPreferencesRepository
            )
        }
        composable(Screen.Main.route) {
            val learningViewModel: LearningViewModel = viewModel(
                factory = LearningViewModel.provideFactory(contentRepository, userPreferencesRepository)
            )
            val guidanceViewModel: GuidanceViewModel = viewModel(
                factory = GuidanceViewModel.provideFactory(contentRepository)
            )
            val profileViewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModel.provideFactory(userPreferencesRepository)
            )
            MainScreen(
                navController = navController,
                learningViewModel = learningViewModel,
                guidanceViewModel = guidanceViewModel,
                profileViewModel = profileViewModel
            )
        }
        composable(
            route = Screen.LearningPathDetail.route,
            arguments = listOf(navArgument("pathId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pathId = backStackEntry.arguments?.getString("pathId")
            if (pathId != null) {
                val learningViewModel: LearningViewModel = viewModel(
                    factory = LearningViewModel.provideFactory(contentRepository, userPreferencesRepository)
                )
                LearningPathDetailScreen(
                    pathId = pathId,
                    viewModel = learningViewModel,
                    navController = navController
                )
            }
        }
    }
}

