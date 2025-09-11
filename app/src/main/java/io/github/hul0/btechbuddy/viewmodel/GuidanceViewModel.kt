package io.github.hul0.btechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.btechbuddy.data.model.CareerRoadmap
import io.github.hul0.btechbuddy.data.repository.ContentRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GuidanceViewModel(private val repository: ContentRepository) : ViewModel() {

    private val _careerRoadmaps = MutableStateFlow<List<CareerRoadmap>>(emptyList())
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    val filteredCareerRoadmaps: StateFlow<List<CareerRoadmap>> =
        combine(_careerRoadmaps, _searchText) { roadmaps, text ->
            if (text.isBlank()) {
                roadmaps
            } else {
                roadmaps.mapNotNull { category ->
                    val filteredRoadmaps = category.roadmaps.filter {
                        it.title.contains(text, ignoreCase = true)
                    }
                    if (filteredRoadmaps.isNotEmpty()) {
                        category.copy(roadmaps = filteredRoadmaps)
                    } else {
                        null
                    }
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    init {
        loadCareerRoadmaps()
    }

    private fun loadCareerRoadmaps() {
        viewModelScope.launch {
            _careerRoadmaps.value = repository.getCareerRoadmaps()
        }
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
    }

    companion object {
        fun provideFactory(
            repository: ContentRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GuidanceViewModel(repository) as T
            }
        }
    }
}
