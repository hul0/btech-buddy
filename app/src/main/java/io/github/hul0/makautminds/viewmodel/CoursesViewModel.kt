package io.github.hul0.makautminds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.makautminds.data.model.Course
import io.github.hul0.makautminds.data.model.CourseCategory
import io.github.hul0.makautminds.data.repository.ContentRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.saket.unfurl.UnfurlResult
import me.saket.unfurl.Unfurler

data class CourseWithPreview(
    val course: Course,
    val preview: UnfurlResult? = null,
    val isLoading: Boolean = false
)

data class CoursesUiState(
    val searchInput: String = "",
    val selectedTag: String? = null,
    val filteredCourses: List<CourseCategory> = emptyList(),
    val allTags: List<String> = emptyList(),
    val coursePreviews: Map<String, CourseWithPreview> = emptyMap()
)

class CoursesViewModel(private val repository: ContentRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CoursesUiState())
    val uiState: StateFlow<CoursesUiState> = _uiState.asStateFlow()

    private val unfurler = Unfurler()
    private val allCourseCategories: List<CourseCategory> by lazy { repository.getCourses() }

    init {
        // Initialize with all courses shown
        val allCourses = allCourseCategories
        _uiState.update { it.copy(filteredCourses = allCourses) }
        prefetchPreviews(allCourses)


        val allTags = allCourseCategories
            .flatMap { it.subcategories }
            .flatMap { it.courses }
            .flatMap { it.tags }
            .distinct()
            .sorted()
        _uiState.update { it.copy(allTags = allTags) }

        // Combine flows for search and filter
        viewModelScope.launch {
            combine(
                _uiState.map { it.searchInput }.distinctUntilChanged(),
                _uiState.map { it.selectedTag }.distinctUntilChanged()
            ) { search, tag ->
                filterCourses(search, tag)
            }.collect { filtered ->
                _uiState.update { it.copy(filteredCourses = filtered) }
                prefetchPreviews(filtered)
            }
        }
    }

    private fun filterCourses(search: String, tag: String?): List<CourseCategory> {
        if (search.isBlank() && tag == null) {
            return allCourseCategories
        }
        val searchLower = search.lowercase()
        return allCourseCategories.mapNotNull { category ->
            val filteredSubcategories = category.subcategories.mapNotNull { subcategory ->
                val courses = subcategory.courses.filter { course ->
                    val matchesSearch = if (search.isBlank()) true else {
                        course.title.lowercase().contains(searchLower) ||
                                course.description.lowercase().contains(searchLower)
                    }
                    val matchesTag = if (tag == null) true else course.tags.contains(tag)
                    matchesSearch && matchesTag
                }
                if (courses.isNotEmpty()) subcategory.copy(courses = courses) else null
            }
            if (filteredSubcategories.isNotEmpty()) category.copy(subcategories = filteredSubcategories) else null
        }
    }

    private fun prefetchPreviews(categories: List<CourseCategory>) {
        val allCourses = categories.flatMap { it.subcategories }.flatMap { it.courses }
        allCourses.forEach { course ->
            if (!_uiState.value.coursePreviews.containsKey(course.id)) {
                viewModelScope.launch {
                    // Set loading state
                    _uiState.update {
                        val newPreviews = it.coursePreviews + (course.id to CourseWithPreview(course, isLoading = true))
                        it.copy(coursePreviews = newPreviews)
                    }

                    // Fetch with error handling
                    val result = try {
                        unfurler.unfurl(course.url)
                    } catch (e: Exception) {
                        // On error, result is null
                        null
                    }

                    // Update with result or failure state
                    _uiState.update {
                        val finalPreview = CourseWithPreview(course, result, isLoading = false)
                        val newPreviews = it.coursePreviews + (course.id to finalPreview)
                        it.copy(coursePreviews = newPreviews)
                    }
                }
            }
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
            repository: ContentRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CoursesViewModel(repository) as T
            }
        }
    }
}

