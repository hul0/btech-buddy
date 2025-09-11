package io.github.hul0.makautminds.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.hul0.makautminds.viewmodel.CoursesViewModel
import me.saket.unfurl.UnfurlResult
import io.github.hul0.makautminds.data.model.Course // Assuming this is the correct import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(viewModel: CoursesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Enhanced Background with sophisticated gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.surface
                        ),
                        radius = 840f // 1200 * 0.7
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Premium Header Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp, // 12 * 0.7
                tonalElevation = 1.dp // 2 * 0.7 (approx)
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 17.dp, // 24 * 0.7
                        vertical = 14.dp // 20 * 0.7
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
                                style = MaterialTheme.typography.headlineMedium, // Adjusted for smaller feel
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Unlock your potential with expert-curated learning",
                                style = MaterialTheme.typography.titleSmall, // Adjusted for smaller feel
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Decorative element
                        Surface(
                            modifier = Modifier.size(39.dp), // 56 * 0.7
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Outlined.School,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp) // 28 * 0.7
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(17.dp)) // 24 * 0.7

                    // Premium Search Bar
                    OutlinedTextField(
                        value = uiState.searchInput,
                        onValueChange = viewModel::onSearchInputChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 4.dp, // 6 * 0.7
                                shape = RoundedCornerShape(14.dp), // 20 * 0.7
                                clip = false
                            ),
                        placeholder = {
                            Text(
                                "Explore courses, skills, and topics...",
                                style = MaterialTheme.typography.bodySmall, // Adjusted for smaller feel
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        },
                        leadingIcon = {
                            Surface(
                                modifier = Modifier.size(25.dp), // 36 * 0.7
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(14.dp) // 20 * 0.7
                                    )
                                }
                            }
                        },
                        trailingIcon = if (uiState.searchInput.isNotEmpty()) {
                            {
                                IconButton(
                                    onClick = { viewModel.onSearchInputChanged("") }
                                ) {
                                    Icon(
                                        Icons.Outlined.Close,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(14.dp) // 20 * 0.7
                                    )
                                }
                            }
                        } else null,
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp), // 20 * 0.7
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f),
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp)) // 20 * 0.7

                    // Enhanced Filter Tags
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
                                horizontalArrangement = Arrangement.spacedBy(6.dp) // 8 * 0.7
                            ) {
                                Icon(
                                    Icons.Outlined.FilterAlt,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(13.dp) // 18 * 0.7
                                )
                                Text(
                                    text = "Categories",
                                    style = MaterialTheme.typography.labelLarge, // Adjusted for smaller feel
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp)) // 12 * 0.7

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp) // 12 * 0.7
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

            // Enhanced Courses List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(17.dp), // 24 * 0.7
                verticalArrangement = Arrangement.spacedBy(17.dp) // 24 * 0.7
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
        targetValue = if (isSelected) 1.05f else 1f, // Adjusted scale
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "chip_scale"
    )

    val animatedElevation by animateFloatAsState(
        targetValue = if (isSelected) 5f else 1f, // 8 * 0.7 (approx), 2 * 0.7 (approx)
        animationSpec = tween(200),
        label = "chip_elevation"
    )

    Surface(
        modifier = Modifier
            .scale(animatedScale)
            .shadow(
                elevation = animatedElevation.dp,
                shape = RoundedCornerShape(11.dp), // 16 * 0.7
                clip = false
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(11.dp), // 16 * 0.7
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp), // 16*0.7, 12*0.7
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp) // 8 * 0.7
        ) {
            if (isSelected) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(11.dp), // 16 * 0.7
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = tag,
                style = MaterialTheme.typography.labelMedium, // Adjusted for smaller feel
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
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
            .padding(vertical = 8.dp), // 12 * 0.7
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp) // 16 * 0.7
    ) {
        Surface(
            modifier = Modifier.size(28.dp), // 40 * 0.7
            shape = RoundedCornerShape(8.dp), // 12 * 0.7
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Outlined.Category,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp) // 20 * 0.7
                )
            }
        }

        Column {
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium, // Adjusted for smaller feel
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Curated learning paths",
                style = MaterialTheme.typography.labelSmall, // Adjusted for smaller feel
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PremiumSubcategoryHeader(subcategory: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp), // 12*0.7, 8*0.7
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp) // 12 * 0.7
    ) {
        Box(
            modifier = Modifier
                .size(17.dp) // 24 * 0.7
                .background(
                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                    RoundedCornerShape(4.dp) // 6 * 0.7
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp) // 8 * 0.7
                    .background(
                        MaterialTheme.colorScheme.tertiary,
                        CircleShape
                    )
            )
        }

        Text(
            text = subcategory,
            style = MaterialTheme.typography.titleSmall, // Adjusted for smaller feel
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
        targetValue = if (isPressed) 0.98f else 1f, // Slightly adjusted for smaller card
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_press"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 3f else 8f, // 4*0.7, 12*0.7
        animationSpec = tween(150),
        label = "card_elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(17.dp), // 24 * 0.7
                clip = false
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    isPressed = true
                    onClick()
                }
            )
            .padding(1.dp), // 2 * 0.7 (approx)
        shape = RoundedCornerShape(17.dp), // 24 * 0.7
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column {
            // Premium Course Content
            Column(modifier = Modifier.padding(17.dp)) { // 24 * 0.7
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Course difficulty indicator
                        Surface(
                            shape = RoundedCornerShape(6.dp), // 8 * 0.7
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = "Expert Level",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp) // 8*0.7, 4*0.7
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp)) // 12 * 0.7

                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.titleMedium, // Adjusted for smaller feel
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = MaterialTheme.typography.titleMedium.lineHeight * 1.1
                        )

                        Spacer(modifier = Modifier.height(8.dp)) // 12 * 0.7

                        Text(
                            text = course.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.4,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Bookmark with enhanced design
                    Surface(
                        modifier = Modifier
                            .padding(start = 8.dp) // 12 * 0.7
                            .size(34.dp), // 48 * 0.7
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Outlined.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(17.dp) // 24 * 0.7
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp)) // 20 * 0.7

                // Enhanced Tags
                if (course.tags.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(7.dp) // 10 * 0.7
                    ) {
                        items(course.tags.take(3)) { tag -> // Limit to 3 tags for cleaner look
                            Surface(
                                shape = RoundedCornerShape(11.dp), // 16 * 0.7
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f),
                                modifier = Modifier.padding(vertical = 1.dp) // 2 * 0.7 (approx)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp), // 12*0.7, 8*0.7
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp) // 6 * 0.7
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp) // 6 * 0.7
                                            .background(
                                                MaterialTheme.colorScheme.onTertiaryContainer,
                                                CircleShape
                                            )
                                    )
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelSmall, // Adjusted for smaller feel
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                        if (course.tags.size > 3) {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(11.dp), // 16 * 0.7
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = "+${course.tags.size - 3}",
                                        style = MaterialTheme.typography.labelSmall, // Adjusted for smaller feel
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp) // 12*0.7, 8*0.7
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Enhanced States
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
            .height(98.dp) // 140 * 0.7
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.3f)
                    ),
                    start = androidx.compose.ui.geometry.Offset.Zero,
                    end = androidx.compose.ui.geometry.Offset.Infinite
                ),
                shape = RoundedCornerShape(bottomStart = 17.dp, bottomEnd = 17.dp) // 24 * 0.7
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp) // 12 * 0.7
        ) {
            // Custom animated loading indicator
            Box {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp), // 40 * 0.7
                    strokeWidth = 3.dp, // 4 * 0.7 (approx)
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    trackColor = Color.Transparent
                )
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp), // 40 * 0.7
                    strokeWidth = 3.dp, // 4 * 0.7 (approx)
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Loading preview...",
                    style = MaterialTheme.typography.labelLarge, // Adjusted for smaller feel
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Fetching course details",
                    style = MaterialTheme.typography.labelSmall, // Adjusted for smaller feel
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
        // Enhanced Image with sophisticated overlay
        data.thumbnail?.toString()?.let { imageUrl ->
            Box {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = data.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(154.dp) // 220 * 0.7
                        .clip(RoundedCornerShape(bottomStart = 17.dp, bottomEnd = 17.dp)), // 24 * 0.7
                    contentScale = ContentScale.Crop
                )

                // Sophisticated gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(154.dp) // 220 * 0.7
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.1f),
                                    Color.Black.copy(alpha = 0.4f)
                                ),
                                startY = 35f // 50 * 0.7
                            ),
                            shape = RoundedCornerShape(bottomStart = 17.dp, bottomEnd = 17.dp) // 24 * 0.7
                        )
                )

                // Premium play button
                Surface(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp), // 72 * 0.7
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.95f),
                    shadowElevation = 8.dp // 12 * 0.7
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Surface(
                            modifier = Modifier.size(45.dp), // 64 * 0.7
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Outlined.PlayArrow,
                                    contentDescription = "Play",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(22.dp) // 32 * 0.7
                                )
                            }
                        }
                    }
                }

                // Course type indicator
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(11.dp), // 16 * 0.7
                    shape = RoundedCornerShape(8.dp), // 12 * 0.7
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), // 12*0.7, 6*0.7
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp) // 4 * 0.7 (approx)
                    ) {
                        Icon(
                            Icons.Outlined.OndemandVideo,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp), // 14 * 0.7
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Video Course",
                            style = MaterialTheme.typography.labelSmall, // Adjusted for smaller feel
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Enhanced preview content
        if (data.title != null || data.description != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(bottomStart = 17.dp, bottomEnd = 17.dp) // 24 * 0.7
            ) {
                Column(
                    modifier = Modifier.padding(17.dp) // 24 * 0.7
                ) {
                    data.title?.let { title ->
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium, // Adjusted for smaller feel
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = MaterialTheme.typography.titleMedium.lineHeight * 1.2
                        )
                    }

                    data.description?.let { description ->
                        Spacer(modifier = Modifier.height(8.dp)) // 12 * 0.7
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall, // Adjusted for smaller feel
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = MaterialTheme.typography.bodySmall.lineHeight * 1.4,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Enhanced CTA
                    data.url.let {
                        Spacer(modifier = Modifier.height(11.dp)) // 16 * 0.7
                        Surface(
                            shape = RoundedCornerShape(8.dp), // 12 * 0.7
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(11.dp), // 16 * 0.7
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Start Learning Now",
                                    style = MaterialTheme.typography.labelLarge, // Adjusted for smaller feel
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Icon(
                                    Icons.Outlined.ArrowForward,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp) // 20 * 0.7
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
