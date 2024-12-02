package com.fgfbrands.myapplication.ui.state

import com.fgfbrands.myapplication.domain.model.Comment

/**
 * Represents the UI state for the Home screen.
 *
 * Key Highlights:
 * - Manages expanded posts, comments, liked posts, and error messages.
 * - Provides a single source of truth for the UI state.
 */
data class HomeUiState(
    val expandedPostIds: Set<Int> = emptySet(),
    val commentsMap: Map<Int, List<Comment>> = emptyMap(),
    val likedPostIds: Set<Int> = emptySet(),
    val errorMessage: String? = null
)