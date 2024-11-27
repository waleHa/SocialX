package com.fgfbrands.myapplication.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fgfbrands.myapplication.domain.model.User

/**
 * Displays detailed information about a user.
 *
 * Key Highlights:
 * - Includes avatar, name, username, and email details.
 * - Adapts layout for responsiveness across devices.
 */
@Composable
fun UserDetailsContent(user: User) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "${user.firstName} ${user.lastName}", style = MaterialTheme.typography.titleLarge)
        Text(text = "Username: ${user.username ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Email: ${user.email ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Address: ${user.address ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
    }
}