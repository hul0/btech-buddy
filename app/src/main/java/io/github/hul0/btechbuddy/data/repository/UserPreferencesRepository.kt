package io.github.hul0.btechbuddy.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(context: Context) {
    private val dataStore = context.dataStore

    private data class UserPreferencesKeys(
        val BRANCH_KEY: Preferences.Key<String> = stringPreferencesKey("branch"),
        val INTERESTS_KEY: Preferences.Key<String> = stringPreferencesKey("interests"),
        val COMPLETED_MODULES_KEY: Preferences.Key<Set<String>> = stringSetPreferencesKey("completed_modules")
    )

    private val keys = UserPreferencesKeys()

    val userPreferences: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            branch = preferences[keys.BRANCH_KEY] ?: "",
            interests = preferences[keys.INTERESTS_KEY] ?: ""
        )
    }

    val completedModuleIds: Flow<Set<String>> = dataStore.data
        .map { preferences ->
            preferences[keys.COMPLETED_MODULES_KEY] ?: emptySet()
        }

    suspend fun updateUserPreferences(branch: String, interests: String) {
        dataStore.edit { preferences ->
            preferences[keys.BRANCH_KEY] = branch
            preferences[keys.INTERESTS_KEY] = interests
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
    val branch: String,
    val interests: String
)

