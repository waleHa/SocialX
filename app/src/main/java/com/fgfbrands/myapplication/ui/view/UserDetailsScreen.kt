package com.fgfbrands.myapplication.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fgfbrands.myapplication.domain.model.User
import com.fgfbrands.myapplication.ui.viewmodel.UserViewModel


/**
 * Renders the User Details screen.
 *
 * Key Highlights:
 * - Handles loading and error states for fetching user details.
 * - Displays user profile information with back navigation.
 */
@Composable
fun UserDetailsScreen(
    userId: Int,
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadUserDetails(userId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator()
            uiState.errorMessage != null -> Text(
                text = uiState.errorMessage ?: "",
                style = MaterialTheme.typography.bodyMedium
            )

            uiState.user != null -> UserDetailsContent(uiState.user!!)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserDetailsPreview() {
    UserDetailsContent(
        user = User(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            imageUrl = "https://via.placeholder.com/100",
            username = "john.doe",
            email = "john.doe@example.com",
            address = "Cityville, State"
        )
    )
}