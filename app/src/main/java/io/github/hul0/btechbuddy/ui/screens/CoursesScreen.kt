package io.github.hul0.btechbuddy.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.hul0.btechbuddy.data.model.Course
import io.github.hul0.btechbuddy.ui.theme.PrimaryBlue
import io.github.hul0.btechbuddy.ui.theme.PrimaryGreen
import io.github.hul0.btechbuddy.viewmodel.CoursesViewModel
import me.saket.unfurl.UnfurlResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(viewModel: CoursesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()

        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Header Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
                ) {
                    // Hero Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Discover Courses",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Unlock your potential with expert-curated learning",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Surface(
                            modifier = Modifier.size(39.dp),
                            shape = CircleShape,
                            color = PrimaryBlue.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Outlined.School,
                                    contentDescription = null,
                                    tint = PrimaryBlue.copy(alpha = 0.9f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search Bar
                    OutlinedTextField(
                        value = uiState.searchInput,
                        onValueChange = viewModel::onSearchInputChanged,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "Explore courses, skills, and topics...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = PrimaryBlue.copy(alpha = 0.9f)
                            )
                        },
                        trailingIcon = if (uiState.searchInput.isNotEmpty()) {
                            {
                                IconButton(onClick = { viewModel.onSearchInputChanged("") }) {
                                    Icon(
                                        Icons.Outlined.Close,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else null,
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue.copy(alpha = 0.9f),
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f),
                            cursorColor = PrimaryBlue.copy(alpha = 0.9f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Filter Tags
                    AnimatedVisibility(
                        visible = uiState.allTags.isNotEmpty(),
                        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                            animationSpec = tween(300, easing = EaseOutQuart)
                        ),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.FilterAlt,
                                    contentDescription = null,
                                    tint = PrimaryBlue.copy(alpha = 0.9f),
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Categories",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(uiState.allTags) { tag ->
                                    PremiumFilterChip(
                                        tag = tag,
                                        isSelected = uiState.selectedTag == tag,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            viewModel.onTagSelected(tag)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Courses List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.filteredCourses.forEach { category ->
                    item {
                        PremiumCategoryHeader(category.category)
                    }
                    category.subcategories.forEach { subcategory ->
                        item {
                            PremiumSubcategoryHeader(subcategory.subcategory)
                        }
                        items(subcategory.courses) { course ->
                            val previewState = uiState.coursePreviews[course.id]
                            PremiumCourseCard(
                                course = course,
                                preview = previewState?.preview,
                                isLoading = previewState?.isLoading ?: false,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val url = previewState?.preview?.url?.toString() ?: course.url
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    try {
                                        context.startActivity(intent)
                                    } catch (e: ActivityNotFoundException) {
                                        Toast.makeText(context, "No browser found.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                    item {
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumFilterChip(
    tag: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "chip_scale"
    )

    Surface(
        modifier = Modifier
            .scale(animatedScale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(11.dp),
        color = if (isSelected) {
            PrimaryBlue.copy(alpha = 0.9f)
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (isSelected) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(11.dp),
                    tint = Color.White
                )
            }
            Text(
                text = tag,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
fun PremiumCategoryHeader(category: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = RoundedCornerShape(8.dp),
            color = PrimaryBlue.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Outlined.Category,
                    contentDescription = null,
                    tint = PrimaryBlue.copy(alpha = 0.9f),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Column {
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Curated learning paths",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PremiumSubcategoryHeader(subcategory: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(17.dp)
                .background(
                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                    RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        MaterialTheme.colorScheme.tertiary,
                        CircleShape
                    )
            )
        }
        Text(
            text = subcategory,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PremiumCourseCard(
    course: Course,
    preview: UnfurlResult?,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_press"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    isPressed = true
                    onClick()
                }
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Difficulty Chip
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PrimaryGreen.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = course.difficulty,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryGreen.copy(alpha = 0.9f),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }

                            Spacer(Modifier.width(6.dp))

                            // Tag Chips
                            if (course.tags.isNotEmpty()) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    items(course.tags.take(3)) { tag ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = PrimaryBlue.copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                text = tag,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = PrimaryBlue.copy(alpha = 0.9f),
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                    if (course.tags.size > 3) {
                                        item {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = PrimaryBlue.copy(alpha = 0.1f)
                                            ) {
                                                Text(
                                                    text = "+${course.tags.size - 3}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = PrimaryBlue.copy(alpha = 0.9f),
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = course.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(34.dp),
                        shape = CircleShape,
                        color = PrimaryBlue.copy(alpha = 0.1f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Outlined.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = PrimaryBlue.copy(alpha = 0.9f),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }
            }

            when {
                isLoading -> {
                    PremiumLoadingState()
                }
                preview != null -> {
                    PremiumLinkPreview(data = preview, onClick = onClick)
                }
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
fun PremiumLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(98.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.3f),
                        PrimaryBlue.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                strokeWidth = 3.dp,
                color = PrimaryBlue.copy(alpha = 0.9f)
            )
            Text(
                text = "Loading preview...",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun PremiumLinkPreview(data: UnfurlResult, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        data.thumbnail?.toString()?.let { imageUrl ->
            Box {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = data.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f) // Use aspect ratio to maintain size
                        .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)),
                    contentScale = ContentScale.Fit // Fit ensures the image is not cropped
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.4f)
                                )
                            ),
                            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                        )
                )

                Surface(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp),
                    shape = CircleShape,
                    color = PrimaryBlue.copy(alpha = 0.9f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Outlined.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}
