package io.github.hul0.btechbuddy.data.model

data class LearningPath(
    val id: String,
    val title: String,
    val description: String,
    val modules: List<Module>,
    var completedModules: Int
)

data class Module(
    val id: String,
    val title: String,
    val resources: List<String>,
    val youtubeUrl: String? = null
)
