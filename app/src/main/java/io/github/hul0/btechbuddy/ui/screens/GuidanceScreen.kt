package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.hul0.btechbuddy.data.model.Faq
import io.github.hul0.btechbuddy.data.model.Roadmap
import io.github.hul0.btechbuddy.viewmodel.GuidanceViewModel
import io.github.hul0.btechbuddy.viewmodel.Tab
import io.github.hul0.btechbuddy.ui.theme.* // Color.kt palette

@Composable
fun GuidanceScreen(viewModel: GuidanceViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Compact TabRow with thin indicator and no elevation/shadow
        TabRow(
            selectedTabIndex = uiState.selectedTab.ordinal,
            containerColor = Gray50,
            contentColor = Gray900,
            indicator = { tabPositions ->
                if (uiState.selectedTab.ordinal < tabPositions.size) {
                    val tabPos = tabPositions[uiState.selectedTab.ordinal]
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Box(
                            Modifier
                                .width(tabPos.width)
                                .height(2.dp)
                                .offset(x = tabPos.left)
                                .background(Blue700)
                        )
                    }
                }
            },
            divider = {
                HorizontalDivider(color = Gray200, thickness = 1.dp)
            }
        ) {
            Tab.entries.forEach { tab ->
                val (iconTint, icon) = when (tab) {
                    Tab.ROADMAPS -> Blue700 to Icons.Default.Explore
                    Tab.FAQS -> Indigo700 to Icons.AutoMirrored.Filled.Help
                }
                Tab(
                    selected = uiState.selectedTab == tab,
                    onClick = { viewModel.selectTab(tab) },
                    selectedContentColor = iconTint,
                    unselectedContentColor = Gray700,
                    icon = {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = if (uiState.selectedTab == tab) iconTint else Gray700
                        )
                    },
                    text = {
                        Text(
                            tab.title,
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1
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

@Composable
fun RoadmapsContent(roadmaps: List<io.github.hul0.btechbuddy.data.model.CareerRoadmap>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        roadmaps.forEach { category ->
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timeline, contentDescription = null, tint = Blue700, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = category.category,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Gray900
                    )
                }
            }
            items(category.roadmaps) { roadmap ->
                RoadmapCard(roadmap = roadmap)
            }
        }
    }
}

@Composable
fun FaqsContent(faqs: List<io.github.hul0.btechbuddy.data.model.FaqCategory>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        faqs.forEach { category ->
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Help, contentDescription = null, tint = Indigo700, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = category.category,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Gray900
                    )
                }
            }
            items(category.faqs) { faq ->
                FaqCard(faq = faq)
            }
        }
    }
}

@Composable
fun RoadmapCard(roadmap: Roadmap) {
    var expanded by remember { mutableStateOf(false) }

    // Outlined style, no elevation/shadows/gradients
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Blue50),
        border = BorderStroke(1.dp, Blue100)
    ) {
        Column(
            modifier = Modifier
                .clickable(role = Role.Button) { expanded = !expanded }
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Blue100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Route,
                        contentDescription = null,
                        tint = Blue700,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = roadmap.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Blue900
                    )
                    Text(
                        text = roadmap.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray800,
                        maxLines = 2
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Blue700
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    roadmap.steps.forEachIndexed { index, step ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Blue700)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "${index + 1}. $step",
                                style = MaterialTheme.typography.bodySmall,
                                color = Gray900,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
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

    // Outlined style, no elevation/shadows/gradients
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Indigo100),
        border = BorderStroke(1.dp, Gray200)
    ) {
        Column(
            modifier = Modifier
                .clickable(role = Role.Button) { expanded = !expanded }
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Gray100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Help,
                        contentDescription = null,
                        tint = Indigo700,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = faq.question,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Gray900,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Indigo700
                )
            }

            AnimatedVisibility(visible = expanded) {
                Text(
                    text = faq.answer,
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray800,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}
