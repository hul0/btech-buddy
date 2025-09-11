package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.hul0.btechbuddy.navigation.Screen
import io.github.hul0.btechbuddy.ui.theme.BtechBuddyTheme
import io.github.hul0.btechbuddy.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavController, viewModel: OnboardingViewModel) {
    var selectedBranch by remember { mutableStateOf("") }
    var selectedInterests by remember { mutableStateOf("") }
    val canProceed = selectedBranch.isNotBlank() && selectedInterests.isNotBlank()
    // 1. Get a coroutine scope that is tied to this composable's lifecycle
    val scope = rememberCoroutineScope()

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

            OnboardingSelection(
                label = "Select Your Branch",
                options = listOf("Computer Science", "IT", "Electronics", "Mechanical", "Civil", "Electrical"),
                selectedText = selectedBranch,
                onSelectionChanged = { selectedBranch = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OnboardingSelection(
                label = "Select Your Interests",
                options = listOf("Tech Roles", "Government Exams", "Management Roles", "Further Studies"),
                selectedText = selectedInterests,
                onSelectionChanged = { selectedInterests = it }
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    // 2. Launch the suspend function within the coroutine scope
                    scope.launch {
                        viewModel.saveUserPreferences(selectedBranch, selectedInterests)
                        // Navigation can happen outside the coroutine, but after the suspend function
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = canProceed
            ) {
                Text("Get Started")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingSelection(
    label: String,
    options: List<String>,
    selectedText: String,
    onSelectionChanged: (String) -> Unit
) {
    // 3. Corrected typo from mutableStateof to mutableStateOf
    var expanded by remember { mutableStateOf(false) }

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

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    BtechBuddyTheme {
        // This preview is visual only and won't have working logic, which is fine.
        // A dummy screen can be composed for preview if needed.
    }
}

