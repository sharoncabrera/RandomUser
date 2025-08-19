package com.challenge.data.repository

import com.challenge.data.api.RandomUserApi

import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: RandomUserApi
) : UserRepository {

}