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

    @Query("SELECT * FROM users WHERE id = :userId AND isDeleted = 0")
    fun getUserById(userId: String): Flow<UserEntity>

    @Query("SELECT * FROM users WHERE isDeleted = 0")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("UPDATE users SET isDeleted = 1 WHERE id = :userId")
    suspend fun markUserAsDeleted(userId: String)

    @Query("SELECT * FROM users WHERE name LIKE :query OR lastName LIKE :query OR email LIKE :query")
    fun searchUsers(query: String): Flow<List<UserEntity>>

}