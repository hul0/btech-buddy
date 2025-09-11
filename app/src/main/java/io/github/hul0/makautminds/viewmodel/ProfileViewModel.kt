package io.github.hul0.makautminds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.hul0.makautminds.data.repository.UserPreferences
import io.github.hul0.makautminds.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class ProfileViewModel(userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    val userPreferences: StateFlow<UserPreferences> = userPreferencesRepository.userPreferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences("", "")
        )

    companion object {
        fun provideFactory(
            repository: UserPreferencesRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(repository) as T
            }
        }
    }
}
