package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository


class GetDeleteUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        repository.deleteUser(user)
    }

}