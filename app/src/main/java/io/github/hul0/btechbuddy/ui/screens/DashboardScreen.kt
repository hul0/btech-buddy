package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hul0.btechbuddy.ui.theme.BtechBuddyTheme
import io.github.hul0.btechbuddy.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    // Collect the UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Greeting card that uses the dynamic greeting text
            Text(
                text = uiState.greeting,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        item {
            QuoteOfTheDayCard(
                quote = uiState.quote.first,
                author = uiState.quote.second
            )
        }
        item {
            // Learning Progress card now shows real data
            DashboardCard(
                title = "Learning Progress",
                subtitle = uiState.learningProgressText,
                icon = Icons.Default.TrendingUp
            )
        }

        // The recommended roadmap card is shown conditionally
        uiState.recommendedRoadmap?.let { roadmap ->
            item {
                DashboardCard(
                    title = "Career Guidance",
                    subtitle = "Explore the ${roadmap.title} roadmap",
                    icon = Icons.Default.AutoAwesome
                )
            }
        }

        item {
            DashboardCard(
                title = "Daily Challenge",
                subtitle = "Solve today's aptitude question",
                icon = Icons.Default.Lightbulb
            )
        }
    }
}

@Composable
fun QuoteOfTheDayCard(quote: String, author: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "\"$quote\"",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "- $author",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun DashboardCard(title: String, subtitle: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    BtechBuddyTheme {
        // Preview can't use a real ViewModel, so it will show default state.
        // DashboardScreen(viewModel = ...)
    }
}
