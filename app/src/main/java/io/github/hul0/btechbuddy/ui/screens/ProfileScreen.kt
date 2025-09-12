package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.hul0.btechbuddy.viewmodel.ProfileViewModel
import io.github.hul0.btechbuddy.ui.theme.* // Color.kt palette
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onEditProfile: () -> Unit = {}
) {
    val userPreferences by viewModel.userPreferences.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()

    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { contentVisible = true }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = "My Profile", fontWeight = FontWeight.Bold, color = Gray900) },
                actions = {
                    IconButton(
                        onClick = onEditProfile,
                        modifier = Modifier.semantics { contentDescription = "Edit Profile" }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Profile",
                            tint = Blue700
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Gray50,
                    titleContentColor = Gray900,
                    navigationIconContentColor = Blue700,
                    actionIconContentColor = Gray800
                )
            )
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            Divider(color = Gray200, thickness = 1.dp)
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioNoBouncy)) +
                        slideInVertically(
                            animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioNoBouncy),
                            initialOffsetY = { it / 6 }
                        )
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        EnhancedProfileHeader(
                            name = userPreferences.name,
                            branch = userPreferences.branch,
                            college = userPreferences.college
                        )
                    }

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
                                containerColor = Blue50,
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
                                containerColor = Purple50,
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
                                containerColor = Green50,
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
                                containerColor = Orange50,
                                contentColor = Orange700
                            )
                        }
                    }

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

                    item { Spacer(modifier = Modifier.height(24.dp)) }
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
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) +
                slideInVertically(
                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                    initialOffsetY = { -it / 8 }
                )
    ) {
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Gray50),
            border = BorderStroke(1.dp, Gray200)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Blue100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Blue700
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
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
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Blue50),
        border = BorderStroke(1.dp, Blue100)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            // Avatar with initials (no shadow)
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Blue100),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getInitials(name),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Blue900
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (name.isNotBlank()) name else "Dear Student",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Gray900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (branch.isNotBlank()) {
                    Text(
                        text = branch,
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray800,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (college.isNotBlank()) {
                    Text(
                        text = college,
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray700,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
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
    containerColor: androidx.compose.ui.graphics.Color = Gray50,
    contentColor: androidx.compose.ui.graphics.Color = Gray900
) {
    if (value.isBlank()) return

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, containerColor.copy(alpha = 0.8f))
    ) {
        ListItem(
            leadingContent = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(containerColor.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor
                    )
                }
            },
            headlineContent = {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Gray900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray700
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
    headerColor: androidx.compose.ui.graphics.Color = Blue700
) {
    val items = csv.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    if (items.isEmpty()) return

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Gray50),
        border = BorderStroke(1.dp, Gray200)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(headerColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = headerColor)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Gray900
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.forEach { label ->
                    AssistChip(
                        onClick = { /* UI only */ },
                        label = {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Tag,
                                contentDescription = null
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Blue100,
                            labelColor = Blue900,
                            leadingIconContentColor = Blue700
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }
    }
}

private fun getInitials(name: String): String {
    val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    return when {
        // Two or more words: first letter of first two words
        parts.size >= 2 -> parts[0].take(1).uppercase() + parts[1].take(1).uppercase()
        // Single word: first two letters
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "U"
    }
}
