package com.fgfbrands.myapplication.data.repo

import com.fgfbrands.myapplication.data.remote.user.UserService
import com.fgfbrands.myapplication.domain.mapper.UserMapper
import com.fgfbrands.myapplication.domain.model.User
import com.fgfbrands.myapplication.domain.repo.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Repository for managing user data.
 *
 * Key Highlights:
 * - Fetches user details from the API and maps to the domain model.
 * - Handles fallback logic for error scenarios.
 */
class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val dispatcher: CoroutineDispatcher
) : UserRepository {

    override suspend fun getUserById(userId: Int): User? {
        return withContext(dispatcher) {
            try {
                val userResponse = userService.getUser(userId)
                UserMapper.mapUserResponseToDomain(userResponse)
            } catch (e: HttpException) {
                if (e.code() == 404) null else throw e
            }
        }
    }
}