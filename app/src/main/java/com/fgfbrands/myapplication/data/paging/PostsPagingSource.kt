package com.fgfbrands.myapplication.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fgfbrands.myapplication.core.consts.Constants
import com.fgfbrands.myapplication.data.local.post.PostDao
import com.fgfbrands.myapplication.data.model.UserDto
import com.fgfbrands.myapplication.data.model.UserResponse
import com.fgfbrands.myapplication.data.remote.post.PostService
import com.fgfbrands.myapplication.data.remote.user.UserService
import com.fgfbrands.myapplication.domain.mapper.PostMapper
import com.fgfbrands.myapplication.domain.mapper.UserMapper
import com.fgfbrands.myapplication.domain.model.Post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


/**
 * PagingSource implementation for fetching paginated posts and user details.
 *
 * **Key Highlights:**
 * - **List Performance**: Efficiently loads data using Paging 3 library.
 * - **Memory Management**: Caches posts locally to optimize memory usage.
 * - **Network Handling**: Handles network exceptions and provides fallback data.
 * - **Complex UI Interactions**: Fetches posts with user data for rich UI experiences.
 */
class PostsPagingSource(
    private val postService: PostService,
    private val userService: UserService,
    private val postDao: PostDao,
    private val dispatcher: CoroutineDispatcher
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val offset = params.key ?: Constants.INITIAL_OFFSET
        return try {
            val response = withContext(dispatcher) {
                postService.getPosts(offset, params.loadSize)
            }
            val postDtos = response.photos

            val posts = postDtos.mapNotNull { postDto ->
                val userDto = fetchUserOrFallback(postDto.userId)
                userDto?.let {
                    val postEntity = PostMapper.mapDtoToEntity(postDto, it)
                    postDao.upsertPosts(listOf(postEntity))
                    PostMapper.mapEntityToDomain(postEntity)
                }
            }

            LoadResult.Page(
                data = posts,
                prevKey = if (offset <= Constants.INITIAL_OFFSET) null else offset - params.loadSize,
                nextKey = if (posts.isEmpty()) null else offset + params.loadSize
            )
        } catch (e: IOException) {
            val cachedPosts = loadCachedPosts()
            if (cachedPosts.isNotEmpty()) {
                LoadResult.Page(
                    data = cachedPosts,
                    prevKey = null,
                    nextKey = null
                )
            } else {
                LoadResult.Error(e)
            }
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun loadCachedPosts(): List<Post> {
        return withContext(dispatcher) {
            val cachedEntities = postDao.getAllPosts()
            cachedEntities.map { PostMapper.mapEntityToDomain(it) }
        }
    }

    private suspend fun fetchUserOrFallback(userId: Int): UserDto? {
        return try {
            val userResponse = withContext(dispatcher) {
                userService.getUser(userId)
            }
            UserMapper.mapToDto(userResponse)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                // User not found, create a fallback user
                val fallbackUserResponse = UserResponse(
                    id = userId,
                    firstName = "Unknown",
                    lastName = "User",
                    image = ""
                )
                UserMapper.mapToDto(fallbackUserResponse)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(Constants.PAGE_SIZE)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(Constants.PAGE_SIZE)
        }
    }
}