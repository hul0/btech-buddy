package io.github.hul0.makautminds.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.hul0.makautminds.data.model.LearningPath
import io.github.hul0.makautminds.viewmodel.LearningViewModel

@Composable
fun LearningScreen(viewModel: LearningViewModel) {
    val learningPaths by viewModel.learningPaths.collectAsState()

    if (learningPaths.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(learningPaths) { path ->
                LearningPathCard(path = path, onClick = { /* TODO: Navigate to detail screen */ })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningPathCard(path: LearningPath, onClick: () -> Unit) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    progress = path.completedModules.toFloat() / path.modules.size,
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
