package io.github.hul0.btechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.btechbuddy.data.model.CareerRoadmap
import io.github.hul0.btechbuddy.data.model.FaqCategory
import io.github.hul0.btechbuddy.data.repository.ContentRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class Tab(val title: String) {
    ROADMAPS("Roadmaps"),
    FAQS("FAQs")
}

data class GuidanceUiState(
    val selectedTab: Tab = Tab.ROADMAPS,
    val roadmaps: List<CareerRoadmap> = emptyList(),
    val faqs: List<FaqCategory> = emptyList()
)

class GuidanceViewModel(private val repository: ContentRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(GuidanceUiState())
    val uiState: StateFlow<GuidanceUiState> = _uiState.asStateFlow()

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch {
            val roadmaps = repository.getCareerRoadmaps()
            val faqs = repository.getFaqs()
            _uiState.update { it.copy(roadmaps = roadmaps, faqs = faqs) }
        }
    }

    fun selectTab(tab: Tab) {
        _uiState.update { it.copy(selectedTab = tab) }
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
