package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetLocalUsersUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Flow<List<User>> {
        return repository.getLocalUsers()
    }
}