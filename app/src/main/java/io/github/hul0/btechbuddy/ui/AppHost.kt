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
import io.github.hul0.btechbuddy.data.repository.UserPreferencesRepository
import io.github.hul0.btechbuddy.navigation.AppNavigation
import io.github.hul0.btechbuddy.navigation.Screen
import kotlinx.coroutines.flow.map

@Composable
fun AppHost() {
    val context = LocalContext.current
    // Use remember to create a single instance of the repository
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }

    // Check if onboarding is complete. Start with `null` to represent the loading state.
    val onboardingComplete by userPreferencesRepository.userPreferences.map {
        it.branch.isNotBlank() && it.interests.isNotBlank()
    }.collectAsState(initial = null)

    Surface(modifier = Modifier.fillMaxSize()) {
        when (onboardingComplete) {
            // While loading preferences, show a spinner
            null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            // If onboarding is complete, start at the Main screen
            true -> {
                AppNavigation(context = context, startDestination = Screen.Main.route)
            }
            // If onboarding is not complete, start at the Onboarding screen
            false -> {
                AppNavigation(context = context, startDestination = Screen.Onboarding.route)
            }
        }
    }
}
