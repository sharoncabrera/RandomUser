package com.challenge.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDTO(
    @Json(name = "gender") val gender: String?,
    @Json(name = "name") val name: Name?,
    @Json(name = "location") val location: Location?,
    @Json(name = "email") val email: String?,
    @Json(name = "login") val login: Login?,
    @Json(name = "dob") val dob: Dob?,
    @Json(name = "registered") val registered: Registered?,
    @Json(name = "phone") val phone: String?,
    @Json(name = "cell") val cell: String?,
    @Json(name = "id") val id: Id?,
    @Json(name = "picture") val picture: Picture?,
    @Json(name = "nat") val nat: String?
)
