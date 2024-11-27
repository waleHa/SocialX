package com.fgfbrands.myapplication.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fgfbrands.myapplication.data.local.comment.CommentDao
import com.fgfbrands.myapplication.data.local.comment.CommentEntity
import com.fgfbrands.myapplication.data.local.converters.UserIdListConverter
import com.fgfbrands.myapplication.data.local.like.LikeDao
import com.fgfbrands.myapplication.data.local.like.LikeEntity
import com.fgfbrands.myapplication.data.local.post.PostDao
import com.fgfbrands.myapplication.data.local.post.PostEntity

/**
 * Room database configuration for local data storage.
 *
 * Purpose:
 * - Manages local storage of likes and comments.
 * - Provides access to DAOs for database operations.
 *
 * Key Highlights:
 * - Ensures efficient data access and caching.
 */
@Database(
    entities = [LikeEntity::class, CommentEntity::class, PostEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(UserIdListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun likeDao(): LikeDao
    abstract fun commentDao(): CommentDao
    abstract fun postDao(): PostDao
}
