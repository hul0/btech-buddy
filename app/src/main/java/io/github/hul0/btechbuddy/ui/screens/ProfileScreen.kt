package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.hul0.btechbuddy.viewmodel.ProfileViewModel
import io.github.hul0.btechbuddy.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onEditProfile: () -> Unit = {}
) {
    val userPreferences by viewModel.userPreferences.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()

    // Animation for content appearance
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        contentVisible = true
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Profile",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = onEditProfile,
                        modifier = Modifier.semantics {
                            contentDescription = "Edit Profile"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Profile",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { innerPadding ->
        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                animationSpec = tween(600),
                initialOffsetY = { it / 4 }
            )
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Enhanced Header
                item {
                    EnhancedProfileHeader(
                        name = userPreferences.name,
                        branch = userPreferences.branch,
                        college = userPreferences.college
                    )
                }

                // Academic Section
                item {
                    AnimatedSectionTitle(
                        text = "Academic Information",
                        icon = Icons.Filled.School
                    )
                }

                if (userPreferences.college.isNotBlank()) {
                    item {
                        EnhancedInfoCard(
                            title = "College",
                            value = userPreferences.college,
                            icon = Icons.Outlined.Apartment,
                            containerColor = Blue100,
                            contentColor = Blue700
                        )
                    }
                }

                if (userPreferences.branch.isNotBlank()) {
                    item {
                        EnhancedInfoCard(
                            title = "Branch of Study",
                            value = userPreferences.branch,
                            icon = Icons.Outlined.Engineering,
                            containerColor = Purple100,
                            contentColor = Purple700
                        )
                    }
                }

                if (userPreferences.yearOfStudy.isNotBlank()) {
                    item {
                        EnhancedInfoCard(
                            title = "Current Year",
                            value = userPreferences.yearOfStudy,
                            icon = Icons.Outlined.DateRange,
                            containerColor = Green100,
                            contentColor = Green700
                        )
                    }
                }

                if (userPreferences.expectedGraduationYear.isNotBlank()) {
                    item {
                        EnhancedInfoCard(
                            title = "Expected Graduation",
                            value = userPreferences.expectedGraduationYear,
                            icon = Icons.Outlined.Event,
                            containerColor = Orange100,
                            contentColor = Orange700
                        )
                    }
                }

                // Learning Preferences Section
                item {
                    AnimatedSectionTitle(
                        text = "Learning Preferences",
                        icon = Icons.Filled.Psychology
                    )
                }

                if (userPreferences.preferredLearningStyle.isNotBlank()) {
                    item {
                        EnhancedInfoCard(
                            title = "Learning Style",
                            value = userPreferences.preferredLearningStyle,
                            icon = Icons.Outlined.Palette,
                            containerColor = Teal100,
                            contentColor = Teal700
                        )
                    }
                }

                if (userPreferences.hoursPerWeek.isNotBlank()) {
                    item {
                        EnhancedInfoCard(
                            title = "Weekly Study Hours",
                            value = "${userPreferences.hoursPerWeek} hours",
                            icon = Icons.Outlined.AccessTime,
                            containerColor = Indigo100,
                            contentColor = Indigo700
                        )
                    }
                }

                if (userPreferences.learningGoals.isNotBlank()) {
                    item {
                        EnhancedChipInfoCard(
                            title = "Learning Goals",
                            csv = userPreferences.learningGoals,
                            icon = Icons.Outlined.Flag,
                            headerColor = Pink700
                        )
                    }
                }

                // Interests & Career Section
                item {
                    AnimatedSectionTitle(
                        text = "Interests & Career",
                        icon = Icons.Filled.Work
                    )
                }

                if (userPreferences.interests.isNotBlank()) {
                    item {
                        EnhancedChipInfoCard(
                            title = "My Interests",
                            csv = userPreferences.interests,
                            icon = Icons.Outlined.Favorite,
                            headerColor = DeepOrange700
                        )
                    }
                }

                if (userPreferences.dreamCompanies.isNotBlank()) {
                    item {
                        EnhancedChipInfoCard(
                            title = "Dream Companies",
                            csv = userPreferences.dreamCompanies,
                            icon = Icons.Outlined.Business,
                            headerColor = Cyan700
                        )
                    }
                }

                // Add some bottom spacing
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun AnimatedSectionTitle(
    text: String,
    icon: ImageVector
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(500),
            initialOffsetX = { -it / 2 }
        ) + fadeIn(animationSpec = tween(500))
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun EnhancedProfileHeader(
    name: String,
    branch: String,
    college: String
) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Enhanced Profile Avatar
                Card(
                    modifier = Modifier
                        .size(80.dp)
                        .shadow(8.dp, CircleShape),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = getInitials(name),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                // Enhanced Profile Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (name.isNotBlank()) name else "Dear Student",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (branch.isNotBlank()) {
                        Text(
                            text = branch,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color.White.copy(alpha = 0.9f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (college.isNotBlank()) {
                        Text(
                            text = college,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedInfoCard(
    title: String,
    value: String,
    icon: ImageVector,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    if (value.isBlank()) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = contentColor.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        ListItem(
            leadingContent = {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = containerColor
                    ),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            headlineContent = {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            modifier = Modifier.padding(8.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EnhancedChipInfoCard(
    title: String,
    csv: String,
    icon: ImageVector,
    headerColor: Color = MaterialTheme.colorScheme.primary
) {
    val items = csv.split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    if (items.isEmpty()) return

    val chipColors = listOf(
        MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.tertiaryContainer,
        MaterialTheme.colorScheme.error to MaterialTheme.colorScheme.errorContainer,
        Color(0xFF6750A4) to Color(0xFFE8DEF8), // Purple variant
        Color(0xFF7D5260) to Color(0xFFFFD8E4)  // Pink variant
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Enhanced Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = headerColor.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = headerColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = headerColor
                )
            }

            // Enhanced Chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.forEachIndexed { index, label ->
                    val colorPair = chipColors[index % chipColors.size]

                    AssistChip(
                        onClick = { /* UI only */ },
                        label = {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = colorPair.first
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Tag,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = colorPair.first
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = colorPair.second,
                            labelColor = colorPair.first,
                            leadingIconContentColor = colorPair.first
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.semantics {
                            contentDescription = "$label chip"
                        }
                    )
                }
            }
        }
    }
}

private fun getInitials(name: String): String {
    if (name.isBlank()) return "U"

    val parts = name.trim().split(" ").filter { it.isNotBlank() }
    return when {
        parts.size >= 2 -> "${parts.first().first().uppercaseChar()}${parts[1].first().uppercaseChar()}"
        parts.size == 1 -> {
            val firstName = parts.first()
            if (firstName.length >= 2) {
                "${firstName[0].uppercaseChar()}${firstName[1].uppercaseChar()}"
            } else {
                firstName.first().uppercaseChar().toString()
            }
        }
        else -> "U"
    }
}
