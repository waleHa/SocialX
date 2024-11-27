package com.fgfbrands.myapplication.data.remote.user

import com.fgfbrands.myapplication.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Defines endpoints for fetching user details.
 *
 * Key Highlights:
 * - Retrieves user information associated with posts.
 */
interface UserService {
    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): UserResponse
}
