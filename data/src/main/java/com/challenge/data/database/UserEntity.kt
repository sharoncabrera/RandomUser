package com.challenge.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val username: String,
    val gender: String?,
    val title: String?,
    val firstName: String?,
    val lastName: String?,
    val street: String?,
    val city: String?,
    val state: String?,
    val email: String?,
    val registeredDate: String?,
    val phone: String?,
    val pictureLarge: String?,
    val pictureMedium: String?,
    val pictureThumbnail: String?
)
