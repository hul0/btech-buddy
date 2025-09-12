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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.hul0.btechbuddy.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val userPreferences by viewModel.userPreferences.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "My Profile",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp)) // Reduced spacer
        }

        item {
            ProfileInfoCard(
                title = "Name",
                value = userPreferences.name,
                icon = Icons.Default.AccountCircle
            )
        }
        item {
            ProfileInfoCard(
                title = "College",
                value = userPreferences.college,
                icon = Icons.Default.Apartment
            )
        }
        item {
            ProfileInfoCard(
                title = "My Branch",
                value = userPreferences.branch,
                icon = Icons.Default.Star
            )
        }
        item {
            ProfileInfoCard(
                title = "Year of Study",
                value = userPreferences.yearOfStudy,
                icon = Icons.Default.School
            )
        }
        item {
            ProfileInfoCard(
                title = "Graduating In",
                value = userPreferences.expectedGraduationYear,
                icon = Icons.Default.Event
            )
        }
        item {
            ProfileInfoCard(
                title = "My Interests",
                value = userPreferences.interests,
                icon = Icons.Default.Interests
            )
        }
        item {
            ProfileInfoCard(
                title = "Learning Goals",
                value = userPreferences.learningGoals,
                icon = Icons.Default.Flag
            )
        }
        item {
            ProfileInfoCard(
                title = "Preferred Learning Style",
                value = userPreferences.preferredLearningStyle,
                icon = Icons.Default.Palette
            )
        }
        item {
            ProfileInfoCard(
                title = "Weekly Learning Hours",
                value = userPreferences.hoursPerWeek,
                icon = Icons.Default.AccessTime
            )
        }
        item {
            ProfileInfoCard(
                title = "Dream Companies",
                value = userPreferences.dreamCompanies,
                icon = Icons.Default.Business
            )
        }
    }
}

@Composable
fun ProfileInfoCard(title: String, value: String, icon: ImageVector) {
    if (value.isNotBlank()) {
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
                    Text(text = title, style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

