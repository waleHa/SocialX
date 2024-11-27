package com.fgfbrands.myapplication.data.di

import android.content.Context
import androidx.room.Room
import com.fgfbrands.myapplication.data.local.db.AppDatabase
import com.fgfbrands.myapplication.data.local.comment.CommentDao
import com.fgfbrands.myapplication.data.local.like.LikeDao
import com.fgfbrands.myapplication.data.local.post.PostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides application-wide dependencies.
 *
 * Key Highlights:
 * - Configures Room database for local data storage.
 * - Supplies DAOs for accessing local data.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideLikeDao(appDatabase: AppDatabase): LikeDao = appDatabase.likeDao()

    @Provides
    fun provideCommentDao(appDatabase: AppDatabase): CommentDao = appDatabase.commentDao()

    @Provides
    fun providePostDao(appDatabase: AppDatabase): PostDao = appDatabase.postDao()
}
