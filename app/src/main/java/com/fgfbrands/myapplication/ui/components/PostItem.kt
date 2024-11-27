package com.fgfbrands.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fgfbrands.myapplication.domain.model.Comment
import com.fgfbrands.myapplication.domain.model.Post


/**
 * Represents a single post in the feed.
 *
 * Key Highlights:
 * - Displays post image, description, likes, and comments.
 * - Supports interactive features like liking and commenting.
 * - Implements expandable sections for viewing comments.
 */
@Composable
fun PostItem(
    post: Post,
    isExpanded: Boolean,
    onToggleDescription: () -> Unit,
    onLikeClick: () -> Unit,
    onAddComment: (String) -> Unit,
    modifier: Modifier = Modifier,
    onUserClick: (Int) -> Unit
) {
    var newComment by remember { mutableStateOf("") }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onUserClick(post.userId) }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(post.userImage),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = post.userName,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = rememberAsyncImagePainter(post.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onLikeClick) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (post.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(text = "${post.likeCount} Likes", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isExpanded) "Hide comments" else "View all comments",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.clickable { onToggleDescription() }
            )

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(post.userImage),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = post.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                post.comments.forEach { comment ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter("https://randomuser.me/api/portraits/men/3.jpg"),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = comment.text,
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newComment,
                    onValueChange = { newComment = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    placeholder = { Text(text = "Add a comment...") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (newComment.isNotBlank()) {
                            onAddComment(newComment.trim())
                            newComment = ""
                        }
                    })
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostItemPreview() {
    val post = Post(
        id = 1,
        title = "Sample Post",
        description = "This is a sample post description.",
        imageUrl = "https://randomuser.me/api/portraits/women/30.jpg",
        likes = emptyList(),
        userId = 1,
        userName = "John Doe",
        userImage = "https://randomuser.me/api/portraits/men/13.jpg",
        isLiked = false,
        likeCount = 0,
        comments = listOf(
            Comment(id = 1, postId = 1, userId = 1, text = "Nice post!"),
            Comment(id = 2, postId = 1, userId = 2, text = "Great job!")
        )
    )

    PostItem(
        post = post,
        isExpanded = true,
        onToggleDescription = {},
        onLikeClick = {},
        onAddComment = {},
        onUserClick = {}
    )
}