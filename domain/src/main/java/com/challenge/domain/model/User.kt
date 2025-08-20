package com.challenge.domain.model

data class User(
    val id: String,
    val name: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val pictureLarge: String,
    val pictureMedium: String,
    val pictureThumbnail: String,
    val gender: String,
    val street: String,
    val streetNumber: String,
    val city: String,
    val state: String,
    val registeredDate: String,
    val isDeleted: Boolean = false
)