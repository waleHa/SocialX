package com.fgfbrands.myapplication.data.model


/**
 * Data Transfer Object for users.
 *
 * Key Highlights:
 * - Represents the structure of user data retrieved from the backend.
 */
data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val username: String?,
    val email: String?,
    val address: AddressDto?
)

data class AddressDto(
    val city: String?,
    val state: String?
)
