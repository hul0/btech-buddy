package io.github.hul0.btechbuddy.ui.screens

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
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.hul0.btechbuddy.data.model.LearningPath
import io.github.hul0.btechbuddy.navigation.Screen
import io.github.hul0.btechbuddy.viewmodel.LearningViewModel
import me.saket.unfurl.UnfurlResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(viewModel: LearningViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        // Search and Filter UI
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = uiState.searchInput,
                    onValueChange = viewModel::onSearchInputChanged,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search learning paths...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = if (uiState.searchInput.isNotEmpty()) {
                        {
                            IconButton(onClick = { viewModel.onSearchInputChanged("") }) {
                                Icon(Icons.Outlined.Close, contentDescription = "Clear")
                            }
                        }
                    } else null,
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.allTags) { tag ->
                        FilterChip(
                            selected = uiState.selectedTag == tag,
                            onClick = { viewModel.onTagSelected(tag) },
                            label = { Text(tag) }
                        )
                    }
                }
            }
        }


        if (uiState.filteredLearningPaths.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No matching learning paths found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.filteredLearningPaths, key = { it.id }) { path ->
                    LearningPathCard(
                        path = path,
                        onClick = { navController.navigate(Screen.LearningPathDetail.createRoute(path.id)) },
                        onYoutubeClick = { url ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningPathCard(path: LearningPath, onClick: () -> Unit, onYoutubeClick: (String) -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = path.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = path.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            val progress = if (path.modules.isNotEmpty()) {
                path.completedModules.toFloat() / path.modules.size
            } else {
                0f
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${path.completedModules}/${path.modules.size} done",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
