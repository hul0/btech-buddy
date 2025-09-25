package io.github.hul0.btechbuddy.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.hul0.btechbuddy.data.model.LearningPath
import io.github.hul0.btechbuddy.navigation.Screen
import io.github.hul0.btechbuddy.viewmodel.LearningViewModel
import me.saket.unfurl.UnfurlResult
import io.github.hul0.btechbuddy.ui.theme.*

// Legendary palette (align with other screens)
private val DeepSpace = Color(0xFF0D1421)
private val NearBlack = Color(0xFF070B14)
private val CyberTeal = Color(0xFF1BFFFF)
private val ElectricPurple = Color(0xFF8458B3)
private val NeonPink = Color(0xFFD4145A)
private val GlowWhite = Color(0xFFFFFFFF)

// Background gradient
private val DarkHeroGradient = Brush.verticalGradient(
    colors = listOf(
        DeepSpace,
        Color(0xFF0B0F1A),
        NearBlack
    )
)

// Glass brushes
private val GlassField = Brush.linearGradient(
    colors = listOf(
        GlowWhite.copy(alpha = 0.10f),
        GlowWhite.copy(alpha = 0.06f),
        GlowWhite.copy(alpha = 0.04f)
    )
)

private val CardBorder = Brush.linearGradient(
    colors = listOf(
        CyberTeal.copy(alpha = 0.45f),
        ElectricPurple.copy(alpha = 0.30f),
        GlowWhite.copy(alpha = 0.08f)
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(viewModel: LearningViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkHeroGradient)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search + filters container with transparent glass
            Column(
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
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                // Search field (dark tuned)
                OutlinedTextField(
                    value = uiState.searchInput,
                    onValueChange = viewModel::onSearchInputChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(GlassField, RoundedCornerShape(20.dp)),
                    placeholder = {
                        Text(
                            "Search learning paths...",
                            color = GlowWhite.copy(alpha = 0.62f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = CyberTeal
                        )
                    },
                    trailingIcon = if (uiState.searchInput.isNotEmpty()) {
                        {
                            IconButton(onClick = { viewModel.onSearchInputChanged("") }) {
                                Icon(
                                    Icons.Outlined.Close,
                                    contentDescription = "Clear",
                                    tint = GlowWhite.copy(alpha = 0.8f)
                                )
                            }
                        }
                    } else null,
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberTeal,
                        unfocusedBorderColor = GlowWhite.copy(alpha = 0.24f),
                        cursorColor = CyberTeal,
                        focusedTextColor = GlowWhite,
                        unfocusedTextColor = GlowWhite,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedPlaceholderColor = GlowWhite.copy(alpha = 0.62f),
                        unfocusedPlaceholderColor = GlowWhite.copy(alpha = 0.62f),
                        focusedLeadingIconColor = CyberTeal,
                        unfocusedLeadingIconColor = GlowWhite.copy(alpha = 0.9f),
                        focusedTrailingIconColor = GlowWhite.copy(alpha = 0.9f),
                        unfocusedTrailingIconColor = GlowWhite.copy(alpha = 0.8f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Filter chips row
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(uiState.allTags) { tag ->
                        val selected = uiState.selectedTag == tag
                        FilterChip(
                            selected = selected,
                            onClick = { viewModel.onTagSelected(tag) },
                            label = {
                                Text(
                                    tag,
                                    color = if (selected) GlowWhite else GlowWhite.copy(alpha = 0.9f)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Label,
                                    contentDescription = null,
                                    tint = if (selected) GlowWhite else GlowWhite.copy(alpha = 0.9f)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = GlowWhite.copy(alpha = 0.06f),
                                labelColor = GlowWhite,
                                iconColor = GlowWhite,
                                selectedContainerColor = CyberTeal.copy(alpha = 0.28f),
                                selectedLabelColor = Color.Black,
                                selectedLeadingIconColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                1.dp,
                                if (selected) CyberTeal.copy(alpha = 0.65f) else GlowWhite.copy(alpha = 0.20f)
                            )
                        )
                    }
                }
            }

            Divider(
                color = GlowWhite.copy(alpha = 0.12f),
                thickness = 1.dp
            )

            if (uiState.filteredLearningPaths.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No matching learning paths found.",
                        color = GlowWhite.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
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
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningPathCard(
    path: LearningPath,
    onClick: () -> Unit,
    onYoutubeClick: (String) -> Unit
) {
    // Outlined glass card
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, GlowWhite.copy(alpha = 0.20f))
    ) {
        Column(
            modifier = Modifier
                .background(GlassField)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(CyberTeal.copy(alpha = 0.16f))
                        .border(1.dp, CyberTeal.copy(alpha = 0.45f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Timeline,
                        contentDescription = null,
                        tint = CyberTeal
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = path.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = GlowWhite
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = path.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GlowWhite.copy(alpha = 0.82f),
                        maxLines = 3
                    )
                }
                Icon(
                    imageVector = Icons.Filled.Sell,
                    contentDescription = null,
                    tint = GlowWhite.copy(alpha = 0.9f),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val progress = if (path.modules.isNotEmpty()) {
                path.completedModules.toFloat() / path.modules.size
            } else 0f

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                    color = CyberTeal,
                    trackColor = GlowWhite.copy(alpha = 0.12f)
                )
                Text(
                    text = "${path.completedModules}/${path.modules.size} done",
                    style = MaterialTheme.typography.labelMedium,
                    color = GlowWhite.copy(alpha = 0.9f)
                )
            }
        }
    }
}
