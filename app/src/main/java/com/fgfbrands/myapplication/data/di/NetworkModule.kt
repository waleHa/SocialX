package com.fgfbrands.myapplication.data.di

import com.fgfbrands.myapplication.core.consts.Constants
import com.fgfbrands.myapplication.data.local.post.PostDao
import com.fgfbrands.myapplication.data.remote.post.PostService
import com.fgfbrands.myapplication.data.remote.user.UserService
import com.fgfbrands.myapplication.data.repo.PostRepositoryImpl
import com.fgfbrands.myapplication.data.repo.UserRepositoryImpl
import com.fgfbrands.myapplication.domain.repo.PostRepository
import com.fgfbrands.myapplication.domain.repo.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Provides dependencies for networking and background operations.
 *
 * Key Highlights:
 * - Configures Retrofit and OkHttp for API communication.
 * - Supplies a coroutine dispatcher for managing background tasks.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @PostsRetrofit
    @Provides
    @Singleton
    fun providePostsRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.POSTS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @UsersRetrofit
    @Provides
    @Singleton
    fun provideUsersRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.USERS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePostService(@PostsRetrofit retrofit: Retrofit): PostService {
        return retrofit.create(PostService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(@UsersRetrofit retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providePostRepository(
        postService: PostService,
        userService: UserService,
        postDao: PostDao,
        dispatcher: CoroutineDispatcher
    ): PostRepository {
        return PostRepositoryImpl(postService, userService, postDao, dispatcher)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userService: UserService,
        dispatcher: CoroutineDispatcher
    ): UserRepository {
        return UserRepositoryImpl(userService, dispatcher)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PostsRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersRetrofit