package io.github.hul0.makautminds.data.repository

import android.content.Context
import io.github.hul0.makautminds.data.model.CareerRoadmap
import io.github.hul0.makautminds.data.model.LearningPath
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
        return Gson().fromJson(jsonString, listType)
    }

    fun getCareerRoadmaps(): List<CareerRoadmap> {
        val jsonString = getJsonDataFromAsset("careerRoadmaps.json")
        val listType = object : TypeToken<List<CareerRoadmap>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}
