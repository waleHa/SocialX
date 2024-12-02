package com.fgfbrands.myapplication.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fgfbrands.myapplication.core.consts.Constants
import com.fgfbrands.myapplication.data.local.post.PostDao
import com.fgfbrands.myapplication.data.model.PostDto
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

    /**
     * Loads a page of data, including fetching posts and their associated user data.
     *
     * Handles caching and network exceptions to provide fallback data when needed.
     *
     * @param params Contains information about the requested load, including key and page size.
     * @return A LoadResult containing the loaded data or an error.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val offset = params.key ?: Constants.INITIAL_OFFSET

        return try {
            val posts = fetchPostsWithUsers(offset, params.loadSize)
            LoadResult.Page(
                data = posts,
                prevKey = calculatePrevKey(offset, params.loadSize),
                nextKey = calculateNextKey(posts, offset, params.loadSize)
            )
        } catch (e: IOException) {
            handleLoadError(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    /**
     * Fetches posts along with user details, ensuring the data is enriched for the UI.
     *
     * Maps each post DTO to a domain object and caches it locally for offline support.
     *
     * @param offset The starting point for fetching posts.
     * @param loadSize The number of posts to fetch.
     * @return A list of domain posts enriched with user data.
     */
    private suspend fun fetchPostsWithUsers(offset: Int, loadSize: Int): List<Post> {
        val response = withContext(dispatcher) {
            postService.getPosts(offset, loadSize)
        }
        val postDtos = response.photos

        return postDtos.mapNotNull { postDto ->
            val userDto = fetchUserOrFallback(postDto.userId)
            userDto?.let { mapPostToDomain(postDto, it) }
        }
    }

    /**
     * Maps a post DTO and user DTO to a domain Post object.
     *
     * Stores the mapped Post entity in the local database for future use.
     *
     * @param postDto The data transfer object representing the post.
     * @param userDto The data transfer object representing the user.
     * @return A domain Post object.
     */
    private suspend fun mapPostToDomain(postDto: PostDto, userDto: UserDto): Post {
        val postEntity = PostMapper.mapDtoToEntity(postDto, userDto)
        postDao.upsertPosts(listOf(postEntity))
        return PostMapper.mapEntityToDomain(postEntity)
    }

    /**
     * Fetches user details from the remote API or provides a fallback user if not found.
     *
     * Handles 404 errors by returning a placeholder user and ensures graceful error handling.
     *
     * @param userId The ID of the user to fetch.
     * @return A UserDto containing user details or a fallback user if not found.
     */
    private suspend fun fetchUserOrFallback(userId: Int): UserDto? {
        return try {
            val userResponse = withContext(dispatcher) { userService.getUser(userId) }
            UserMapper.mapToDto(userResponse)
        } catch (e: HttpException) {
            handleUserFallback(e, userId)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Provides a fallback user if the user is not found in the remote API.
     *
     * Specifically handles 404 HTTP errors by returning a placeholder user.
     *
     * @param e The HttpException encountered.
     * @param userId The ID of the missing user.
     * @return A UserDto containing fallback user details or null for other errors.
     */
    private fun handleUserFallback(e: HttpException, userId: Int): UserDto? {
        return if (e.code() == 404) {
            val fallbackUserResponse = UserResponse(
                id = userId,
                firstName = "Unknown",
                lastName = "User",
                image = ""
            )
            UserMapper.mapToDto(fallbackUserResponse)
        } else null
    }

    /**
     * Handles IO exceptions by attempting to load cached posts from the local database.
     *
     * If cached posts are available, they are returned as the result; otherwise, an error is thrown.
     *
     * @param exception The IOException encountered during the load.
     * @return A LoadResult containing cached posts or an error.
     */
    private suspend fun handleLoadError(exception: IOException): LoadResult<Int, Post> {
        val cachedPosts = loadCachedPosts()
        return if (cachedPosts.isNotEmpty()) {
            LoadResult.Page(
                data = cachedPosts,
                prevKey = null,
                nextKey = null
            )
        } else {
            LoadResult.Error(exception)
        }
    }

    /**
     * Fetches cached posts from the local database.
     *
     * Maps Post entities to domain objects for consistency in the UI layer.
     *
     * @return A list of cached posts in the domain model format.
     */
    private suspend fun loadCachedPosts(): List<Post> {
        return withContext(dispatcher) {
            postDao.getAllPosts().map(PostMapper::mapEntityToDomain)
        }
    }

    /**
     * Calculates the previous key for pagination.
     *
     * Ensures that the key is null if the offset is less than or equal to the initial offset.
     *
     * @param offset The current offset.
     * @param loadSize The number of items to load.
     * @return The previous key for pagination or null if at the start.
     */
    private fun calculatePrevKey(offset: Int, loadSize: Int): Int? {
        return if (offset <= Constants.INITIAL_OFFSET) null else offset - loadSize
    }

    /**
     * Calculates the next key for pagination.
     *
     * Ensures that the key is null if no more data is available.
     *
     * @param posts The list of posts retrieved in the current load.
     * @param offset The current offset.
     * @param loadSize The number of items to load.
     * @return The next key for pagination or null if no more data is available.
     */
    private fun calculateNextKey(posts: List<Post>, offset: Int, loadSize: Int): Int? {
        return if (posts.isEmpty()) null else offset + loadSize
    }

    /**
     * Provides the refresh key for Paging 3.
     *
     * Determines the most appropriate key to load when refreshing the data source.
     *
     * @param state The current PagingState containing information about the loaded pages.
     * @return The key for the page to reload or null if none is found.
     */
    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(Constants.PAGE_SIZE)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(Constants.PAGE_SIZE)
        }
    }
}
