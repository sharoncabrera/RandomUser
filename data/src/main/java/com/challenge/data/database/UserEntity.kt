package com.challenge.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val gender: String?,
    val lastName: String?,
    val street: String?,
    val streetNumber: String?,
    val city: String?,
    val state: String?,
    val email: String?,
    val registeredDate: String?,
    val phone: String?,
    val pictureLarge: String?,
    val pictureMedium: String?,
    val pictureThumbnail: String?,
    val isDeleted: Boolean = false
)
