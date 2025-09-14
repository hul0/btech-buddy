package io.github.hul0.btechbuddy.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import io.github.hul0.btechbuddy.data.repository.UserPreferencesRepository
import io.github.hul0.btechbuddy.navigation.AppNavigation
import io.github.hul0.btechbuddy.navigation.Screen
import kotlinx.coroutines.flow.map

@Composable
fun AppHost() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }

    // Combine checks for a cohesive loading state
    val authState by remember(userPreferencesRepository) {
        userPreferencesRepository.accessToken.map { token ->
            if (token == null) AuthState.UNAUTHENTICATED
            else AuthState.AUTHENTICATED
        }
    }.collectAsState(initial = AuthState.LOADING)

    val onboardingComplete by userPreferencesRepository.userPreferences.map {
        it.branch.isNotBlank() && it.name.isNotBlank()
    }.collectAsState(initial = null)

    Surface(modifier = Modifier.fillMaxSize()) {
        when {
            // Show loading indicator while checking auth and onboarding status
            authState == AuthState.LOADING || onboardingComplete == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            // If authenticated and onboarding is done, go to Main
            authState == AuthState.AUTHENTICATED && onboardingComplete == true -> {
                AppNavigation(context = context, navController = navController, startDestination = Screen.Main.route)
            }
            // If not authenticated, go to Auth screen
            authState == AuthState.UNAUTHENTICATED -> {
                AppNavigation(context = context, navController = navController, startDestination = Screen.Auth.route)
            }
            // If authenticated but onboarding is not done, go to Onboarding
            else -> {
                AppNavigation(context = context, navController = navController, startDestination = Screen.Onboarding.route)
            }
        }
    }
}

enum class AuthState {
    LOADING,
    AUTHENTICATED,
    UNAUTHENTICATED
}
