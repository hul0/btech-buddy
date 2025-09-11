package io.github.hul0.makautminds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.makautminds.data.model.LearningPath
import io.github.hul0.makautminds.data.repository.ContentRepository
import io.github.hul0.makautminds.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LearningViewModel(
    private val contentRepository: ContentRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _learningPaths = MutableStateFlow<List<LearningPath>>(emptyList())
    val learningPaths: StateFlow<List<LearningPath>> = _learningPaths.asStateFlow()

    init {
        loadLearningPathsAndProgress()
    }

    private fun loadLearningPathsAndProgress() {
        viewModelScope.launch {
            val basePaths = contentRepository.getLearningPaths()
            val pathsWithProgress = basePaths.map { path ->
                val completedCount = path.modules.count { module ->
                    // .first() gets the current value from DataStore
                    userPreferencesRepository.isModuleCompleted(module.id).first()
                }
                path.copy(completedModules = completedCount)
            }
            _learningPaths.value = pathsWithProgress
        }
    }

    fun getLearningPathById(id: String): Flow<LearningPath?> {
        return learningPaths.map { paths ->
            paths.find { it.id == id }
        }
    }

    fun isModuleCompleted(moduleId: String): Flow<Boolean> {
        return userPreferencesRepository.isModuleCompleted(moduleId)
    }

    fun setModuleCompleted(moduleId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setModuleCompleted(moduleId, isCompleted)
            // After updating a module's status, just reload the progress for all paths
            loadLearningPathsAndProgress()
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

