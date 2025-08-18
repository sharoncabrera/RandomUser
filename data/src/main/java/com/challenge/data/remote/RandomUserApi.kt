package com.challenge.data.remote

import com.challenge.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApi {
    @GET("/")
    suspend fun getUsers(@Query("results") count: Int): UserResponse
}
