package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.hul0.btechbuddy.data.model.Module
import io.github.hul0.btechbuddy.viewmodel.LearningViewModel

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
                title = { Text(learningPath?.title ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                learningPath?.let {
                    Text(it.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it.description, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
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

@Composable
fun ModuleItem(
    module: Module,
    viewModel: LearningViewModel
) {
    val isCompleted by viewModel.isModuleCompleted(module.id).collectAsState(initial = false)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isCompleted,
            onCheckedChange = {
                viewModel.setModuleCompleted(module.id, it)
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = module.title, style = MaterialTheme.typography.bodyLarge)
    }
}
