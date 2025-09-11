package io.github.hul0.makautminds.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.hul0.makautminds.data.repository.UserPreferencesRepository
import io.github.hul0.makautminds.navigation.Screen
import io.github.hul0.makautminds.ui.theme.MAKAUTMINDSTheme
import io.github.hul0.makautminds.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    navController: NavController,
    userPreferencesRepository: UserPreferencesRepository
) {
    val viewModel: OnboardingViewModel = viewModel(
        factory = OnboardingViewModel.provideFactory(userPreferencesRepository)
    )
    val coroutineScope = rememberCoroutineScope()
    var selectedBranch by remember { mutableStateOf("") }
    var selectedInterests by remember { mutableStateOf("") }


    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Your Learning & Career Companion",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Let's personalize your journey. Select your branch and interests.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            OnboardingSelection("Select Your Branch", selectedBranch) {
                selectedBranch = it
            }

            Spacer(modifier = Modifier.height(16.dp))

            OnboardingSelection("Select Your Interests", selectedInterests) {
                selectedInterests = it
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveUserPreferences(selectedBranch, selectedInterests)
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedBranch.isNotBlank() && selectedInterests.isNotBlank()
            ) {
                Text("Get Started")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingSelection(label: String, selectedText: String, onSelectionChanged: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = when (label) {
        "Select Your Branch" -> listOf("Computer Science", "Mechanical", "Civil", "Electronics", "Other")
        else -> listOf("Development", "Data Science", "Cybersecurity", "Core Engineering", "Government Exams")
    }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelectionChanged(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
