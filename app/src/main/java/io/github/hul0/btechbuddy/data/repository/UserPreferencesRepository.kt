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

    private object PrefKeys {
        val NAME_KEY = stringPreferencesKey("name")
        val COLLEGE_KEY = stringPreferencesKey("college")
        val YEAR_OF_STUDY_KEY = stringPreferencesKey("year_of_study")
        val GRADUATION_YEAR_KEY = stringPreferencesKey("graduation_year")
        val LEARNING_GOALS_KEY = stringPreferencesKey("learning_goals")
        val LEARNING_STYLE_KEY = stringPreferencesKey("learning_style")
        val HOURS_PER_WEEK_KEY = stringPreferencesKey("hours_per_week")
        val DREAM_COMPANIES_KEY = stringPreferencesKey("dream_companies")
        val BRANCH_KEY = stringPreferencesKey("branch")
        val INTERESTS_KEY = stringPreferencesKey("interests")
        val COMPLETED_MODULES_KEY = stringSetPreferencesKey("completed_modules")
        val TODO_LIST_KEY = stringSetPreferencesKey("todo_list")

        // Keys for Authentication Tokens
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    val userPreferences: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            name = preferences[PrefKeys.NAME_KEY] ?: "",
            college = preferences[PrefKeys.COLLEGE_KEY] ?: "",
            yearOfStudy = preferences[PrefKeys.YEAR_OF_STUDY_KEY] ?: "",
            expectedGraduationYear = preferences[PrefKeys.GRADUATION_YEAR_KEY] ?: "",
            learningGoals = preferences[PrefKeys.LEARNING_GOALS_KEY] ?: "",
            preferredLearningStyle = preferences[PrefKeys.LEARNING_STYLE_KEY] ?: "",
            hoursPerWeek = preferences[PrefKeys.HOURS_PER_WEEK_KEY] ?: "",
            dreamCompanies = preferences[PrefKeys.DREAM_COMPANIES_KEY] ?: "",
            branch = preferences[PrefKeys.BRANCH_KEY] ?: "",
            interests = preferences[PrefKeys.INTERESTS_KEY] ?: ""
        )
    }

    // Flow to observe the access token
    val accessToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PrefKeys.ACCESS_TOKEN_KEY]
    }

    val todos: Flow<List<TodoItem>> = dataStore.data.map { preferences ->
        preferences[PrefKeys.TODO_LIST_KEY]?.mapNotNull {
            try {
                Json.decodeFromString<TodoItem>(it)
            } catch (e: Exception) {
                null
            }
        }?.sortedBy { it.isCompleted } ?: emptyList()
    }

    val completedModuleIds: Flow<Set<String>> = dataStore.data
        .map { preferences ->
            preferences[PrefKeys.COMPLETED_MODULES_KEY] ?: emptySet()
        }

    // Save tokens to DataStore
    suspend fun saveAuthTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[PrefKeys.ACCESS_TOKEN_KEY] = accessToken
            preferences[PrefKeys.REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    // Clear tokens on logout
    suspend fun clearAuthTokens() {
        dataStore.edit { preferences ->
            preferences.remove(PrefKeys.ACCESS_TOKEN_KEY)
            preferences.remove(PrefKeys.REFRESH_TOKEN_KEY)
        }
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
            preferences[PrefKeys.NAME_KEY] = name
            preferences[PrefKeys.COLLEGE_KEY] = college
            preferences[PrefKeys.YEAR_OF_STUDY_KEY] = yearOfStudy
            preferences[PrefKeys.GRADUATION_YEAR_KEY] = expectedGraduationYear
            preferences[PrefKeys.LEARNING_GOALS_KEY] = learningGoals
            preferences[PrefKeys.LEARNING_STYLE_KEY] = preferredLearningStyle
            preferences[PrefKeys.HOURS_PER_WEEK_KEY] = hoursPerWeek
            preferences[PrefKeys.DREAM_COMPANIES_KEY] = dreamCompanies
            preferences[PrefKeys.BRANCH_KEY] = branch
            preferences[PrefKeys.INTERESTS_KEY] = interests
        }
    }

    suspend fun saveTodos(todos: List<TodoItem>) {
        dataStore.edit { preferences ->
            val jsonSet = todos.map { Json.encodeToString(it) }.toSet()
            preferences[PrefKeys.TODO_LIST_KEY] = jsonSet
        }
    }

    fun isModuleCompleted(moduleId: String): Flow<Boolean> {
        return dataStore.data
            .map { preferences ->
                preferences[PrefKeys.COMPLETED_MODULES_KEY]?.contains(moduleId) ?: false
            }
    }

    suspend fun setModuleCompleted(moduleId: String, isCompleted: Boolean) {
        dataStore.edit { preferences ->
            val currentCompleted = preferences[PrefKeys.COMPLETED_MODULES_KEY]?.toMutableSet() ?: mutableSetOf()
            if (isCompleted) {
                currentCompleted.add(moduleId)
            } else {
                currentCompleted.remove(moduleId)
            }
            preferences[PrefKeys.COMPLETED_MODULES_KEY] = currentCompleted
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
