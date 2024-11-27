package com.fgfbrands.myapplication.data.local.like


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * DAO for managing likes in the local database.
 *
 * Purpose:
 * - Provides methods for inserting, deleting, and retrieving likes.
 *
 * Key Highlights:
 * - Enables offline like functionality.
 */
@Dao
interface LikeDao {
    @Insert
    suspend fun insert(like: LikeEntity)

    @Delete
    suspend fun delete(like: LikeEntity)

    @Query("SELECT * FROM likes WHERE postId = :postId AND userId = :userId LIMIT 1")
    suspend fun getLikeByPostIdAndUserId(postId: Int, userId: Int): LikeEntity?
}
