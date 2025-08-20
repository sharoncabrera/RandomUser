package com.challenge.randomuser.di

import com.challenge.data.api.RandomUserApi
import com.challenge.data.database.UserDao
import com.challenge.data.repository.UserRepositoryImpl
import com.challenge.domain.repository.UserRepository
import com.challenge.domain.usecase.GetDeleteUserUseCase
import com.challenge.domain.usecase.GetFilterUsersUseCase
import com.challenge.domain.usecase.GetLocalUsersUseCase
import com.challenge.domain.usecase.GetUserInfoUseCase
import com.challenge.domain.usecase.GetUsersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserRepository(
        randomUserApi: RandomUserApi,
        userDao: UserDao
    ): UserRepository {
        return UserRepositoryImpl(randomUserApi, userDao)
    }

    @Provides
    @Singleton
    fun provideGetUsersUseCase(
        userRepository: UserRepository
    ): GetUsersUseCase {
        return GetUsersUseCase(userRepository)
    }
    @Provides
    @Singleton
    fun provideDeleteUserUseCase(userRepository: UserRepository): GetDeleteUserUseCase {
        return GetDeleteUserUseCase(userRepository)
    }
    @Provides
    @Singleton
    fun provideFilterUsersUseCase(userRepository: UserRepository): GetFilterUsersUseCase {
        return GetFilterUsersUseCase(userRepository)
    }
    @Provides
    @Singleton
    fun provideGetLocalUsersUseCase(
        userRepository: UserRepository
    ): GetLocalUsersUseCase {
        return GetLocalUsersUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(
        userRepository: UserRepository
    ): GetUserInfoUseCase {
        return GetUserInfoUseCase(userRepository)
    }
}