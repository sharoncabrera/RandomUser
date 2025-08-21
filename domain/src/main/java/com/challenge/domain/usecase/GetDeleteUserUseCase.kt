package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import com.challenge.domain.util.DataError
import com.challenge.domain.util.Result


class GetDeleteUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit, DataError> {
        return repository.deleteUser(user)
    }
}