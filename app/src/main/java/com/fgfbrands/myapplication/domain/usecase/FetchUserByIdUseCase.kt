package com.fgfbrands.myapplication.domain.usecase


import com.fgfbrands.myapplication.domain.model.User
import com.fgfbrands.myapplication.domain.repo.UserRepository
import javax.inject.Inject

/**
 * Use case for fetching user details by ID.
 *
 * Key Highlights:
 * - Delegates fetching to the repository.
 */
class FetchUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Int): User? {
        return userRepository.getUserById(userId)
    }
}
