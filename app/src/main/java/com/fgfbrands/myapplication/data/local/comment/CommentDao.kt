package com.fgfbrands.myapplication.data.local.comment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * DAO for managing comments in the local database.
 *
 * Purpose:
 * - Provides methods for inserting and retrieving comments.
 *
 * Note:
 * - Comments feature is currently not used but included for future expansion.
 */
@Dao
interface CommentDao {

    @Insert
    suspend fun insert(comment: CommentEntity)

    @Query("SELECT * FROM comments WHERE postId = :postId")
    suspend fun getCommentsByPostId(postId: Int): List<CommentEntity>

    @Query("DELETE FROM comments WHERE postId = :postId")
    suspend fun deleteCommentsForPost(postId: Int)
}
