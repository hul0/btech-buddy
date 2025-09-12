package io.github.hul0.btechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.hul0.btechbuddy.data.model.TodoItem
import io.github.hul0.btechbuddy.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class TodoViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    val todos: StateFlow<List<TodoItem>> = userPreferencesRepository.todos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTodo(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val newTodo = TodoItem(id = UUID.randomUUID().toString(), text = text)
            val updatedList = todos.value + newTodo
            userPreferencesRepository.saveTodos(updatedList)
        }
    }

    fun toggleTodoCompleted(id: String) {
        viewModelScope.launch {
            val updatedList = todos.value.map {
                if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
            }
            userPreferencesRepository.saveTodos(updatedList)
        }
    }

    fun deleteTodo(id: String) {
        viewModelScope.launch {
            val updatedList = todos.value.filterNot { it.id == id }
            userPreferencesRepository.saveTodos(updatedList)
        }
    }

    companion object {
        fun provideFactory(
            repository: UserPreferencesRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TodoViewModel(repository) as T
            }
        }
    }
}
