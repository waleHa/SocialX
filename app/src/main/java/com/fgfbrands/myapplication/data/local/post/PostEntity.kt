package com.fgfbrands.myapplication.data.local.post

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fgfbrands.myapplication.data.local.converters.UserIdListConverter

/**
 * Represents a post stored locally in the database.
 *
 * Purpose:
 * - Facilitates offline storage and retrieval of posts.
 * - Supports caching for better performance and offline access.
 */
@Entity(tableName = "posts")
@TypeConverters(UserIdListConverter::class)
data class PostEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val likes: List<Int> = emptyList(),
    val userId: Int,
    val userName: String,
    val userImage: String
)