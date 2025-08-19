package com.challenge.data.repository

import com.challenge.data.api.RandomUserApi
import com.challenge.data.api.mapper.toUser
import com.challenge.data.database.UserDao
import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: RandomUserApi,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUsers(count: Int): Flow<List<User>> = flow {
        val users = api.getUsers(count).results.map { it.toUser() }
        emit(users)
    }.flowOn(Dispatchers.IO)
}
