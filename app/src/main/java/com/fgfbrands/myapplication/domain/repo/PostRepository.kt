package com.fgfbrands.myapplication.domain.repo

import androidx.paging.PagingData
import com.fgfbrands.myapplication.domain.model.Comment
import com.fgfbrands.myapplication.domain.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining operations for fetching posts, managing likes, and retrieving comments.
 *
 * Key Highlights:
 * - Clean architecture: Decouples data source from business logic.
 * - Supports pagination: Fetches paginated posts for performance optimization.
 * - Includes methods for comments and post details.
 */
interface PostRepository {
    fun getPostsPager(): Flow<PagingData<Post>>
    suspend fun likePost(postId: Int, userId: Int): Boolean
    suspend fun getPostById(postId: Int): Post?
    suspend fun getCommentsForPost(postId: Int): List<Comment>
    suspend fun getAllPosts(): List<Post>
    suspend fun deleteAllPosts()
}
