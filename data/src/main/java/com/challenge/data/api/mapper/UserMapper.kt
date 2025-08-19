package com.challenge.data.api.mapper

import com.challenge.data.api.model.UserDTO
import com.challenge.data.database.UserEntity
import com.challenge.domain.model.User

fun UserDTO.toUser(): User {
    return User(
        id= this.login?.uuid ?: "",
        gender = this.gender ?: "",
        name = this.name?.first?: "",
        lastName = this.name?.last ?: "",
        email = this.email ?: "",
        phone = this.phone ?: "",
        street = this.location?.street?.name ?: "",
        city = this.location?.city ?: "",
        state = this.location?.state ?: "",
        registeredDate = this.registered?.date ?: "",
        pictureThumbnail = this.picture?.thumbnail ?: "",
        pictureMedium = this.picture?.medium ?: "",
        pictureLarge = this.picture?.large ?: "",
    )
}


fun UserEntity.toUser(): User {
    return  User(
        id = this.id,
        gender = this.gender ?: "",
        name = this.name,
        lastName = this.lastName?: "",
        email = this.email ?: "",
        phone = this.phone ?: "",
        street = this.street ?: "",
        city = this.city ?: "",
        state = this.state ?: "",
        registeredDate = this.registeredDate ?: "",
        pictureThumbnail = this.pictureThumbnail?: "",
        pictureMedium = this.pictureMedium?: "",
        pictureLarge = this.pictureLarge?: "",
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        lastName = this.lastName,
        email = this.email,
        phone = this.phone,
        pictureLarge = this.pictureLarge,
        pictureMedium = this.pictureMedium,
        pictureThumbnail = this.pictureThumbnail,
        gender = this.gender,
        street = this.street,
        city = this.city,
        state = this.state,
        registeredDate = this.registeredDate
    )
}

