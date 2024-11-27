package com.fgfbrands.myapplication.ui.state

import com.fgfbrands.myapplication.domain.model.Comment

/**
 * Represents the UI state for the Home screen.
 *
 * Key Highlights:
 * - Manages post data, likes, and comments.
 * - Ensures efficient state updates for a dynamic feed.
 */
data class HomeUiState(
    val expandedPostIds: Set<Int> = emptySet(),
    val commentsMap: Map<Int, List<Comment>> = emptyMap(),
    val errorMessage: String? = null
)