package io.github.hul0.btechbuddy.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Gradient
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.github.hul0.btechbuddy.data.model.Course
import io.github.hul0.btechbuddy.viewmodel.CoursesViewModel
import me.saket.unfurl.UnfurlResult
import io.github.hul0.btechbuddy.ui.theme.*

// LEGENDARY COLOR PALETTE - 2025 Design System
private val LegendaryBlue = Color(0xFF2E3192)
private val CyberTeal = Color(0xFF1BFFFF)
private val ElectricPurple = Color(0xFF8458B3)
private val NeonPink = Color(0xFFD4145A)
private val SolarGold = Color(0xFFFFB03B)
private val DeepSpace = Color(0xFF0D1421)
private val GlowWhite = Color(0xFFFFFFFF)
private val MysticViolet = Color(0xFFa0d2eb)
private val DreamPurple = Color(0xFFd0bdf4)

// DARK BACKGROUND GRADIENT
private val DarkHeroGradient = Brush.verticalGradient(
    colors = listOf(
        DeepSpace,                // top: deep navy
        Color(0xFF0B0F1A),        // mid: near-black blue
        Color(0xFF070B14)         // bottom: ink
    )
)

// GLASSMORPHISM BRUSHES (dark-tuned)
private val GlassBrush = Brush.linearGradient(
    colors = listOf(
        GlowWhite.copy(alpha = 0.14f),
        GlowWhite.copy(alpha = 0.08f),
        GlowWhite.copy(alpha = 0.04f)
    )
)

private val CategoryGlass = Brush.radialGradient(
    colors = listOf(
        GlowWhite.copy(alpha = 0.16f),
        CyberTeal.copy(alpha = 0.08f),
        LegendaryBlue.copy(alpha = 0.06f)
    )
)

private val BorderGlass = Brush.linearGradient(
    colors = listOf(
        CyberTeal.copy(alpha = 0.45f),
        ElectricPurple.copy(alpha = 0.35f),
        GlowWhite.copy(alpha = 0.08f)
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(viewModel: CoursesViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // DARK BACKGROUND
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkHeroGradient)
    ) {
        // SUBTLE FLOATING ORBS - BACKGROUND DECOR
//        FloatingOrb(
//            modifier = Modifier
//                .size(200.dp)
//                .offset(x = (-50).dp, y = 100.dp),
//            color = CyberTeal.copy(alpha = 0.06f)
//        )
//
//        FloatingOrb(
//            modifier = Modifier
//                .size(150.dp)
//                .offset(x = 280.dp, y = 300.dp),
//            color = SolarGold.copy(alpha = 0.08f)
//        )
//
//        FloatingOrb(
//            modifier = Modifier
//                .size(120.dp)
//                .offset(x = 50.dp, y = 500.dp),
//            color = NeonPink.copy(alpha = 0.05f)
//        )

        Crossfade(
            targetState = uiState.selectedCategory,
            animationSpec = spring(
                stiffness = Spring.StiffnessMediumLow,
                dampingRatio = Spring.DampingRatioNoBouncy
            ),
            label = "CourseScreenCrossfade"
        ) { category ->
            if (category == null) {
                CategorySelectionScreen(viewModel = viewModel)
            } else {
                CourseListScreen(viewModel = viewModel, categoryName = category)
            }
        }
    }
}

