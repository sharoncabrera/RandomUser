package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import com.challenge.domain.util.DataError
import com.challenge.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetUserInfoUseCaseTest {

    @Mock
    private lateinit var mockRepository: UserRepository

    private lateinit var getUserInfoUseCase: GetUserInfoUseCase

    @BeforeEach
    fun setUp() {
        getUserInfoUseCase = GetUserInfoUseCase(mockRepository)
    }

    @Test
    fun `the use case should return a user when the repository finds one`() = runTest {

        val expectedUser = User(
            "1", "test@test.com", "Test", "User",
            "", "", "", "", "", "",
            "", "", "", ""
        )

        whenever(mockRepository.getUserById(any())).thenReturn(flowOf(Result.Success(expectedUser)))

        val result = getUserInfoUseCase.invoke("1").first()

        assertTrue(result is Result.Success)
        assertEquals(expectedUser, (result as Result.Success).data)
    }

    @Test
    fun `the use case should fail when a user is not found in the DB`() = runTest {
        // Preparation
        val errorResult = Result.Error(DataError.Local.USER_NOT_FOUND_IN_DB)
        whenever(mockRepository.getUserById(any())).thenReturn(flowOf(errorResult))

        //Action
        val result = getUserInfoUseCase.invoke("1").first()

        assertTrue(result is Result.Error)
        assertEquals(DataError.Local.USER_NOT_FOUND_IN_DB, (result as Result.Error).error)
    }


    @Test
    fun `the use case should handle database read errors properly`() = runTest {
        val errorResult = Result.Error(DataError.Local.DATABASE_READ_ERROR)
        whenever(mockRepository.getUserById(any())).thenReturn(flowOf(errorResult))

        val result = getUserInfoUseCase.invoke("1").first()

        assertTrue(result is Result.Error)
        assertEquals(DataError.Local.DATABASE_READ_ERROR, (result as Result.Error).error)
    }


}