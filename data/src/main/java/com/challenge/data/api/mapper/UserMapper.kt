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
        streetNumber = this.location?.street?.number.toString(),
        city = this.location?.city ?: "",
        state = this.location?.state ?: "",
        registeredDate = this.registered?.date ?: "",
        pictureThumbnail = this.picture?.thumbnail ?: "",
        pictureMedium = this.picture?.medium ?: "",
        pictureLarge = this.picture?.large ?: "",
        isDeleted = false
    )
}

fun UserDTO.toUserEntity(): UserEntity {
    return UserEntity(
        id = this.login?.uuid.toString(),
        name = this.name?.first?: "",
        lastName = this.name?.last,
        email = this.email,
        phone = this.phone,
        pictureLarge = this.picture?.large,
        pictureMedium = this.picture?.medium,
        pictureThumbnail = this.picture?.thumbnail,
        gender = this.gender,
        street = "${this.location?.street?.name}",
        streetNumber = this.location?.street?.number.toString(),
        city = this.location?.city,
        state = this.location?.state,
        registeredDate = this.registered?.date,
        isDeleted = false
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
        streetNumber = this.streetNumber.toString(),
        city = this.city ?: "",
        state = this.state ?: "",
        registeredDate = this.registeredDate ?: "",
        pictureThumbnail = this.pictureThumbnail?: "",
        pictureMedium = this.pictureMedium?: "",
        pictureLarge = this.pictureLarge?: "",
        isDeleted = this.isDeleted
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
        streetNumber = this.streetNumber,
        city = this.city,
        state = this.state,
        registeredDate = this.registeredDate,
        isDeleted = this.isDeleted
    )
}

