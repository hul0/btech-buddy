package io.github.hul0.makautminds.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.hul0.makautminds.viewmodel.CoursesViewModel
import me.saket.unfurl.UnfurlResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(viewModel: CoursesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar
        OutlinedTextField(
            value = uiState.searchInput,
            onValueChange = viewModel::onSearchInputChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            label = { Text("Search Courses") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            singleLine = true
        )

        // Filter Tags
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.allTags) { tag ->
                FilterChip(
                    selected = uiState.selectedTag == tag,
                    onClick = { viewModel.onTagSelected(tag) },
                    label = { Text(tag) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Courses List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            uiState.filteredCourses.forEach { category ->
                item {
                    Text(
                        text = category.category,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                category.subcategories.forEach { subcategory ->
                    item {
                        Text(
                            text = subcategory.subcategory,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                        )
                    }
                    items(subcategory.courses) { course ->
                        val previewState = uiState.coursePreviews[course.id]
                        CourseCard(
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

@Composable
fun CourseCard(
    course: io.github.hul0.makautminds.data.model.Course,
    preview: UnfurlResult?,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = course.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    course.tags.forEach { tag ->
                        SuggestionChip(onClick = {}, label = { Text(tag) })
                    }
                }
            }

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                preview != null -> {
                    LinkPreview(data = preview, onClick = onClick)
                }
            }
        }
    }
}

@Composable
fun LinkPreview(data: UnfurlResult, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        data.thumbnail?.toString()?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = data.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        data.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        data.description?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

