package com.fgfbrands.myapplication.domain.mapper

import com.fgfbrands.myapplication.data.model.AddressDto
import com.fgfbrands.myapplication.data.model.UserDto
import com.fgfbrands.myapplication.data.model.UserResponse
import com.fgfbrands.myapplication.domain.model.User

/**
 * Maps between User-related DTOs, entities, and domain models.
 *
 * Key Highlights:
 * - Handles complex mappings for user data.
 * - Supports error handling for incomplete API responses.
 */
object UserMapper {

    fun mapToDto(userResponse: UserResponse): UserDto {
        val addressDto = userResponse.address?.let {
            AddressDto(
                city = it.city,
                state = it.state
            )
        }
        return UserDto(
            id = userResponse.id,
            firstName = userResponse.firstName ?: "Unknown",
            lastName = userResponse.lastName ?: "User",
            imageUrl = userResponse.image ?: "",
            username = userResponse.username,
            email = userResponse.email,
            address = addressDto
        )
    }

    fun mapToDomain(userDto: UserDto): User {
        val address = userDto.address?.let {
            listOfNotNull(it.city, it.state).joinToString(", ")
        }

        return User(
            id = userDto.id,
            firstName = userDto.firstName,
            lastName = userDto.lastName,
            imageUrl = userDto.imageUrl,
            username = userDto.username,
            email = userDto.email,
            address = address
        )
    }

    fun mapUserResponseToDomain(userResponse: UserResponse): User {
        val address = userResponse.address?.let {
            listOfNotNull(it.city, it.state).joinToString(", ")
        }

        return User(
            id = userResponse.id,
            firstName = userResponse.firstName ?: "Unknown",
            lastName = userResponse.lastName ?: "User",
            imageUrl = userResponse.image ?: "",
            username = userResponse.username,
            email = userResponse.email,
            address = address
        )
    }
}
