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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.hul0.makautminds.viewmodel.GuidanceViewModel
import io.github.hul0.makautminds.viewmodel.LearningViewModel

sealed class BottomNavScreen(val route: String, val icon: ImageVector, val label: String) {
    object Dashboard : BottomNavScreen("dashboard", Icons.Default.Dashboard, "Dashboard")
    object Learning : BottomNavScreen("learning", Icons.Default.School, "Learning")
    object Guidance : BottomNavScreen("guidance", Icons.Default.Explore, "Guidance")
    object Profile : BottomNavScreen("profile", Icons.Default.Person, "Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(learningViewModel: LearningViewModel, guidanceViewModel: GuidanceViewModel) {
    val navController = rememberNavController()
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
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
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
        NavHost(
            navController,
            startDestination = BottomNavScreen.Dashboard.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavScreen.Dashboard.route) { DashboardScreen() }
            composable(BottomNavScreen.Learning.route) { LearningScreen(learningViewModel) }
            composable(BottomNavScreen.Guidance.route) { GuidanceScreen(guidanceViewModel) }
            composable(BottomNavScreen.Profile.route) { ProfileScreen() }
        }
    }
}
