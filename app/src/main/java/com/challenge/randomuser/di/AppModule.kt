package com.challenge.randomuser.di

import com.challenge.data.api.RandomUserApi
import com.challenge.data.database.UserDao
import com.challenge.data.repository.UserRepositoryImpl
import com.challenge.domain.repository.UserRepository
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
}