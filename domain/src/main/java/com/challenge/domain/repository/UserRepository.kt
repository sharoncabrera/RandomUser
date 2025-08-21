package com.challenge.domain.repository

import com.challenge.domain.model.User
import com.challenge.domain.util.DataError
import com.challenge.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(count: Int, initialLoad: Boolean): Flow<Result<List<User>, DataError>>
    suspend fun deleteUser(user: User): Result<Unit, DataError>
    suspend fun filterUsers(query: String): Flow<Result<List<User>, DataError>>
    suspend fun getUserById(userId: String): Flow<Result<User, DataError>>

}