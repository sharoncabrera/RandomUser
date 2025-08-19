package com.challenge.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "street") val street: Street?,
    @Json(name = "city") val city: String?,
    @Json(name = "state") val state: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "postcode") val postcode: String?,
    @Json(name = "coordinates") val coordinates: Coordinates?,
    @Json(name = "timezone") val timezone: Timezone?
)
