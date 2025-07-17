package com.github.mockcat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mockcat.data.User
import com.github.mockcat.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Represents the different states our UI can be in
sealed class UserUiState {
    object Idle : UserUiState()
    object Loading : UserUiState()
    data class Success(val user: User) : UserUiState()
    data class Error(val message: String) : UserUiState()
}

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Idle)
    val uiState: StateFlow<UserUiState> = _uiState

    fun fetchUser() {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            try {
                // Make the network call
                val response = RetrofitClient.api.getUser(2)
                _uiState.value = UserUiState.Success(response.data)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UserUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}