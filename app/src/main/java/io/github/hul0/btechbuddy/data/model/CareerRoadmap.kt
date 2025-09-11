package io.github.hul0.btechbuddy.data.model

data class CareerRoadmap(
    val category: String,
    val roadmaps: List<Roadmap>
)

data class Roadmap(
    val id: String,
    val title: String,
    val description: String,
    val steps: List<String>
)
