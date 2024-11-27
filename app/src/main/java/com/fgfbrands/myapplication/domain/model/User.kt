package com.fgfbrands.myapplication.domain.model

/**
 * Represents a user in the domain layer.
 *
 * Key Highlights:
 * - Provides essential user data for profiles and interactions.
 */
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val username: String?,
    val email: String?,
    val address: String?
)
