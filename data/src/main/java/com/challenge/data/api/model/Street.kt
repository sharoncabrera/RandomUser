package com.challenge.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Street(
    @Json(name = "number") val number: Int?,
    @Json(name = "name") val name: String?
)
