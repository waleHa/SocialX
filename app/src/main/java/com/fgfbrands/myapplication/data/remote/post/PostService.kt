package com.fgfbrands.myapplication.data.remote.post

import com.fgfbrands.myapplication.data.model.CommentDto
import com.fgfbrands.myapplication.data.model.PostDto
import com.fgfbrands.myapplication.data.model.PostsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines API endpoints for fetching posts and comments.
 *
 * Key Highlights:
 * - Supports paginated fetching of posts.
 * - Provides endpoint for fetching comments for a post.
 */
interface PostService {
    @GET("v1/sample-data/photos")
    suspend fun getPosts(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PostsResponse

    @GET("photos/{id}")
    suspend fun getPostById(
        @Path("id") postId: Int
    ): PostDto

    @GET("photos/{postId}/comments")
    suspend fun fetchCommentsForPost(
        @Path("postId") postId: Int
    ): List<CommentDto>
}
