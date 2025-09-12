package io.github.hul0.btechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.btechbuddy.data.model.Course
import io.github.hul0.btechbuddy.data.model.CourseCategory
import io.github.hul0.btechbuddy.data.model.CourseSubcategory
import io.github.hul0.btechbuddy.data.repository.ContentRepository
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
    val selectedCategory: String? = null,
    val allCategories: List<CourseCategory> = emptyList(),
    val filteredCourses: List<CourseSubcategory> = emptyList(),
    val coursePreviews: Map<String, CourseWithPreview> = emptyMap()
)

class CoursesViewModel(private val repository: ContentRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CoursesUiState())
    val uiState: StateFlow<CoursesUiState> = _uiState.asStateFlow()

    private val unfurler = Unfurler()
    private val allCourseCategories: List<CourseCategory> by lazy { repository.getCourses() }

    init {
        _uiState.update { it.copy(allCategories = allCourseCategories) }

        viewModelScope.launch {
            _uiState.map { it.selectedCategory }.distinctUntilChanged().collect { category ->
                filterCourses(category)
            }
        }
    }

    private fun filterCourses(categoryName: String?) {
        if (categoryName == null) {
            _uiState.update { it.copy(filteredCourses = emptyList()) }
            return
        }

        val category = allCourseCategories.find { it.category == categoryName }
        val subcategories = category?.subcategories ?: emptyList()
        _uiState.update { it.copy(filteredCourses = subcategories) }
        prefetchPreviews(subcategories.flatMap { it.courses })
    }

    private fun prefetchPreviews(courses: List<Course>) {
        courses.forEach { course ->
            if (!_uiState.value.coursePreviews.containsKey(course.id)) {
                viewModelScope.launch {
                    _uiState.update {
                        val newPreviews = it.coursePreviews + (course.id to CourseWithPreview(course, isLoading = true))
                        it.copy(coursePreviews = newPreviews)
                    }

                    val result = try {
                        unfurler.unfurl(course.url)
                    } catch (e: Exception) {
                        null
                    }

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
