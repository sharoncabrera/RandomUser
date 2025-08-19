package com.challenge.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE firstName LIKE :query OR lastName LIKE :query OR email LIKE :query")
    fun searchUsers(query: String): Flow<List<UserEntity>>

    @Delete
    suspend fun deleteUser(user: UserEntity)
}