package com.challenge.randomuser.di

import com.challenge.data.api.RandomUserApi
import com.challenge.data.database.UserDao
import com.challenge.data.repository.UserRepositoryImpl
import com.challenge.domain.repository.UserRepository
import com.challenge.domain.usecase.DeleteUserUseCase
import com.challenge.domain.usecase.FilterUsersUseCase
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
    fun provideDeleteUserUseCase(userRepository: UserRepository): DeleteUserUseCase {
        return DeleteUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideFilterUsersUseCase(userRepository: UserRepository): FilterUsersUseCase {
        return FilterUsersUseCase(userRepository)
    }
}