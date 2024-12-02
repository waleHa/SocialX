package com.fgfbrands.myapplication.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.fgfbrands.myapplication.ui.components.PostItem
import com.fgfbrands.myapplication.ui.viewmodel.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState

/**
 * Renders the Home screen, displaying a feed of posts.
 *
 * Key Highlights:
 * - Integrates pagination for scrolling.
 * - Implements swipe-to-refresh for data updates.
 * - Supports liking, commenting, and navigation to user profiles.
 */

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val posts = viewModel.postsFlow.collectAsLazyPagingItems()
    val isRefreshing = posts.loadState.refresh is androidx.paging.LoadState.Loading

    SwipeRefresh(
        state = SwipeRefreshState(isRefreshing),
        onRefresh = { posts.refresh() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(posts) { post ->
                    post?.let { postItem ->
                        val isExpanded = uiState.expandedPostIds.contains(postItem.id)
                        PostItem(
                            post = postItem,
                            isExpanded = isExpanded,
                            onToggleDescription = { viewModel.expandPostComments(postItem.id) },
                            onLikeClick = { viewModel.updateLikeState(postItem.id) },
                            onAddComment = { commentText ->
                                viewModel.addComment(
                                    postItem.id,
                                    commentText
                                )
                            },
                            onUserClick = { userId ->
                                navController.navigate("userDetails/$userId")
                            }
                        )
                    }
                }
            }
        }
    }
}