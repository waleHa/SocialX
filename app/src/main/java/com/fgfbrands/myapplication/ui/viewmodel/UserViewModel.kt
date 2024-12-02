package com.fgfbrands.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fgfbrands.myapplication.domain.model.User
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

    /**
     * Initiates loading user details for a given user ID.
     *
     * This function:
     * - Updates the UI state to show a loading indicator.
     * - Fetches the user details using the `fetchUserByIdUseCase`.
     * - Handles errors gracefully, providing feedback through the `uiState`.
     *
     * @param userId The ID of the user whose details are to be fetched.
     */
    fun loadUserDetails(userId: Int) {
        viewModelScope.launch {
            setLoadingState(true)
            try {
                val user = fetchUserByIdUseCase(userId)
                if (user != null) {
                    updateUserState(user)
                } else {
                    updateErrorState("User not found.")
                }
            } catch (e: Exception) {
                updateErrorState("Error fetching user details: ${e.localizedMessage}")
            } finally {
                setLoadingState(false)
            }
        }
    }

    /**
     * Updates the UI state to indicate loading progress.
     *
     * @param isLoading A boolean indicating whether data is currently being loaded.
     */
    private fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    /**
     * Updates the UI state with the fetched user details.
     *
     * @param user The user details fetched from the repository.
     */
    private fun updateUserState(user: User) {
        _uiState.update { it.copy(user = user, errorMessage = null) }
    }

    /**
     * Updates the UI state with an error message.
     *
     * @param message The error message to display in the UI.
     */
    private fun updateErrorState(message: String) {
        _uiState.update { it.copy(errorMessage = message, user = null) }
    }
}
