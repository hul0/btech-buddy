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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.hul0.btechbuddy.data.model.Course
import io.github.hul0.btechbuddy.viewmodel.CoursesViewModel
import me.saket.unfurl.UnfurlResult
import io.github.hul0.btechbuddy.ui.theme.* // use Color.kt palette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(viewModel: CoursesViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Crossfade(
        targetState = uiState.selectedCategory,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioNoBouncy),
        label = "CourseScreenCrossfade"
    ) { category ->
        if (category == null) {
            CategorySelectionScreen(viewModel = viewModel)
        } else {
            CourseListScreen(viewModel = viewModel, categoryName = category)
        }
    }
}

@Composable
fun CategorySelectionScreen(viewModel: CoursesViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Show one tile per distinct tag (or curated category if you prefer)
    val categories = uiState.distinctTags

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories, key = { it }) { category ->
            CategoryCard(
                categoryName = category,
                onClick = { viewModel.onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryCard(categoryName: String, onClick: () -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Blue50),
        border = BorderStroke(1.dp, Blue100)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Blue900,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(viewModel: CoursesViewModel, categoryName: String) {
    val uiState by viewModel.uiState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName, fontWeight = FontWeight.SemiBold, color = Gray900) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onCategorySelected(null) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Blue700)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Gray50,
                    titleContentColor = Gray900,
                    navigationIconContentColor = Blue700
                )
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            Divider(color = Gray200, thickness = 1.dp)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.filteredCourses.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No courses available in this category yet.", color = Gray700)
                        }
                    }
                } else {
                    uiState.filteredCourses.forEach { subcategory ->
                        item {
                            Text(
                                text = subcategory.subcategory,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Gray900
                            )
                        }
                        items(subcategory.courses, key = { it.id }) { course ->
                            val previewState = uiState.coursePreviews[course.id]
                            PremiumCourseCard(
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
        }
    }
}

@Composable
fun PremiumCourseCard(
    course: Course,
    preview: UnfurlResult?,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Gray50),
        border = BorderStroke(1.dp, Gray200)
    ) {
        Column {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Gray900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = course.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray800,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(shape = RoundedCornerShape(8.dp), color = Green100) {
                        Text(
                            text = course.difficulty,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Green700,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(course.tags) { tag ->
                            Surface(shape = RoundedCornerShape(8.dp), color = Blue100) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Blue700,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
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
                    PremiumLoadingState()
                } else if (preview != null) {
                    PremiumLinkPreview(data = preview, onClick = onClick)
                }
            }
        }
    }
}

@Composable
fun PremiumLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Gray100),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Blue700)
    }
}

@Composable
fun PremiumLinkPreview(data: UnfurlResult, onClick: () -> Unit) {
    data.thumbnail?.toString()?.let { imageUrl ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 14.dp, bottomEnd = 14.dp))
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
            // No gradient overlay; keep a minimal solid scrim if desired
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(Gray900.copy(alpha = 0.35f))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Blue700),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.PlayArrow, contentDescription = "Play", tint = Gray50)
            }
        }
    }
}
