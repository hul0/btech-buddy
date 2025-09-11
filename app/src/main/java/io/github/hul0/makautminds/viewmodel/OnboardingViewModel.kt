package io.github.hul0.makautminds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.makautminds.data.repository.UserPreferencesRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    fun saveUserPreferences(branch: String, interests: String) {
        viewModelScope.launch {
            // Corrected method name from saveUserPreferences to updateUserPreferences
            userPreferencesRepository.updateUserPreferences(branch, interests)
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

