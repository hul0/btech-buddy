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

// Data class to hold all the dynamic information for the dashboard UI
data class DashboardUiState(
    val greeting: String = "Welcome!",
    val learningProgressText: String = "Start your first module!",
    val recommendedRoadmap: Roadmap? = null,
    val quote: Pair<String, String> = Pair(
        "The best way to predict the future is to create it.",
        "Abraham Lincoln"
    )
)

class DashboardViewModel(
    learningViewModel: LearningViewModel, // We'll get progress from here
    userPreferencesRepository: UserPreferencesRepository, // For user's branch/interests
    contentRepository: ContentRepository // To get the roadmap details
) : ViewModel() {

    private val allRoadmaps = contentRepository.getCareerRoadmaps().flatMap { it.roadmaps }

    // This is the core logic. It combines multiple flows into one UI state.
    // Whenever learning progress OR user preferences change, this will re-evaluate.
    val uiState: StateFlow<DashboardUiState> = combine(
        learningViewModel.learningPaths,
        userPreferencesRepository.userPreferences
    ) { learningPaths, userPrefs ->
        // 1. Calculate Learning Progress
        val totalModules = learningPaths.sumOf { it.modules.size }
        val completedModules = learningPaths.sumOf { it.completedModules }
        val progressText = if (totalModules > 0) {
            "$completedModules / $totalModules Modules Completed"
        } else {
            "No modules available."
        }

        // 2. Create Personalized Greeting
        val greeting = if (userPrefs.branch.isNotBlank()) {
            "Welcome, ${userPrefs.branch} Engineer!"
        } else {
            "Welcome to B.Tech Buddy !"
        }

        // 3. Find a Recommended Roadmap
        val recommendedRoadmap = findRecommendedRoadmap(userPrefs)

        // 4. Return the complete state object
        DashboardUiState(
            greeting = greeting,
            learningProgressText = progressText,
            recommendedRoadmap = recommendedRoadmap
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState() // Start with a default state
    )

    private fun findRecommendedRoadmap(userPrefs: UserPreferences): Roadmap? {
        // Simple logic to match user interest to a roadmap title
        return when {
            userPrefs.interests.contains("Tech") -> allRoadmaps.find { it.title.contains("SDE") }
            userPrefs.interests.contains("Government") -> allRoadmaps.find { it.title.contains("UPSC") }
            userPrefs.interests.contains("Management") -> allRoadmaps.find { it.title.contains("Product Manager") }
            else -> allRoadmaps.firstOrNull() // Default recommendation
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
