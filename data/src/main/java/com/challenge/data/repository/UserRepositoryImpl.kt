package com.challenge.data.repository

import android.database.sqlite.SQLiteException
import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.challenge.data.api.RandomUserApi
import com.challenge.data.api.mapper.toUser
import com.challenge.data.api.mapper.toUserEntity
import com.challenge.data.database.UserDao
import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import com.challenge.domain.util.DataError
import com.challenge.domain.util.Result
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class UserRepositoryImpl @Inject constructor(
    private val api: RandomUserApi,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUsers(
        count: Int,
        initialLoad: Boolean
    ): Flow<Result<List<User>, DataError>> = flow {
        try {
            if (userDao.getUserCount() == 0 || !initialLoad) {
                val apiUsers = api.getUsers(count).results
                val entities = apiUsers.map { it.toUserEntity() }
                userDao.insertAll(entities)
            }

            emitAll(
                userDao.getAllUsers()
                    .map { list ->
                        Result.Success(list.map { it.toUser() }) as Result<List<User>, DataError>
                    }
            )
        } catch (e: IOException) {
            emit(Result.Error(DataError.Network.NO_INTERNET_CONNECTION))
        } catch (e: CancellationException) {
            throw e // coroutine cancellation
        } catch (e: Exception) {
            emit(Result.Error(DataError.Network.UNKNOWN))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteUser(user: User): Result<Unit, DataError> {
        return try {
            userDao.markUserAsDeleted(user.id)
            Result.Success(Unit)
        } catch (e: IOException) {
            Result.Error(DataError.Local.DATABASE_WRITE_ERROR)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun filterUsers(query: String): Flow<Result<List<User>, DataError>> = flow {
        try {
            val formattedQuery = "%$query%"
            emitAll(
                userDao.searchUsers(formattedQuery)
                    .map { list ->
                        Result.Success(list.map { it.toUser() }) as Result<List<User>, DataError>
                    }
            )
        } catch (e: IOException) {
            emit(Result.Error(DataError.Local.DATABASE_READ_ERROR))
        } catch (e: Exception) {
            emit(Result.Error(DataError.Local.UNKNOWN))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getUserById(userId: String): Flow<Result<User, DataError>> = flow {
        try {
            emitAll(
                userDao.getUserById(userId)
                    .map { entity ->
                        if (entity != null) {
                            Result.Success(entity.toUser()) as Result<User, DataError>
                        } else {
                            Result.Error(DataError.Local.USER_NOT_FOUND_IN_DB)
                        }
                    }
            )
        } catch (e: IOException) {
            emit(Result.Error(DataError.Local.DATABASE_READ_ERROR))
        } catch (e: Exception) {
            emit(Result.Error(DataError.Local.UNKNOWN))
        }
    }.flowOn(Dispatchers.IO)

}