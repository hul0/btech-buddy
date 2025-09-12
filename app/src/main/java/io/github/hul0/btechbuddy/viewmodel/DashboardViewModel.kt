package io.github.hul0.btechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.btechbuddy.data.model.Roadmap
import io.github.hul0.btechbuddy.data.repository.ContentRepository
import io.github.hul0.btechbuddy.data.repository.UserPreferences
import io.github.hul0.btechbuddy.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val greeting: String = "Welcome!",
    val quote: Pair<String, String> = Pair(
        "The beautiful thing about learning is that nobody can take it away from you.",
        "B.B. King"
    ),
    val learningProgress: Float = 0f,
    val modulesCompleted: Int = 0,
    val totalModules: Int = 0,
    val recommendedRoadmap: Roadmap? = null
)

class DashboardViewModel(
    learningViewModel: LearningViewModel,
    userPreferencesRepository: UserPreferencesRepository,
    contentRepository: ContentRepository
) : ViewModel() {

    private val allRoadmaps = contentRepository.getCareerRoadmaps().flatMap { it.roadmaps }

    val uiState: StateFlow<DashboardUiState> = combine(
        learningViewModel.learningPaths,
        userPreferencesRepository.userPreferences
    ) { learningPaths, userPrefs ->
        // Calculate Learning Progress
        val totalModules = learningPaths.sumOf { it.modules.size }
        val completedModules = learningPaths.sumOf { it.completedModules }
        val progress = if (totalModules > 0) completedModules.toFloat() / totalModules else 0f

        // Create Personalized Greeting
        val greeting = if (userPrefs.name.isNotBlank()) {
            userPrefs.name.split(" ").first()
        } else if (userPrefs.branch.isNotBlank()) {
            "B.Tech Buddy"
        } else {
            "B.Tech Buddy"
        }

        // Find a Recommended Roadmap
        val recommendedRoadmap = findRecommendedRoadmap(userPrefs)

        DashboardUiState(
            greeting = greeting,
            learningProgress = progress,
            modulesCompleted = completedModules,
            totalModules = totalModules,
            recommendedRoadmap = recommendedRoadmap,
            // You can add logic here to fetch a random quote
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    private fun findRecommendedRoadmap(userPrefs: UserPreferences): Roadmap? {
        return when {
            userPrefs.interests.contains("Tech") -> allRoadmaps.find { it.title.contains("SDE") }
            userPrefs.interests.contains("Government") -> allRoadmaps.find { it.title.contains("UPSC") }
            userPrefs.interests.contains("Management") -> allRoadmaps.find { it.title.contains("Product Manager") }
            else -> allRoadmaps.firstOrNull()
        }
    }

    companion object {
        fun provideFactory(
            learningViewModel: LearningViewModel,
            userPreferencesRepository: UserPreferencesRepository,
            contentRepository: ContentRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DashboardViewModel(
                    learningViewModel,
                    userPreferencesRepository,
                    contentRepository
                ) as T
            }
        }
    }
}
