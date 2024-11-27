package com.fgfbrands.myapplication.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for representing a post from the API.
 *
 * Key Highlights:
 * - Maps directly to the API response for individual posts.
 */
data class PostDto(
    val id: Int,
    val title: String,
    val description: String,
    @SerializedName("url")
    val imageUrl: String,
    @SerializedName("user")
    val userId: Int
)
