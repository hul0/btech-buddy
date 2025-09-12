package io.github.hul0.btechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.btechbuddy.data.model.LearningPath
import io.github.hul0.btechbuddy.data.repository.ContentRepository
import io.github.hul0.btechbuddy.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LearningUiState(
    val searchInput: String = "",
    val selectedTag: String? = null,
    val filteredLearningPaths: List<LearningPath> = emptyList(),
    val allTags: List<String> = emptyList()
)

class LearningViewModel(
    private val contentRepository: ContentRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LearningUiState())
    val uiState: StateFlow<LearningUiState> = _uiState.asStateFlow()

    private val baseLearningPaths = contentRepository.getLearningPaths()

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
            initialValue = baseLearningPaths
        )

    init {
        val allTags = baseLearningPaths.map { it.title.split(" ").first() }.distinct()
        _uiState.update { it.copy(allTags = allTags) }

        viewModelScope.launch {
            combine(
                learningPaths,
                _uiState.map { it.searchInput }.distinctUntilChanged(),
                _uiState.map { it.selectedTag }.distinctUntilChanged()
            ) { paths, search, tag ->
                filterLearningPaths(paths, search, tag)
            }.collect { filtered ->
                _uiState.update { it.copy(filteredLearningPaths = filtered) }
            }
        }
    }
    private fun filterLearningPaths(paths: List<LearningPath>, search: String, tag: String?): List<LearningPath> {
        val searchLower = search.lowercase()
        return paths.filter { path ->
            val matchesSearch = if (search.isBlank()) true else {
                path.title.lowercase().contains(searchLower) || path.description.lowercase().contains(searchLower)
            }
            val matchesTag = if (tag == null) true else path.title.contains(tag, ignoreCase = true)
            matchesSearch && matchesTag
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
        }
    }
    fun onSearchInputChanged(input: String) {
        _uiState.update { it.copy(searchInput = input) }
    }

    fun onTagSelected(tag: String) {
        _uiState.update {
            val newTag = if (it.selectedTag == tag) null else tag
            it.copy(selectedTag = newTag)
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
