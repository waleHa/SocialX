package com.fgfbrands.myapplication.domain.model

/**
 * Represents a comment in the domain layer.
 *
 * Key Highlights:
 * - Encapsulates data required for UI and operations.
 */
data class Comment(
    val id: Int,
    val postId: Int,
    val userId: Int,
    val text: String
)
