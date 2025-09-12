package io.github.hul0.btechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.btechbuddy.data.repository.UserPreferencesRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    fun saveUserPreferences(
        name: String,
        college: String,
        yearOfStudy: String,
        expectedGraduationYear: String,
        learningGoals: String,
        preferredLearningStyle: String,
        hoursPerWeek: String,
        dreamCompanies: String,
        branch: String,
        interests: String
    ) {
        viewModelScope.launch {
            userPreferencesRepository.updateUserPreferences(
                name,
                college,
                yearOfStudy,
                expectedGraduationYear,
                learningGoals,
                preferredLearningStyle,
                hoursPerWeek,
                dreamCompanies,
                branch,
                interests
            )
        }
    }

    companion object {
        fun provideFactory(
            repository: UserPreferencesRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OnboardingViewModel(repository) as T
            }
        }
    }
}
