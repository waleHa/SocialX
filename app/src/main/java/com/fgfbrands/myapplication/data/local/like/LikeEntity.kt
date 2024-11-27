package com.fgfbrands.myapplication.data.local.like

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a like stored locally in the database.
 *
 * Key Highlights:
 * - Facilitates offline - like/unlike functionality.
 */
@Entity(tableName = "likes")
data class LikeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val postId: Int,
    val userId: Int
)
