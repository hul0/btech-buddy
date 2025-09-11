package io.github.hul0.makautminds.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.hul0.makautminds.data.model.CareerRoadmap
import io.github.hul0.makautminds.data.model.CourseCategory
import io.github.hul0.makautminds.data.model.LearningPath
import java.io.IOException

class ContentRepository(private val context: Context) {

    private fun getJsonDataFromAsset(fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }

    fun getLearningPaths(): List<LearningPath> {
        val jsonString = getJsonDataFromAsset("learningPaths.json")
        val listType = object : TypeToken<List<LearningPath>>() {}.type
        return Gson().fromJson(jsonString, listType) ?: emptyList()
    }

    fun getCareerRoadmaps(): List<CareerRoadmap> {
        val jsonString = getJsonDataFromAsset("careerRoadmaps.json")
        val listType = object : TypeToken<List<CareerRoadmap>>() {}.type
        return Gson().fromJson(jsonString, listType) ?: emptyList()
    }

    fun getCourses(): List<CourseCategory> {
        val jsonString = getJsonDataFromAsset("courses.json")
        val listType = object : TypeToken<List<CourseCategory>>() {}.type
        return Gson().fromJson(jsonString, listType) ?: emptyList()
    }
}

