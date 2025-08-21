package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import com.challenge.domain.util.DataError
import com.challenge.domain.util.Result
import kotlinx.coroutines.flow.Flow


class GetUsersUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(count: Int, initial: Boolean): Flow<Result<List<User>, DataError>> {
        return repository.getUsers(count, initial)
    }
}
