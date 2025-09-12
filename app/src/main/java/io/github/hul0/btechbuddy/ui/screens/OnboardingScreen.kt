package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Interests
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.hul0.btechbuddy.navigation.Screen
import io.github.hul0.btechbuddy.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch
import io.github.hul0.btechbuddy.ui.theme.* // Color.kt palette

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Header
        Spacer(modifier = Modifier.size(30.dp))
        Text(
            text = "Welcome to Your Learning & Career Companion",
            style = MaterialTheme.typography.titleMedium,
            color = Gray900,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Let's personalize your journey. Please fill in your details.",
            style = MaterialTheme.typography.bodySmall,
            color = Gray700,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Divider(color = Gray200, thickness = 1.dp)
        Spacer(Modifier.height(12.dp))

        // Content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Identity
            item { SectionLabel("Identity", Icons.Filled.Badge, Blue700) }
            item {
                CompactOutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full Name",
                    leadingIcon = Icons.Filled.Person
                )
            }
            item {
                CompactOutlinedTextField(
                    value = college,
                    onValueChange = { college = it },
                    label = "College Name",
                    leadingIcon = Icons.Filled.School
                )
            }

            // Academics
            item { SectionLabel("Academics", Icons.Filled.Apartment, Indigo700) }
            item {
                OnboardingSelection(
                    label = "Select Your Branch",
                    options = listOf("Computer Science", "IT", "Electronics", "Mechanical", "Civil", "Electrical"),
                    selectedText = selectedBranch,
                    onSelectionChanged = { selectedBranch = it }
                )
            }
            item {
                CompactOutlinedTextField(
                    value = yearOfStudy,
                    onValueChange = { yearOfStudy = it },
                    label = "Current Year of Study",
                    leadingIcon = Icons.Filled.CalendarMonth,
                    keyboardType = KeyboardType.Number
                )
            }
            item {
                CompactOutlinedTextField(
                    value = expectedGraduationYear,
                    onValueChange = { expectedGraduationYear = it },
                    label = "Expected Graduation Year",
                    leadingIcon = Icons.Filled.CalendarMonth,
                    keyboardType = KeyboardType.Number
                )
            }

            // Interests & goals
            item { SectionLabel("Interests & Goals", Icons.Filled.Interests, Purple700) }
            item {
                OnboardingSelection(
                    label = "Select Your Interests",
                    options = listOf("Tech Roles", "Government Exams", "Management Roles", "Further Studies"),
                    selectedText = selectedInterests,
                    onSelectionChanged = { selectedInterests = it }
                )
            }
            item {
                CompactOutlinedTextField(
                    value = learningGoals,
                    onValueChange = { learningGoals = it },
                    label = "Learning Goals (e.g., Learn DSA, Web Dev)",
                    leadingIcon = Icons.Filled.Flag
                )
            }

            // Time & companies
            item { SectionLabel("Plan", Icons.Filled.Timer, Green700) }
            item {
                CompactOutlinedTextField(
                    value = hoursPerWeek,
                    onValueChange = { hoursPerWeek = it },
                    label = "Hours per week for learning",
                    leadingIcon = Icons.Filled.Timer,
                    keyboardType = KeyboardType.Number
                )
            }
            item {
                CompactOutlinedTextField(
                    value = dreamCompanies,
                    onValueChange = { dreamCompanies = it },
                    label = "Dream Companies (comma-separated)",
                    leadingIcon = Icons.Filled.CheckCircle
                )
            }

            item { Spacer(Modifier.height(8.dp)) }
            item { Divider(color = Gray200, thickness = 1.dp) }
            item {
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
                    enabled = canProceed,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue700,
                        contentColor = Gray50,
                        disabledContainerColor = Gray300,
                        disabledContentColor = Gray600
                    )
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Get Started")
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tint: androidx.compose.ui.graphics.Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = Gray900
        )
    }
}

@Composable
private fun CompactOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = Blue700) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Blue700,
            unfocusedBorderColor = Gray300,
            focusedLabelColor = Blue700,
            cursorColor = Blue700,
            focusedTextColor = Gray900,
            unfocusedTextColor = Gray900,
            focusedContainerColor = Gray50,
            unfocusedContainerColor = Gray50,
            focusedPlaceholderColor = Gray500,
            unfocusedPlaceholderColor = Gray500
        )
    )
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
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue700,
                unfocusedBorderColor = Gray300,
                focusedLabelColor = Blue700,
                cursorColor = Blue700,
                focusedTextColor = Gray900,
                unfocusedTextColor = Gray900,
                focusedContainerColor = Gray50,
                unfocusedContainerColor = Gray50
            )
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
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = Gray900
                    )
                )
            }
        }
    }
}
