package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow


class GetUserInfoUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String): Flow<User> {
        return repository.getUserById(userId)
    }
}