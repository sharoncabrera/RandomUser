package com.challenge.domain.repository

import com.challenge.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(count: Int): Flow<List<User>>
    suspend fun deleteUser(user: User)
    suspend fun filterUsers(query: String): Flow<List<User>>
}