package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class GetFilterUsersUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(query: String, allUsers: List<User>): Flow<List<User>> {
        return if (query.isBlank()) {
            flowOf(allUsers)
        } else {
            repository.filterUsers(query)
        }
    }
}
