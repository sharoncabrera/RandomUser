package com.challenge.domain.repository

import com.challenge.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getLocalUsers(): Flow<List<User>>
    suspend fun getUsers(count: Int): Flow<List<User>>
    suspend fun deleteUser(user: User)
    suspend fun filterUsers(query: String): Flow<List<User>>
    suspend fun getUserById(userId: String): Flow<User>
}