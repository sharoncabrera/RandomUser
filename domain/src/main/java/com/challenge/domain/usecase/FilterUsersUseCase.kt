package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow


class FilterUsersUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(query: String): Flow<List<User>> {
        if (query.isBlank()) {
            //TODO: sharon
        }
        return repository.filterUsers(query)
    }
}
