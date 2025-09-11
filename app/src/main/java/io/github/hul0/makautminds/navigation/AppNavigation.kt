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
// Corrected import from 'hulo' to 'hul0'
import io.github.hul0.makautminds.ui.screens.OnboardingScreen
import io.github.hul0.makautminds.viewmodel.*

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
            val coursesViewModel: CoursesViewModel = viewModel(
                factory = CoursesViewModel.provideFactory(contentRepository)
            )
            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModel.provideFactory(
                    learningViewModel = learningViewModel,
                    userPreferencesRepository = userPreferencesRepository,
                    contentRepository = contentRepository
                )
            )

            MainScreen(
                mainNavController = navController,
                dashboardViewModel = dashboardViewModel,
                learningViewModel = learningViewModel,
                guidanceViewModel = guidanceViewModel,
                profileViewModel = profileViewModel,
                coursesViewModel = coursesViewModel
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
