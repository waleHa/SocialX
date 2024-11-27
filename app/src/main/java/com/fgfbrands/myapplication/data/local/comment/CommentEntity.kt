package com.fgfbrands.myapplication.data.local.comment

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a comment stored locally in the database.
 *
 * Key Highlights:
 * - Facilitates offline comment storage and retrieval.
 */
@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val postId: Int,
    val userId: Int,
    val text: String
)
