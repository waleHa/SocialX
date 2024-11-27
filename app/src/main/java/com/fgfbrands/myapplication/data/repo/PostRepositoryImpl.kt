package com.fgfbrands.myapplication.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fgfbrands.myapplication.core.consts.Constants
import com.fgfbrands.myapplication.data.local.post.PostDao
import com.fgfbrands.myapplication.data.paging.PostsPagingSource
import com.fgfbrands.myapplication.data.remote.post.PostService
import com.fgfbrands.myapplication.data.remote.user.UserService
import com.fgfbrands.myapplication.domain.mapper.PostMapper
import com.fgfbrands.myapplication.domain.mapper.UserMapper
import com.fgfbrands.myapplication.domain.model.Comment
import com.fgfbrands.myapplication.domain.model.Post
import com.fgfbrands.myapplication.domain.repo.PostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository implementation for handling posts and related data operations.
 *
 * **Key Highlights:**
 * - **List Performance**: Integrates PagingSource for efficient data loading.
 * - **Memory Management**: Caches posts locally to reduce redundant network calls.
 * - **Network Handling**: Performs asynchronous operations with proper dispatchers.
 * - **Complex UI Interactions**: Manages likes and comments for posts.
 */
class PostRepositoryImpl @Inject constructor(
    private val postService: PostService,
    private val userService: UserService,
    private val postDao: PostDao,
    private val ioDispatcher: CoroutineDispatcher
) : PostRepository {

    override fun getPostsPager(): Flow<PagingData<Post>> = Pager(
        config = PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            PostsPagingSource(postService, userService, postDao, ioDispatcher)
        }
    ).flow

    override suspend fun likePost(postId: Int, userId: Int): Boolean {
        return withContext(ioDispatcher) {
            val postEntity = postDao.getPostById(postId)
            if (postEntity != null) {
                val updatedLikes = postEntity.likes.toMutableList()
                if (updatedLikes.contains(userId)) {
                    updatedLikes.remove(userId)
                } else {
                    updatedLikes.add(userId)
                }
                postDao.updateLikes(postId, updatedLikes)
                true
            } else {
                false
            }
        }
    }

    override suspend fun getPostById(postId: Int): Post? {
        return withContext(ioDispatcher) {
            val postEntity = postDao.getPostById(postId)
            if (postEntity != null) {
                PostMapper.mapEntityToDomain(postEntity)
            } else {
                try {
                    val postDto = postService.getPostById(postId)
                    val userResponse = userService.getUser(postDto.userId)
                    val userDto = UserMapper.mapToDto(userResponse)
                    val postEntity = PostMapper.mapDtoToEntity(postDto, userDto)
                    postDao.upsertPosts(listOf(postEntity))
                    PostMapper.mapEntityToDomain(postEntity)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    override suspend fun getCommentsForPost(postId: Int): List<Comment> {
        return withContext(ioDispatcher) {
            try {
                val commentDtos = postService.fetchCommentsForPost(postId)
                commentDtos.map { dto ->
                    Comment(
                        id = dto.id,
                        postId = dto.postId,
                        userId = dto.userId,
                        text = dto.text
                    )
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getAllPosts(): List<Post> {
        return withContext(ioDispatcher) {
            val postEntities = postDao.getAllPosts()
            if (postEntities.isNotEmpty()) {
                postEntities.map { PostMapper.mapEntityToDomain(it) }
            } else {
                val response = postService.getPosts(Constants.INITIAL_OFFSET, Constants.PAGE_SIZE)
                val posts = response.photos.mapNotNull { postDto ->
                    try {
                        val userResponse = userService.getUser(postDto.userId)
                        val userDto = UserMapper.mapToDto(userResponse)
                        val postEntity = PostMapper.mapDtoToEntity(postDto, userDto)
                        postEntity
                    } catch (e: Exception) {
                        null
                    }
                }
                postDao.upsertPosts(posts)
                posts.map { PostMapper.mapEntityToDomain(it) }
            }
        }
    }

    override suspend fun deleteAllPosts() {
        withContext(ioDispatcher) {
            postDao.deleteAllPosts()
        }
    }
}