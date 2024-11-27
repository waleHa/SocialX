package com.fgfbrands.myapplication.domain.repo

import com.fgfbrands.myapplication.domain.model.User

/**
 * Repository interface for user-related operations.
 *
 * Key Highlights:
 * - Abstracts data sources for user data.
 */
interface UserRepository {
    suspend fun getUserById(userId: Int): User?
}
