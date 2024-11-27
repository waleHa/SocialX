package com.fgfbrands.myapplication.data.model

/**
 * Data Transfer Object (DTO) for comments fetched from the backend.
 *
 * Purpose:
 * - Represents the structure of a comment in the API response.
 * - Used to map API responses to the domain model.
 */
data class CommentDto(
    val id: Int,
    val postId: Int,
    val userId: Int,
    val text: String
)
