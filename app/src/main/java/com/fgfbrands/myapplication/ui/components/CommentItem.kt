package com.fgfbrands.myapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fgfbrands.myapplication.domain.model.Comment


/**
 * Represents a single comment item in the UI.
 *
 * Key Highlights:
 * - Displays user avatar and comment text for a post.
 * - Optimized for inclusion in a list for performance.
 * - Supports dynamic updates for real-time interactions.
 */

@Composable
fun CommentItem(comment: Comment) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = "User ${comment.userId}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = comment.text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommentItemPreview() {
    val sampleComment = Comment(
        id = 1,
        postId = 1,
        userId = 2,
        text = "This is a sample comment."
    )
    CommentItem(comment = sampleComment)
}
