package com.challenge.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Login(
    @Json(name = "uuid") val uuid: String?,
    @Json(name = "username") val username: String?,
)
