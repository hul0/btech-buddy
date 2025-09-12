package io.github.hul0.btechbuddy.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.hul0.btechbuddy.data.model.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(context: Context) {
    private val dataStore = context.dataStore

    private data class UserPreferencesKeys(
        val NAME_KEY: Preferences.Key<String> = stringPreferencesKey("name"),
        val COLLEGE_KEY: Preferences.Key<String> = stringPreferencesKey("college"),
        val YEAR_OF_STUDY_KEY: Preferences.Key<String> = stringPreferencesKey("year_of_study"),
        val GRADUATION_YEAR_KEY: Preferences.Key<String> = stringPreferencesKey("graduation_year"),
        val LEARNING_GOALS_KEY: Preferences.Key<String> = stringPreferencesKey("learning_goals"),
        val LEARNING_STYLE_KEY: Preferences.Key<String> = stringPreferencesKey("learning_style"),
        val HOURS_PER_WEEK_KEY: Preferences.Key<String> = stringPreferencesKey("hours_per_week"),
        val DREAM_COMPANIES_KEY: Preferences.Key<String> = stringPreferencesKey("dream_companies"),
        val BRANCH_KEY: Preferences.Key<String> = stringPreferencesKey("branch"),
        val INTERESTS_KEY: Preferences.Key<String> = stringPreferencesKey("interests"),
        val COMPLETED_MODULES_KEY: Preferences.Key<Set<String>> = stringSetPreferencesKey("completed_modules"),
        val TODO_LIST_KEY: Preferences.Key<Set<String>> = stringSetPreferencesKey("todo_list")
    )

    private val keys = UserPreferencesKeys()

    val userPreferences: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            name = preferences[keys.NAME_KEY] ?: "",
            college = preferences[keys.COLLEGE_KEY] ?: "",
            yearOfStudy = preferences[keys.YEAR_OF_STUDY_KEY] ?: "",
            expectedGraduationYear = preferences[keys.GRADUATION_YEAR_KEY] ?: "",
            learningGoals = preferences[keys.LEARNING_GOALS_KEY] ?: "",
            preferredLearningStyle = preferences[keys.LEARNING_STYLE_KEY] ?: "",
            hoursPerWeek = preferences[keys.HOURS_PER_WEEK_KEY] ?: "",
            dreamCompanies = preferences[keys.DREAM_COMPANIES_KEY] ?: "",
            branch = preferences[keys.BRANCH_KEY] ?: "",
            interests = preferences[keys.INTERESTS_KEY] ?: ""
        )
    }

    val todos: Flow<List<TodoItem>> = dataStore.data.map { preferences ->
        preferences[keys.TODO_LIST_KEY]?.mapNotNull {
            try {
                Json.decodeFromString<TodoItem>(it)
            } catch (e: Exception) {
                null
            }
        }?.sortedBy { it.isCompleted } ?: emptyList()
    }

    val completedModuleIds: Flow<Set<String>> = dataStore.data
        .map { preferences ->
            preferences[keys.COMPLETED_MODULES_KEY] ?: emptySet()
        }

    suspend fun updateUserPreferences(
        name: String,
        college: String,
        yearOfStudy: String,
        expectedGraduationYear: String,
        learningGoals: String,
        preferredLearningStyle: String,
        hoursPerWeek: String,
        dreamCompanies: String,
        branch: String,
        interests: String
    ) {
        dataStore.edit { preferences ->
            preferences[keys.NAME_KEY] = name
            preferences[keys.COLLEGE_KEY] = college
            preferences[keys.YEAR_OF_STUDY_KEY] = yearOfStudy
            preferences[keys.GRADUATION_YEAR_KEY] = expectedGraduationYear
            preferences[keys.LEARNING_GOALS_KEY] = learningGoals
            preferences[keys.LEARNING_STYLE_KEY] = preferredLearningStyle
            preferences[keys.HOURS_PER_WEEK_KEY] = hoursPerWeek
            preferences[keys.DREAM_COMPANIES_KEY] = dreamCompanies
            preferences[keys.BRANCH_KEY] = branch
            preferences[keys.INTERESTS_KEY] = interests
        }
    }

    suspend fun saveTodos(todos: List<TodoItem>) {
        dataStore.edit { preferences ->
            val jsonSet = todos.map { Json.encodeToString(it) }.toSet()
            preferences[keys.TODO_LIST_KEY] = jsonSet
        }
    }

    fun isModuleCompleted(moduleId: String): Flow<Boolean> {
        return dataStore.data
            .map { preferences ->
                preferences[keys.COMPLETED_MODULES_KEY]?.contains(moduleId) ?: false
            }
    }

    suspend fun setModuleCompleted(moduleId: String, isCompleted: Boolean) {
        dataStore.edit { preferences ->
            val currentCompleted = preferences[keys.COMPLETED_MODULES_KEY]?.toMutableSet() ?: mutableSetOf()
            if (isCompleted) {
                currentCompleted.add(moduleId)
            } else {
                currentCompleted.remove(moduleId)
            }
            preferences[keys.COMPLETED_MODULES_KEY] = currentCompleted
        }
    }
}

data class UserPreferences(
    val name: String,
    val college: String,
    val yearOfStudy: String,
    val expectedGraduationYear: String,
    val learningGoals: String,
    val preferredLearningStyle: String,
    val hoursPerWeek: String,
    val dreamCompanies: String,
    val branch: String,
    val interests: String
)


