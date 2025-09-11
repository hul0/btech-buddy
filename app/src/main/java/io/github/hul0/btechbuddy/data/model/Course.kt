package io.github.hul0.btechbuddy.data.model

data class CourseCategory(
    val category: String,
    val subcategories: List<CourseSubcategory>
)

data class CourseSubcategory(
    val subcategory: String,
    val courses: List<Course>
)

data class Course(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val difficulty: String,
    val tags: List<String>
)

