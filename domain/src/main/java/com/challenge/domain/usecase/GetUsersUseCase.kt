package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow


class GetUsersUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(count: Int, initial: Boolean): Flow<List<User>> {
        return repository.getUsers(count, initial)
    }
}