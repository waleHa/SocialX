package com.fgfbrands.myapplication.data.local.post


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO for managing local database operations related to posts.
 *
 * Key Highlights:
 * - Database operations: Efficiently handles CRUD operations for posts.
 * - Caching: Supports storing and retrieving posts for offline access.
 * - Like management: Enables adding and removing likes at the database level.
 */
@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPosts(posts: List<PostEntity>)

    @Query("SELECT * FROM posts ORDER BY id ASC")
    suspend fun getAllPosts(): List<PostEntity>

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Int): PostEntity?

    @Query("UPDATE posts SET likes = :updatedLikes WHERE id = :postId")
    suspend fun updateLikes(postId: Int, updatedLikes: List<Int>)

    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()
}

