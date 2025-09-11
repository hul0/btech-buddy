package io.github.hul0.btechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.btechbuddy.data.model.LearningPath
import io.github.hul0.btechbuddy.data.repository.ContentRepository
import io.github.hul0.btechbuddy.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LearningViewModel(
    private val contentRepository: ContentRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // Get the static list of paths just once.
    private val baseLearningPaths = contentRepository.getLearningPaths()

    // This is the reactive flow. It combines the static path data with the dynamic
    // set of completed module IDs from DataStore. Whenever the set of completed IDs changes,
    // it re-calculates the progress for all paths and emits a new list to the UI.
    val learningPaths: StateFlow<List<LearningPath>> = userPreferencesRepository.completedModuleIds
        .map { completedIds ->
            baseLearningPaths.map { path ->
                val completedCount = path.modules.count { module -> module.id in completedIds }
                path.copy(completedModules = completedCount)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = baseLearningPaths // Show the initial list immediately
        )

    fun getLearningPathById(id: String): Flow<LearningPath?> {
        // Find the specific path from the reactive flow
        return learningPaths.map { paths ->
            paths.find { it.id == id }
        }
    }

    fun isModuleCompleted(moduleId: String): Flow<Boolean> {
        return userPreferencesRepository.isModuleCompleted(moduleId)
    }

    // This function is now much simpler. It just tells the repository to update the data.
    // The reactive flow in 'learningPaths' will automatically handle the UI update.
    fun setModuleCompleted(moduleId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setModuleCompleted(moduleId, isCompleted)
        }
    }

    companion object {
        fun provideFactory(
            contentRepository: ContentRepository,
            userPreferencesRepository: UserPreferencesRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LearningViewModel(contentRepository, userPreferencesRepository) as T
            }
        }
    }
}

