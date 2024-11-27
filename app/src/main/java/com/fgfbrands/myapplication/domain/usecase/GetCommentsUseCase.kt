package com.fgfbrands.myapplication.domain.usecase

import com.fgfbrands.myapplication.domain.model.Comment
import com.fgfbrands.myapplication.domain.repo.PostRepository
import javax.inject.Inject

/**
 * Use case for fetching comments for a specific post.
 *
 * Key Highlights:
 * - Retrieves comments from the repository.
 * - Decouples data fetching from UI logic.
 */
class GetCommentsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: Int): List<Comment> {
        return postRepository.getCommentsForPost(postId)
    }
}
