package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hul0.btechbuddy.data.model.Roadmap
import io.github.hul0.btechbuddy.viewmodel.DashboardViewModel
import io.github.hul0.btechbuddy.ui.theme.* // Uses Color.kt palette

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            GreetingHeader(greeting = uiState.greeting)
        }
        item {
            StatisticsGrid(
                progress = uiState.learningProgress,
                modulesCompleted = uiState.modulesCompleted,
                totalModules = uiState.totalModules
            )
        }
        item {
            QuoteOfTheDayCard(
                quote = uiState.quote.first,
                author = uiState.quote.second
            )
        }
        uiState.recommendedRoadmap?.let { roadmap ->
            item {
                RecommendedRoadmapCard(roadmap = roadmap)
            }
        }
        item {
            QuickActions()
        }
    }
}

@Composable
fun GreetingHeader(greeting: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Hello there,",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        // Compact icon actions, no elevation/shadows/gradients
        CompactIconChip(
            icon = Icons.Default.Notifications,
            container = Blue50,
            tint = Blue700
        )
        Spacer(Modifier.width(8.dp))
        CompactIconChip(
            icon = Icons.Default.Settings,
            container = Gray100,
            tint = Gray800
        )
        Spacer(Modifier.width(8.dp))
        CompactIconChip(
            icon = Icons.Default.Person,
            container = Green50,
            tint = Green700
        )
    }
}

@Composable
private fun CompactIconChip(
    icon: ImageVector,
    container: Color,
    tint: Color
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(container)
            .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun StatisticsGrid(progress: Float, modulesCompleted: Int, totalModules: Int) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Insights, contentDescription = null, tint = Blue700, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Your Progress",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Completion",
                value = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            progress = progress,
                            modifier = Modifier.size(48.dp),
                            strokeWidth = 4.dp,
                            color = Blue700,
                            trackColor = Blue100
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Blue800
                        )
                    }
                },
                icon = Icons.Default.CheckCircle,
                color = Blue700,
                container = Blue50,
                border = Blue100
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Modules",
                value = {
                    Text(
                        text = "$modulesCompleted/$totalModules",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Green800
                    )
                },
                icon = Icons.Default.School,
                color = Green700,
                container = Green50,
                border = Green100
            )
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: @Composable () -> Unit,
    icon: ImageVector,
    color: Color,
    container: Color,
    border: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = container,
        border = BorderStroke(1.dp, border)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Gray800
                )
            }
            value()
        }
    }
}

@Composable
fun QuoteOfTheDayCard(quote: String, author: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Indigo100,
        border = BorderStroke(1.dp, Gray200)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Accent bar without elevation/shadow
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(IntrinsicSize.Min)
                    .background(Indigo700)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.FormatQuote,
                        contentDescription = null,
                        tint = Indigo700,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "\"$quote\"",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = Charcoal
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "- $author",
                    style = MaterialTheme.typography.labelSmall,
                    color = Slate,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun RecommendedRoadmapCard(roadmap: Roadmap) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Orange50,
        border = BorderStroke(1.dp, Orange100)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Orange100),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Orange700
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Recommended for you",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Orange800
                )
                Text(
                    text = "Explore the ${roadmap.title} roadmap",
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray800
                )
            }
        }
    }
}

@Composable
fun QuickActions() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Bolt, contentDescription = null, tint = Purple700, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickActionCard(
                title = "Daily Challenge",
                subtitle = "Solve today's aptitude question",
                icon = Icons.Default.Lightbulb,
                container = Yellow100,
                accent = Amber
            )
            QuickActionCard(
                title = "Community Forum",
                subtitle = "Ask questions and help peers",
                icon = Icons.Default.Groups,
                container = Blue100,
                accent = Blue700
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    container: Color,
    accent: Color
) {
    Card(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = container),
        border = BorderStroke(1.dp, container.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = accent
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = Gray900)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Gray700)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Gray700, modifier = Modifier.size(18.dp))
        }
    }
}
