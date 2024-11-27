package com.fgfbrands.myapplication.data.model

import com.google.gson.annotations.SerializedName

/**
 * DTO representing the response structure for fetching paginated posts.
 *
 * Key Highlights:
 * - Encapsulates the top-level response and extracts the list of photos (posts).
 */
data class PostsResponse(
    @SerializedName("photos") val photos: List<PostDto>
)
