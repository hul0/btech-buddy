package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.hul0.btechbuddy.viewmodel.*

sealed class BottomNavScreen(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Dashboard : BottomNavScreen("dashboard", "Dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard)
    object Learning : BottomNavScreen("learning", "Learning", Icons.Filled.School, Icons.Outlined.School)
    object Courses : BottomNavScreen("courses", "Courses", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary)
    object Guidance : BottomNavScreen("guidance", "Guidance", Icons.Filled.Explore, Icons.Outlined.Explore)
    // Use CheckCircle to avoid missing icon issues with Checklist
    object Todo : BottomNavScreen("todo", "To-Do", Icons.Filled.CheckCircle, Icons.Outlined.CheckCircle)
    object Profile : BottomNavScreen("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainNavController: NavController,
    dashboardViewModel: DashboardViewModel,
    learningViewModel: LearningViewModel,
    coursesViewModel: CoursesViewModel,
    guidanceViewModel: GuidanceViewModel,
    profileViewModel: ProfileViewModel,
    todoViewModel: TodoViewModel
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            EnhancedCoolBottomNavBar(
                navController = bottomNavController,
                items = listOf(
                    BottomNavScreen.Dashboard,
                    BottomNavScreen.Learning,
                    BottomNavScreen.Courses,
                    BottomNavScreen.Guidance,
                    BottomNavScreen.Todo,
                    BottomNavScreen.Profile,
                )
            )
        }
    ) { innerPadding ->
        NavHost(
            bottomNavController,
            startDestination = BottomNavScreen.Dashboard.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavScreen.Dashboard.route) { DashboardScreen(dashboardViewModel) }
            composable(BottomNavScreen.Learning.route) { LearningScreen(learningViewModel, mainNavController) }
            composable(BottomNavScreen.Courses.route) { CoursesScreen(coursesViewModel) }
            composable(BottomNavScreen.Guidance.route) { GuidanceScreen(guidanceViewModel) }
            composable(BottomNavScreen.Todo.route) { TodoScreen(todoViewModel) }
            composable(BottomNavScreen.Profile.route) { ProfileScreen(
                profileViewModel
            ) }
        }
    }
}

@Composable
fun EnhancedCoolBottomNavBar(
    navController: NavController,
    items: List<BottomNavScreen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }.let { if (it == -1) 0 else it }

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    // Indicator dimensions
    val indicatorWidth = 22.dp
    val indicatorHeight = 3.dp

    // Smooth spring-based indicator offset (no delays/tweens)
    val indicatorOffset: Dp by animateDpAsState(
        targetValue = with(density) {
            if (containerSize.width > 0 && items.isNotEmpty()) {
                val itemWidthPx = containerSize.width.toFloat() / items.size.toFloat()
                val indPx = indicatorWidth.toPx()
                val targetPx = (itemWidthPx * selectedIndex) + (itemWidthPx - indPx) / 2f
                val clampedPx = targetPx.coerceIn(0f, containerSize.width - indPx)
                clampedPx.toDp()
            } else 0.dp
        },
        animationSpec = spring(dampingRatio = 1f, stiffness = 300f),
        label = "indicatorOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(35.dp),
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
            shadowElevation = 10.dp,
            tonalElevation = 2.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { containerSize = it }
            ) {
                // Underline-style active indicator (no gradient)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = indicatorOffset, y = (-10).dp)
                        .width(indicatorWidth)
                        .height(indicatorHeight)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEach { screen ->
                        val isSelected = currentRoute == screen.route
                        EnhancedCoolBottomNavItem(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            screen = screen,
                            isSelected = isSelected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedCoolBottomNavItem(
    modifier: Modifier = Modifier,
    screen: BottomNavScreen,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    // Smooth, no-delay spring animations (no tween)
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.12f else 1.0f,
        animationSpec = spring(dampingRatio = 1f, stiffness = 300f),
        label = "iconScale"
    )
    val iconSize by animateDpAsState(
        targetValue = if (isSelected) 26.dp else 22.dp,
        animationSpec = spring(dampingRatio = 1f, stiffness = 300f),
        label = "iconSize"
    )

    val interaction = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clickable(
                interactionSource = interaction,
                indication = null, // no ripple (deprecated rememberRipple avoided)
                role = Role.Tab,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
            contentDescription = screen.label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            modifier = Modifier
                .scale(scale)
                .size(iconSize)
        )
    }
}
