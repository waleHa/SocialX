package com.fgfbrands.myapplication.domain.model

/**
 * Represents a post in the domain layer.
 *
 * Key Highlights:
 * - Includes fields for likes, comments, and user details.
 * - Supports state management for UI interactions.
 */

data class Post(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val likes: List<Int>,
    val userId: Int,
    val userName: String,
    val userImage: String,
    val isLiked: Boolean = false,
    val likeCount: Int = 0,
    val comments: List<Comment> = emptyList()
)