package com.fgfbrands.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fgfbrands.myapplication.domain.usecase.FetchUserByIdUseCase
import com.fgfbrands.myapplication.ui.state.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the User Details screen.
 *
 * Key Highlights:
 * - Fetches user details from the repository.
 * - Manages loading and error states efficiently.
 * - Ensures lifecycle-aware data handling.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val fetchUserByIdUseCase: FetchUserByIdUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun loadUserDetails(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val user = fetchUserByIdUseCase(userId)
                if (user != null) {
                    _uiState.update { it.copy(user = user, isLoading = false) }
                } else {
                    _uiState.update { it.copy(errorMessage = "User not found.", isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error fetching user details: ${e.localizedMessage}",
                        isLoading = false
                    )
                }
            }
        }
    }
}
