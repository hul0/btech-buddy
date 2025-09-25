package io.github.hul0.btechbuddy.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.hul0.btechbuddy.data.model.Module
import io.github.hul0.btechbuddy.viewmodel.LearningViewModel
import io.github.hul0.btechbuddy.ui.theme.*

// Legendary palette hooks (reuse your palette if already defined)
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

private val CardBorder = Brush.linearGradient(
    colors = listOf(
        CyberTeal.copy(alpha = 0.45f),
        ElectricPurple.copy(alpha = 0.30f),
        GlowWhite.copy(alpha = 0.08f)
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningPathDetailScreen(
    pathId: String,
    viewModel: LearningViewModel,
    navController: NavController
) {
    val learningPath by viewModel.getLearningPathById(pathId).collectAsState(initial = null)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkHeroGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                // Scrim behind top bar for legibility
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
                ) {
                    TopAppBar(
                        title = {
                            Text(
                                learningPath?.title ?: "Details",
                                style = MaterialTheme.typography.titleLarge,
                                color = GlowWhite
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = GlowWhite
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = GlowWhite,
                            navigationIconContentColor = GlowWhite,
                            actionIconContentColor = GlowWhite
                        )
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                // Section label to orient users
                Text(
                    text = if (learningPath?.modules?.isNotEmpty() == true) "Modules" else "",
                    style = MaterialTheme.typography.titleMedium,
                    color = GlowWhite.copy(alpha = 0.8f),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 4.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header card with path info
                    item {
                        learningPath?.let { itLp ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                tonalElevation = 0.dp,
                                shadowElevation = 0.dp,
                                color = Color.Transparent,
                                border = BorderStroke(1.dp, Color.Unspecified)
                            ) {
                                // Card background + border layers
                                Box(
                                    modifier = Modifier
                                        .background(GlassCard, RoundedCornerShape(16.dp))
                                        .border(
                                            BorderStroke(1.dp, Color(0x33FFFFFF)),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(CircleShape)
                                                .background(CyberTeal.copy(alpha = 0.16f)),
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
                                                itLp.title,
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = GlowWhite
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                itLp.description,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = GlowWhite.copy(alpha = 0.8f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Module items
                    items(learningPath?.modules ?: emptyList()) { module ->
                        ModuleItem(
                            module = module,
                            viewModel = viewModel
                        )
                    }

                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun ModuleItem(
    module: Module,
    viewModel: LearningViewModel
) {
    val isCompleted by viewModel.isModuleCompleted(module.id).collectAsState(initial = false)

    // Dark list row using glass surface and accent states
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        color = Color.Transparent
    ) {
        val containerBrush = if (isCompleted) {
            // Slight success tint for completed state
            Brush.linearGradient(
                listOf(
                    SuccessGreen.copy(alpha = 0.14f),
                    GlowWhite.copy(alpha = 0.05f)
                )
            )
        } else {
            GlassCard
        }

        Box(
            modifier = Modifier
                .background(containerBrush, RoundedCornerShape(14.dp))
                .border(
                    BorderStroke(
                        1.dp,
                        if (isCompleted) SuccessGreen.copy(alpha = 0.45f) else Color(0x33FFFFFF)
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                .clickable {
                    // Keep existing interaction: toggle completion on whole row
                    viewModel.setModuleCompleted(module.id, !isCompleted)
                }
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { viewModel.setModuleCompleted(module.id, it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = SuccessGreen,
                        uncheckedColor = GlowWhite.copy(alpha = 0.45f),
                        checkmarkColor = Color.Black, // strong contrast over success
                        disabledCheckedColor = SuccessGreen.copy(alpha = 0.6f),
                        disabledUncheckedColor = GlowWhite.copy(alpha = 0.25f)
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (isCompleted) SuccessGreen.copy(alpha = 0.20f) else CyberTeal.copy(alpha = 0.16f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Article,
                        contentDescription = null,
                        tint = if (isCompleted) SuccessGreen else CyberTeal
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = module.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isCompleted) GlowWhite.copy(alpha = 0.85f) else GlowWhite,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
