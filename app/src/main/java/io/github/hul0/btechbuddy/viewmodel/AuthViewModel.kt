package io.github.hul0.btechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.btechbuddy.data.repository.UserPreferencesRepository
import kotlinx.coroutines.launch
import java.net.URLDecoder

class AuthViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    fun saveSessionFromUrl(urlFragment: String) {
        viewModelScope.launch {
            val params = urlFragment.split("&").associate {
                val (key, value) = it.split("=")
                // Decode URL-encoded characters
                URLDecoder.decode(key, "UTF-8") to URLDecoder.decode(value, "UTF-8")
            }
            val accessToken = params["access_token"]
            val refreshToken = params["refresh_token"]

            if (accessToken != null && refreshToken != null) {
                userPreferencesRepository.saveAuthTokens(accessToken, refreshToken)
            }
        }
    }

    companion object {
        fun provideFactory(
            repository: UserPreferencesRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(repository) as T
            }
        }
    }
}
