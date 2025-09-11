package io.github.hul0.makautminds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.makautminds.data.model.Course
import io.github.hul0.makautminds.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.saket.unfurl.UnfurlResult
import me.saket.unfurl.Unfurler

// Data class to hold both the course info and its fetched preview
data class CourseWithPreview(
    val course: Course,
    val preview: UnfurlResult? = null,
    val isLoading: Boolean = true
)

class CoursesViewModel(private val repository: ContentRepository) : ViewModel() {

    private val _courses = MutableStateFlow<List<CourseWithPreview>>(emptyList())
    val courses: StateFlow<List<CourseWithPreview>> = _courses.asStateFlow()

    private val unfurler = Unfurler()

    init {
        loadCoursesAndFetchPreviews()
    }

    private fun loadCoursesAndFetchPreviews() {
        viewModelScope.launch {
            // 1. Load the initial list of courses from JSON
            val initialCourses = repository.getCourses().map { CourseWithPreview(course = it) }
            _courses.value = initialCourses

            // 2. Iterate and fetch the preview for each course individually
            initialCourses.forEachIndexed { index, courseItem ->
                launch {
                    val result = unfurler.unfurl(courseItem.course.url)
                    // 3. Update the state with the fetched preview data
                    _courses.update { currentList ->
                        currentList.toMutableList().also {
                            it[index] = it[index].copy(preview = result, isLoading = false)
                        }
                    }
                }
            }
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

