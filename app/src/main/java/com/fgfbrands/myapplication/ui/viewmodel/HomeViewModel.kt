package com.fgfbrands.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.fgfbrands.myapplication.domain.model.Comment
import com.fgfbrands.myapplication.domain.model.Post
import com.fgfbrands.myapplication.domain.usecase.GetCommentsUseCase
import com.fgfbrands.myapplication.domain.usecase.GetPostsUseCase
import com.fgfbrands.myapplication.domain.usecase.LikePostUseCase
import com.fgfbrands.myapplication.ui.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

/**
 * ViewModel for managing the state and interactions of the Home screen.
 *
 * Key Highlights:
 * - Fetches posts with pagination for efficient scrolling.
 * - Handles likes and comments asynchronously, ensuring immediate feedback.
 * - Manages UI state using StateFlow for reactive updates.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val getCommentsUseCase: GetCommentsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val userComments = MutableStateFlow<Map<Int, List<Comment>>>(emptyMap())

    private val commentIdGenerator = AtomicInteger(0)

    val postsFlow: Flow<PagingData<Post>> = fetchPostsFlow()

    /**
     * Fetches posts with additional information like likes and comments.
     * Feature: Scrollable Feed
     *
     * @return A Flow of PagingData containing enriched Post objects.
     * Used to be combinePostsFlow
     */
    private fun fetchPostsFlow(): Flow<PagingData<Post>> {
        return combine(
            getPostsUseCase().cachedIn(viewModelScope),
            userComments,
            uiState.map { it.likedPostIds }
        ) { pagingData, userCommentsMap, likedPostIds ->
            pagingData.map { post ->
                val isLiked = likedPostIds.contains(post.id)
                val likeCountAdjustment = if (isLiked) 1 else 0
                val apiComments = uiState.value.commentsMap[post.id] ?: emptyList()
                val userComments = userCommentsMap[post.id] ?: emptyList()
                post.copy(
                    isLiked = isLiked,
                    likeCount = post.likes.size + likeCountAdjustment,
                    comments = apiComments + userComments
                )
            }
        }
    }

    /**
     * Expands or collapses the comments section for a post.
     * Feature: Comment Section
     *
     * @param postId The ID of the post whose comments section is to be toggled.
     * Used to be togglePostDescription.
     */
    fun expandPostComments(postId: Int) {
        _uiState.update { currentState ->
            val updatedExpandedIds = currentState.expandedPostIds.toMutableSet().apply {
                if (contains(postId)) remove(postId) else add(postId)
            }
            currentState.copy(expandedPostIds = updatedExpandedIds)
        }
        fetchCommentsIfNeeded(postId)
    }

    /**
     * Updates the like state of a post and synchronizes with the backend.
     * Feature: Like/Unlike Functionality
     *
     * @param postId The ID of the post to like or unlike.
     * Used to be toggleLike
     */
    fun updateLikeState(postId: Int) {
        _uiState.update { currentState ->
            val updatedLikedPostIds = currentState.likedPostIds.toMutableSet().apply {
                if (contains(postId)) remove(postId) else add(postId)
            }
            currentState.copy(likedPostIds = updatedLikedPostIds)
        }
        viewModelScope.launch {
            try {
                if (!likePostUseCase(postId)) revertLikeState(postId)
            } catch (e: Exception) {
                revertLikeState(postId)
            }
        }
    }

    /**
     * Reverts the like state for a post in case of backend failure.
     *
     * @param postId The ID of the post whose like state needs to be reverted.
     */
    private fun revertLikeState(postId: Int) {
        _uiState.update { currentState ->
            val updatedLikedPostIds = currentState.likedPostIds.toMutableSet().apply {
                if (contains(postId)) remove(postId) else add(postId)
            }
            currentState.copy(likedPostIds = updatedLikedPostIds, errorMessage = "Failed to update like state.")
        }
    }

    /**
     * Adds a comment to a post and updates the UI immediately.
     * Feature: Comment Section
     *
     * @param postId The ID of the post to which the comment is added.
     * @param commentText The content of the comment.
     */
    fun addComment(postId: Int, commentText: String) {
        val newComment = Comment(
            id = generateUniqueCommentId(),
            postId = postId,
            userId = 1, // Placeholder for current user ID
            text = commentText
        )
        val updatedComments = userComments.value[postId]?.toMutableList() ?: mutableListOf()
        updatedComments.add(newComment)
        userComments.update { it.toMutableMap().apply { put(postId, updatedComments) } }
    }

    /**
     * Fetches comments for a post if not already loaded.
     * Feature: Comment Section
     *
     * @param postId The ID of the post whose comments are to be fetched.
     */
    private fun fetchCommentsIfNeeded(postId: Int) {
        if (uiState.value.commentsMap.containsKey(postId)) return
        viewModelScope.launch {
            try {
                val comments = getCommentsUseCase(postId)
                _uiState.update { currentState ->
                    val updatedCommentsMap = currentState.commentsMap.toMutableMap().apply {
                        put(postId, comments)
                    }
                    currentState.copy(commentsMap = updatedCommentsMap)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error loading comments: ${e.localizedMessage}") }
            }
        }
    }

    /**
     * Generates a unique ID for user-added comments.
     *
     * @return A unique integer ID.
     */
    private fun generateUniqueCommentId(): Int = commentIdGenerator.incrementAndGet()
}
