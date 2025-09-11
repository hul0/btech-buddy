package io.github.hul0.makautminds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.hul0.makautminds.data.repository.UserPreferencesRepository

class OnboardingViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    suspend fun saveUserPreferences(branch: String, interests: String) {
        userPreferencesRepository.updateUserPreferences(branch, interests)
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
