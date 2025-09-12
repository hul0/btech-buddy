package io.github.hul0.btechbuddy.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.hul0.btechbuddy.data.model.LearningPath
import io.github.hul0.btechbuddy.navigation.Screen
import io.github.hul0.btechbuddy.viewmodel.LearningViewModel
import me.saket.unfurl.UnfurlResult
import io.github.hul0.btechbuddy.ui.theme.* // Color.kt palette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(viewModel: LearningViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        // Header: compact, no elevation/shadow/gradient
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray50)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchInput,
                onValueChange = viewModel::onSearchInputChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search learning paths...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search", tint = Blue700) },
                trailingIcon = if (uiState.searchInput.isNotEmpty()) {
                    {
                        IconButton(onClick = { viewModel.onSearchInputChanged("") }) {
                            Icon(Icons.Outlined.Close, contentDescription = "Clear", tint = Gray700)
                        }
                    }
                } else null,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue700,
                    unfocusedBorderColor = Gray300,
                    focusedLabelColor = Blue700,
                    cursorColor = Blue700,
                    focusedTextColor = Gray900,
                    unfocusedTextColor = Gray900,
                    focusedContainerColor = Gray50,
                    unfocusedContainerColor = Gray50,
                    focusedPlaceholderColor = Gray500,
                    unfocusedPlaceholderColor = Gray500
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.allTags) { tag ->
                    FilterChip(
                        selected = uiState.selectedTag == tag,
                        onClick = { viewModel.onTagSelected(tag) },
                        label = { Text(tag) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Label,
                                contentDescription = null,
                                tint = if (uiState.selectedTag == tag) Blue700 else Gray700
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Gray100,
                            labelColor = Gray800,
                            iconColor = Gray700,
                            selectedContainerColor = Blue100,
                            selectedLabelColor = Blue900,
                            selectedLeadingIconColor = Blue700
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (uiState.selectedTag == tag) Blue100 else Gray200)
                    )
                }
            }
        }
        Divider(color = Gray200, thickness = 1.dp)

        if (uiState.filteredLearningPaths.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No matching learning paths found.", color = Gray700)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
    // Outlined, compact, no elevation/shadow
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Blue50),
        border = BorderStroke(1.dp, Blue100)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Blue100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Timeline, contentDescription = null, tint = Blue700)
                }
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = path.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Blue900
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = path.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray800,
                        maxLines = 3
                    )
                }
                // Minimal forward affordance without shadow
                Icon(
                    imageVector = Icons.Filled.Sell,
                    contentDescription = null,
                    tint = Blue700,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val progress = if (path.modules.isNotEmpty()) {
                path.completedModules.toFloat() / path.modules.size
            } else 0f

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp),
                    color = Blue700,
                    trackColor = Blue100
                )
                Text(
                    text = "${path.completedModules}/${path.modules.size} done",
                    style = MaterialTheme.typography.labelSmall,
                    color = Gray800
                )
            }
        }
    }
}
