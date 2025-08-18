package com.challenge.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "results") val results: List<User>,
    @Json(name = "info") val info: Info
)

@JsonClass(generateAdapter = true)
data class Info(
    @Json(name = "seed") val seed: String,
    @Json(name = "results") val results: Int,
    @Json(name = "page") val page: Int,
    @Json(name = "version") val version: String
)
