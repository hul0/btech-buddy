package io.github.hul0.makautminds.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.hul0.makautminds.viewmodel.DashboardViewModel
import io.github.hul0.makautminds.viewmodel.GuidanceViewModel
import io.github.hul0.makautminds.viewmodel.LearningViewModel
import io.github.hul0.makautminds.viewmodel.ProfileViewModel

sealed class BottomNavScreen(val route: String, val icon: ImageVector, val label: String) {
    object Dashboard : BottomNavScreen("dashboard", Icons.Default.Dashboard, "Dashboard")
    object Learning : BottomNavScreen("learning", Icons.Default.School, "Learning")
    object Guidance : BottomNavScreen("guidance", Icons.Default.Explore, "Guidance")
    object Profile : BottomNavScreen("profile", Icons.Default.Person, "Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    // This is the NavController for the whole app (handles navigation to DetailScreen, etc.)
    mainNavController: NavController,
    dashboardViewModel: DashboardViewModel,
    learningViewModel: LearningViewModel,
    guidanceViewModel: GuidanceViewModel,
    profileViewModel: ProfileViewModel
) {
    // This is a NEW, local NavController specifically for the bottom bar tabs.
    val bottomNavController = rememberNavController()

    val items = listOf(
        BottomNavScreen.Dashboard,
        BottomNavScreen.Learning,
        BottomNavScreen.Guidance,
        BottomNavScreen.Profile,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MAKAUT MINDS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                // The back stack entry should now be from our local bottomNavController
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            // Use the local bottomNavController to navigate between tabs
                            bottomNavController.navigate(screen.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // This NavHost manages the content of the tabs using the local bottomNavController
        NavHost(
            bottomNavController,
            startDestination = BottomNavScreen.Dashboard.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavScreen.Dashboard.route) { DashboardScreen(dashboardViewModel) }
            // LearningScreen needs the mainNavController to navigate to the detail page
            composable(BottomNavScreen.Learning.route) { LearningScreen(learningViewModel, mainNavController) }
            composable(BottomNavScreen.Guidance.route) { GuidanceScreen(guidanceViewModel) }
            composable(BottomNavScreen.Profile.route) { ProfileScreen(profileViewModel) }
        }
    }
}

