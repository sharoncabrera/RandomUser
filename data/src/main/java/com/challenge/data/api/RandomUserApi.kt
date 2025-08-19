package com.challenge.data.api

import com.challenge.data.api.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApi {
    @GET("/")
    suspend fun getUsers(@Query("results") count: Int): UserResponse
}
