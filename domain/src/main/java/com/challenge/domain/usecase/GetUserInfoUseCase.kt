package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import com.challenge.domain.util.DataError
import com.challenge.domain.util.Result
import kotlinx.coroutines.flow.Flow


class GetUserInfoUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String): Flow<Result<User, DataError>> {
        return repository.getUserById(userId)
    }
}