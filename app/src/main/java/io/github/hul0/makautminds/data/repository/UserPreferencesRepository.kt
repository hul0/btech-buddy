package io.github.hul0.makautminds.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val userBranch = stringPreferencesKey("user_branch")
        val userInterests = stringPreferencesKey("user_interests")
        fun moduleCompletionKey(moduleId: String) = booleanPreferencesKey("module_completed_$moduleId")
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data
        .map { preferences ->
            UserPreferences(
                branch = preferences[Keys.userBranch] ?: "",
                interests = preferences[Keys.userInterests] ?: ""
            )
        }

    suspend fun saveUserPreferences(branch: String, interests: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.userBranch] = branch
            preferences[Keys.userInterests] = interests
        }
    }

    fun isModuleCompleted(moduleId: String): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[Keys.moduleCompletionKey(moduleId)] ?: false
        }
    }

    suspend fun setModuleCompleted(moduleId: String, isCompleted: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.moduleCompletionKey(moduleId)] = isCompleted
        }
    }
}

data class UserPreferences(
    val branch: String,
    val interests: String
)
