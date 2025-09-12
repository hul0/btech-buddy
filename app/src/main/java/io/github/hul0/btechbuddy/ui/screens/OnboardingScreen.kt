package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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
    var name by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("") }
    var yearOfStudy by remember { mutableStateOf("") }
    var expectedGraduationYear by remember { mutableStateOf("") }
    var learningGoals by remember { mutableStateOf("") }
    var preferredLearningStyle by remember { mutableStateOf("") }
    var hoursPerWeek by remember { mutableStateOf("") }
    var dreamCompanies by remember { mutableStateOf("") }
    var selectedBranch by remember { mutableStateOf("") }
    var selectedInterests by remember { mutableStateOf("") }
    val canProceed = selectedBranch.isNotBlank() && selectedInterests.isNotBlank() && name.isNotBlank() && college.isNotBlank()
    val scope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Welcome to Your Learning & Career Companion",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Let's personalize your journey. Please fill in your details.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = college,
                    onValueChange = { college = it },
                    label = { Text("College Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OnboardingSelection(
                    label = "Select Your Branch",
                    options = listOf("Computer Science", "IT", "Electronics", "Mechanical", "Civil", "Electrical"),
                    selectedText = selectedBranch,
                    onSelectionChanged = { selectedBranch = it }
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = yearOfStudy,
                    onValueChange = { yearOfStudy = it },
                    label = { Text("Current Year of Study") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = expectedGraduationYear,
                    onValueChange = { expectedGraduationYear = it },
                    label = { Text("Expected Graduation Year") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OnboardingSelection(
                    label = "Select Your Interests",
                    options = listOf("Tech Roles", "Government Exams", "Management Roles", "Further Studies"),
                    selectedText = selectedInterests,
                    onSelectionChanged = { selectedInterests = it }
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = learningGoals,
                    onValueChange = { learningGoals = it },
                    label = { Text("Learning Goals (e.g., Learn DSA, Web Dev)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OnboardingSelection(
                    label = "Preferred Learning Style",
                    options = listOf("Visual (Videos)", "Practical (Projects)", "Reading (Articles)", "Balanced"),
                    selectedText = preferredLearningStyle,
                    onSelectionChanged = { preferredLearningStyle = it }
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = hoursPerWeek,
                    onValueChange = { hoursPerWeek = it },
                    label = { Text("Hours per week for learning") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = dreamCompanies,
                    onValueChange = { dreamCompanies = it },
                    label = { Text("Dream Companies (comma-separated)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = {
                        scope.launch {
                            viewModel.saveUserPreferences(
                                name,
                                college,
                                yearOfStudy,
                                expectedGraduationYear,
                                learningGoals,
                                preferredLearningStyle,
                                hoursPerWeek,
                                dreamCompanies,
                                selectedBranch,
                                selectedInterests
                            )
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
                Spacer(modifier = Modifier.height(16.dp))
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
