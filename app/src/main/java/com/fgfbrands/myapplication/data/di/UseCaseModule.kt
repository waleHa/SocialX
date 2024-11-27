package com.fgfbrands.myapplication.data.di

import com.fgfbrands.myapplication.domain.repo.PostRepository
import com.fgfbrands.myapplication.domain.repo.UserRepository
import com.fgfbrands.myapplication.domain.usecase.FetchUserByIdUseCase
import com.fgfbrands.myapplication.domain.usecase.GetCommentsUseCase
import com.fgfbrands.myapplication.domain.usecase.GetPostsUseCase
import com.fgfbrands.myapplication.domain.usecase.LikePostUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


/**
 * Provides use cases for dependency injection.
 *
 * Key Highlights:
 * - Supplies use cases to ViewModels.
 */
@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetPostsUseCase(postRepository: PostRepository): GetPostsUseCase =
        GetPostsUseCase(postRepository)

    @Provides
    fun provideGetCommentsUseCase(postRepository: PostRepository): GetCommentsUseCase {
        return GetCommentsUseCase(postRepository)
    }

    @Provides
    fun provideLikePostUseCase(postRepository: PostRepository): LikePostUseCase =
        LikePostUseCase(postRepository)

    @Provides
    fun provideFetchUserByIdUseCase(userRepository: UserRepository): FetchUserByIdUseCase =
        FetchUserByIdUseCase(userRepository)


}