package com.fgfbrands.myapplication.domain.usecase

import com.fgfbrands.myapplication.core.consts.Constants
import com.fgfbrands.myapplication.domain.repo.PostRepository
import javax.inject.Inject

/**
 * Use case for toggling the like state of a post.
 *
 * Key Highlights:
 * - Encapsulates logic for liking or unliking a post.
 */
class LikePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Boolean {
        return postRepository.likePost(postId, Constants.CURRENT_USER_ID)
    }
}
