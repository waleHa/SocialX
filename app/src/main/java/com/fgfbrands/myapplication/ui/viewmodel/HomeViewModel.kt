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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

/**
 * ViewModel for managing the state and interactions of the Home screen.
 *
 * Key Highlights:
 * - Fetches posts with pagination.
 * - Handles likes and comments asynchronously.
 * - Optimizes UI updates for a smooth user experience.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val getCommentsUseCase: GetCommentsUseCase
) : ViewModel() {

    private val commentIdGenerator = AtomicInteger(0)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _userCommentsMap = MutableStateFlow<Map<Int, List<Comment>>>(emptyMap())

    private val _likedPosts = MutableStateFlow<Set<Int>>(emptySet())

    val postsFlow: Flow<PagingData<Post>> = combine(
        getPostsUseCase().cachedIn(viewModelScope),
        _userCommentsMap,
        _likedPosts
    ) { pagingData, userCommentsMap, likedPostsSet ->
        pagingData.map { post ->
            val isLiked = likedPostsSet.contains(post.id)
            val likeCountAdjustment = if (isLiked) 1 else 0
            val apiComments = _uiState.value.commentsMap[post.id] ?: emptyList()
            val userComments = userCommentsMap[post.id] ?: emptyList()
            post.copy(
                isLiked = isLiked,
                likeCount = post.likes.size + likeCountAdjustment,
                comments = apiComments + userComments
            )
        }
    }

    fun togglePostDescription(postId: Int) {
        val currentSet = _uiState.value.expandedPostIds.toMutableSet()
        if (currentSet.contains(postId)) {
            currentSet.remove(postId)
        } else {
            currentSet.add(postId)
            if (!_uiState.value.commentsMap.containsKey(postId)) {
                loadCommentsForPost(postId)
            }
        }
        _uiState.update { it.copy(expandedPostIds = currentSet) }
    }

    fun toggleLike(postId: Int) {
        val likedPosts = _likedPosts.value.toMutableSet()
        if (likedPosts.contains(postId)) {
            likedPosts.remove(postId)
        } else {
            likedPosts.add(postId)
        }
        _likedPosts.value = likedPosts

        viewModelScope.launch {
            try {
                val success = likePostUseCase(postId)
                if (!success) {
                    val updatedLikedPosts = _likedPosts.value.toMutableSet()
                    if (updatedLikedPosts.contains(postId)) {
                        updatedLikedPosts.remove(postId)
                    } else {
                        updatedLikedPosts.add(postId)
                    }
                    _likedPosts.value = updatedLikedPosts
                    _uiState.update { it.copy(errorMessage = "Failed to like/unlike the post.") }
                }
            } catch (e: Exception) {
                val updatedLikedPosts = _likedPosts.value.toMutableSet()
                if (updatedLikedPosts.contains(postId)) {
                    updatedLikedPosts.remove(postId)
                } else {
                    updatedLikedPosts.add(postId)
                }
                _likedPosts.value = updatedLikedPosts
                _uiState.update { it.copy(errorMessage = "Error toggling like: ${e.localizedMessage}") }
            }
        }
    }

    fun addComment(postId: Int, commentText: String) {
        val newComment = Comment(
            id = generateUniqueCommentId(),
            postId = postId,
            userId = 1,
            text = commentText
        )
        val updatedComments = _userCommentsMap.value[postId]?.toMutableList() ?: mutableListOf()
        updatedComments.add(newComment)
        val updatedCommentsMap = _userCommentsMap.value.toMutableMap()
        updatedCommentsMap[postId] = updatedComments
        _userCommentsMap.value = updatedCommentsMap
    }

    private fun generateUniqueCommentId(): Int {
        return commentIdGenerator.incrementAndGet()
    }

    private fun loadCommentsForPost(postId: Int) {
        viewModelScope.launch {
            try {
                val comments = getCommentsUseCase(postId)
                val updatedCommentsMap = _uiState.value.commentsMap.toMutableMap()
                updatedCommentsMap[postId] = comments
                _uiState.update { it.copy(commentsMap = updatedCommentsMap) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error loading comments: ${e.localizedMessage}") }
            }
        }
    }
}
