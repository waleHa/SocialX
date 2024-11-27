package com.fgfbrands.myapplication.ui.state

import com.fgfbrands.myapplication.domain.model.User

/**
 * Represents the UI state for the User Details screen.
 *
 * Key Highlights:
 * - Manages user details and loading/error states.
 * - Ensures a seamless user profile experience.
 */

data class UserUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)