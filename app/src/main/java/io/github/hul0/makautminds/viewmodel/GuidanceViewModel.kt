package io.github.hul0.makautminds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.makautminds.data.model.CareerRoadmap
import io.github.hul0.makautminds.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GuidanceViewModel(private val repository: ContentRepository) : ViewModel() {

    private val _careerRoadmaps = MutableStateFlow<List<CareerRoadmap>>(emptyList())
    val careerRoadmaps: StateFlow<List<CareerRoadmap>> = _careerRoadmaps

    init {
        loadCareerRoadmaps()
    }

    private fun loadCareerRoadmaps() {
        viewModelScope.launch {
            _careerRoadmaps.value = repository.getCareerRoadmaps()
        }
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
