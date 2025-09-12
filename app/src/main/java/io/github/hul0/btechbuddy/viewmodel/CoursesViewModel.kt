package io.github.hul0.btechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.btechbuddy.data.model.Course
import io.github.hul0.btechbuddy.data.model.CourseCategory
import io.github.hul0.btechbuddy.data.model.CourseSubcategory
import io.github.hul0.btechbuddy.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.saket.unfurl.UnfurlResult
import me.saket.unfurl.Unfurler

data class CourseWithPreview(
    val course: Course,
    val preview: UnfurlResult? = null,
    val isLoading: Boolean = false
)

data class CoursesUiState(
    val selectedCategory: String? = null,
    val allCategories: List<CourseCategory> = emptyList(),
    val filteredCourses: List<CourseSubcategory> = emptyList(),
    val coursePreviews: Map<String, CourseWithPreview> = emptyMap(),
    // New: unique tags across all courses, used to render the grid as “categories”
    val distinctTags: List<String> = emptyList()
)

class CoursesViewModel(private val repository: ContentRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CoursesUiState())
    val uiState: StateFlow<CoursesUiState> = _uiState.asStateFlow()

    private val unfurler = Unfurler()

    private val allCourseCategories: List<CourseCategory> by lazy { repository.getCourses() }

    init {
        // Load all categories first
        _uiState.update { it.copy(allCategories = allCourseCategories) }

        // Build distinct tags once from all courses
        val tags = allCourseCategories
            .flatMap { it.subcategories }
            .flatMap { it.courses }
            .flatMap { it.tags }
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinct()
            .sorted()

        _uiState.update { it.copy(distinctTags = tags) }

        // React to selection changes
        viewModelScope.launch {
            uiState.map { it.selectedCategory }
                .distinctUntilChanged()
                .collect { category ->
                    filterCourses(category)
                }
        }
    }

    private fun filterCourses(categoryOrTag: String?) {
        if (categoryOrTag == null) {
            _uiState.update { it.copy(filteredCourses = emptyList()) }
            return
        }

        // Try curated category match first
        val category = allCourseCategories.find { it.category == categoryOrTag }

        val subcategories: List<CourseSubcategory> = if (category != null) {
            category.subcategories
        } else {
            // Treat selection as a tag: collect courses across all subcategories that contain the tag
            val allSubs = allCourseCategories.flatMap { it.subcategories }
            val filtered = allSubs.mapNotNull { sub ->
                val courses = sub.courses.filter { c ->
                    c.tags.any { t -> t.equals(categoryOrTag, ignoreCase = true) }
                }
                if (courses.isNotEmpty()) sub.copy(courses = courses) else null
            }
            filtered
        }

        _uiState.update { it.copy(filteredCourses = subcategories) }
        prefetchPreviews(subcategories.flatMap { it.courses })
    }

    private fun prefetchPreviews(courses: List<Course>) {
        courses.forEach { course ->
            if (!_uiState.value.coursePreviews.containsKey(course.id)) {
                viewModelScope.launch {
                    // Mark as loading
                    _uiState.update {
                        val newPreviews = it.coursePreviews + (course.id to CourseWithPreview(course, isLoading = true))
                        it.copy(coursePreviews = newPreviews)
                    }

                    val result = try {
                        unfurler.unfurl(course.url)
                    } catch (_: Exception) {
                        null
                    }

                    // Publish result
                    _uiState.update {
                        val finalPreview = CourseWithPreview(course, result, isLoading = false)
                        val newPreviews = it.coursePreviews + (course.id to finalPreview)
                        it.copy(coursePreviews = newPreviews)
                    }
                }
            }
        }
    }

    fun onCategorySelected(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    companion object {
        fun provideFactory(
            repository: ContentRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CoursesViewModel(repository) as T
            }
        }
    }
}
