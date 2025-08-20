package com.challenge.data.repository

import com.challenge.data.api.RandomUserApi
import com.challenge.data.api.mapper.toUser
import com.challenge.data.api.mapper.toUserEntity
import com.challenge.data.database.UserDao
import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: RandomUserApi,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUsers(count: Int): Flow<List<User>> {
        val apiUsers = api.getUsers(count).results
        val entities = apiUsers.map { it.toUserEntity() }
        userDao.insertAll(entities)

        return userDao.getAllUsers()
            .map { list -> list.map { it.toUser() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun deleteUser(user: User) {
        userDao.markUserAsDeleted(user.id)
    }


    override suspend fun filterUsers(query: String): Flow<List<User>> {
        val formattedQuery = "%$query%"
        return userDao.searchUsers(formattedQuery)
            .map { list -> list.map { it.toUser() } }
            .flowOn(Dispatchers.IO)
    }
}
