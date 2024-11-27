package com.fgfbrands.myapplication.domain.usecase

import androidx.paging.PagingData
import com.fgfbrands.myapplication.domain.model.Post
import com.fgfbrands.myapplication.domain.repo.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a paginated list of posts.
 *
 * Key Highlights:
 * - Encapsulates the logic for fetching posts with pagination support.
 */
class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke(): Flow<PagingData<Post>> {
        return postRepository.getPostsPager()
    }
}
