package io.github.hul0.btechbuddy.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.hul0.btechbuddy.data.repository.ContentRepository
import io.github.hul0.btechbuddy.data.repository.UserPreferencesRepository
import io.github.hul0.btechbuddy.ui.screens.AboutScreen
import io.github.hul0.btechbuddy.ui.screens.LearningPathDetailScreen
import io.github.hul0.btechbuddy.ui.screens.MainScreen
import io.github.hul0.btechbuddy.ui.screens.OnboardingScreen
import io.github.hul0.btechbuddy.viewmodel.*

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Main : Screen("main")
    object LearningPathDetail : Screen("learningPathDetail/{pathId}") {
        fun createRoute(pathId: String) = "learningPathDetail/$pathId"
    }
    object About : Screen("about")
}

@Composable
fun AppNavigation(context: Context, startDestination: String) {
    val navController = rememberNavController()
    val contentRepository = ContentRepository(context)
    val userPreferencesRepository = UserPreferencesRepository(context)

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Onboarding.route) {
            val onboardingViewModel: OnboardingViewModel = viewModel(
                factory = OnboardingViewModel.provideFactory(userPreferencesRepository)
            )
            OnboardingScreen(
                navController = navController,
                viewModel = onboardingViewModel
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
            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModel.provideFactory(
                    learningViewModel = learningViewModel,
                    userPreferencesRepository = userPreferencesRepository,
                    contentRepository = contentRepository
                )
            )
            val coursesViewModel: CoursesViewModel = viewModel(
                factory = CoursesViewModel.provideFactory(contentRepository)
            )
            val todoViewModel: TodoViewModel = viewModel(
                factory = TodoViewModel.provideFactory(userPreferencesRepository)
            )

            MainScreen(
                mainNavController = navController,
                dashboardViewModel = dashboardViewModel,
                learningViewModel = learningViewModel,
                coursesViewModel = coursesViewModel,
                guidanceViewModel = guidanceViewModel,
                profileViewModel = profileViewModel,
                todoViewModel = todoViewModel
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
        composable(Screen.About.route) {
            AboutScreen(navController = navController)
        }
    }
}

