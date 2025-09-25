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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hul0.btechbuddy.data.model.Roadmap
import io.github.hul0.btechbuddy.viewmodel.DashboardViewModel
import io.github.hul0.btechbuddy.ui.theme.*

// Legendary palette (aligned with other screens)
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
private val GlassCard = Brush.linearGradient(
    colors = listOf(
        GlowWhite.copy(alpha = 0.10f),
        GlowWhite.copy(alpha = 0.06f),
        GlowWhite.copy(alpha = 0.04f)
    )
)

private val AccentBorder = Brush.linearGradient(
    colors = listOf(
        CyberTeal.copy(alpha = 0.45f),
        ElectricPurple.copy(alpha = 0.30f),
        GlowWhite.copy(alpha = 0.08f)
    )
)

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkHeroGradient),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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

        item { Spacer(Modifier.height(80.dp)) }
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
                color = GlowWhite.copy(alpha = 0.8f)
            )
            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = GlowWhite
            )
        }
        CompactIconChip(
            icon = Icons.Default.Notifications,
            container = GlowWhite.copy(alpha = 0.10f),
            tint = GlowWhite
        )
        Spacer(Modifier.width(8.dp))
        CompactIconChip(
            icon = Icons.Default.Settings,
            container = GlowWhite.copy(alpha = 0.10f),
            tint = GlowWhite
        )
        Spacer(Modifier.width(8.dp))
        CompactIconChip(
            icon = Icons.Default.Person,
            container = GlowWhite.copy(alpha = 0.10f),
            tint = GlowWhite
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
            .size(38.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(container)
            .border(BorderStroke(1.dp, GlowWhite.copy(alpha = 0.22f)), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun StatisticsGrid(progress: Float, modulesCompleted: Int, totalModules: Int) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Insights, contentDescription = null, tint = CyberTeal, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Your Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = GlowWhite
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Completion",
                value = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Animated circular indicator
                        val pct = remember(progress) { (progress.coerceIn(0f, 1f) * 100).toInt() }
                        CircularProgressIndicator(
                            progress = { progress.coerceIn(0f, 1f) },
                            modifier = Modifier.size(56.dp),
                            strokeWidth = 5.dp,
                            color = CyberTeal,
                            trackColor = GlowWhite.copy(alpha = 0.12f)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "$pct%",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CyberTeal
                        )
                    }
                },
                icon = Icons.Default.CheckCircle,
                color = CyberTeal
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Modules",
                value = {
                    Text(
                        text = "$modulesCompleted/$totalModules",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = GlowWhite
                    )
                },
                icon = Icons.Default.School,
                color = ElectricPurple
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
    color: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, GlowWhite.copy(alpha = 0.20f))
    ) {
        Column(
            modifier = Modifier
                .background(GlassCard, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.16f))
                        .border(1.dp, color.copy(alpha = 0.45f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = GlowWhite.copy(alpha = 0.9f)
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
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, GlowWhite.copy(alpha = 0.20f))
    ) {
        Row(
            modifier = Modifier
                .background(GlassCard, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(IntrinsicSize.Min)
                    .background(CyberTeal)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.FormatQuote,
                        contentDescription = null,
                        tint = GlowWhite,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "\"$quote\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = GlowWhite
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "- $author",
                    style = MaterialTheme.typography.labelMedium,
                    color = GlowWhite.copy(alpha = 0.9f),
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
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, GlowWhite.copy(alpha = 0.20f))
    ) {
        Row(
            modifier = Modifier
                .background(GlassCard, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(NeonPink.copy(alpha = 0.16f))
                    .border(1.dp, NeonPink.copy(alpha = 0.45f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = NeonPink
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Recommended for you",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = GlowWhite
                )
                Text(
                    text = "Explore the ${roadmap.title} roadmap",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GlowWhite.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun QuickActions() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Bolt, contentDescription = null, tint = ElectricPurple, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = GlowWhite
            )
        }
        Spacer(Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionCard(
                title = "Daily Challenge",
                subtitle = "Solve today's aptitude question",
                icon = Icons.Default.Lightbulb,
                accent = CyberTeal
            )
            QuickActionCard(
                title = "Community Forum",
                subtitle = "Ask questions and help peers",
                icon = Icons.Default.Groups,
                accent = ElectricPurple
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
    accent: Color
) {
    Card(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, GlowWhite.copy(alpha = 0.20f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .background(GlassCard, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.16f))
                    .border(1.dp, accent.copy(alpha = 0.45f), CircleShape),
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
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = GlowWhite)
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = GlowWhite.copy(alpha = 0.9f))
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = GlowWhite, modifier = Modifier.size(20.dp))
        }
    }
}
