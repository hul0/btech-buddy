package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.hul0.btechbuddy.data.model.Faq
import io.github.hul0.btechbuddy.data.model.Roadmap
import io.github.hul0.btechbuddy.viewmodel.GuidanceViewModel
import io.github.hul0.btechbuddy.viewmodel.Tab
import io.github.hul0.btechbuddy.ui.theme.*

// Palette snippets aligned with other screens
private val DeepSpace = Color(0xFF0D1421)
private val NearBlack = Color(0xFF070B14)
private val CyberTeal = Color(0xFF1BFFFF)
private val ElectricPurple = Color(0xFF8458B3)
private val NeonPink = Color(0xFFD4145A)
private val GlowWhite = Color(0xFFFFFFFF)
private val SuccessGreen = Color(0xFF00C853)

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
fun GuidanceScreen(viewModel: GuidanceViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkHeroGradient)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Tabs with neon indicator on dark scrim
            TabRow(
                selectedTabIndex = uiState.selectedTab.ordinal,
                containerColor = Color.Transparent,
                contentColor = GlowWhite,
                indicator = { tabPositions ->
                    if (uiState.selectedTab.ordinal < tabPositions.size) {
                        val tabPos = tabPositions[uiState.selectedTab.ordinal]
                        Box(Modifier.fillMaxWidth()) {
                            Box(
                                Modifier
                                    .width(tabPos.width)
                                    .height(3.dp)
                                    .offset(x = tabPos.left)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            listOf(CyberTeal, ElectricPurple, NeonPink)
                                        ),
                                        shape = RoundedCornerShape(3.dp)
                                    )
                            )
                        }
                    }
                },
                divider = {
                    HorizontalDivider(color = GlowWhite.copy(alpha = 0.12f), thickness = 1.dp)
                },
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.22f),
                                Color.Transparent
                            )
                        )
                    )
            ) {
                Tab.entries.forEach { tab ->
                    val icon = when (tab) {
                        Tab.ROADMAPS -> Icons.Filled.Explore
                        Tab.FAQS -> Icons.AutoMirrored.Filled.Help
                    }
                    Tab(
                        selected = uiState.selectedTab == tab,
                        onClick = { viewModel.selectTab(tab) },
                        selectedContentColor = GlowWhite,
                        unselectedContentColor = GlowWhite.copy(alpha = 0.7f),
                        icon = { Icon(icon, contentDescription = null, tint = GlowWhite) },
                        text = {
                            Text(
                                tab.title,
                                style = MaterialTheme.typography.labelLarge,
                                maxLines = 1,
                                color = GlowWhite
                            )
                        }
                    )
                }
            }

            when (uiState.selectedTab) {
                Tab.ROADMAPS -> RoadmapsContent(uiState.roadmaps)
                Tab.FAQS -> FaqsContent(uiState.faqs)
            }
        }
    }
}

@Composable
fun RoadmapsContent(roadmaps: List<io.github.hul0.btechbuddy.data.model.CareerRoadmap>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        roadmaps.forEach { category ->
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Timeline,
                        contentDescription = null,
                        tint = CyberTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = category.category,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = GlowWhite
                    )
                }
            }
            items(category.roadmaps) { roadmap ->
                RoadmapCard(roadmap = roadmap)
            }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun FaqsContent(faqs: List<io.github.hul0.btechbuddy.data.model.FaqCategory>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        faqs.forEach { category ->
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.AutoMirrored.Filled.Help,
                        contentDescription = null,
                        tint = ElectricPurple,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = category.category,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = GlowWhite
                    )
                }
            }
            items(category.faqs) { faq ->
                FaqCard(faq = faq)
            }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun RoadmapCard(roadmap: Roadmap) {
    var expanded by remember { mutableStateOf(false) }

    // Screenshot-worthy glass card with accent border
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        color = Color.Transparent,
        border = BorderStroke(1.dp, GlowWhite.copy(alpha = 0.20f))
    ) {
        Column(
            modifier = Modifier
                .background(GlassCard, RoundedCornerShape(18.dp))
                .clickable(role = Role.Button) { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(CyberTeal.copy(alpha = 0.16f))
                        .border(1.dp, CyberTeal.copy(alpha = 0.45f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Route,
                        contentDescription = null,
                        tint = CyberTeal
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = roadmap.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = GlowWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = roadmap.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GlowWhite.copy(alpha = 0.82f),
                        maxLines = 3
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = GlowWhite
                )
            }

            AnimatedVisibility(visible = expanded) {
                // Neon timeline steps
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    roadmap.steps.forEachIndexed { index, step ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            // Step dot + connector
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(
                                            brush = Brush.radialGradient(
                                                listOf(
                                                    CyberTeal.copy(alpha = 0.9f),
                                                    CyberTeal.copy(alpha = 0.2f)
                                                )
                                            )
                                        )
                                )
                                if (index != roadmap.steps.lastIndex) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(22.dp)
                                            .background(
                                                Brush.verticalGradient(
                                                    listOf(
                                                        CyberTeal.copy(alpha = 0.5f),
                                                        ElectricPurple.copy(alpha = 0.5f)
                                                    )
                                                )
                                            )
                                    )
                                }
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Step ${index + 1}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = CyberTeal
                                )
                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = GlowWhite,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FaqCard(faq: Faq) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        color = Color.Transparent,
        border = BorderStroke(1.dp, GlowWhite.copy(alpha = 0.20f))
    ) {
        Column(
            modifier = Modifier
                .background(GlassCard, RoundedCornerShape(16.dp))
                .clickable(role = Role.Button) { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ElectricPurple.copy(alpha = 0.16f))
                        .border(1.dp, ElectricPurple.copy(alpha = 0.45f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Help,
                        contentDescription = null,
                        tint = ElectricPurple
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = faq.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = GlowWhite,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = GlowWhite
                )
            }

            AnimatedVisibility(visible = expanded) {
                Text(
                    text = faq.answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GlowWhite.copy(alpha = 0.9f),
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}
