package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.hul0.btechbuddy.data.model.Module
import io.github.hul0.btechbuddy.viewmodel.LearningViewModel
import io.github.hul0.btechbuddy.ui.theme.* // Color.kt palette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningPathDetailScreen(
    pathId: String,
    viewModel: LearningViewModel,
    navController: NavController
) {
    val learningPath by viewModel.getLearningPathById(pathId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        learningPath?.title ?: "Details",
                        style = MaterialTheme.typography.titleMedium,
                        color = Gray900
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Blue700
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Gray50,
                    titleContentColor = Gray900,
                    navigationIconContentColor = Blue700,
                    actionIconContentColor = Gray800
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Divider(color = Gray200, thickness = 1.dp)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    learningPath?.let {
                        // Header: outlined and compact (no elevation/shadows/gradients)
                        OutlinedCard(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Blue50),
                            border = BorderStroke(1.dp, Blue100)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Blue100),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Filled.Timeline,
                                        contentDescription = null,
                                        tint = Blue700
                                    )
                                }
                                Spacer(Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        it.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Blue900
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        it.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Gray800
                                    )
                                }
                            }
                        }
                    }
                }

                items(learningPath?.modules ?: emptyList()) { module ->
                    ModuleItem(
                        module = module,
                        viewModel = viewModel
                    )
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

    // Outlined row card: compact, colorful, no elevation/shadows/gradients
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isCompleted) Green50 else Gray50),
        border = BorderStroke(1.dp, if (isCompleted) Green100 else Gray200)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.setModuleCompleted(module.id, !isCompleted)
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { viewModel.setModuleCompleted(module.id, it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Green700,
                    uncheckedColor = Gray500,
                    checkmarkColor = Gray50
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) Green100 else Blue100),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Article,
                    contentDescription = null,
                    tint = if (isCompleted) Green700 else Blue700
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = module.title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCompleted) Charcoal else Gray900,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
