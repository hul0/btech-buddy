package io.github.hul0.makautminds.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hul0.makautminds.ui.theme.MAKAUTMINDSTheme

@Composable
fun DashboardScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            QuoteOfTheDayCard()
        }
        item {
            DashboardCard(
                title = "Learning Progress",
                subtitle = "3/10 Modules Completed",
                icon = Icons.Default.TrendingUp
            )
        }
        item {
            DashboardCard(
                title = "Career Guidance",
                subtitle = "Explore Tech Roadmaps",
                icon = Icons.Default.AutoAwesome
            )
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
fun QuoteOfTheDayCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "\"The best way to predict the future is to create it.\"",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "- Abraham Lincoln",
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
    MAKAUTMINDSTheme {
        DashboardScreen()
    }
}