@Composable
fun FloatingOrb(
    modifier: Modifier = Modifier,
    color: Color
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "OrbScale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(CircleShape)
            .background(color)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectionScreen(viewModel: CoursesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }

    val categories = uiState.distinctTags
    val filteredCategories = remember(searchText, categories) {
        if (searchText.isEmpty()) {
            categories
        } else {
            categories.filter { it.contains(searchText, ignoreCase = true) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = GlowWhite,
                    fontSize = 32.sp
                )
                Text(
                    text = "Your Learning Journey",
                    style = MaterialTheme.typography.titleMedium,
                    color = GlowWhite.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }

            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = "Magic",
                tint = SolarGold,
                modifier = Modifier.size(28.dp)
            )
        }

        // SEARCH BAR
        LegendarySearchBar(
            searchText = searchText,
            onSearchTextChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        // CATEGORY GRID
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredCategories, key = { it }) { category ->
                GlassmorphismCategoryCard(
                    categoryName = category,
                    onClick = { viewModel.onCategorySelected(category) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegendarySearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp)),
        placeholder = {
            Text(
                text = "Search topics...",
                color = GlowWhite.copy(alpha = 0.62f),
                fontWeight = FontWeight.Medium
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = CyberTeal.copy(alpha = 0.9f)
            )
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { onSearchTextChange("") }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = GlowWhite.copy(alpha = 0.72f)
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = GlowWhite,
            unfocusedTextColor = GlowWhite,
            focusedBorderColor = CyberTeal,
            unfocusedBorderColor = GlowWhite.copy(alpha = 0.24f),
            cursorColor = CyberTeal,
            focusedContainerColor = GlowWhite.copy(alpha = 0.07f),
            unfocusedContainerColor = GlowWhite.copy(alpha = 0.04f),
            focusedLeadingIconColor = CyberTeal,
            unfocusedLeadingIconColor = GlowWhite.copy(alpha = 0.8f),
            focusedTrailingIconColor = GlowWhite.copy(alpha = 0.9f),
            unfocusedTrailingIconColor = GlowWhite.copy(alpha = 0.7f),
            focusedPlaceholderColor = GlowWhite.copy(alpha = 0.62f),
            unfocusedPlaceholderColor = GlowWhite.copy(alpha = 0.62f)
        ),
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}

@Composable
fun GlassmorphismCategoryCard(
    categoryName: String,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "CardScale"
    )

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale)
            .clip(RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        // GLASSMORPHISM BACKGROUND
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(CategoryGlass)
        )

        // BORDER EFFECT
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = BorderGlass,
                    alpha = 0.10f
                )
        )

        // CONTENT
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Gradient,
                    contentDescription = null,
                    tint = CyberTeal,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = GlowWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(viewModel: CoursesViewModel, categoryName: String) {
    val uiState by viewModel.uiState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    var searchText by remember { mutableStateOf("") }

    // Filter courses based on search
    val filteredCourses = remember(searchText, uiState.filteredCourses) {
        if (searchText.isEmpty()) {
            uiState.filteredCourses
        } else {
            uiState.filteredCourses.map { subcategory ->
                subcategory.copy(
                    courses = subcategory.courses.filter { course ->
                        course.title.contains(searchText, ignoreCase = true) ||
                                course.description.contains(searchText, ignoreCase = true) ||
                                course.tags.any { it.contains(searchText, ignoreCase = true) }
                    }
                )
            }.filter { it.courses.isNotEmpty() }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            LegendaryTopBar(
                title = categoryName,
                onBackClick = { viewModel.onCategorySelected(null) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // SEARCH BAR
            LegendarySearchBar(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (filteredCourses.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No courses found matching your search.",
                                color = GlowWhite.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    filteredCourses.forEach { subcategory ->
                        if (subcategory.courses.isNotEmpty()) {
                            item {
                                Text(
                                    text = subcategory.subcategory,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = GlowWhite,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(subcategory.courses, key = { it.id }) { course ->
                                val previewState = uiState.coursePreviews[course.id]
                                LegendaryCourseCard(
                                    course = course,
                                    preview = previewState?.preview,
                                    isLoading = previewState?.isLoading ?: false,
                                    onClick = {
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

                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegendaryTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.22f),
                        Color.Transparent
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(GlowWhite.copy(alpha = 0.12f))
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = GlowWhite,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = GlowWhite,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun LegendaryCourseCard(
    course: Course,
    preview: UnfurlResult?,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "CourseCardScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        // GLASSMORPHISM BACKGROUND
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(GlassBrush)
        )

        // GRADIENT BORDER
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            CyberTeal.copy(alpha = 0.35f),
                            SolarGold.copy(alpha = 0.22f),
                            NeonPink.copy(alpha = 0.18f)
                        )
                    ),
                    alpha = 0.10f
                )
        )

        Column {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = GlowWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = course.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GlowWhite.copy(alpha = 0.82f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LegendaryChip(
                        text = course.difficulty,
                        color = NeonPink
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(course.tags) { tag ->
                            LegendaryChip(
                                text = tag,
                                color = CyberTeal
                            )
                        }
                    }
                }
            }

            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    (fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) togetherWith
                            fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)))
                },
                label = "PreviewLoading"
            ) { loading ->
                if (loading) {
                    LegendaryLoadingState()
                } else if (preview != null) {
                    LegendaryLinkPreview(data = preview, onClick = onClick)
                }
            }
        }
    }
}

@Composable
fun LegendaryChip(
    text: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        color.copy(alpha = 0.20f),
                        color.copy(alpha = 0.10f)
                    )
                )
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = GlowWhite,
            fontSize = 12.sp
        )
    }
}

@Composable
fun LegendaryLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        GlowWhite.copy(alpha = 0.03f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(28.dp),
            color = CyberTeal,
            strokeWidth = 3.dp
        )
    }
}

@Composable
fun LegendaryLinkPreview(data: UnfurlResult, onClick: () -> Unit) {
    data.thumbnail?.toString()?.let { imageUrl ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = data.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            // GRADIENT OVERLAY
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                DeepSpace.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // PLAY BUTTON WITH GLASSMORPHISM
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                GlowWhite.copy(alpha = 0.22f),
                                CyberTeal.copy(alpha = 0.14f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.PlayArrow,
                    contentDescription = "Play",
                    tint = GlowWhite,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
