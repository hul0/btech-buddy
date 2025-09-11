package io.github.hul0.makautminds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.makautminds.data.model.LearningPath
import io.github.hul0.makautminds.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LearningViewModel(private val repository: ContentRepository) : ViewModel() {

    private val _learningPaths = MutableStateFlow<List<LearningPath>>(emptyList())
    val learningPaths: StateFlow<List<LearningPath>> = _learningPaths

    init {
        loadLearningPaths()
    }

    private fun loadLearningPaths() {
        viewModelScope.launch {
            _learningPaths.value = repository.getLearningPaths()
        }
    }

    companion object {
        fun provideFactory(
            repository: ContentRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LearningViewModel(repository) as T
            }
        }
    }
}
